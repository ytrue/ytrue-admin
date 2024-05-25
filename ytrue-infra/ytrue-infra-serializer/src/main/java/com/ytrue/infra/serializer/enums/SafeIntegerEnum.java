package com.ytrue.infra.serializer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SafeIntegerEnum {

    /**
     * 根据 JS Number.MAX_SAFE_INTEGER 与 Number.MIN_SAFE_INTEGER 得来
     * 最大数
     */
    MAX_SAFE_INTEGER(9007199254740991L),
    /**
     * 最小数
     */
    MIN_SAFE_INTEGER(-9007199254740991L);

    private final long number;
}
