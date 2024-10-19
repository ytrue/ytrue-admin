package com.ytrue.infra.core.base;

/**
 *
 * @author ytrue
 * @date 2024/10/19
 * @description  服务器响应信息接口，定义了响应的状态码和消息。
 *
 * <p>
 * 该接口用于统一响应格式，方便在应用中处理不同的响应信息。
 * </p>
 */
public interface IServerResponseInfo {

    /**
     * 获取状态码。
     *
     * @return 返回响应的状态码
     */
    String code();

    /**
     * 获取响应消息。
     *
     * @return 返回响应的消息内容
     */
    String message();

}
