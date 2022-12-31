package com.ytrue.tools.query.builder;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.ytrue.tools.query.builder.strategy.AdditionalCondition;
import com.ytrue.tools.query.builder.strategy.AdditionalConditionFactory;
import com.ytrue.tools.query.entity.Filter;
import com.ytrue.tools.query.enums.Operator;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

import java.io.StringReader;
import java.util.Set;

/**
 * @author ytrue
 * @date 2022/9/2 09:25
 * @description 对sql语句附加条件处理
 */
public class AdditionalSqlCondition {


    /**
     * 追加条件
     *
     * @param sql
     * @param filters
     * @return
     * @throws JSQLParserException
     */
    public String appendWhereCondition(String sql, Set<Filter> filters) throws JSQLParserException {

        String appendWhereConditionString = getCondExpression(filters);

        if (StrUtil.isBlank(appendWhereConditionString)) {
            return sql;
        }

        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Statement statement = parserManager.parse(new StringReader(sql));

        if (statement instanceof Select) {
            return parseSelect((Select) statement, appendWhereConditionString);
        } else if (statement instanceof Update) {
            return parseUpdate((Update) statement, appendWhereConditionString);
        } else if (statement instanceof Delete) {
            return parseDelete((Delete) statement, appendWhereConditionString);
        } else {
            return sql;
        }
    }


    /**
     * 针对 Delete 类型的处理
     *
     * @param delete
     * @param whereString
     * @return
     * @throws JSQLParserException
     */
    private String parseDelete(Delete delete, String whereString) throws JSQLParserException {

        final Expression expression = delete.getWhere();
        final Expression envCondition = CCJSqlParserUtil.parseCondExpression(whereString);
        if (expression == null) {
            delete.setWhere(envCondition);
        } else {
            AndExpression andExpression = new AndExpression(expression, envCondition);
            delete.setWhere(andExpression);
        }

        return delete.toString();
    }

    /**
     * 针对 Update 类型的处理
     *
     * @param update
     * @param whereString
     * @return
     * @throws JSQLParserException
     */
    private String parseUpdate(Update update, String whereString) throws JSQLParserException {

        final Expression expression = update.getWhere();
        final Expression envCondition = CCJSqlParserUtil.parseCondExpression(whereString);
        if (expression == null) {
            update.setWhere(envCondition);
        } else {
            AndExpression andExpression = new AndExpression(expression, envCondition);
            update.setWhere(andExpression);
        }

        return update.toString();
    }


    /**
     * 针对 Select 类型的处理
     *
     * @param select
     * @param whereString
     * @return
     */
    private String parseSelect(Select select, String whereString) throws JSQLParserException {
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        final Expression expression = plainSelect.getWhere();
        final Expression envCondition = CCJSqlParserUtil.parseCondExpression(whereString);

        if (expression == null) {
            plainSelect.setWhere(envCondition);
        } else {
            AndExpression andExpression = new AndExpression(expression, envCondition);
            plainSelect.setWhere(andExpression);
        }

        return select.toString();
    }


    /**
     * 处理 filters 转成字符串
     *
     * @param filters
     * @return
     */
    private String getCondExpression(Set<Filter> filters) {

        //要判断一下是否为空，不然会报空指针异常
        if (CollUtil.isEmpty(filters)) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();

        filters.forEach(filter -> {
            // 进行匹配
            AdditionalCondition additionalCondition = AdditionalConditionFactory.getInstance(filter.getCondition());
            // 对value是字符串进行处理
            additionalCondition.additional(stringBuffer, filter);
        });
        String s = stringBuffer.toString().trim();
        //删除前面的 and 或者是 or
        String and = Operator.and.name();
        String or = Operator.or.name();

        if (s.startsWith(and)) {
            s = s.substring(and.length() + 1);
        }

        if (s.startsWith(or)) {
            s = s.substring(or.length() + 1);
        }
        return s;
    }
}
