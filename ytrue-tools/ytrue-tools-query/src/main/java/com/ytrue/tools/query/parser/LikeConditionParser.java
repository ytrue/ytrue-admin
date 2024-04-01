package com.ytrue.tools.query.parser;

import com.baomidou.mybatisplus.core.toolkit.sql.StringEscape;
import com.ytrue.tools.query.entity.Filter;
import com.ytrue.tools.query.util.ExpressionGenerator;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;

/**
 * @author ytrue
 * @date 2023-08-25 16:05
 * @description LikeConditionParser
 */
public class LikeConditionParser implements ConditionParser {

    @Override
    public Expression parser(Filter filter) throws Exception {
        // 创建左侧表达式
        Expression leftExpression = ExpressionGenerator.generateExpression(filter.getColumnAlias());


        // 这个模糊查询基本都是字符串
        Object value = filter.getValue();
        if (value instanceof String str) {
            value = StringEscape.escapeRawString(str);
        } else if (value instanceof Boolean b) {
            // 这里强转数字
            // 布尔类型,这里要转换成数组，true = 1, false = 0
            value = b ? 1L : 0L;
        }

        String newValue = switch (filter.getCondition()) {
            case like -> "'%" + value + "%'";
            case likeLeft -> "'" + value + "%'";
            case likeRight -> "'%" + value + "'";
            default -> throw new RuntimeException("类型错误");
        };

        // 创建 LikeExpression
        BinaryExpression likeExpression = new LikeExpression();
        likeExpression.setLeftExpression(leftExpression);
        likeExpression.setRightExpression(new StringValue(newValue));
        return likeExpression;
    }
}
