package com.ytrue.tools.query.entity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author ytrue
 * @date 2022/4/20 16:09
 * @description 查询条件实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageQueryEntity<T> extends QueryEntity<T> implements Serializable {

    private static final long serialVersionUID = 6551142650282442009L;

    /**
     * 当前页码，默认是1
     */
    private Integer currentPage = 1;

    /**
     * 当前页码，默认是10
     */
    private Integer limit = 10;


    /**
     * 组合page
     *
     * @return {@link IPage<T>}
     */
    public IPage<T> getPage() {
        return new Page<>(currentPage, limit);

    }
}
