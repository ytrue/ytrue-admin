package com.ytrue.infra.core.excptions;


import com.ytrue.infra.core.base.BaseCodeException;
import com.ytrue.infra.core.base.IServerResponseCode;

import java.io.Serial;

/**
 * @author ytrue
 * @date 2022/4/21 16:47
 * @description 断言匹配错误
 */
public class AssertInvalidArgumentException extends BaseCodeException {

    @Serial
    private static final long serialVersionUID = 2246414284819335465L;

    public AssertInvalidArgumentException(IServerResponseCode responseCode) {
        super(responseCode);
    }

    public AssertInvalidArgumentException(String message) {
        super(message);
    }
}
