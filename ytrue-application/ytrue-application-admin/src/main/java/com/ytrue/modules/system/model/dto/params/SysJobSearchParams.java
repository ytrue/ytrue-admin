package com.ytrue.modules.system.model.dto.params;

import com.ytrue.tools.query.annotation.Query;
import com.ytrue.tools.query.enums.QueryMethod;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ytrue
 * @description: 岗位搜索参数
 * @date 2022/12/20 11:09
 */
@Data
public class SysJobSearchParams implements Serializable {

    private static final long serialVersionUID = -8023936370399354477L;

    @Query(condition = QueryMethod.like)
    @ApiModelProperty(value = "岗位名称")
    private String jobName;

    @Query
    @ApiModelProperty(value = "是否启用")
    private Boolean status;

    @Query(condition = QueryMethod.between)
    @ApiModelProperty(value = "创建时间")
    private List<String> createTime;
}
