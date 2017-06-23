package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model;

/**
 * Created by xintong on 2016/7/5.
 */
public class ErrorException {

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
