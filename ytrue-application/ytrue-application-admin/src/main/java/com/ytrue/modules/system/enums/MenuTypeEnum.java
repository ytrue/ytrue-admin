package com.ytrue.modules.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ytrue
 * @description: MenuTypeEnum
 * @date 2022/12/12 15:13
 */
@Getter
@AllArgsConstructor
public enum MenuTypeEnum {

    /**
     * C菜单
     */
    MENU("C"),

    /**
     * M目录
     */
    DIRECTORY("M"),

    /**
     * F按钮
     */
    BUTTON("F");

    /**
     * 类型
     */
    private final String type;
}
