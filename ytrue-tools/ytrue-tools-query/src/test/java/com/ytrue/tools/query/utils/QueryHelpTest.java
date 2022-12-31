package com.ytrue.tools.query.utils;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.sql.StringEscape;
import com.ytrue.tools.query.builder.AdditionalSqlCondition;
import com.ytrue.tools.query.entity.Filter;
import com.ytrue.tools.query.enums.QueryMethod;
import net.sf.jsqlparser.JSQLParserException;
import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;

class QueryHelpTest {


    @Test
    public void test01() throws JSQLParserException {

        LinkedHashSet<Filter> set = new LinkedHashSet<>();

        Filter filter1 = new Filter("name", QueryMethod.eq, "123");
        Filter filter2 = new Filter("name", QueryMethod.in, CollectionUtil.newHashSet(1,1,2,21));


        set.add(filter1);
        set.add(filter2);


        AdditionalSqlCondition additionalSqlCondition = new AdditionalSqlCondition();

        String sql = "select * from tmp";

        String s = additionalSqlCondition.appendWhereCondition(sql, set);


        System.out.println(s);




    }

}