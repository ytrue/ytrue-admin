package com.ytrue.common.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author ytrue
 * @description: 自定义BaseMapper，重写其方法，便于扩展；之后的Mapper继承本接口
 * @date 2022/12/7 9:25
 */
public interface IBaseDao<T> extends BaseMapper<T> {
}
