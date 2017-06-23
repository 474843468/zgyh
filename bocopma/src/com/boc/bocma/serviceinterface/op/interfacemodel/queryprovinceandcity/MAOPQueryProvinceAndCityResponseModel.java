package com.boc.bocma.serviceinterface.op.interfacemodel.queryprovinceandcity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

/**
 * 所属地方返回报文
 * 
 * @author lxw
 * 
 */
public class MAOPQueryProvinceAndCityResponseModel extends
		MAOPBaseResponseModel {

	private final static String distMapping_const = "distMapping";
	private final static String distCode_const = "distCode";
	private final static String distType_const = "distType";
	private final static String distName_const = "distName";
	private final static String parDist_const = "parDist";
	private final static String distIndex_const = "distIndex";

	private List<DistMapping> distMappingList = new ArrayList<DistMapping>();

	public MAOPQueryProvinceAndCityResponseModel(JSONObject jsonResponse)
			throws JSONException {
		JSONArray distMappingResultList = jsonResponse
				.optJSONArray(distMapping_const);
		for (int i = 0; distMappingResultList != null
				&& i < distMappingResultList.length(); i++) {
			JSONObject distMappingInfo = distMappingResultList.getJSONObject(i);
			DistMapping item = new DistMapping();
			item.setDistCode(distMappingInfo.optString(distCode_const));
			item.setDistType(distMappingInfo.optString(distType_const));
			item.setDistName(distMappingInfo.optString(distName_const));
			item.setParDist(distMappingInfo.optString(parDist_const));
			item.setDistIndex(distMappingInfo.optString(distIndex_const));
			distMappingList.add(item);
		}
	}

	public List<DistMapping> getDistMappingList() {
		return distMappingList;
	}

	public static final Creator<MAOPQueryProvinceAndCityResponseModel> CREATOR = new Creator<MAOPQueryProvinceAndCityResponseModel>() {
		@Override
		public MAOPQueryProvinceAndCityResponseModel createFromJson(
				JSONObject jsonResponse) throws JSONException {
			return new MAOPQueryProvinceAndCityResponseModel(jsonResponse);
		}

	};

	public static class DistMapping {
		// 地区代码 p601 String(6) 查询省份时，返回为身份代码，查询城市时，返回为城市代码
		private String distCode;
		// 地区类型
		private String distType;
		// 地区名称
		private String distName;
		// 上级地区代码
		private String parDist;
		// 地区索引
		private String distIndex;

		public DistMapping() {

		}

		public String getDistCode() {
			return distCode;
		}

		public void setDistCode(String distCode) {
			this.distCode = distCode;
		}

		public String getDistType() {
			return distType;
		}

		public void setDistType(String distType) {
			this.distType = distType;
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

		public String getDistIndex() {
			return distIndex;
		}

		public void setDistIndex(String distIndex) {
			this.distIndex = distIndex;
		}

	}
}
