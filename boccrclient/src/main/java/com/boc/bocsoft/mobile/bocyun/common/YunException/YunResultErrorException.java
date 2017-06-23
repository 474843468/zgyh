package com.boc.bocsoft.mobile.bocyun.common.YunException;

import com.boc.bocsoft.mobile.bocyun.common.model.YunResponse;
import com.boc.bocsoft.mobile.common.client.exception.HttpException;

/**
 * BII网络异常类
 * Created by Me on 2016/4/22.
 */
public class YunResultErrorException extends HttpException {


    public YunResultErrorException() {

    }

    public YunResultErrorException(String detailMessage) {
        super(detailMessage);
    }


    public YunResultErrorException(HttpException exception) {
        super(exception);
    }

    public YunResultErrorException(YunResponse response) {
        super(response.getReturnMessage());
        setType(ExceptionType.RESULT);

        setErrorMessage(response.getReturnMessage());
        setErrorCode(response.getReturnCode());

        setErrorType("接口错误");
    }

    /*message 错误信息*/
    private String errorMessage;
    /**type 错误类型*/
    private String errorType;


    private String errorCode;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
