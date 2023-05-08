package com.ytrue.tools.security.annotation;

import java.lang.annotation.*;


/**
 * @author ytrue
 * @date 2023/5/8 10:29
 * @description 忽略认证
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface IgnoreWebSecurity {
}
