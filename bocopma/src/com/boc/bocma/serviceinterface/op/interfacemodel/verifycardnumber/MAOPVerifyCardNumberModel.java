package com.boc.bocma.serviceinterface.op.interfacemodel.verifycardnumber;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

import org.json.JSONException;
import org.json.JSONObject;

public class MAOPVerifyCardNumberModel extends MAOPBaseResponseModel {
    private static final String RESULT_KEY = "result";
    
    private static final int RESULT_VALID = 0;
    
    private int resultCode = -1;
    
    public MAOPVerifyCardNumberModel(JSONObject jsonResponse) {
        resultCode = jsonResponse.optInt(RESULT_KEY);
    }

    /**
     * @return true if card number is valid
     */
    public boolean isCardNumberValid() {
        return resultCode == RESULT_VALID;
    }
    
    public static final Creator<MAOPVerifyCardNumberModel> CREATOR = new Creator<MAOPVerifyCardNumberModel>() {
        @Override
        public MAOPVerifyCardNumberModel createFromJson(JSONObject jsonResponse) throws JSONException {
            return new MAOPVerifyCardNumberModel(jsonResponse);
        }
    };
}
