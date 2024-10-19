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
 * AssertUtil 是一个断言工具类，提供了多种断言方法用于验证参数的有效性。
 * 该类的方法可用于检查对象是否为 null、数字比较、集合和字符串的有效性等。
 */
public class AssertUtil {

    /**
     * 断言对象不为 null。
     *
     * @param object      被检查的对象
     * @param responseCode 失败时返回的响应信息
     */
    public static void notNull(Object object, IServerResponseInfo responseCode) {
        if (Objects.isNull(object)) {
            reportInvalidArgument(responseCode);
        }
    }

    /**
     * 断言对象为 null。
     *
     * @param object      被检查的对象
     * @param responseCode 失败时返回的响应信息
     */
    public static void isNull(Object object, IServerResponseInfo responseCode) {
        if (Objects.nonNull(object)) {
            reportInvalidArgument(responseCode);
        }
    }

    /**
     * 断言两个对象相等。
     *
     * @param object1    第一个对象
     * @param object2    第二个对象
     * @param responseCode 失败时返回的响应信息
     */
    public static void objectEquals(Object object1, Object object2, IServerResponseInfo responseCode) {
        if (ObjectUtil.notEqual(object1, object2)) {
            reportInvalidArgument(responseCode);
        }
    }

    /**
     * 断言两个对象不相等。
     *
     * @param object1    第一个对象
     * @param object2    第二个对象
     * @param responseCode 失败时返回的响应信息
     */
    public static void objectNotEquals(Object object1, Object object2, IServerResponseInfo responseCode) {
        if (ObjectUtil.equal(object1, object2)) {
            reportInvalidArgument(responseCode);
        }
    }

    /**
     * 断言两个数字相等。
     *
     * @param number1    第一个数字
     * @param number2    第二个数字
     * @param responseCode 失败时返回的响应信息
     */
    public static void numberEquals(Number number1, Number number2, IServerResponseInfo responseCode) {
        if (!NumberUtil.equals(number1, number2)) {
            reportInvalidArgument(responseCode);
        }
    }

    /**
     * 断言两个数字不相等。
     *
     * @param number1    第一个数字
     * @param number2    第二个数字
     * @param responseCode 失败时返回的响应信息
     */
    public static void numberNotEquals(Number number1, Number number2, IServerResponseInfo responseCode) {
        if (NumberUtil.equals(number1, number2)) {
            reportInvalidArgument(responseCode);
        }
    }

    /**
     * 断言数值小于等于指定限制。
     *
     * @param value      被检查的数值
     * @param limit      限制值
     * @param responseCode 失败时返回的响应信息
     * @param <T>       数值类型，需实现 Number 和 Comparable 接口
     */
    public static <T extends Number & Comparable<? super T>> void lessThanEq(T value, T limit, IServerResponseInfo responseCode) {
        if (value.compareTo(limit) > 0) {
            reportInvalidArgument(responseCode);
        }
    }

    /**
     * 断言数值小于指定限制。
     *
     * @param value      被检查的数值
     * @param limit      限制值
     * @param responseCode 失败时返回的响应信息
     * @param <T>       数值类型，需实现 Number 和 Comparable 接口
     */
    public static <T extends Number & Comparable<? super T>> void lessThan(T value, T limit, IServerResponseInfo responseCode) {
        if (value.compareTo(limit) >= 0) {
            reportInvalidArgument(responseCode);
        }
    }

    /**
     * 断言数值大于等于指定限制。
     *
     * @param value      被检查的数值
     * @param limit      限制值
     * @param responseCode 失败时返回的响应信息
     * @param <T>       数值类型，需实现 Number 和 Comparable 接口
     */
    public static <T extends Number & Comparable<? super T>> void greaterThanEq(T value, T limit, IServerResponseInfo responseCode) {
        if (value.compareTo(limit) < 0) {
            reportInvalidArgument(responseCode);
        }
    }

    /**
     * 断言数值大于指定限制。
     *
     * @param value      被检查的数值
     * @param limit      限制值
     * @param responseCode 失败时返回的响应信息
     * @param <T>       数值类型，需实现 Number 和 Comparable 接口
     */
    public static <T extends Number & Comparable<? super T>> void greaterThan(T value, T limit, IServerResponseInfo responseCode) {
        if (value.compareTo(limit) <= 0) {
            reportInvalidArgument(responseCode);
        }
    }

    /**
     * 断言表达式为 true。
     *
     * @param expression  被检查的表达式
     * @param responseCode 失败时返回的响应信息
     */
    public static void isTrue(boolean expression, IServerResponseInfo responseCode) {
        if (!expression) {
            reportInvalidArgument(responseCode);
        }
    }

    /**
     * 断言表达式为 false。
     *
     * @param expression  被检查的表达式
     * @param responseCode 失败时返回的响应信息
     */
    public static void isFalse(boolean expression, IServerResponseInfo responseCode) {
        if (expression) {
            reportInvalidArgument(responseCode);
        }
    }

    /**
     * 断言字符串为空。
     *
     * @param value      被检查的字符串
     * @param responseCode 失败时返回的响应信息
     */
    public static void strIsEmpty(CharSequence value, IServerResponseInfo responseCode) {
        if (StrUtil.isNotEmpty(value)) {
            reportInvalidArgument(responseCode);
        }
    }

    /**
     * 断言字符串不为空。
     *
     * @param value      被检查的字符串
     * @param responseCode 失败时返回的响应信息
     */
    public static void strIsNotEmpty(CharSequence value, IServerResponseInfo responseCode) {
        if (StrUtil.isEmpty(value)) {
            reportInvalidArgument(responseCode);
        }
    }

    /**
     * 断言集合不为空。
     *
     * @param collection  被检查的集合
     * @param responseCode 失败时返回的响应信息
     */
    public static void collectionIsNotEmpty(Collection<?> collection, IServerResponseInfo responseCode) {
        if (CollUtil.isEmpty(collection)) {
            reportInvalidArgument(responseCode);
        }
    }

    /**
     * 断言集合为空。
     *
     * @param collection  被检查的集合
     * @param responseCode 失败时返回的响应信息
     */
    public static void collectionIsEmpty(Collection<?> collection, IServerResponseInfo responseCode) {
        if (CollUtil.isNotEmpty(collection)) {
            reportInvalidArgument(responseCode);
        }
    }

    /**
     * 报告无效的参数并抛出 AssertInvalidArgumentException 异常。
     *
     * @param responseCode 失败时返回的响应信息
     */
    private static void reportInvalidArgument(IServerResponseInfo responseCode) {
        throw new AssertInvalidArgumentException(responseCode);
    }
}
