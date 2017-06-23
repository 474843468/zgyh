package com.boc.bocsoft.remoteopenacc.buss.model.getrandomnum;

import com.boc.bocma.serviceinterface.op.interfacemodel.get_random_num.MAOPGetRandomNumResponseModel;
import com.boc.bocsoft.remoteopenacc.common.model.BaseResultModel;

/**
 * 获取随机数返回model
 * @author lxw
 *
 */
public class GetRandomNumResponseModel implements BaseResultModel<GetRandomNumResponseModel> {


	public String serverRandom;
	
	@Override
	public GetRandomNumResponseModel parseResultModel(Object resultModel) {
		MAOPGetRandomNumResponseModel result =(MAOPGetRandomNumResponseModel)resultModel;
		this.serverRandom = result.getServerRandom();
		return this;
	}
}
