package com.ytrue.tools.query.additional;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ytrue.tools.query.entity.Filter;
import com.ytrue.tools.query.entity.QueryEntity;
import com.ytrue.tools.query.entity.Sort;
import com.ytrue.tools.query.enums.Operator;
import com.ytrue.tools.query.enums.QueryMethod;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author ytrue
 * @date 2022/9/2 09:38
 * @description QueryWrapperBuilder
 */
public class AdditionalQueryWrapper {

    private static final HashMap<QueryMethod, AppendQueryWrapper> APPEND_QUERY_WRAPPER_MAP = new HashMap<>();

    static {
        APPEND_QUERY_WRAPPER_MAP.put(QueryMethod.eq, (queryWrapper, filter) -> queryWrapper.eq(filter.getColumnAlias(), filter.getValue()));
        APPEND_QUERY_WRAPPER_MAP.put(QueryMethod.ne, (queryWrapper, filter) -> queryWrapper.ne(filter.getColumnAlias(), filter.getValue()));

        APPEND_QUERY_WRAPPER_MAP.put(QueryMethod.gt, (queryWrapper, filter) -> queryWrapper.gt(filter.getColumnAlias(), filter.getValue()));
        APPEND_QUERY_WRAPPER_MAP.put(QueryMethod.lt, (queryWrapper, filter) -> queryWrapper.lt(filter.getColumnAlias(), filter.getValue()));
        APPEND_QUERY_WRAPPER_MAP.put(QueryMethod.ge, (queryWrapper, filter) -> queryWrapper.gt(filter.getColumnAlias(), filter.getValue()));
        APPEND_QUERY_WRAPPER_MAP.put(QueryMethod.le, (queryWrapper, filter) -> queryWrapper.le(filter.getColumnAlias(), filter.getValue()));


        APPEND_QUERY_WRAPPER_MAP.put(QueryMethod.like, (queryWrapper, filter) -> queryWrapper.like(filter.getColumnAlias(), filter.getValue()));
        APPEND_QUERY_WRAPPER_MAP.put(QueryMethod.likeLeft, (queryWrapper, filter) -> queryWrapper.likeLeft(filter.getColumnAlias(), filter.getValue()));
        APPEND_QUERY_WRAPPER_MAP.put(QueryMethod.likeRight, (queryWrapper, filter) -> queryWrapper.likeRight(filter.getColumnAlias(), filter.getValue()));

        APPEND_QUERY_WRAPPER_MAP.put(QueryMethod.between, (queryWrapper, filter) -> queryWrapper.between(filter.getColumnAlias(), ((List<?>) filter.getValue()).get(0), ((List<?>) filter.getValue()).get(1)));
        APPEND_QUERY_WRAPPER_MAP.put(QueryMethod.notBetween, (queryWrapper, filter) -> queryWrapper.notBetween(filter.getColumnAlias(), ((List<?>) filter.getValue()).get(0), ((List<?>) filter.getValue()).get(1)));

        APPEND_QUERY_WRAPPER_MAP.put(QueryMethod.in, (queryWrapper, filter) -> queryWrapper.in(filter.getColumnAlias(), filter.getValue()));
        APPEND_QUERY_WRAPPER_MAP.put(QueryMethod.notin, (queryWrapper, filter) -> queryWrapper.notIn(filter.getColumnAlias(), filter.getValue()));
    }


    /**
     * QueryWrapper 构建
     *
     * @param queryWrapper
     * @param queryEntity
     */
    public void queryWrapperBuilder(QueryWrapper<?> queryWrapper, QueryEntity queryEntity) {
        Set<Filter> filters = queryEntity.getFilters();
        Set<Sort> sorts = queryEntity.getSorts();

        queryWrapperBuilder(queryWrapper, filters);
        queryWrapperBuilder(queryWrapper, sorts);
    }


    public void queryWrapperBuilder(QueryWrapper<?> queryWrapper, Set<?> filterOrSortSet) {

        // 空处理
        if (queryWrapper == null) {
            throw new NullPointerException("queryWrapper  is null");
        }
        // 要判断一下是否为空，不然会报空指针异常
        if (CollectionUtils.isEmpty(filterOrSortSet)) {
            return;
        }

        Set<Filter> filters = new LinkedHashSet<>();
        Set<Sort> sorts = new LinkedHashSet<>();
        // 循环处理下数据
        filterOrSortSet.forEach(item -> {
            if (item instanceof Filter filter) {
                filters.add(filter);
            }
            if (item instanceof Sort sort) {
                sorts.add(sort);
            }
        });

        //要判断一下是否为空，不然会报空指针异常
        if (!CollectionUtils.isEmpty(filters)) {
            filters.forEach(filter -> {
                // 如果是null就不处理
                if (ObjectUtil.isNull(filter.getValue())) {
                    return;
                }
                // 进行匹配
                AppendQueryWrapper appendQueryWrapper = APPEND_QUERY_WRAPPER_MAP.get(filter.getCondition());
                Assert.notNull(appendQueryWrapper, "类型匹配错误");
                filter.setColumn(filter.getColumn());

                // or的处理
                queryWrapper.or(filter.getOperator().equals(Operator.or));
                appendQueryWrapper.append(queryWrapper, filter);
            });
        }

        //要判断一下是否为空，不然会报空指针异常
        if (!CollectionUtils.isEmpty(sorts)) {
            sorts.forEach(filter -> {
                sorts.forEach(sort -> queryWrapper.orderBy(StrUtil.isNotBlank(sort.getColumnAlias()), sort.getAsc(), sort.getColumn()));
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
         * @param filter
         */
        void append(QueryWrapper<?> queryWrapper, Filter filter);
    }
}
