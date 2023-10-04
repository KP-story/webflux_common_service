package com.delight.auth.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Picture {
    @JsonProperty("data.height")
    private Integer height;
    @JsonProperty("data.width")
    private Integer width;
    @JsonProperty("data.url")
    private String url;
}