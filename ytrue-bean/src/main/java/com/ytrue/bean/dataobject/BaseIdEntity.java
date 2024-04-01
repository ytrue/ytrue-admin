package com.ytrue.bean.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author ytrue
 * @date 2023-09-19 9:19
 * @description BaseIdEntity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseIdEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    @Schema(description = "唯一标识")
    private Long id;
}
