package com.ytrue.infra.mysql.query.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description 自定义排序注解组，用于包含多个 OrderBy 注解
 */
@Target(ElementType.FIELD) // 注解的目标是字段
@Retention(RetentionPolicy.RUNTIME) // 注解在运行时可用
public @interface OrderByGroup {

    /**
     * 多个 OrderBy 注解
     *
     * @return 包含多个 OrderBy 注解的数组
     */
    OrderBy[] value();
}
