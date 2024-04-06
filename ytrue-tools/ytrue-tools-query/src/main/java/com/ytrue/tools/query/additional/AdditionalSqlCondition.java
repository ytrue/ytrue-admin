package com.ytrue.tools.query.additional;

import cn.hutool.core.collection.CollUtil;
import com.ytrue.tools.query.entity.Filter;
import com.ytrue.tools.query.enums.Operator;
import com.ytrue.tools.query.parser.ConditionParser;
import com.ytrue.tools.query.parser.ConditionParserFactory;
import lombok.RequiredArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

import java.io.StringReader;
import java.util.Collection;
import java.util.Set;

/**
 * @author ytrue
 * @date 2022/9/2 09:25
 * @description 对sql语句附加条件处理
 */
public class AdditionalSqlCondition {

    /**
     * 追加条件
     *
     * @param sql
     * @param filters
     * @return
     * @throws JSQLParserException
     */
    public String appendWhereCondition(String sql, Set<Filter> filters) throws JSQLParserException {

        ExpressionWrap expressionWrap = getCondExpression(filters);

        if (expressionWrap == null || expressionWrap.getExpression() == null) {
            return sql;
        }

        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Statement statement = parserManager.parse(new StringReader(sql));

        if (statement instanceof Select select) {
            return parseSelect(select, expressionWrap);
        } else if (statement instanceof Update update) {
            return parseUpdate(update, expressionWrap);
        } else if (statement instanceof Delete delete) {
            return parseDelete(delete, expressionWrap);
        } else {
            return sql;
        }
    }


    /**
     * 针对 Delete 类型的处理
     *
     * @param delete
     * @param expressionWrap
     * @return
     * @throws JSQLParserException
     */
    private String parseDelete(Delete delete, ExpressionWrap expressionWrap) throws JSQLParserException {
        if (delete.getWhere() == null) {
            delete.setWhere(expressionWrap.getExpression());
        } else {
            Expression expression1 = appendExpression(delete.getWhere(), expressionWrap);
            if (expression1 != null) {
                delete.setWhere(expression1);
            }
        }

        return delete.toString();
    }


    /**
     * 针对 Update 类型的处理
     *
     * @param update
     * @param expressionWrap
     * @return
     * @throws JSQLParserException
     */
    private String parseUpdate(Update update, ExpressionWrap expressionWrap) throws JSQLParserException {
        if (update.getWhere() == null) {
            update.setWhere(expressionWrap.getExpression());
        } else {
            Expression expression1 = appendExpression(update.getWhere(), expressionWrap);
            if (expression1 != null) {
                update.setWhere(expression1);
            }
        }

        return update.toString();
    }


    /**
     * 针对 Select 类型的处理
     *
     * @param select
     * @param expressionWrap
     * @return
     */
    private String parseSelect(Select select, ExpressionWrap expressionWrap) throws JSQLParserException {
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        if (plainSelect.getWhere() == null) {
            plainSelect.setWhere(expressionWrap.getExpression());
        } else {
            Expression expression1 = appendExpression(plainSelect.getWhere(), expressionWrap);
            if (expression1 != null) {
                plainSelect.setWhere(expression1);
            }
        }

        return select.toString();
    }


    /**
     * @param expression
     * @param expressionWrap
     * @return
     */
    private Expression appendExpression(Expression expression, ExpressionWrap expressionWrap) {
        Expression expression1 = null;

        if (expressionWrap.getFirstOperator().equals(Operator.and)) {
            expression1 = new AndExpression(expression, expressionWrap.getExpression());
        }
        if (expressionWrap.getFirstOperator().equals(Operator.or)) {
            expression1 = new OrExpression(expression, expressionWrap.getExpression());
        }
        return expression1;
    }


    /**
     * 处理 filters 转成字符串
     *
     * @param filters
     * @return
     */
    public ExpressionWrap getCondExpression(Set<Filter> filters) {

        //要判断一下是否为空，不然会报空指针异常
        if (CollUtil.isEmpty(filters)) {
            return null;
        }

        ExpressionWrap expressionWrap = new ExpressionWrap();

        filters.forEach(filter -> {
            // 这里针对集合空处理
            if (filter.getValue() instanceof Collection<?> collection) {
                if (CollUtil.isEmpty(collection)) {
                    return;
                }
            }

            ConditionParser conditionParser = ConditionParserFactory.getInstance(filter.getCondition());
            try {
                Expression expression = conditionParser.parser(filter);
                if (expressionWrap.getExpression() == null) {
                    expressionWrap.setExpression(expression);
                    // 设置第一个是什么
                    expressionWrap.setFirstOperator(filter.getOperator());
                } else {
                    // 等于 and
                    Expression expression1 = null;
                    if (filter.getOperator().equals(Operator.and)) {
                        expression1 = new AndExpression(expressionWrap.getExpression(), expression);
                    }
                    if (filter.getOperator().equals(Operator.or)) {
                        expression1 = new OrExpression(expressionWrap.getExpression(), expression);
                    }
                    expressionWrap.setExpression(expression1);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return expressionWrap;
    }


    @Data
    @RequiredArgsConstructor
    @NoArgsConstructor
    private static class ExpressionWrap {
        private Operator firstOperator;
        Expression expression;
    }
}
