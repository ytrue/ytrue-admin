package com.ytrue.modules.system.model.dto.operation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author ytrue
 * @description: 密码修改操作
 * @date 2022/12/27 11:28
 */
@Data
public class PasswordOperationDTO {

    @NotBlank
    @ApiModelProperty(value = "旧密码")
    private String oldPassword;

    @NotBlank
    @ApiModelProperty(value = "新密码")
    private String newPassword;
}
