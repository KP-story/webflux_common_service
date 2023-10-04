package com.delight.assets.service.provider;

import co.elastic.thumbnails4j.core.Dimensions;
import co.elastic.thumbnails4j.core.Thumbnailer;
import co.elastic.thumbnails4j.image.ImageThumbnailer;
import com.delight.assets.api.model.FileInfo;
import com.delight.assets.dao.entity.UploadConfigEntity;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3AsyncClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component("AWS_S3")
@Slf4j
public class AwsS3Provider implements StorageProvider {
    private Cache<String, S3AsyncClient> awsClientProviders = Caffeine.newBuilder().scheduler(Scheduler.systemScheduler())
            .expireAfterWrite(10, TimeUnit.MINUTES).evictionListener((key, value, cause) -> {
                log.error("close s3 client");
                S3AsyncClient s3Client = (S3AsyncClient) value;
                s3Client.close();
            }).removalListener((key, value, cause) ->
            {
                log.error("close s3 client");
                S3AsyncClient s3Client = (S3AsyncClient) value;
                s3Client.close();
            })
            .maximumSize(100)
            .build();

    S3AsyncClient s3Client(String configCode, Map<String, String> parameters, String regionStr) {
        return awsClientProviders.get(configCode, configCode2 -> {
            Region region = Region.of(regionStr);
            S3Configuration serviceConfiguration = S3Configuration.builder()
                    .checksumValidationEnabled(false)
                    .chunkedEncodingEnabled(true)
                    .build();
            S3AsyncClientBuilder b = S3AsyncClient.builder()
                    .region(region)
                    .credentialsProvider(() -> AwsBasicCredentials.create(
                            parameters.get("ACCESS_KEY_ID"),
                            parameters.get("SECRET_ACCESS_KEY")))
                    .serviceConfiguration(serviceConfiguration);
            return b.build();


        });
    }


    @Override
    public Mono<String> save(Flux<DataBuffer> dataBuffers, FileInfo fileInfo, UploadConfigEntity uploadConfigEntity) throws Exception {
        String region = uploadConfigEntity.getParameters().get("REGION");
        S3AsyncClient s3Client = s3Client(uploadConfigEntity.getCode(), uploadConfigEntity.getParameters(), region);
        String key;
        if (uploadConfigEntity.getDuplicateNameProcess() == null || !uploadConfigEntity.getDuplicateNameProcess()) {
            key = System.currentTimeMillis() + ":" + fileInfo.getName();
        } else {
            key = fileInfo.getName();
        }
        String url = getPrefixLink(uploadConfigEntity);
        return DataBufferUtils.join(dataBuffers).flatMap(dataBuffer -> {
            AsyncRequestBody asyncRequestBody = AsyncRequestBody.fromByteBuffer(dataBuffer.toByteBuffer());
            PutObjectRequest objectRequest = PutObjectRequest.builder().acl(ObjectCannedACL.PUBLIC_READ)
                    .bucket(uploadConfigEntity.getBucket()).contentLength((long) dataBuffer.readableByteCount())
                    .key(key).contentType(fileInfo.getContentType())
                    .build();

            CompletableFuture<PutObjectResponse> responseCompletableFuture = s3Client.putObject(objectRequest, asyncRequestBody);
            return Mono.fromFuture(responseCompletableFuture).map(putObjectResponse -> url + URLEncoder.encode(key, Charset.defaultCharset())).doOnSuccess(s -> {
                if (uploadConfigEntity.getThumbnailHeight() == null || uploadConfigEntity.getThumbnailWidth() == null) {
                    return;
                }
                String[] types = fileInfo.getContentType().split("/");
                String thumbnailType = types[types.length - 1];
                Thumbnailer thumbnailer = new ImageThumbnailer(thumbnailType);
                List<Dimensions> outputDimensions = Collections.singletonList(new Dimensions(uploadConfigEntity.getThumbnailWidth(), uploadConfigEntity.getThumbnailHeight()));
                try {
                    BufferedImage output = thumbnailer.getThumbnails(dataBuffer.asInputStream(), outputDimensions).get(0);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    ImageIO.write(output, thumbnailType, out);
                    byte[] bytes = out.toByteArray();
                    StringBuilder sizeKey = new StringBuilder("/width=").append(uploadConfigEntity.getThumbnailWidth()).append(",height=").append(uploadConfigEntity.getThumbnailHeight());
                    PutObjectRequest thumbnail = PutObjectRequest.builder().acl(ObjectCannedACL.PUBLIC_READ)
                            .bucket(uploadConfigEntity.getBucket()).contentLength((long) bytes.length)
                            .key(key + sizeKey).contentType(fileInfo.getContentType())
                            .build();
                    AsyncRequestBody thumbnailRequestBody = AsyncRequestBody.fromBytes(bytes);
                    s3Client.putObject(thumbnail, thumbnailRequestBody);
                } catch (Exception e) {
                    log.error("gen thumbnail failed {}", e);
                }

            });
        });
    }

    @Override
    public List<String> getPrefixLinks(List<UploadConfigEntity> uploadConfigEntity) {
        return uploadConfigEntity.stream().map(this::getPrefixLink).collect(Collectors.toList());
    }

    @Override
    public String getPrefixLink(UploadConfigEntity uploadConfigEntity) {
        String region = uploadConfigEntity.getParameters().get("REGION");
        String url = new StringBuffer("https://s3.").append(region).append(".amazonaws.com/").append(uploadConfigEntity.getBucket()).append("/").toString();
        return url;

    }

    @Override
    public Mono<Void> delete(UploadConfigEntity uploadConfigEntity, String file) {
        String region = uploadConfigEntity.getParameters().get("REGION");
        S3AsyncClient s3Client = s3Client(uploadConfigEntity.getCode(), uploadConfigEntity.getParameters(), region);
        if(uploadConfigEntity.getThumbnailWidth()!=null)
        {

            StringBuilder sizeKey = new StringBuilder("/width=").append(uploadConfigEntity.getThumbnailWidth()).append(",height=").append(uploadConfigEntity.getThumbnailHeight());
            ObjectIdentifier   deleteObjectKey = ObjectIdentifier.builder()
                    .key( URLDecoder.decode(file,Charset.defaultCharset()))
                    .build();
            ObjectIdentifier deleteThumbnailObjectKey= ObjectIdentifier.builder()
                    .key(URLDecoder.decode(file + sizeKey,Charset.defaultCharset()))
                    .build();
            Delete del = Delete.builder()
                    .objects(List.of(deleteObjectKey,deleteThumbnailObjectKey))
                    .build();
            DeleteObjectsRequest deleteObjectsRequest= DeleteObjectsRequest.builder().bucket(uploadConfigEntity.getBucket()).delete(del).build();
            return Mono.fromFuture(s3Client.deleteObjects(deleteObjectsRequest)).then();

        }else
        {
            DeleteObjectRequest deleteObjectRequest= DeleteObjectRequest.builder().bucket(uploadConfigEntity.getBucket()).key(file).build();
            return Mono.fromFuture(s3Client.deleteObject(deleteObjectRequest)).then();
        }


    }

    @Override
    public Mono<Void> delete(UploadConfigEntity uploadConfigEntity, Collection<String> files) {
        String region = uploadConfigEntity.getParameters().get("REGION");
        S3AsyncClient s3Client = s3Client(uploadConfigEntity.getCode(), uploadConfigEntity.getParameters(), region);
        List<ObjectIdentifier> identifiers;
        if(uploadConfigEntity.getThumbnailWidth()!=null)
        {
            identifiers = files.stream().flatMap(file -> {
                StringBuilder sizeKey = new StringBuilder("/width=").append(uploadConfigEntity.getThumbnailWidth()).append(",height=").append(uploadConfigEntity.getThumbnailHeight());
                ObjectIdentifier deleteObjectKey = ObjectIdentifier.builder()
                        .key(URLDecoder.decode(file,Charset.defaultCharset()))
                        .build();
                ObjectIdentifier deleteThumbnailObjectKey = ObjectIdentifier.builder()
                        .key(URLDecoder.decode(file + sizeKey,Charset.defaultCharset()))
                        .build();
                return Stream.of(deleteObjectKey, deleteThumbnailObjectKey);

            }).collect(Collectors.toList());


        }else
        {
            identifiers = files.stream().map(file -> {
                ObjectIdentifier deleteObjectKey = ObjectIdentifier.builder()
                        .key(URLDecoder.decode(file,Charset.defaultCharset()))
                        .build();
                return deleteObjectKey;

            }).collect(Collectors.toList());
        }

        Delete del = Delete.builder()
                .objects(identifiers)
                .build();
        DeleteObjectsRequest deleteObjectsRequest= DeleteObjectsRequest.builder().bucket(uploadConfigEntity.getBucket()).delete(del).build();
        return Mono.fromFuture(s3Client.deleteObjects(deleteObjectsRequest)).then();
    }


    @Override
    public ReadResult read(String uri) throws Exception {
        return null;
    }
}
