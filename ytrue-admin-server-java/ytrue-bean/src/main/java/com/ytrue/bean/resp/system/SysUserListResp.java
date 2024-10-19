package com.ytrue.bean.resp.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ytrue
 * @description: SysUserListResp
 * @date 2022/12/15 11:11
 */
@Data
public class SysUserListResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "用户部门名称")
    private String deptName;


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


    @Schema(description = "是否启用")
    private Boolean status;

    @Schema(description = "是否为admin账号")
    private Boolean admin;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
