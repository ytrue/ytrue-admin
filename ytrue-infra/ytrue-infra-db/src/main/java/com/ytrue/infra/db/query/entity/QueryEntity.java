package com.ytrue.infra.db.query.entity;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.ytrue.infra.db.query.additional.AdditionalQueryWrapper;
import com.ytrue.infra.db.query.additional.AdditionalSqlCondition;
import com.ytrue.infra.db.query.additional.AdditionalSqlSort;
import com.ytrue.infra.db.query.enums.Operator;
import com.ytrue.infra.db.query.enums.QueryMethod;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.springframework.util.CollectionUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author ytrue
 * @description: QueryEntity
 * @date 2022/12/7 10:36
 */
@Slf4j
@Data
public class QueryEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -4364862339415897353L;

    /**
     * 过滤条件
     */
    private LinkedHashSet<Filter> filters = new LinkedHashSet<>();

    /**
     * 排序
     */
    private LinkedHashSet<Sort> sorts = new LinkedHashSet<>();

    /**
     * Column 小驼峰转下划线,默认开启，这里就不针对每个字段做处理，
     */
    private Boolean columnToUnderline = true;


    /**
     * 获取fields
     *
     * @return
     */
    public Set<Filter> getFilters() {
        if (!CollectionUtils.isEmpty(filters)) {
            filters.forEach(field -> field.setColumn(getConversionAfterColumn(field.getColumn())));
        }
        return filters;
    }

    /**
     * 获取sorts
     *
     * @return
     */
    public Set<Sort> getSorts() {
        if (!CollectionUtils.isEmpty(sorts)) {
            sorts.forEach(sort -> sort.setColumn(getConversionAfterColumn(sort.getColumn())));
        }
        return sorts;
    }

    /**
     * 获得 QueryWrapper
     *
     * @return {@link LambdaQueryWrapper <T>}
     */
    public <T> LambdaQueryWrapper<T> lambdaQueryWrapperBuilder() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        // 追加
        AdditionalQueryWrapper additionalQueryWrapper = new AdditionalQueryWrapper();
        // 构建
        additionalQueryWrapper.queryWrapperBuilder(queryWrapper, this);
        // 排序处理
        if (!CollectionUtils.isEmpty(sorts)) {
            sorts.forEach(sort -> queryWrapper.orderBy(StrUtil.isNotBlank(sort.getColumn()), sort.getAsc(), getConversionAfterColumn(sort.getColumn())));
        }
        // 返回
        return queryWrapper.lambda();
    }


    /**
     * 获取字段
     *
     * @param func
     * @return
     */
    private <T> String getFieldName(SFunction<T, ?> func) {
        String fieldName = PropertyNamer.methodToProperty(LambdaUtils.extract(func).getImplMethodName());
        return getConversionAfterColumn(fieldName);
    }

    /**
     * 处理小驼峰转下划线
     *
     * @param column
     * @return
     */
    private String getConversionAfterColumn(String column) {
        return getColumnToUnderline() ? StrUtil.toUnderlineCase(column) : column;
    }

    /**
     * 构建者
     *
     * @return
     */
    public static QueryEntityBuilder builder() {
        return new QueryEntityBuilder();
    }

    /**
     * 构建者模式
     */
    public static final class QueryEntityBuilder {
        private LinkedHashSet<Filter> filters = new LinkedHashSet<>();
        private LinkedHashSet<Sort> sorts = new LinkedHashSet<>();
        private Boolean columnToUnderline = true;

        private QueryEntityBuilder() {
        }

        public QueryEntityBuilder filters(LinkedHashSet<Filter> filters) {
            this.filters = filters;
            return this;
        }

        public QueryEntityBuilder sorts(LinkedHashSet<Sort> sorts) {
            this.sorts = sorts;
            return this;
        }

        public QueryEntityBuilder columnToUnderline(Boolean columnToUnderline) {
            this.columnToUnderline = columnToUnderline;
            return this;
        }

        public QueryEntity build() {
            QueryEntity queryEntity = new QueryEntity();
            queryEntity.setFilters(filters);
            queryEntity.setSorts(sorts);
            queryEntity.setColumnToUnderline(columnToUnderline);
            return queryEntity;
        }
    }


    /**
     * 获取自定义SQL 简化自定义XML复杂情况
     *
     * @return
     */
    public String getCustomSql() {
        try {
            AdditionalSqlCondition additionalSqlCondition = new AdditionalSqlCondition();
            AdditionalSqlSort additionalSqlSort = new AdditionalSqlSort();
            String testSql = "select tmp.* from tmp";
            int testSqlStrLength = testSql.length();

            testSql = additionalSqlCondition.appendWhereCondition(testSql, this.filters);
            testSql = additionalSqlSort.appendSort(testSql, this.sorts);
            return testSql.substring(testSqlStrLength);
        } catch (JSQLParserException e) {
            log.error(e.getMessage());
            return "";
        }
    }


    /**
     * 追加字段--有用到指定的再加了...
     */
    public QueryEntity addFilter(String column, QueryMethod method, Object value) {
        return this.addFilter(column, method, value, "", Operator.and);
    }


    /**
     * 追加字段
     */
    public <T> QueryEntity addFilter(SFunction<T, ?> func, QueryMethod method, Object value) {
        return this.addFilter(func, method, value, "", Operator.and);
    }

    /**
     * 追加字段
     */
    public <T> QueryEntity addFilter(SFunction<T, ?> func, QueryMethod method, Object value, String alias) {
        return this.addFilter(func, method, value, alias, Operator.and);
    }

    /**
     * 追加字段
     */
    public <T> QueryEntity addFilter(SFunction<T, ?> func, QueryMethod method, Object value, Operator operator) {
        return this.addFilter(func, method, value, "", operator);


    }

    /**
     * 追加字段
     */
    public <T> QueryEntity addFilter(SFunction<T, ?> func, QueryMethod method, Object value, String alias, Operator operator) {
        return this.addFilter(getFieldName(func), method, value, alias, operator);
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
    public <T> QueryEntity addFilter(String str, QueryMethod method, Object value, String alias, Operator operator) {
        filters.add(new Filter(str, method, value, alias, operator));
        return this;
    }


    /**
     * 追加排序
     *
     * @param column
     * @param isAsc
     * @return
     */
    public QueryEntity addSort(String column, boolean isAsc) {
        return this.addSort(column, isAsc, "");
    }

    /**
     * 追加排序
     *
     * @param func
     * @param isAsc
     * @return
     */
    public <T> QueryEntity addSort(SFunction<T, ?> func, boolean isAsc) {
        return this.addSort(getFieldName(func), isAsc, "");
    }

    /**
     * 追加排序
     *
     * @param func
     * @param isAsc
     * @return
     */
    public <T> QueryEntity addSort(SFunction<T, ?> func, boolean isAsc, String alias) {
        return this.addSort(getFieldName(func), isAsc, alias);
    }

    public <T> QueryEntity addSort(String column, boolean isAsc, String alias) {
        sorts.add(new Sort(column, isAsc, alias));
        return this;
    }


}
