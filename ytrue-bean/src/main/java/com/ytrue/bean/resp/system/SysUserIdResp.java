package com.ytrue.bean.resp.system;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ytrue.infra.core.ser.LongCollToStrSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * @author ytrue
 * @description: SysUserIdResp
 * @date 2022/12/7 17:01
 */
@Data
public class SysUserIdResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

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

    @Schema(description = "头像真实名称")
    private String avatarName;

    @Schema(description = "头像存储的路径")
    private String avatarPath;

    @Schema(description = "是否启用")
    private Boolean status;

    @Schema(description = "角色id集合")
    @JsonSerialize(using = LongCollToStrSerializer.class)
    private Set<Long> roleIds;

    @Schema(description = "岗位id集合")
    @JsonSerialize(using = LongCollToStrSerializer.class)
    private Set<Long> jobIds;
}
