package com.ytrue.common.excptions;

import com.ytrue.common.base.IBaseExceptionCode;
import com.ytrue.common.base.BaseCodeException;


/**
 * @author ytrue
 * @date 2022/5/28 10:10
 * @description BizException 业务异常
 */
public class BizException extends BaseCodeException {


    private static final long serialVersionUID = 1922140199999930024L;

    public BizException(String message) {
        super(message);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }


    public BizException(IBaseExceptionCode responseCode) {
        super(responseCode);
    }

}
