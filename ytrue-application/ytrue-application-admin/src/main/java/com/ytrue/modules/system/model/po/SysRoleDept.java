package com.ytrue.modules.system.model.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ytrue
 * @description: SysRoleDept
 * @date 2022/12/7 15:50
 */
@Data
public class SysRoleDept {

    @TableId
    private Long id;

    private Long roleId;

    private Long deptId;
}
