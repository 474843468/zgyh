package com.boc.bocsoft.remoteopenacc.buss.model.queryopenaccountprogress;

import android.util.Log;

import com.boc.bocma.serviceinterface.op.interfacemodel.queryopenaccountprogress.MAOPQueryOpenAccountProgressParamsModel;
import com.boc.bocsoft.remoteopenacc.common.model.BaseParamsModel;

/**
 * 查询开户进度请求model
 * 
 * @author lxw
 * 
 */
public class QueryOpenAccountProgressParamsModel implements BaseParamsModel {
	/**
	 * p601 将custFullName改成custName
	 */
	public String custName; // 姓名
	public String certNo; // 身份证号
	public String cardNo; // 他行卡号
	public String mobile; // 手机号
	public String validCode; // 短信验证码
	// p604改造 lgw 2016.6.12>>>>
	public String validRs; // 短信验证码随机串 String(172) Y 短信验证码加密用的随机串
	public String rs; // 服务器端随机串 String(24) Y 由“获取服务器随机数”接口获得
	public String flag; // 渠道标识 String(7) Y 1 WEB 2 APP 3 手机银行

	// p604改造 lgw 2016.6.12<<<<

	@Override
	public MAOPQueryOpenAccountProgressParamsModel transformParamsModel() {
		MAOPQueryOpenAccountProgressParamsModel model = new MAOPQueryOpenAccountProgressParamsModel();
		model.setCardNo(cardNo);
		model.setCertNo(certNo);
		model.setCustName(custName);
		model.setMobile(mobile);
		model.setValidCode(validCode);
		// p604改造 lgw 2016.6.12>>>>
		model.setValidRs(validRs);
		model.setRs(rs);
		model.setFlag(flag);
		// p604改造 lgw 2016.6.12<<<<
		Log.d("feib", "---->model" + "  " + model.toString());
		return model;
	}

}
