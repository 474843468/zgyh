package com.chinamworld.bocmbci.biz.quickOpen;

import java.util.List;
import java.util.Map;

import com.chinamworld.bocmbci.utils.StringUtil;

public class QuickOpenDataCenter {
	
	private static QuickOpenDataCenter instance;
	/** 查询中行账户列表返回数据 */
	private List<Map<String, Object>> listCommonQueryAllChinaBankAccount;
	/** 查询客户信息返回数据 */
	private Map<String, Object> mapStockThirdQueryCustInfoExtend;
	/** 中银证券开户预交易返回数据 */
	private Map<String, Object> mapStockThirdQuickOpenPre;

	/** 获取中银证券开户预交易返回数据 */
	public Map<String, Object> getMapStockThirdQuickOpenPre() {
		return mapStockThirdQuickOpenPre;
	}
	/** 设置中银证券开户预交易返回数据 */
	public void setMapStockThirdQuickOpenPre(Map<String, Object> mapStockThirdQuickOpenPre) {
		this.mapStockThirdQuickOpenPre = mapStockThirdQuickOpenPre;
	}
	
	/** 获取查询客户信息返回数据 */
	public Map<String, Object> getMapStockThirdQueryCustInfoExtend() {
		return mapStockThirdQueryCustInfoExtend;
	}
	/** 设置查询客户信息返回数据 */
	public void setMapStockThirdQueryCustInfoExtend(Map<String, Object> mapStockThirdQueryCustInfoExtend) {
		this.mapStockThirdQueryCustInfoExtend = mapStockThirdQueryCustInfoExtend;
	}
	/** 获取查询中行账户列表返回数据 */
	public List<Map<String, Object>> getListCommonQueryAllChinaBankAccount() {
		return listCommonQueryAllChinaBankAccount;
	}
	/** 设置查询中行账户列表返回数据 */
	public void setListCommonQueryAllChinaBankAccount(List<Map<String, Object>> listCommonQueryAllChinaBankAccount) {
		this.listCommonQueryAllChinaBankAccount = listCommonQueryAllChinaBankAccount;
	}

	public static QuickOpenDataCenter getInstance(){
		if (instance == null) {
			instance = new QuickOpenDataCenter();
		}
		return instance;
	}
	
	public void clearAllData(){
		if (!StringUtil.isNullOrEmpty(listCommonQueryAllChinaBankAccount)) {
			listCommonQueryAllChinaBankAccount.clear();
		}
		if (!StringUtil.isNullOrEmpty(mapStockThirdQueryCustInfoExtend)) {
			mapStockThirdQueryCustInfoExtend.clear();
		}
		if (!StringUtil.isNullOrEmpty(mapStockThirdQuickOpenPre)) {
			mapStockThirdQuickOpenPre.clear();
		}
	}
}
