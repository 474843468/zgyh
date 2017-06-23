package com.boc.bocsoft.remoteopenacc.buss.model.queryelecaccopeningbank;

import java.util.ArrayList;
import java.util.List;

import com.boc.bocma.serviceinterface.op.interfacemodel.queryelecaccopeningbank.MAOPQueryElecAccOpeningBankResponseModel;
import com.boc.bocsoft.remoteopenacc.common.model.BaseResultModel;

/**
 * 电子账户开户行查询
 * 
 * @author lxw
 * 
 */
public final class QueryElecAccOpeningBankResponseModel implements
		BaseResultModel<QueryElecAccOpeningBankResponseModel> {

	public List<BocBankInfo> bocBankInfoList = new ArrayList<BocBankInfo>();

	public static class BocBankInfo {
		
		// 地区代码
		public String orgName;
		// 地区名称
		public String orgCode;
		// 上级地区代码
		public String orgType;

	}

	@Override
	public QueryElecAccOpeningBankResponseModel parseResultModel(
			Object resultModel) {
		MAOPQueryElecAccOpeningBankResponseModel result = (MAOPQueryElecAccOpeningBankResponseModel) resultModel;
		
		List<MAOPQueryElecAccOpeningBankResponseModel.BocBankInfo> resultBocBankInfoList = result.getBocBankInfoList();
		//othBankInfoList.clear();
		
		for (MAOPQueryElecAccOpeningBankResponseModel.BocBankInfo bocBankInfo : resultBocBankInfoList) {
			BocBankInfo item = new BocBankInfo();
			item.orgName = bocBankInfo.getOrgName();
			item.orgCode = bocBankInfo.getOrgCode();
			item.orgType = bocBankInfo.getOrgType();
			bocBankInfoList.add(item);
		}
		return this;
	}
}
