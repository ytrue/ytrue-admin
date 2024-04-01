package com.ytrue.tools.query.interceptor;

import com.ytrue.tools.query.additional.AdditionalSqlCondition;
import com.ytrue.tools.query.additional.AdditionalSqlSort;
import com.ytrue.tools.query.entity.Filter;
import com.ytrue.tools.query.entity.QueryEntity;
import com.ytrue.tools.query.entity.Sort;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.util.CollectionUtils;

import java.sql.Connection;
import java.util.LinkedHashSet;

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

        //直接获取DAO方法的 QueryEntity参数
        Object value = metaObject.getValue(TARGET_DELEGATE_PARAMETERIZABLE_PARAMETERISED);
        LinkedHashSet<QueryEntity> queryEntitySet = new LinkedHashSet<>();

        // 如果是多个参数 这里就要处理
        if (value instanceof MapperMethod.ParamMap paramMap) {
            paramMap.forEach((k, v) -> {
                if (v instanceof QueryEntity queryEntity) {
                    queryEntitySet.add(queryEntity);
                }
            });
        }

        //只有一个参数，直接加入就行
        if (value instanceof QueryEntity queryEntity) {
            queryEntitySet.add(queryEntity);
        }
        // 获取全部的Filter
        LinkedHashSet<Filter> filterSet = new LinkedHashSet<>();
        queryEntitySet.stream().filter(queryEntity -> !CollectionUtils.isEmpty(queryEntity.getFilters())).forEach(queryEntity -> filterSet.addAll(queryEntity.getFilters()));
        // 获取全部的sort
        LinkedHashSet<Sort> sortSet = new LinkedHashSet<>();
        queryEntitySet.stream().filter(queryEntity -> !CollectionUtils.isEmpty(queryEntity.getSorts())).forEach(queryEntity -> sortSet.addAll(queryEntity.getSorts()));

        // 附加sql条件
        AdditionalSqlCondition additionalSqlCondition = new AdditionalSqlCondition();
        String appendWhereConditionSql = additionalSqlCondition.appendWhereCondition(sql, filterSet);
        //如果是原来的sql 那就不用处理了
        if (!sql.equals(appendWhereConditionSql)) {
            // 替换之前的sql
            metaObject.setValue(TARGET_DELEGATE_BOUNDS_SQL, appendWhereConditionSql);
        }

        // 这里对排序做处理
        AdditionalSqlSort additionalSqlSort = new AdditionalSqlSort();
        String appendSortAfterSql = additionalSqlSort.appendSort(appendWhereConditionSql, sortSet);
        if (!appendSortAfterSql.equals(appendWhereConditionSql)) {
            // 替换之前的sql
            metaObject.setValue(TARGET_DELEGATE_BOUNDS_SQL, appendSortAfterSql);
        }

        // 放行
        return invocation.proceed();
    }

}
