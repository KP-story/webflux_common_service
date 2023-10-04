package com.delight.assets.api.controller;

import com.delight.assets.api.model.UploadRS;
import com.delight.assets.api.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RequestMapping("/assets")
@RestController
@AllArgsConstructor
public class UploadController {
    private FileService fileService;

    @PostMapping("/upload")
    public Mono<UploadRS> upload(@RequestPart("file") FilePart filePart, @RequestHeader(name = HttpHeaders.CONTENT_LENGTH) Long contentLength, @RequestPart String config, @RequestPart(required = false) String fileName) {
        return fileService.uploadFile(config, fileName, contentLength, filePart);
    }

    @GetMapping("/url-prefixs")
    public Mono<Map<String, String>> getPrefixUrls(@RequestParam(required = true) List<String> configs) {
        return fileService.getUrlPrefixs(configs);
    }


    @GetMapping("/url-prefix")
    public Mono<String> getPrefixUrl(@RequestParam(required = true) String config) {
        return fileService.getUrlPrefix(config);
    }


    @GetMapping("/**")
    public Mono<Void> loadFile(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) throws Exception {
        String uri = serverHttpRequest.getURI().getPath();
        return fileService.loadFile(uri, serverHttpResponse);
    }
}
