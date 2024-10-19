package com.ytrue.infra.validation.validator;

import com.ytrue.infra.validation.annotation.EnumValue;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 自定义枚举值验证器。
 * 实现枚举值的验证逻辑。
 *
 * <p>
 * 该验证器用于校验字符串值是否属于指定的枚举类型的合法值。
 * 如果值为null或为空，则根据具体需求决定是否允许。
 * </p>
 *
 * @author ytrue
 * @date 2024/10/19
 */
public class EnumValueValidator implements ConstraintValidator<EnumValue, String> {

    private Enum<?>[] enumValues; // 枚举值数组
    private boolean ignoreCase; // 是否忽略大小写

    /**
     * 初始化方法，在注解被应用时调用。
     *
     * @param constraintAnnotation 枚举值注解的实例
     */
    @Override
    public void initialize(EnumValue constraintAnnotation) {
        // 初始化枚举类型和大小写选项
        this.enumValues = constraintAnnotation.enumClass().getEnumConstants();
        this.ignoreCase = constraintAnnotation.ignoreCase();
    }

    /**
     * 校验方法，用于验证输入值是否有效。
     *
     * @param value 输入的值
     * @param context 校验上下文
     * @return 如果输入值有效，返回true；否则返回false
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // 允许空值，具体根据需求决定
        }

        // 验证值是否在枚举值中
        for (Enum<?> enumValue : enumValues) {
            // 根据忽略大小写的设置进行匹配
            if (ignoreCase ? enumValue.name().equalsIgnoreCase(value) : enumValue.name().equals(value)) {
                return true; // 找到匹配的枚举值
            }
        }
        return false; // 没有匹配
    }
}
