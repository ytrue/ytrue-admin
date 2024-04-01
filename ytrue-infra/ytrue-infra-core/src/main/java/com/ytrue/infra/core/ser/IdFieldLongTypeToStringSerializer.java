package com.ytrue.infra.core.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import java.io.IOException;

/**
 * @author ytrue
 * @date 2023-11-22 9:22
 * @description id 或者 xxxId结尾的 字段 Long 类型字段序列化时转为字符串，避免js丢失精度
 */
@JacksonStdImpl
public class IdFieldLongTypeToStringSerializer extends NumberSerializer {

    private final static String ID_KEY = "Id";

    private final static String PID_KEY = "pid";



    /**
     * 装饰器的设计模式
     */
    private NumberSerializer numberSerializer = null;

    /**
     * 提供实例
     */
    public static final IdFieldLongTypeToStringSerializer DEFAULT_INSTANCE = new IdFieldLongTypeToStringSerializer(Number.class);

    public static final IdFieldLongTypeToStringSerializer BIG_NUMBER_ENHANCE_INSTANCE = new IdFieldLongTypeToStringSerializer(Number.class, BigNumberSerializer.DEFAULT_INSTANCE);

    public IdFieldLongTypeToStringSerializer(Class<? extends Number> rawType) {
        super(rawType);
    }

    public IdFieldLongTypeToStringSerializer(Class<? extends Number> rawType, NumberSerializer enhanceNumberSerializer) {
        super(rawType);
        this.numberSerializer = enhanceNumberSerializer;
    }

    @Override
    public void serialize(Number value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
        String keyName = jsonGenerator.getOutputContext().getCurrentName();
        // 空就不处理了
        if (keyName != null) {
            String lowerCaseKeyName = ID_KEY.toLowerCase();
            if (keyName.endsWith(ID_KEY) || lowerCaseKeyName.equals(keyName) || keyName.equals(PID_KEY)) {
                String text = value == null ? null : String.valueOf(value);
                if (text != null) {
                    jsonGenerator.writeString(text);
                    return;
                }
            }
        }

        // 不为空调取增强的
        if (numberSerializer != null) {
            numberSerializer.serialize(value, jsonGenerator, provider);
            return;
        }
        // 调取父类的
        super.serialize(value, jsonGenerator, provider);
    }
}
