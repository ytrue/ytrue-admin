package com.ytrue.modules.system.model.dto.params;

import com.ytrue.tools.query.annotation.Query;
import com.ytrue.tools.query.enums.QueryMethod;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author ytrue
 * @description: SysUserSearchParams
 * @date 2022/12/20 11:11
 */
@Data
public class SysUserSearchParams implements Serializable {
    private static final long serialVersionUID = -1210636061661488366L;

    @Query(condition = QueryMethod.like, alias = "u")
    @ApiModelProperty(value = "用户名称")
    private String username;

    @Query(condition = QueryMethod.like, alias = "u")
    @ApiModelProperty(value = "电话号码")
    private String phone;


    @Query(alias = "u")
    @ApiModelProperty(value = "部门id")
    private String deptId;


    @Query(alias = "u")
    @ApiModelProperty(value = "是否启用")
    private Boolean status;

    @Query(condition = QueryMethod.between, alias = "u")
    @ApiModelProperty(value = "创建时间")
    private List<String> createTime;
}
