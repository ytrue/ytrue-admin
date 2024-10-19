package com.ytrue.infra.mysql.query.entity;

import cn.hutool.core.util.StrUtil;
import com.ytrue.infra.mysql.query.enums.Operator;
import com.ytrue.infra.mysql.query.enums.QueryMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 表示查询条件的类。
 * <p>
 * 此类用于封装数据库查询中的条件信息，包括字段、条件类型、值、别名以及逻辑运算符（AND/OR）。
 * </p>
 *
 * @author ytrue
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class Where {
    /**
     * 字段名。
     */
    private String column;

    /**
     * 查询条件。
     */
    private QueryMethod condition;

    /**
     * 查询值，可以是 String、int、数组或 List。
     */
    private Object value;

    /**
     * 别名，用于指定表的别名。
     */
    private String alias = "";

    /**
     * 逻辑运算符，默认是 AND。
     */
    private Operator operator = Operator.and;

    /**
     * 构造函数，初始化字段、条件和查询值。
     *
     * @param column 字段名
     * @param condition 查询条件
     * @param value 查询值
     */
    public Where(String column, QueryMethod condition, Object value) {
        this.column = column;
        this.value = value;
        this.condition = condition;
    }

    /**
     * 构造函数，初始化字段、条件、查询值和别名。
     *
     * @param column 字段名
     * @param condition 查询条件
     * @param value 查询值
     * @param alias 别名
     */
    public Where(String column, QueryMethod condition, Object value, String alias) {
        this.column = column;
        this.value = value;
        this.condition = condition;
        this.alias = alias;
    }

    /**
     * 构造函数，初始化字段、条件、查询值和逻辑运算符。
     *
     * @param column 字段名
     * @param condition 查询条件
     * @param value 查询值
     * @param operator 逻辑运算符
     */
    public Where(String column, QueryMethod condition, Object value, Operator operator) {
        this.column = column;
        this.value = value;
        this.condition = condition;
        this.operator = operator;
    }

    /**
     * 获取字段的别名，格式为 "tableAlias.columnName"。
     *
     * @return 字段的别名
     */
    public String getColumnAlias() {
        String column = this.getColumn();

        if (StrUtil.isNotBlank(this.getAlias())) {
            column = this.getAlias() + "." + column;
        }
        return column;
    }
}
