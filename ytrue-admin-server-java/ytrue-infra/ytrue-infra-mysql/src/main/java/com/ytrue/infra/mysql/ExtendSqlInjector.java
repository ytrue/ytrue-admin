package com.ytrue.infra.mysql;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;

import java.util.List;

/**
 * 扩展 MyBatis Plus 的 SQL 注入器，允许在 Mapper 接口中添加自定义的 SQL 方法。
 * <p>
 * 该类重写了 {@link DefaultSqlInjector} 的 {@link #getMethodList(Class, TableInfo)} 方法，
 * 在默认方法列表中添加了批量插入方法 {@link InsertBatchSomeColumn}。
 * </p>
 *
 * @author ytrue
 * @date 2023-06-10 20:12
 */
public class ExtendSqlInjector extends DefaultSqlInjector {

    /**
     * 获取自定义的 SQL 方法列表。
     *
     * @param mapperClass Mapper 接口的 Class 对象
     * @param tableInfo   表信息
     * @return 自定义的 SQL 方法列表，包括批量插入方法
     */
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        // 添加 InsertBatchSomeColumn 方法以支持批量插入
        methodList.add(new InsertBatchSomeColumn());
        return methodList;
    }
}
