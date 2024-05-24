package com.ytrue.infra.db.query.parser;

import com.ytrue.infra.db.query.entity.Filter;
import net.sf.jsqlparser.expression.operators.relational.Between;

/**
 * @author ytrue
 * @date 2023-09-09 16:16
 * @description NotBetweenConditionParser
 */
public class NotBetweenConditionParser implements ConditionParser {
    @Override
    public Between parser(Filter filter) throws Exception {
        BetweenConditionParser betweenConditionParser = new BetweenConditionParser();
        Between between = betweenConditionParser.parser(filter);
        between.setNot(true);
        return between;
    }
}
