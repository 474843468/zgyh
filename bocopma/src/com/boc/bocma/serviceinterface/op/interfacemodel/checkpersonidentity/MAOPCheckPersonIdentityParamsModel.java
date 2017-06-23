package com.boc.bocma.serviceinterface.op.interfacemodel.checkpersonidentity;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

/**
 * 1.2.1 【SA9178】开户前校验接口 checkpersonidentity
 * 
 * @author gwluo
 * 
 */
public class MAOPCheckPersonIdentityParamsModel extends MAOPBaseParamsModel {

	private static final String INTERFACE_URL = "checkpersonidentity";

	private static final String custSurname_const = "custSurname";
	private static final String custName_const = "custName";
	private static final String certNo_const = "certNo";
	private static final String mobile_const = "mobile";
	private static final String validCode_const = "validCode";
	// p604改造 lgw 2016.6.12>>>>
	private static final String validRs_const = "validRs";
	private static final String rs_const = "rs";
	// p604改造 lgw 2016.6.12<<<<
	private static final String orgIdt_const = "orgIdt";
	private static final String flag_const = "flag";

	public String getCustSurname() {
		return custSurname;
	}

	public void setCustSurname(String custSurname) {
		this.custSurname = custSurname;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCertNo() {
		return certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getValidCode() {
		return validCode;
	}

	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}

	public String getOrgIdt() {
		return orgIdt;
	}

	public void setOrgIdt(String orgIdt) {
		this.orgIdt = orgIdt;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	// p604改造 lgw 2016.6.12>>>>
	public String getValidRs() {
		return validRs;
	}

	public void setValidRs(String validRs) {
		this.validRs = validRs;
	}

	public String getRs() {
		return rs;
	}

	public void setRs(String rs) {
		this.rs = rs;
	}

	// p604改造 lgw 2016.6.12<<<<

	private String custSurname;
	private String custName;
	private String certNo;
	private String mobile;
	private String validCode;
	// p604改造 lgw 2016.6.12>>>>
	private String validRs; // 短信验证码随机串 String(172) 短信验证码加密用的随机串 Y
	private String rs; // 服务器端随机串 String(24) 由“获取服务器随机数”接口获得 Y
	// p604改造 lgw 2016.6.12<<<<
	private String orgIdt;
	private String flag;

	@Override
	public String getUrl() {
		return OPURL.getFAABaseTransUrl() + INTERFACE_URL;
	}

	@Override
	public String getJsonBody() throws JSONException {
		JSONObject body = new JSONObject();
		body.put(custSurname_const, custSurname);
		body.put(custName_const, custName);
		body.put(certNo_const, certNo);
		body.put(mobile_const, mobile);
		body.put(validCode_const, validCode);
		// p604改造 lgw 2016.6.12>>>>
		body.put(validRs_const, validRs);
		body.put(rs_const, rs);
		// p604改造 lgw 2016.6.12<<<<
		body.put(orgIdt_const, orgIdt);
		body.put(flag_const, flag);
		return body.toString();
	}

}
