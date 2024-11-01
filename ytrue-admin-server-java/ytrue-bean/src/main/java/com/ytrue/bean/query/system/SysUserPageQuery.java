package com.ytrue.bean.query.system;

import com.ytrue.bean.query.PageQuery;
import com.ytrue.infra.mysql.query.annotation.Where;
import com.ytrue.infra.mysql.query.enums.QueryMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;
import java.util.List;

/**
 * @author ytrue
 * @description: SysUserPageQuery
 * @date 2022/12/20 11:11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SysUserPageQuery extends PageQuery {

    @Serial
    private static final long serialVersionUID = 1L;

    @Where(condition = QueryMethod.like, alias = "u")
    @Schema(description = "用户名称")
    private String username;

    @Where(condition = QueryMethod.like, alias = "u")
    @Schema(description = "电话号码")
    private String phone;

    @Where(alias = "u")
    @Schema(description = "部门id")
    private String deptId;

    @Where(alias = "u")
    @Schema(description = "是否启用")
    private Boolean status;

    @Where(condition = QueryMethod.between, alias = "u")
    @Schema(description = "创建时间")
    private List<String> createTime;
}
