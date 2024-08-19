package com.ytrue.infra.db.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * @author ytrue
 * @description: 自定义BaseMapper，重写其方法，便于扩展；之后的Mapper继承本接口
 * @date 2022/12/7 9:25
 */
public interface IBaseDao<T> extends BaseMapper<T> {

    /**
     * 批量插入
     *
     * @param batchList
     * @return
     */
    int insertAllBatch(@Param("list") Collection<T> batchList);


    /**
     * 批量插入 仅适用于mysql
     *
     * @param entityList
     * @return
     */
    Integer insertBatchSomeColumn(Collection<T> entityList);
}
