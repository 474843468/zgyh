package com.chinamworld.bocmbci.biz.foreign;

import com.chinamworld.bocmbci.utils.StringUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * class 类名&功能
 *
 * @author luqp 2016/11/3
 */
public class StringTools {

    public StringTools() {
    }

    public static String parseOfTheString(String str){
        if (!StringUtil.isNull(str)) {
            StringBuilder sb1 = new StringBuilder(str);
            if (str.contains("+") && str.contains("%")) {
                String currDiffStr = sb1.substring(1, str.length() - 1).toString().trim();
                String currStr = StringTools.parseStringPattern(currDiffStr, 3);
                return "+" + currStr + "%"; //涨跌幅 保留3位小数
            } else if (str.contains("+")) {
                String currDiffStr = sb1.substring(1).toString().trim();
                String currStr = StringTools.parseStringPattern(currDiffStr, 3);
                return "+" + currStr; //涨跌幅 保留3位小数
            } else if (str.contains("%")) {
                String currDiffStr = sb1.substring(0, str.length() - 1).toString().trim();
                String currStr = StringTools.parseStringPattern(currDiffStr, 3);
                return currStr + "%"; //涨跌幅 保留3位小数
            } else {
                return str;
            }
        }
        return "--";
    }

    public static String parseStringPattern(String text, int scale) {
        if (text != null && !"".equals(text) && !"null".equals(text)) {
            if (!text.contains(",") && !text.contains("，")) {
                if (text.matches("^.*[.].*[.].*$")) {
                    return text;
                } else {
                    String temp = "###,###,###,###,###,###,###,##0";
                    if (scale > 0) {
                        temp = temp + ".";
                    }

                    for (int e = 0; e < scale; ++e) {
                        temp = temp + "0";
                    }

                    try {
                        DecimalFormat var6 = new DecimalFormat(temp);
                        BigDecimal d = new BigDecimal(text);
                        return var6.format(d).toString();
                    } catch (Exception var5) {
                        return text;
                    }
                }
            } else {
                return text;
            }
        } else {
            return "--";
        }
    }

    public static String isNullChange(String text) {
        return text != null && text.length() != 0?text:"--";
    }

    /**
     * 先把多余的符号去掉在保留小数位
     * @param text 需要去掉多余符号的字符串 格式如:"+1.12345"或"+1.12345%"或"1.12345%"
     * @return 如果字符串为空返回"--".
     */
    public static String parseStringPattern5(String text){
        if (!StringUtil.isNull(text)) {
            StringBuilder sb1 = new StringBuilder(text);
            if (text.contains("+") && text.contains("%")) {
                text = sb1.substring(1, text.length() - 1).toString().trim();
                text = subZeroAndDot(text); //截取小数点后面的0
                int number = splitStringwith2pointnew(text); // 判断小数位后面几个0
                if(number>=3){
                    text = StringUtil.parseStringPattern(text, 3);
                }
                return "+" + text + "%"; //保留3位小数
            } else if (text.contains("+")) {
                text = sb1.substring(1).toString().trim();
                text = subZeroAndDot(text); //截取小数点后面的0
                int number = splitStringwith2pointnew(text); // 判断小数位后面几个0
                if(number>=3){
                    text = StringUtil.parseStringPattern(text, 3);
                }
                return "+" + text; //保留3位小数
            } else if (text.contains("%")) {
                text = sb1.substring(0, text.length() - 1).toString().trim();
                text = subZeroAndDot(text); //截取小数点后面的0
                int number = splitStringwith2pointnew(text); // 判断小数位后面几个0
                if(number>=3){
                    text = StringUtil.parseStringPattern(text, 3);
                }
                return text + "%"; //保留3位小数
            } else {
                return text;
            }
        }
        return "--";
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     * @param text 小数点后面去掉末尾0 如:1.0011100 去掉尾0后:1.00111
     * @return 返回去掉尾0后的小数
     */
    public static String subZeroAndDot(String text){
        if (StringUtil.isNull(text)){
            return "";
        }
        if(text.indexOf(".") > 0){
            text = text.replaceAll("0+?$", "");//去掉多余的0
            text = text.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return text;
    }

    /**
     * 判断小数位有几位
     * @param text 需要判断的字符串
     * @return 返回小数位后面位数
     */
    public static int splitStringwith2pointnew(String text) {
        if(!"".equals(text) && text.contains(".")) {
            String[] strings = text.split("\\.");
            String after = strings[1];
            return after == null?0:after.length();
        } else {
            return 0;
        }
    }

    public static String valueOf1(String str) {
        return str != null && !"null".equals(str) && !str.equals("")?str:"--";
    }
}
