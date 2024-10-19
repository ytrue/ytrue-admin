package com.ytrue.infra.mysql.query.util;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ytrue.infra.mysql.query.builder.QueryWrapperBuilder;
import com.ytrue.infra.mysql.query.entity.OrderBy;
import com.ytrue.infra.mysql.query.entity.QueryDefinition;
import com.ytrue.infra.mysql.query.entity.Where;
import com.ytrue.infra.mysql.query.enums.Operator;
import com.ytrue.infra.mysql.query.enums.QueryMethod;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 查询帮助工具类，提供构建 MyBatis-Plus QueryWrapper 的功能。
 */
@Slf4j
public class QueryHelp {

    /**
     * 构建 LambdaQueryWrapper。
     *
     * @param query 查询条件对象
     * @param <T>   查询对象类型
     * @return 构建好的 LambdaQueryWrapper
     */
    public static <T> LambdaQueryWrapper<T> builderlambdaQueryWrapper(Object query) {
        return QueryHelp.<T>buildQueryWrapper(query).lambda();
    }

    /**
     * 构建 QueryWrapper。
     *
     * @param query 查询条件对象
     * @param <T>   查询对象类型
     * @return 构建好的 QueryWrapper
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
     * 生成 QueryDefinition 对象，提取查询条件和排序信息。
     *
     * @param query 查询条件对象
     * @return 生成的 QueryDefinition
     */
    public static QueryDefinition generateQueryDefinition(Object query) {
        Class<?> clazz = query.getClass();
        // 获取所有字段
        List<Field> fields = FieldUtil.getAllFields(clazz);
        // 存储的 where 条件
        LinkedHashSet<Where> whereSets = new LinkedHashSet<>();
        // 存储的 order by 条件
        LinkedHashSet<OrderBy> orderBySets = new LinkedHashSet<>();

        try {
            // 循环处理每个字段
            for (Field field : fields) {
                // 设置对象的访问权限，保证对 private 属性的访问
                field.setAccessible(true);

                // 处理 Where 注解
                com.ytrue.infra.mysql.query.annotation.Where[] whereAnnotations = field.getAnnotationsByType(com.ytrue.infra.mysql.query.annotation.Where.class);
                for (com.ytrue.infra.mysql.query.annotation.Where w : whereAnnotations) {
                    if (w == null) {
                        continue;
                    }
                    // 获取条件信息
                    String alias = w.alias();
                    QueryMethod condition = w.condition();
                    Operator operator = w.operator();
                    Object value = field.get(query);
                    String column = getColumn(w.column(), field.getName());

                    // 构建 Where 条件
                    whereSets.add(new Where(column, condition, value, alias, operator));
                }

                // 处理 OrderBy 注解
                for (com.ytrue.infra.mysql.query.annotation.OrderBy o : getSortedOrderByAnnotations(fields)) {
                    if (o == null) {
                        continue;
                    }
                    // 获取排序信息
                    String alias = o.alias();
                    boolean asc = o.asc();
                    String column = getColumn(o.column(), field.getName());

                    // 构建 OrderBy 条件
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
     * 获取列名，若列名为空则将字段名转换为下划线格式。
     *
     * @param columnName 列名
     * @param fieldName  字段名
     * @return 最终的列名
     */
    private static String getColumn(String columnName, String fieldName) {
        String column = columnName;
        if (StrUtil.isBlank(columnName)) {
            // 将小驼峰命名转换为下划线命名
            column = StrUtil.toUnderlineCase(fieldName);
        }
        return column;
    }

    /**
     * 获取所有字段上的 OrderBy 注解，并按照 index 升序排序。
     *
     * @param fields 目标字段列表
     * @return 排序后的 OrderBy 注解列表
     */
    public static List<com.ytrue.infra.mysql.query.annotation.OrderBy> getSortedOrderByAnnotations(List<Field> fields) {
        return fields.stream()
                .flatMap(field -> Arrays.stream(field.getAnnotationsByType(com.ytrue.infra.mysql.query.annotation.OrderBy.class)))
                .sorted(Comparator.comparingInt(com.ytrue.infra.mysql.query.annotation.OrderBy::index))  // 按 index 升序排序
                .collect(Collectors.toList());
    }
}
