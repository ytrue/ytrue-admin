package com.ytrue.infra.db.query.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WhereGroup {

    /**
     * 多个query
     *
     * @return
     */
    Where[] value();
}
