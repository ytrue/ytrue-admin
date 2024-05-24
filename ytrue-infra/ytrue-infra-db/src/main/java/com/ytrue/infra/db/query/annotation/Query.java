package com.ytrue.infra.db.query.annotation;

import com.ytrue.infra.db.query.enums.Operator;
import com.ytrue.infra.db.query.enums.QueryMethod;

import java.lang.annotation.*;

/**
 * @author ytrue
 * @description: Query
 * @date 2022/12/21 16:22
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Queries.class)
public @interface Query {

    /**
     * 字段
     */
    String column() default "";


    /**
     * 条件
     *
     * @return
     */
    QueryMethod condition() default QueryMethod.eq;

    /**
     * 别名
     *
     * @return
     */
    String alias() default "";

    /**
     * and 还是 or
     *
     * @return
     */
    Operator operator() default Operator.and;
}
