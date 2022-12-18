package com.ytrue.tools.query.entity;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ytrue.tools.query.builder.AdditionalQueryWrapper;
import com.ytrue.tools.query.enums.QueryMethod;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author ytrue
 * @description: QueryEntity
 * @date 2022/12/7 10:36
 */
@Data
public class QueryEntity<T> implements Serializable {

    private static final long serialVersionUID = -4364862339415897353L;

    /**
     * 字段条件
     */
    private List<Field> fields;

    /**
     * 排序的字段
     */
    private String orderField;

    /**
     * 是否是asc
     */
    private Boolean asc = false;


    /**
     * 追加字段 这个field想变成lamb的方式 TODO 待优化成Lambda
     */
    public QueryEntity<T> appendField(String field, QueryMethod method, Object value) {
        fields.add(new Field(field, method, value));
        return this;
    }


    /**
     * 获得 QueryWrapper
     *
     * @return {@link LambdaQueryWrapper <T>}
     */
    public LambdaQueryWrapper<T> getQueryModel() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        // 追加
        AdditionalQueryWrapper additionalQueryWrapper = new AdditionalQueryWrapper();
        // 构建
        additionalQueryWrapper.queryWrapperBuilder(queryWrapper, this.getFields());

        // TODO 这里排序要处理
        if (!StrUtil.hasEmpty(orderField)) {
            queryWrapper.orderBy(true, asc, orderField);
        }
        // 返回
        return queryWrapper.lambda();
    }
}
