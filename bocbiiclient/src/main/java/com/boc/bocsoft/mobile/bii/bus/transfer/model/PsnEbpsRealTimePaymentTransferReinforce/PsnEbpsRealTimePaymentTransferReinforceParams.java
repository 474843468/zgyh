package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentTransferReinforce;

/**
 * Created by WM on 2016/6/24.
 */
public class PsnEbpsRealTimePaymentTransferReinforceParams {

    private String	transactionId	;
    private String	batSequence	;
    private String	transMonStatus	;
    private String	questionId	;
    private String	questionText	;
    private String	CredentialNullFlag	;

    public String getBatSequence() {
        return batSequence;
    }

    public void setBatSequence(String batSequence) {
        this.batSequence = batSequence;
    }

    public String getCredentialNullFlag() {
        return CredentialNullFlag;
    }

    public void setCredentialNullFlag(String credentialNullFlag) {
        CredentialNullFlag = credentialNullFlag;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransMonStatus() {
        return transMonStatus;
    }

    public void setTransMonStatus(String transMonStatus) {
        this.transMonStatus = transMonStatus;
    }
}
