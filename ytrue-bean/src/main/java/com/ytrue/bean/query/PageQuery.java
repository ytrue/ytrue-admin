package com.ytrue.bean.query;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ytrue
 * @description: PageQuery
 * @date 2022/12/21 19:06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class PageQuery extends ListQuery {

    /**
     * 当前页码，默认是1
     */
    private Integer pageIndex = 1;

    /**
     * 当前页码，默认是10
     */
    private Integer pageSize = 10;


    /**
     * 组合page
     *
     * @return {@link IPage <?>}
     */
    public <T> IPage<T> page() {
        return new Page<>(pageIndex, pageSize);
    }
}
