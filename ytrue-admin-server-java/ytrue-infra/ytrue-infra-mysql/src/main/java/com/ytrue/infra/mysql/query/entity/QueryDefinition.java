package com.ytrue.infra.mysql.query.entity;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.ytrue.infra.mysql.query.enums.Operator;
import com.ytrue.infra.mysql.query.enums.QueryMethod;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedHashSet;

/**
 * 查询定义类，封装了查询条件和排序信息。
 * <p>
 * 该类使用链式调用的方式，允许动态构建查询条件（where）和排序（order by），
 * 提供了多种方法来方便地添加过滤条件和排序规则。
 * </p>
 *
 * @author ytrue
 */
@Data
@Slf4j
public class QueryDefinition implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 过滤条件集合。
     * 存储所有的查询条件，使用 LinkedHashSet 以保持插入顺序。
     */
    private LinkedHashSet<Where> whereSets = new LinkedHashSet<>();

    /**
     * 排序条件集合。
     * 存储所有的排序条件，使用 LinkedHashSet 以保持插入顺序。
     */
    private LinkedHashSet<OrderBy> orderSets = new LinkedHashSet<>();

    /**
     * 追加过滤条件。
     *
     * @param column 列名
     * @param method 查询方法
     * @param value 查询值
     * @return 当前 QueryDefinition 对象
     */
    public QueryDefinition where(String column, QueryMethod method, Object value) {
        return this.where(column, method, value, "", Operator.and);
    }

    /**
     * 追加过滤条件。
     *
     * @param func Lambda 表达式，用于获取字段名
     * @param method 查询方法
     * @param value 查询值
     * @param <T> 实体类型
     * @return 当前 QueryDefinition 对象
     */
    public <T> QueryDefinition where(SFunction<T, ?> func, QueryMethod method, Object value) {
        return this.where(func, method, value, "", Operator.and);
    }

    /**
     * 追加过滤条件。
     *
     * @param func Lambda 表达式，用于获取字段名
     * @param method 查询方法
     * @param value 查询值
     * @param alias 别名
     * @return 当前 QueryDefinition 对象
     */
    public <T> QueryDefinition where(SFunction<T, ?> func, QueryMethod method, Object value, String alias) {
        return this.where(func, method, value, alias, Operator.and);
    }

    /**
     * 追加过滤条件。
     *
     * @param func Lambda 表达式，用于获取字段名
     * @param method 查询方法
     * @param value 查询值
     * @param operator 逻辑运算符
     * @param <T> 实体类型
     * @return 当前 QueryDefinition 对象
     */
    public <T> QueryDefinition where(SFunction<T, ?> func, QueryMethod method, Object value, Operator operator) {
        return this.where(func, method, value, "", operator);
    }

    /**
     * 追加过滤条件。
     *
     * @param func Lambda 表达式，用于获取字段名
     * @param method 查询方法
     * @param value 查询值
     * @param alias 别名
     * @param operator 逻辑运算符
     * @param <T> 实体类型
     * @return 当前 QueryDefinition 对象
     */
    public <T> QueryDefinition where(SFunction<T, ?> func, QueryMethod method, Object value, String alias, Operator operator) {
        return this.where(getColumnName(func), method, value, alias, operator);
    }

    /**
     * 最全的过滤条件追加方法。
     *
     * @param str 列名
     * @param method 查询方法
     * @param value 查询值
     * @param alias 别名
     * @param operator 逻辑运算符
     * @param <T> 实体类型
     * @return 当前 QueryDefinition 对象
     */
    public <T> QueryDefinition where(String str, QueryMethod method, Object value, String alias, Operator operator) {
        whereSets.add(new Where(str, method, value, alias, operator));
        return this;
    }

    /**
     * 追加排序条件。
     *
     * @param column 列名
     * @param isAsc 是否升序
     * @return 当前 QueryDefinition 对象
     */
    public QueryDefinition orderBy(String column, boolean isAsc) {
        return this.orderBy(column, isAsc, "");
    }

    /**
     * 追加排序条件。
     *
     * @param func Lambda 表达式，用于获取字段名
     * @param isAsc 是否升序
     * @param <T> 实体类型
     * @return 当前 QueryDefinition 对象
     */
    public <T> QueryDefinition orderBy(SFunction<T, ?> func, boolean isAsc) {
        return this.orderBy(getColumnName(func), isAsc, "");
    }

    /**
     * 追加排序条件。
     *
     * @param func Lambda 表达式，用于获取字段名
     * @param isAsc 是否升序
     * @param alias 别名
     * @param <T> 实体类型
     * @return 当前 QueryDefinition 对象
     */
    public <T> QueryDefinition orderBy(SFunction<T, ?> func, boolean isAsc, String alias) {
        return this.orderBy(getColumnName(func), isAsc, alias);
    }

    /**
     * 追加排序条件。
     *
     * @param column 列名
     * @param isAsc 是否升序
     * @param alias 别名
     * @return 当前 QueryDefinition 对象
     */
    public <T> QueryDefinition orderBy(String column, boolean isAsc, String alias) {
        orderSets.add(new OrderBy(column, isAsc, alias));
        return this;
    }

    /**
     * 获取字段名称。
     *
     * @param func Lambda 表达式
     * @param <T> 实体类型
     * @return 字段名称
     */
    private <T> String getColumnName(SFunction<T, ?> func) {
        String fieldName = PropertyNamer.methodToProperty(LambdaUtils.extract(func).getImplMethodName());
        // 处理小驼峰转下划线
        return StrUtil.toUnderlineCase(fieldName);
    }
}
