package com.boc.bocsoft.remoteopenacc.buss.model.querysysdate;

import com.boc.bocma.serviceinterface.op.interfacemodel.querysysdate.MAOPQuerySysDateResponseModel;
import com.boc.bocsoft.remoteopenacc.common.model.BaseResultModel;

/**
 * 获取系统日期结果Model
 * @author fb
 *
 */
public class QuerySysDateResponseModel implements BaseResultModel<QuerySysDateResponseModel>{

	// 系统日期
	public String sysdate;	
	@Override
	public QuerySysDateResponseModel parseResultModel(
			Object resultModel) {
		MAOPQuerySysDateResponseModel result = (MAOPQuerySysDateResponseModel) resultModel;
		this.sysdate = result.getSysdate();
		return this;
	}

}
