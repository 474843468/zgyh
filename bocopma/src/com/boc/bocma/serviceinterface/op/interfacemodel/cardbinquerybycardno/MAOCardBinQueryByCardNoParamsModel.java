package com.boc.bocma.serviceinterface.op.interfacemodel.cardbinquerybycardno;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

/**
 * 1.2.14 【SA9180】查询非农补卡BIN信息接口
 * 
 * @author gwluo
 * 
 */
public class MAOCardBinQueryByCardNoParamsModel extends MAOPBaseParamsModel {
	private final String INTERFACE_URL = "cardBinQueryByCardNo";
	private final static String CARDNO = "cardNo";
	/**
	 * 他行卡卡号 String (32) Y 纯数字
	 */
	public String cardNo;

	@Override
	public String getUrl() {
		return OPURL.getFAABaseTransUrl() + INTERFACE_URL;
	}

	@Override
	public String getJsonBody() throws JSONException {
		JSONObject body = new JSONObject();
		body.put(CARDNO, cardNo);
		return body.toString();
	}
}
