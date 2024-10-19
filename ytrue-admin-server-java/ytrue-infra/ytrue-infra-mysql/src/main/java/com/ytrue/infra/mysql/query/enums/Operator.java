package com.ytrue.infra.mysql.query.enums;

/**
 * 操作符枚举类，用于表示查询条件之间的逻辑关系。
 * <p>
 * 该枚举包含了常见的逻辑操作符，如 AND 和 OR。
 * </p>
 *
 * @author ytrue
 * @date 2022/12/21 16:57
 */
public enum Operator {
    /**
     * AND 操作符，用于表示两个条件之间的逻辑与关系。
     */
    and,

    /**
     * OR 操作符，用于表示两个条件之间的逻辑或关系。
     */
    or,
}
