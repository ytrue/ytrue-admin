package com.ytrue.infra.db.query.enums;

/**
 * @author ytrue
 * @date 2022/4/20 16:07
 * @description 条件枚举
 */
public enum QueryMethod {

    /**
     * ==
     */
    eq,

    /**
     * !=
     */
    ne,

    /**
     * like
     */
    like,

    /**
     * 左模糊
     */
    likeLeft,

    /**
     * 右边模糊
     */
    likeRight,

    /**
     * 范围查询 [111,222]
     */
    between,

    notBetween,

    /**
     * in
     */
    in,

    notin,

    gt,

    lt,

    ge,

    le,
}
