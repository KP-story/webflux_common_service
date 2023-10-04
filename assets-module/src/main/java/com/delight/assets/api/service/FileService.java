package com.delight.assets.api.service;

import com.delight.assets.api.model.UploadRS;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface FileService {
    Mono<Void> loadFile(String urn, ServerHttpResponse serverHttpResponse) throws Exception;
    Mono<Void> deleteFile(String url) ;
    Mono<Void> deleteFile(List<String> url) ;

    Mono<UploadRS> uploadFile(String config, String fileName, long contentLength, FilePart filePart);

    Mono<UploadRS> uploadFile(String config, String fileName, long contentLength, String contentType, Flux<DataBuffer> content);

    Mono<Map<String, String>> getUrlPrefixs(List<String> configs);

    Mono<String> getUrlPrefix(String config);

}
