package com.delight.notify.dao.entity;

import com.delight.gaia.jpa.converter.annotation.JsonType;
import com.delight.gaia.jpa.converter.annotation.TypeDef;
import com.delight.gaia.jpa.entity.BaseEntity;
import io.r2dbc.postgresql.codec.Json;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Table(value = "email_provider")
@TypeDef
public class EmailProviderEntity extends BaseEntity {
    private String name;
    private String code;
    private String providerType;
    private String defaultFrom;
    private String defaultFromName;
    @JsonType(Json.class)
    private ParameterEntity parameters;
}
