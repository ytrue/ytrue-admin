package com.ytrue.common.utils;

import com.ytrue.common.base.IBaseExceptionCode;
import com.ytrue.common.excptions.AssertInvalidArgumentException;

import java.util.Objects;

/**
 * @author ytrue
 * @date 2022/4/21 16:44
 * @description 断言工具
 */
public class AssertUtils {


    /**
     * 断言 object 不为空
     *
     * @param object
     * @param responseCode
     */
    public static void notNull(Object object, IBaseExceptionCode responseCode) {
        if (Objects.isNull(object)) {
            reportInvalidArgument(responseCode);
        }
    }

    /**
     * 断言 object 为空
     *
     * @param object
     * @param responseCode
     */
    public static void isNull(Object object, IBaseExceptionCode responseCode) {
        if (Objects.nonNull(object)) {
            reportInvalidArgument(responseCode);
        }
    }


    /**
     * 断言true
     *
     * @param expression
     * @param responseCode
     */
    public static void isTrue(boolean expression, IBaseExceptionCode responseCode) {
        if (!expression) {
            reportInvalidArgument(responseCode);
        }
    }


    /**
     * 断言 value 和 expect 相等
     *
     * @param value
     * @param expect
     * @param responseCode
     */
    public static void equals(Object value, Object expect, IBaseExceptionCode responseCode) {
        if (!value.equals(expect)) {
            reportInvalidArgument(responseCode);
        }
    }

    /**
     * 断言 字符串 value 和 字符串 expect 相等
     *
     * @param value
     * @param expect
     * @param responseCode
     */
    public static void equals(String value, String expect, IBaseExceptionCode responseCode) {
        if (!value.equals(expect)) {
            reportInvalidArgument(responseCode);
        }
    }


    /**
     * 断言 value 和 expect 相等
     *
     * @param value
     * @param expect
     * @param responseCode
     */
    public static void noteEquals(Object value, Object expect, IBaseExceptionCode responseCode) {
        if (value.equals(expect)) {
            reportInvalidArgument(responseCode);
        }
    }

    /**
     * 断言 value 和 expect 相等
     *
     * @param value
     * @param expect
     * @param responseCode
     */
    public static void noteEquals(String value, String expect, IBaseExceptionCode responseCode) {
        if (value.equals(expect)) {
            reportInvalidArgument(responseCode);
        }
    }


    /**
     * 断言 value 一定小于等于 limit
     *
     * @param value
     * @param limit
     * @param responseCode
     */
    public static void lessThanEq(long value, long limit, IBaseExceptionCode responseCode) {
        if (value > limit) {
            reportInvalidArgument(responseCode);
        }
    }

    /**
     * 断言 value 一定大于等于 limit
     *
     * @param value
     * @param limit
     * @param responseCode
     */
    public static void greaterThanEq(long value, long limit, IBaseExceptionCode responseCode) {
        if (value < limit) {
            reportInvalidArgument(responseCode);
        }
    }


    /**
     * 报告无效参数
     *
     * @param responseCode
     */
    private static void reportInvalidArgument(IBaseExceptionCode responseCode) {
        throw new AssertInvalidArgumentException(responseCode);
    }


    /**
     * 报告无效参数
     *
     * @param message
     */
    private static void reportInvalidArgument(String message) {
        throw new AssertInvalidArgumentException(message);
    }
}
