package com.boc.bocma.serviceinterface.op.interfacemodel.resendopenaccount;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

/**
 * 1.2.12 【SA01458】重新发送开户请求接口 请求model
 * 
 * @author lgw
 */
public class MAOPResendOpenAccountParamsModel extends MAOPBaseParamsModel {
	private static final String INTERFACE_URL = "resendopenaccount";
	private final static String CERTNO = "certNo";// 身份证号 X(18)
													// 18位最后一位可为X，其余为纯数字 Y
	private final static String REUUID1 = "reUuid1";
	private final static String REUUID2 = "reUuid2";
	/**
	 * 身份证号 X(18) 18位最后一位可为X，其余为纯数字 Y
	 */
	// private String certNo;

	// public String getCertNo() {
	// return certNo;
	// }
	//
	// public void setCertNo(String certNo) {
	// this.certNo = certNo;
	// }
	/**
	 * 待重发二（二、三同时）类账户开户申请uuid String(32) N reUuid1和reUuid2不能同时为空
	 */
	public String reUuid1;
	/**
	 * 待重发三类账户开户申请uuid String(32) N
	 */
	public String reUuid2;


	public void setReUuid1(String reUuid1) {
		this.reUuid1 = reUuid1;
	}

	public void setReUuid2(String reUuid2) {
		this.reUuid2 = reUuid2;
	}

	@Override
	public String getUrl() {
		return OPURL.getFAABaseTransUrl() + INTERFACE_URL;
	}

	@Override
	public String getJsonBody() throws JSONException {
		JSONObject body = new JSONObject();
		// body.put(CERTNO, certNo);
		body.put(REUUID1, reUuid1);
		body.put(REUUID2, reUuid2);
		return body.toString();
	}
}
