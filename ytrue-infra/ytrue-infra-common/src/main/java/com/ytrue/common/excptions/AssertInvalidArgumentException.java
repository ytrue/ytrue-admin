package com.ytrue.common.excptions;

import com.ytrue.common.base.BaseCodeException;
import com.ytrue.common.base.IBaseExceptionCode;


/**
 * @author ytrue
 * @date 2022/4/21 16:47
 * @description 断言匹配错误
 */
public class AssertInvalidArgumentException extends BaseCodeException {

    private static final long serialVersionUID = 2246414284819335465L;

    public AssertInvalidArgumentException(IBaseExceptionCode responseCode) {
        super(responseCode);
    }

    public AssertInvalidArgumentException(String message) {
        super(5000, message);
    }
}
