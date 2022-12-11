package com.ytrue.tools.log.event;


import com.ytrue.tools.log.enitiy.OperationLog;
import org.springframework.context.ApplicationEvent;

/**
 * @author ytrue
 * @date 2022/6/1 13:39
 * @description 系统日志事件
 */
public class SysLogEvent extends ApplicationEvent {


    private static final long serialVersionUID = 1984305710734463751L;

    public SysLogEvent(OperationLog source) {
        super(source);
    }
}
