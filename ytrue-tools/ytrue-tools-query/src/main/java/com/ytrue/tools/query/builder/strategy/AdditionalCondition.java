package com.ytrue.tools.query.builder.strategy;

import com.ytrue.tools.query.entity.Filter;

/**
 * @author ytrue
 * @date 2022/12/31 16:54
 * @description 追加条件
 */
public interface AdditionalCondition {

    /**
     * 附加条件
     *
     * @param stringBuffer
     * @param filter
     */
    void additional(StringBuffer stringBuffer, Filter filter);
}
