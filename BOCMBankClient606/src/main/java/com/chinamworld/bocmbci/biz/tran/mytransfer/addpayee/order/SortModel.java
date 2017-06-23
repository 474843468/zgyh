package com.chinamworld.bocmbci.biz.tran.mytransfer.addpayee.order;

public class SortModel {

	private String bankName;   //显示的数据  
	private String sortLetters; //显示数据拼音的首字母  
	private String bankAlias;
	private String bankBtp;
	private String bankCcpc;
	private String bankType;
	private String bankStatus;
	private String bankBpm;
	private String bankClr;
	private String bankCode;
	private String flag = "N"; //是否是常用联系人     默认否
	
	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getName() {
		return bankName;
	}
	public String getBankAlias() {
		return bankAlias;
	}
	public void setBankAlias(String bankAlias) {
		this.bankAlias = bankAlias;
	}
	public String getBankBtp() {
		return bankBtp;
	}
	public void setBankBtp(String bankBtp) {
		this.bankBtp = bankBtp;
	}
	public String getBankCcpc() {
		return bankCcpc;
	}
	public void setBankCcpc(String bankCcpc) {
		this.bankCcpc = bankCcpc;
	}
	public String getBankType() {
		return bankType;
	}
	public void setBankType(String bankType) {
		this.bankType = bankType;
	}
	public String getBankStatus() {
		return bankStatus;
	}
	public void setBankStatus(String bankStatus) {
		this.bankStatus = bankStatus;
	}
	public String getBankBpm() {
		return bankBpm;
	}
	public void setBankBpm(String bankBpm) {
		this.bankBpm = bankBpm;
	}
	public String getBankClr() {
		return bankClr;
	}
	public void setBankClr(String bankClr) {
		this.bankClr = bankClr;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public void setName(String name) {
		this.bankName = name;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
}
