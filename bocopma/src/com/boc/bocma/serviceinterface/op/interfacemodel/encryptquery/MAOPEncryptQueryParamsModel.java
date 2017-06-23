package com.boc.bocma.serviceinterface.op.interfacemodel.encryptquery;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 根据用户ID生成128位长度的字符串接口的参数类
 */
public class MAOPEncryptQueryParamsModel extends MAOPBaseParamsModel {
    private static final String INTERFACE_URL = "faa/encryptuserid";
    
    private static final String USER_ID_KEY = "userid";
    
    private String userId;
    
    public void setUserId(String userId) {
        this.userId = userId;
    }

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

}
