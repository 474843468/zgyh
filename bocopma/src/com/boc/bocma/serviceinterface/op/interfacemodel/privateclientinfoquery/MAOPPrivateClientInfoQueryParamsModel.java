package com.boc.bocma.serviceinterface.op.interfacemodel.privateclientinfoquery;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

import org.json.JSONException;
import org.json.JSONObject;

public class MAOPPrivateClientInfoQueryParamsModel extends MAOPBaseParamsModel {
    private static final String URL = "unlogin/querycardindcusinfo";

    private static final String CARD_NUMBER_KEY = "cardno";
    private static final String CARD_IDENTIFIER_KEY = "accrem";
    private static final String USER_ID_KEY = "userid";
    private static final String PRIORITY_KEY = "priority";
    private static final String SYS_CHANNEL_KEY = "syschannel";
    
    /**
     * 设置卡号，当卡唯一标识未知时上送
     */
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
    
    /**
     * 设置卡唯一标识，当卡号未知时上送
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 设置应用标识，01-注册用户，02- 新增卡
     * @param priority
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * 设置系统标识，01-容器，02-APS
     */
    public void setSysChannel(String sysChannel) {
        this.sysChannel = sysChannel;
    }

    private String cardNo;
    private String identifier;
    private String userId;
    private String priority;
    private String sysChannel;
    
    @Override
    public String getUrl() {
        return OPURL.getFAABaseTransUrl() + URL;
    }
    
    private void putIfNotNull(JSONObject body, String key, String value) throws JSONException{
        if (value != null) {
            body.put(key, value);
        }
    }
    
    @Override
    public String getJsonBody() throws JSONException {
        JSONObject body = new JSONObject();
        putIfNotNull(body, CARD_NUMBER_KEY, cardNo);
        putIfNotNull(body, CARD_IDENTIFIER_KEY, identifier);
        body.put(USER_ID_KEY, userId);
        body.put(PRIORITY_KEY, priority);
        body.put(SYS_CHANNEL_KEY, sysChannel);
        return body.toString();
    }

}
