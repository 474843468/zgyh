package com.boc.bocsoft.remoteopenacc.buss.model.checkpersonidentity;

import android.util.Log;

import com.boc.bocma.serviceinterface.op.interfacemodel.checkpersonidentity.MAOPCheckPersonIdentityParamsModel;
import com.boc.bocsoft.remoteopenacc.common.model.BaseParamsModel;

/**
 * 开户前身份验证请求model
 * 
 * @author fb
 * 
 */
public class CheckPersonIdentityParamsModel implements BaseParamsModel {

	public String custSurname; // 姓
	public String custName; // 名
	public String certNo; // 身份证号
	public String mobile; // 手机号
	public String validCode; // 短信验证码
	// p604改造 lgw 2016.6.12>>>>
	public String validRs; // 短信验证码随机串 String(172) 短信验证码加密用的随机串 Y
	public String rs; // 服务器端随机串 String(24) 由“获取服务器随机数”接口获得 Y
	// p604改造 lgw 2016.6.12<<<<
	public String orgIdt;// 网点机构号 String(5) 非必填，5位数字 N
	public String flag;// 渠道标识 String(7) 非必填，7位数字 N

	@Override
	public MAOPCheckPersonIdentityParamsModel transformParamsModel() {
		MAOPCheckPersonIdentityParamsModel model = new MAOPCheckPersonIdentityParamsModel();
		model.setCustSurname(custSurname);
		model.setCustName(custName);
		model.setCertNo(certNo);
		model.setMobile(mobile);
		model.setValidCode(validCode);
		// p604改造 lgw 2016.6.12>>>>
		model.setValidRs(validRs);
		model.setRs(rs);
		// p604改造 lgw 2016.6.12<<<<
		model.setFlag(flag);
		model.setOrgIdt(orgIdt);
		Log.d("feib", "---->model" + "  " + custSurname);
		return model;
	}

}
