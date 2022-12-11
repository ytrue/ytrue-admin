package com.ytrue.modules.system.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author ytrue
 * @description: SysUser
 * @date 2022/12/7 15:20
 */
@Data
@TableName("sys_user")
public class SysUser {

    @TableId
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

    @ApiModelProperty(value = "是否为admin账号")
    private Boolean isAdmin = false;

    @ApiModelProperty(value = "最后修改密码的时间")
    private LocalDateTime pwdResetTime;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建人", hidden = true)
    private String createBy;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新人", hidden = true)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
