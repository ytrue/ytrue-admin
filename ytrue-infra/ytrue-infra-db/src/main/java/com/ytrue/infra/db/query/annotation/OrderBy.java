package com.ytrue.infra.db.query.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(OrderByGroup.class)
public @interface OrderBy {

    /**
     * 字段
     */
    String column() default "";

    /**
     * 别名
     *
     * @return
     */
    String alias() default "";

    /**
     * 是否asc
     *
     * @return
     */
    boolean asc() default false;

    /**
     * 顺序
     *
     * @return
     */
    int index() default 0;
}
