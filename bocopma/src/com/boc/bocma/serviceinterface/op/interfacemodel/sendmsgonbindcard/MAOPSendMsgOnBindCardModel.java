package com.boc.bocma.serviceinterface.op.interfacemodel.sendmsgonbindcard;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 增加卡及短信发送结果类
 *
 */
public class MAOPSendMsgOnBindCardModel extends MAOPBaseResponseModel {
    private static final String RESULT_KEY = "result";
    
    private String result;
    public MAOPSendMsgOnBindCardModel(JSONObject jsonResponse) {
        result = jsonResponse.optString(RESULT_KEY);
    }

    public String getResult() {
        return result;
    }
    
    public static final Creator<MAOPSendMsgOnBindCardModel> CREATOR = new Creator<MAOPSendMsgOnBindCardModel>() {
        @Override
        public MAOPSendMsgOnBindCardModel createFromJson(JSONObject jsonResponse) throws JSONException {
            return new MAOPSendMsgOnBindCardModel(jsonResponse);
        }
        
    };
}
