package com.ytrue.bean.query;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ytrue.infra.db.query.util.QueryHelp;

import java.io.Serializable;

public abstract class ListQuery implements Serializable {

    /**
     * 获取query wrapper
     *
     * @return
     */
    public  <T> QueryWrapper<T> getQueryWrapper() {
        return QueryHelp.buildQueryWrapper(this);
    }

    /**
     * 获取 LambdaQueryWrapper
     *
     * @return
     */
    public <T> LambdaQueryWrapper<T> getLambdaQueryWrapper() {
        return QueryHelp.builderlambdaQueryWrapper(this);
    }

}
