package com.delight.assets.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileInfo {
    private String name;
    private String contentType;
    private Long size;
}
