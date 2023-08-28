package com.ytrue.tools.query.parser;

import com.ytrue.tools.query.entity.Filter;
import com.ytrue.tools.query.utils.ExpressionGenerator;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;

/**
 * @author ytrue
 * @date 2023-08-25 14:26
 * @description NotEqualConditionParser
 */
public class NotEqualConditionParser implements ConditionParser {
    @Override
    public Expression parser(Filter filter) throws Exception {
        // 创建左侧表达式
        Expression leftExpression = ExpressionGenerator.generateExpression(getColumnAlias(filter));
        // 创建右侧表达式
        Expression rightExpression = ExpressionGenerator.generateExpression(filter.getValue());
        // 创建 NotEqualsTo 对象
        BinaryExpression notEqualsTo = new NotEqualsTo();
        notEqualsTo.setLeftExpression(leftExpression);
        notEqualsTo.setRightExpression(rightExpression);
        return notEqualsTo;
    }
}
