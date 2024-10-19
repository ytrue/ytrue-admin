package com.ytrue.infra.operationlog.event;



import com.ytrue.infra.operationlog.enitiy.OperationLog;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;

/**
 * @author ytrue
 * @date 2022/6/1 13:39
 * @description 系统日志事件
 */
public class OperateLogEvent extends ApplicationEvent {


    @Serial
    private static final long serialVersionUID = 1984305710734463751L;

    public OperateLogEvent(OperationLog source) {
        super(source);
    }
}
