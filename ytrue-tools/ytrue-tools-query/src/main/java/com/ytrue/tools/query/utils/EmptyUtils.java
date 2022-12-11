package com.ytrue.tools.query.utils;


import java.util.Collection;
import java.util.Map;

/**
 * @author ytrue
 * @date 2022/7/13 11:26
 * @description EmptyUtils
 */
public class EmptyUtils {

    /**
     * 禁止默认构造函数以实现不可实例化
     */
    private EmptyUtils() {
        throw new AssertionError();
    }

    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof int[]) {
            return ((int[]) object).length == 0;
        }
        if (object instanceof double[]) {
            return ((double[]) object).length == 0;
        }
        if (object instanceof long[]) {
            return ((long[]) object).length == 0;
        }
        if (object instanceof byte[]) {
            return ((byte[]) object).length == 0;
        }
        if (object instanceof short[]) {
            return ((short[]) object).length == 0;
        }
        if (object instanceof float[]) {
            return ((float[]) object).length == 0;
        }
        if (object instanceof char[]) {
            return ((char[]) object).length == 0;
        }
        if (object instanceof Object[]) {
            return ((Object[]) object).length == 0;
        }
        if (object instanceof CharSequence) {
            return ((CharSequence) object).length() == 0;
        }
        if (object instanceof Collection) {
            return ((Collection) object).isEmpty();
        }
        if (object instanceof Map) {
            return ((Map) object).isEmpty();
        }
        return false;
    }

    public static boolean isNoEmpty(Object object) {
        return !isEmpty(object);
    }
}
