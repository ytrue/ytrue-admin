package com.ytrue.bean.enums.system;

import lombok.RequiredArgsConstructor;
import lombok.Getter;

/**
 * @author ytrue
 * @description: MenuTypeEnum
 * @date 2022/12/12 15:13
 */
@Getter
@RequiredArgsConstructor
public enum MenuTypeEnum {



    /**
     * M目录
     */
    DIRECTORY("M"),

    /**
     * C菜单
     */
    MENU("C"),

    /**
     * F按钮
     */
    BUTTON("F");

    /**
     * 类型
     */
    private final String type;
}
