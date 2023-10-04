package com.delight.notify.service;

import java.time.LocalDateTime;

public interface IdGenerator {
    Long nextId();


    Long genIdByTime(LocalDateTime var1);

    Long genIdByTime(long var1);

    void updateInstanceId(long var1);
}
