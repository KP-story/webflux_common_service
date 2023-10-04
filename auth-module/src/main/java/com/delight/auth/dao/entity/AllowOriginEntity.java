package com.delight.auth.dao.entity;

import com.delight.gaia.jpa.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table(value = "allow_origin")
public class AllowOriginEntity extends BaseEntity {
    private String origin;
}
