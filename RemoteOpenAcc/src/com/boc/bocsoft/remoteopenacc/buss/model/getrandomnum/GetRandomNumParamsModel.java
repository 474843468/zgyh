package com.boc.bocsoft.remoteopenacc.buss.model.getrandomnum;

import com.boc.bocma.serviceinterface.op.interfacemodel.get_random_num.MAOPGetRandomNumParamsModel;
import com.boc.bocsoft.remoteopenacc.common.model.BaseParamsModel;

/**
 * 生成服务器随机数请求model
 * 
 * @author lxw
 * 
 */
public class GetRandomNumParamsModel implements BaseParamsModel {

	// bocop-中银开放平台
	public String systemFlag;


	@Override
	public MAOPGetRandomNumParamsModel transformParamsModel() {
		MAOPGetRandomNumParamsModel model = new MAOPGetRandomNumParamsModel();
		model.setSystemFlag(this.systemFlag);
		return model;
	}

}
