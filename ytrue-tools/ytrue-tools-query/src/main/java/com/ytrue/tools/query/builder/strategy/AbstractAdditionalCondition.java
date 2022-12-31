package com.ytrue.tools.query.builder.strategy;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.sql.StringEscape;
import com.ytrue.tools.query.entity.Filter;
import com.ytrue.tools.query.enums.MysqlMethod;
import com.ytrue.tools.query.enums.QueryMethod;

/**
 * @author ytrue
 * @date 2022/12/31 17:01
 * @description AbstractAdditionalCondition
 */
public abstract class AbstractAdditionalCondition implements AdditionalCondition {

    protected final static String BLANK_SPACE = " ";

    @Override
    public void additional(StringBuffer stringBuffer, Filter filter) {
        StringBuffer newStr = splitOperatorAndAlias(stringBuffer, filter);
        // 交个子类处理
        splitCondition(newStr, filter);
    }


    /**
     * 拼接条件
     *
     * @param stringBuffer
     * @param filter
     */
    public abstract void splitCondition(StringBuffer stringBuffer, Filter filter);


    /**
     * 公共拼接
     *
     * @param stringBuffer
     * @param filter
     * @param type
     */
    protected void commonSplit(StringBuffer stringBuffer, Filter filter, String type) {
        stringBuffer.append(filter.getColumn());
        stringBuffer.append(BLANK_SPACE);
        stringBuffer.append(type);
        stringBuffer.append(BLANK_SPACE);
        stringBuffer.append(escapeStringValue(filter.getValue()));
    }


    /**
     * 拼接操作符号和别名
     *
     * @param stringBuffer
     * @param filter
     * @return
     */
    protected StringBuffer splitOperatorAndAlias(StringBuffer stringBuffer, Filter filter) {
        // 通用处理
        stringBuffer.append(BLANK_SPACE).append(filter.getOperator()).append(BLANK_SPACE);
        if (StrUtil.isNotBlank(filter.getAlias())) {
            stringBuffer.append(filter.getAlias()).append(".");
        }
        return stringBuffer;
    }


    /**
     * 针对字符串要做处理
     * ' % / 要做转义,并且 字符串 加上单引号，因为日期处理会有问题
     *
     * @return
     */
    protected Object escapeStringValue(Object value) {
        if (value instanceof String) {
            String str = (String) value;
            // 特殊字符转义
            str = StringEscape.escapeRawString(str);
            // 自动加单引号
            str = StringEscape.escapeString(str);
            return str;
        }
        return value;
    }
}
