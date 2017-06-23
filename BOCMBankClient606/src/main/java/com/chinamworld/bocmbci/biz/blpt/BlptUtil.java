package com.chinamworld.bocmbci.biz.blpt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;

import com.chinamworld.bocmbci.bii.constant.Blpt;
import com.chinamworld.bocmbci.utils.StringUtil;

public class BlptUtil {

	private static BlptUtil instance;
	/** 特定activity管理 */
	private ArrayList<Activity> acList;
	/** 申请服务 */
	private Map<String, Object> applyLastPagMap;
	/** 缴费 */
	private Map<String, Object> payLastPagMap;
	/** 撤销缴费 */
	private Map<String, Object> cancePayLastPagMap;
	/** 省份列表 **/
	private List<Map<String, Object>> provList;
	/** 城市列表 **/
	private List<Map<String, Object>> cityList;
	/** 缴费上送信息临时存储 */
	private Map<String, String> mapData;
	/** 随机数临时存储 */
	private String randomNumber;
	/** 临时存储提价需要上传数据 */
	private List<Map<String, Object>> acctList;
	private List<Map<String, Object>> exeList;
	private List<Map<String, Object>> spList;
	/** 布局数据临存储 */
	private Map<String, String> layoutMap;
	
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	private String userCode;

	public List<Map<String, Object>> getProvList() {
		return provList;
	}

	public void setProvList(List<Map<String, Object>> provList) {
		this.provList = provList;
	}

	public List<Map<String, Object>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, Object>> cityList) {
		this.cityList = cityList;
	}

	public Map<String, Object> getApplyLastPagMap() {
		return applyLastPagMap;
	}

	public void setApplyLastPagMap(Map<String, Object> applyLastPagMap) {
		this.applyLastPagMap = applyLastPagMap;
	}

	public Map<String, Object> getPayLastPagMap() {
		return payLastPagMap;
	}

	public void setPayLastPagMap(Map<String, Object> payLastPagMap) {
		this.payLastPagMap = payLastPagMap;
	}

	public Map<String, Object> getCancePayLastPagMap() {
		return cancePayLastPagMap;
	}

	public void setCancePayLastPagMap(Map<String, Object> cancePayLastPagMap) {
		this.cancePayLastPagMap = cancePayLastPagMap;
	}

	public Map<String, String> getMapData() {
		return mapData;
	}

	public void setMapData(Map<String, String> mapData) {
		this.mapData = mapData;
	}

	public String getRandomNumber() {
		return randomNumber;
	}

	public void setRandomNumber(String randomNumber) {
		this.randomNumber = randomNumber;
	}

	public List<Map<String, Object>> getAcctList() {
		return acctList;
	}

	public void setAcctList(List<Map<String, Object>> acctList) {
		this.acctList = acctList;
	}

	public List<Map<String, Object>> getExeList() {
		return exeList;
	}

	public void setExeList(List<Map<String, Object>> exeList) {
		this.exeList = exeList;
	}

	public List<Map<String, Object>> getSpList() {
		return spList;
	}

	public void setSpList(List<Map<String, Object>> spList) {
		this.spList = spList;
	}

	public Map<String, String> getLayoutMap() {
		return layoutMap;
	}

	public void setLayoutMap(Map<String, String> layoutMap) {
		this.layoutMap = layoutMap;
	}

	/*** 缴费省份 ***/
	public static final String[] PROVINCE_ZH_NAME = { "安徽", "北京", "重庆", "福建",
			"广东", "甘肃", "广西", "贵州", "河南", "湖北", "河北", "海南", "香港", "黑龙江", "湖南",
			"吉林", "江苏", "江西", "辽宁", "澳门", "内蒙古", "宁夏", "青海", "四川", "山东",
			"上海", "陕西", "山西", "天津", "台湾", "新疆", "西藏", "云南", "浙江", "深圳" };

	public static final String[] PROVINCE_SHORT_NAME = { "AH", "BJ", "CQ",
			"FJ", "GD", "GS", "GX", "GZ", "HA", "HB", "HE", "HI", "HK", "HL",
			"HN", "JL", "JS", "JX", "LN", "MO", "NM", "NX", "QH", "SC", "SD",
			"SH", "SN", "SX", "TJ", "TW", "XJ", "XZ", "YN", "JZ", "SZ" };

	public static BlptUtil getInstance() {
		if (instance == null) {
			instance = new BlptUtil();
		}
		return instance;
	}

	/**
	 * String转int
	 * 
	 * @param str
	 * @return
	 */
	public int stringToint(String str) {
		int mInt = 0;
		if (!StringUtil.isNullOrEmpty(str)) {
			mInt = Integer.parseInt(str);
		}
		return mInt;
	}

	/**
	 * 将定位之后的省中文转简称
	 * 
	 * @param province
	 * @return
	 */
	public String provZhNameToShortName(String provZhName) {
		String proShortName = "";
		if (!StringUtil.isNullOrEmpty(provZhName)) {
			for (int i = 0; i < PROVINCE_ZH_NAME.length; i++) {
				if (provZhName.contains(PROVINCE_ZH_NAME[i])) {
					proShortName = PROVINCE_SHORT_NAME[i];
				}
			}
		}
		return proShortName;
	}

	public void addActivity(Activity ac) {
		if (acList == null) {
			acList = new ArrayList<Activity>();
		}
		if (!acList.contains(ac)) {
			acList.add(ac);
		}
	}

	public void finshActivity() {
		if (acList != null && acList.size() != 0) {
			for (int i = 0; i < acList.size(); i++) {
				acList.get(i).finish();
			}
		}
	}

	/**
	 * 缴费项目信息存储
	 * 
	 * @param descriptionName
	 * @param masterDispName
	 * @param city
	 * @param merchantId
	 * @param payjNum
	 * @param prvcShortName
	 */
	public void bimsgSave(Context mContext, String descriptionName,
			String payeeDispName, String city, String merchantId,
			String payjNum, String prvcShortName) {
		Map<String, String> map = null;
		if (map == null) {
			map = new HashMap<String, String>();
		}
		map.put(Blpt.KEY_DSNAME, descriptionName);
		map.put(Blpt.KEY_PAYEENAME, payeeDispName);
		map.put(Blpt.KEY_CITY, city);
		map.put(Blpt.KEY_MERCHID, merchantId);
		map.put(Blpt.KEY_PAYJNUM, payjNum);
		map.put(Blpt.KEY_PROVICESHORTNAME, prvcShortName);
		setMapData(map);
	}
	
	/**
	 * 处理返回数据
	 * 
	 * @param result
	 * @return
	 */
//	public Object httpResponseDeal(Object result) {
//		BiiResponse biiResponse = (BiiResponse) result;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		return biiResponseBody.getResult();
//	}
	
	/**
	 * 保存usercode
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public static void saveUserCode(List<Map<String, Object>> list){
		if (StringUtil.isNullOrEmpty(list)) {
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).containsKey(Blpt.SIGN_PAY_EXELEFT_RE)) {
				Map<String, Object> map = (Map<String, Object>) list.get(i).get(Blpt.SIGN_PAY_EXELEFT_RE);
				if (StringUtil.isNull((String)map.get(Blpt.EXE_DATA)) && 
						map.get(Blpt.EXE_DATA).equals(Blpt.SIGN_PAY_USERCODE) &&
						!StringUtil.isNull((String)map.get(Blpt.SIGN_PAY_VALUE))) {
					instance.setUserCode((String)map.get(Blpt.SIGN_PAY_VALUE));
				}
			}else if(list.get(i).containsKey(Blpt.SIGN_PAY_EXERIGHT_RE)){
				Map<String, Object> map = (Map<String, Object>) list.get(i).get(Blpt.SIGN_PAY_EXELEFT_RE);
				if (StringUtil.isNull((String)map.get(Blpt.EXE_DATA)) && 
						map.get(Blpt.EXE_DATA).equals(Blpt.SIGN_PAY_USERCODE) &&
						!StringUtil.isNull((String)map.get(Blpt.SIGN_PAY_VALUE))) {
					instance.setUserCode((String)map.get(Blpt.SIGN_PAY_VALUE));
				}
			}else{
				if (StringUtil.isNull((String)list.get(i).get(Blpt.EXE_DATA)) && 
						list.get(i).get(Blpt.EXE_DATA).equals(Blpt.SIGN_PAY_USERCODE) &&
						!StringUtil.isNull((String)list.get(i).get(Blpt.SIGN_PAY_VALUE))) {
					instance.setUserCode((String)list.get(i).get(Blpt.SIGN_PAY_VALUE));
				}
			}
		}
	}
	
	/**
	 * 必传字段上传
	 * @param list
	 * @param params
	 * @param dataName
	 * @param value
	 */
	@SuppressWarnings("unchecked")
	public static void setDataName(List<Map<String, Object>> list,Map<String, Object> params,String dataName,String value){
		if (StringUtil.isNullOrEmpty(list)) {
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).containsKey(Blpt.SIGN_PAY_EXELEFT_RE)) {
				Map<String, Object> map = (Map<String, Object>) list.get(i).get(Blpt.SIGN_PAY_EXELEFT_RE);
				if (!StringUtil.isNull((String)map.get(Blpt.EXE_DATA)) && map.get(Blpt.EXE_DATA).equals(dataName)) {
					if(StringUtil.isNull((String)map.get(Blpt.SIGN_PAY_VALUE))){
						params.put(dataName, value);
					}
				}else{
					params.put(dataName, value);
				}
			}else if(list.get(i).containsKey(Blpt.SIGN_PAY_EXERIGHT_RE)){
				Map<String, Object> map = (Map<String, Object>) list.get(i).get(Blpt.SIGN_PAY_EXERIGHT_RE);
				if (!StringUtil.isNull((String)map.get(Blpt.EXE_DATA)) && map.get(Blpt.EXE_DATA).equals(dataName)) {
					if(StringUtil.isNull((String)map.get(Blpt.SIGN_PAY_VALUE))){
						params.put(dataName, value);
					}
				}else{
					params.put(dataName, value);
				}
			}else{
				if (!StringUtil.isNull((String)list.get(i).get(Blpt.EXE_DATA)) && list.get(i).get(Blpt.EXE_DATA).equals(dataName)) {
					if(StringUtil.isNull((String)list.get(i).get(Blpt.SIGN_PAY_VALUE))){
						params.put(dataName, value);
					}
				}else{
					params.put(dataName, value);
				}
			}
		}
	}

	/** 清除所有临时存储数据 **/
	public void clearAllData() {
		if (!StringUtil.isNullOrEmpty(acList)) {
			acList.clear();
		}
		if (!StringUtil.isNullOrEmpty(applyLastPagMap)) {
			applyLastPagMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(payLastPagMap)) {
			payLastPagMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(cancePayLastPagMap)) {
			cancePayLastPagMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(provList)) {
			provList.clear();
		}
		if (!StringUtil.isNullOrEmpty(cityList)) {
			cityList.clear();
		}
		if (!StringUtil.isNullOrEmpty(mapData)) {
			mapData.clear();
		}
		if (!StringUtil.isNullOrEmpty(acctList)) {
			acctList.clear();
		}
		if (!StringUtil.isNullOrEmpty(exeList)) {
			exeList.clear();
		}
		if (!StringUtil.isNullOrEmpty(spList)) {
			spList.clear();
		}
		if (!StringUtil.isNullOrEmpty(layoutMap)) {
			layoutMap.clear();
		}
	}
}
