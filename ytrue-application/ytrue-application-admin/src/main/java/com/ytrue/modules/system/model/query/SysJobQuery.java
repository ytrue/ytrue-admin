package com.ytrue.modules.system.model.query;

import com.ytrue.tools.query.annotation.Query;
import com.ytrue.tools.query.enums.QueryMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author ytrue
 * @description: 岗位搜索参数
 * @date 2022/12/20 11:09
 */
@Data
public class SysJobQuery implements Serializable {

    private static final long serialVersionUID = -8023936370399354477L;

    @Query(condition = QueryMethod.like)
    @Schema(title = "岗位名称")
    private String jobName;

    @Query
    @Schema(title = "是否启用")
    private Boolean status;

    @Query(condition = QueryMethod.between)
    @Schema(title = "创建时间")
    private List<String> createTime;
}
