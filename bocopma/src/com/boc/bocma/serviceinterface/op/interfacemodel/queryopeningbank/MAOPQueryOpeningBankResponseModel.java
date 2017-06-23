package com.boc.bocma.serviceinterface.op.interfacemodel.queryopeningbank;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

public class MAOPQueryOpeningBankResponseModel extends MAOPBaseResponseModel {

	private final static String bankInfo_const = "bankInfo";
	private final static String othCardBankName_const = "othCardBankName";
	private final static String othCardCnaps_const = "othCardCnaps";
	private final static String othCardTopCnaps_const = "othCardTopCnaps";

	private List<OtherBankInfo> othBankInfoList = new ArrayList<OtherBankInfo>();
	
	 public MAOPQueryOpeningBankResponseModel(JSONObject jsonResponse) throws JSONException {
		 JSONArray bankInfoList = jsonResponse.optJSONArray(bankInfo_const);
	        for (int i = 0; bankInfoList !=null && i < bankInfoList.length(); i++) {
	            JSONObject  bankInfo = bankInfoList.getJSONObject(i);
	            OtherBankInfo item = new OtherBankInfo();
	            item.setOthCardBankName(bankInfo.optString(othCardBankName_const));
	            item.setOthCardCnaps(bankInfo.optString(othCardCnaps_const));
	            item.setOthCardTopCnaps(bankInfo.optString(othCardTopCnaps_const));
	            othBankInfoList.add(item);
	        }
	    }
	 
	 public List<OtherBankInfo> getOtherBankInfoList(){
		 return othBankInfoList;
	 }
	 
	public static final Creator<MAOPQueryOpeningBankResponseModel> CREATOR = new Creator<MAOPQueryOpeningBankResponseModel>() {
        @Override
        public MAOPQueryOpeningBankResponseModel createFromJson(JSONObject jsonResponse) throws JSONException {
            return new MAOPQueryOpeningBankResponseModel(jsonResponse);
        }
        
    };
    
    public static class OtherBankInfo{
    	// 银行机构名称
    	private String othCardBankName;
    	// 开户行人行行号
		private String othCardCnaps;
		// 总行人行号
		private String othCardTopCnaps;
		
		public OtherBankInfo(){
			
		}

		public String getOthCardBankName() {
			return othCardBankName;
		}

		public void setOthCardBankName(String othCardBankName) {
			this.othCardBankName = othCardBankName;
		}

		public String getOthCardCnaps() {
			return othCardCnaps;
		}

		public void setOthCardCnaps(String othCardCnaps) {
			this.othCardCnaps = othCardCnaps;
		}

		public String getOthCardTopCnaps() {
			return othCardTopCnaps;
		}

		public void setOthCardTopCnaps(String othCardTopCnaps) {
			this.othCardTopCnaps = othCardTopCnaps;
		}
    }
}
