package com.delight.assets.dao.entity;

import com.delight.gaia.jpa.converter.annotation.JsonType;
import com.delight.gaia.jpa.converter.annotation.TypeDef;
import com.delight.gaia.jpa.entity.TraceableEntity;
import io.r2dbc.postgresql.codec.Json;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;

@Getter
@Setter
@Table("upload_config")
@TypeDef
public class UploadConfigEntity extends TraceableEntity {
    private String code;
    private Long maxSize;
    private Long minSize;
    private Set<String> acceptContentTypes;
    private String bucket;
    private String name;
    private String vendor;
    @JsonType(Json.class)
    private ParametersEntity parameters;
    private Integer thumbnailHeight;
    private Integer thumbnailWidth;
    private Boolean duplicateNameProcess;

}
