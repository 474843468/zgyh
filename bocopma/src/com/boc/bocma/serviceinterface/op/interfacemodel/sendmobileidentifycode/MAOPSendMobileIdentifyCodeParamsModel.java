package com.boc.bocma.serviceinterface.op.interfacemodel.sendmobileidentifycode;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

public class MAOPSendMobileIdentifyCodeParamsModel extends MAOPBaseParamsModel {

	
	private static final String INTERFACE_URL = "sendmobileidentifycode";
	
	private static final String mobile_const = "mobile";
	private static final String transType_const = "transType";

	private String mobile;
	private String transType;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	@Override
	public String getUrl() {
		return OPURL.getFAABaseTransUrl() + INTERFACE_URL;
	}

	@Override
	public String getJsonBody() throws JSONException {
		JSONObject body = new JSONObject();
		body.put(mobile_const, mobile);
		body.put(transType_const, transType);
		return body.toString();
	}

}
