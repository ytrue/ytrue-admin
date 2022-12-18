package com.ytrue.tools.query.entity;

import com.ytrue.tools.query.enums.QueryMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author ytrue
 * @date 2022/4/20 16:06
 * @description 字段
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Field {
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
     * Column 小驼峰转下划线,默认开启
     */
    private Boolean columnToUnderline = true;


    public Field(String column, QueryMethod condition, Object value) {
        this.column = column;
        this.value = value;
        this.condition = condition;
    }
}
