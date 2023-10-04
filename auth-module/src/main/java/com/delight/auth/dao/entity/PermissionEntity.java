package com.delight.auth.dao.entity;

import com.delight.gaia.jpa.entity.TraceableEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@Table(value = "se_permission")
public class PermissionEntity extends TraceableEntity {
    private String code;
}
