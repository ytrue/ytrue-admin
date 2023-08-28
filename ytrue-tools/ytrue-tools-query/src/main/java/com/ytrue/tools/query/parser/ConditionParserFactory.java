package com.ytrue.tools.query.parser;

import cn.hutool.core.lang.Assert;
import com.ytrue.tools.query.enums.QueryMethod;

import java.util.HashMap;

/**
 * @author ytrue
 * @date 2023-08-25 16:32
 * @description ConditionParserFactory
 */
public class ConditionParserFactory {

    private static final HashMap<QueryMethod, ConditionParser> CONDITION_PARSER_MAP = new HashMap<>(16);


    static {
        CONDITION_PARSER_MAP.put(QueryMethod.eq, new EqualConditionParser());
        CONDITION_PARSER_MAP.put(QueryMethod.ne, new NotEqualConditionParser());
        CONDITION_PARSER_MAP.put(QueryMethod.gt, new GreaterThanConditionParser());
        CONDITION_PARSER_MAP.put(QueryMethod.lt, new MinorThanConditionParser());
        CONDITION_PARSER_MAP.put(QueryMethod.ge, new GreaterThanEqualsConditionParser());
        CONDITION_PARSER_MAP.put(QueryMethod.le, new MinorThanEqualsConditionParser());
        CONDITION_PARSER_MAP.put(QueryMethod.like, new LikeConditionParser());
        CONDITION_PARSER_MAP.put(QueryMethod.likeLeft, new LikeConditionParser());
        CONDITION_PARSER_MAP.put(QueryMethod.likeRight, new LikeConditionParser());
        CONDITION_PARSER_MAP.put(QueryMethod.in, new InConditionParser());
        CONDITION_PARSER_MAP.put(QueryMethod.notin, new NotInConditionParser());
        CONDITION_PARSER_MAP.put(QueryMethod.between, new BetweenConditionParser());
    }

    public static ConditionParser getInstance(QueryMethod queryMethod) {
        // 进行匹配
        ConditionParser additionalCondition = CONDITION_PARSER_MAP.get(queryMethod);
        Assert.notNull(additionalCondition, "类型匹配错误");
        return additionalCondition;
    }

}
