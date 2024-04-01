package com.ytrue.tools.query.util;


import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;

/**
 * @author ytrue
 * @date 2023-08-25 14:36
 * @description ExpressionGenerator
 */
public class ExpressionGenerator {

    /**
     * 暂时提供这么多类型
     *
     * @param value
     * @return
     * @throws Exception
     */
    public static Expression generateExpression(Object value) throws Exception {
        Expression expression;
        if (value instanceof String str) {
            // 字符串类型
            expression = new StringValue(str);
        } else if (value instanceof Boolean b) {
            // 布尔类型,这里要转换成数组，true = 1, false = 0
            long i = b ? 1L : 0L;
            expression = new LongValue(i);
        } else if (value instanceof Double) {
            // 浮点类型
            return new DoubleValue(String.valueOf(value));
        } else if (value instanceof Number) {
            // 数字类型
            expression = new LongValue(((Number) value).longValue());
        } else {
            throw new IllegalArgumentException("Unsupported object type: " + value.getClass());
        }
        return expression;
    }

}
