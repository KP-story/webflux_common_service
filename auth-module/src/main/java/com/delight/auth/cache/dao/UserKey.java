package com.delight.auth.cache.dao;

import com.delight.gaia.base.constant.AccountType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@EqualsAndHashCode
@Accessors(chain = true)
public class UserKey {
    private Long userId;
    private AccountType accountType;
}
