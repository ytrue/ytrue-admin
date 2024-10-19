package com.ytrue.infra.mysql.query.builder;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ytrue.infra.mysql.query.entity.OrderBy;
import com.ytrue.infra.mysql.query.entity.QueryDefinition;
import com.ytrue.infra.mysql.query.entity.Where;
import com.ytrue.infra.mysql.query.enums.Operator;
import com.ytrue.infra.mysql.query.enums.QueryMethod;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * 构建 MyBatis Plus 的 QueryWrapper 的工具类。
 * <p>
 * 该类用于根据给定的查询定义构建 SQL 查询条件和排序。
 * </p>
 */
public class QueryWrapperBuilder {

    private static final HashMap<QueryMethod, BiConsumer<QueryWrapper<?>, Where>> QUERY_METHOD_ACTIONS = new HashMap<>();

    static {
        // 初始化查询方法与相应操作的映射
        QUERY_METHOD_ACTIONS.put(QueryMethod.eq, (queryWrapper, filter) -> queryWrapper.eq(filter.getColumnAlias(), filter.getValue()));
        QUERY_METHOD_ACTIONS.put(QueryMethod.ne, (queryWrapper, filter) -> queryWrapper.ne(filter.getColumnAlias(), filter.getValue()));

        QUERY_METHOD_ACTIONS.put(QueryMethod.gt, (queryWrapper, filter) -> queryWrapper.gt(filter.getColumnAlias(), filter.getValue()));
        QUERY_METHOD_ACTIONS.put(QueryMethod.lt, (queryWrapper, filter) -> queryWrapper.lt(filter.getColumnAlias(), filter.getValue()));
        QUERY_METHOD_ACTIONS.put(QueryMethod.ge, (queryWrapper, filter) -> queryWrapper.ge(filter.getColumnAlias(), filter.getValue()));
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
     * 根据 QueryDefinition 构建 QueryWrapper。
     *
     * @param queryWrapper 目标 QueryWrapper
     * @param queryDefinition 查询定义
     */
    public static void buildQueryWrapper(QueryWrapper<?> queryWrapper, QueryDefinition queryDefinition) {
        // 检查 queryWrapper 是否为 null
        if (queryWrapper == null) {
            throw new NullPointerException("queryWrapper is null");
        }

        LinkedHashSet<Where> whereSets = queryDefinition.getWhereSets();
        // 判断 whereSets 是否为空，以避免空指针异常
        if (CollUtil.isNotEmpty(whereSets)) {
            whereSets.stream().filter(Objects::nonNull).forEach(where -> applyWhereCondition(queryWrapper, where));
        }

        LinkedHashSet<OrderBy> orderSets = queryDefinition.getOrderSets();
        // 判断 orderSets 是否为空，以避免空指针异常
        if (CollUtil.isNotEmpty(orderSets)) {
            orderSets.stream().filter(Objects::nonNull).forEach(orderBy -> applyOrderBy(queryWrapper, orderBy));
        }
    }

    /**
     * 应用 where 条件到 QueryWrapper。
     *
     * @param queryWrapper 目标 QueryWrapper
     * @param where 查询条件
     */
    public static void applyWhereCondition(QueryWrapper<?> queryWrapper, Where where) {
        // 如果 where 的值为 null，则不处理
        if (ObjectUtil.isNull(where.getValue())) {
            return;
        }

        // 检查 where 的值是否合法
        checkWhereValue(where);

        // 获取处理器并执行
        BiConsumer<QueryWrapper<?>, Where> action = QUERY_METHOD_ACTIONS.get(where.getCondition());
        Assert.notNull(action, "类型匹配错误");

        // 处理 or 的情况
        queryWrapper.or(where.getOperator().equals(Operator.or));
        action.accept(queryWrapper, where);
    }

    /**
     * 应用排序条件到 QueryWrapper。
     *
     * @param queryWrapper 目标 QueryWrapper
     * @param orderBy 排序条件
     */
    public static void applyOrderBy(QueryWrapper<?> queryWrapper, OrderBy orderBy) {
        queryWrapper.orderBy(StrUtil.isNotBlank(orderBy.getColumnAlias()), orderBy.getAsc(), orderBy.getColumn());
    }

    /**
     * 检查 where 条件的值是否合法。
     *
     * @param where 查询条件
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
