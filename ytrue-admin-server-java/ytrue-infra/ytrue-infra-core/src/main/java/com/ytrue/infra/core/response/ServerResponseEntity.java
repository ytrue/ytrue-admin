package com.ytrue.infra.core.response;

import com.ytrue.infra.core.base.IServerResponseInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Objects;

/**
 * ServerResponseEntity 类用于封装服务器响应的信息，包括状态码、消息和数据。
 *
 * @param <T> 响应数据的类型
 * @author ytrue
 */
@Slf4j
@Data
public class ServerResponseEntity<T> implements Serializable {

    /**
     * 状态码
     */
    private String code;

    /**
     * 信息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    /**
     * 判断当前响应是否成功。
     *
     * @return 如果状态码为成功，则返回 true；否则返回 false
     */
    public boolean isSuccess() {
        return Objects.equals(ServerResponseInfoEnum.OK.code(), this.code);
    }

    /**
     * 创建一个成功响应，包含数据。
     *
     * @param data 响应数据
     * @param <T>  响应数据的类型
     * @return 成功的 ServerResponseEntity 实例
     */
    public static <T> ServerResponseEntity<T> success(T data) {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setData(data);
        serverResponseEntity.setCode(ServerResponseInfoEnum.OK.code());
        serverResponseEntity.setMessage(ServerResponseInfoEnum.OK.message());
        return serverResponseEntity;
    }

    /**
     * 创建一个成功响应，不包含数据。
     *
     * @return 成功的 ServerResponseEntity 实例
     */
    public static ServerResponseEntity<Void> success() {
        ServerResponseEntity<Void> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setCode(ServerResponseInfoEnum.OK.code());
        serverResponseEntity.setMessage(ServerResponseInfoEnum.OK.message());
        return serverResponseEntity;
    }

    /**
     * 创建一个失败响应，使用默认错误码。
     *
     * @param msg 错误消息
     * @return 失败的 ServerResponseEntity 实例
     */
    public static ServerResponseEntity<Void> fail(String msg) {
        ServerResponseEntity<Void> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setMessage(msg);
        serverResponseEntity.setCode(ServerResponseInfoEnum.CUSTOM_FAIL_MESSAGE.code());
        return serverResponseEntity;
    }

    /**
     * 创建一个失败响应，自定义状态码和消息。
     *
     * @param code 自定义状态码
     * @param msg  自定义错误消息
     * @return 失败的 ServerResponseEntity 实例
     */
    public static ServerResponseEntity<Void> fail(String code, String msg) {
        ServerResponseEntity<Void> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setCode(code);
        serverResponseEntity.setMessage(msg);
        return serverResponseEntity;
    }

    /**
     * 创建一个失败响应，使用 IServerResponseInfo 中的状态码和消息。
     *
     * @param responseCode 响应信息接口实现
     * @return 失败的 ServerResponseEntity 实例
     */
    public static ServerResponseEntity<Void> fail(IServerResponseInfo responseCode) {
        ServerResponseEntity<Void> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setMessage(responseCode.message());
        serverResponseEntity.setCode(responseCode.code());
        return serverResponseEntity;
    }
}
