package com.ytrue.tools.query.builder.strategy;

import com.ytrue.tools.query.entity.Filter;
import com.ytrue.tools.query.enums.MysqlMethod;

import java.util.List;

/**
 * @author ytrue
 * @date 2022/12/31 17:40
 * @description InAdditionalCondition
 */
public class InAdditionalCondition extends AbstractAdditionalCondition {

    @Override
    public void splitCondition(StringBuffer stringBuffer, Filter filter) {
        // 强转list，不想处理其他类型
        List<?> value = (List<?>) filter.getValue();

        StringBuilder data = new StringBuilder();
        for (int i = 0; i < value.size(); i++) {

            Object v = escapeStringValue(value.get(i));
            if (i == 0) {
                data.append(v);
            } else {
                data.append(",").append(v);
            }
        }

        stringBuffer.append(filter.getColumn());
        stringBuffer.append(BLANK_SPACE);
        stringBuffer.append(MysqlMethod.IN.getValue());
        stringBuffer.append(BLANK_SPACE);
        stringBuffer.append("(");
        stringBuffer.append(data);
        stringBuffer.append(")");
    }
}
