package com.ytrue.infra.mysql.base;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Collection;

/**
 * @author ytrue
 * @description BaseServiceImpl 提供基本的服务实现类，继承自 MyBatis-Plus 的 ServiceImpl。
 * @date 2022/12/7 9:24
 * @param <M> DAO 接口类型
 * @param <T> 实体类类型
 */
public abstract class BaseServiceImpl<M extends IBaseDao<T>, T> extends ServiceImpl<M, T> implements IBaseService<T> {

    /**
     * 重写 getOne 方法，添加 "LIMIT 1" 以防止出现异常。
     * <p>
     * 该方法调用会自动处理多个条件，避免重复添加 "LIMIT 1"。
     * </p>
     *
     * @param wrapper 查询条件封装
     * @return 单个实体对象
     */
    @Override
    public T getOne(Wrapper<T> wrapper) {
        AbstractWrapper<T, SFunction<T, ?>, ?> abstractWrapper = (AbstractWrapper<T, SFunction<T, ?>, ?>) wrapper;
        abstractWrapper.last("LIMIT 1");
        return this.getOne(abstractWrapper, true);
    }

    /**
     * 批量插入实体列表。
     *
     * @param list 实体对象集合
     * @return 插入的记录数量
     */
    @Override
    public Integer insertAllBatch(Collection<T> list) {
        return baseMapper.insertBatchSomeColumn(list);
    }
}
