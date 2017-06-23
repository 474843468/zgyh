package com.boc.bocma.serviceinterface.op.interfacemodel.encryptquery;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 根据用户ID生成128位长度的字符串接口的结果类
 */
public class MAOPEncryptQueryModel extends MAOPBaseResponseModel {
    private static final String ENCRYPT_KEY = "encrypt";
    
    public MAOPEncryptQueryModel(JSONObject jsonResponse) {
        encrypt = jsonResponse.optString(ENCRYPT_KEY);
    }

    public static final Creator<MAOPEncryptQueryModel> CREATOR = new Creator<MAOPEncryptQueryModel>() {
        @Override
        public MAOPEncryptQueryModel createFromJson(JSONObject jsonResponse) throws JSONException {
            return new MAOPEncryptQueryModel(jsonResponse);
        }
    };
    
    /**
     * @return the encrypt 128位长度的字符串信息
     */
    public String getEncrypt() {
        return encrypt;
    }
    
    private String encrypt;

}
