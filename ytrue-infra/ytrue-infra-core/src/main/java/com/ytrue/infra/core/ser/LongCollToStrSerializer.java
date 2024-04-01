package com.ytrue.infra.core.ser;

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
 * @description LongCollToStrSerializer
 */
@Slf4j
public class LongCollToStrSerializer extends JsonSerializer<Collection<Long>> {

    @Override
    public void serialize(Collection<Long> values, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String[] newValues = ObjectUtil.defaultIfNull(values, Collections.emptyList())
                .stream()
                .map(String::valueOf)
                .toArray(String[]::new);
        gen.writeArray(newValues, 0, newValues.length);
    }
}
