package com.boc.bocsoft.remoteopenacc.buss.model.querysysdate;

import com.boc.bocma.serviceinterface.op.interfacemodel.querysysdate.MAOPQuerySysDateParamsModel;
import com.boc.bocsoft.remoteopenacc.common.model.BaseParamsModel;

/**
 * 获取系统日期请求Model
 * @author fb
 *
 */
public class QuerySysDateParamsModel  implements BaseParamsModel{

	
	@Override
	public MAOPQuerySysDateParamsModel transformParamsModel() {
		MAOPQuerySysDateParamsModel model = new MAOPQuerySysDateParamsModel();
		return model;
	}
}
