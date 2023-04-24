package com.ytrue.modules.system.model.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ytrue
 * @description: SysJob
 * @date 2022/12/7 10:54
 */
@Data
@TableName("sys_job")
public class SysJob implements Serializable {

    private static final long serialVersionUID = 8557945474976276338L;

    @TableId
    @Schema(title = "id")
    private Long id;

    @NotBlank
    @Schema(title = "岗位名称")
    private String jobName;

    @NotNull
    @Schema(title = "岗位排序")
    private Long jobSort;

    @NotNull
    @Schema(title = "是否启用")
    private Boolean status;

    @TableField(fill = FieldFill.INSERT)
    @Schema(title = "创建人", hidden = true)
    private String createBy;

    @TableField(fill = FieldFill.UPDATE)
    @Schema(title = "更新人", hidden = true)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT)
    @Schema(title = "创建时间")
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.UPDATE)
    @Schema(title = "更新时间")
    private LocalDateTime updateTime;
}
