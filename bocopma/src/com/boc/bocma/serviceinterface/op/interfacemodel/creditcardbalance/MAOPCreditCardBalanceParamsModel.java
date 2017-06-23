package com.boc.bocma.serviceinterface.op.interfacemodel.creditcardbalance;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 信用卡余额查询参数类
 */
public class MAOPCreditCardBalanceParamsModel extends MAOPBaseParamsModel {
    private static final String URL = "creditbalsearch";
    private static final String CARD_IDENTIFIER_KEY = "lmtamt";
    
    private String identifier;
    
    @Override
    public String getUrl() {
        return OPURL.getFAABaseTransUrl() + URL;
    }

    public void setCard(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getJsonBody() throws JSONException {
        JSONObject body = new JSONObject();
        body.put(CARD_IDENTIFIER_KEY, identifier);
        return body.toString();
    }

}
