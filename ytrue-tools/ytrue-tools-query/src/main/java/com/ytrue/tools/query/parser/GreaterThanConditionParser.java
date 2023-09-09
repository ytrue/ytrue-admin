package com.ytrue.tools.query.parser;

import com.ytrue.tools.query.entity.Filter;
import com.ytrue.tools.query.util.ExpressionGenerator;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;

/**
 * @author ytrue
 * @date 2023-08-25 16:24
 * @description GreaterThan
 */
public class GreaterThanConditionParser implements ConditionParser {
    @Override
    public Expression parser(Filter filter) throws Exception {
        // 创建左侧表达式
        Expression leftExpression = ExpressionGenerator.generateExpression(filter.getColumnAlias());
        // 创建右侧表达式
        Expression rightExpression = ExpressionGenerator.generateExpression(filter.getValue());
        // 创建 GreaterThan 对象
        BinaryExpression greaterThan = new GreaterThan();
        greaterThan.setLeftExpression(leftExpression);
        greaterThan.setRightExpression(rightExpression);
        return greaterThan;
    }
}
