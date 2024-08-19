package com.ytrue.bean.enums.system;

import lombok.RequiredArgsConstructor;
import lombok.Getter;

/**
 * @author ytrue
 * @description: ComponentTypeEnum
 * @date 2022/12/12 15:17
 */
@Getter
@RequiredArgsConstructor
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
