package com.ytrue.tools.query.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.reflect.Field;

/**
 * @author ytrue
 * @date 2022/7/13 11:39
 * @description QueryWrapperUtils
 */
public class QueryWrapperUtils {


    public static <T> QueryWrapper<T> queryWrapperBuilder(T t) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapperBuilder(queryWrapper, t);
        return queryWrapper;
    }

    /**
     * 处理
     *
     * @param queryWrapper
     * @param t
     * @param <T>
     */
    public static <T> void queryWrapperBuilder(QueryWrapper<T> queryWrapper, T t) {

        Class<?> clazz = t.getClass();

        // 循环它的字段
        for (Field field : clazz.getDeclaredFields()) {
            // TODO 待完善
            // System.out.println(field.getName());
        }
    }
}
