package com.ytrue.tools.query.entity;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ytrue.tools.query.builder.AdditionalQueryWrapper;
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


        System.out.println(orderField);
        // TODO 这里排序要处理
        if (!StrUtil.hasEmpty(orderField)) {
            queryWrapper.orderBy(true, asc, orderField);
        }
        // 返回
        return queryWrapper.lambda();
    }

}
