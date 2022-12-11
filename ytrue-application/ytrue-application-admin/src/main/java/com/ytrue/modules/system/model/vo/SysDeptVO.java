package com.ytrue.modules.system.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ytrue
 * @date 2022/12/11 17:15
 * @description SysDeptVO
 */
@Data
public class SysDeptVO implements Serializable {

    private static final long serialVersionUID = 2021887051210443344L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "上级部门")
    private Long pid;

    @ApiModelProperty(value = "子节点数目", hidden = true)
    private Integer subCount;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "负责人")
    private String leader;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @Email
    @ApiModelProperty(value = "邮箱")
    private String email;

    @NotNull
    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    @ApiModelProperty(value = "排序")
    private Integer deptSort;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 是否包含子子级
     */
    @ApiModelProperty(value = "是否包含子级数据")
    private Boolean hasChildren;
}
