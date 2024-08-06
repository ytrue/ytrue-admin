package com.ytrue.serviceimpl.system;

import com.ytrue.bean.dataobject.system.SysLog;
import com.ytrue.infra.core.util.BeanUtils;
import com.ytrue.infra.db.base.BaseServiceImpl;
import com.ytrue.dao.system.SysLogDao;
import com.ytrue.infra.security.util.SecurityUtil;
import com.ytrue.service.system.SysLogService;
import com.ytrue.infra.log.enitiy.OperationLog;
import com.ytrue.infra.log.event.OperateLogEvent;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * @author ytrue
 * @description: SysLogServiceImpl
 * @date 2022/12/9 10:21
 */
@Service
public class SysLogServiceImpl extends BaseServiceImpl<SysLogDao, SysLog> implements SysLogService, ApplicationListener<OperateLogEvent> {

    /**
     * 监听日志处理
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(OperateLogEvent event) {
        OperationLog optLog = (OperationLog) event.getSource();

        SysLog sysLog = BeanUtils.copyProperties(optLog, SysLog::new);

        // 可能存在没有登录的情况
        if (SecurityUtil.getAuthentication() != null) {
            // 用户名
            String operator = SecurityUtil.getLoginUser().getUsername();
            sysLog.setOperator(operator);
        }

        // 这里自动填充会失效有待考究
        //sysLog.setId(SnowFlake.getId());
        // 保存日志
        this.save(sysLog);
    }
}
