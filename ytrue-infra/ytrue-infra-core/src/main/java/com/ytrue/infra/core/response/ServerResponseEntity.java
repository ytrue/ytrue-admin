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
        return Objects.equals(ResponseCodeEnum.SUCCESS.getCode(), this.code);
    }


    public static <T> ServerResponseEntity<T> success(T data) {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setData(data);
        serverResponseEntity.setCode(ResponseCodeEnum.SUCCESS.getCode());
        serverResponseEntity.setMessage(ResponseCodeEnum.SUCCESS.getMessage());
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> success() {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setCode(ResponseCodeEnum.SUCCESS.getCode());
        serverResponseEntity.setMessage(ResponseCodeEnum.SUCCESS.getMessage());
        return serverResponseEntity;
    }


    public static <T> ServerResponseEntity<T> fail(String msg) {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setMessage(msg);
        serverResponseEntity.setCode(ResponseCodeEnum.CUSTOM_FAIL_MESSAGE.getCode());
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> fail(String code, String msg) {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setCode(code);
        serverResponseEntity.setMessage(msg);
        return serverResponseEntity;
    }


    public static <T> ServerResponseEntity<T> fail(IServerResponseCode responseCode) {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setMessage(responseCode.getMessage());
        serverResponseEntity.setCode(responseCode.getCode());
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> fail(IServerResponseCode responseCode, T data) {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setMessage(responseCode.getMessage());
        serverResponseEntity.setCode(responseCode.getCode());
        serverResponseEntity.setData(data);
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> transform(ServerResponseEntity<?> oldServerResponseEntity) {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setMessage(oldServerResponseEntity.getMessage());
        serverResponseEntity.setCode(oldServerResponseEntity.getCode());
        return serverResponseEntity;
    }

}