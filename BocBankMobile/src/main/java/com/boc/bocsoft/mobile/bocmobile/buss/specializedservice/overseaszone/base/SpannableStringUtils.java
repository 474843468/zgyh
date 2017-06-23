package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.base;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;

/**
 * 作者：lq7090
 * 创建时间：2016/10/27.
 * 用途：SpannableString 的一些处理
 */
public class SpannableStringUtils {

    /**
     * 设置部分可点击
     *
     * @param s
     * @param l     点击监听
     * @param start 点击部分起点
     * @param end   点击部分终点
     * @return spanableInfo
     */
    public static SpannableString getClickableSpan(String s, View.OnClickListener l, int start, int end) {
        SpannableString spanableInfo = new SpannableString(s);
        spanableInfo.setSpan(new ClickSpan(l), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanableInfo.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanableInfo.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//粗体
        return spanableInfo;
    }

    /**
     * 设置部分可点击及背景
     *
     * @param s
     * @param l     点击监听
     * @param start 起点
     * @param end   终点
     * @param color 背景
     * @return spanableInfo
     */
    public static SpannableString getClickableBackSpan(String s, View.OnClickListener l, int start, int end, int color) {
        SpannableString spanableInfo = new SpannableString(s);

        spanableInfo.setSpan(new ClickSpan(l), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//设置可点击区域
        spanableInfo.setSpan(new BackgroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//为可点击部分设置背景色
        spanableInfo.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//粗体
        return spanableInfo;
    }


    /**
     * 设置部分可点击及字体颜色
     *
     * @param s
     * @param l     点击监听
     * @param start 点击起点
     * @param end   点击终点
     * @param color 字体颜色
     * @return spanableInfo
     */
    public static SpannableString getClickableForeSpan(String s, View.OnClickListener l, int start, int end, int color) {
        SpannableString spanableInfo = new SpannableString(s);

        spanableInfo.setSpan(new ClickSpan(l), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//设置可点击区域
        spanableInfo.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//为可点击部分设置背景色
        spanableInfo.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//粗体
        return spanableInfo;
    }

    /**
     * 设置可点击部分字体颜色
     *
     * @param s
     * @param l      点击监听
     * @param fcolor 字体颜色
     * @param bcolor 背景色
     * @param start  起点
     * @param end    终点
     * @return spanableInfo
     */
    public static SpannableString getClickableBackForeSpan(String s, View.OnClickListener l, int start, int end, int bcolor, int fcolor) {
        SpannableString spanableInfo = new SpannableString(s);

        spanableInfo.setSpan(new ClickSpan(l), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//设置可点击区域
        spanableInfo.setSpan(new BackgroundColorSpan(bcolor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//为可点击部分设置背景色
        spanableInfo.setSpan(new ForegroundColorSpan(fcolor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//为可点击部分设置背景色
        return spanableInfo;
    }


    /**
     * 设置部分字体背景颜色
     *
     * @param s
     * @param color 颜色
     * @param start 起点
     * @param end   终点
     * @return spanableInfo
     */
    public static SpannableString getBackColorSpan(String s, int color, int start, int end) {
        SpannableString spanableInfo = new SpannableString(s);
        spanableInfo.setSpan(new BackgroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//为可点击部分设置背景色
        return spanableInfo;
    }

    /**
     * 设置部分字体颜色
     *
     * @param s
     * @param color 颜色
     * @param start 起点
     * @param end   终点
     * @return spanableInfo
     */
    public static SpannableString getForeColorSpan(String s, int color, int start, int end) {
        SpannableString spanableInfo = new SpannableString(s);
        spanableInfo.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//为可点击部分设置背景色
        return spanableInfo;
    }



    /**
     * @param s     用‘;’ 分割的的多处部分可点击字符串
     * @param l     点击监听
     * @param style 可点击部分字体
     * @param color 可点击部分字体颜色
     * @return spanableInfo
     */
    public static SpannableString getClickableForeSpan(String s, View.OnClickListener l, int style, int color) {
        String[] tmp = s.split(";");
        if (tmp != null && tmp.length == 2) {
            StringBuilder sb = new StringBuilder();
            sb.append(tmp[0]);
            sb.append(tmp[1]);
            SpannableString spanableInfo = new SpannableString(sb);
            spanableInfo.setSpan(new ClickSpan(l), tmp[0].length(), sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//设置可点击区域
            spanableInfo.setSpan(new ForegroundColorSpan(color), tmp[0].length(), sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//为可点击部分设置背景色
            spanableInfo.setSpan(new StyleSpan(style), tmp[0].length(), sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//粗体
            return spanableInfo;
        } else {
            return new SpannableString(s);
        }
    }


    /**
     * @param s     不可点击String
     * @param c     可点击String
     * @param l     点击监听
     * @param style 点击部分字体
     * @param color 点击部分字体颜色
     * @return spanableInfo
     */
    public static SpannableString getClickableForeSpan(String s, String c, View.OnClickListener l, int style, int color) {
        StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(c);
        String st = sb.toString();
        SpannableString spanableInfo = new SpannableString(st);
        spanableInfo.setSpan(new ClickSpan(l), s.length(), sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//设置可点击区域
        spanableInfo.setSpan(new ForegroundColorSpan(color), s.length(), sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//为可点击部分设置背景色
        spanableInfo.setSpan(new StyleSpan(style), s.length(), sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//可点击部分字体
        return spanableInfo;

    }

}

/**
 * 通过此类设定SpannableString 的点击监听
 */
class ClickSpan extends ClickableSpan implements View.OnClickListener {
    private final View.OnClickListener mListener;

    public ClickSpan(View.OnClickListener l) {

        mListener = l;
    }

    @Override
    public void onClick(View v) {

        mListener.onClick(v);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
//        ds.setColor(Color.WHITE);       //设置文件颜色
        ds.setUnderlineText(false);      //设置下划线
    }
}









