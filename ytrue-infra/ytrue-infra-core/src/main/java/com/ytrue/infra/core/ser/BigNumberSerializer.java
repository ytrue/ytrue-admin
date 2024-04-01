package com.ytrue.infra.core.ser;

/**
 * @author ytrue
 * @date 2023-11-23 20:09
 * @description BigNumberSerializer
 */

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import java.io.IOException;

/**
 * 超出 JS 最大最小值 处理
 *
 * @author Lion Li
 */

/**
 * @author ytrue
 * @date 2023-11-23 11:58
 * @description 超出 JS 最大最小值 处理
 */
@JacksonStdImpl
public class BigNumberSerializer extends NumberSerializer {

    /**
     * 根据 JS Number.MAX_SAFE_INTEGER 与 Number.MIN_SAFE_INTEGER 得来
     */
    private static final long MAX_SAFE_INTEGER = 9007199254740991L;
    private static final long MIN_SAFE_INTEGER = -9007199254740991L;

    /**
     * 装饰器的设计模式
     */
    private NumberSerializer numberSerializer = null;


    /**
     * 提供实例
     */
    public static final BigNumberSerializer DEFAULT_INSTANCE = new BigNumberSerializer(Number.class);

    public BigNumberSerializer(Class<? extends Number> rawType) {
        super(rawType);
    }


    public BigNumberSerializer(Class<? extends Number> rawType, NumberSerializer enhanceNumberSerializer) {
        super(rawType);
        this.numberSerializer = enhanceNumberSerializer;
    }

    @Override
    public void serialize(Number value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        // 超出范围 序列化位字符串
        if (value.longValue() > MIN_SAFE_INTEGER && value.longValue() < MAX_SAFE_INTEGER) {

            // 不为空调取增强的
            if (numberSerializer != null) {
                numberSerializer.serialize(value, gen, provider);
                return;
            }
            // 调取父类的
            super.serialize(value, gen, provider);
        } else {
            gen.writeString(value.toString());
        }
    }
}
