package com.ytrue.infra.core.excptions;

import com.ytrue.infra.core.base.BaseCodeException;
import com.ytrue.infra.core.base.IServerResponseInfo;

import java.io.Serial;

/**
 * BizException 类表示业务异常，用于处理业务逻辑中的错误情况。
 *
 * <p>
 * 该异常用于封装业务逻辑中出现的错误信息，并提供多种构造函数以支持不同的异常场景。
 * </p>
 *
 * @author ytrue
 * @date 2022/5/28 10:10
 */
public class BizException extends BaseCodeException {

    @Serial
    private static final long serialVersionUID = 1922140199999930024L;

    /**
     * 使用指定的错误消息创建 BizException 实例。
     *
     * @param message 错误消息
     */
    public BizException(String message) {
        super(message);
    }

    /**
     * 使用指定的错误消息和引发该异常的原因创建 BizException 实例。
     *
     * @param message 错误消息
     * @param cause 引发该异常的原因
     */
    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 使用服务器响应信息接口的实现创建 BizException 实例。
     *
     * @param responseCode 响应信息接口的实现，包含状态码和消息
     */
    public BizException(IServerResponseInfo responseCode) {
        super(responseCode);
    }

}
