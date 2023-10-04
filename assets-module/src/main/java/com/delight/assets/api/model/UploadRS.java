package com.delight.assets.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UploadRS {
    private String uri;
    private FileInfo fileInfo;
}
