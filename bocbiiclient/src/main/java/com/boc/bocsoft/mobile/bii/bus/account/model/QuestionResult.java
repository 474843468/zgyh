package com.boc.bocsoft.mobile.bii.bus.account.model;

/**
 * @author wangyang
 *         16/7/26 10:49
 *         挑战问题
 */
public class QuestionResult extends FactorAndCaResult{

    /** 加强认证状态-- QUESTION（交易需要QA挑战）、SMS（交易需要短信挑战） */
    private String transMonStatus;
    /** 挑战问题编码-- transMonStatus为QUESTION时返回该字段，且接口不会返回原有交易返回数据 */
    private String querstionId;
    /** 挑战问题内容-- transMonStatus为QUESTION时返回该字段，且接口不会返回原有交易返回数据 */
    private String questionText;
    /** 被拒绝原因-- 当transMonStatus为DENY返回该字段无可用加强认证方式：1 */
    private String CredentialNullFlag;

    public String getTransMonStatus() {
        return transMonStatus;
    }

    public void setTransMonStatus(String transMonStatus) {
        this.transMonStatus = transMonStatus;
    }

    public String getQuerstionId() {
        return querstionId;
    }

    public void setQuerstionId(String querstionId) {
        this.querstionId = querstionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getCredentialNullFlag() {
        return CredentialNullFlag;
    }

    public void setCredentialNullFlag(String credentialNullFlag) {
        CredentialNullFlag = credentialNullFlag;
    }
}
