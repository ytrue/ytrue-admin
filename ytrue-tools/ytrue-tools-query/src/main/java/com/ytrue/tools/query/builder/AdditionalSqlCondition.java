package com.ytrue.tools.query.builder;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.ytrue.tools.query.entity.Filter;
import com.ytrue.tools.query.enums.MysqlMethod;
import com.ytrue.tools.query.enums.Operator;
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
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ytrue
 * @date 2022/9/2 09:25
 * @description 对sql语句附加条件处理
 */
public class AdditionalSqlCondition {

    private static final HashMap<QueryMethod, AdditionalCondition> ADDITIONAL_CONDITION_MAP = new HashMap<>();
    private final static String STATIC_QUOTES = "'";

    static {
        ADDITIONAL_CONDITION_MAP.put(QueryMethod.eq, (stringBuffer, field) -> splicingString(stringBuffer, field, MysqlMethod.EQ.getValue()));
        ADDITIONAL_CONDITION_MAP.put(QueryMethod.ne, (stringBuffer, field) -> splicingString(stringBuffer, field, MysqlMethod.NE.getValue()));
        ADDITIONAL_CONDITION_MAP.put(QueryMethod.like, (stringBuffer, field) -> splicingString(stringBuffer, field, MysqlMethod.LIKE.getValue(), "'%", "%'"));
        ADDITIONAL_CONDITION_MAP.put(QueryMethod.likeLeft, (stringBuffer, field) -> splicingString(stringBuffer, field, MysqlMethod.LIKE.getValue(), "'%", STATIC_QUOTES));
        ADDITIONAL_CONDITION_MAP.put(QueryMethod.likeRight, (stringBuffer, field) -> splicingString(stringBuffer, field, MysqlMethod.LIKE.getValue(), STATIC_QUOTES, "%'"));

        // between处理
        ADDITIONAL_CONDITION_MAP.put(QueryMethod.between, (stringBuffer, filter) -> {
            Object start = disposeValue(((List<?>) filter.getValue()).get(0));
            Object end = disposeValue(((List<?>) filter.getValue()).get(1));
            stringBuffer.append(" ").append(filter.getOperator()).append(" ");
            if (StrUtil.isNotBlank(filter.getAlias())) {
                stringBuffer.append(filter.getAlias()).append(".");
            }
            stringBuffer.append(filter.getColumn());
            stringBuffer.append(" ");
            stringBuffer.append(filter.getCondition());
            stringBuffer.append(" ");
            stringBuffer.append(start);
            stringBuffer.append(" and ");
            stringBuffer.append(" ");
            stringBuffer.append(end);
        });

        // in处理--- TODO 关于这里的处理后期换成策略模式处理，使用map满足不了了，代码太多了
        ADDITIONAL_CONDITION_MAP.put(QueryMethod.in, new AdditionalCondition() {
            @Override
            public void additional(StringBuffer stringBuffer, Filter filter) {

            }
        });
    }


    /**
     * 追加条件
     *
     * @param sql
     * @param filters
     * @return
     * @throws JSQLParserException
     */
    public String appendWhereCondition(String sql, Set<Filter> filters) throws JSQLParserException {

        String appendWhereConditionString = getCondExpression(filters);

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
     * 处理 filters 转成字符串
     *
     * @param filters
     * @return
     */
    private String getCondExpression(Set<Filter> filters) {

        //要判断一下是否为空，不然会报空指针异常
        if (CollUtil.isEmpty(filters)) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();

        filters.forEach(filter -> {
            // 进行匹配
            AdditionalCondition appendCondition = ADDITIONAL_CONDITION_MAP.get(filter.getCondition());
            Assert.notNull(appendCondition, "类型匹配错误");

            filter.setValue(AdditionalSqlCondition.disposeValue(filter.getValue()));
            appendCondition.additional(stringBuffer, filter);

        });

        String s = stringBuffer.toString().trim();
        //删除前面的 and 或者是 or
        String and = Operator.and.name();
        String or = Operator.or.name();

        if (s.startsWith(and)) {
            s = s.substring(and.length() + 1);
        }

        if (s.startsWith(or)) {
            s = s.substring(or.length() + 1);
        }

        return s;
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
         * @param filter
         */
        void additional(StringBuffer stringBuffer, Filter filter);
    }

    /**
     * 拼接字符串
     *
     * @param stringBuffer
     * @param filter
     * @param type
     */
    private static void splicingString(StringBuffer stringBuffer, Filter filter, String type) {
        splicingString(stringBuffer, filter, type, "", "");
    }

    /**
     * 拼接字符串
     *
     * @param stringBuffer
     * @param filter
     * @param type
     * @param prefixString
     * @param suffixString
     */
    private static void splicingString(StringBuffer stringBuffer, Filter filter, String type, String prefixString, String suffixString) {

        stringBuffer.append(" ").append(filter.getOperator()).append(" ");
        if (StrUtil.isNotBlank(filter.getAlias())) {
            stringBuffer.append(filter.getAlias()).append(".");
        }
        stringBuffer.append(filter.getColumn());
        stringBuffer.append(" ");
        stringBuffer.append(type);
        stringBuffer.append(" ");


        Object value = filter.getValue();
        // 如果是LIKE 要加之前字符串处理的要做掉
        if (value instanceof String && type.equals(MysqlMethod.LIKE.getValue())) {
            String str = (String) value;
            if (str.startsWith(STATIC_QUOTES) && str.endsWith(STATIC_QUOTES)) {
                value = str.substring(1, str.length() - 1);
            }
        }

        stringBuffer.append(prefixString).append(value).append(suffixString);
    }


    /**
     * 字符串做处理
     *
     * @param value
     * @return
     */
    public static Object disposeValue(Object value) {
        if (value instanceof String) {
            String str = (String) value;
            if (!str.startsWith(STATIC_QUOTES) && !str.endsWith(STATIC_QUOTES)) {
                str = STATIC_QUOTES + str + STATIC_QUOTES;
            }
            return str;
        }
        return value;
    }
}
