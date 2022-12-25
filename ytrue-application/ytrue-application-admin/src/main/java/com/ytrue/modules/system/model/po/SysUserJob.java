package com.ytrue.modules.system.model.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author ytrue
 * @description: SysUserJob
 * @date 2022/12/7 17:19
 */
@Data
@TableName("sys_user_job")
public class SysUserJob {


    @TableId
    private Long id;

    private Long userId;

    private Long jobId;
}
