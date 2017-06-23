package com.boc.bocma.serviceinterface.op.interfacemodel.queryelecaccattr;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

public class MAOPQueryElecAccAttrResponseModel extends MAOPBaseResponseModel {

	private static final String distMappings_const = "distMappings";
	private static final String distCode_const = "distCode";
	private static final String distName_const = "distName";
	private static final String parDist_const = "parDist";
	private static final String distType_const = "distType";
	private static final String distIndex_const = "distIndex";

	List<DistMapping> distMappingsList = new ArrayList<DistMapping>();
	
	public MAOPQueryElecAccAttrResponseModel(JSONObject jsonResponse) throws JSONException{
		JSONArray list = jsonResponse.optJSONArray(distMappings_const);
		for (int i = 0; list !=null && i < list.length(); i++) {
			 JSONObject  info = list.getJSONObject(i);
			 DistMapping item = new DistMapping();
			 item.setDistCode(info.optString(distCode_const));
			 item.setDistName(info.optString(distName_const));
			 item.setParDist(info.optString(parDist_const));
			 item.setDistType(info.optString(distType_const));
			 item.setDistIndex(info.optString(distIndex_const));
			 distMappingsList.add(item);
		}
	}
	
	
	public static final Creator<MAOPQueryElecAccAttrResponseModel> CREATOR = new Creator<MAOPQueryElecAccAttrResponseModel>() {
        @Override
        public MAOPQueryElecAccAttrResponseModel createFromJson(JSONObject jsonResponse) throws JSONException {
            return new MAOPQueryElecAccAttrResponseModel(jsonResponse);
        }
        
    };
    
	public List<DistMapping> getDistMappingsList() {
		return distMappingsList;
	}

	public void setDistMappingsList(List<DistMapping> distMappingsList) {
		this.distMappingsList = distMappingsList;
	}

	public static class DistMapping {
		private String distCode;
		private String distName;
		private String parDist;
		private String distType;
		private String distIndex;

		public String getDistCode() {
			return distCode;
		}

		public void setDistCode(String distCode) {
			this.distCode = distCode;
		}

		public String getDistName() {
			return distName;
		}

		public void setDistName(String distName) {
			this.distName = distName;
		}

		public String getParDist() {
			return parDist;
		}

		public void setParDist(String parDist) {
			this.parDist = parDist;
		}

		public String getDistType() {
			return distType;
		}

		public void setDistType(String distType) {
			this.distType = distType;
		}

		public String getDistIndex() {
			return distIndex;
		}

		public void setDistIndex(String distIndex) {
			this.distIndex = distIndex;
		}

	}
}
