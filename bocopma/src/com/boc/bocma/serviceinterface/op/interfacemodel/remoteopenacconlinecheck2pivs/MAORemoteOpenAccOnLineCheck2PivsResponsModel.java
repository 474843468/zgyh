package com.boc.bocma.serviceinterface.op.interfacemodel.remoteopenacconlinecheck2pivs;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

/**
 * 1.2.16 【SA9206】远程开户单笔联网核查接口SV2007
 * 
 * @author gwluo
 * 
 */
public class MAORemoteOpenAccOnLineCheck2PivsResponsModel extends
		MAOPBaseResponseModel {
	private final String SUCCESS = "success";
	/**
	 * 更新成功标识 X(1) 必填 1. 更新成功 2. 更新失败
	 */
	public String success;

	public MAORemoteOpenAccOnLineCheck2PivsResponsModel(JSONObject jsonResponse) {
		success = jsonResponse.optString(SUCCESS);
	}

	public static final Creator<MAORemoteOpenAccOnLineCheck2PivsResponsModel> CREATOR = new Creator<MAORemoteOpenAccOnLineCheck2PivsResponsModel>() {
		@Override
		public MAORemoteOpenAccOnLineCheck2PivsResponsModel createFromJson(
				JSONObject jsonResponse) throws JSONException {
			return new MAORemoteOpenAccOnLineCheck2PivsResponsModel(jsonResponse);
		}

	};
}
