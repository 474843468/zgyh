package com.chinamworld.bocmbci.biz.epay.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

public class EpayUtil {

	/**
	 * 根据列表过滤已存在账户
	 * 
	 * @param acclist
	 * @param acclistPattern
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> filterAccList(List<Object> acclist, List<Object> acclistPattern) {
		for (int i = 0; i < acclistPattern.size(); i++) {
			Map<Object, Object> map = EpayUtil.getMap(acclistPattern.get(i));
			String accountId = getString(map.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_ID), "");
			if (StringUtil.isNullOrEmpty(accountId))
				continue;
			for (int j = 0; j < acclist.size(); j++) {
				Map<Object, Object> mapPattern = EpayUtil.getMap(acclist.get(j));
				String accountIdPattern = getString(
						mapPattern.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_ID), "");
				if (StringUtil.isNullOrEmpty(accountIdPattern))
					continue;
				if (accountId.equals(accountIdPattern)) {
					acclist.remove(j);
				}
			}
		}

		return acclist;
	}

	/**
	 * 根据账户ID过滤已存在账户
	 * 
	 * @param acclist
	 * @param accIdPattern
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> filterAccList(List<Object> acclist, String accIdPattern) {
		if (!StringUtil.isNullOrEmpty(accIdPattern)) {
			for (int i = 0; i < acclist.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) acclist.get(i);
				String accountId = getString(map.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_ID), "");
				if (StringUtil.isNullOrEmpty(accountId))
					continue;
				if (accountId.equals(accIdPattern)) {
					acclist.remove(i);
					i = 0;
				}
			}
		}
		return acclist;
	}

	/**
	 * 根据yyyy/MM/dd字符串获取当前日期
	 * 
	 * @param dateStr
	 * @return
	 */
	public static int[] getDateInt(String dateStr) {
		int[] dateInt = new int[3];
		String[] dateStrs = dateStr.split("/");
		dateInt[0] = Integer.valueOf(dateStrs[0]);
		dateInt[1] = Integer.valueOf(dateStrs[1]);
		dateInt[2] = Integer.valueOf(dateStrs[2]);

		return dateInt;
	}

	/**
	 * 获取yyyy/MM/dd日期字符串
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static String getDateStr(int year, int month, int day) {
		StringBuffer sb = new StringBuffer();
		sb.append(year).append("/");
		if (month > 10) {
			sb.append(month).append("/");
		} else {
			sb.append("0").append(month).append("/");
		}

		if (day > 10) {
			sb.append(day);
		} else {
			sb.append("0").append(day);
		}
		return sb.toString();
	}

	/**
	 * 获取日期字符串,获取天数（包含当天）
	 * 
	 * @param date
	 *            日期
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static String getDateStr(Date date, int year, int month, int day) {
		if (null == date)
			return null;
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String dateStr = sdf.format(date);
		if (0 == year && 0 == month && 0 == day) {
			dateStr = sdf.format(date);
		} else {
			int[] dateInt = getDateInt(dateStr);
			if (month <= 0) {
				month -= 1;
			} else {
				month += 1;
			}
			// calendar.set(dateInt[0] + year, dateInt[1] + month, dateInt[2] +
			// day);
			// 修改包含当天
			if (day == 0) {
				calendar.set(dateInt[0] + year, dateInt[1] + month, dateInt[2]);
			} else {
				calendar.set(dateInt[0] + year, dateInt[1] + month, dateInt[2] + day + 1);
			}
			dateStr = sdf.format(calendar.getTime());
		}
		return dateStr;
	}

	/**
	 * 清除账号列表选中状态
	 * 
	 * @param list
	 * @return
	 */
	public static List<Object> clearSelectedStatus(List<Object> list) {
		if (list == null || list.isEmpty())
			return null;
		for (int i = 0; i < list.size(); i++) {
			Map<Object, Object> account = (Map<Object, Object>) list.get(i);

			if (account.containsKey(PubConstants.PUB_FIELD_ISSELECTED)) {
				account.put(PubConstants.CONTEXT_FIELD_SELECTED_ACCLIST, false);
			} else {
				continue;
			}
		}

		return list;
	}

	/**
	 * 获取登录信息
	 * 
	 * @param key
	 * @return
	 */
	public static String getLoginInfo(String key) {
		Map<Object, Object> loginInfo = getMap(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA));
		return getString(loginInfo.get(key), "");
	}

	/**
	 * 获取电子支付账户类型
	 * 
	 * @return
	 */
	public static List<Object> getAccTypeList() {
		List<Object> list = new ArrayList<Object>();
		list.add("103");
		list.add("104");
		list.add("119");
		list.add("108");
		list.add("109");
		list.add("107");
		return list;
	}

	/**
	 * 格式化日期为yyyy/MM/dd
	 * 
	 * @param systemTime
	 * @return
	 */
	public static String converSystemTime(String systemTime) {
		Date curDate = null;
		try {
			curDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(systemTime);
		} catch (ParseException e) {
			curDate = new Date();
		}
		return new SimpleDateFormat("yyyy/MM/dd").format(curDate);
	}

	/**
	 * 将object装换为字符串
	 * 
	 * @param obj
	 * @param defaultValue
	 *            默认值
	 * @return
	 */
	public static String getString(Object obj, String defaultValue) {
		if (StringUtil.isNullOrEmpty(obj))
			return defaultValue;
		else
			return String.valueOf(obj).trim();
	}

	/**
	 * 将object装换为布尔
	 * 
	 * @param obj
	 * @param defaultValue
	 *            默认值
	 * @return
	 */
	public static Boolean getBoolean(Object obj) {
		if (StringUtil.isNullOrEmpty(obj))
			return false;
		else
			return Boolean.valueOf(obj.toString().trim());
	}

	/**
	 * 将object装换为List
	 * 
	 * @param obj
	 * @param defaultValue
	 *            默认值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> getList(Object obj) {
		List<Object> resultList = new ArrayList<Object>();
		;
		if (StringUtil.isNullOrEmpty(obj)) {
			return resultList;
		}
		if (obj instanceof List)
			resultList = (List<Object>) obj;
		return resultList;
	}

	/**
	 * 将object装换为Map
	 * 
	 * @param obj
	 * @param defaultValue
	 *            默认值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<Object, Object> getMap(Object obj) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		if (StringUtil.isNullOrEmpty(obj)) {
			return map;
		}
		if (obj instanceof Map)
			map = (Map<Object, Object>) obj;
		return map;
	}
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getMapResponse(Object obj) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtil.isNullOrEmpty(obj)) {
			return map;
		}
		if (obj instanceof Map)
			map = (Map<String, Object>) obj;
		return map;
	}

	
	public static <T> T IsChecked(Object obj) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtil.isNullOrEmpty(obj)) {
			return null;
		}
		if (obj instanceof Map)
			return (T) obj;
		return null;
	}
	
	/**
	 * 将object装换为整型
	 * 
	 * @param obj
	 * @param defaultValue
	 *            默认值
	 * @return
	 */
//	public static int getInt(Object obj, int defaultValue) {
//		if (StringUtil.isNullOrEmpty(obj))
//			return defaultValue;
//		else
//			return Integer.valueOf(obj.toString().trim());
//	}

	/**
	 * 将object装换为长整型
	 * 
	 * @param obj
	 * @return
	 */
//	public static long getLong(Object obj) {
//		if (StringUtil.isNullOrEmpty(obj)) {
//			return 0;
//		} else {
//			return Long.valueOf(obj.toString().trim());
//		}
//	}

	/**
	 * 获取安全验证工具
	 * 
	 * @param resultMap
	 * @return
	 */
	public static List<Object> getFactorList(Map<Object, Object> resultMap) {
		List<Object> resultList = getList(resultMap.get(PubConstants.PUB_FIELD_FACTORLIST));
		ArrayList<Object> factorList = new ArrayList<Object>();

		for (Object obj : resultList) {
			Map<Object, Object> fieldMap = getMap(obj);
			Map<Object, Object> subMap = getMap(fieldMap.get(PubConstants.METHOD_GET_SECURITY_FACTOR_FIELD));
			Object name = subMap.get(PubConstants.METHOD_GET_SECURITY_FACTOR_FIELD_NAME);
			Object type = subMap.get(PubConstants.METHOD_GET_SECURITY_FACTOR_FIELD_TYPE);
			if (!"_signedData".equals(name))
				factorList.add(name);
		}

		return factorList;
	}
	/**
	 * 获取安全验证工具
	 * 
	 * @param resultMap
	 * @return
	 */
//	public static List<Object> getFactorListResponse1(Map<String, Object> resultMap) {
//		List<Object> resultList = getList(resultMap.get(PubConstants.PUB_FIELD_FACTORLIST));
//		ArrayList<Object> factorList = new ArrayList<Object>();
//
//		for (Object obj : resultList) {
//			Map<Object, Object> fieldMap = getMap(obj);
//			Map<Object, Object> subMap = getMap(fieldMap.get(PubConstants.METHOD_GET_SECURITY_FACTOR_FIELD));
//			Object name = subMap.get(PubConstants.METHOD_GET_SECURITY_FACTOR_FIELD_NAME);
//			Object type = subMap.get(PubConstants.METHOD_GET_SECURITY_FACTOR_FIELD_TYPE);
//			if (!"_signedData".equals(name))
//				factorList.add(name);
//		}
//
//		return factorList;
//	}

}
