package com.boc.bocma.serviceinterface.op.interfacemodel.queryboundcardlist;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 根据userid查询用户借记卡列表参数Model
 *
 */
public class MAOPQueryBoundCardListParamsModel extends MAOPBaseParamsModel {
    private static final String INTERFACE_URL = "faa/queryCardByUId";
    
    public static final String USER_ID_KEY = "userid";
    
    private String userId;
    
    @Override
    public String getUrl() {
        return OPURL.getFAABaseTransUrl() + INTERFACE_URL;
    }

    @Override
    public String getJsonBody() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(USER_ID_KEY, userId);
        return object.toString();
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
