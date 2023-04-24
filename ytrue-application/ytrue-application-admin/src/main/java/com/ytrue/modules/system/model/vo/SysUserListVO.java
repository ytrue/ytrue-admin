package com.ytrue.modules.system.model.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author ytrue
 * @description: SysUserListVO
 * @date 2022/12/15 11:11
 */
@Data
public class SysUserListVO {

    @TableId
    @Schema(title = "id")
    private Long id;

    @Schema(title = "用户部门名称")
    private String deptName;

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

    @NotNull
    @Schema(title = "是否启用")
    private Boolean status;

    @Schema(title = "创建时间")
    private LocalDateTime createTime;

}
