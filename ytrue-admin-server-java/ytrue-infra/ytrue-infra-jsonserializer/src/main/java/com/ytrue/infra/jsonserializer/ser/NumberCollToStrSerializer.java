package com.ytrue.infra.jsonserializer.ser;

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
 * @description NumberCollToStrSerializer 用于将数字集合序列化为字符串数组。
 */
@Slf4j
public class NumberCollToStrSerializer extends JsonSerializer<Collection<? extends Number>> {

    /**
     * 序列化数字集合为字符串数组。
     *
     * @param value      要序列化的数字集合
     * @param gen        JSON 生成器
     * @param serializers 序列化提供者
     * @throws IOException 如果序列化过程中出现 I/O 错误
     */
    @Override
    public void serialize(Collection<? extends Number> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String[] newValues = ObjectUtil.defaultIfNull(value, Collections.emptyList())
                .stream()
                .map(String::valueOf)
                .toArray(String[]::new);
        gen.writeArray(newValues, 0, newValues.length);
    }
}
