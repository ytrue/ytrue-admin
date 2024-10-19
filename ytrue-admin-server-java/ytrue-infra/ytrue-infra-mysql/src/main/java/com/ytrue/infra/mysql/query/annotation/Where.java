package com.ytrue.infra.mysql.query.annotation;



import com.ytrue.infra.mysql.query.enums.Operator;
import com.ytrue.infra.mysql.query.enums.QueryMethod;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(WhereGroup.class)
public @interface Where {

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
