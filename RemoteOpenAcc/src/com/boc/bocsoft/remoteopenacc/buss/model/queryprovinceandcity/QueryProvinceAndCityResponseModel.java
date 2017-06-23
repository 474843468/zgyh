package com.boc.bocsoft.remoteopenacc.buss.model.queryprovinceandcity;

import java.util.ArrayList;
import java.util.List;

import com.boc.bocma.serviceinterface.op.interfacemodel.queryopeningbank.MAOPQueryOpeningBankResponseModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.queryprovinceandcity.MAOPQueryProvinceAndCityResponseModel;
import com.boc.bocsoft.remoteopenacc.common.model.BaseResultModel;

/**
 * 电子账户账户归属地查询
 * 
 * @author lxw
 * 
 */
public class QueryProvinceAndCityResponseModel implements
		BaseResultModel<QueryProvinceAndCityResponseModel> {

	public List<DistMapping> distMappingList = new ArrayList<DistMapping>();

	public static class DistMapping {
		// 地区码 p601 String(6) 查询省份时，返回为身份代码，查询城市时，返回为城市代码
		public String distCode;
		// 地区类型
		public String distType;
		// 地区名称
		public String distName;
		// 上级地区代码
		public String parDist;
		// 地区索引
		public String distIndex;

	}

	@Override
	public QueryProvinceAndCityResponseModel parseResultModel(Object resultModel) {
		MAOPQueryProvinceAndCityResponseModel result = (MAOPQueryProvinceAndCityResponseModel) resultModel;
		List<MAOPQueryProvinceAndCityResponseModel.DistMapping> resultList = result
				.getDistMappingList();
		distMappingList.clear();
		for (MAOPQueryProvinceAndCityResponseModel.DistMapping disMap : resultList) {
			DistMapping item = new DistMapping();
			item.distCode = disMap.getDistCode();
			item.distIndex = disMap.getDistIndex();
			item.distName = disMap.getDistName();
			item.distType = disMap.getDistType();
			item.parDist = disMap.getParDist();
			distMappingList.add(item);
		}
		return this;
	}
}
