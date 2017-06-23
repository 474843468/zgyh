package com.boc.bocma.serviceinterface.op.interfacemodel.uploadapptoken;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;
/**
 * FA0019上传通过容器登陆获取到的token  上送报文体内容
 * @author WHJ
 *
 */
public class MAOPUploadAppTokenParamsModel extends MAOPBaseParamsModel{
	private static final String URL = "appToken";
	
	/** user_id */
	private static final String USER_ID_KEY = "user_id";
	/** Client_Key */
	private static final String CLIENT_ID_KEY = "client_id";
	/** 访问令牌 */
	private static final String ACCESS_TOKEN_KEY = "access_token";
	/** 刷新令牌 */
	private static final String REFRESH_TOKEN_KEY = "refresh_token";
	
	/** user_id */
    private String user_id;
    /** Client_Key */
    private String client_id;
    /** 访问令牌 */
    private String access_token;
    /** 刷新令牌 */
    private String refresh_token;
    
	
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	@Override
	public String getUrl() {
		return OPURL.getFAABaseTransUrl() + URL;
	}

	@Override
	public String getJsonBody() throws JSONException {
		JSONObject body = new JSONObject();
        body.put(USER_ID_KEY, user_id);
        body.put(CLIENT_ID_KEY, client_id);
        body.put(ACCESS_TOKEN_KEY, access_token);
        body.put(REFRESH_TOKEN_KEY, refresh_token);
        return body.toString();
	}
}
