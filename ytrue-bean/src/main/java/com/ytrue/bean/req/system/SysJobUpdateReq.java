package com.ytrue.bean.req.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SysJobUpdateReq {

    @Schema(description = "唯一标识")
    private Long id;

    @Schema(description = "岗位名称")
    private String jobName;

    @Schema(description = "岗位排序")
    private Long jobSort;


    @Schema(description = "是否启用")
    private Boolean status;
}
