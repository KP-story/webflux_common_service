package com.delight.news;

import com.delight.gaia.jpa.converter.annotation.JsonRawConverter;
import io.r2dbc.postgresql.codec.Json;
import org.springframework.stereotype.Component;

@Component
public class JsonRawTypeConverter implements JsonRawConverter<Json> {
    @Override
    public byte[] toByte(io.r2dbc.postgresql.codec.Json toByte) {
        return toByte.asArray();
    }

    @Override
    public io.r2dbc.postgresql.codec.Json fromByte(byte[] bytes) {
        return Json.of(bytes);
    }
}
