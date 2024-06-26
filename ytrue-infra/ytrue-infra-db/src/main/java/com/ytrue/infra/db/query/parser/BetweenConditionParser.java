package com.ytrue.infra.db.query.parser;

import com.ytrue.infra.db.query.entity.Filter;
import com.ytrue.infra.db.query.util.ExpressionGenerator;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.Between;

import java.util.List;

/**
 * @author ytrue
 * @date 2023-08-25 16:35
 * @description BetweenConditionParser
 */
public class BetweenConditionParser implements ConditionParser {

    @Override
    public Between parser(Filter filter) throws Exception {
        // 强转list，暂不不定义其他的
        List<?> list = (List<?>) filter.getValue();

        int size = 2;
        if (list.size() != size) {
            throw new IllegalArgumentException("filter.getValue 参数个数必须是2个");
        }

        Object start = escapeStringValue(list.get(0));
        Object end = escapeStringValue(list.get(1));

        // 创建左侧表达式
        Expression leftExpression = ExpressionGenerator.generateExpression(filter.getColumnAlias());
        // 创建右侧表达式

        // 创建 Between 对象表示 BETWEEN 条件
        Between betweenCondition = new Between();
        betweenCondition.setLeftExpression(leftExpression);
        // 设置值
        Expression startValue = ExpressionGenerator.generateExpression(start);
        Expression endValue = ExpressionGenerator.generateExpression(end);
        betweenCondition.setBetweenExpressionStart(startValue);
        betweenCondition.setBetweenExpressionEnd(endValue);

        return betweenCondition;
    }
}
