package com.ytrue.infra.db.query.excption;


/**
 * @author ytrue
 * @date 2022/4/20 17:40
 * @description 查询条件匹配异常
 */
public class MatchException extends RuntimeException {

    private static final long serialVersionUID = -5564261593319882319L;

    public MatchException(String message) {
        super(message);
    }
}
