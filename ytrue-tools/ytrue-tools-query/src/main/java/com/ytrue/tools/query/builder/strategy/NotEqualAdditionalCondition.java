package com.ytrue.tools.query.builder.strategy;

import com.ytrue.tools.query.entity.Filter;
import com.ytrue.tools.query.enums.MysqlMethod;

/**
 * @author ytrue
 * @date 2022/12/31 17:00
 * @description NotEqualAdditionalCondition
 */
public class NotEqualAdditionalCondition extends AbstractAdditionalCondition {

    @Override
    public void splitCondition(StringBuffer stringBuffer, Filter filter) {
        commonSplit(stringBuffer, filter, MysqlMethod.NE.getValue());
    }
}
