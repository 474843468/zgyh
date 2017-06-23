package com.boc.bocsoft.remoteopenacc.buss.model.queryprovinceandcity;

import com.boc.bocma.serviceinterface.op.interfacemodel.queryprovinceandcity.MAOPQueryProvinceAndCityParamsModel;
import com.boc.bocsoft.remoteopenacc.common.model.BaseParamsModel;

/**
 * 开户行模糊查询请求model
 * 
 * @author lxw
 * @version p601添加pageFlag字段输入，用来区分个人信息页面和归属地查询页面，此条件是为了判断查询城市列表。by lgw at
 *          15.12.3
 */
public class QueryProvinceAndCityParamsModel implements BaseParamsModel {

	/**
	 * 地区类型 String(1) 0-省份；1-城市
	 */
	public String distType;
	/**
	 * 地区代码 String(5) 当地区类型上送1-城市时，该输入项必输，为查省份时返回的地区代码
	 */
	public String distCode;
	/**
	 * p601 新增 页面标识 String(1) 0-个人信息页面； 1-归属地查询页面。
	 */
	public String pageFlag;

	@Override
	public MAOPQueryProvinceAndCityParamsModel transformParamsModel() {
		MAOPQueryProvinceAndCityParamsModel model = new MAOPQueryProvinceAndCityParamsModel();
		model.setDistCode(distCode);
		model.setDistType(distType);
		model.setPageFlag(pageFlag);
		return model;
	}

}
