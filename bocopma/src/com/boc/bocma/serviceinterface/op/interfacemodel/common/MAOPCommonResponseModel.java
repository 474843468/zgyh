package com.boc.bocma.serviceinterface.op.interfacemodel.common;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

import org.json.JSONException;
import org.json.JSONObject;

public class MAOPCommonResponseModel extends MAOPBaseResponseModel {
    private static final String RESULT_KEY = "result";
    
    private String result;
    public MAOPCommonResponseModel(JSONObject jsonResponse) {
        result = jsonResponse.optString(RESULT_KEY);
    }

    public String getResult() {
        return result;
    }
    
    public static final Creator<MAOPCommonResponseModel> CREATOR = new Creator<MAOPCommonResponseModel>() {
        @Override
        public MAOPCommonResponseModel createFromJson(JSONObject jsonResponse) throws JSONException {
            return new MAOPCommonResponseModel(jsonResponse);
        }
        
    };
}
