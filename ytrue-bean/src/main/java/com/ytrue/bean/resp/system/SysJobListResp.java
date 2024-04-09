package com.ytrue.bean.resp.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class SysJobListResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    @Schema(description = "唯一标识")
    private Long id;

    @Schema(description = "岗位名称")
    private String jobName;

    @Schema(description = "岗位排序")
    private Long jobSort;


    @Schema(description = "是否启用")
    private Boolean status;
}
