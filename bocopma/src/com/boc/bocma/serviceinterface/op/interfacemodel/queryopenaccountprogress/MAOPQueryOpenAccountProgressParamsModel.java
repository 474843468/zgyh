package com.boc.bocma.serviceinterface.op.interfacemodel.queryopenaccountprogress;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

/**
 * 1.2.6 【SA9184】查询开户进度
 * 
 * @author
 * @version p601 1.新增4-20项返回字段 ;2.将appliStat新增一个状态6，标识为重发状态。3.
 *          将custFullName改成custName by lgw at 15.12.3
 */
public class MAOPQueryOpenAccountProgressParamsModel extends
		MAOPBaseParamsModel {

	private static final String INTERFACE_URL = "queryopenaccountprogress";
	/**
	 * p601 将custFullName改成custName
	 */
	private static final String custName_const = "custName";
	private static final String certNo_const = "certNo";
	private static final String cardNo_const = "cardNo";
	private static final String mobile_const = "mobile";
	private static final String validCode_const = "validCode";
	// p604改造 lgw 2016.6.12>>>>
	private static final String validRs_const = "validRs";
	private static final String rs_const = "rs";
	private static final String flag_const = "flag";
	// p604改造 lgw 2016.6.12<<<<
	/**
	 * p601 将custFullName改成custName 姓名 X(32) 必输 Y
	 */
	private String custName;
	private String certNo;
	private String cardNo;
	private String mobile;
	private String validCode;
	// p604改造 lgw 2016.6.12>>>>
	private String validRs;
	private String rs;
	private String flag;
	// p604改造 lgw 2016.6.12<<<<

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

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
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

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	// p604改造 lgw 2016.6.12<<<<

	@Override
	public String getUrl() {
		return OPURL.getFAABaseTransUrl() + INTERFACE_URL;
	}

	@Override
	public String getJsonBody() throws JSONException {
		JSONObject body = new JSONObject();
		/**
		 * p601 将custFullName改成custName
		 */
		body.put(custName_const, custName);
		body.put(certNo_const, certNo);
		body.put(cardNo_const, cardNo);
		body.put(mobile_const, mobile);
		body.put(validCode_const, validCode);
		// p604改造 lgw 2016.6.12>>>>
		body.put(validRs_const, validRs);
		body.put(rs_const, rs);
		body.put(flag_const, flag);
		// p604改造 lgw 2016.6.12<<<<
		return body.toString();
	}

}
