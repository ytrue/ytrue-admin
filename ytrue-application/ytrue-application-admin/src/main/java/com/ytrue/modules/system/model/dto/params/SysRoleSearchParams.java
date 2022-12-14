package com.ytrue.modules.system.model.dto.params;

import com.ytrue.tools.query.annotation.Query;
import com.ytrue.tools.query.enums.QueryMethod;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author ytrue
 * @description: SysRoleSearchParams
 * @date 2022/12/20 11:10
 */
@Data
public class SysRoleSearchParams implements Serializable {
    private static final long serialVersionUID = -7570626552880302350L;

    @Query(condition = QueryMethod.like)
    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @Query(condition = QueryMethod.like)
    @ApiModelProperty(value = "角色标识")
    private String roleCode;

    @Query
    @ApiModelProperty(value = "状态:0=禁用,1=正常")
    private Boolean status;

    @Query(condition = QueryMethod.between)
    @ApiModelProperty(value = "创建时间")
    private List<String> createTime;
}
