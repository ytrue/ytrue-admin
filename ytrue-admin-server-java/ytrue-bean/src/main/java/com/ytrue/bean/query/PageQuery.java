package com.ytrue.bean.query;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author ytrue
 * @description: PageQuery
 * @date 2022/12/21 19:06
 */
@Data
public abstract class PageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 当前页码，默认是1
     */
    private Integer pageIndex = 1;

    /**
     * 当前页码，默认是10
     */
    private Integer pageSize = 10;


    /**
     * 获取pageIndex
     * @return
     */
    public Integer getPageIndex() {
        return Math.max(1, pageIndex != null ? pageIndex : 1);
    }

    /**
     * 获取pageSize
     * @return
     */
    public Integer getPageSize() {
        return Math.max(1, pageSize != null ? pageSize : 10);
    }


    /**
     * 组合page
     *
     * @return {@link IPage <?>}
     */
    public <T> IPage<T> page() {
        return new Page<>(getPageIndex(), getPageSize());
    }
}
