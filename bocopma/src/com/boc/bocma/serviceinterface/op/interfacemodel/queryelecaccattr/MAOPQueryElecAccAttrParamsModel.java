package com.boc.bocma.serviceinterface.op.interfacemodel.queryelecaccattr;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

public class MAOPQueryElecAccAttrParamsModel extends MAOPBaseParamsModel{

	private static final String INTERFACE_URL = "queryelecaccattr";
	
	private static final String distType_const = "distType";
	private static final String distCode_const = "distCode";
	

	private String distType;
	private String distCode;
	
	public String getDistType() {
		return distType;
	}

	public void setDistType(String distType) {
		this.distType = distType;
	}

	public String getDistCode() {
		return distCode;
	}

	public void setDistCode(String distCode) {
		this.distCode = distCode;
	}

	@Override
	public String getUrl() {
		return OPURL.getFAABaseTransUrl() + INTERFACE_URL;
	}

	@Override
	public String getJsonBody() throws JSONException {
		 JSONObject body = new JSONObject();
	        body.put(distType_const, distType);
	        body.put(distCode_const, distCode);
	        return body.toString();
	}

}
