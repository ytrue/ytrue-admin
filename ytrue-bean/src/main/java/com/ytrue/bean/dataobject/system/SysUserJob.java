package com.ytrue.bean.dataobject.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ytrue.bean.dataobject.BaseIdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ytrue
 * @description: SysUserJob
 * @date 2022/12/7 17:19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_user_job")
public class SysUserJob extends BaseIdEntity {


    private Long userId;

    private Long jobId;
}
