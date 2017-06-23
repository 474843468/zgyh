package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundDetailQueryOutlay;

/**
 * 登录前基金详情查询上送
 * Created by lxw on 2016/8/16 0016.
 */
public class PsnFundDetailQueryOutlayParams {

    private String fundCode;

    private String conversationId;
    public String getFundCode() {
        return fundCode;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }
}
