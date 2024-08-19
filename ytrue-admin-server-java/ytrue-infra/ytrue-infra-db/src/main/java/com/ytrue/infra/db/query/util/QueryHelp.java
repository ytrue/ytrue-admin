package com.ytrue.infra.db.query.util;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.infra.db.query.builder.QueryWrapperBuilder;
import com.ytrue.infra.db.query.entity.OrderBy;
import com.ytrue.infra.db.query.entity.QueryDefinition;
import com.ytrue.infra.db.query.entity.Where;
import com.ytrue.infra.db.query.enums.Operator;
import com.ytrue.infra.db.query.enums.QueryMethod;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ytrue
 * @date 2022/7/13 11:39
 * @description QueryHelp
 */
@Slf4j
public class QueryHelp {

    public static <T> LambdaQueryWrapper<T> builderlambdaQueryWrapper(Object query) {
        return QueryHelp.<T>buildQueryWrapper(query).lambda();
    }

    /**
     * 构建 QueryWrapper
     *
     * @param query
     * @param <T>
     * @return
     */
    public static <T> QueryWrapper<T> buildQueryWrapper(Object query) {
        QueryWrapper<T> queryWrapper = Wrappers.query();

        // 进行构建
        if (query instanceof QueryDefinition definition) {
            QueryWrapperBuilder.buildQueryWrapper(queryWrapper, definition);
        } else if (query instanceof Where where) {
            QueryWrapperBuilder.applyWhereCondition(queryWrapper, where);
        } else if (query instanceof OrderBy orderBy) {
            QueryWrapperBuilder.applyOrderBy(queryWrapper, orderBy);
        } else {
            QueryWrapperBuilder.buildQueryWrapper(queryWrapper, generateQueryDefinition(query));
        }
        return queryWrapper;
    }


    /**
     * 生成 queryDefinition
     *
     * @param query
     * @return
     */
    public static QueryDefinition generateQueryDefinition(Object query) {
        Class<?> clazz = query.getClass();
        // 获取所有的字段,其实这里可以去拿get is 方法 参考mybatis这里暂时不做了
        List<Field> fields = FieldUtil.getAllFields(clazz);
        // 存储的where
        LinkedHashSet<Where> whereSets = new LinkedHashSet<>();
        // 存储的order by
        LinkedHashSet<OrderBy> orderBySets = new LinkedHashSet<>();

        try {
            // 循环处理
            for (Field field : fields) {
                // 设置对象的访问权限，保证对private的属性的访
                field.setAccessible(true);
                //  Query q = field.getAnnotation(Query.class);
                com.ytrue.infra.db.query.annotation.Where[] whereAnnotations = field.getAnnotationsByType(com.ytrue.infra.db.query.annotation.Where.class);
                for (com.ytrue.infra.db.query.annotation.Where w : whereAnnotations) {
                    if (w == null) {
                        continue;
                    }
                    // 获取别名
                    String alias = w.alias();
                    // 获取条件类型
                    QueryMethod condition = w.condition();
                    // 获取连接条件
                    Operator operator = w.operator();
                    // 获取值
                    Object value = field.get(query);
                    // 获取字段
                    String column = getColumn(w.column(), field.getName());
                    // 开始构建条件
                    whereSets.add(new Where(column, condition, value, alias, operator));
                }

                // 处理order by
                for (com.ytrue.infra.db.query.annotation.OrderBy o : getSortedOrderByAnnotations(fields)) {
                    if (o == null) {
                        continue;
                    }
                    // 获取别名
                    String alias = o.alias();
                    // 获取值
                    boolean asc = o.asc();
                    // 获取字段
                    String column = getColumn(o.column(), field.getName());
                    // 开始 构建 orderBy
                    orderBySets.add(new OrderBy(column, asc, alias));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        QueryDefinition queryDefinition = new QueryDefinition();
        queryDefinition.setWhereSets(whereSets);
        queryDefinition.setOrderSets(orderBySets);
        return queryDefinition;
    }


    /**
     * 获取 colum
     *
     * @param columnName
     * @param fieldName
     * @return
     */
    private static String getColumn(String columnName, String fieldName) {
        // 存在的话,就拿这个，不进行处理
        String column = columnName;
        if (StrUtil.isBlank(columnName)) {
            // 小驼峰转下划线
            column = fieldName;
            column = StrUtil.toUnderlineCase(column);
        }

        return column;
    }

    /**
     * 获取所有字段上的 OrderBy 注解，并按照 index 升序排序
     *
     * @param fields 目标字段列表
     * @return 排序后的 OrderBy 注解列表
     */
    public static List<com.ytrue.infra.db.query.annotation.OrderBy> getSortedOrderByAnnotations(List<Field> fields) {
        return fields.stream()
                .flatMap(field -> Arrays.stream(field.getAnnotationsByType(com.ytrue.infra.db.query.annotation.OrderBy.class)))
                .sorted(Comparator.comparingInt(com.ytrue.infra.db.query.annotation.OrderBy::index))  // 按 index 升序排序
                .collect(Collectors.toList());
    }
}
