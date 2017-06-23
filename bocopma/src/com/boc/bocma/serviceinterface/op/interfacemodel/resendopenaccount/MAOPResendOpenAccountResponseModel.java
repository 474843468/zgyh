package com.boc.bocma.serviceinterface.op.interfacemodel.resendopenaccount;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

/**
 * 1.2.12 【SA01458】重新发送开户请求接口 返回model
 * 
 * @author lgw
 * 
 */
public class MAOPResendOpenAccountResponseModel extends MAOPBaseResponseModel {
	private final static String SERVICERESPONSE = "serviceResponse";
	private final static String RESPONSECODE = "responseCode";
	private final static String RESPONSEMSG = "responseMsg";
	/**
	 * 返回码 X(7) 0000000:交易成功 9999999:系统异常 1000001:必输项不能为空 1000002:数据校验失败
	 * 1000003:查询结果为空 Y
	 */
	private String responseCode;

	private String responseMsg;// 返回消息 X(100) 必输

	public MAOPResendOpenAccountResponseModel(JSONObject jsonResponse) {
		JSONObject jsonObject = jsonResponse.optJSONObject(SERVICERESPONSE);
		responseCode = jsonObject.optString(RESPONSECODE);
		responseMsg = jsonObject.optString(RESPONSEMSG);
	}

	public String getResponseCode() {
		return responseCode;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public static final Creator<MAOPResendOpenAccountResponseModel> CREATOR = new Creator<MAOPResendOpenAccountResponseModel>() {
		@Override
		public MAOPResendOpenAccountResponseModel createFromJson(
				JSONObject jsonResponse) throws JSONException {
			return new MAOPResendOpenAccountResponseModel(jsonResponse);
		}

	};

}
