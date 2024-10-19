package com.ytrue.infra.mysql.base;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;

/**
 * @author ytrue
 * @description 继承 MybatisPlus 的 IService 接口，以方便进行自定义和扩展。
 * @date 2022/12/7 9:22
 * @param <T> 实体类类型
 */
public interface IBaseService<T> extends IService<T> {

    /**
     * 批量插入实体列表。
     *
     * @param list 实体对象集合
     * @return 插入成功的行数
     */
    Integer insertAllBatch(Collection<T> list);
}
