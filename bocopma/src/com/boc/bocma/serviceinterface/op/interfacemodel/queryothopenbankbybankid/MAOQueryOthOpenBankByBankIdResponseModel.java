package com.boc.bocma.serviceinterface.op.interfacemodel.queryothopenbankbybankid;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

/**
 * 1.2.15 【SA9205】非农补卡BIN信息查询开户行接口
 * 
 * @author gwluo
 * 
 */
public class MAOQueryOthOpenBankByBankIdResponseModel extends
		MAOPBaseResponseModel {
	private final String OTHCARDBANKNAME = "othCardBankName";
	private final String OTHCARDTOPCNAPS = "othCardTopCnaps";
	private final String SERVICERESPONSE = "serviceResponse";
	private final String RESPONSECODE = "responseCode";
	private final String RESPONSEMSG = "responseMsg";
	/**
	 * 他行开户行名称 String (50) Y
	 */
	public String othCardBankName;
	/**
	 * 总行机构号 String (16) Y
	 */
	public String othCardTopCnaps; 
	/**
	 * 返回码 String(7) Y 详见附录
	 */
	public String responseCode;
	/**
	 * 返回消息 String(100) Y 返回具体的错误信息
	 */
	public String responseMsg;

	public MAOQueryOthOpenBankByBankIdResponseModel(JSONObject jsonResponse) {
		othCardBankName = jsonResponse.optString(OTHCARDBANKNAME);
		othCardTopCnaps = jsonResponse.optString(OTHCARDTOPCNAPS);
		JSONObject serviceResponse = jsonResponse
				.optJSONObject(SERVICERESPONSE);
		responseCode = serviceResponse.optString(RESPONSECODE);
		responseMsg = serviceResponse.optString(RESPONSEMSG);
	}

	public static final Creator<MAOQueryOthOpenBankByBankIdResponseModel> CREATOR = new Creator<MAOQueryOthOpenBankByBankIdResponseModel>() {
		@Override
		public MAOQueryOthOpenBankByBankIdResponseModel createFromJson(
				JSONObject jsonResponse) throws JSONException {
			return new MAOQueryOthOpenBankByBankIdResponseModel(jsonResponse);
		}
	};
}
