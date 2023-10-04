package com.delight.assets.api.controller;

import com.delight.assets.api.model.UploadRS;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequestMapping("/assets")
@RestController
@AllArgsConstructor
public class AssetController {

    @DeleteMapping("/delete")
    public Mono<Void> deleteByUrl(@RequestBody String url) {
        return null;
    }
}
