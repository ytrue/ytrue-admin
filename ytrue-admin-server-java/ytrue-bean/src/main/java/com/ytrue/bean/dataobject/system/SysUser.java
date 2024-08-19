package com.ytrue.bean.dataobject.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ytrue.bean.dataobject.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author ytrue
 * @description: SysUser
 * @date 2022/12/7 15:20
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_user")
public class SysUser extends BaseEntity {


    @Schema(description = "用户部门Id")
    private Long deptId;


    @Schema(description = "用户名称")
    private String username;


    @Schema(description = "用户昵称")
    private String nickName;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "电话号码")
    private String phone;

    @Schema(description = "用户性别")
    private Integer gender;

    @Schema(description = "头像存储的路径")
    private String avatar;

    @JsonIgnore
    @Schema(description = "密码")
    private String password;

    @Schema(description = "是否启用")
    private Boolean status;

    @Schema(description = "是否为admin账号")
    private Boolean admin;

    @Schema(description = "最后修改密码的时间")
    private LocalDateTime pwdResetTime;
}
