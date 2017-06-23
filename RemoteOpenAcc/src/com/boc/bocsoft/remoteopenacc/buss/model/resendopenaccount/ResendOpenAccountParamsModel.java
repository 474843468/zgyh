package com.boc.bocsoft.remoteopenacc.buss.model.resendopenaccount;

import com.boc.bocma.serviceinterface.op.interfacemodel.resendopenaccount.MAOPResendOpenAccountParamsModel;
import com.boc.bocsoft.remoteopenacc.common.model.BaseParamsModel;

/**
 * 1.2.12 【SA01458】重新发送开户请求接口 请求model
 * 
 * @author lgw<br>
 *         x64 增加reUuid1，reUuid2
 * 
 */
public class ResendOpenAccountParamsModel implements BaseParamsModel {
	/**
	 * 身份证号 X(18) 18位最后一位可为X，其余为纯数字 Y
	 */
	// public String certNo;
	/**
	 * 待重发二（二、三同时）类账户开户申请uuid String(32) N reUuid1和reUuid2不能同时为空
	 */
	public String reUuid1;
	/**
	 * 待重发三类账户开户申请uuid String(32) N
	 */
	public String reUuid2;

	@Override
	public MAOPResendOpenAccountParamsModel transformParamsModel() {
		MAOPResendOpenAccountParamsModel model = new MAOPResendOpenAccountParamsModel();
		// model.setCertNo(certNo);
		model.setReUuid1(reUuid1);
		model.setReUuid2(reUuid2);
		return model;
	}
}
