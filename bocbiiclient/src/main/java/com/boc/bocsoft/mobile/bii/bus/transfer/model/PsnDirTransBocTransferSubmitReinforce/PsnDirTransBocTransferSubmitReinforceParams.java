package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocTransferSubmitReinforce;

/**
 * Created by WM on 2016/6/12.
 */
public class PsnDirTransBocTransferSubmitReinforceParams {

    /**
     * method : PsnDirTransBocTransferSubmitReinforce
     * id : 13100214862186
     * params : {"questionId ":"123456","questionText ":"您的姓名","questionAnswer ":"张三","status":"01","token":"j1dcfurr"}
     * conversationId : 991ea30e-298d-43fe-b65b-698912e3156b
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