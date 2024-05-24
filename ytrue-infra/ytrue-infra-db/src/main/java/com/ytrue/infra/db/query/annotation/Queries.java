package com.ytrue.infra.db.query.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ytrue
 * @date 2024-01-08 15:45
 * @description Queries
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Queries {

    /**
     * 多个query
     *
     * @return
     */
    Query[] value();
}
