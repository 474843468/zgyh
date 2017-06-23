package com.boc.bocma.serviceinterface.op.interfacemodel.uploadapptoken;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

/**
 * FA0019上传通过容器登陆获取到的token  返回报文内容
 * @author WHJ
 *
 */
public class MAOPUploadAppTokenModel extends MAOPBaseResponseModel{
	/**
     * 错误码  ASR-000000(操作成功)
     */
    private static final String MSGCDE_KEY = "msgcde";
    /**
     * 错误信息
     */
    private static final String RTNMSG_KEY = "rtnmsg";
    
    private String msgcde;
    private String rtnmsg;
    
    public MAOPUploadAppTokenModel(JSONObject jsonResponse) {
    	msgcde = jsonResponse.optString(MSGCDE_KEY);
    	rtnmsg = jsonResponse.optString(RTNMSG_KEY);
    }
    
    public MAOPUploadAppTokenModel() {
    }
    
    public static final Creator<MAOPUploadAppTokenModel> CREATOR = new Creator<MAOPUploadAppTokenModel>() {
        @Override
        public MAOPUploadAppTokenModel createFromJson(JSONObject jsonResponse) throws JSONException {
            return new MAOPUploadAppTokenModel(jsonResponse);
        }
        
    };

	public String getMsgcde() {
		return msgcde;
	}

	public String getRtnmsg() {
		return rtnmsg;
	}
    
    
}
