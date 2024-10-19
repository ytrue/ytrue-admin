package com.ytrue.infra.jsonserializer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ytrue
 * @date 2023-06-02 14:59
 * @description SafeIntegerEnum 枚举类用于定义安全整数范围
 */
@AllArgsConstructor
@Getter
public enum SafeIntegerEnum {

    /**
     * 根据 JS Number.MAX_SAFE_INTEGER 与 Number.MIN_SAFE_INTEGER 得来
     * 最大安全整数值
     */
    MAX_SAFE_INTEGER(9007199254740991L),

    /**
     * 最小安全整数值
     */
    MIN_SAFE_INTEGER(-9007199254740991L);

    /**
     * 安全整数值
     */
    private final long number;
}
