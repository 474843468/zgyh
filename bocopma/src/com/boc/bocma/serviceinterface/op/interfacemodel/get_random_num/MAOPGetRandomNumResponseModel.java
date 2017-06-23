package com.boc.bocma.serviceinterface.op.interfacemodel.get_random_num;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

public class MAOPGetRandomNumResponseModel extends MAOPBaseResponseModel {

	
	private static final String serverRandom_const = "serverRandom";
	private String serverRandom;
	
	public String getServerRandom(){
		return serverRandom;
	}
	public MAOPGetRandomNumResponseModel(JSONObject jsonResponse) {
		serverRandom = jsonResponse.optString(serverRandom_const);
	}

	public static final Creator<MAOPGetRandomNumResponseModel> CREATOR = new Creator<MAOPGetRandomNumResponseModel>() {
		@Override
		public MAOPGetRandomNumResponseModel createFromJson(
				JSONObject jsonResponse) throws JSONException {
			return new MAOPGetRandomNumResponseModel(jsonResponse);
		}

	};
}
