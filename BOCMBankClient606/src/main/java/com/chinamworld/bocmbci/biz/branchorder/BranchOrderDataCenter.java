package com.chinamworld.bocmbci.biz.branchorder;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.chinamworld.bocmbci.utils.StringUtil;


public class BranchOrderDataCenter {
	
	private static BranchOrderDataCenter instance;
	/** 省份   */
	private List<Map<String, Object>> proviceList;
	/** 城市  **/
	private List<Map<String, Object>> cityList;
	/** 区   **/
	private List<Map<String, Object>> areaList;
	/** 网点列表  **/
	private List<Map<String, Object>> orderList;
	/** 网点列表项   **/
	private Map<String, Object> orderListItem;
	/** 网点详细信息  **/
	private Map<String, Object> orderDetail;
	/** 网点预约人数查询 **/
	private Map<String, Object> orderApplyQueryMap;
	/** 网点当前排队信息查询 **/
	private Map<String, Object> orderInfoQueryMap;
	/** 远程取号 **/
	private Map<String, Object> orderTakeOnMap;
	/** 预约排队 **/
	private Map<String, Object> orderApplyMap;
	/** 非中行用户  中行用户  客户类型  **/
	public final static int NON_CHINA_BANK_CUSTOMER = 1;
	public final static int CHINA_BANK_CUSTOMER = 2;
	public static int customerType = CHINA_BANK_CUSTOMER;
	public final static int UN_VIP = 101;
	public final static int VIP = 102;
	
	/** 客户录入信息*/
	private Map<String, String> customerInfo = new HashMap<String, String>();
	/** 客户等级  是否X级以上*/
	public static boolean isXGrade = false;
	/** 特殊业务标志*/
	public static boolean specialServiceFlag = false;
	/** 是否进行提前提醒*/
	public static boolean isTipChecked = true;
	
	public static boolean isFromMain ;
	public static boolean isFromMyAround ;
	/** 是否同时选择了普通业务和特殊业务*/
	public static boolean isSelectCommomAndSpecial = false;
	
	public static BranchOrderDataCenter getInstance() {
		if (instance == null) {
			instance = new BranchOrderDataCenter();
		}
		return instance;
	}

	public List<Map<String, Object>> getProviceList() {
		return proviceList;
	}

	public void setProviceList(List<Map<String, Object>> proviceList) {
		this.proviceList = proviceList;
	}

	public List<Map<String, Object>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, Object>> cityList) {
		this.cityList = cityList;
	}

	public List<Map<String, Object>> getAreaList() {
		return areaList;
	}

	public void setAreaList(List<Map<String, Object>> areaList) {
		this.areaList = areaList;
	}

	public List<Map<String, Object>> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<Map<String, Object>> orderList) {
		this.orderList = orderList;
	}
	

	public Map<String, Object> getOrderListItem() {
		return orderListItem;
	}

	public void setOrderListItem(Map<String, Object> orderListItem) {
		this.orderListItem = orderListItem;
	}

	public Map<String, Object> getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(Map<String, Object> orderDetail) {
		this.orderDetail = orderDetail;
	}
	
	
	

	public Map<String, Object> getOrderApplyQueryMap() {
		return orderApplyQueryMap;
	}

	public void setOrderApplyQueryMap(Map<String, Object> orderApplyQueryMap) {
		this.orderApplyQueryMap = orderApplyQueryMap;
	}

	public Map<String, Object> getOrderInfoQueryMap() {
		return orderInfoQueryMap;
	}

	public void setOrderInfoQueryMap(Map<String, Object> orderInfoQueryMap) {
		this.orderInfoQueryMap = orderInfoQueryMap;
	}

	public Map<String, String> getCustomerInfo() {
		return customerInfo;
	}
	

	public Map<String, Object> getOrderTakeOnMap() {
		return orderTakeOnMap;
	}

	public void setOrderTakeOnMap(Map<String, Object> orderTakeOnMap) {
		this.orderTakeOnMap = orderTakeOnMap;
	}

	public Map<String, Object> getOrderApplyMap() {
		return orderApplyMap;
	}

	public void setOrderApplyMap(Map<String, Object> orderApplyMap) {
		this.orderApplyMap = orderApplyMap;
	}

	public void setCustomerInfo(String key, String value) {
		if(customerInfo == null)
			customerInfo = new HashMap<String, String>();
		customerInfo.put(key, value);
	}
	
	public String getCustomerInfo(String key){
		if(customerInfo == null || StringUtil.isNull(key))
			return "-";
		return customerInfo.get(key);
	}


	public static final Map<String, String> areaMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
	};

	public void clearAllData() {
		if (!StringUtil.isNullOrEmpty(proviceList)) {
			proviceList.clear();
		}
		if (!StringUtil.isNullOrEmpty(cityList)) {
			cityList.clear();
		}
		if (!StringUtil.isNullOrEmpty(areaList)) {
			areaList.clear();
		}
		if (!StringUtil.isNullOrEmpty(orderList)) {
			orderList.clear();
		}
		if (!StringUtil.isNullOrEmpty(orderListItem)) {
			orderListItem.clear();
		}
		if (!StringUtil.isNullOrEmpty(orderDetail)) {
			orderDetail.clear();
		}
		if (!StringUtil.isNullOrEmpty(orderApplyQueryMap)) {
			orderApplyQueryMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(orderInfoQueryMap)) {
			orderInfoQueryMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(orderTakeOnMap)) {
			orderTakeOnMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(orderApplyMap)) {
			orderApplyMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(customerInfo)) {
			customerInfo.clear();
		}
		if(specialServiceFlag){
			specialServiceFlag = false;
		}
		if(isXGrade){
			isXGrade = false;
		}
		if(!isTipChecked){
			isTipChecked = true;
		}
		if(!isFromMyAround){
			isFromMyAround = false;
		}
		if(isSelectCommomAndSpecial){
			isSelectCommomAndSpecial = false;
		}
	}
	
	public final static Map<String, Object> cardType = new LinkedHashMap<String, Object>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("01", "身份证");
			put("02", "借记卡");
			put("03", "信用卡");
			put("04", "存折");
			put("52", "临时身份证");
			put("53", "护照");
			put("54", "户口薄");
			put("55", "军人身份证");
			put("56", "武装警察身份证");
			put("57", "港澳台居民来往内地通行证");
			put("58", "外交人员身份证");
			put("59", "外国人居留许可证");
			put("60", "边民出入境通行证");
			put("47", "港澳居民来往内地通行证（香港）");
			put("48", "港澳居民来往内地通行证（澳门）");
			put("49", "台湾居民来往大陆通行证");
			put("61", "其他");
		}
	};
	
	public final static Map<String, Object> cardTypeFzh = new LinkedHashMap<String, Object>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		{
			put("01", "身份证");
//			put("02", "借记卡");
//			put("03", "信用卡");
//			put("04", "存折");
			put("52", "临时身份证");
			put("53", "护照");
			put("54", "户口薄");
			put("55", "军人身份证");
			put("56", "武装警察身份证");
			put("57", "港澳台居民来往内地通行证");
			put("58", "外交人员身份证");
			put("59", "外国人居留许可证");
			put("60", "边民出入境通行证");
			put("47", "港澳居民来往内地通行证（香港）");
			put("48", "港澳居民来往内地通行证（澳门）");
			put("49", "台湾居民来往大陆通行证");
			put("61", "其他");
		}
	};
	
	public final static Map<String, Object> Currency = new LinkedHashMap<String, Object>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
//			put("012", "英镑");
//			put("013", "港元");
//			put("014", "美元");
//			put("015", "瑞士法郎");
//			put("018", "新加坡元");
//			put("021", "瑞典克朗");
//			put("022", "丹麦克朗");
//			put("023", "挪威克朗");
//			put("027", "日元");
//			put("028", "加拿大元");
//			put("029", "澳大利亚元");
//			put("038", "欧元");
//			put("081", "澳门元");
//			put("087", "新西兰元");
			put("GBP", "英镑");
			put("HKD", "港币");
			put("USD", "美元");
			put("CHF", "瑞士法郎");
			put("SGD", "新加坡元");
			put("SEK", "瑞典克朗");
			put("DKK", "丹麦克朗");
			put("NOK", "挪威克朗");
			put("JPY", "日元");
			put("CAD", "加拿大元");
			put("AUD", "澳大利亚元");
			put("EUR", "欧元");
			put("MOP", "澳门元");
			put("NZD", "新西兰元");
		}
	};
	
}
