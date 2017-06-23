package com.chinamworld.bocmbci.biz.thridmanage;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.utils.QueryDateUtils;

public class ThirdUtil {

	@SuppressWarnings("unchecked")
	public static List<String> getResponseResultToList(BiiResponseBody biiResponseBody) {
		List<String> list = (List<String>) biiResponseBody.getResult();
		return list;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getResponseResultToMap(BiiResponseBody biiResponseBody) {
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		return result;
	}

	/**
	 * @ClassName: QueryDate
	 * @Description: 查询比较
	 * @author lql
	 * @date 2013-9-12 下午04:36:34
	 * 
	 */
	public enum QueryDate {
		/** 查询范围最大为三个月 */
		START_END_THREE_MONTH,
		/** 起始日期最早为系统当前日期一年前 */
		START_SYSDATE_ONE_YEAR,
		/** 开始时间大于结束时间 */
		START_END,
		/** 结束日期最晚为系统当前日期 */
		END_SYSDATE,
	}

	/**
	 * @param dataTime
	 *            系统时间
	 * @param startDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @param pattern
	 *            格式化
	 *            <p>
	 *            yyyy/MM/dd
	 *            <p>
	 * @return boolean true 比较通过，false比较失败
	 * */
	public static boolean compareDate(Context context, String dateTime, String startDate, String endDate,
			String pattern, QueryDate... queryDate) {
		return compareDate(context, dateTime, startDate, endDate, pattern, queryDate);
	}

	public static boolean compareDate(Context context, String dateTime, String startDate, String endDate,
			String pattern, List<QueryDate> queryDates) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		// TODO
		for (QueryDate queryData : queryDates) {

			if (queryData == QueryDate.START_SYSDATE_ONE_YEAR) {
				if (!QueryDateUtils.compareDateOneYear(startDate, dateTime)) {
					// 开始日期在系统日期前一年以内
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							context.getString(R.string.acc_check_start_enddate));
					return false;
				}
			}

			if (queryData == QueryDate.END_SYSDATE) {
				if (!QueryDateUtils.compareDate(endDate, dateTime)) {
					// 结束日期在系统日期之前
					BaseDroidApp.getInstanse().showInfoMessageDialog(context.getString(R.string.acc_check_enddate));
					return false;
				}
			}

			if (queryData == QueryDate.START_END) {
				if (!QueryDateUtils.compareDate(startDate, endDate)) {
					// 开始日期在结束日期之前
					BaseDroidApp.getInstanse().showInfoMessageDialog(context.getString(R.string.acc_query_errordate));
					return false;
				}
			}

			if (queryData == QueryDate.START_END_THREE_MONTH) {
				if (!QueryDateUtils.compareDateThree(startDate, endDate)) {
					// 起始日期与结束日期最大间隔为三个自然月
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							context.getString(R.string.acc_check_start_end_date));
					return false;
				}
			}
		}

		return true;
	}

}
