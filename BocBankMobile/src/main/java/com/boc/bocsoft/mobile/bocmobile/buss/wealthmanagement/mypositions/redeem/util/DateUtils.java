package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 日期工具类
 * Created by yx on 2016/11/2.
 */
public class DateUtils {

    private final static ThreadLocal<SimpleDateFormat> dateAndTimeFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy/MM/dd");
        }
    };

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        return toDate(sdate, dateFormater.get());
    }

    public static Date toDate(String sdate, SimpleDateFormat dateFormater) {
        try {
            return dateFormater.parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String fromDate(Date date, String format) {
        SimpleDateFormat formater = new SimpleDateFormat(format);
        return formater.format(date);
    }

    public static String getDateString(Date date) {
        return dateFormater.get().format(date);
    }

    /**
     *
     * @param date
     *
     * @return"yyyy/MM/dd"
     */
    public static String getDateString2(Date date) {
        return dateFormater2.get().format(date);
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public static int getWeekOfDate(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return w;
    }

    /***
     * 计算两个时间差，返回的是的秒s
     *
     * @author 火蚁 2015-2-9 下午4:50:06
     *
     * @return long
     * @param dete1
     * @param date2
     * @return
     */
    public static long calDateDifferent(String dete1, String date2) {

        long diff = 0;

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = dateAndTimeFormater.get().parse(dete1);
            d2 = dateAndTimeFormater.get().parse(date2);

            // 毫秒ms
            diff = d2.getTime() - d1.getTime();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return diff / 1000;
    }

    // =====================================下面闫勋添加===2015年11月17日===========================
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
    private static SimpleDateFormat format_minus_sign = new SimpleDateFormat(
            "yyyy-MM-dd");
    private static SimpleDateFormat format24 = new SimpleDateFormat(
            "yyyy/MM/dd HH:mm:ss");
    private static SimpleDateFormat formathms = new SimpleDateFormat("HH:mm:ss");
    private static SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy/MM/dd", Locale.CHINA);
    private static SimpleDateFormat formatLocal = new SimpleDateFormat(
            "yyyy/MM/dd", Locale.getDefault());
    private static SimpleDateFormat longDateFormat = new SimpleDateFormat(
            "yyyy/MM/dd HH:mm:ss", Locale.getDefault());

    /**
     * 将字符串转为"yyyy/MM/dd"格式的date类型
     *
     * @param dateString
     * @return
     */
    public static Date formatStrToDate(String dateString) {
        Date resultDate = null;
        try {
            resultDate = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return resultDate;
    }

    /**
     * 格式化 日期 yyyy/MM/dd yx
     */
    public static String formatDateDefault(Date date) {
        return format.format(date);
    }

    /**
     * 格式化 日期 yyyy-MM-dd hcp
     */
    public static String formatDateMinusSign(Date date) {
        return format_minus_sign.format(date);
    }

    /**
     * 将传入日期yyyy/MM/DD转化为yyyy年mm月dd日
     *
     * @param date
     *            金额
     * @return
     */
    public static String transDate(String date) {

        Date d = new Date(date);
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy年MM月dd日");
        return mFormat.format(d);
    }

    /**
     * 字符串形式日期按照格式转换
     *
     * @param date
     * @param format
     * @return
     * @throws ParseException
     */
    public static String S2SWithFormat(String date, String format) {
        Date d = new Date(date);
        SimpleDateFormat mFormat = new SimpleDateFormat(format);
        return mFormat.format(d);
    }

    /**
     * 前一周
     *
     * @author fhx
     *
     * @param sysTime
     * @return
     */
    public static String getlastweek(String sysTime) {
        return getDateFormToday(sysTime, 7);
    }

    /**
     * 日期字符串转换, 传入日期为空的话返回设备时间 yyyy/MM/dd HH:mm:ss->yyyy/MM/dd
     *
     * @param date
     *            yyyy/MM/dd HH:mm:ss型
     * @return yyyy/MM/dd型
     */
    public static String parseDifDate(String date) {
        // 传入日期为空的情况，返回设备时间
        if (null == date || "".equals(date) || "null".equals(date)) {
            return getSystemCurrentMonDay();
        }
        return formatDateDefault(new Date(date));
    }

    /**
     * 格式化 日期 yyyy/MM/dd HH:mm:ss zyk
     */
    public static String formatDate24(Date date) {
        return format24.format(date);
    }

    /**
     * 截取日期的时分秒
     *
     * @param ymdhms
     *            {yyyy/MM/dd HH:mm:ss 格式}
     * @return
     */
    public static String getHourMinuteSecond(String ymdhms) {
        Date date;
        try {
            date = format24.parse(ymdhms);
            return formathms.format(date);
        } catch (ParseException e) {
        }
        return "";
    }

    /**
     * 比较两个字符串日期间隔是否超过mouthRange个月，str1 < str2
     * ,格式为：yyyy/MM/dd,false为超过mouthRange个月，true为没有超过。
     *
     * @param str1
     * @param str2
     * @param mouthRange
     * @return
     */
    public static boolean compareDateRange(String str1, String str2,
                                           int mouthRange) {

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        Date date1 = null;
        Date date2 = null;
        try {

            date1 = format.parse(str1);
            date2 = format.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }

        c1.setTime(date1);
        c2.setTime(date2);
        c2.add(Calendar.MONTH, -mouthRange);
        return !c1.before(c2);
    }

    /**
     * 比较两个字符串日期间隔是否超过mouthRange个月，str1 < str2
     * ,格式为：yyyy/MM/dd,false为超过mouthRange个月，true为没有超过。
     *
     * @param str1
     * @param str2
     * @param mouthRange
     * @return
     */
    public static boolean compareDateRange(Date str1, Date str2, int mouthRange) {

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        Date date1 = str1;
        Date date2 = str2;
        c1.setTime(date1);
        c2.setTime(date2);
        c2.add(Calendar.MONTH, -mouthRange);
        return !c1.before(c2);
    }

    /**
     * 获得两个时间的间隔天数
     *
     * @author yanxun
     * @param begin
     * @param end
     * @return
     */
    public static int getIntervalDays(Date begin, Date end) {
        if (begin == null || end == null)
            return 0;
        long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
        return (int) between / (24 * 3600);
    }

    /**
     * 根据传入的要减去的时间差，来返回时间
     *
     * @param mDay
     *            是否是要计算天数
     * @param mMonth
     *            是否是要计算月数
     * @param mYear
     *            是否是要计算年数
     * @param mTVDay
     *            天数
     * @param mTVMonth
     *            月数
     * @param mTVYear
     *            年数
     * @return
     */
    public static Date dateOpinionShow(Date curDate, boolean mDay,
                                       boolean mMonth, boolean mYear, int mTVDay, int mTVMonth, int mTVYear) {
        Date mDateTime = curDate;
        GregorianCalendar eCalendar = new GregorianCalendar();
        if (curDate != null) {
            eCalendar.setTime(curDate);
            if (mDay) {
                eCalendar.add(Calendar.DAY_OF_MONTH, -mTVDay);
                mDateTime = eCalendar.getTime();
                return mDateTime;
            } else if (mMonth) {
                eCalendar.add(Calendar.MONTH, -mTVMonth);
                mDateTime = eCalendar.getTime();
                return mDateTime;
            } else if (mYear) {
                eCalendar.add(Calendar.YEAR, -mTVYear);
                mDateTime = eCalendar.getTime();
                return mDateTime;
            }
        }
        return mDateTime;

    }

    /**
     * 明天
     *
     * @author yx
     * @date 2016年1月29日14:34:24
     * @param sysTime
     *            系统时间 格式： yyyy/MM/dd hh:mm:ss
     * @return 前一天 格式 yyyy/MM/dd
     */
    public static String getTomorrow(String sysTime) {
        if (TextUtils.isEmpty(sysTime)) {
            Date date = new Date();
            return format.format(date);
        }
        Date mDateTiem = dateOpinionShow(new Date(sysTime), true, false, false,
                -1, 0, 0);
        return format.format(mDateTiem);
    }

    /**
     * 前一天
     *
     * @param sysTime
     *            系统时间 格式： yyyy/MM/dd hh:mm:ss
     * @return 前一天 格式 yyyy/MM/dd
     */
    public static String getYesterday(String sysTime) {
        if (TextUtils.isEmpty(sysTime)) {
            Date date = new Date();
            return format.format(date);
        }
        Date mDateTiem = dateOpinionShow(new Date(sysTime), true, false, false,
                1, 0, 0);
        return format.format(mDateTiem);
    }

    /**
     * 近三天
     *
     * @param sysTime
     *            系统时间 格式： yyyy/MM/dd hh:mm:ss
     * @return 近三天 , 格式 yyyy/MM/dd
     */
    public static String getRecentThreeDays(String sysTime) {
        return getDateFormToday(sysTime, 2);
    }

    /**
     * 三日内
     *
     * @param systime
     * @return
     */
    public static String getNextThreeDays(String systime) {
        return getDateFormToday(systime, -2);
    }

    /**
     * 以今天为参照 获取间隔天的日期字符串
     *
     * @param days
     *            间隔天数
     * @return yyyy/MM/dd
     */
    private static String getDateFormToday(String sysTime, int days) {
        if (TextUtils.isEmpty(sysTime)) {
            Date date = new Date();
            return format.format(date);
        }
        Date mDateTiem = dateOpinionShow(new Date(sysTime), true, false, false,
                days, 0, 0);
        return format.format(mDateTiem);
    }

    /**
     * 前一年
     *
     * @param sysTime
     *            系统时间 格式： yyyy/MM/dd hh:mm:ss
     * @return 前一年 格式 yyyy/MM/dd zhanghj
     */
    public static String getRecentYear(String sysTime) {
        if (TextUtils.isEmpty(sysTime)) {
            Date date = new Date();
            return format.format(date);
        }
        Date mDateTiem = dateOpinionShow(new Date(sysTime), false, false, true,
                0, 0, 1);
        return format.format(mDateTiem);
    }

    /**
     * 前一年
     *
     * @param sysTime
     *            系统时间
     * @return 前一年
     */
    public static Date getRecentYear(Date sysTime) {
        if (null == sysTime) {
            return dateOpinionShow(new Date(), false, false, true, 0, 0, 1);
        }
        return dateOpinionShow(sysTime, false, false, true, 0, 0, 1);
    }

    /**
     * 近一周
     *
     * @param sysTime
     *            系统时间 格式： yyyy/MM/dd hh:mm:ss
     * @return 近一周 , 格式 yyyy/MM/dd
     */
    public static String getRecentWeek(String sysTime) {
        if (TextUtils.isEmpty(sysTime)) {
            Date date = new Date();
            return format.format(date);
        }
        Date mDateTiem = dateOpinionShow(new Date(sysTime), true, false, false,
                6, 0, 0);
        return format.format(mDateTiem);
    }

    /**
     * 近一周（时间控件用）
     *
     * @author hcp
     * @param sysTime
     *            系统时间
     * @return 近一周 , 格式 yyyy-MM-dd
     */
    public static Date getRecentWeek(Date sysTime) {
        if (null == sysTime) {
            return dateOpinionShow(new Date(), true, false, false, 7, 0, 0);
        }
        return dateOpinionShow(sysTime, true, false, false, 7, 0, 0);
    }

    /**
     * 一周内
     *
     * @param sysTime
     * @return
     */
    public static String getNextWeek(String sysTime) {
        return getDateFormToday(sysTime, -6);
    }

    /**
     * 近一月（等同于系统时间的前一个月）
     *
     * @param sysTime
     *            系统时间 格式： yyyy/MM/dd hh:mm:ss
     * @return 近一月 , 格式 yyyy/MM/dd
     */
    public static String getRecentMonth(String sysTime) {
        if (TextUtils.isEmpty(sysTime)) {
            Date date = new Date();
            return format.format(date);
        }
        Date mDateTiem = dateOpinionShow(new Date(sysTime), false, true, false,
                0, 1, 0);
        return format.format(mDateTiem);
    }

    /**
     * 近3月（等同于系统时间的前3个月）
     *
     * @param sysTime
     *            系统时间
     * @return 近3月
     * @author fhx
     */
    public static Date getRecenThreetMonth(Date sysTime) {
        if (null == sysTime) {
            Date mDateTiemOld = dateOpinionShow(new Date(), false, true, false, 0, 3, 0);
            return dateOpinionShow(mDateTiemOld, true, false, false,-1, 0, 0);
        }
        Date mDateTiemOld = dateOpinionShow(sysTime, false, true, false, 0, 3, 0);	/**
         * P603投产验证问题
         * 近三个月计算方法修改：
         * java计算，当前日期－3个月＋1天
         */
        return dateOpinionShow(mDateTiemOld, true, false, false,-1, 0, 0);
    }

    /**
     * 近3月（等同于系统时间的前3个月）
     *
     * @param sysTime
     *            系统时间 格式： yyyy/MM/dd hh:mm:ss
     * @return 近3月 , 格式 yyyy/MM/dd
     * @author fhx
     */
    public static String getRecenThreetMonth(String sysTime) {
        if (TextUtils.isEmpty(sysTime)) {
            Date date = new Date();
            return format.format(date);
        }
        Date mDateTiemOld = dateOpinionShow(new Date(sysTime), false, true, false,
                0, 3, 0);
        /**
         * P603投产验证问题
         * 近三个月计算方法修改：
         * java计算，当前日期－3个月＋1天
         *
         */
        Date mDateTiemNew = dateOpinionShow(mDateTiemOld, true, false, false,
                -1, 0, 0);
        return format.format(mDateTiemNew);
    }

    /**
     * 近一月（等同于系统时间的前一个月）
     *
     * @param sysTime
     *            系统时间
     * @return 近一月
     */
    public static Date getRecentMonth(Date sysTime) {
        if (null == sysTime) {
            return dateOpinionShow(new Date(), false, true, false, 0, 1, 0);
        }
        return dateOpinionShow(sysTime, false, true, false, 0, 1, 0);
    }

    /**
     * 后一月（等同于系统时间的加一个月）
     *
     * @param sysTime
     *            系统时间 格式： yyyy/MM/dd hh:mm:ss
     * @return 后一月 , 格式 yyyy/MM/dd
     */
    public static String getNextMonth(String sysTime) {
        if (TextUtils.isEmpty(sysTime)) {
            Date date = new Date();
            return format.format(date);
        }
        Date mDateTiem = dateOpinionShow(new Date(sysTime), false, true, false,
                0, -1, 0);
        return format.format(mDateTiem);
    }

    /**
     * 后一月（等同于系统时间的加一个月）
     *
     * @param sysTime
     *            系统时间
     * @return 后一月
     */
    public static Date getNextMonth(Date sysTime) {
        if (null == sysTime) {
            return dateOpinionShow(new Date(), false, true, false, 0, -1, 0);
        }
        return dateOpinionShow(sysTime, false, true, false, 0, -1, 0);
    }

    /**
     * 后三个月（等同于系统时间的加三个月）
     *
     * @param sysTime
     *            系统时间
     * @return 后一月
     */
    public static Date getNextThreeMonth(Date sysTime) {
        if (null == sysTime) {
            return dateOpinionShow(new Date(), false, true, false, 0, -3, 0);
        }
        return dateOpinionShow(sysTime, false, true, false, 0, -3, 0);
    }

    /**
     * 指定日期后dayAdd天
     *
     * @param date
     *            系统时间 格式： yyyy/MM/dd
     * @return 指定日期后 dayAdd 天
     */
    public static String getDateAfterDayCount(String date, int dayAdd) {
        if (TextUtils.isEmpty(date)) {
            return format.format(dateOpinionShow(new Date(), true, false,
                    false, -dayAdd, 0, 0));
        }
        Date mDateTiem = dateOpinionShow(new Date(date), true, false, false,
                -dayAdd, 0, 0);
        return format.format(mDateTiem);
    }

    /**
     * 指定日期后dayAdd天
     *
     * @param date
     *            系统时间 格式： yyyy/MM/dd
     * @return 指定日期后 dayAdd 天
     */
    public static Date getDateAfterDayCount(Date date, int dayAdd) {
        Calendar mCalendar = Calendar.getInstance();
        if (null == date) {
            mCalendar.add(Calendar.DAY_OF_MONTH, dayAdd);
        } else {
            mCalendar.setTime(date);
            mCalendar.add(Calendar.DAY_OF_MONTH, dayAdd);
        }
        return mCalendar.getTime();
    }

    /**
     * 日期格式转换（yyyy/MM/dd HH:mm:ss->yyyy/MM/dd）
     *
     * @param date
     *            yyyy/MM/dd HH:mm:ss
     * @return yyyy/MM/dd
     */
    public static String getYearMonDay(String date) {
        if (TextUtils.isEmpty(date)) {
            Date localDate = new Date();
            return format.format(localDate);
        }
        Date mdate = null;
        try {
            mdate = new Date(date);
        } catch (Exception e) {
            return "";
        }
        return format.format(mdate);

    }

    /**
     * 获取现在的年
     *
     * @return
     */
    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * 获取现在的月份
     *
     * @return
     */
    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    /**
     * 获取现在是几号
     *
     * @return
     */
    public static int getNowDayInMonth() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取该月有多少天
     *
     * @param month
     *            1--12
     * @return
     */
    public static int getDaysInMonth(int year, int month) {
        if (month > 12 || month < 1) {
            return 0;
        }
        if (year < 1970) {
            year = getYear();
        }
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
                || month == 10 || month == 12) {
            return 31;
        } else if (month == 2) {
            if ((year % 400 == 0) || (year % 4 == 0) && (year % 100 != 0)) {
                return 29;
            }
            return 28;
        } else {
            return 30;
        }
    }

    /**
     * 获取设备的当前时间
     *
     * @return yyyy/MM/dd
     */
    public static String getSystemCurrentMonDay() {
        Date localDate = new Date();
        return format.format(localDate);

    }

    /**
     * 获取设备的当前时间
     *
     * @return date
     */
    public static Date getDeviceCurrentDate() {
        return new Date();
    }

    /**
     * 获取系统时间的后一天
     *
     * @param date
     *            yyyy/MM/dd型，系统时间（字符串）
     * @return Calendar 系统时间的后一天
     */
    public static Calendar getServerTimeNextDay(String date) {
        Calendar calendar = GregorianCalendar.getInstance();
        if (TextUtils.isEmpty(date)) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            return calendar;
        }
        try {
            Date formatDate = formatter.parse(date);
            calendar.setTime(formatDate);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    /**
     * 获取系统时间的后一天
     *
     * @param date
     *            系统时间（Date型）
     * @return Calendar 系统时间的后一天
     */
    public static Calendar getServerTimeNextDay(Date date) {
        Calendar calendar = GregorianCalendar.getInstance();
        if (null == date) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            return calendar;
        }
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar;
    }

    /**
     * 仅限滑轮日期组件使用
     *
     * @param timeInMillis
     *            毫秒数
     * @param timeType
     *            返回日期类型 0：yyyy/MM/dd HH:mm:ss 1：yyyy/MM/dd
     * @return 格式化的日期字符串
     */
    public static String getDateFromMillis(String timeInMillis, int timeType) {
        String formatedTime = "";
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(timeInMillis));
        if (timeType == 1) {

            formatedTime = formatLocal.format(calendar.getTime());
        } else if (timeType == 0) {

            formatedTime = longDateFormat.format(calendar.getTime());
        }
        return formatedTime;
    }

    /**
     * 比较date2是否在date1之后（日期相等也返回true）
     *
     * @param date1
     * @param date2
     * @return if date2>=date1
     */
    public static Boolean isDateBefore(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        } else {
            return !date1.after(date2);
        }
    }

    /**
     * 比较date2是否在date1之后（日期相等也返回true）
     *
     * @param date1
     *            yyyy/MM/dd格式
     * @param date2
     *            yyyy/MM/dd格式
     * @return if date2>=date1
     */
    public static Boolean isDateBefore(String date1, String date2) {
        if (date1 == null || date2 == null) {
            return false;
        } else {
            Date date11 = null;
            Date date22 = null;
            try {
                date11 = format.parse(date1);
                date22 = format.parse(date2);
            } catch (ParseException e) {
                return false;
            }
            return isDateBefore(date11, date22);
        }
    }

    /**
     * 比较date2是否在date1之后（日期相等返回false）
     *
     * @param date1
     *            yyyy/MM/dd格式
     * @param date2
     *            yyyy/MM/dd格式
     * @return true if date2>date1
     */
    public static Boolean isDateBeforeNotEqual(String date1, String date2) {
        if (date1 == null || date2 == null) {
            return false;
        } else {
            Date date11 = null;
            Date date22 = null;
            try {
                date11 = format.parse(date1);
                date22 = format.parse(date2);
            } catch (ParseException e) {
                return false;
            }
            return date22.after(date11);
        }
    }

    /**
     * 判断日期是否在今天之后。false 今天之前，true为今天和今天之后
     *
     * @param time
     * @param now
     * @return
     */
    public static boolean checkDateIsNow(String time, Calendar now) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd");
        String nowTime = sd.format(now.getTime());

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        Date date1 = null;
        Date date2 = null;
        try {
            date1 = sd.parse(time);
            date2 = sd.parse(nowTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        c1.setTime(date1);
        c2.setTime(date2);
        return !c1.before(c2);
    }

    /**
     * 判断日期是否超过当前一年
     *
     * @param str1
     * @param d2
     * @return true没有超过一年，false为超过一年
     */
    public static boolean checkDateIsBeforeOneYear(String str1, Calendar d2) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd");
        String str2 = sd.format(d2.getTime());

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        Date date1 = null;
        Date date2 = null;
        try {
            date1 = sd.parse(str1);
            date2 = sd.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        c1.setTime(date1);
        c2.setTime(date2);

        c1.add(Calendar.YEAR, +1);
        return !c1.before(c2);
    }

    /**
     * 比较两个字符串日期间隔是否超过三个月，str1 < str2 ,格式为：yyyy/MM/dd,false为超过三个月，true为没有超过。
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean compareDateString(String str1, String str2) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd");

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        Date date1 = null;
        Date date2 = null;
        try {

            date1 = sd.parse(str1);
            date2 = sd.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }

        c1.setTime(date1);
        c2.setTime(date2);
        c2.add(Calendar.MONTH, -3);
        return !c1.before(c2);
    }

    /**
     * 比较两个字符串日期间隔是否超过三个月，str1 < str2 ,格式为：yyyy/MM/dd,false为超过三个月，true为没有超过。
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean compareDate(String str1, String str2,
                                      SimpleDateFormat sd) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        Date date1 = null;
        Date date2 = null;
        try {

            date1 = sd.parse(str1);
            date2 = sd.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }

        c1.setTime(date1);
        c2.setTime(date2);
        // c2.add(Calendar.MONTH, -3);
        return c1.after(c2);
    }

    /**
     * 判断日期是否是d2日期之后，false 为str1在d2之后，true为str1和d2相等或在之前。
     *
     * @param str1
     * @param d2
     * @return
     */
    public static boolean checkDateIsAfterToday(String str1, Calendar d2) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd");
        String str2 = sd.format(d2.getTime());

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        Date date1 = null;
        Date date2 = null;
        try {
            date1 = sd.parse(str1);
            date2 = sd.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        c1.setTime(date1);
        c2.setTime(date2);
        return !c1.after(c2);
    }

    /**
     * 格式化 日期
     */
    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat mFormat = new SimpleDateFormat(pattern);
        return mFormat.format(date);
    }

    /**
     * 截取日期
     *
     * @param str
     *            日期格式2014/01/01
     * @return 截取后的数组
     */
    public static String[] splitStr(String str) {
        if (TextUtils.isEmpty(str) || !str.contains("/")) {
            return null;
        }
        return str.split("/");
    }

    /**
     *
     * @param mCalendar
     *            时间
     * @param dateStr
     *            {yyyy/MM/dd HH:mm:ss 格式}
     * @return
     */
    public static boolean isSameDay(Calendar mCalendar, String dateStr) {
        try {
            Date date = format24.parse(dateStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            if (mCalendar.get(Calendar.YEAR) == cal.get(Calendar.YEAR)
                    && mCalendar.get(Calendar.MONTH) == cal.get(Calendar.MONTH)
                    && mCalendar.get(Calendar.DAY_OF_MONTH) == cal
                    .get(Calendar.DAY_OF_MONTH))
                return true;
        } catch (ParseException e) {
        }
        return false;
    }

    /**
     * 把yyyymmdd转成yyyy-MM-dd格式
     *
     * @param str
     * @return
     * @author xunyan
     */
    public static String formatDate(String str) {
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sf2 = new SimpleDateFormat("yyyy/MM/dd");
        String sfstr = "";
        try {
            sfstr = sf2.format(sf1.parse(str));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sfstr;
    }

    /**
     * clone一个calender
     *
     * @param mCalendar
     * @return
     */
    public static Calendar cloneCalendar(Calendar mCalendar) {
        Calendar calendar = Calendar.getInstance();
        if (mCalendar == null) {
            return mCalendar;
        }

        calendar.setTime(mCalendar.getTime());
        return calendar;
    }

    /**
     * @param isSingaPore
     *            是否新加坡
     * @return 日期格式
     */
    public static String getDateFormat(boolean isSingaPore) {
        if (isSingaPore) {
            return "dd/MM/yyyy";
        } else {
            return "yyyy/MM/dd";
        }
    }

    /**
     * 带时间的日期时间格式
     *
     * @param isSingaPore
     * @return
     */
    public static String getDateFormatWithTime(boolean isSingaPore) {
        if (isSingaPore) {
            return "dd/MM/yyyy HH:mm:ss";
        } else {
            return "yyyy/MM/dd HH:mm:ss";
        }
    }

    // ===============================2016-04-12 16:22:59 闫勋 添加===============
    public static SimpleDateFormat yearSdf = new SimpleDateFormat("yyyy");

    public static SimpleDateFormat mmSdf = new SimpleDateFormat("MM");

    public static SimpleDateFormat ddSdf = new SimpleDateFormat("dd");

    public static SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");

    public static SimpleDateFormat timestampSdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat yearmmSdf = new SimpleDateFormat("yyyy/MM");
    public static SimpleDateFormat mmyearSdf = new SimpleDateFormat("MM/yyyy");
    /**
     * 数据转换格式，24小时制
     */
    public final static String date24Format = "yyyy-MM-dd HH:mm:ss";

    // private static SimpleDateFormat dateSdf2 = new
    // SimpleDateFormat("yyyyMMdd");
    /**
     * 获取日期中年份
     *
     * @param date
     * @return
     */
    public static String getYear(Date date) {
        if (date == null) {
            return "";
        } else {
            return yearSdf.format(date);
        }

    }

    /** 获取日期中的月份 */
    public static String getMonth(Date date) {
        if (date == null) {
            return "";
        } else {
            return mmSdf.format(date);
        }
    }

    /** 获取日期的天 */
    public static String getDay(Date date) {
        if (date == null) {
            return "";
        } else {
            return ddSdf.format(date);
        }
    }

    /**
     * 获取日期-yyyy/MM
     *
     * @author yx
     * @date 2016-04-13 14:15:24
     */
    public static String getYearMM(Date date) {
        if (date == null) {
            return "";
        } else {
            return yearmmSdf.format(date);
        }
    }

    /**
     * 获取日期-MM/yyyy
     *
     * @author yx
     * @date 2016-04-13 14:15:24
     * @param date
     * @return
     */
    public static String getMMYear(Date date) {
        if (date == null) {
            return "";
        } else {
            return mmyearSdf.format(date);
        }
    }

    /**
     *
     * 前两月
     *
     * @date 2016-04-12 16:44:07
     * @author yx
     * @param sysTime
     *            系统时间
     * @return 前两月日期
     */
    public static Date getTwoMonth(Date sysTime) {
        try {
            if (null != sysTime) {
                return dateOpinionShow(sysTime, false, true, false, 0, 2, 0);
            } else {
                return dateOpinionShow(new Date(), false, true, false, 0, 2, 0);
            }
        } catch (Exception e) {
            // TODO: handle exception
            return dateOpinionShow(new Date(), false, true, false, 0, 2, 0);
        }
    }
}
