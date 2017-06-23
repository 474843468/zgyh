package com.boc.bocsoft.remoteopenacc.buss.model.msgcode;

import com.boc.bocma.serviceinterface.op.interfacemodel.sendmobileidentifycode.MAOPSendMobileIdentifyCodeResponseModel;
import com.boc.bocsoft.remoteopenacc.common.model.BaseResultModel;

public class SendMobileIdentifyCodeResponseModel implements BaseResultModel<SendMobileIdentifyCodeResponseModel>{

	@Override
	public SendMobileIdentifyCodeResponseModel parseResultModel(
			Object resultModel) {
		MAOPSendMobileIdentifyCodeResponseModel result = (MAOPSendMobileIdentifyCodeResponseModel) resultModel;
		
		return this;
	}

}
