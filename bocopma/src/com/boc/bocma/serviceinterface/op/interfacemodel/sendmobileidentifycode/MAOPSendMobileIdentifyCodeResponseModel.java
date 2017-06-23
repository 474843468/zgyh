package com.boc.bocma.serviceinterface.op.interfacemodel.sendmobileidentifycode;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

public class MAOPSendMobileIdentifyCodeResponseModel extends
		MAOPBaseResponseModel {
	
	public MAOPSendMobileIdentifyCodeResponseModel(JSONObject jsonResponse) {

	}

	public static final Creator<MAOPSendMobileIdentifyCodeResponseModel> CREATOR = new Creator<MAOPSendMobileIdentifyCodeResponseModel>() {
		@Override
		public MAOPSendMobileIdentifyCodeResponseModel createFromJson(
				JSONObject jsonResponse) throws JSONException {
			return new MAOPSendMobileIdentifyCodeResponseModel(jsonResponse);
		}

	};

}
