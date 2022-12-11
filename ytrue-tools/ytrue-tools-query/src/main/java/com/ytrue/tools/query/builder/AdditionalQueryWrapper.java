package com.ytrue.tools.query.builder;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ytrue.tools.query.entity.Field;
import com.ytrue.tools.query.enums.QueryMethod;
import com.ytrue.tools.query.utils.EmptyUtils;

import java.util.HashMap;
import java.util.List;

/**
 * @author ytrue
 * @date 2022/9/2 09:38
 * @description QueryWrapperBuilder
 */
public class AdditionalQueryWrapper {

    private static final HashMap<QueryMethod, AppendQueryWrapper> APPEND_QUERY_WRAPPER_MAP = new HashMap<>();

    static {
        APPEND_QUERY_WRAPPER_MAP.put(QueryMethod.eq, (queryWrapper, field) -> queryWrapper.eq(field.getColumn(), field.getValue()));
        APPEND_QUERY_WRAPPER_MAP.put(QueryMethod.ne, (queryWrapper, field) -> queryWrapper.ne(field.getColumn(), field.getValue()));
        APPEND_QUERY_WRAPPER_MAP.put(QueryMethod.like, (queryWrapper, field) -> queryWrapper.like(field.getColumn(), field.getValue()));
        APPEND_QUERY_WRAPPER_MAP.put(QueryMethod.likeLeft, (queryWrapper, field) -> queryWrapper.likeLeft(field.getColumn(), field.getValue()));
        APPEND_QUERY_WRAPPER_MAP.put(QueryMethod.likeRight, (queryWrapper, field) -> queryWrapper.likeRight(field.getColumn(), field.getValue()));
    }


    /**
     * QueryWrapper 构建
     *
     * @param queryWrapper
     * @param fields
     */
    public void queryWrapperBuilder(QueryWrapper<?> queryWrapper, List<Field> fields) {
        //要判断一下是否为空，不然会报空指针异常
        if (CollUtil.isNotEmpty(fields)) {
            fields.forEach(field -> {
                // 如果是空就不处理
                if (EmptyUtils.isEmpty(field.getValue())) {
                    return;
                }

                // 处理小驼峰转下划线
                if (field.getColumnToUnderline()) {
                    field.setColumn(StrUtil.toUnderlineCase(field.getColumn()));
                }

                // 进行匹配
                AppendQueryWrapper appendQueryWrapper = APPEND_QUERY_WRAPPER_MAP.get(field.getCondition());
                Assert.notNull(appendQueryWrapper, "类型匹配错误");
                appendQueryWrapper.append(queryWrapper, field);
            });
        }
    }


    /**
     * 查询包装器追加
     */
    @FunctionalInterface
    private interface AppendQueryWrapper {
        /**
         * 追加 QueryWrapper
         *
         * @param queryWrapper
         * @param field
         */
        void append(QueryWrapper<?> queryWrapper, Field field);
    }
}
