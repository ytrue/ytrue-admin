package com.ytrue.modules.quartz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ytrue
 * @date 2021/4/8 15:36
 * @description ScheduleStatusEnum
 */
@Getter
@AllArgsConstructor
public enum ScheduleStatusEnum {

    /**
     * 正常
     */
    NORMAL(0),
    /**
     * 暂停
     */
    PAUSE(1);

    /**
     * 类型
     */
    private final Integer type;
}
