package com.ytrue.modules.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ytrue
 * @description: ComponentTypeEnum
 * @date 2022/12/12 15:17
 */
@Getter
@AllArgsConstructor
public enum ComponentTypeEnum {

    /**
     * Layout
     */
    LAYOUT("Layout"),

    /**
     * ParentView
     */
    PARENT_VIEW("ParentView"),

    /**
     * InnerLink
     */
    INNER_LINK("InnerLink");


    private final String type;
}
