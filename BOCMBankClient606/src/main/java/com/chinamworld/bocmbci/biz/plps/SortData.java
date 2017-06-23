package com.chinamworld.bocmbci.biz.plps;

public class SortData {
	private String name;   //显示的数据
	private String sortLetters;  //显示数据拼音的首字母
	/**城市多语言代码*/
	private String disPlayNo;
	
	public String getDisPlayNo() {
		return disPlayNo;
	}
	public void setDisPlayNo(String disPlayNo) {
		this.disPlayNo = disPlayNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
}
