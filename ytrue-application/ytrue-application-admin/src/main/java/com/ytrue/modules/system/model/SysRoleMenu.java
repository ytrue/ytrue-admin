package com.ytrue.modules.system.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ytrue
 * @description: SysRoleMenu
 * @date 2022/12/7 15:50
 */
@Data
@TableName("sys_role_menu")
@Accessors(chain = true)
public class SysRoleMenu {

    @TableId
    private Long id;

    private Long roleId;

    private Long menuId;
}
