package com.chinamworld.bocmbci.base.application;

/**
 * 公共数据存储中心
 * 一些全局需要使用的数据，均可以存储于此类中。尽量不要直接向BaseDroidApp中存储数据，保持程序的解耦
 * 
 * @author yuht
 *
 */
public class PublicDataCenter {

	private PublicDataCenter(){
		
	}
	  
	private static PublicDataCenter instance;
	public static PublicDataCenter getInstance(){
		if(instance == null)
			instance = new PublicDataCenter();
		return instance;
	}
	
	/**
	 * 版本更新，获得的最新版本
	 */
	public String lastVersion;
	
}
