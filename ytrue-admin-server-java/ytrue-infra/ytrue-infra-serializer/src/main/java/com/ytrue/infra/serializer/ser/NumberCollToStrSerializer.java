package com.ytrue.infra.serializer.ser;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

/**
 * @author ytrue
 * @date 2023-11-23 11:58
 * @description NumberCollToStrSerializer
 */
@Slf4j
public class NumberCollToStrSerializer extends JsonSerializer<Collection<? extends Number>> {

    @Override
    public void serialize(Collection<? extends Number> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String[] newValues = ObjectUtil.defaultIfNull(value, Collections.emptyList())
                .stream()
                .map(String::valueOf)
                .toArray(String[]::new);
        gen.writeArray(newValues, 0, newValues.length);
    }
}
