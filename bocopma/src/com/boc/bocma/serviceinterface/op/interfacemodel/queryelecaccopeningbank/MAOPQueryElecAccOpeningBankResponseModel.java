package com.boc.bocma.serviceinterface.op.interfacemodel.queryelecaccopeningbank;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

public class MAOPQueryElecAccOpeningBankResponseModel extends
		MAOPBaseResponseModel {

	private final static String bocBankInfo_const = "bocBankInfo";
	private final static String orgName_const = "orgName";
	private final static String orgCode_const = "orgCode";
	private final static String orgType_const = "orgType";

	private List<BocBankInfo> bocBankInfoList = new ArrayList<MAOPQueryElecAccOpeningBankResponseModel.BocBankInfo>();
	
	public List<BocBankInfo> getBocBankInfoList(){
		return bocBankInfoList;
	}
	
	public MAOPQueryElecAccOpeningBankResponseModel(JSONObject jsonResponse) throws JSONException{
		JSONArray list= jsonResponse.optJSONArray(bocBankInfo_const);
		for(int i = 0; list !=null && i < list.length(); i++) {
	            JSONObject  info = list.getJSONObject(i);
	            BocBankInfo item = new BocBankInfo();
	            item.setOrgName(info.optString(orgName_const));
	            item.setOrgCode(info.optString(orgCode_const));
	            item.setOrgType(info.optString(orgType_const));
	            bocBankInfoList.add(item);
		}
	}
	
	public static final Creator<MAOPQueryElecAccOpeningBankResponseModel> CREATOR = new Creator<MAOPQueryElecAccOpeningBankResponseModel>() {
        @Override
        public MAOPQueryElecAccOpeningBankResponseModel createFromJson(JSONObject jsonResponse) throws JSONException {
            return new MAOPQueryElecAccOpeningBankResponseModel(jsonResponse);
        }
        
    };
    
    public static class BocBankInfo{
    	private String orgName;
    	private String orgCode;
    	private String orgType;
    	
		public String getOrgName() {
			return orgName;
		}
		public void setOrgName(String orgName) {
			this.orgName = orgName;
		}
		public String getOrgCode() {
			return orgCode;
		}
		public void setOrgCode(String orgCode) {
			this.orgCode = orgCode;
		}
		public String getOrgType() {
			return orgType;
		}
		public void setOrgType(String orgType) {
			this.orgType = orgType;
		}
    }
}
