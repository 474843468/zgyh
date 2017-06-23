package com.chinamworld.bocmbci.biz.crcd.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 信用卡排序
 * 
 * @author huangyuchao
 * 
 */
public class SortUtils {

	private static SortUtils instance;

	private SortUtils() {

	}

	public SortUtils getInstance() {
		if (null == instance) {
			instance = new SortUtils();
		}
		return instance;
	}

	/** 时间排序 */
	public static List<Map<String, Object>> datesort(
			List<Map<String, Object>> list) {
		if (!StringUtil.isNullOrEmpty(list)) {
			Collections.sort(list, new Comparator<Map<String, Object>>() {

				@Override
				public int compare(Map<String, Object> object1,
						Map<String, Object> object2) {
					return (((String.valueOf(object2.get(Crcd.CRCD_TRANSDATE))))
							.compareTo((String.valueOf(object1
									.get(Crcd.CRCD_TRANSDATE)))));
				}
			});
		}
		return list;
	}

	/** 收入在前 */
	public static List<Map<String, Object>> insort(
			List<Map<String, Object>> list) {
		if (!StringUtil.isNullOrEmpty(list)) {
			Collections.sort(list, new Comparator<Map<String, Object>>() {

				@Override
				public int compare(Map<String, Object> object1,
						Map<String, Object> object2) {
					return (((String.valueOf(object2.get(Crcd.CRCD_TRANAMOUNT))))
							.compareTo((String.valueOf(object1
									.get(Crcd.CRCD_TRANAMOUNT)))));
				}
			});
		}
		return list;
	}

	/** 支出在前 */
	public static List<Map<String, Object>> outsort(
			List<Map<String, Object>> list) {
		if (!StringUtil.isNullOrEmpty(list)) {
			Collections.sort(list, new Comparator<Map<String, Object>>() {

				@Override
				public int compare(Map<String, Object> object1,
						Map<String, Object> object2) {
					return (((String.valueOf(object1.get(Crcd.CRCD_TRANAMOUNT))))
							.compareTo((String.valueOf(object2
									.get(Crcd.CRCD_TRANAMOUNT)))));
				}
			});
		}
		return list;
	}

	/** 收入 */
	public static List<Map<String, Object>> getIn(List<Map<String, Object>> list) {
		List<Map<String, Object>> inList = new ArrayList<Map<String, Object>>();
		if (!StringUtil.isNullOrEmpty(list)) {
			for (Map<String, Object> map : list) {
				String debitCreditFlag = (String) map.get(Crcd.CRCD_DEBITFLAG);
				if ("CRED".equals(debitCreditFlag)) {
					inList.add(map);
				}
			}
		}
		return inList;
	}

	/** 支出 */
	public static List<Map<String, Object>> getOut(
			List<Map<String, Object>> list) {
		List<Map<String, Object>> outList = new ArrayList<Map<String, Object>>();
		if (!StringUtil.isNullOrEmpty(list)) {
			for (Map<String, Object> map : list) {
				String debitCreditFlag = (String) map.get(Crcd.CRCD_DEBITFLAG);
				if ("DEBT".equals(debitCreditFlag)
						|| "NMON".equals(debitCreditFlag)) {
					outList.add(map);
				}
			}
		}
		return outList;
	}
}
