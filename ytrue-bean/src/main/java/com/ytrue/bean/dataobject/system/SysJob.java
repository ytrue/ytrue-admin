package com.ytrue.bean.dataobject.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ytrue.bean.dataobject.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;

/**
 * @author ytrue
 * @description: SysJob
 * @date 2022/12/7 10:54
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@TableName("sys_job")
public class SysJob extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 8557945474976276338L;


    @Schema(description = "岗位名称")
    private String jobName;

    @Schema(description = "岗位排序")
    private Long jobSort;


    @Schema(description = "是否启用")
    private Boolean status;

}
