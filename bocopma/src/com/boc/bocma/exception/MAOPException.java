package com.boc.bocma.exception;

public class MAOPException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public static final String MSG_CODE_KEY = "msgcde";
    public static final String RTN_MSG_KEY = "rtnmsg";
    
    private static final String PROGRAM_ERROR = "program error";
    
    public MAOPException(Exception e) {
        msgCode = e.getCause() == null ? PROGRAM_ERROR : e.getCause().getMessage();
        rtnMsg = e.getMessage();
    }
    
    public MAOPException(MAOPException e) {
        msgCode = e.getMsgCode();
        rtnMsg = e.getMessage();
    }

    public MAOPException() {
    }

    private String msgCode;
    private String rtnMsg;
    
    
    public String getMsgCode() {
        return msgCode;
    }
    
    @Override
    public String getMessage() {
        return rtnMsg;
    }
    
    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }
    
    public void setMessage(String rtnMsg) {
        this.rtnMsg = rtnMsg;
    }
}
