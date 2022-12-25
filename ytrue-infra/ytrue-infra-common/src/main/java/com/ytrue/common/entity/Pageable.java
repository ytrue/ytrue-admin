package com.ytrue.common.entity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ytrue
 * @description: Pageable
 * @date 2022/12/21 19:06
 */
@Data
public class Pageable implements Serializable {

    private static final long serialVersionUID = -6949451396264513939L;

    /**
     * 当前页码，默认是1
     */
    private Integer page = 1;

    /**
     * 当前页码，默认是10
     */
    private Integer limit = 10;

    /**
     * 组合page
     *
     * @return {@link IPage <?>}
     */
    public <T> IPage<T> page() {
        return new Page<>(page, limit);
    }
}
