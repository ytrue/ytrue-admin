package com.ytrue.bean.dataobject.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ytrue.bean.dataobject.BaseIdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ytrue
 * @description: SysUserRole
 * @date 2022/12/7 17:19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_user_role")
public class SysUserRole extends BaseIdEntity {


    private Long userId;

    private Long roleId;
}
