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
 * @description: SysMenuQuery
 * @date 2022/12/20 11:10
 */
@Data
public class SysMenuListQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Query(condition = QueryMethod.like)
    @Schema(description = "菜单名称")
    private String menuName;

    @Query
    @Schema(description = " 菜单状态（0正常 1停用）")
    private Boolean status;

    @Query(condition = QueryMethod.between)
    @Schema(description = "创建时间")
    private List<String> createTime;
}
