package com.sharing.common.exception;

import com.sharing.common.utils.MessageUtils;

/**
 * @author: Techmile tecsmile@outlook.com
 * @date: 2019/7/15
 * @description: 自定义异常
 */

public class DIYException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private int code;
    private String msg;

    public DIYException(int code) {
        this.code = code;
        this.msg = MessageUtils.getMessage(code);
    }

    public DIYException(int code, String... params) {
        this.code = code;
        this.msg = MessageUtils.getMessage(code, params);
    }

    public DIYException(int code, Throwable e) {
        super(e);
        this.code = code;
        this.msg = MessageUtils.getMessage(code);
    }

    public DIYException(int code, Throwable e, String... params) {
        super(e);
        this.code = code;
        this.msg = MessageUtils.getMessage(code, params);
    }

    public DIYException(String msg) {
        super(msg);
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.msg = msg;
    }

    public DIYException(String msg, Throwable e) {
        super(msg, e);
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
