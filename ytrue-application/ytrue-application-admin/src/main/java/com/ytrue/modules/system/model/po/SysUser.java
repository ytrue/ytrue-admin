package com.ytrue.modules.system.model.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ytrue.common.constant.TimeFormat;
import com.ytrue.common.constant.TimeZone;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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

    @Schema(title = "是否为admin账号")
    private Boolean isAdmin;

    @Schema(title = "最后修改密码的时间")
    private LocalDateTime pwdResetTime;

    @TableField(fill = FieldFill.INSERT)
    @Schema(title = "创建人", hidden = true)
    private String createBy;

    @TableField(fill = FieldFill.UPDATE)
    @Schema(title = "更新人", hidden = true)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT)
    @Schema(title = "创建时间")
    @JsonFormat(timezone = TimeZone.GMT8,pattern = TimeFormat.DATE_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeFormat.DATE_TIME_FORMAT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    @Schema(title = "更新时间")
    @JsonFormat(timezone = TimeZone.GMT8,pattern = TimeFormat.DATE_TIME_FORMAT)
    @DateTimeFormat(pattern = TimeFormat.DATE_TIME_FORMAT)
    private LocalDateTime updateTime;
}
