package com.chinamworld.bocmbci.biz.crcd.mycrcd;


public class CardEntity {

	private String masterorsuppl;//主附卡标示  1 主卡 2附属卡
	private String mastercrcdNum;//主卡卡号
	private String subcrcdNum;//附卡卡号
	private String subcrcdname;//附卡名称
	private String accountid;//
	private String accounttype;//

	
	public String getMastercrcdNum() {
		return mastercrcdNum;
	}






	public void setMastercrcdNum(String mastercrcdNum) {
		this.mastercrcdNum = mastercrcdNum;
	}






	public String getSubcrcdNum() {
		return subcrcdNum;
	}






	public void setSubcrcdNum(String subcrcdNum) {
		this.subcrcdNum = subcrcdNum;
	}






	public String getSubcrcdname() {
		return subcrcdname;
	}






	public void setSubcrcdname(String subcrcdname) {
		this.subcrcdname = subcrcdname;
	}






	public void setMasterorsuppl(String masterorsuppl) {
		this.masterorsuppl = masterorsuppl;
	}






	public String getMasterorsuppl() {
		return masterorsuppl;
	}






	public String getAccountid() {
		return accountid;
	}






	public void setAccountid(String accountid) {
		this.accountid = accountid;
	}






	public String getAccounttype() {
		return accounttype;
	}






	public void setAccounttype(String accounttype) {
		this.accounttype = accounttype;
	}






	/**
	 * 无参构造函数
	 */
	public CardEntity() {
		super();
	}
	
	


	


}
