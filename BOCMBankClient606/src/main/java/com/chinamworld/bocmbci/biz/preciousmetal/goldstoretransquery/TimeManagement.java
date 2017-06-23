package com.chinamworld.bocmbci.biz.preciousmetal.goldstoretransquery;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by linyl on 2016/9/13.
 */
public class TimeManagement {
    /**
     * 返回 yyyy-MM-dd
     *
     * @param
     * @return 返回 yyyy-MM-dd
     * @throws ParseException
     */
    public static String exchangeStringDate(String date) throws ParseException {
        if (date != null && date.length() > 10) {
            String result = date.substring(0, 10);
            return result;
        }else{
            return null;
        }

    }

    /**
     * 返回HH:mm:ss
     *
     * @param
     * @return 返回HH:mm:ss
     * @throws ParseException
     */
    public static String exchangeStringTime(String date) throws ParseException {
        if (date != null && date.length() > 10) {
            String result = date.substring(10, date.length());
            return result;
        }else{
            return null;
        }
    }

    /**
     * 根据日期 返回 星期
     */
    public static String getWeekOfDate(String date) throws ParseException{
        String[] weekDays = {"周日","周一","周二","周三","周四","周五","周六"};
        DateFormat df =new SimpleDateFormat("yyyy/MM/dd");
        Date dt = df.parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 根据日期 返回年月 上送格式 2016/8/10
     *  返回格式  8月/2016
     */
    public static String getYearAndMonth(String date){
        String str = null;
        // 年
        String year = date.substring(0, 4);
        // 月
        String month = date.substring(5, 7);

        str = month+"月/"+year;
        return str;
    }

    /**
     * 根据日期 返回年月 上送格式 2016/8/10
     *  返回格式  10
     */
    public static String getDay(String date){
        // 日
        String day = date.substring(8, 10);
        return day;
    }



}
