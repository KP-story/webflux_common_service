package com.delight.auth.dao.entity;

import com.delight.gaia.jpa.entity.TraceableEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@Table(value = "account")
public class UserEntity extends TraceableEntity {
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private LocalDate birthday;
    private boolean hasAvatar;
    private Short gender;
    private String lang;
}
