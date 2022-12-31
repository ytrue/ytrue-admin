package com.ytrue.tools.query.builder.strategy;

import com.ytrue.tools.query.entity.Filter;
import com.ytrue.tools.query.enums.MysqlMethod;

import java.util.List;

/**
 * @author ytrue
 * @date 2022/12/31 17:40
 * @description BetweenAdditionalCondition
 */
public class BetweenAdditionalCondition extends AbstractAdditionalCondition {
    @Override
    public void splitCondition(StringBuffer stringBuffer, Filter filter) {
        // 强转list，不想处理其他类型
        Object start = escapeStringValue(((List<?>) filter.getValue()).get(0));
        Object end = escapeStringValue(((List<?>) filter.getValue()).get(1));

        stringBuffer.append(filter.getColumn());
        stringBuffer.append(BLANK_SPACE);
        stringBuffer.append(MysqlMethod.BETWEEN.getValue());
        stringBuffer.append(BLANK_SPACE);
        stringBuffer.append(start);
        stringBuffer.append(" and ");
        stringBuffer.append(BLANK_SPACE);
        stringBuffer.append(end);
    }
}
