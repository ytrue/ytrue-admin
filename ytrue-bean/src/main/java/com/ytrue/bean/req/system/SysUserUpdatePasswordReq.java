package com.ytrue.bean.req.system;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SysUserUpdatePasswordReq {

    @NotBlank
    @Schema(description = "旧密码")
    private String oldPassword;

    @NotBlank
    @Schema(description = "新密码")
    private String newPassword;
}
