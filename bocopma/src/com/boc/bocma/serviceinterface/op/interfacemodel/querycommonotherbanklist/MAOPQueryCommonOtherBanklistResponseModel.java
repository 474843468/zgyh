package com.boc.bocma.serviceinterface.op.interfacemodel.querycommonotherbanklist;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

/**
 * 根据userid查询用户借记卡列表结果Model
 */
public class MAOPQueryCommonOtherBanklistResponseModel extends
		MAOPBaseResponseModel {
	private final static String bankInfo_const = "bankInfo";
	private final static String othCardBankName_const = "othCardBankName";
	private final static String othCardTopCnaps_const = "othCardTopCnaps";

	private List<OtherBankInfo> othBankInfoList = new ArrayList<OtherBankInfo>();

	public MAOPQueryCommonOtherBanklistResponseModel(JSONObject jsonResponse)
			throws JSONException {
		JSONArray bankInfoList = jsonResponse.optJSONArray(bankInfo_const);
		for (int i = 0; bankInfoList != null && i < bankInfoList.length(); i++) {
			JSONObject bankInfo = bankInfoList.getJSONObject(i);
			OtherBankInfo item = new OtherBankInfo();
			item.setOthCardBankName(bankInfo.optString(othCardBankName_const));
			item.setOthCardTopCnaps(bankInfo.optString(othCardTopCnaps_const));
			othBankInfoList.add(item);
		}
	}

	public List<OtherBankInfo> getOtherBankInfoList() {
		return othBankInfoList;
	}

	public static final Creator<MAOPQueryCommonOtherBanklistResponseModel> CREATOR = new Creator<MAOPQueryCommonOtherBanklistResponseModel>() {
		@Override
		public MAOPQueryCommonOtherBanklistResponseModel createFromJson(
				JSONObject jsonResponse) throws JSONException {
			return new MAOPQueryCommonOtherBanklistResponseModel(jsonResponse);
		}

	};

	public static class OtherBankInfo {
		private String othCardBankName;
		private String othCardTopCnaps;

		public OtherBankInfo() {

		}

		public String getOthCardBankName() {
			return othCardBankName;
		}

		public void setOthCardBankName(String othCardBankName) {
			this.othCardBankName = othCardBankName;
		}

		public String getOthCardTopCnaps() {
			return othCardTopCnaps;
		}

		public void setOthCardTopCnaps(String othCardTopCnaps) {
			this.othCardTopCnaps = othCardTopCnaps;
		}

	}
}
