package com.ytrue.infra.mysql.query.annotation;

import java.lang.annotation.*;

/**
 * @description 自定义排序注解
 */
@Target(ElementType.FIELD) // 注解的目标是字段
@Retention(RetentionPolicy.RUNTIME) // 注解在运行时可用
@Repeatable(OrderByGroup.class) // 支持重复使用该注解
public @interface OrderBy {

    /**
     * 字段名称
     *
     * @return 字段名称
     */
    String column() default "";

    /**
     * 别名
     *
     * @return 字段别名
     */
    String alias() default "";

    /**
     * 是否升序
     *
     * @return true 表示升序，false 表示降序
     */
    boolean asc() default false;

    /**
     * 顺序索引
     *
     * @return 排序顺序的索引值，值越小表示优先级越高
     */
    int index() default 0;
}
