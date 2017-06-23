package com.boc.bocma.serviceinterface.op.interfacemodel.keywordupload;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 关键字上送结果类
 */
public class MAOPKeywordUploadModel extends MAOPBaseResponseModel {
    private static final String RESULT_KEY = "result";
    private static final int RESULT_SUCCESS = 0;
    
    public MAOPKeywordUploadModel(JSONObject jsonResponse) {
        result = jsonResponse.optInt(RESULT_KEY);
    }

    private int result;
    
    /**
     * @return 成功返回true，否则返回false
     */
    public boolean isSuccess() {
        return result == RESULT_SUCCESS;
    }
    
    public static final Creator<MAOPKeywordUploadModel> CREATOR = new Creator<MAOPKeywordUploadModel>() {
        @Override
        public MAOPKeywordUploadModel createFromJson(JSONObject jsonResponse) throws JSONException {
            return new MAOPKeywordUploadModel(jsonResponse);
        }
    };
}
