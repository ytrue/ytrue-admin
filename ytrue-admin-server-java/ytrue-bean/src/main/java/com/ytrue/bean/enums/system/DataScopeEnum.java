package com.ytrue.bean.enums.system;

import lombok.RequiredArgsConstructor;
import lombok.Getter;

/**
 * @author ytrue
 * @description: 数据范围枚举
 * @date 2022/12/29 9:07
 */
@Getter
@RequiredArgsConstructor
public enum DataScopeEnum {


    /**
     * 全部数据权限
     */
    DATA_SCOPE_ALL(1),

    /**
     * 自定数据权限
     */
    DATA_SCOPE_CUSTOM(2),

    /**
     * 部门数据权限
     */
    DATA_SCOPE_DEPT(3),

    /**
     * 部门及以下数据权限
     */
    DATA_SCOPE_DEPT_AND_CHILD(4),

    /**
     * 仅本人数据权限
     */
    DATA_SCOPE_SELF(5);

    private final Integer value;
}
