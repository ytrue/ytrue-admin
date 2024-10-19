package com.ytrue.infra.validation.annotation;

import com.ytrue.infra.validation.validator.IPValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义IP地址验证注解。
 *
 * <p>
 * 该注解用于验证字段、方法参数或其他注解中的值是否为有效的IP地址。
 * 使用此注解的字段会通过 {@link IPValidator} 验证器进行验证。
 * </p>
 *
 * @author ytrue
 * @date 2024/10/19
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE}) // 注解的适用范围
@Retention(RetentionPolicy.RUNTIME) // 注解在运行时可用
@Constraint(validatedBy = IPValidator.class) // 指定校验器
public @interface IP {

    /**
     * 验证不通过时的默认错误消息。
     *
     * @return 默认错误消息
     */
    String message() default "无效的IP地址";

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
}
