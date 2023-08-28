package com.ytrue.tools.query.parser;

import com.ytrue.tools.query.entity.Filter;
import com.ytrue.tools.query.utils.ExpressionGenerator;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;

/**
 * @author ytrue
 * @date 2023-08-25 16:29
 * @description MinorThanEquals
 */
public class MinorThanEqualsConditionParser implements ConditionParser {
    @Override
    public Expression parser(Filter filter) throws Exception {
        // 创建左侧表达式
        Expression leftExpression = ExpressionGenerator.generateExpression(getColumnAlias(filter));
        // 创建右侧表达式
        Expression rightExpression = ExpressionGenerator.generateExpression(filter.getValue());
        // 创建 MinorThanEquals 对象
        BinaryExpression minorThanEquals = new MinorThanEquals();
        minorThanEquals.setLeftExpression(leftExpression);
        minorThanEquals.setRightExpression(rightExpression);
        return minorThanEquals;
    }
}
