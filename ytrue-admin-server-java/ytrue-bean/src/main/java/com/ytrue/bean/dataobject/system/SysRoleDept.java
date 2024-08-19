package com.ytrue.bean.dataobject.system;

import com.ytrue.bean.dataobject.BaseIdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author ytrue
 * @description: SysRoleDept
 * @date 2022/12/7 15:50
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class SysRoleDept extends BaseIdEntity {

    private Long roleId;

    private Long deptId;
}
