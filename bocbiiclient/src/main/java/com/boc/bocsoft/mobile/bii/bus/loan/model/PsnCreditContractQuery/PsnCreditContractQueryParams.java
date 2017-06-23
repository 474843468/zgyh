package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnCreditContractQuery;

/**
 * 征信授权协议接口请求参数
 * Created by xintong on 2016/6/13.
 */
public class PsnCreditContractQueryParams {

    //创建交易会话后的id,必须与额度申请交易共用一个conversation！
    private String conversationId;
    //协议ID，如果id为空则返回最新的协议(可选参数)
    private String contractNo;
    //语言类型,CHN-中文ENG-英文
    private String eLanguage;

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String geteLanguage() {
        return eLanguage;
    }

    public void seteLanguage(String eLanguage) {
        this.eLanguage = eLanguage;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
