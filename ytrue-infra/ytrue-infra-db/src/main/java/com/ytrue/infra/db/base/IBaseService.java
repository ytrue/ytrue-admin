package com.ytrue.infra.db.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;


import java.util.Collection;
import java.util.List;

/**
 * @author ytrue
 * @description: 继承MybatisPlus的IService，方便进行自定义和扩展
 * @date 2022/12/7 9:22
 */
public interface IBaseService<T> extends IService<T> {



    /**
     * 批量插入
     *
     * @param list 列表
     * @return 行数
     */
    Integer insertAllBatch(Collection<T> list);
}
