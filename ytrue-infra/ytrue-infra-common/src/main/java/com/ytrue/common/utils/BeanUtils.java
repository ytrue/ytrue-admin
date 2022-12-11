package com.ytrue.common.utils;

import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.CollectionUtils;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * @author ytrue
 */

public class BeanUtils {

    /**
     * 缓存BeanCopier 对象 提升性能
     */
    private static final Map<String, BeanCopier> BEAN_COPIER_MAP = new ConcurrentHashMap<>();


    /**
     * Bean属性复制工具方法。
     *
     * @param sources   原始集合
     * @param supplier: 目标类::new(eg: UserVO::new)
     */
    public static <S, T> List<T> cgLibCopyList(List<S> sources, Supplier<T> supplier) {
        if (CollectionUtils.isEmpty(sources)) {
            return Collections.emptyList();
        }
        List<T> list = new ArrayList<>(sources.size());
        BeanCopier beanCopier = null;
        for (S source : sources) {
            T t = supplier.get();
            if (beanCopier == null) {
                beanCopier = getBeanCopier(source.getClass(), t.getClass());
            }
            beanCopier.copy(source, t, null);
            list.add(t);
        }
        return list;
    }

    /**
     * Bean属性复制工具方法。
     *
     * @param source    目标对象
     * @param supplier: 目标类::new(eg: UserVO::new)
     */
    public static <T> T cgLibCopyBean(Object source, Supplier<T> supplier) {
        T t = supplier.get();
        getBeanCopier(source.getClass(), t.getClass()).copy(source, t, null);
        return t;
    }


    /**
     * 获取BeanCopier对象 如果缓存中有从缓存中获取 如果没有则新创建对象并加入缓存
     *
     * @param sourceClass 目标对象
     * @param targetClass 目标类
     * @return BeanCopier
     */
    private static BeanCopier getBeanCopier(Class<?> sourceClass, Class<?> targetClass) {
        String key = getKey(sourceClass.getName(), targetClass.getName());
        BeanCopier beanCopier;
        beanCopier = BEAN_COPIER_MAP.get(key);
        if (beanCopier == null) {
            beanCopier = BeanCopier.create(sourceClass, targetClass, false);
            BEAN_COPIER_MAP.put(key, beanCopier);
        }
        return beanCopier;
    }

    /**
     * 生成缓存key
     */
    private static String getKey(String sourceClassName, String targetClassName) {
        return sourceClassName + targetClassName;
    }

    /**
     * bean转map值
     *
     * @param bean  bean
     * @param clazz class
     * @throws Exception 异常信息
     */
    public static Map<String, Object> beanToMap(Object bean, Class clazz)
            throws Exception {
        Map<String, Object> wrapper = new HashMap<>(16);
        if (bean == null) {
            return wrapper;
        }
        for (PropertyDescriptor pd : Introspector.getBeanInfo(clazz).getPropertyDescriptors()) {
            Object value = pd.getReadMethod().invoke(bean);
            wrapper.put(pd.getName(), value);
        }
        return wrapper;
    }


}