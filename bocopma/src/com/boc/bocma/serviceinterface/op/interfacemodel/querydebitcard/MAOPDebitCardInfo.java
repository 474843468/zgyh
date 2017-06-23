package com.boc.bocma.serviceinterface.op.interfacemodel.querydebitcard;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

public class MAOPDebitCardInfo extends MAOPBaseResponseModel {
    private static final String CARD_NO_KEY = "cardno";
    private static final String ORG_ID_KEY = "orgid";
    private static final String ORGID_NAME_KEY = "orgidname";
    
    public MAOPDebitCardInfo(JSONObject jsonResponse) {
    	cardno = jsonResponse.optString(CARD_NO_KEY);
    	orgid = jsonResponse.optString(ORG_ID_KEY);
    	orgidname = jsonResponse.optString(ORGID_NAME_KEY);
    }
    
    public static final Creator<MAOPDebitCardInfo> CREATOR = new Creator<MAOPDebitCardInfo>() {
        @Override
        public MAOPDebitCardInfo createFromJson(JSONObject jsonResponse) throws JSONException {
            return new MAOPDebitCardInfo(jsonResponse);
        }
        
    };
    /**
     * @return 借记卡卡号
     */
    public String getCardno() {
		return cardno;
	}
    /**
     * @return 开卡机构
     */
    public String getOrgid() {
		return orgid;
	}
   
	/**
     * @return 开卡机构名称
     */
    public String getOrgidname() {
		return orgidname;
	}

    /**
     * 借记卡卡号
     */
    private String cardno;
    /**
     * 开卡机构
     */
    private String orgid;
    /**
     * 开卡机构名称
     */
    private String orgidname;

}

