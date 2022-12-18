package com.ytrue.modules.system.model.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ytrue
 * @description: SysUserRole
 * @date 2022/12/7 17:19
 */
@Data
@TableName("sys_user_role")
public class SysUserRole {

    @TableId
    private Long id;

    private Long userId;

    private Long roleId;
}
