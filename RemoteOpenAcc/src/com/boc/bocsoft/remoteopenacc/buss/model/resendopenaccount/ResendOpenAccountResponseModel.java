package com.boc.bocsoft.remoteopenacc.buss.model.resendopenaccount;

import com.boc.bocma.serviceinterface.op.interfacemodel.resendopenaccount.MAOPResendOpenAccountResponseModel;
import com.boc.bocsoft.remoteopenacc.common.model.BaseResultModel;

/**
 * 1.2.12 【SA01458】重新发送开户请求接口 返回model
 * 
 * @author lgw
 * 
 */
public class ResendOpenAccountResponseModel implements
		BaseResultModel<ResendOpenAccountResponseModel> {
	/**
	 * 返回码 X(7) 0000000:交易成功 9999999:系统异常 1000001:必输项不能为空 1000002:数据校验失败
	 * 1000003:查询结果为空 Y
	 */
	public String responseCode;

	public String responseMsg;// 返回消息 X(100) 必输

	@Override
	public ResendOpenAccountResponseModel parseResultModel(Object resultModel) {
		MAOPResendOpenAccountResponseModel result = (MAOPResendOpenAccountResponseModel) resultModel;
		responseCode = result.getResponseCode();
		responseMsg = result.getResponseMsg();
		return this;
	}

}
