package com.ytrue.bean.req.system;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SysDeptAddReq {

    @Schema(description = "上级部门")
    private Long pid;


    @Schema(description = "名称")
    private String deptName;

    @Schema(description = "负责人")
    private String leader;

    @Schema(description = "联系电话")
    private String phone;

    @Email
    @Schema(description = "邮箱")
    private String email;

    @NotNull
    @Schema(description = "是否启用")
    private Boolean status;

    @Schema(description = "排序")
    private Integer deptSort;
}
