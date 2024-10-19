package com.ytrue.infra.jsonserializer.ser;

/**
 * @author ytrue
 * @date 2023-11-23 20:09
 * @description BigNumberOrSpecifyFieldToStrTypeAdapter
 */

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import com.ytrue.infra.jsonserializer.enums.SafeIntegerEnum;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 超出 JS 最大最小值 处理
 *
 * @author Lion Li
 */

/**
 * @author ytrue
 * @date 2023-11-23 11:58
 * @description 超出 JS 最大最小值 处理, 或者指定字段
 */
@Setter
@JacksonStdImpl
public class BigNumberOrSpecifyFieldToStrSerializer extends NumberSerializer {


    /**
     * 对于没有匹配的，但是超过最大值的，如果 == true 就是处理，反之
     */
    private boolean enableBigNumToStr = true;

    /**
     * 前缀匹配
     */
    private List<String> prefixMatchList = new ArrayList<>();
    /**
     * 后缀匹配
     */
    private List<String> suffixMatchList = new ArrayList<>();
    /**
     * 完全匹配
     */
    private List<String> perfectMatchList = new ArrayList<>();


    /**
     * 构造器，接受数字的原始类型。
     *
     * @param rawType 数字的原始类型
     */
    public BigNumberOrSpecifyFieldToStrSerializer(Class<? extends Number> rawType) {
        super(rawType);
    }

    /**
     * 构造器，接受数字的原始类型及匹配列表。
     *
     * @param rawType         数字的原始类型
     * @param prefixMatchList 前缀匹配列表
     * @param suffixMatchList 后缀匹配列表
     * @param perfectMatchList 完全匹配列表
     */
    public BigNumberOrSpecifyFieldToStrSerializer(
            Class<? extends Number> rawType,
            List<String> prefixMatchList,
            List<String> suffixMatchList,
            List<String> perfectMatchList
    ) {
        super(rawType);
        this.prefixMatchList = prefixMatchList;
        this.suffixMatchList = suffixMatchList;
        this.perfectMatchList = perfectMatchList;
    }

    /**
     * 序列化数字值。
     *
     * @param value         要序列化的数字值
     * @param jsonGenerator JSON 生成器
     * @param provider      序列化提供者
     * @throws IOException 如果序列化过程中出现 I/O 错误
     */
    @Override
    public void serialize(Number value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
        // 为null 直接交给父类处理
        if (Objects.isNull(value)) {
            // 调取父类的
            super.serialize(null, jsonGenerator, provider);
            return;
        }

        // 获取字段
        String currentName = jsonGenerator.getOutputContext().getCurrentName();
        // 获取父级的,如果当前的是空，就去获取上级的，判断字段是否匹配, { "ids":[1,3] } 这种也匹配
        String parentName = jsonGenerator.getOutputContext().getParent().getCurrentName();

        // 超出范围 序列化位字符串,这里不处理数组的情况  { "ids":[1,3] } 不处理
        if (StrUtil.isNotEmpty(currentName)) {
            // 超过范围的,并且enableBigNumToStr开启
            if (safeIntegerAndJsonGeneratorWriteString(value, jsonGenerator) && enableBigNumToStr) {
                return;
            }
            // 匹配，但是没有超过的
            if (matchingAndJsonGeneratorWriteString(currentName, value, jsonGenerator)) {
                return;
            }
        }

        // 为空说明是数组，只处理匹配的数组，没有匹配的 手动使用 @JsonSerialize(using = LongCollToStrSerializer.class)处理
        if (StrUtil.isEmpty(currentName)) {
            // 父级是否匹配,并且可以转换
            if (matchingAndJsonGeneratorWriteString(parentName, value, jsonGenerator)) {
                return;
            }
        }

        // 直接调用父类的
        super.serialize(value, jsonGenerator, provider);
    }


    /**
     * 检查数字是否安全，并将其序列化为字符串。
     *
     * @param value         要检查的数字
     * @param jsonGenerator JSON 生成器
     * @return 如果处理成功，返回 true；否则返回 false。
     * @throws IOException 如果序列化过程中出现 I/O 错误
     */
    private boolean safeIntegerAndJsonGeneratorWriteString(Number value, JsonGenerator jsonGenerator) throws IOException {
        if (value.longValue() <= SafeIntegerEnum.MIN_SAFE_INTEGER.getNumber() || value.longValue() >= SafeIntegerEnum.MAX_SAFE_INTEGER.getNumber()) {
            return this.jsonGeneratorWriteString(value, jsonGenerator);
        }
        return false;
    }

    /**
     * 匹配字段并将其序列化为字符串。
     *
     * @param keyName       当前字段名称
     * @param value         要序列化的数字值
     * @param jsonGenerator JSON 生成器
     * @return 如果处理成功，返回 true；否则返回 false。
     * @throws IOException 如果序列化过程中出现 I/O 错误
     */
    private boolean matchingAndJsonGeneratorWriteString(String keyName, Number value, JsonGenerator jsonGenerator) throws IOException {
        return isMatching(keyName) && this.jsonGeneratorWriteString(value, jsonGenerator);
    }

    /**
     * 将数字序列化为字符串。
     *
     * @param value         要序列化的数字值
     * @param jsonGenerator JSON 生成器
     * @return 如果处理成功，返回 true；否则返回 false。
     * @throws IOException 如果序列化过程中出现 I/O 错误
     */
    private boolean jsonGeneratorWriteString(Number value, JsonGenerator jsonGenerator) throws IOException {
        String text = String.valueOf(value);
        if (text != null) {
            jsonGenerator.writeString(text);
            return true;
        }
        return false;
    }

    /**
     * 判断输入字符串是否与匹配列表中的任一项匹配。
     *
     * @param input 输入字符串
     * @return 如果匹配，返回 true；否则返回 false。
     */
    private boolean isMatching(String input) {
        if (StrUtil.isEmpty(input)) {
            return false;
        }

        // 前缀匹配
        boolean isPrefixMatch = Optional.ofNullable(prefixMatchList)
                .map(list -> list.stream().anyMatch(prefix -> prefix != null && input.startsWith(prefix)))
                .orElse(false);

        // 后缀匹配
        boolean isSuffixMatch = Optional.ofNullable(suffixMatchList)
                .map(list -> list.stream().anyMatch(suffix -> suffix != null && input.endsWith(suffix)))
                .orElse(false);

        // 完全匹配
        boolean isPerfectMatch = Optional.ofNullable(perfectMatchList)
                .map(list -> list.contains(input))
                .orElse(false);

        return isPrefixMatch || isSuffixMatch || isPerfectMatch;
    }
}
