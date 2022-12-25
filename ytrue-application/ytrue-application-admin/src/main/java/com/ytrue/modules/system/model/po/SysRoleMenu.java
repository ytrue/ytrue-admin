package com.ytrue.modules.system.model.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author ytrue
 * @description: SysRoleMenu
 * @date 2022/12/7 15:50
 */
@Data
@TableName("sys_role_menu")
public class SysRoleMenu {

    @TableId
    private Long id;

    private Long roleId;

    private Long menuId;
}
