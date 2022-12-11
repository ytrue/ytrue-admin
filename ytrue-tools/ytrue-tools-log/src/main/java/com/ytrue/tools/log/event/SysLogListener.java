package com.ytrue.tools.log.event;


import com.ytrue.tools.log.enitiy.OperationLog;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import java.util.function.Consumer;

/**
 * @author ytrue
 * @date 2022/6/1 13:39
 * @description 异步监听日志事件
 */
@Slf4j
@AllArgsConstructor
public class SysLogListener {

    private final Consumer<OperationLog> consumer;

    @Async
    @Order
    @EventListener(SysLogEvent.class)
    public void saveSysLog(SysLogEvent event) {
        OperationLog optLog = (OperationLog) event.getSource();
        consumer.accept(optLog);
    }
}
