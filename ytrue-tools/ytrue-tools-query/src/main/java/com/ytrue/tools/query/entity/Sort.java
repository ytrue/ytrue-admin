package com.ytrue.tools.query.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ytrue
 * @description: Order
 * @date 2022/12/20 9:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sort {


    /**
     * 字段
     */
    private String column;

    /**
     * 是否asc
     */
    private Boolean asc = false;

    /**
     * 别名
     */
    private String alias = "";

    public Sort(String column, Boolean asc) {
        this.column = column;
        this.asc = asc;
    }
}
