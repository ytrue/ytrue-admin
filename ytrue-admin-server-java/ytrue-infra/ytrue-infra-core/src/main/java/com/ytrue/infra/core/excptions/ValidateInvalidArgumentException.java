package com.ytrue.infra.core.excptions;

import com.ytrue.infra.core.base.BaseCodeException;
import com.ytrue.infra.core.base.IServerResponseInfo;

import java.io.Serial;

public class ValidateInvalidArgumentException extends BaseCodeException {

    @Serial
    private static final long serialVersionUID = 2246414284819335465L;

    public ValidateInvalidArgumentException(IServerResponseInfo responseCode) {
        super(responseCode);
    }

    public ValidateInvalidArgumentException(String message) {
        super(message);
    }
}
