package com.ytrue.bean.query.system;

import com.ytrue.tools.query.annotation.Query;
import com.ytrue.tools.query.enums.QueryMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author ytrue
 * @date 2023-06-28 13:58
 * @description SysTenantPackageQuery
 */
@Data
public class SysTenantPackageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Query(condition = QueryMethod.like)
    @Schema(description = "套餐名称")
    private String name;

    @Query(condition = QueryMethod.like)
    @Schema(description = "状态:0=禁用,1=正常")
    private Boolean status;

}
