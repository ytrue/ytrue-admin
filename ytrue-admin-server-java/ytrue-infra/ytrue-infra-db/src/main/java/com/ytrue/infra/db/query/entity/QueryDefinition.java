package com.ytrue.infra.db.query.entity;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.ytrue.infra.db.query.enums.Operator;
import com.ytrue.infra.db.query.enums.QueryMethod;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedHashSet;

@Data
@Slf4j
public class QueryDefinition implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 过滤条件
     */
    private LinkedHashSet<Where> whereSets = new LinkedHashSet<>();

    /**
     * 排序
     */
    private LinkedHashSet<OrderBy> orderSets = new LinkedHashSet<>();


    /**
     * 追加字段--有用到指定的再加了...
     */
    public QueryDefinition where(String column, QueryMethod method, Object value) {
        return this.where(column, method, value, "", Operator.and);
    }


    /**
     * 追加字段
     */
    public <T> QueryDefinition where(SFunction<T, ?> func, QueryMethod method, Object value) {
        return this.where(func, method, value, "", Operator.and);
    }

    /**
     * 追加字段
     */
    public <T> QueryDefinition where(SFunction<T, ?> func, QueryMethod method, Object value, String alias) {
        return this.where(func, method, value, alias, Operator.and);
    }

    /**
     * 追加字段
     */
    public <T> QueryDefinition where(SFunction<T, ?> func, QueryMethod method, Object value, Operator operator) {
        return this.where(func, method, value, "", operator);


    }

    /**
     * 追加字段
     */
    public <T> QueryDefinition where(SFunction<T, ?> func, QueryMethod method, Object value, String alias, Operator operator) {
        return this.where(getColumnName(func), method, value, alias, operator);
    }


    /**
     * 最全的
     *
     * @param str
     * @param method
     * @param value
     * @param alias
     * @param operator
     * @param <T>
     * @return
     */
    public <T> QueryDefinition where(String str, QueryMethod method, Object value, String alias, Operator operator) {
        whereSets.add(new Where(str, method, value, alias, operator));
        return this;
    }


    /**
     * 追加排序
     *
     * @param column
     * @param isAsc
     * @return
     */
    public QueryDefinition orderBy(String column, boolean isAsc) {
        return this.orderBy(column, isAsc, "");
    }

    /**
     * 追加排序
     *
     * @param func
     * @param isAsc
     * @return
     */
    public <T> QueryDefinition orderBy(SFunction<T, ?> func, boolean isAsc) {
        return this.orderBy(getColumnName(func), isAsc, "");
    }

    /**
     * 追加排序
     *
     * @param func
     * @param isAsc
     * @return
     */
    public <T> QueryDefinition orderBy(SFunction<T, ?> func, boolean isAsc, String alias) {
        return this.orderBy(getColumnName(func), isAsc, alias);
    }

    public <T> QueryDefinition orderBy(String column, boolean isAsc, String alias) {
        orderSets.add(new OrderBy(column, isAsc, alias));
        return this;
    }

    /**
     * 获取字段
     *
     * @param func
     * @return
     */
    private <T> String getColumnName(SFunction<T, ?> func) {
        String fieldName = PropertyNamer.methodToProperty(LambdaUtils.extract(func).getImplMethodName());
        // 处理小驼峰转下划线
        return StrUtil.toUnderlineCase(fieldName);
    }


}
