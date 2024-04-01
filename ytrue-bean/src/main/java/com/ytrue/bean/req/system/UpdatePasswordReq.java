package com.ytrue.bean.req.system;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author ytrue
 * @description: 密码修改操作
 * @date 2022/12/27 11:28
 */
@Data
public class UpdatePasswordReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    @Schema(description = "旧密码")
    private String oldPassword;

    @NotBlank
    @Schema(description = "新密码")
    private String newPassword;
}
