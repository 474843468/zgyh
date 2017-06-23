package com.boc.bocma.serviceinterface.op.interfacemodel.querydebitcard;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

import org.json.JSONException;
import org.json.JSONObject;

public class MAOPQueryDebitCardParamsModel extends MAOPBaseParamsModel {
    private static final String INTERFACE_URL = "faa/queryDebitCardByUId";
    
    private static final String USER_ID_KEY = "userid";
    
    @Override
    public String getUrl() {
        return OPURL.getFAABaseTransUrl() + INTERFACE_URL;
    }

    @Override
    public String getJsonBody() throws JSONException {
        JSONObject body = new JSONObject();
        body.put(USER_ID_KEY, userId);
        return body.toString();
    }

    private String userId;

    /**
     * @param userId 用户ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
