package com.ytrue.infra.mysql.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * @author ytrue
 * @description 自定义 BaseMapper 接口，重写其方法以便于扩展；之后的 Mapper 应该继承此接口。
 * @date 2022/12/7 9:25
 * @param <T> 实体类类型
 */
public interface IBaseDao<T> extends BaseMapper<T> {

    /**
     * 批量插入实体列表。
     *
     * @param batchList 实体对象集合
     * @return 插入成功的记录数量
     */
    int insertAllBatch(@Param("list") Collection<T> batchList);

    /**
     * 批量插入，仅适用于 MySQL 数据库。
     *
     * @param entityList 实体对象集合
     * @return 插入成功的记录数量
     */
    Integer insertBatchSomeColumn(Collection<T> entityList);
}
