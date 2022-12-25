package com.ytrue.tools.query.entity;

import com.ytrue.tools.query.enums.Operator;
import com.ytrue.tools.query.enums.QueryMethod;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author ytrue
 * @date 2022/4/20 16:06
 * @description 字段
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Filter {

    /**
     * 字段
     */
    private String column;

    /**
     * 内容 ,一般是 string 和 int, array,list
     */
    private Object value;

    /**
     * 条件
     */
    private QueryMethod condition;

    /**
     * 别名
     */
    private String alias = "";

    // operator  = or and

    /**
     * and 还是  or
     */
    private Operator operator = Operator.and;


    public Filter(String column, QueryMethod condition, Object value) {
        this.column = column;
        this.value = value;
        this.condition = condition;
    }

    public Filter(String column, QueryMethod condition, Object value, String alias) {
        this.column = column;
        this.value = value;
        this.condition = condition;
        this.alias = alias;
    }

    public Filter(String column, QueryMethod condition, Object value, Operator operator) {
        this.column = column;
        this.value = value;
        this.condition = condition;
        this.operator = operator;
    }


    public Filter(String column, QueryMethod condition, Object value, String alias, Operator operator) {
        this.column = column;
        this.value = value;
        this.condition = condition;
        this.alias = alias;
        this.operator = operator;
    }

}
