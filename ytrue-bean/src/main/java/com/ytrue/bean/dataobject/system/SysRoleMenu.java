package com.ytrue.bean.dataobject.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ytrue.bean.dataobject.BaseIdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author ytrue
 * @description: SysRoleMenu
 * @date 2022/12/7 15:50
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role_menu")
@Data
public class SysRoleMenu extends BaseIdEntity {


    private Long roleId;

    private Long menuId;
}
