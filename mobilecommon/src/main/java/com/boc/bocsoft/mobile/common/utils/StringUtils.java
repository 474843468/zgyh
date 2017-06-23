package com.boc.bocsoft.mobile.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by XieDu on 2016/5/18.
 */
public class StringUtils {

    /**
     * 判断字符串是否为空
     *
     * @param string 待判断的字符串
     * @return 是否为空
     */
    public static boolean isEmpty(String string) {
        return null == string || string.isEmpty();
    }

    /**
     * 判断字符串是否为空或为"null"字符串
     */
    public static boolean isEmptyOrNull(String str) {
        return isEmpty(str) || "null".equals(str.toLowerCase());
    }

    /**
     * 返回字符串是否包含全角字符
     *
     * @return true 包含 false 不包含
     */
    public static boolean isContainFullWidthChar(String str) {
        if (isEmpty(str)) {
            return false;
        }
        Matcher matcher = Pattern.compile("[\uFF00-\uFF19\uFF21-\uFF3A\uFF41-\uFF5A]").matcher(str);
        while (matcher.find()) {
            return true;
        }
        return false;
    }

    /**
     * 是否为合法的别名(别名：请输入半角英文、半角数字、中文，可包含_，最长20个字符)
     *
     * @param alias 别名
     * @return 检测结果
     */
    public static boolean checkAlias(String alias) {
        if (StringUtils.isEmpty(alias)) {
            return false;
        }

        return Pattern.matches("[a-zA-Z0-9\u4E00-\u9FFF_]{0,20}", alias);
    }

    /**
     * 获取字符串长度（一个汉字占2个字符）
     */
    public static int getStringLength(String s) {
        if (StringUtils.isEmpty(s)) {
            return 0;
        }
        char[] chs = s.toCharArray();
        int count = 0;
        for (int i = 0; i < chs.length; i++) {
            if ((chs[i] + "").getBytes().length >= 2) {
                count += 2;
            } else {
                count++;
            }
        }
        return count;
    }

    public static String subZeroAndDot(String s) {
        if (isEmpty(s)) {
            return "";
        }
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }
}
