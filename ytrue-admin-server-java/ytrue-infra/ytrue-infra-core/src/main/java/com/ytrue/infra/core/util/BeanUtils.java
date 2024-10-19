package com.ytrue.infra.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * BeanUtils 是一个扩展 Spring BeanUtils 的工具类，
 * 提供了对象属性拷贝的实用方法，包括单个对象和集合对象的拷贝。
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {

    /**
     * 拷贝源对象的属性到目标对象。
     *
     * @param source  源对象
     * @param supplier 目标对象的供应器，用于创建目标对象实例
     * @param <T>     目标对象的类型
     * @return 目标对象
     */
    public static <T> T copyProperties(Object source, Supplier<T> supplier) {
        T t = supplier.get();  // 使用供应器创建目标对象
        copyProperties(source, t);  // 拷贝属性
        return t;  // 返回目标对象
    }

    /**
     * 拷贝集合数据的属性。
     *
     * @param sources 数据源类的集合
     * @param target  目标类的供应器，用于创建目标对象实例（例如：UserVO::new）
     * @param <S>     数据源对象的类型
     * @param <T>     目标对象的类型
     * @return 目标对象集合
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        return copyListProperties(sources, target, null);  // 默认没有回调
    }

    /**
     * 带回调函数的集合数据的属性拷贝（可自定义字段拷贝规则）。
     *
     * @param sources  数据源类的集合
     * @param target   目标类的供应器，用于创建目标对象实例（例如：UserVO::new）
     * @param callBack 回调函数，用于在每个对象拷贝后执行额外逻辑
     * @param <S>     数据源对象的类型
     * @param <T>     目标对象的类型
     * @return 目标对象集合
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target, BeanUtilsCallBack<S, T> callBack) {
        List<T> list = new ArrayList<>(sources.size());  // 初始化目标集合
        for (S source : sources) {
            T t = target.get();  // 创建目标对象
            copyProperties(source, t);  // 拷贝属性
            list.add(t);  // 添加到目标集合
            if (callBack != null) {
                // 如果提供了回调，执行回调
                callBack.callBack(source, t);
            }
        }
        return list;  // 返回目标对象集合
    }

    /**
     * 回调函数接口，用于在拷贝属性后执行额外的逻辑。
     *
     * @param <S> 源对象类型
     * @param <T> 目标对象类型
     */
    @FunctionalInterface
    public interface BeanUtilsCallBack<S, T> {

        /**
         * 定义默认回调方法。
         *
         * @param s 源对象
         * @param t 目标对象
         */
        void callBack(S s, T t);
    }
}
