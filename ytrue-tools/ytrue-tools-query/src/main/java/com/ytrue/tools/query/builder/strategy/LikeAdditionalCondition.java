package com.ytrue.tools.query.builder.strategy;

import com.baomidou.mybatisplus.core.toolkit.sql.StringEscape;
import com.ytrue.tools.query.entity.Filter;
import com.ytrue.tools.query.enums.MysqlMethod;

/**
 * @author ytrue
 * @date 2022/12/31 17:38
 * @description LikeAdditionalCondition
 */
public class LikeAdditionalCondition extends AbstractAdditionalCondition {

    @Override
    public void splitCondition(StringBuffer stringBuffer, Filter filter) {
        stringBuffer.append(filter.getColumn());
        stringBuffer.append(BLANK_SPACE);
        stringBuffer.append(MysqlMethod.LIKE);
        stringBuffer.append(BLANK_SPACE);

        Object value = filter.getValue();
        if (value instanceof String) {
            value = StringEscape.escapeRawString((String) value);
        }

        switch (filter.getCondition()) {
            case like:
                stringBuffer.append("'%").append(value).append("%'");
                break;
            case likeLeft:
                stringBuffer.append("'%").append(value).append("'");
                break;
            case likeRight:
                stringBuffer.append("'").append(value).append("%'");
                break;
            default:
                throw new RuntimeException("类型错误");
        }
    }
}
