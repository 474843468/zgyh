package com.boc.bocsoft.mobile.bocmobile.buss.account.utils;

import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wangyang
 *         16/8/26 02:43
 *         时间工具类
 */
public class DateUtil {

    /**
     * 根据格式化字符串,与毫秒值得到格式化日期字符串
     *
     * @param milliseconds
     * @param format
     * @return
     * @author wangyang 2014-7-16 下午3:47:44
     */
    public static String format(long milliseconds, String format) {
        return new SimpleDateFormat(format).format(new Date(milliseconds));
    }

    public static String format(long milliseconds) {
        return format(milliseconds, DateFormatters.DATE_FORMAT_V2_1);
    }

    public static long parse(String date, String format) {
        try {
            return new SimpleDateFormat(format).parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long parse(String date) {
        try {
            return new SimpleDateFormat(DateFormatters.DATE_FORMAT_V2_1).parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean compare(long date1, long date2) {
        return date2 > date1;
    }
}
