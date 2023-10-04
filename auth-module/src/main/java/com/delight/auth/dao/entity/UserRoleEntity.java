package com.delight.auth.dao.entity;

import com.delight.gaia.jpa.entity.BaseEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@Table(value = "user_role")
public class UserRoleEntity extends BaseEntity {
    private Long userId;
    private Long roleId;
}
