package com.delight.auth.dao.entity;

import com.delight.auth.constant.ProviderType;
import com.delight.gaia.jpa.entity.TraceableEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Table(value = "linked_account")
public class LinkedAccountEntity extends TraceableEntity {
    private String providerId;
    private ProviderType providerType;
    private Long userId;

}
