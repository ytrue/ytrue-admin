package com.ytrue.modules.quartz.model.dto;

import com.ytrue.tools.query.annotation.Query;
import com.ytrue.tools.query.enums.QueryMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author ytrue
 * @description: ScheduleJobLogSearchParams
 * @date 2022/12/24 10:09
 */
@Data
public class ScheduleJobLogSearchParams {

    @Query
    @Schema(title  = "任务id")
    private Long jobId;

    @Query
    @Schema(title  = "任务状态:0=成功,1=失败")
    private Integer status;

    @Query(condition = QueryMethod.between)
    @Schema(title  = "创建时间")
    private List<String> createTime;
}
