package com.ytrue.manager;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ytrue.infra.security.util.SecurityUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public   class MpMetaObjectManager implements MetaObjectHandler {


    private final static String UPDATE_TIME = "updateTime";
    private final static String CREATE_TIME = "createTime";
    private final static String CREATE_BY = "createBy";
    private final static String UPDATE_BY = "updateBy";
    private final static String PRIMARY_KEY = "id";

    /**
     * 新增时自动填充
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        if (SecurityUtil.getAuthentication() != null) {
            if (SecurityUtil.isLogged()) {
                setFieldValByName(CREATE_BY, SecurityUtil.getLoginUser().getUsername(), metaObject);
            }
        }
        setFieldValByName(CREATE_TIME, LocalDateTime.now(), metaObject);
        setFieldValByName(UPDATE_TIME, LocalDateTime.now(), metaObject);


//            // 关于主键的写入
//            if (metaObject.hasGetter(PRIMARY_KEY)) {
//                //如果已经配置id，则不再写入
//                if (metaObject.getValue(PRIMARY_KEY) == null) {
//                    // 还是数字
//                    this.setFieldValByName(PRIMARY_KEY, SnowFlake.getId(), metaObject);
//                }
//            }
    }

    /**
     * 更新时自动填充
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        if (SecurityUtil.getAuthentication() != null) {
            if (SecurityUtil.isLogged()) {
                setFieldValByName(UPDATE_BY, SecurityUtil.getLoginUser().getUsername(), metaObject);
            }
        }
        setFieldValByName(UPDATE_TIME, LocalDateTime.now(), metaObject);
    }
}
