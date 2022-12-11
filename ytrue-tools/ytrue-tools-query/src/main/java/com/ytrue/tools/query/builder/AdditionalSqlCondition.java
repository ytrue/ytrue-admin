package com.ytrue.tools.query.builder;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.ytrue.tools.query.entity.Field;
import com.ytrue.tools.query.enums.QueryMethod;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author ytrue
 * @date 2022/9/2 09:25
 * @description 对sql语句附加条件处理
 */
public class AdditionalSqlCondition {

    private static final HashMap<QueryMethod, AdditionalCondition> ADDITIONAL_CONDITION_MAP = new HashMap<>();

    static {
        ADDITIONAL_CONDITION_MAP.put(QueryMethod.eq, (stringBuffer, field) -> splicingString(stringBuffer, field, "="));
        ADDITIONAL_CONDITION_MAP.put(QueryMethod.ne, (stringBuffer, field) -> splicingString(stringBuffer, field, "!="));
        ADDITIONAL_CONDITION_MAP.put(QueryMethod.like, (stringBuffer, field) -> splicingString(stringBuffer, field, "LIKE", "'%", "%'"));
        ADDITIONAL_CONDITION_MAP.put(QueryMethod.likeLeft, (stringBuffer, field) -> splicingString(stringBuffer, field, "LIKE", "'%", "'"));
        ADDITIONAL_CONDITION_MAP.put(QueryMethod.likeRight, (stringBuffer, field) -> splicingString(stringBuffer, field, "LIKE", "'", "%'"));
    }


    /**
     * 追加条件
     *
     * @param sql
     * @param fieldList
     * @return
     * @throws JSQLParserException
     */
    public String appendWhereCondition(String sql, HashSet<Field> fieldList) throws JSQLParserException {

        String appendWhereConditionString = getCondExpression(fieldList);

        if (StrUtil.isBlank(appendWhereConditionString)) {
            return sql;
        }

        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Statement statement = parserManager.parse(new StringReader(sql));

        if (statement instanceof Select) {
            return parseSelect((Select) statement, appendWhereConditionString);
        } else if (statement instanceof Update) {
            return parseUpdate((Update) statement, appendWhereConditionString);
        } else if (statement instanceof Delete) {
            return parseDelete((Delete) statement, appendWhereConditionString);
        } else {
            return sql;
        }
    }


    /**
     * 针对 Delete 类型的处理
     *
     * @param delete
     * @param whereString
     * @return
     * @throws JSQLParserException
     */
    private String parseDelete(Delete delete, String whereString) throws JSQLParserException {

        final Expression expression = delete.getWhere();
        final Expression envCondition = CCJSqlParserUtil.parseCondExpression(whereString);
        if (expression == null) {
            delete.setWhere(envCondition);
        } else {
            AndExpression andExpression = new AndExpression(expression, envCondition);
            delete.setWhere(andExpression);
        }

        return delete.toString();
    }

    /**
     * 针对 Update 类型的处理
     *
     * @param update
     * @param whereString
     * @return
     * @throws JSQLParserException
     */
    private String parseUpdate(Update update, String whereString) throws JSQLParserException {

        final Expression expression = update.getWhere();
        final Expression envCondition = CCJSqlParserUtil.parseCondExpression(whereString);
        if (expression == null) {
            update.setWhere(envCondition);
        } else {
            AndExpression andExpression = new AndExpression(expression, envCondition);
            update.setWhere(andExpression);
        }

        return update.toString();
    }


    /**
     * 针对 Select 类型的处理
     *
     * @param select
     * @param whereString
     * @return
     */
    private String parseSelect(Select select, String whereString) throws JSQLParserException {
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        final Expression expression = plainSelect.getWhere();
        final Expression envCondition = CCJSqlParserUtil.parseCondExpression(whereString);

        if (expression == null) {
            plainSelect.setWhere(envCondition);
        } else {
            AndExpression andExpression = new AndExpression(expression, envCondition);
            plainSelect.setWhere(andExpression);
        }

        return select.toString();
    }


    /**
     * 处理 fields 转成字符串
     *
     * @param fields
     * @return
     */
    private String getCondExpression(HashSet<Field> fields) {

        //要判断一下是否为空，不然会报空指针异常
        if (CollUtil.isEmpty(fields)) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();

        fields.forEach(field -> {
            // 进行匹配
            AdditionalCondition appendCondition = ADDITIONAL_CONDITION_MAP.get(field.getCondition());
            Assert.notNull(appendCondition, "类型匹配错误");
            appendCondition.additional(stringBuffer, field);

        });
        //删除前面的 and
        String s = stringBuffer.toString();
        return s.substring(5);
    }


    /**
     * 条件拼接
     */
    @FunctionalInterface
    private interface AdditionalCondition {
        /**
         * 附加条件
         *
         * @param stringBuffer
         * @param field
         */
        void additional(StringBuffer stringBuffer, Field field);
    }

    /**
     * 拼接字符串
     *
     * @param stringBuffer
     * @param field
     * @param type
     */
    private static void splicingString(StringBuffer stringBuffer, Field field, String type) {
        splicingString(stringBuffer, field, type, "", "");
    }

    /**
     * 拼接字符串
     *
     * @param stringBuffer
     * @param field
     * @param type
     * @param prefixString
     * @param suffixString
     */
    private static void splicingString(StringBuffer stringBuffer, Field field, String type, String prefixString, String suffixString) {
        stringBuffer.append(" and ");
        stringBuffer.append(field.getColumn());
        stringBuffer.append(" ");
        stringBuffer.append(type);
        stringBuffer.append(" ");
        stringBuffer.append(prefixString).append(field.getValue()).append(suffixString);
    }
}
