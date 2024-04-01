package com.ytrue.bean.dataobject.system;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ytrue.bean.dataobject.BaseIdEntity;
import com.ytrue.infra.core.constant.DateFormat;
import com.ytrue.infra.core.constant.TimeZone;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author ytrue
 * @description: SysRoleDept
 * @date 2022/12/7 15:50
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysRoleDept extends BaseIdEntity {

    private Long roleId;

    private Long deptId;
}
