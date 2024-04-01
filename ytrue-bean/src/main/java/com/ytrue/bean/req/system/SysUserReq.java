package com.ytrue.bean.req.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * @author ytrue
 * @description: SysUserDetailResp
 * @date 2022/12/7 17:01
 */
@Data
public class SysUserReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "用户部门Id")
    private Long deptId;

    @NotBlank
    @Schema(description = "用户名称")
    private String username;

    @NotBlank
    @Schema(description = "用户昵称")
    private String nickName;

    @Email
    @NotBlank
    @Schema(description = "邮箱")
    private String email;

    @NotBlank
    @Schema(description = "电话号码")
    private String phone;

    @Schema(description = "用户性别")
    private Integer gender;

    @Schema(description = "头像真实名称")
    private String avatarName;

    @Schema(description = "头像存储的路径")
    private String avatarPath;

    @Schema(description = "密码")
    @JsonIgnore
    private String password;

    @NotNull
    @Schema(description = "是否启用")
    private Boolean status;

    @Schema(description = "角色id集合")
    private Set<Long> roleIds;

    @Schema(description = "岗位id集合")
    private Set<Long> jobIds;
}
