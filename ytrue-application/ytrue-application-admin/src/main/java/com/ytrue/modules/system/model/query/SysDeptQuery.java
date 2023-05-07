package com.ytrue.modules.system.model.query;

import com.ytrue.tools.query.annotation.Query;
import com.ytrue.tools.query.enums.QueryMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author ytrue
 * @description: SysDeptQuery
 * @date 2022/12/20 11:10
 */
@Data
public class SysDeptQuery implements Serializable {
    private static final long serialVersionUID = 5230520838989522329L;

    @Query(condition = QueryMethod.like)
    @Schema(title = "名称")
    private String deptName;

    @Query
    @Schema(title = "是否启用")
    private Boolean status;

    @Query(condition = QueryMethod.between)
    @Schema(title = "创建时间")
    private List<String> createTime;
}
