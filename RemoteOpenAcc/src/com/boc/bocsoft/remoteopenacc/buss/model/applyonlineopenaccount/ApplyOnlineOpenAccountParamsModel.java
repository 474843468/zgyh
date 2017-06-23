package com.boc.bocsoft.remoteopenacc.buss.model.applyonlineopenaccount;

import com.boc.bocma.serviceinterface.op.interfacemodel.applyonlineopenaccount.MAOPApplyOnlineOpenAccountParamsModel;
import com.boc.bocsoft.remoteopenacc.common.model.BaseParamsModel;

/**
 * 1.2.5 【SA9182】开户前身份验证请求model
 * 
 * @author lxw
 * @version p601 1、新增23-35项，请详细阅读上送报文接口列表 修改salary字段类型长度和说明 2、修改渠道标识channelFlag
 *          3、核心修改此字段为可输，删除addressCode字段 by lgw at 2015.12.3<br>
 * 
 */
public class ApplyOnlineOpenAccountParamsModel implements BaseParamsModel {

	// X(20) bocop-中银开放平台
	// bocnet-手机银行 Y
	public String systemFlag;
	/**
	 * p601 修改渠道标识<br>
	 * X(2) 1-手机终端 2-PC终端 3-WEB接入TDG 4-APP 接入TDG 5-第三方接入TDG 6-手机银行接入TDG
	 */
	public String channelFlag;
	// 应用标识
	public String clientId;
	// uuid
	public String uuid;
	// 身份证起始日
	public String certValidDateFrom;
	// 身份证截止日
	public String certValidDateTo;
	// 姓名拼音 -姓
	public String custNameSpell;
	/**
	 * p601 姓名拼音-名 String(20) 只允许半角“A-Za-z.”及空格 Y
	 */
	public String custNameSpells;
	// 家庭地址1
	public String custAddress1;
	// 家庭地址2
	public String custAddress2;
	// 家庭地址3
	public String custAddress3;
	// 邮编
	public String zipCode;
	// 职业
	public String vocation;
	/**
	 * 月收入 p601 修改为一个字符长度<br>
	 * String(1) 1:0-4999 2:5000-19999 3:20000-49999 4:50000-99999 5:100000以上
	 * ，必输Y
	 */
	// public String salary;
	// 中行账户开户行行内机构号
	public String orgCode;
	/**
	 * 机构名称
	 */
	public String orgName;
	// 他行卡账号，x64修改为 绑定卡账号
	public String othCardNo;
	// 他行卡开户银行人行行号
	// public String othCardCnaps;
	// 他行卡开户银行人行行号总行人行号，x64改为，绑定卡总行行号
	public String othCardTopCnaps;
	// 他行开户行名称，x64改为，绑定卡总行名称
	public String othCardBankName;
	// 输入电子卡密码
	public String password;
	// 输入密码随机串
	public String password_RC;
	// 确认电子卡密码
	public String affirmPass;
	// 确认密码随机串
	public String affirmPass_RC;
	// 服务器端随机串
	public String rs;
	// public String companyName;// 单位名称 X(60) 当职业代码不为Z0：无职业活动人员时，必输；其他情况可输 N
	// public String companyType;// 单位所属行业 X(2) 见附录2
	// 当职业代码不为Z0：无职业活动人员时，必输；其他情况可输N
	public String nation;// 民族 X(2) 见附录3 Y
	/**
	 * 账户用途 X(10) 此账户用途字段共10个字节： 第1位是储蓄， 第2位是代发工资， 第3位是社保医疗， 第,4位是投资理财，
	 * 第,5位是偿还贷款， 第,6位是处理日常收支， 第7位是其他， 第8位至第10位暂留 0表示不选择此字节代表的选项，1表示选择此字节代表的此项。
	 * 若是账户用途选择为储蓄、投资理财和其他，此字段请送值：1001001000 对私账户至少选择其中一个用途，不能送值全0
	 */
	// public String purpose;
	/**
	 * 开户原因 X(10) 此开户原因字段共10个字节： 第1位是移民， 第2位是留学， 第3位是工作， 第4位是投资， 第5位是其他，
	 * 第6位至第10位暂留 0表示不选择此字节代表的选项，1表示选择此字节代表的此项。 若是账户用途为留学和其他，此字段请送值：0100100000 Y
	 */
	// public String reason;
	public String openBocnet;// 是否开通网银 X(1) 0不开通 1开通 Y
	// public String bindBocnet;// 是否绑定网银 X(1) 0不绑定 1绑定 Y
	public String openEz;// 是否开通易商 X(1) 0不开通 1开通 Y
	public String bindEz;// 是否绑定易商 X(1) 0不绑定 1绑定 Y
	public String bindExEz;// 三类账户是否绑定易商 String(1) Y 0不绑定 1绑定
	public String openPhone;// 是否开通电话银行 X(1) 0不开通 1开通 Y
	//2016-05-26 20:07:26 yx  delete 改造
//	public String bindPhone;// 是否绑定电话银行 X(1) 0不绑定 1绑定 Y
	public String custNo;// 易商登录用户ID X（16） 用户登录时的userId N
	public String openAccountType;// 开卡类型 String(2) Y
									// 01开立二类账户02开立三类账户03同时开立二、三类账户

	// p601新增结束

	@Override
	public MAOPApplyOnlineOpenAccountParamsModel transformParamsModel() {
		MAOPApplyOnlineOpenAccountParamsModel model = new MAOPApplyOnlineOpenAccountParamsModel();
		// 系统标识
		model.setSystemFlag(systemFlag);
		// 渠道标识
		model.setChannelFlag(channelFlag);
		// 应用标识
		model.setClientId(clientId);
		// uuid
		model.setUuid(uuid);
		// 身份证有效期
		model.setCertValidDateFrom(certValidDateFrom);
		model.setCertValidDateTo(certValidDateTo);
		// 姓名拼音
		model.setCustNameSpell(custNameSpell);
		// 家庭地址1
		model.setCustAddress1(custAddress1);
		// 家庭地址2
		model.setCustAddress2(custAddress2);
		// 家庭地址3
		model.setCustAddress3(custAddress3);
		// 邮编
		model.setZipCode(zipCode);
		// 职业
		model.setVocation(vocation);
		// 月收入
		// model.setSalary(salary);
		// 中行账户开户行行内机构号
		model.setOrgCode(orgCode);
		model.setOrgName(orgName);
		// 他行卡账号
		model.setOthCardNo(othCardNo);
		// 他行卡开户银行人行行号
		// model.setOthCardCnaps(othCardCnaps);
		// 他行卡开户银行人行行号总行人行号
		model.setOthCardTopCnaps(othCardTopCnaps);
		// 他行开户行名称
		model.setOthCardBankName(othCardBankName);
		// 输入电子卡密码
		model.setPassword(password);
		// 输入密码随机串
		model.setPassword_RC(password_RC);
		// 确认电子卡密码
		model.setAffirmPass(affirmPass);
		// 确认密码随机串
		model.setAffirmPass_RC(affirmPass_RC);
		// 服务器端随机串
		model.setRs(rs);
		// p601新增
		model.setCustNameSpells(custNameSpells);
		// model.setCompanyName(companyName);
		// model.setCompanyType(companyType);
		model.setNation(nation);
		// model.setPurpose(purpose);
		// model.setReason(reason);
		model.setOpenBocnet(openBocnet);
		// model.setBindBocnet(bindBocnet);
		model.setOpenEz(openEz);
		model.setBindEz(bindEz);
		model.setBindExEz(bindExEz);
		model.setOpenPhone(openPhone);
//		model.setBindPhone(bindPhone);
		model.setCustNo(custNo);
		model.setOpenAccountType(openAccountType);
		return model;
	}

}
