package com.ytrue.modules.quartz.model.dto;

import com.ytrue.tools.query.annotation.Query;
import com.ytrue.tools.query.enums.QueryMethod;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author ytrue
 * @description: ScheduleJobSearchParams
 * @date 2022/12/24 10:09
 */
@Data
public class ScheduleJobSearchParams {

    @Query(condition = QueryMethod.like)
    @ApiModelProperty(value = "bean名称")
    private String beanName;

    @Query
    @ApiModelProperty(value = "任务状态:0=正常,1=暂停")
    private Integer status;

    @Query(condition = QueryMethod.between)
    @ApiModelProperty(value = "创建时间")
    private List<String> createTime;
}
