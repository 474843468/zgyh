package com.chinamworld.bocmbci.biz.crcd;


public class CrcdDataCenter {
	private static CrcdDataCenter dataCenter;
	
	
	
	public static CrcdDataCenter getInstance() {
		if (dataCenter == null) {
			dataCenter = new CrcdDataCenter();
		}
		return dataCenter;
	}
	
	
	
}
