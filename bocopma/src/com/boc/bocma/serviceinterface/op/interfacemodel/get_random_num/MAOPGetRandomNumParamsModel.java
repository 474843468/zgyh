package com.boc.bocma.serviceinterface.op.interfacemodel.get_random_num;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

public class MAOPGetRandomNumParamsModel extends MAOPBaseParamsModel {

	private static final String INTERFACE_URL = "get_random_num";
	
	private static final String systemFlag_const = "systemFlag";
	
	private String systemFlag;
	
	public String getSystemFlag() {
		return systemFlag;
	}

	public void setSystemFlag(String systemFlag) {
		this.systemFlag = systemFlag;
	}

	@Override
	public String getUrl() {
		return OPURL.remoteOpenAccountUrl() + INTERFACE_URL;
	}

	@Override
	public String getJsonBody() throws JSONException {
		JSONObject body = new JSONObject();
		body.put(systemFlag_const, systemFlag);
		return body.toString();
	}

}
