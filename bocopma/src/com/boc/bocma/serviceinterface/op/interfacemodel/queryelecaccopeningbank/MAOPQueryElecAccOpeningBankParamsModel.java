package com.boc.bocma.serviceinterface.op.interfacemodel.queryelecaccopeningbank;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

public class MAOPQueryElecAccOpeningBankParamsModel extends MAOPBaseParamsModel {

	private static final String INTERFACE_URL = "queryelecaccopeningbank";
	
	private static final String provinceCode_const = "provinceCode";
	private static final String cityCode_const = "cityCode";

	
	private String provinceCode;
	private String cityCode;
	


	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	@Override
	public String getUrl() {
		return OPURL.getFAABaseTransUrl() + INTERFACE_URL;
	}

	@Override
	public String getJsonBody() throws JSONException {
		JSONObject body = new JSONObject();
        body.put(provinceCode_const, provinceCode);
        body.put(cityCode_const, cityCode);
        return body.toString();
	}

}
