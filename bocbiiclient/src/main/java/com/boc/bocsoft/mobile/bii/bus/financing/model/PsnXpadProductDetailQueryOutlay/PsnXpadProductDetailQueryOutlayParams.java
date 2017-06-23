package com.boc.bocsoft.mobile.bii.bus.financing.model.PsnXpadProductDetailQueryOutlay;

/**
 * 4.52 登录前查询理财产品详情
 */
public class PsnXpadProductDetailQueryOutlayParams {


    private String productCode;//产品代码 StringM
    private String productKind;//产品性质 String O 0:结构性理财产品 1:类基金理财产品 可不上送，不送时“产品性质”默认为“0:结构性理财”
    private String conversationId;
    private String ibknum;// 省行联行号 返回项需展示(剩余额度、工作时间、挂单时间)，此项必输

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public void setProductKind(String productKind) {
        this.productKind = productKind;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public void setIbknum(String ibknum) {
        this.ibknum = ibknum;
    }
}
