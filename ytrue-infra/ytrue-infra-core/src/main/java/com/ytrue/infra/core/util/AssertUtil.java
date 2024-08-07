package com.ytrue.infra.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ytrue.infra.core.base.IServerResponseInfo;
import com.ytrue.infra.core.excptions.AssertInvalidArgumentException;

import java.util.Collection;
import java.util.Objects;

/**
 * @author ytrue
 * @date 2022/4/21 16:44
 * @description 断言工具
 */
public class AssertUtil {

    public static void notNull(Object object, IServerResponseInfo responseCode) {
        if (Objects.isNull(object)) {
            reportInvalidArgument(responseCode);
        }
    }

    public static void isNull(Object object, IServerResponseInfo responseCode) {
        if (Objects.nonNull(object)) {
            reportInvalidArgument(responseCode);
        }
    }

    public static void objectEquals(Object object1, Object object2, IServerResponseInfo responseCode) {
        if (ObjectUtil.notEqual(object1, object2)) {
            reportInvalidArgument(responseCode);
        }
    }

    public static void objectNotEquals(Object object1, Object object2, IServerResponseInfo responseCode) {
        if (ObjectUtil.equal(object1, object2)) {
            reportInvalidArgument(responseCode);
        }
    }


    public static void numberEquals(Number number1, Number number2, IServerResponseInfo responseCode) {
        if (!NumberUtil.equals(number1, number2)) {
            reportInvalidArgument(responseCode);
        }
    }

    public static void numberNotEquals(Number number1, Number number2, IServerResponseInfo responseCode) {
        if (NumberUtil.equals(number1, number2)) {
            reportInvalidArgument(responseCode);
        }
    }


    public static <T extends Number & Comparable<? super T>> void lessThanEq(T value, T limit, IServerResponseInfo responseCode) {
        if (value.compareTo(limit) > 0) {
            reportInvalidArgument(responseCode);
        }
    }


    public static <T extends Number & Comparable<? super T>> void lessThan(T value, T limit, IServerResponseInfo responseCode) {
        if (value.compareTo(limit) >= 0) {
            reportInvalidArgument(responseCode);
        }
    }

    public static <T extends Number & Comparable<? super T>> void greaterThanEq(T value, T limit, IServerResponseInfo responseCode) {
        if (value.compareTo(limit) < 0) {
            reportInvalidArgument(responseCode);
        }
    }

    public static <T extends Number & Comparable<? super T>> void greaterThan(T value, T limit, IServerResponseInfo responseCode) {
        if (value.compareTo(limit) <= 0) {
            reportInvalidArgument(responseCode);
        }
    }


    public static void isTrue(boolean expression, IServerResponseInfo responseCode) {
        if (!expression) {
            reportInvalidArgument(responseCode);
        }
    }

    public static void isFalse(boolean expression, IServerResponseInfo responseCode) {
        if (expression) {
            reportInvalidArgument(responseCode);
        }
    }


    public static void strIsEmpty(CharSequence value, IServerResponseInfo responseCode) {
        if (StrUtil.isNotEmpty(value)) {
            reportInvalidArgument(responseCode);
        }
    }


    public static void strIsNotEmpty(CharSequence value, IServerResponseInfo responseCode) {
        if (StrUtil.isEmpty(value)) {
            reportInvalidArgument(responseCode);
        }
    }

    public static void collectionIsNotEmpty(Collection<?> collection, IServerResponseInfo responseCode) {
        if (CollUtil.isEmpty(collection)) {
            reportInvalidArgument(responseCode);
        }
    }


    public static void collectionIsEmpty(Collection<?> collection, IServerResponseInfo responseCode) {
        if (CollUtil.isNotEmpty(collection)) {
            reportInvalidArgument(responseCode);
        }
    }

    private static void reportInvalidArgument(IServerResponseInfo responseCode) {
        throw new AssertInvalidArgumentException(responseCode);
    }
}
