package com.boc.bocsoft.mobile.bocyun.common.model;

/**
 * BII的错误信息
 * Created by XieDu on 2016/3/1.
 */
public class YunError {
    /**
     * 错误信息描述
     */
    private String message;
    /**
     * 异常类型
     */
    private String type;
    /**
     * 错误码
     */
    private String code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public YunError(String message, String type, String code) {
        this.message = message;
        this.type = type;
        this.code = code;
    }
}
