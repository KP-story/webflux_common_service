package com.delight.assets.service.impl;

import com.delight.assets.api.model.FileInfo;
import com.delight.assets.api.model.UploadRS;
import com.delight.assets.api.service.FileService;
import com.delight.assets.dao.entity.UploadConfigEntity;
import com.delight.assets.dao.repo.UploadConfigRepo;
import com.delight.assets.service.provider.StorageProvider;
import com.delight.gaia.base.exception.CommandFailureException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import reactor.core.CorePublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.delight.assets.constant.AssetsErrorCode.*;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final UploadConfigRepo uploadConfigRepo;
    private final Map<String, StorageProvider> storageProviders;

    @Override
    public Mono<Void> loadFile(String urn, ServerHttpResponse response) throws Exception {
        urn = urn.replace("/assets", "");
        var rs = storageProviders.get("LOCAL_DISK").read(urn);
        response.getHeaders().setContentType(MediaType.valueOf(rs.getContentType()));
        return response.writeWith(rs.getDataBuffer());
    }

    @Override
    public Mono<Void> deleteFile(String url)  {
        if (url.contains("amazonaws.com")) {
            int firstIndex = url.indexOf("amazonaws.com/") + "amazonaws.com/".length();
            int secondIndex = url.indexOf("/", firstIndex);
            String bucket = url.substring(firstIndex, secondIndex);
            String[] split = url.split("/");
            String filename = split[split.length - 1];
            return uploadConfigRepo.findActiveConfigByBucket(bucket).flatMap(uploadConfigEntity -> {
                StorageProvider storageProvider = storageProviders.get(uploadConfigEntity.getVendor());
                if (storageProvider == null) {
                    return Mono.empty();
                } else {
                    return storageProvider.delete(uploadConfigEntity, filename);
                }
            }).collectList().then();
        }
        return Mono.just(true).then();
    }

    @Override
    public Mono<Void> deleteFile(List<String> urls)  {
        Map<String, Set<String>> buckets = new HashMap<>();
        for (String url : urls) {
            if (url.contains("amazonaws.com")) {
                int firstIndex = url.indexOf("amazonaws.com/") + "amazonaws.com/".length();
                int secondIndex = url.indexOf("/", firstIndex);
                String bucket = url.substring(firstIndex, secondIndex);
                String[] split = url.split("/");
                String filename = split[split.length - 1];
                Set<String> ids = buckets.get(bucket);
                if (ids == null) {
                    ids = new HashSet<>();
                    buckets.put(bucket, ids);
                }
                ids.add(filename);
            }
        }
        List<Mono<Void>> corePublishers = buckets.keySet().stream().map(bucket -> {
            Set<String> ids = buckets.get(bucket);
            if (!ids.isEmpty()) {
                Flux<UploadConfigEntity> activeConfigByBucket = uploadConfigRepo.findActiveConfigByBucket(bucket);
                if (ids.size() == 1) {

                    return activeConfigByBucket.flatMap(uploadConfigEntity -> {
                        StorageProvider storageProvider = storageProviders.get(uploadConfigEntity.getVendor());
                        if (storageProvider == null) {
                            return Mono.just(true).then();
                        } else {
                            return storageProvider.delete(uploadConfigEntity, ids.iterator().next());
                        }
                    }).collectList().then();
                } else {
                    return activeConfigByBucket.flatMap(uploadConfigEntity -> {
                        StorageProvider storageProvider = storageProviders.get(uploadConfigEntity.getVendor());
                        if (storageProvider == null) {
                            return  Mono.just(true).then();
                        } else {
                            return storageProvider.delete(uploadConfigEntity, ids);
                        }
                    }).collectList().then();
                }

            }
            return  Mono.just(true).then();

        }).collect(Collectors.toList());
        return  Mono.zip(corePublishers, (t)->t).then();

    }



    @Override
    public Mono<UploadRS> uploadFile(String config, String fileName, long contentLength, FilePart filePart) {
        if (fileName == null) {
            fileName = filePart.filename();
        }
        return uploadFile(config, fileName, contentLength, filePart.headers().getContentType().toString(), filePart.content());

    }

    @Override
    public Mono<UploadRS> uploadFile(String config, String fileName, long contentLength, String contentType, Flux<DataBuffer> content) {
        return uploadConfigRepo.findActiveConfig(config).switchIfEmpty(Mono.defer(()
                -> Mono.error(new CommandFailureException(CONFIG_NOT_FOUND)))).flatMap(uploadConfigEntity -> {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setName(fileName);
            fileInfo.setContentType(contentType);
            fileInfo.setSize(contentLength);
            if (contentLength > uploadConfigEntity.getMaxSize() || contentLength < uploadConfigEntity.getMinSize()) {
                return Mono.error(new CommandFailureException(FILE_SIZE_INVALID, uploadConfigEntity.getMaxSize() + "", uploadConfigEntity.getMinSize() + ""));
            }

            if (uploadConfigEntity.getAcceptContentTypes() != null && !uploadConfigEntity.getAcceptContentTypes().isEmpty() && !uploadConfigEntity.getAcceptContentTypes().contains(contentType)) {
                return Mono.error(new CommandFailureException(CONTENT_TYPE_INVALID));
            }
            StorageProvider storageProvider = storageProviders.get(uploadConfigEntity.getVendor());
            if (storageProvider == null) {
                return Mono.error(new CommandFailureException(PROVIDER_NOT_FOUND));
            }
            try {
                return storageProvider.save(content, fileInfo, uploadConfigEntity)
                        .map(s -> new UploadRS().setFileInfo(fileInfo).setUri(s));
            } catch (Exception e) {
                return Mono.error(e);
            }
        });
    }

    @Override
    public Mono<Map<String, String>> getUrlPrefixs(List<String> configs) {
        return uploadConfigRepo.findActiveConfigs(configs)
                .collectMap(uploadConfigEntity -> uploadConfigEntity.getCode(), uploadConfigEntity -> storageProviders.get(uploadConfigEntity.getVendor()).getPrefixLink(uploadConfigEntity));
    }

    @Override
    public Mono<String> getUrlPrefix(String config) {
        return uploadConfigRepo.findActiveConfig(config).map(uploadConfigEntity -> storageProviders.get(uploadConfigEntity.getVendor()).getPrefixLink(uploadConfigEntity));
    }
}
