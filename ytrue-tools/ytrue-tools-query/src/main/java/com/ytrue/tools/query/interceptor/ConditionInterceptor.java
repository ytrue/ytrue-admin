package com.ytrue.tools.query.interceptor;

import com.ytrue.tools.query.builder.AdditionalSqlCondition;
import com.ytrue.tools.query.entity.Field;
import com.ytrue.tools.query.entity.Fields;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.util.HashSet;

/**
 * @author ytrue
 * @date 2022/7/2 11:59
 * @description ConditionInterceptor 条件拦截
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class ConditionInterceptor implements Interceptor {

    private final static String TARGET_DELEGATE_BOUNDS_SQL = "target.delegate.boundSql.sql";
    private final static String TARGET_DELEGATE_PARAMETERIZABLE_PARAMETERISED = "target.delegate.parameterHandler.parameterObject";


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 通过反射处理
        MetaObject metaObject = SystemMetaObject.forObject(invocation);
        // 获取执行的sql语句
        String sql = (String) metaObject.getValue(TARGET_DELEGATE_BOUNDS_SQL);

        //直接获取DAO方法的 Fields参数
        Object value = metaObject.getValue(TARGET_DELEGATE_PARAMETERIZABLE_PARAMETERISED);
        HashSet<Fields> fieldsSet = new HashSet<>();

        // 如果是多个参数 这里就要处理
        if (value instanceof MapperMethod.ParamMap) {
            ((MapperMethod.ParamMap) value).forEach((k, v) -> {
                if (v instanceof Fields) {
                    fieldsSet.add((Fields) v);
                }
            });
        }

        //只有一个参数，直接加入就行
        if (value instanceof Fields) {
            fieldsSet.add((Fields) value);
        }
        // 获取全部的Field
        HashSet<Field> fieldList = new HashSet<>();
        fieldsSet.forEach(fields -> fieldList.addAll(fields.getFields()));

        // 附加sql条件
        AdditionalSqlCondition additionalSqlCondition = new AdditionalSqlCondition();
        String newSql = additionalSqlCondition.appendWhereCondition(sql, fieldList);

        //如果是原来的sql 那就不用处理了
        if (!sql.equals(newSql)) {
            // 替换之前的sql
            metaObject.setValue(TARGET_DELEGATE_BOUNDS_SQL, newSql);
        }
        // 放行
        return invocation.proceed();
    }

}
