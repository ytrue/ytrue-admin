package com.ytrue.bean.query.system;

import com.ytrue.tools.query.annotation.Query;
import com.ytrue.tools.query.enums.QueryMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author ytrue
 * @description: SysUserQuery
 * @date 2022/12/20 11:11
 */
@Data
public class SysUserQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Query(condition = QueryMethod.like, alias = "u")
    @Schema(description = "用户名称")
    private String username;

    @Query(condition = QueryMethod.like, alias = "u")
    @Schema(description = "电话号码")
    private String phone;

    @Query(alias = "u")
    @Schema(description = "部门id")
    private String deptId;

    @Query(alias = "u")
    @Schema(description = "是否启用")
    private Boolean status;

    @Query(condition = QueryMethod.between, alias = "u")
    @Schema(description = "创建时间")
    private List<String> createTime;
}
