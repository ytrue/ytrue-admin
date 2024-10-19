package com.ytrue.infra.mysql.query.entity;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 排序条件类，用于表示数据库查询中的排序字段及其顺序。
 * <p>
 * 该类包含字段名、排序方向（升序或降序）以及字段的别名，
 * 可用于在构建查询时指定排序规则。
 * </p>
 *
 * @author ytrue
 * @date 2022/12/20 9:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderBy {

    /**
     * 排序字段的名称。
     */
    private String column;

    /**
     * 是否为升序排序。默认为 false，表示降序排序。
     */
    private Boolean asc = false;

    /**
     * 字段的别名，用于在查询中为字段指定别名。
     */
    private String alias = "";

    /**
     * 构造函数，用于创建 OrderBy 对象，指定字段及排序方向。
     *
     * @param column 排序字段名称
     * @param asc 是否升序排序
     */
    public OrderBy(String column, Boolean asc) {
        this.column = column;
        this.asc = asc;
    }

    /**
     * 获取字段的完整别名，包括可能的别名和字段名。
     *
     * @return 字段的完整别名，例如 "alias.column"。
     */
    public String getColumnAlias() {
        String column = this.getColumn();

        if (StrUtil.isNotBlank(this.getAlias())) {
            column = this.getAlias() + "." + column;
        }
        return column;
    }
}
