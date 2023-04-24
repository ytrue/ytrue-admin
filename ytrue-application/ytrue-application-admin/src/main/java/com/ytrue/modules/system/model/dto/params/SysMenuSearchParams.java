package com.ytrue.modules.system.model.dto.params;

import com.ytrue.tools.query.annotation.Query;
import com.ytrue.tools.query.enums.QueryMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ytrue
 * @description: SysMenuSearchParams
 * @date 2022/12/20 11:10
 */
@Data
public class SysMenuSearchParams implements Serializable {
    private static final long serialVersionUID = -760374525244276001L;

    @Query(condition = QueryMethod.like)
    @Schema(title = "菜单名称")
    private String menuName;

    @Query
    @Schema(title = " 菜单状态（0正常 1停用）")
    private Boolean status;

    @Query(condition = QueryMethod.between)
    @Schema(title = "创建时间")
    private List<String> createTime;
}
