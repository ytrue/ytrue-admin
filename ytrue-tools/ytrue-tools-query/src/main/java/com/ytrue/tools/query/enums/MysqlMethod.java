package com.ytrue.tools.query.enums;

import lombok.Getter;

/**
 * @author ytrue
 * @description: MysqlMethod
 * @date 2022/12/22 15:38
 */
@Getter
public enum MysqlMethod {

    /**
     * ==
     */
    EQ("="),
    NE("!="),
    LIKE("LIKE"),
    BETWEEN("BetweenAdditionalCondition"),
    IN("in");

    private final String value;

    MysqlMethod(String value) {
        this.value = value;
    }
}
