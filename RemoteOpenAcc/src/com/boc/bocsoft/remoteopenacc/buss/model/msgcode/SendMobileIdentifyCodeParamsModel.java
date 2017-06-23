package com.boc.bocsoft.remoteopenacc.buss.model.msgcode;

import com.boc.bocma.serviceinterface.op.interfacemodel.sendmobileidentifycode.MAOPSendMobileIdentifyCodeParamsModel;
import com.boc.bocsoft.remoteopenacc.common.model.BaseParamsModel;

/**
 * 发送手机验证码请求Model 1.2.7 【SA9185】发送手机验证码
 * 
 * @author fb
 * 
 */
public class SendMobileIdentifyCodeParamsModel implements BaseParamsModel {

	public String mobile; // 手机号
	public String transType; // 交易类型01：个人身份信息联网核查

	// 02：查询开户进度

	@Override
	public MAOPSendMobileIdentifyCodeParamsModel transformParamsModel() {
		MAOPSendMobileIdentifyCodeParamsModel model = new MAOPSendMobileIdentifyCodeParamsModel();
		model.setMobile(mobile);
		model.setTransType(transType);
		return model;
	}

}
