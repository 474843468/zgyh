package com.boc.bocsoft.mobile.bocmobile.base.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

/**
 * 调用系统日期控件
 * Created by niuguobin on 2016/6/3.
 */
public class DateTimePicker {
    private static String time;

    /**
     * 获取日期
     *
     * @param context
     * @param changeView:显示日期的TextView控件
     */
    public static void showDatePick(final Context context, final TextView changeView) {
        try {
            final LocalDate date = LocalDate.now();
            time = date.format(DateFormatters.dateFormatter2);
            new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    time = date.of(year, monthOfYear + 1, dayOfMonth).format(DateFormatters.dateFormatter2);
                    changeView.setText(time);
                }
            }, date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth()).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取日期
     *
     * @param context
     * @param dateTimeFormatter 需要的日期格式
     * @param datePickCallBack  点击完成按钮后的回调
     */

    public static void showDatePick(final Context context, final DateTimeFormatter dateTimeFormatter, final DatePickCallBack datePickCallBack) {
        try {
            final LocalDate currentDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();
            new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    LocalDate choiceDate = currentDate.of(year, monthOfYear + 1, dayOfMonth);
                    time = choiceDate.format(dateTimeFormatter);
                    datePickCallBack.onChoiceDateSet(time, choiceDate);
                }
            }, currentDate.getYear(), currentDate.getMonthValue() - 1, currentDate.getDayOfMonth()).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取日期
     *
     * @param context
     * @param currentDate       初始化的日期
     * @param dateTimeFormatter 需要的日期格式
     * @param datePickCallBack  点击完成按钮后的回调
     */
    public static void showDatePick(final Context context, final LocalDate currentDate, final DateTimeFormatter dateTimeFormatter, final DatePickCallBack datePickCallBack) {
        try {
            new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    LocalDate choiceDate = currentDate.of(year, monthOfYear + 1, dayOfMonth);
                    time = choiceDate.format(dateTimeFormatter);
                    datePickCallBack.onChoiceDateSet(time, choiceDate);
                }
            }, currentDate.getYear(), currentDate.getMonthValue() - 1, currentDate.getDayOfMonth()).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取两个时间范围内选择的时间值{可以控制区间}
     *
     * @param context           上下文对象
     * @param currentDate       当前系统日期
     * @param minDate           最小时间选择
     * @param maxDate           最大时间选择
     * @param dateTimeFormatter 格式
     * @param datePickCallBack  回调
     */
    public static void showRangeDatePick(final Context context, final LocalDate currentDate, long minDate, long maxDate, final DateTimeFormatter dateTimeFormatter, final DatePickCallBack datePickCallBack) {
        try {
            DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    LocalDate choiceDate = currentDate.of(year, monthOfYear + 1, dayOfMonth);
                    time = choiceDate.format(dateTimeFormatter);
                    datePickCallBack.onChoiceDateSet(time, choiceDate);
                }
            }, currentDate.getYear(), currentDate.getMonthValue() - 1, currentDate.getDayOfMonth());

            if (minDate > 0)
                dialog.getDatePicker().setMinDate(minDate);
            if (maxDate > 0)
                dialog.getDatePicker().setMaxDate(maxDate);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public interface DatePickCallBack {
        void onChoiceDateSet(String strChoiceTime, LocalDate localDate);
    }


    /**
     * 获取日期
     * 返回用户选择日期
     * 格式：yyyy-MM-dd
     *
     * @param context
     */
    public static String showDataPick(final Context context) {
        try {
            final LocalDate date = LocalDate.now();
            time = date.format(DateFormatters.dateFormatter2);
            new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    time = date.of(year, monthOfYear + 1, dayOfMonth).format(DateFormatters.dateFormatter2);
                }
            }, date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth()).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

}
