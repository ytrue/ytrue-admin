package com.ytrue.modules.system.model.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

/**
 * @author ytrue
 * @description: SysUserDetailRes
 * @date 2022/12/7 17:01
 */
@Data
public class SysUserDetailRes {

    @Schema(title = "id")
    private Long id;

    @Schema(title = "用户部门Id")
    private Long deptId;

    @Schema(title = "用户名称")
    private String username;

    @Schema(title = "用户昵称")
    private String nickName;

    @Schema(title = "邮箱")
    private String email;

    @Schema(title = "电话号码")
    private String phone;

    @Schema(title = "用户性别")
    private Integer gender;

    @Schema(title = "头像真实名称")
    private String avatarName;

    @Schema(title = "头像存储的路径")
    private String avatarPath;

    @Schema(title = "是否启用")
    private Boolean status;

    @Schema(title = "角色id集合")
    private Set<Long> roleIds;

    @Schema(title = "岗位id集合")
    private Set<Long> jobIds;
}
