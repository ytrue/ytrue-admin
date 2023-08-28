package com.ytrue.tools.query.parser;

import com.ytrue.tools.query.entity.Filter;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.InExpression;

/**
 * @author ytrue
 * @date 2023-08-25 15:37
 * @description NotInConditionParser
 */
public class NotInConditionParser implements ConditionParser {
    @Override
    public Expression parser(Filter filter) throws Exception {
        InConditionParser inConditionParser = new InConditionParser();
        InExpression inCondition = inConditionParser.parser(filter);
        inCondition.setNot(true);
        return inCondition;
    }
}
