package com.boc.bocma.serviceinterface.op.interfacemodel.checkbindcardmsg;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 增加卡短信验证结果类
 *
 */
public class MAOPCheckBindCardMsgModel extends MAOPBaseResponseModel {
    private static final String RESULT_KEY = "result";
    
    private String result;
    public MAOPCheckBindCardMsgModel(JSONObject jsonResponse) {
        result = jsonResponse.optString(RESULT_KEY);
    }

    public String getResult() {
        return result;
    }
    
    public static final Creator<MAOPCheckBindCardMsgModel> CREATOR = new Creator<MAOPCheckBindCardMsgModel>() {
        @Override
        public MAOPCheckBindCardMsgModel createFromJson(JSONObject jsonResponse) throws JSONException {
            return new MAOPCheckBindCardMsgModel(jsonResponse);
        }
        
    };
}
