package com.boc.bocma.serviceinterface.op.interfacemodel.queryothopenbankbybankid;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

/**
 * 1.2.15 【SA9205】非农补卡BIN信息查询开户行接口
 * 
 * @author gwluo
 * 
 */
public class MAOQueryOthOpenBankByBankIdParamsModel extends MAOPBaseParamsModel {
	private final String INTERFACE_URL = "queryOthOpenBankByBankId";
	private final static String OTHCARDTOPCNAPS = "othCardTopCnaps";
	/**
	 * 总行行号 String (3) Y 纯数字，为接口13根据卡号查询卡bin信息返回的bankId
	 */
	public String othCardTopCnaps;

	@Override
	public String getUrl() {
		return OPURL.getFAABaseTransUrl() + INTERFACE_URL;
	}

	@Override
	public String getJsonBody() throws JSONException {
		JSONObject body = new JSONObject();
		body.put(OTHCARDTOPCNAPS, othCardTopCnaps);
		return body.toString();
	}

}
