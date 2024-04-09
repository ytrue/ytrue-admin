package com.ytrue.bean.resp.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class SysDeptIdResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    @Schema(description = "唯一标识")
    private Long id;

    @Schema(description = "上级部门")
    private Long pid;


    @Schema(description = "名称")
    private String deptName;

    @Schema(description = "负责人")
    private String leader;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "是否启用")
    private Boolean status;

    @Schema(description = "排序")
    private Integer deptSort;
}
