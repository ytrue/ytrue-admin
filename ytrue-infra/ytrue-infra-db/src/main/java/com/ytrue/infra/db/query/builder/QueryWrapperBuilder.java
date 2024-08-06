package com.ytrue.infra.db.query.builder;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ytrue.infra.db.query.entity.OrderBy;
import com.ytrue.infra.db.query.entity.QueryDefinition;
import com.ytrue.infra.db.query.entity.Where;
import com.ytrue.infra.db.query.enums.Operator;
import com.ytrue.infra.db.query.enums.QueryMethod;

import java.util.*;
import java.util.function.BiConsumer;

public class QueryWrapperBuilder {

    private static final HashMap<QueryMethod, BiConsumer<QueryWrapper<?>, Where>> QUERY_METHOD_ACTIONS = new HashMap<>();

    static {
        QUERY_METHOD_ACTIONS.put(QueryMethod.eq, (queryWrapper, filter) -> queryWrapper.eq(filter.getColumnAlias(), filter.getValue()));
        QUERY_METHOD_ACTIONS.put(QueryMethod.ne, (queryWrapper, filter) -> queryWrapper.ne(filter.getColumnAlias(), filter.getValue()));

        QUERY_METHOD_ACTIONS.put(QueryMethod.gt, (queryWrapper, filter) -> queryWrapper.gt(filter.getColumnAlias(), filter.getValue()));
        QUERY_METHOD_ACTIONS.put(QueryMethod.lt, (queryWrapper, filter) -> queryWrapper.lt(filter.getColumnAlias(), filter.getValue()));
        QUERY_METHOD_ACTIONS.put(QueryMethod.ge, (queryWrapper, filter) -> queryWrapper.gt(filter.getColumnAlias(), filter.getValue()));
        QUERY_METHOD_ACTIONS.put(QueryMethod.le, (queryWrapper, filter) -> queryWrapper.le(filter.getColumnAlias(), filter.getValue()));


        QUERY_METHOD_ACTIONS.put(QueryMethod.like, (queryWrapper, filter) -> queryWrapper.like(filter.getColumnAlias(), filter.getValue()));
        QUERY_METHOD_ACTIONS.put(QueryMethod.likeLeft, (queryWrapper, filter) -> queryWrapper.likeLeft(filter.getColumnAlias(), filter.getValue()));
        QUERY_METHOD_ACTIONS.put(QueryMethod.likeRight, (queryWrapper, filter) -> queryWrapper.likeRight(filter.getColumnAlias(), filter.getValue()));

        QUERY_METHOD_ACTIONS.put(QueryMethod.between, (queryWrapper, filter) -> queryWrapper.between(filter.getColumnAlias(), ((List<?>) filter.getValue()).get(0), ((List<?>) filter.getValue()).get(1)));
        QUERY_METHOD_ACTIONS.put(QueryMethod.notBetween, (queryWrapper, filter) -> queryWrapper.notBetween(filter.getColumnAlias(), ((List<?>) filter.getValue()).get(0), ((List<?>) filter.getValue()).get(1)));

        QUERY_METHOD_ACTIONS.put(QueryMethod.in, (queryWrapper, filter) -> queryWrapper.in(filter.getColumnAlias(), filter.getValue()));
        QUERY_METHOD_ACTIONS.put(QueryMethod.notin, (queryWrapper, filter) -> queryWrapper.notIn(filter.getColumnAlias(), filter.getValue()));
    }

    /**
     * QueryWrapper 构建
     *
     * @param queryWrapper
     * @param queryDefinition
     */
    public static void buildQueryWrapper(QueryWrapper<?> queryWrapper, QueryDefinition queryDefinition) {
        // 空处理
        if (queryWrapper == null) {
            throw new NullPointerException("queryWrapper  is null");
        }

        LinkedHashSet<Where> whereSets = queryDefinition.getWhereSets();
        //要判断一下是否为空，不然会报空指针异常
        if (CollUtil.isNotEmpty(whereSets)) {
            whereSets.stream().filter(Objects::nonNull).forEach(where -> applyWhereCondition(queryWrapper, where));
        }


        LinkedHashSet<OrderBy> orderSets = queryDefinition.getOrderSets();
        //要判断一下是否为空，不然会报空指针异常
        if (CollUtil.isNotEmpty(orderSets)) {
            orderSets.stream().filter(Objects::nonNull).forEach(orderBy -> applyOrderBy(queryWrapper, orderBy));
        }
    }


    /**
     * 构建 where
     *
     * @param queryWrapper
     * @param where
     */
    public static void applyWhereCondition(QueryWrapper<?> queryWrapper, Where where) {
        // 如果是null就不处理
        if (ObjectUtil.isNull(where.getValue())) {
            return;
        }

        // 检查where value
        checkWhereValue(where);

        // 获取处理器
        BiConsumer<QueryWrapper<?>, Where> action = QUERY_METHOD_ACTIONS.get(where.getCondition());
        Assert.notNull(action, "类型匹配错误");
        // or的处理
        queryWrapper.or(where.getOperator().equals(Operator.or));
        action.accept(queryWrapper, where);
    }

    /**
     * 构建 order by
     *
     * @param queryWrapper
     * @param orderBy
     */
    public static void applyOrderBy(QueryWrapper<?> queryWrapper, OrderBy orderBy) {
        queryWrapper.orderBy(StrUtil.isNotBlank(orderBy.getColumnAlias()), orderBy.getAsc(), orderBy.getColumn());
    }

    /**
     * 检查 where value
     *
     * @param where
     */
    private static void checkWhereValue(Where where) {
        Object value = where.getValue();
        QueryMethod queryMethod = where.getCondition();

        switch (queryMethod) {
            case between:
            case notBetween:
                if (value instanceof Collection<?> collection) {
                    if (collection.size() != 2) {
                        throw new IllegalArgumentException("For 'between' or 'notBetween', the value must be a Collection with exactly two elements.");
                    }
                } else if (value.getClass().isArray()) {
                    Object[] arrayValue = (Object[]) value;
                    if (arrayValue.length != 2) {
                        throw new IllegalArgumentException("For 'between' or 'notBetween', the value must be an array with exactly two elements.");
                    }
                    // 转换为 List
                    where.setValue(Arrays.asList(arrayValue));
                } else {
                    throw new IllegalArgumentException("For 'between' or 'notBetween', the value must be a Collection or an array with exactly two elements.");
                }
                break;

            case in:
            case notin:
                if (value instanceof Collection<?>) {
                    // 如果已经是集合，直接返回
                    return;
                } else if (value.getClass().isArray()) {
                    // 转换为 List
                    where.setValue(Arrays.asList((Object[]) value));
                } else {
                    throw new IllegalArgumentException("For 'in' or 'notIn', the value must be a Collection or an array.");
                }
                break;

            default:
                // 对于其他 QueryMethod，暂不做处理
                break;
        }
    }


}
