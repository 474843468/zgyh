package com.boc.device.key;

/**
 * key中证书信息数据结构
 * @author lxw
 *
 */
public class KeyCertInfo {

	// 证书颁发者
	private String publisher;
	
	// 证书所有者
	private String owner;
	
	// 证书开始日期(日期 YYMMDD)
	private String startDate;
	
	// 证书过期日期(YYMMDD)
	private String expiredDate;

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(String expiredDate) {
		this.expiredDate = expiredDate;
	}
	
	
}
