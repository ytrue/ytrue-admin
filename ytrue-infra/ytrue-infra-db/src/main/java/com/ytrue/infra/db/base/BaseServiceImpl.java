package com.ytrue.infra.db.base;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Collection;

/**
 * @author ytrue
 * @description: BaseServiceImpl
 * @date 2022/12/7 9:24
 */
public abstract class BaseServiceImpl<M extends IBaseDao<T>, T> extends ServiceImpl<M, T> implements IBaseService<T> {


    /**
     * 这个在tools-query库有做针对处理,这里只是演示下
     * description:重写getOne方法，加上"LIMIT 1"，防止出现异常
     * 不用担心存在多个abstractWrapper.last("LIMIT 1")，默认已经处理，不会出现重复
     *
     * @param wrapper /
     * @return /
     */
    @Override
    public T getOne(Wrapper<T> wrapper) {
        AbstractWrapper<T, SFunction<T, ?>, ?> abstractWrapper = (AbstractWrapper<T, SFunction<T, ?>, ?>) wrapper;
        abstractWrapper.last("LIMIT 1");
        return this.getOne(abstractWrapper, true);
    }


    /**
     * 批量插入
     *
     * @param list 列表
     * @return
     */
    @Override
    public Integer insertAllBatch(Collection<T> list) {
        return baseMapper.insertBatchSomeColumn(list);
    }
}
