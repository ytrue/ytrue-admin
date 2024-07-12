package com.ytrue.infra.core.excptions;


import com.ytrue.infra.core.base.BaseCodeException;
import com.ytrue.infra.core.base.IServerResponseInfo;

import java.io.Serial;

/**
 * @author ytrue
 * @date 2022/5/28 10:10
 * @description BizException 业务异常
 */
public class BizException extends BaseCodeException {


    @Serial
    private static final long serialVersionUID = 1922140199999930024L;

    public BizException(String message) {
        super(message);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }


    public BizException(IServerResponseInfo responseCode) {
        super(responseCode);
    }

}
