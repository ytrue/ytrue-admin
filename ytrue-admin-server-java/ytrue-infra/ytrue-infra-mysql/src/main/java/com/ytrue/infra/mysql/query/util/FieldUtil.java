package com.ytrue.infra.mysql.query.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 工具类，用于处理与反射相关的字段操作。
 */
public class FieldUtil {

    /**
     * 获取指定类及其所有父类的字段，父类字段优先。
     *
     * @param clazz 当前类
     * @return 包含所有字段的列表，包括父类的字段
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        collectFields(clazz, fields);
        return fields;
    }

    /**
     * 递归收集字段，优先获取父类的字段。
     *
     * @param clazz 当前类
     * @param fields 存储字段的列表
     */
    private static void collectFields(Class<?> clazz, List<Field> fields) {
        if (clazz == null) {
            return;
        }

        // 递归获取父类字段
        collectFields(clazz.getSuperclass(), fields);

        // 添加当前类的字段
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
    }
}
