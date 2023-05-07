package com.ytrue.db.mybatis.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ytrue.tools.query.entity.PageQueryEntity;
import com.ytrue.tools.query.entity.QueryEntity;

import java.util.List;

/**
 * @author ytrue
 * @description: 继承MybatisPlus的IService，方便进行自定义和扩展
 * @date 2022/12/7 9:22
 */
public interface IBaseService<T> extends IService<T> {

    /**
     * 分页查询
     *
     * @param pageQueryEntity
     * @return
     */
    IPage<T> paginate(PageQueryEntity pageQueryEntity);

    /**
     * 条件列表查询
     *
     * @param queryEntity
     * @return
     */
    List<T> list(QueryEntity queryEntity);
}
