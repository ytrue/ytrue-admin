package com.ytrue.bean.query.system;

import com.ytrue.bean.Pageable;
import com.ytrue.infra.db.query.annotation.Query;
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
 * @description: SysJobPageQuery
 * @date 2022/12/20 11:09
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@Builder
public class SysJobPageQuery extends Pageable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Query(condition = QueryMethod.like)
    @Schema(description = "岗位名称")
    private String jobName;

    @Query
    @Schema(description = "是否启用")
    private Boolean status;

    @Query(condition = QueryMethod.between)
    @Schema(description = "创建时间")
    private List<String> createTime;
}
