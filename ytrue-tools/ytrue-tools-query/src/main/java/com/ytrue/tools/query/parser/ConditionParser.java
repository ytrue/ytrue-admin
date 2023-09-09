package com.ytrue.tools.query.parser;

import com.baomidou.mybatisplus.core.toolkit.sql.StringEscape;
import com.ytrue.tools.query.entity.Filter;
import net.sf.jsqlparser.expression.Expression;

/**
 * @author ytrue
 * @date 2023-08-25 14:24
 * @description ConditionParser
 */
public interface ConditionParser {

    /**
     * 解析
     *
     * @param filter
     * @return
     * @throws Exception
     */
    Expression parser(Filter filter) throws Exception;


    /**
     * 针对字符串要做处理
     * ' % / 要做转义,并且 字符串 加上单引号，因为日期处理会有问题
     *
     * @param value
     * @return
     */
    default Object escapeStringValue(Object value) {
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
