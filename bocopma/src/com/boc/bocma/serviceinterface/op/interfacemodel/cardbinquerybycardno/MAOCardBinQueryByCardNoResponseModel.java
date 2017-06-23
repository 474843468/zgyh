package com.boc.bocma.serviceinterface.op.interfacemodel.cardbinquerybycardno;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

/**
 * 1.2.14 【SA9180】查询非农补卡BIN信息接口
 * 
 * @author gwluo
 * 
 */
public class MAOCardBinQueryByCardNoResponseModel extends MAOPBaseResponseModel {
	private final String AGCARDFLAG = "agCardFlag";
	private final String BANKID = "bankId";
	private final String BINDTYPE = "bindType";
	private final String OPENBANK = "openBank";
	private final String ERRCODE = "errCode";
	private final String ERRREASON = "errReason";
	private final String SERVICERESPONSE = "serviceResponse";
	private final String RESPONSECODE = "responseCode";
	private final String RESPONSEMSG = "responseMsg";
	/**
	 * 是否农补卡标识 String (1) 1-农补卡
	 */
	public String agCardFlag;
	/**
	 * 银行行号 String (32) Y
	 */
	public String bankId;
	/**
	 * 绑定账号类型 String (2) Y
	 */
	public String bindType;
	/**
	 * 开户行 String(128) Y
	 */
	public String openBank;
	/**
	 * 错误码 String (9)
	 */
	public String errCode;
	/**
	 * 错误原因 String(100)
	 */
	public String errReason;
	/**
	 * 返回码 String(7) Y 详见附录
	 */
	public String responseCode;
	/**
	 * 返回消息 String(100) Y 返回具体的错误信息
	 */
	public String responseMsg;

	public MAOCardBinQueryByCardNoResponseModel(JSONObject jsonResponse) {
		agCardFlag = jsonResponse.optString(AGCARDFLAG);
		bankId = jsonResponse.optString(BANKID);
		bindType = jsonResponse.optString(BINDTYPE);
		openBank = jsonResponse.optString(OPENBANK);
		errCode = jsonResponse.optString(ERRCODE);
		errReason = jsonResponse.optString(ERRREASON);
		JSONObject serviceResponse = jsonResponse
				.optJSONObject(SERVICERESPONSE);
		responseCode = serviceResponse.optString(RESPONSECODE);
		responseMsg = serviceResponse.optString(RESPONSEMSG);
	}

	public static final Creator<MAOCardBinQueryByCardNoResponseModel> CREATOR = new Creator<MAOCardBinQueryByCardNoResponseModel>() {
		@Override
		public MAOCardBinQueryByCardNoResponseModel createFromJson(
				JSONObject jsonResponse) throws JSONException {
			return new MAOCardBinQueryByCardNoResponseModel(jsonResponse);
		}

	};
}
