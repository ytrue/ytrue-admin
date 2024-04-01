package com.ytrue.infra.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ytrue.infra.core.base.IServerResponseCode;
import com.ytrue.infra.core.excptions.AssertInvalidArgumentException;

import java.util.Collection;
import java.util.Objects;

/**
 * @author ytrue
 * @date 2022/4/21 16:44
 * @description 断言工具
 */
public class AssertUtil {

    public static void notNull(Object object, IServerResponseCode responseCode) {
        if (Objects.isNull(object)) {
            reportInvalidArgument(responseCode);
        }
    }

    public static void isNull(Object object, IServerResponseCode responseCode) {
        if (Objects.nonNull(object)) {
            reportInvalidArgument(responseCode);
        }
    }

    public static void objectEquals(Object object1, Object object2, IServerResponseCode responseCode) {
        if (ObjectUtil.notEqual(object1, object2)) {
            reportInvalidArgument(responseCode);
        }
    }

    public static void objectNotEquals(Object object1, Object object2, IServerResponseCode responseCode) {
        if (ObjectUtil.equal(object1, object2)) {
            reportInvalidArgument(responseCode);
        }
    }


    public static void numberEquals(Number number1, Number number2, IServerResponseCode responseCode) {
        if (!NumberUtil.equals(number1, number2)) {
            reportInvalidArgument(responseCode);
        }
    }

    public static void numberNotEquals(Number number1, Number number2, IServerResponseCode responseCode) {
        if (NumberUtil.equals(number1, number2)) {
            reportInvalidArgument(responseCode);
        }
    }


    public static void lessThanEq(double value, double limit, IServerResponseCode responseCode) {
        if (value > limit) {
            reportInvalidArgument(responseCode);
        }
    }

    public static void lessThan(double value, double limit, IServerResponseCode responseCode) {
        if (value >= limit) {
            reportInvalidArgument(responseCode);
        }
    }

    public static void greaterThanEq(double value, double limit, IServerResponseCode responseCode) {
        if (value < limit) {
            reportInvalidArgument(responseCode);
        }
    }

    public static void greaterThan(double value, double limit, IServerResponseCode responseCode) {
        if (value <= limit) {
            reportInvalidArgument(responseCode);
        }
    }


    public static void isTrue(boolean expression, IServerResponseCode responseCode) {
        if (!expression) {
            reportInvalidArgument(responseCode);
        }
    }

    public static void isFalse(boolean expression, IServerResponseCode responseCode) {
        if (expression) {
            reportInvalidArgument(responseCode);
        }
    }


    public static void strIsEmpty(String value, IServerResponseCode responseCode) {
        if (StrUtil.isNotEmpty(value)) {
            reportInvalidArgument(responseCode);
        }
    }


    public static void strIsNotEmpty(String value, IServerResponseCode responseCode) {
        if (StrUtil.isEmpty(value)) {
            reportInvalidArgument(responseCode);
        }
    }

    public static void collectionIsNotEmpty(Collection<?> collection, IServerResponseCode responseCode) {
        if (CollUtil.isEmpty(collection)) {
            reportInvalidArgument(responseCode);
        }
    }


    public static void collectionIsEmpty(Collection<?> collection, IServerResponseCode responseCode) {
        if (CollUtil.isNotEmpty(collection)) {
            reportInvalidArgument(responseCode);
        }
    }

    private static void reportInvalidArgument(IServerResponseCode responseCode) {
        throw new AssertInvalidArgumentException(responseCode);
    }
}
