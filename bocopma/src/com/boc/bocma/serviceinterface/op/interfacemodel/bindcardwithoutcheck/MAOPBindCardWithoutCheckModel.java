package com.boc.bocma.serviceinterface.op.interfacemodel.bindcardwithoutcheck;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

import org.json.JSONException;
import org.json.JSONObject;

public class MAOPBindCardWithoutCheckModel extends MAOPBaseResponseModel {
    private static final String RTNMSG_KEY = "rtnmsg";
    
    private static final int RESULT_OK = 0;
    
    private int rtnmsg = -1;
    public MAOPBindCardWithoutCheckModel(JSONObject jsonResponse) throws JSONException {
        rtnmsg = jsonResponse.optInt(RTNMSG_KEY);
    }

    public MAOPBindCardWithoutCheckModel() {
    }

    public boolean isSuccess() {
        return rtnmsg == RESULT_OK;
    }
    
    public static final Creator<MAOPBindCardWithoutCheckModel> CREATOR = new Creator<MAOPBindCardWithoutCheckModel>() {
        @Override
        public MAOPBindCardWithoutCheckModel createFromJson(JSONObject jsonResponse) throws JSONException {
            return new MAOPBindCardWithoutCheckModel(jsonResponse);
        }
        
    };
}
