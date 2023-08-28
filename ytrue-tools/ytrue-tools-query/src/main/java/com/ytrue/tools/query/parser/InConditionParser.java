package com.ytrue.tools.query.parser;

import com.ytrue.tools.query.entity.Filter;
import com.ytrue.tools.query.utils.ExpressionGenerator;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author ytrue
 * @date 2023-08-25 14:26
 * @description InConditionParser
 */
public class InConditionParser implements ConditionParser {

    @Override
    public InExpression parser(Filter filter) throws Exception {
        // 强转list,如果强转不了，这里抛移除
        List<?> valueList = (List<?>) filter.getValue();
        // 创建左侧表达式
        Expression leftExpression = ExpressionGenerator.generateExpression(getColumnAlias(filter));

        // 创建值列表
        List<Expression> inValueDataList = new ArrayList<>();
        // 加入进去
        valueList.forEach((Consumer<Object>) o -> {
            try {
                inValueDataList.add(ExpressionGenerator.generateExpression(o));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // 创建 InExpression 对象表示 IN 条件
        InExpression inCondition = new InExpression();
        inCondition.setLeftExpression(leftExpression);

        // 设置进去
        ExpressionList expressionList = new ExpressionList();
        expressionList.setExpressions(inValueDataList);
        inCondition.setRightItemsList(expressionList);

        return inCondition;

    }
}
