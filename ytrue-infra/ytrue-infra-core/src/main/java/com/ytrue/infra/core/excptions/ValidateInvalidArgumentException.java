package com.ytrue.infra.core.excptions;

import com.ytrue.infra.core.base.BaseCodeException;
import com.ytrue.infra.core.base.IServerResponseCode;

import java.io.Serial;

public class ValidateInvalidArgumentException extends BaseCodeException {

    @Serial
    private static final long serialVersionUID = 2246414284819335465L;

    public ValidateInvalidArgumentException(IServerResponseCode responseCode) {
        super(responseCode);
    }

    public ValidateInvalidArgumentException(String message) {
        super(message);
    }
}
