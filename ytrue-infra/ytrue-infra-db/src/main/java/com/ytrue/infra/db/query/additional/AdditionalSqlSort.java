package com.ytrue.infra.db.query.additional;

import cn.hutool.core.collection.CollUtil;
import com.ytrue.infra.db.query.entity.Sort;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.springframework.util.CollectionUtils;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author ytrue
 * @description: 追加sql排序
 * @date 2022/12/22 9:41
 */
@Slf4j
public class AdditionalSqlSort {


    /**
     * 追加排序
     *
     * @param sql
     * @param sorts
     * @return
     * @throws JSQLParserException
     */
    public String appendSort(String sql, Set<Sort> sorts) throws JSQLParserException {
        //要判断一下是否为空，不然会报空指针异常
        if (CollUtil.isEmpty(sorts)) {
            return sql;
        }

        List<OrderByElement> orderByElements = this.getOrderByElement(sorts);

        if (CollectionUtils.isEmpty(orderByElements)) {
            return sql;
        }

        CCJSqlParserManager parserManager = new CCJSqlParserManager();

        log.info("AdditionalSqlSort parse before sql {}", sql);

        Statement statement = parserManager.parse(new StringReader(sql));

        if (statement instanceof Select select) {
            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();

            List<OrderByElement> oldOrderByElements = plainSelect.getOrderByElements();

            // 没有
            if (CollectionUtils.isEmpty(oldOrderByElements)) {
                plainSelect.setOrderByElements(orderByElements);
            } else {
                orderByElements.addAll(oldOrderByElements);
                plainSelect.setOrderByElements(orderByElements);
            }
            return select.toString();
        }
        return sql;
    }


    /**
     * 获取OrderByElement
     *
     * @param sorts
     * @return
     * @throws JSQLParserException
     */
    public List<OrderByElement> getOrderByElement(Set<Sort> sorts) throws JSQLParserException {
        //要判断一下是否为空，不然会报空指针异常
        if (CollUtil.isEmpty(sorts)) {
            return null;
        }

        ArrayList<OrderByElement> orderByElements = new ArrayList<>();

        for (Sort sort : sorts) {
            OrderByElement orderByElement = new OrderByElement();

            String column = sort.getColumnAlias();
            orderByElement.setExpression(CCJSqlParserUtil.parseExpression(column));
            orderByElement.setAsc(sort.getAsc());
            orderByElements.add(orderByElement);
        }

        return orderByElements;
    }
}
