package com.ytrue.modules.system.model.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ytrue.common.constant.DataFormat;
import com.ytrue.common.constant.TimeZone;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ytrue
 * @date 2022-08-04
 * @description 部门实体类
 */
@Data
@TableName("sys_dept")
public class SysDept implements Serializable {

    private static final long serialVersionUID = 2021887051210043344L;

    @TableId
    @Schema(title = "id")
    private Long id;

    @Schema(title = "上级部门")
    private Long pid;

    @Schema(title = "子节点数目", hidden = true)
    private Integer subCount;

    @Schema(title = "名称")
    private String deptName;

    @Schema(title = "负责人")
    private String leader;

    @Schema(title = "联系电话")
    private String phone;

    @Email
    @Schema(title = "邮箱")
    private String email;

    @NotNull
    @Schema(title = "是否启用")
    private Boolean status;

    @Schema(title = "排序")
    private Integer deptSort;

    @TableField(fill = FieldFill.INSERT)
    @Schema(title = "创建人", hidden = true)
    private String createBy;

    @TableField(fill = FieldFill.UPDATE)
    @Schema(title = "更新人", hidden = true)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT)
    @Schema(title = "创建时间")
    @JsonFormat(timezone = TimeZone.GMT8, pattern = DataFormat.DATE_TIME_FORMAT)
    @DateTimeFormat(pattern = DataFormat.DATE_TIME_FORMAT)
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.UPDATE)
    @Schema(title = "更新时间")
    @JsonFormat(timezone = TimeZone.GMT8, pattern = DataFormat.DATE_TIME_FORMAT)
    @DateTimeFormat(pattern = DataFormat.DATE_TIME_FORMAT)
    private LocalDateTime updateTime;
}
