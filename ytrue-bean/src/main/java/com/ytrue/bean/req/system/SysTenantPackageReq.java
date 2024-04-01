package com.ytrue.bean.req.system;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ytrue.infra.core.ser.LongCollToStrSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * @author ytrue
 * @date 2023-06-28 14:50
 * @description SysTenantPackageReq
 */
@Data
public class SysTenantPackageReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 383926519508416659L;

    @Schema(description = "id")

    private Long id;

    @Schema(description = "套餐名称")
    private String name;


    @Schema(description = "状态:0=禁用,1=正常")
    private Boolean status;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "菜单id集合")
    @JsonSerialize(using = LongCollToStrSerializer.class)
    private Set<Long> menuIds;

    @Schema(description = "菜单树选择项是否关联显示:0=父子不互相关联显示,1=父子互相关联显示")
    private Boolean menuCheckStrictly;
}
