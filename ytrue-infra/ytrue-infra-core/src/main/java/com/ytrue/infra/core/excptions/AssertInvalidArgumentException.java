package com.ytrue.infra.core.excptions;


import com.ytrue.infra.core.base.BaseCodeException;
import com.ytrue.infra.core.base.IServerResponseInfo;

import java.io.Serial;

/**
 * @author ytrue
 * @date 2022/4/21 16:47
 * @description 断言匹配错误
 */
public class AssertInvalidArgumentException extends BaseCodeException {

    @Serial
    private static final long serialVersionUID = 2246414284819335465L;

    public AssertInvalidArgumentException(IServerResponseInfo responseCode) {
        super(responseCode);
    }

    public AssertInvalidArgumentException(String message) {
        super(message);
    }
}
