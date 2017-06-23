package com.boc.bocsoft.mobile.common.utils.date;

import org.threeten.bp.format.DateTimeFormatter;

/**
 * Created by XieDu on 2016/5/28.
 */
public class DateFormatters {

    public static final String DATETIME_FORMAT = "yyyy/MM/dd HH:mm:ss";
    public static final String DATETIME_FORMAT_1 = "yyyy/MM/dd HH:mm";
    public static final String DATE_FORMAT_V2_1 = "yyyy/MM/dd";
    public static final String DATE_FORMAT_V2_2 = "yyyy-MM-dd";
    public static final String DATE_FORMAT_V2_3 = "yyyyMMdd";
    public static final String MONTH_FORMAT_1 = "MM月/yyyy";
    public static final String MONTH_FORMAT_2 = "yyyy/MM";
    public static final String MONTH_DATE_FORMAT1 = "MM-dd";
    public static final String MONTH_DATE_FORMAT2 = "MM/dd";
    public static final String TIME_FORMAT = "HH:mm:ss";

    public static final DateTimeFormatter dateAndTimeFormatter =
            DateTimeFormatter.ofPattern(DATETIME_FORMAT);
    public static final DateTimeFormatter dateAndTimeFormatter1 =
            DateTimeFormatter.ofPattern(DATETIME_FORMAT_1);
    public static final DateTimeFormatter dateFormatter1 =
            DateTimeFormatter.ofPattern(DATE_FORMAT_V2_1);
    public static final DateTimeFormatter dateFormatter2 =
            DateTimeFormatter.ofPattern(DATE_FORMAT_V2_2);
    public static final DateTimeFormatter dateFormatter3 =
            DateTimeFormatter.ofPattern(DATE_FORMAT_V2_3);
    public static final DateTimeFormatter monthFormatter1 =
            DateTimeFormatter.ofPattern(MONTH_FORMAT_1);
    public static final DateTimeFormatter monthFormatter2 =
            DateTimeFormatter.ofPattern(MONTH_FORMAT_2);
    public static final DateTimeFormatter monthDateFormatter1 =
            DateTimeFormatter.ofPattern(MONTH_DATE_FORMAT1);
    public static final DateTimeFormatter monthDateFormatter2 =
            DateTimeFormatter.ofPattern(MONTH_DATE_FORMAT2);
    public static final DateTimeFormatter timeFormatter =
            DateTimeFormatter.ofPattern(TIME_FORMAT);

    public static final DateTimeFormatter[] DATE_TIME_FORMATTERS = {
            dateAndTimeFormatter, dateAndTimeFormatter1, dateFormatter1, dateFormatter2,
            dateFormatter3, monthFormatter1, monthFormatter2
    };
}
