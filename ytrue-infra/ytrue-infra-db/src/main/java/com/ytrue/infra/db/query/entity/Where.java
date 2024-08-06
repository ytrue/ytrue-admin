package com.ytrue.infra.db.query.entity;

import cn.hutool.core.util.StrUtil;
import com.ytrue.infra.db.query.enums.Operator;
import com.ytrue.infra.db.query.enums.QueryMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class Where {
    /**
     * 字段
     */
    private String column;


    /**
     * 条件
     */
    private QueryMethod condition;

    /**
     * 内容 ,一般是 string 和 int, array,list
     */
    private Object value;

    /**
     * 别名
     */
    private String alias = "";

    /**
     * and 还是  or
     */
    private Operator operator = Operator.and;

    public Where(String column, QueryMethod condition, Object value) {
        this.column = column;
        this.value = value;
        this.condition = condition;
    }

    public Where(String column, QueryMethod condition, Object value, String alias) {
        this.column = column;
        this.value = value;
        this.condition = condition;
        this.alias = alias;
    }

    public Where(String column, QueryMethod condition, Object value, Operator operator) {
        this.column = column;
        this.value = value;
        this.condition = condition;
        this.operator = operator;
    }


    /**
     * 获取字段别名， tableAlias.columName
     *
     * @return
     */
    public String getColumnAlias() {

        String column = this.getColumn();

        if (StrUtil.isNotBlank(this.getAlias())) {
            column = this.getAlias() + "." + column;
        }
        return column;
    }
}
