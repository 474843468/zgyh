package com.boc.bocsoft.mobile.wfss.common.exception;

import com.boc.bocsoft.mobile.common.client.exception.HttpException;
import com.boc.bocsoft.mobile.wfss.common.model.WFSSResponse;

/**
 * BII网络异常类
 * Created by Me on 2016/4/22.
 */
public class WFSSResultErrorException extends HttpException {


    public final static String ERROR_CODE_SESSION_INVALID = "validation.session_invalid";
    public final static String ERROR_CODE_SESSION_TIMEOUT = "validation.session_timeout";
    public final static String ERROR_CODE_ROLE_INVALID = "role.invalid_user";
    public final static String ERROR_CODE_PRODUCTEXCEPTION = "ProductException";


    public WFSSResultErrorException() {

    }

    public WFSSResultErrorException(String detailMessage) {
        super(detailMessage);
    }


    public WFSSResultErrorException(HttpException exception) {
        super(exception);
    }

    public WFSSResultErrorException(WFSSResponse response) {
        super(response.getHead().getResult());
        setType(ExceptionType.RESULT);
        setErrorMessage(response.getHead().getResult());
        if (null == response.getHead().getStat() || "".equals( response.getHead().getStat())) {
            setErrorCode(ERROR_CODE_PRODUCTEXCEPTION);
        } else {
            setErrorCode(response.getHead().getStat());
        }

        setErrorType(response.getType());
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
