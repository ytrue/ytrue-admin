package com.ytrue.infra.db.query.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ytrue.infra.db.query.additional.AdditionalQueryWrapper;
import com.ytrue.infra.db.query.annotation.Query;
import com.ytrue.infra.db.query.entity.Filter;
import com.ytrue.infra.db.query.entity.QueryEntity;
import com.ytrue.infra.db.query.enums.Operator;
import com.ytrue.infra.db.query.enums.QueryMethod;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author ytrue
 * @date 2022/7/13 11:39
 * @description QueryHelp
 */
@Slf4j
public class QueryHelp {


    public static <T> LambdaQueryWrapper<T> lambdaQueryWrapperBuilder(Object query) {
        return QueryHelp.<T>queryWrapperBuilder(query).lambda();
    }

    /**
     * po or queryEntity or Set<Filter> or Set<Sort>  =>> QueryWrapper
     *
     * @param query
     * @param <T>
     * @return
     */
    public static <T> QueryWrapper<T> queryWrapperBuilder(Object query) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        AdditionalQueryWrapper additionalQueryWrapper = new AdditionalQueryWrapper();
        // 进行构建
        if (query instanceof QueryEntity queryEntity) {
            additionalQueryWrapper.queryWrapperBuilder(queryWrapper, queryEntity);
            // 是 set<Filter> 就处理
        } else if (query instanceof Set<?> set) {
            additionalQueryWrapper.queryWrapperBuilder(queryWrapper, set);
        } else {
            queryWrapperBuilder(queryWrapper, query);
        }
        return queryWrapper;
    }


    /**
     * po =>> QueryWrapper
     *
     * @param queryWrapper
     * @param query
     */
    public static void queryWrapperBuilder(QueryWrapper<?> queryWrapper, Object query) {
        // 追加
        AdditionalQueryWrapper additionalQueryWrapper = new AdditionalQueryWrapper();
        // 构建
        additionalQueryWrapper.queryWrapperBuilder(queryWrapper, queryEntityBuilder(query));
    }


    /**
     * po =>> fields
     *
     * @param query
     * @return
     */
    public static QueryEntity queryEntityBuilder(Object query) {
        return QueryEntity.builder().filters(filterBuilder(query)).build();
    }

    /**
     * po =>> field
     *
     * @param query
     * @return
     */
    public static LinkedHashSet<Filter> filterBuilder(Object query) {
        Class<?> clazz = query.getClass();
        // 存储的
        LinkedHashSet<Filter> filterList = new LinkedHashSet<>();
        // 获取所有的字段
        List<Field> fields = getAllFields(clazz, new ArrayList<>());
        try {
            // 循环处理
            for (Field field : fields) {
                // 设置对象的访问权限，保证对private的属性的访
                field.setAccessible(true);
                //  Query q = field.getAnnotation(Query.class);
                Query[] queryList = field.getAnnotationsByType(Query.class);

                for (Query q : queryList) {
                    if (q != null) {
                        // 获取字段
                        String column = StrUtil.isBlank(q.column()) ? field.getName() : q.column();
                        // 获取别名
                        String alias = q.alias();
                        // 获取条件类型
                        QueryMethod condition = q.condition();

                        // 获取连接条件
                        Operator operator = q.operator();
                        // 获取值
                        Object value = field.get(query);
                        // 如果是null就不处理
                        if (ObjectUtil.isNull(value)) {
                            continue;
                        }
                        // 构建
                        // 小驼峰转下划线
                        column = StrUtil.toUnderlineCase(column);
                        filterList.add(new Filter(column, condition, value, alias, operator));
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return filterList;
    }


    /**
     * 获取所以的字段,包含父类
     *
     * @param clazz
     * @param fields
     * @return
     */
    private static List<Field> getAllFields(Class clazz, List<Field> fields) {
        if (clazz != null) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            getAllFields(clazz.getSuperclass(), fields);
        }
        return fields;
    }
}
