package com.boc.bocma.serviceinterface.op.interfacemodel.keywordupload;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 关键字上送参数类
 */
public class MAOPKeywordUploadParamsModel extends MAOPBaseParamsModel {
    private static final String INTERFACE_URL = "faa/keywords/save";
    
    private static final String USER_ID_KEY = "userid";
    private static final String TRANS_DESCRIPTION_KEY = "transdescription";
    
    private String userId;
    
    private String transDescription;
    
    /**
     * @param userId 用户ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @param transDescription 关键字描述
     */
    public void setTransDescription(String transDescription) {
        this.transDescription = transDescription;
    }

    @Override
    public String getUrl() {
        return OPURL.getFAABaseTransUrl() + INTERFACE_URL;
    }

    @Override
    public String getJsonBody() throws JSONException {
        JSONObject body = new JSONObject();
        body.put(USER_ID_KEY, userId);
        body.put(TRANS_DESCRIPTION_KEY, transDescription);
        return body.toString();
    }

}
