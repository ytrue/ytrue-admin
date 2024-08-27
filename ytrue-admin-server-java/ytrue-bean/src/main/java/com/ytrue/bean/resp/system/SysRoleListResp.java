package com.ytrue.bean.resp.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SysRoleListResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "唯一标识")
    private Long id;

    @Schema(description = "角色名称")
    private String roleName;


    @Schema(description = "角色标识")
    private String roleCode;

    @Schema(description = "数据范围:1=全部数据权限,2=自定数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限")
    private Integer dataScope;

    @Schema(description = "排序")
    private Integer roleSort;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "状态:0=禁用,1=正常")
    private Boolean status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
