package com.ytrue.tools.query.builder.strategy;

import cn.hutool.core.lang.Assert;
import com.ytrue.tools.query.enums.QueryMethod;

import java.util.HashMap;

/**
 * @author ytrue
 * @date 2022/12/31 17:13
 * @description AdditionalConditionFactory
 */
public class AdditionalConditionFactory {

    private static final HashMap<QueryMethod, AdditionalCondition> ADDITIONAL_CONDITION_MAP = new HashMap<>();

    static {
        ADDITIONAL_CONDITION_MAP.put(QueryMethod.eq, new EqualAdditionalCondition());
        ADDITIONAL_CONDITION_MAP.put(QueryMethod.ne, new NotEqualAdditionalCondition());
        ADDITIONAL_CONDITION_MAP.put(QueryMethod.like, new LikeAdditionalCondition());
        ADDITIONAL_CONDITION_MAP.put(QueryMethod.likeLeft, new LikeAdditionalCondition());
        ADDITIONAL_CONDITION_MAP.put(QueryMethod.likeRight, new LikeAdditionalCondition());
        ADDITIONAL_CONDITION_MAP.put(QueryMethod.between, new BetweenAdditionalCondition());
        ADDITIONAL_CONDITION_MAP.put(QueryMethod.in, new InAdditionalCondition());
    }

    public static AdditionalCondition getInstance(QueryMethod queryMethod) {
        // 进行匹配
        AdditionalCondition additionalCondition = ADDITIONAL_CONDITION_MAP.get(queryMethod);
        Assert.notNull(additionalCondition, "类型匹配错误");
        return additionalCondition;
    }
}
