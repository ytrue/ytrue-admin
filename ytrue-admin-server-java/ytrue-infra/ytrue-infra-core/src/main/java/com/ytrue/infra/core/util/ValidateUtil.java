package com.ytrue.infra.core.util;

import com.ytrue.infra.core.base.IServerResponseInfo;
import com.ytrue.infra.core.excptions.ValidateInvalidArgumentException;

import java.util.Objects;

public class ValidateUtil {


    public static <T> T notNull(final T object, IServerResponseInfo responseCode) {
        if (Objects.isNull(object)) {
            reportInvalidArgument(responseCode);
        }
        return object;
    }



    private static void reportInvalidArgument(IServerResponseInfo responseCode) {
        throw new ValidateInvalidArgumentException(responseCode);
    }
}
