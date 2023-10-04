package com.delight.notify.dao.entity;

import com.delight.gaia.jpa.entity.TraceableEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Table(value = "app")
public class AppEntity extends TraceableEntity {
    private String name;
    private String code;
    private String key;
    private String topic;
    private String loginTopic;
}
