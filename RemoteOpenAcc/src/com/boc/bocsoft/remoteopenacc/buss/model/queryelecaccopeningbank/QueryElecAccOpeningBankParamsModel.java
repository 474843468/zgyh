package com.boc.bocsoft.remoteopenacc.buss.model.queryelecaccopeningbank;

import com.boc.bocma.serviceinterface.op.interfacemodel.queryelecaccopeningbank.MAOPQueryElecAccOpeningBankParamsModel;
import com.boc.bocsoft.remoteopenacc.common.model.BaseParamsModel;

/**
 * 电子账户开户行查询
 * 
 * @author lxw
 * 
 */
public class QueryElecAccOpeningBankParamsModel implements BaseParamsModel {

	public String provinceCode;// 省份代码
	public String cityCode;// 机构类型

	@Override
	public MAOPQueryElecAccOpeningBankParamsModel transformParamsModel() {
		MAOPQueryElecAccOpeningBankParamsModel paramsModel = new MAOPQueryElecAccOpeningBankParamsModel();
		paramsModel.setProvinceCode(provinceCode);
		paramsModel.setCityCode(cityCode);
		return paramsModel;
	}

}
