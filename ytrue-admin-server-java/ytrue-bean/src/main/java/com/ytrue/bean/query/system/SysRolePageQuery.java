package com.ytrue.bean.query.system;

import com.ytrue.bean.query.PageQuery;
import com.ytrue.infra.db.query.annotation.Where;
import com.ytrue.infra.db.query.enums.QueryMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;
import java.util.List;

/**
 * @author ytrue
 * @description: SysRolePageQuery
 * @date 2022/12/20 11:10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class SysRolePageQuery extends PageQuery {

    @Serial
    private static final long serialVersionUID = 1L;

    @Where(condition = QueryMethod.like)
    @Schema(description = "角色名称")
    private String roleName;

    @Where(condition = QueryMethod.like)
    @Schema(description = "角色标识")
    private String roleCode;

    @Where
    @Schema(description = "状态:0=禁用,1=正常")
    private Boolean status;

    @Where(condition = QueryMethod.between)
    @Schema(description = "创建时间")
    private List<String> createTime;
}
