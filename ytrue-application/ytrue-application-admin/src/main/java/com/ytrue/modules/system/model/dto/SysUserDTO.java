package com.ytrue.modules.system.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
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
@Accessors(chain = true)
public class SysUserDTO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "用户部门Id")
    private Long deptId;

    @NotBlank
    @ApiModelProperty(value = "用户名称")
    private String username;

    @NotBlank
    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @Email
    @NotBlank
    @ApiModelProperty(value = "邮箱")
    private String email;

    @NotBlank
    @ApiModelProperty(value = "电话号码")
    private String phone;

    @ApiModelProperty(value = "用户性别")
    private String gender;

    @ApiModelProperty(value = "头像真实名称")
    private String avatarName;

    @ApiModelProperty(value = "头像存储的路径")
    private String avatarPath;

    @ApiModelProperty(value = "密码")
    @JsonIgnore
    private String password;

    @NotNull
    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    @ApiModelProperty(value = "角色id集合")
    private Set<Long> roleIds;

    @ApiModelProperty(value = "岗位id集合")
    private Set<Long> jobIds;
}
