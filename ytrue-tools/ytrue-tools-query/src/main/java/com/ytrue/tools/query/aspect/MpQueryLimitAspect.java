package com.ytrue.tools.query.aspect;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ytrue
 * @date 2022/8/8 17:18
 * <p>
 * 这个只能通过aop去拦截
 * @description mybatis plus的 selectOne 加上 limit =1
 */
@Aspect
public class MpQueryLimitAspect {


    private final static String LIMIT_ONE = " limit 1";
    private final static String LIMIT = " limit";


    @Pointcut("execution(public * com.baomidou.mybatisplus.extension.service.impl.ServiceImpl.getOne(..)) || " +
            "execution(public * com.baomidou.mybatisplus.core.mapper.BaseMapper.selectOne(..)))")
    public void appendLimit() {
    }


    @Around("appendLimit()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        //获取方法的参数值数组
        Object[] methodArgs = proceedingJoinPoint.getArgs();

        // 如果有参数
        if (methodArgs.length > 0) {
            // 如果第一个参数是 AbstractWrapper 类型
            if (methodArgs[0] instanceof AbstractWrapper) {
                // 操作处理
                AbstractWrapper wrapper = (AbstractWrapper) methodArgs[0];

                // 获取对应的字段
                Field lastSqlField = getAllFields(wrapper).stream().filter(field -> "lastSql".equals(field.getName())).findFirst().orElse(null);

                if (null != lastSqlField) {
                    lastSqlField.setAccessible(true);
                    SharedString sharedString = (SharedString) lastSqlField.get(wrapper);

                    String lastSql = sharedString.getStringValue();
                    if (!lastSql.contains(LIMIT)) {
                        lastSql += LIMIT_ONE;
                    }
                    sharedString.setStringValue(lastSql);
                    lastSqlField.set(wrapper, sharedString);

                    // 覆盖
                    methodArgs[0] = wrapper;
                }
            }
        }
        return proceedingJoinPoint.proceed(methodArgs);
    }


    /**
     * 获取指定类的所有字段属性，包含父类的
     *
     * @param object
     * @return
     */
    private List<Field> getAllFields(Object object) {
        List<Field> fieldList = new ArrayList<>();
        Class tempClass = object.getClass();
        //当父类为null的时候说明到达了最上层的父类(Object类).
        while (tempClass != null) {
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            //得到父类,然后赋给自己
            tempClass = tempClass.getSuperclass();
        }
        return fieldList;
    }
}
