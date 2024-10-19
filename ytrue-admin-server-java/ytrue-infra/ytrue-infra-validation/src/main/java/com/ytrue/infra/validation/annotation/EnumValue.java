package com.ytrue.infra.validation.annotation;

import com.ytrue.infra.validation.validator.EnumValueValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义枚举值验证注解。
 * 用于验证字段的值是否为指定枚举类型的合法值。
 *
 * <p>
 * 该注解可用于字段、方法参数或其他注解中，支持运行时校验。
 * 校验器为 {@link EnumValueValidator}，该校验器将根据指定的枚举类进行值的验证。
 * </p>
 *
 * @author ytrue
 * @date 2024/10/19
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME) // 注解在运行时可用
@Constraint(validatedBy = EnumValueValidator.class) // 指定校验器
public @interface EnumValue {

    /**
     * 验证不通过时的默认错误消息。
     *
     * @return 默认错误消息
     */
    String message() default "无效的枚举值";

    /**
     * 分组信息，默认不使用。
     *
     * @return 分组信息
     */
    Class<?>[] groups() default {};

    /**
     * 负载信息，默认不使用。
     *
     * @return 负载信息
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 枚举类的类型，用于指定合法的枚举值。
     *
     * @return 指定的枚举类
     */
    Class<? extends Enum<?>> enumClass();

    /**
     * 指定值是否需要忽略大小写。
     *
     * @return true 如果忽略大小写，false 否则
     */
    boolean ignoreCase() default false;
}
