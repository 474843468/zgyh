package com.boc.bocma.serviceinterface.op.interfacemodel.applyonlineopenaccount;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

public class MAOPApplyOnlineOpenAccountParamsModel extends MAOPBaseParamsModel {

	private static final String INTERFACE_URL = "applyonlineopenaccount";

	private static final String systemFlag_const = "systemFlag";
	private static final String channelFlag_const = "channelFlag";
	private static final String clientId_const = "clientId";
	private static final String uuid_const = "uuid";
	private static final String certValidDateFrom_const = "certValidDateFrom";
	private static final String certValidDateTo_const = "certValidDateTo";
	private static final String custNameSpell_const = "custNameSpell";
	private static final String custAddress1_const = "custAddress1";
	private static final String custAddress2_const = "custAddress2";
	private static final String custAddress3_const = "custAddress3";
	private static final String zipCode_const = "zipCode";
	private static final String vocation_const = "vocation";
	private static final String orgCode_const = "orgCode";
	private static final String orgName_const = "orgName";
	private static final String othCardNo_const = "othCardNo";
	private static final String othCardTopCnaps_const = "othCardTopCnaps";
	private static final String othCardBankName_const = "othCardBankName";
	private static final String password_const = "password";
	private static final String password_RC_const = "password_RC";
	private static final String affirmPass_const = "affirmPass";
	private static final String affirmPass_RC_const = "affirmPass_RC";
	private static final String rs_const = "rs";
	// p601新增
	private static final String CUSTNAMESPELLS = "custNameSpells";
	// private static final String COMPANYNAME = "companyName";
	// private static final String COMPANYTYPE = "companyType";
	private static final String NATION = "nation";
	// private static final String PURPOSE = "purpose";
	// private static final String REASON = "reason";
	private static final String OPENBOCNET = "openBocnet";
	// private static final String BINDBOCNET = "bindBocnet";
	private static final String OPENEZ = "openEz";
	private static final String BINDEZ = "bindEz";
	private static final String BINDEXEZ = "bindExEz";
	private static final String OPENPHONE = "openPhone";
	private static final String BINDPHONE = "bindPhone";
	private static final String CUSTNO = "custNo";
	private static final String OPENACCOUNTTYPE = "openAccountType";

	@Override
	public String getUrl() {
		return OPURL.getFAABaseTransUrl() + INTERFACE_URL;
	}

	@Override
	public String getJsonBody() throws JSONException {
		JSONObject body = new JSONObject();
		body.put(systemFlag_const, systemFlag);
		body.put(channelFlag_const, channelFlag);
		body.put(clientId_const, clientId);
		body.put(uuid_const, uuid);
		body.put(certValidDateFrom_const, certValidDateFrom);
		body.put(certValidDateTo_const, certValidDateTo);
		body.put(custNameSpell_const, custNameSpell);
		body.put(custAddress1_const, custAddress1);
		body.put(custAddress2_const, custAddress2);
		body.put(custAddress3_const, custAddress3);
		body.put(zipCode_const, zipCode);
		body.put(vocation_const, vocation);
		// body.put(salary_const, salary);
		body.put(orgCode_const, orgCode);
		body.put(orgName_const, orgName);
		body.put(othCardNo_const, othCardNo);
		body.put(othCardTopCnaps_const, othCardTopCnaps);
		body.put(othCardBankName_const, othCardBankName);
		body.put(password_const, password);
		body.put(password_RC_const, password_RC);
		body.put(affirmPass_const, affirmPass);
		body.put(affirmPass_RC_const, affirmPass_RC);
		body.put(rs_const, rs);
		// p601新增
		body.put(CUSTNAMESPELLS, custNameSpells);
		// body.put(COMPANYNAME, companyName);
		// body.put(COMPANYTYPE, companyType);
		body.put(NATION, nation);
		// body.put(PURPOSE, purpose);
		// body.put(REASON, reason);
		body.put(OPENBOCNET, openBocnet);
		// body.put(BINDBOCNET, bindBocnet);
		body.put(OPENEZ, openEz);
		body.put(BINDEZ, bindEz);
		body.put(BINDEXEZ, bindExEz);
		body.put(OPENPHONE, openPhone);
//		body.put(BINDPHONE, bindPhone);
		body.put(CUSTNO, custNo);
		body.put(OPENACCOUNTTYPE, openAccountType);

		return body.toString();
	}

	/**
	 * X(20) bocop-中银开放平台 bocnet-手机银行 Y
	 */
	private String systemFlag;
	/**
	 * p601 修改渠道标识<br>
	 * X(2) 1-手机终端 2-PC终端 3-WEB接入TDG 4-APP 接入TDG 5-第三方接入TDG 6-手机银行接入TDG
	 */
	private String channelFlag;
	private String clientId;
	private String uuid;
	private String certValidDateFrom;
	private String certValidDateTo;
	private String custNameSpell;
	private String custAddress1;
	private String custAddress2;
	private String custAddress3;
	private String zipCode;
	private String vocation;
	/**
	 * 月收入 p601 修改为一个字符长度<br>
	 * String(1) 1:0-4999 2:5000-19999 3:20000-49999 4:50000-99999 5:100000以上
	 * ，必输Y
	 */
	// private String salary;
	private String orgCode;
	private String orgName;
	private String othCardNo;
	// private String othCardCnaps;
	private String othCardTopCnaps;
	private String othCardBankName;
	private String password;
	private String password_RC;
	private String affirmPass;
	private String affirmPass_RC;
	private String rs;

	// 以下p601新增
	/**
	 * p601 姓名拼音-名 String(20) 只允许半角“A-Za-z.”及空格 Y
	 */
	private String custNameSpells;
	// private String companyName;// 单位名称 X(60) 当职业代码不为Z0：无职业活动人员时，必输；其他情况可输 N
	// private String companyType;// 单位所属行业 X(2) 见附录2
	// 当职业代码不为Z0：无职业活动人员时，必输；其他情况可输N
	private String nation;// 民族 X(2) 见附录3 Y
	/**
	 * 账户用途 X(10) 此账户用途字段共10个字节： 第1位是储蓄， 第2位是代发工资， 第3位是社保医疗， 第,4位是投资理财，
	 * 第,5位是偿还贷款， 第,6位是处理日常收支， 第7位是其他， 第8位至第10位暂留 0表示不选择此字节代表的选项，1表示选择此字节代表的此项。
	 * 若是账户用途选择为储蓄、投资理财和其他，此字段请送值：1001001000 对私账户至少选择其中一个用途，不能送值全0
	 */
	// private String purpose;
	/**
	 * 开户原因 X(10) 此开户原因字段共10个字节： 第1位是移民， 第2位是留学， 第3位是工作， 第4位是投资， 第5位是其他，
	 * 第6位至第10位暂留 0表示不选择此字节代表的选项，1表示选择此字节代表的此项。 若是账户用途为留学和其他，此字段请送值：0100100000 Y
	 */
	// private String reason;
	private String openBocnet;// 是否开通网银 X(1) 0不开通 1开通 Y
	// private String bindBocnet;// 是否绑定网银 X(1) 0不绑定 1绑定 Y
	private String openEz;// 是否开通易商 X(1) 0不开通 1开通 Y
	private String bindEz;// 是否绑定易商 X(1) 0不绑定 1绑定 Y
	private String bindExEz;// 三类账户是否绑定易商 String(1) Y 0不绑定 1绑定
	private String openPhone;// 是否开通电话银行 X(1) 0不开通 1开通 Y
//	2016-05-26 20:06:00 yx delete 改造
//	private String bindPhone;// 是否绑定电话银行 X(1) 0不绑定 1绑定 Y
	private String custNo;// 易商登录用户ID X（16） 用户登录时的userId N
	private String openAccountType;// 开卡类型 String(2) Y 01开立二类账户
									// 02开立三类账户03同时开立二、三类账户

	// p601新增结束

	public String getOpenAccountType() {
		return openAccountType;
	}

	public void setOpenAccountType(String openAccountType) {
		this.openAccountType = openAccountType;
	}

	public String getCustNo() {
		return custNo;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	public String getCustNameSpells() {
		return custNameSpells;
	}

	public void setCustNameSpells(String custNameSpells) {
		this.custNameSpells = custNameSpells;
	}

	// public String getCompanyName() {
	// return companyName;
	// }

	// public void setCompanyName(String companyName) {
	// this.companyName = companyName;
	// }
	//
	// public String getCompanyType() {
	// return companyType;
	// }
	//
	// public void setCompanyType(String companyType) {
	// this.companyType = companyType;
	// }

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	// public String getPurpose() {
	// return purpose;
	// }
	//
	// public void setPurpose(String purpose) {
	// this.purpose = purpose;
	// }

	// public String getReason() {
	// return reason;
	// }
	//
	// public void setReason(String reason) {
	// this.reason = reason;
	// }

	public String getOpenBocnet() {
		return openBocnet;
	}

	public void setOpenBocnet(String openBocnet) {
		this.openBocnet = openBocnet;
	}

	// public String getBindBocnet() {
	// return bindBocnet;
	// }
	//
	// public void setBindBocnet(String bindBocnet) {
	// this.bindBocnet = bindBocnet;
	// }

	public String getOpenEz() {
		return openEz;
	}

	public void setOpenEz(String openEz) {
		this.openEz = openEz;
	}

	public String getBindEz() {
		return bindEz;
	}

	public void setBindEz(String bindEz) {
		this.bindEz = bindEz;
	}

	public String getOpenPhone() {
		return openPhone;
	}

	public void setOpenPhone(String openPhone) {
		this.openPhone = openPhone;
	}

//	public String getBindPhone() {
//		return bindPhone;
//	}
//
//	public void setBindPhone(String bindPhone) {
//		this.bindPhone = bindPhone;
//	}

	public String getCustAddress1() {
		return custAddress1;
	}

	public void setCustAddress1(String custAddress1) {
		this.custAddress1 = custAddress1;
	}

	public String getSystemFlag() {
		return systemFlag;
	}

	public void setSystemFlag(String systemFlag) {
		this.systemFlag = systemFlag;
	}

	public String getChannelFlag() {
		return channelFlag;
	}

	public void setChannelFlag(String channelFlag) {
		this.channelFlag = channelFlag;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getCertValidDateFrom() {
		return certValidDateFrom;
	}

	public void setCertValidDateFrom(String certValidDateFrom) {
		this.certValidDateFrom = certValidDateFrom;
	}

	public String getCertValidDateTo() {
		return certValidDateTo;
	}

	public void setCertValidDateTo(String certValidDateTo) {
		this.certValidDateTo = certValidDateTo;
	}

	public String getCustNameSpell() {
		return custNameSpell;
	}

	public void setCustNameSpell(String custNameSpell) {
		this.custNameSpell = custNameSpell;
	}

	public String getCustAddress2() {
		return custAddress2;
	}

	public void setCustAddress2(String custAddress2) {
		this.custAddress2 = custAddress2;
	}

	public String getCustAddress3() {
		return custAddress3;
	}

	public void setCustAddress3(String custAddress3) {
		this.custAddress3 = custAddress3;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getVocation() {
		return vocation;
	}

	public void setVocation(String vocation) {
		this.vocation = vocation;
	}

	// public String getSalary() {
	// return salary;
	// }

	// public void setSalary(String salary) {
	// this.salary = salary;
	// }

	public String getOrgCode() {
		return orgCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOthCardNo() {
		return othCardNo;
	}

	public void setOthCardNo(String othCardNo) {
		this.othCardNo = othCardNo;
	}

	// public String getOthCardCnaps() {
	// return othCardCnaps;
	// }
	//
	// public void setOthCardCnaps(String othCardCnaps) {
	// this.othCardCnaps = othCardCnaps;
	// }

	public String getOthCardBankName() {
		return othCardBankName;
	}

	public void setOthCardBankName(String othCardBankName) {
		this.othCardBankName = othCardBankName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword_RC() {
		return password_RC;
	}

	public void setPassword_RC(String password_RC) {
		this.password_RC = password_RC;
	}

	public String getAffirmPass() {
		return affirmPass;
	}

	public void setAffirmPass(String affirmPass) {
		this.affirmPass = affirmPass;
	}

	public String getAffirmPass_RC() {
		return affirmPass_RC;
	}

	public void setAffirmPass_RC(String affirmPass_RC) {
		this.affirmPass_RC = affirmPass_RC;
	}

	public String getRs() {
		return rs;
	}

	public void setRs(String rs) {
		this.rs = rs;
	}

	public String getOthCardTopCnaps() {
		return othCardTopCnaps;
	}

	public void setOthCardTopCnaps(String othCardTopCnaps) {
		this.othCardTopCnaps = othCardTopCnaps;
	}

	public String getBindExEz() {
		return bindExEz;
	}

	public void setBindExEz(String bindExEz) {
		this.bindExEz = bindExEz;
	}

}
