package com.ytrue.modules.system.model.dto.operation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author ytrue
 * @description: 用户信息操作
 * @date 2022/12/27 11:28
 */
@Data
public class UserProfileOperationDTO {

    @NotBlank
    @Schema(title = "用户昵称")
    private String nickName;

    @Email
    @NotBlank
    @Schema(title = "邮箱")
    private String email;

    @NotBlank
    @Schema(title = "电话号码")
    private String phone;

    @Schema(title = "用户性别")
    private Integer gender;
}
