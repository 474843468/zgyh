package com.chinamworld.bocmbci.biz.branchorder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Order;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class BranchOrderUtils {
	protected static SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");
	/**
	 * 处理返回数据
	 * 
	 * @param result
	 * @return
	 */
//	public static Object httpResponseDeal(Object result) {
//		BiiResponse biiResponse = (BiiResponse) result;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		return biiResponseBody.getResult();
//	}
//	
	/**
	 * 循环遍历设置浮动框
	 * @param textViews控件列表
	 */
	public static void setOnShowAllTextListener(Context context, TextView... textViews){
		if(textViews == null || textViews.length <= 0)
			return;
		for(TextView tv : textViews){
			if(tv != null)
				PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
						tv);
		}
	}
	
	/**
	 * 初始化下拉框
	 * @param c
	 * @param sp
	 * @param list
	 */
	public static List<String> initSpinnerView(Context c,Spinner sp,Map<String, Object> map) {
		if(StringUtil.isNullOrEmpty(map))
			return null;
		List<String> list = getMapValueList(map, false);
		boolean isdefault = true;
		if (StringUtil.isNullOrEmpty(list)) {
			list = new ArrayList<String>();
			list.add("");
			isdefault = false;
		}
		setSpinnerBackground(sp, isdefault);
		initSpinnerView(c, sp, list);
		return list;
	}
	
	/**
	 * 初始化下拉框
	 * @param c
	 * @param sp
	 * @param list
	 * 603新来业务确认预约事项按接口返回顺序前端不排序
	 */
	public static List<String> initServiceSpinnerView(Context c,Spinner sp,Map<String, Object> map) {
		if(StringUtil.isNullOrEmpty(map))
			return null;
		List<String> list = getArrayListWithValue(map, false);
		boolean isdefault = true;
		if (StringUtil.isNullOrEmpty(list)) {
			list = new ArrayList<String>();
			list.add("");
			isdefault = false;
		}
		setSpinnerBackground(sp, isdefault);
		initSpinnerView(c, sp, list);
		return list;
	}
	
	/**
	 * 获取Map字典的List的值列表或Key列表
	 * @param map原字典
	 * @param isKey 是否是获取key值列表
	 * @return
	 */
	public static List<String> getArrayListWithValue(Map<String, Object> map, boolean isSort){
		ArrayList<String> list = new ArrayList<String>();
		Iterator<Entry<String, Object>> iter = map.entrySet().iterator();
		Entry<String, Object> entry;
		while(iter.hasNext()){
			entry = iter.next();
			list.add((String)entry.getValue());
		}
		return isSort ? sortByPinYin(list) : list;
	}
	
	/**
	 * 初始化城市列表
	 * @param c
	 * @param sp
	 * @param list
	 * @param value 初始值
	 * @param isSet 是否进行初始化
	 */
	public static List<String> initCityList(Context c,Spinner sp,Map<String, Object> map, 
			String value, boolean isSet) {
		if(StringUtil.isNullOrEmpty(map))
			return null;
		List<String> list = getMapValueList(map, false);
		boolean isdefault = true;
		if (StringUtil.isNullOrEmpty(list)) {
			list = new ArrayList<String>();
			list.add("");
			isdefault = false;
		}
		setSpinnerBackground(sp, isdefault);
		initSpinnerView(c, sp, list);
		int index = getIndex(list, value, isSet);
		if(index > 0)
			sp.setSelection(index);
		return list;
	}
	
	/**
	 * 获取当前值索引
	 * @param list
	 * @param value
	 * @param locationSu
	 * @return
	 */
	public static int getIndex(List<String> list, String value, boolean locationSu){
		if(StringUtil.isNullOrEmpty(list))
			return -1;
		if(locationSu){
			for(String string : list){
				if(string.contains(value))
					return list.indexOf(string);
			}
		}
		return -1;	
	}
	
	public static List<String> initSpinnerView(Context c, Spinner sp, List<String> list){
		ArrayAdapter<ArrayList<String>> mAdapter = new ArrayAdapter(c,
				 R.layout.spinner_item,list);
		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(mAdapter);
		return list;
	}
	
	/**
	 * 设置sipnner背景
	 * @param v
	 * @param isdefault
	 */
	public static void setSpinnerBackground(View v,boolean isdefault){
		if (v != null) {
			if (isdefault) {
				v.setClickable(true);
				v.setBackgroundResource(R.drawable.bg_spinner);
			}else{
				v.setClickable(false);
				v.setBackgroundResource(R.drawable.bg_spinner_default);
			}
		}
	}
	
	/**
	 * 获取Map字典的List的值列表或Key列表
	 * @param map原字典
	 * @param isKey 是否是获取key值列表
	 * @return
	 */
	public static List<String> getMapValueList(Map<String, Object> map, boolean isKey){
		List<String> list = new ArrayList<String>();
		Iterator<Entry<String, Object>> iter = map.entrySet().iterator();
		Entry<String, Object> entry;
		while(iter.hasNext()){
			entry = iter.next();
			if(isKey){
				list.add(entry.getKey());
			}else{
				list.add((String)entry.getValue());
			}
		}
		return list;
	}
	
	public static String getKeyByValue(Map<String, Object> map, String value){
		if(StringUtil.isNullOrEmpty(map))
			return "";
		Set set = map.entrySet();
		Iterator iterator = set.iterator();
		while(iterator.hasNext()){
			Map.Entry<String, Object> enter = (Map.Entry<String, Object>)iterator.next();
			if(isStrEquals(value, (String)enter.getValue()))
				return enter.getKey();
		}
		return "";
	}
	
	/**
	 * 判斷兩個字符串是否一样
	 * @param orig 要判断的字符串
	 * @param newStr 目的串
	 * @return true:一样，false:不一样
	 */
	public static boolean isStrEquals(String orig, String newStr){
		if(orig == null || newStr == null || orig.equals("") || newStr.equals(""))
			return false;
		if(!orig.equals(newStr))
			return false;
		return true;
	}
	
	/**
	 * 根据字典列表得到相应的字符串列表
	 * @param listMap 原字典列表
	 * @param key 目标字段
	 * @param isFilterDupl 是否需要根据某字段去重
	 * @param DuplKey 去重字段名
	 * @return
	 */
	public static List<String> getListFromListMapByKey(List<Map<String, Object>> listMap, String key, 
			boolean isFilterDupl){
			if(StringUtil.isNullOrEmpty(listMap) || StringUtil.isNullOrEmpty(key))
				return new ArrayList<String>();
			else
				return getListFromListMap(listMap, key, isFilterDupl);
	}
	
	/**
	 * 从ListMap中获取对应字段的list
	 * @param listMap
	 * @param key
	 * @param isFilterDupl
	 * @return
	 */
	public static List<String> getListFromListMap(List<Map<String, Object>> listMap, String key, 
			boolean isFilterDupl){
		List<String> list = new ArrayList<String>();
		for(Map<String, Object> map : listMap){
			String str = (String)map.get(key);
			if(isFilterDupl){
				if(!StringUtil.isNull(str) && !list.contains(str))
					list.add(str);
			}else{
				if(!StringUtil.isNull(str))
					list.add(str);
			}
		}
		return list;

	}
	
	/**
	 * 从ListMap中获取特殊业务的可预约日期列表
	 * @param listMap
	 * @param key
	 * @param isFilterDupl
	 * @return
	 */
	public static List<String> getSpecialOrderTimeList(List<Map<String, Object>> listMap, String key, 
			boolean isFilterDupl,String currentDate){
		List<String> list = new ArrayList<String>();
		for(Map<String, Object> map : listMap){
			String str = (String)map.get(key);
			if(isFilterDupl){
				if(!StringUtil.isNull(str) && !list.contains(str)){
					boolean openFlag = StringUtil.parseStrToBoolean((String)map.get(Order.OPENFLAG));
					boolean bookFlag = StringUtil.parseStrToBoolean((String)map.get(Order.BOOKFLAG));
					if(openFlag && bookFlag){
						if(!BranchOrderUtils.isStrEquals(currentDate,str)){
							list.add(str);
						}
					}
				}
			}else{
				if(!StringUtil.isNull(str)){
					boolean openFlag = StringUtil.parseStrToBoolean((String)map.get(Order.OPENFLAG));
					boolean bookFlag = StringUtil.parseStrToBoolean((String)map.get(Order.BOOKFLAG));
					if(openFlag && bookFlag)
						list.add(str);
				}
			}
		}
		return sortByDate(list);
		
	}
	
	/**
	 * 根据选择日期动态更换当日可预约时段  列表
	 * @param listMap 网点普通业务列表
	 * @param dateKey 普通业务办理日期key
	 * @param date 当前所选预约日期
	 * @param peroidKey 时段Key
	 * @param isFilterDupl 是否过滤
	 * @return
	 */
	public static List<Map<String, Object>> getListPeroidByDateAndService(List<Map<String, Object>> listMap, String dateKey, 
			String date, String peroidKey,String serviceId){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> map : listMap){
			String str = (String)map.get(dateKey);
			String service = (String)map.get(Order.BSID);
			if(BranchOrderUtils.isStrEquals(date, str) && BranchOrderUtils.isStrEquals(serviceId, service))
				list = (List<Map<String, Object>>)map.get(peroidKey);
		}
		return list;
	}
	
	
	public static Map<String, Object> getMapFromMapList(List<Map<String, Object>> listMap, 
			String key, String valueKey){
		Map<String, Object> resultMap= new LinkedHashMap<String, Object>();
		if(StringUtil.isNullOrEmpty(listMap) || StringUtil.isNull(key) || StringUtil.isNull(valueKey))
			return resultMap;
		for(Map<String, Object> map : listMap){
			if(map != null && map.containsKey(key) && map.containsKey(valueKey)){
				resultMap.put((String)map.get(key), map.get(valueKey));
			}
		}
		return resultMap;
	}
	
	/**
	 * 远程取号过滤业务
	 */
	public static Map<String, Object> getMapFromMapListByDate(List<Map<String, Object>> listMap, 
			String key, String valueKey, String dateTime){
		Map<String, Object> resultMap= new LinkedHashMap<String, Object>();
		if(StringUtil.isNullOrEmpty(listMap) || StringUtil.isNull(key) || StringUtil.isNull(valueKey))
			return resultMap;
		for(Map<String, Object> map : listMap){
			if(map != null && map.containsKey(key) && map.containsKey(valueKey)){
				String dt = (String)map.get(Order.BSDATE);
				if(isStrEquals(dateTime, dt))
					resultMap.put((String)map.get(key), map.get(valueKey));
			}
		}
		return resultMap;
	}
	/**
	 * 使用汉字拼音排序从 A-Z
	 * @param hanziList
	 * @return
	 */
	public static List<String> sortByPinYin(List<String> hanziList){
		List<String> result = new ArrayList<String>();
		try{
			for(String string : hanziList){
				String newPy = PinyinConv.cn2py(string);
				if(result.size() == 0) result.add(string);
				else{
					boolean isAdd = false;
					for(int i = 0; i < result.size(); i ++){
						String iterPy = PinyinConv.cn2py(result.get(i));
						if(newPy.compareTo(iterPy) < 0){
							isAdd = true;
							result.add(i, string);
							break;
						}
					}
					if(!isAdd)
						result.add(string);
				}
			}
		}catch(Exception e){
			LogGloble.e("sortByPinYin", e.toString());
		}
		return result;
	}
	
	/**
	 * 对日期进行排序  由近及远
	 * @param hanziList
	 * @return
	 */
	public static List<String> sortByDate(List<String> dateList){
		List<String> result = new ArrayList<String>();
		try{
			for(String string : dateList){
				if(result.size() == 0) result.add(string);
				else{
					boolean isAdd = false;
					for(int i = 0; i < result.size(); i ++){
						if(string.compareTo(result.get(i)) < 0){
							isAdd = true;
							result.add(i, string);
							break;
						}
					}
					if(!isAdd)
						result.add(string);
				}
			}
		}catch(Exception e){
			LogGloble.e("sortByDate", e.toString());
		}
		return result;
	}	
	/**
	 * add by niuchf
	 * 判断当前时间时候在预约时段内
	 * 预约时段 startTime-endTime
	 **/
	public static boolean compareStartDateToEndDate(String startTime,String endTime, String dateTime) {
		Date startdate = null;
		Date enddate = null;
		Date currentdate = null;
		try {
			startdate = sdf3.parse(startTime);
			enddate = sdf3.parse(endTime);
			currentdate = sdf3.parse(dateTime);
			if (startdate.getTime() <=currentdate.getTime()) {
				return true;
			}else {
				return false;
			}
		} catch (ParseException e) {
			LogGloble.exceptionPrint(e);
		}
		
		return false;
	}
	/**
	 * add by niuchf
	 * 当前预约时段没有值  预约日期过滤当天日期
	 * @param orderTimeList
	 * @param currentDate
	 * @return
	 */
	public static List<String> filterCurrentDate(List<String> orderTimeList,String currentDate){
		List<String> list = new ArrayList<String>();
		for(String str : orderTimeList){
				if(!StringUtil.isNull(str) && !list.contains(str)){
						if(!BranchOrderUtils.isStrEquals(currentDate,str)){
							list.add(str);
						}
			}
		}
		return sortByDate(list);		
	}
}
