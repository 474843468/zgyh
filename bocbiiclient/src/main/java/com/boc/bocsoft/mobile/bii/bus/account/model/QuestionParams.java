package com.boc.bocsoft.mobile.bii.bus.account.model;

/**
 * @author wangyang
 *         16/7/26 14:36
 *         挑战问题参数
 */
public class QuestionParams extends PublicParams{

    /** 挑战问题编码-- 为QA挑战时上送该字段 */
    private String querstionId;
    /** 挑战问题内容-- 为QA挑战时上送该字段 */
    private String questionText;
    /** 挑战问题答案-- 为QA挑战时上送该字段*/
    private String questionAnswer;

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

    public String getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer;
    }
}
