package com.ytrue.serviceimpl.system;

import com.ytrue.bean.dataobject.system.SysLog;
import com.ytrue.infra.db.base.BaseServiceImpl;
import com.ytrue.infra.db.dao.system.SysLogDao;
import com.ytrue.service.system.SysLogService;
import com.ytrue.tools.log.enitiy.OperationLog;
import com.ytrue.tools.log.event.SysLogEvent;
import com.ytrue.tools.security.util.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * @author ytrue
 * @description: SysLogServiceImpl
 * @date 2022/12/9 10:21
 */
@Service
public class SysLogServiceImpl extends BaseServiceImpl<SysLogDao, SysLog> implements SysLogService, ApplicationListener<SysLogEvent> {

    /**
     * 监听日志处理
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(SysLogEvent event) {
        OperationLog optLog = (OperationLog) event.getSource();

        SysLog sysLog = new SysLog();
        BeanUtils.copyProperties(optLog, sysLog);

        // 可能存在没有登录的情况
        if (SecurityUtils.getAuthentication() != null) {
            // 用户名
            String operator = SecurityUtils.getLoginUser().getUsername();
            sysLog.setOperator(operator);
        }

        // 这里自动填充会失效有待考究
        //sysLog.setId(SnowFlake.getId());
        // 保存日志
        this.save(sysLog);
    }
}
