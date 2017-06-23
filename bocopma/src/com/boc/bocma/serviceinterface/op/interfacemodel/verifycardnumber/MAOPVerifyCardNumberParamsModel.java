package com.boc.bocma.serviceinterface.op.interfacemodel.verifycardnumber;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

import org.json.JSONException;
import org.json.JSONObject;

public class MAOPVerifyCardNumberParamsModel extends MAOPBaseParamsModel {
    private static final String INTERFACE_URL = "unlogin/checkcardno";
    
    private static final String CARD_NUMBER_KEY = "cardno";

    @Override
    public String getUrl() {
       return OPURL.getFAABaseTransUrl() + INTERFACE_URL;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    
    private String cardNumber;

    @Override
    public String getJsonBody() throws JSONException {
        JSONObject body = new JSONObject();
        body.put(CARD_NUMBER_KEY, cardNumber);
        return body.toString();
    }
}
