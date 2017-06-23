package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdReportLossResult;

/**
 * 信用卡挂失及补卡结果响应
 *
 * Created by liuweidong on 2016/6/13.
 */
public class PsnCrcdReportLossResultResult {

    private String remitNo;
    private String transMonStatus;
    private String questionId;
    private String questionText;

    public String getRemitNo() {
        return remitNo;
    }

    public void setRemitNo(String remitNo) {
        this.remitNo = remitNo;
    }

    public String getTransMonStatus() {
        return transMonStatus;
    }

    public void setTransMonStatus(String transMonStatus) {
        this.transMonStatus = transMonStatus;
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
}
