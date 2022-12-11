package com.ytrue.config;

import com.ytrue.common.utils.BeanUtils;
import com.ytrue.modules.system.dao.SysLogDao;
import com.ytrue.modules.system.model.SysLog;
import com.ytrue.tools.log.enitiy.OperationLog;
import com.ytrue.tools.log.event.SysLogListener;
import com.ytrue.tools.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author ytrue
 * @date 2022/6/1 15:40
 * @description 日志配置类
 */
@Slf4j
@Configuration
public class SysOperateLogConfiguration {

    /**
     * 自动配置日志监听器组件
     *
     * @param logService
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public SysLogListener sysLogListener(LogService logService) {
        return new SysLogListener(logService::saveLog);
    }


    @Slf4j
    @Component
    @AllArgsConstructor
    public static class LogService {

        private final SysLogDao sysLogDao;

        /**
         * 保存操作日志
         *
         * @param operationLog
         */
        public void saveLog(OperationLog operationLog) {
            SysLog sysLog = BeanUtils.cgLibCopyBean(operationLog, SysLog::new);
            // 可能存在没有登录的情况
            if (SecurityUtils.getAuthentication() != null) {
                // 用户名
                String operator = SecurityUtils.getLoginUser().getUsername();
                sysLog.setOperator(operator);
            }
            sysLogDao.insert(sysLog);
        }
    }

}
