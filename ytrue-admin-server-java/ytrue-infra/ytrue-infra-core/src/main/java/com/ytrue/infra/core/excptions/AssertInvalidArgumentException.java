package com.ytrue.infra.core.excptions;

import com.ytrue.infra.core.base.BaseCodeException;
import com.ytrue.infra.core.base.IServerResponseInfo;

import java.io.Serial;

/**
 * AssertInvalidArgumentException 类表示断言匹配错误异常，用于处理方法参数的非法状态或不匹配的情况。
 *
 * <p>
 * 该异常用于封装在进行参数验证时发现不符合要求的情况，便于快速定位和处理错误。
 * </p>
 *
 * @author ytrue
 * @date 2022/4/21 16:47
 * @description 断言匹配错误
 */
public class AssertInvalidArgumentException extends BaseCodeException {

    @Serial
    private static final long serialVersionUID = 2246414284819335465L;

    /**
     * 使用服务器响应信息接口的实现创建 AssertInvalidArgumentException 实例。
     *
     * @param responseCode 响应信息接口的实现，包含状态码和消息
     */
    public AssertInvalidArgumentException(IServerResponseInfo responseCode) {
        super(responseCode);
    }

    /**
     * 使用指定的错误消息创建 AssertInvalidArgumentException 实例。
     *
     * @param message 错误消息
     */
    public AssertInvalidArgumentException(String message) {
        super(message);
    }
}
