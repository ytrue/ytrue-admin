package com.ytrue.infra.core.response;

import com.ytrue.infra.core.base.IServerResponseCode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Objects;

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


    public boolean isSuccess() {
        return Objects.equals(ResponseCodeEnum.SUCCESS.code(), this.code);
    }


    public static <T> ServerResponseEntity<T> success(T data) {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setData(data);
        serverResponseEntity.setCode(ResponseCodeEnum.SUCCESS.code());
        serverResponseEntity.setMessage(ResponseCodeEnum.SUCCESS.message());
        return serverResponseEntity;
    }

    public static ServerResponseEntity<Void> success() {
        ServerResponseEntity<Void> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setCode(ResponseCodeEnum.SUCCESS.code());
        serverResponseEntity.setMessage(ResponseCodeEnum.SUCCESS.message());
        return serverResponseEntity;
    }


    public static ServerResponseEntity<Void> fail(String msg) {
        ServerResponseEntity<Void> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setMessage(msg);
        serverResponseEntity.setCode(ResponseCodeEnum.CUSTOM_FAIL_MESSAGE.code());
        return serverResponseEntity;
    }

    public static ServerResponseEntity<Void> fail(String code, String msg) {
        ServerResponseEntity<Void> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setCode(code);
        serverResponseEntity.setMessage(msg);
        return serverResponseEntity;
    }


    public static ServerResponseEntity<Void> fail(IServerResponseCode responseCode) {
        ServerResponseEntity<Void> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setMessage(responseCode.message());
        serverResponseEntity.setCode(responseCode.code());
        return serverResponseEntity;
    }
}