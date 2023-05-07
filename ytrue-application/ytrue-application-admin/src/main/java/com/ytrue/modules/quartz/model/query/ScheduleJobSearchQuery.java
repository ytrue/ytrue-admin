package com.ytrue.modules.quartz.model.query;

import com.ytrue.tools.query.annotation.Query;
import com.ytrue.tools.query.enums.QueryMethod;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author ytrue
 * @description: ScheduleJobSearchQuery
 * @date 2022/12/24 10:09
 */
@Data
public class ScheduleJobSearchQuery {

    @Query(condition = QueryMethod.like)
    @Schema(title = "bean名称")
    private String beanName;

    @Query
    @Schema(title = "任务状态:0=正常,1=暂停")
    private Integer status;

    @Query(condition = QueryMethod.between)
    @Schema(title = "创建时间")
    private List<String> createTime;
}
