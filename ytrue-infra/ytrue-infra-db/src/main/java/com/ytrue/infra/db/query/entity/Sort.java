package com.ytrue.infra.db.query.entity;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ytrue
 * @description: Order
 * @date 2022/12/20 9:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    /**
     * 获取字段别名
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
