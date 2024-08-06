package com.ytrue.bean.query.system;

import com.ytrue.bean.query.ListQuery;
import com.ytrue.infra.db.query.annotation.Where;
import com.ytrue.infra.db.query.enums.QueryMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.util.List;

/**
 * @author ytrue
 * @description: SysDeptListQuery
 * @date 2022/12/20 11:10
 */
@Data
public class SysDeptListQuery extends ListQuery {

    @Serial
    private static final long serialVersionUID = 1L;

    @Where(condition = QueryMethod.like)
    @Schema(description = "名称")
    private String deptName;

    @Where
    @Schema(description = "是否启用")
    private Boolean status;

    @Where(condition = QueryMethod.between)
    @Schema(description = "创建时间")
    private List<String> createTime;
}
