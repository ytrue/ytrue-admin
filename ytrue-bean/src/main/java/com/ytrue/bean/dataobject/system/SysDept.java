package com.ytrue.bean.dataobject.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ytrue.bean.dataobject.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;

/**
 * @author ytrue
 * @date 2022-08-04
 * @description 部门实体类
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_dept")
public class SysDept extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 2021887051210043344L;


    @Schema(description = "上级部门")
    private Long pid;

    @Schema(description = "子节点数目", hidden = true)
    private Integer subCount;

    @Schema(description = "名称")
    private String deptName;

    @Schema(description = "负责人")
    private String leader;

    @Schema(description = "联系电话")
    private String phone;

    @Email
    @Schema(description = "邮箱")
    private String email;

    @NotNull
    @Schema(description = "是否启用")
    private Boolean status;

    @Schema(description = "排序")
    private Integer deptSort;


}
