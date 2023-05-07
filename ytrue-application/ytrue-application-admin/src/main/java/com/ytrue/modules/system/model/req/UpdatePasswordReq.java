package com.ytrue.modules.system.model.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author ytrue
 * @description: 密码修改操作
 * @date 2022/12/27 11:28
 */
@Data
public class UpdatePasswordReq {

    @NotBlank
    @Schema(title = "旧密码")
    private String oldPassword;

    @NotBlank
    @Schema(title = "新密码")
    private String newPassword;
}
