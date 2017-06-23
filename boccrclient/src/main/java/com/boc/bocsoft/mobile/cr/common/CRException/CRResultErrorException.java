package com.boc.bocsoft.mobile.cr.common.CRException;

import com.boc.bocsoft.mobile.common.client.exception.HttpException;
import com.boc.bocsoft.mobile.cr.common.model.CRResponse;

/**
 * BII网络异常类
 * Created by Me on 2016/4/22.
 */
public class CRResultErrorException extends HttpException {


    public final static String ERROR_CODE_SESSION_INVALID = "validation.session_invalid";
    public final static String ERROR_CODE_SESSION_TIMEOUT = "validation.session_timeout";
    public final static String ERROR_CODE_ROLE_INVALID = "role.invalid_user";
    public final static String ERROR_CODE_PRODUCTEXCEPTION = "ProductException";


    public CRResultErrorException() {

    }

    public CRResultErrorException(String detailMessage) {
        super(detailMessage);
    }


    public CRResultErrorException(HttpException exception) {
        super(exception);
    }

    public CRResultErrorException(CRResponse response) {
        super(response.getMessage());
        setType(ExceptionType.RESULT);
        setErrorMessage(response.getMessage());
        if (null == response.getCode() || "".equals( response.getCode())) {
            setErrorCode(ERROR_CODE_PRODUCTEXCEPTION);
        } else {
            setErrorCode(response.getCode());
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
