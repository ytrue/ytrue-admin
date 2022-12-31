package com.ytrue.tools.query.builder;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ytrue.tools.query.entity.Filter;
import com.ytrue.tools.query.entity.QueryEntity;
import com.ytrue.tools.query.entity.Sort;
import com.ytrue.tools.query.enums.Operator;
import com.ytrue.tools.query.enums.QueryMethod;
import com.ytrue.tools.query.utils.EmptyUtils;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author ytrue
 * @date 2022/9/2 09:38
 * @description QueryWrapperBuilder
 */
public class AdditionalQueryWrapper {

    private static final HashMap<QueryMethod, AppendQueryWrapper> APPEND_QUERY_WRAPPER_MAP = new HashMap<>();

    static {
        APPEND_QUERY_WRAPPER_MAP.put(QueryMethod.eq, (queryWrapper, filter) -> queryWrapper.eq(filter.getColumn(), filter.getValue()));
        APPEND_QUERY_WRAPPER_MAP.put(QueryMethod.ne, (queryWrapper, filter) -> queryWrapper.ne(filter.getColumn(), filter.getValue()));
        APPEND_QUERY_WRAPPER_MAP.put(QueryMethod.like, (queryWrapper, filter) -> queryWrapper.like(filter.getColumn(), filter.getValue()));
        APPEND_QUERY_WRAPPER_MAP.put(QueryMethod.likeLeft, (queryWrapper, filter) -> queryWrapper.likeLeft(filter.getColumn(), filter.getValue()));
        APPEND_QUERY_WRAPPER_MAP.put(QueryMethod.likeRight, (queryWrapper, filter) -> queryWrapper.likeRight(filter.getColumn(), filter.getValue()));
        APPEND_QUERY_WRAPPER_MAP.put(QueryMethod.between, (queryWrapper, filter) -> queryWrapper.between(filter.getColumn(), ((List<?>) filter.getValue()).get(0), ((List<?>) filter.getValue()).get(1)));
        APPEND_QUERY_WRAPPER_MAP.put(QueryMethod.in, (queryWrapper, filter) -> queryWrapper.in(filter.getColumn(), filter.getValue()));
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

        //要判断一下是否为空，不然会报空指针异常
        if (!CollectionUtils.isEmpty(filters)) {
            filters.forEach(filter -> {
                // 如果是null就不处理
                if (EmptyUtils.isEmpty(filter.getValue())) {
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
                // 进行匹配
                sorts.forEach(sort -> queryWrapper.orderBy(StrUtil.isNotBlank(sort.getColumn()), sort.getAsc(), sort.getColumn()));
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
