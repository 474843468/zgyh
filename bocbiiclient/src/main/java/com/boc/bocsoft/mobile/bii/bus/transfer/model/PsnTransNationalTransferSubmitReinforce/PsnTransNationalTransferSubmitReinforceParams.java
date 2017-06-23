package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalTransferSubmitReinforce;

/**
 * Created by WM on 2016/6/16.
 */
public class PsnTransNationalTransferSubmitReinforceParams {

    /**
     * questionId  : 123456
     * questionText  : 您的姓名
     * questionAnswer  : 张三
     * status : 01
     * token : j1dcfurr
     */

    private String questionId;
    private String questionText;
    private String questionAnswer;
    private String status;
    private String token;

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

    public String getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
