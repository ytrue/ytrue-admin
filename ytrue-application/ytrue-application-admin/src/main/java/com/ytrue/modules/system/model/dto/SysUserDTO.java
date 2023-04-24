package com.ytrue.modules.system.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author ytrue
 * @description: SysUserDTO
 * @date 2022/12/7 17:01
 */
@Data
public class SysUserDTO {

    @Schema(title = "id")
    private Long id;

    @Schema(title = "用户部门Id")
    private Long deptId;

    @NotBlank
    @Schema(title = "用户名称")
    private String username;

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

    @Schema(title = "头像真实名称")
    private String avatarName;

    @Schema(title = "头像存储的路径")
    private String avatarPath;

    @Schema(title = "密码")
    @JsonIgnore
    private String password;

    @NotNull
    @Schema(title = "是否启用")
    private Boolean status;

    @Schema(title = "角色id集合")
    private Set<Long> roleIds;

    @Schema(title = "岗位id集合")
    private Set<Long> jobIds;
}
