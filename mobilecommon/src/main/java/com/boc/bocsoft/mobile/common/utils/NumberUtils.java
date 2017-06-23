package com.boc.bocsoft.mobile.common.utils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 号码类工具，实现检测格式、格式化等功能
 * Created by XieDu on 2016/5/23.
 */
public class NumberUtils {

    /**
     * 将手机号格式化成XXX****XXXX的形式
     *
     * @param number 手机号，请调用者保证参数为11位手机号
     * @return 格式化后的手机号
     */
    public static String formatMobileNumberWithAsterrisk(String number) {

        if (!checkMobileNumber(number)) {
            return number;
        }
        return number.replaceAll("(\\d{3})\\d+(\\d{4})", "$1****$2");
    }

    /**
     * 将手机号格式化成XXX  XXXX  XXXX的形式
     *
     * @param number 手机号，请调用者保证参数为11位手机号
     * @return 格式化后的手机号
     */
    public static String formatMobileNumber(String number) {

        if (!checkMobileNumber(number)) {
            return number;
        }
        StringBuilder numSB = new StringBuilder(number);
        return numSB.insert(7, " ").insert(3, " ").toString();
    }

    /**
     * 检测是否为11位手机号字符串
     *
     * @param number 手机号
     * @return 检测结果
     */
    public static boolean checkMobileNumber(String number) {
        if (StringUtils.isEmpty(number)) {
            return false;
        }
        Pattern pattern = Pattern.compile("\\b\\d{11}\\b");
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    /**
     * 将卡号格式化成XXXX ****** XXXX的形式
     *
     * @param cardNumber 卡号
     * @return 格式化后的卡号
     */
    public static String formatCardNumber(String cardNumber) {
        if (!checkCardNumber(cardNumber)) {
            return cardNumber;
        }
        return cardNumber.replaceAll("(\\d{4})\\d+(\\d{4})", "$1 ****** $2");
    }

    /**
     * 将卡号格式化成XXXX ****** XXXX的形式(增强版)
     * author:zhx
     *
     * @param cardNumber 卡号
     * @return 格式化后的卡号
     */
    public static String formatCardNumberStrong(String cardNumber) {
        if (StringUtils.isEmpty(cardNumber)) {
            return "";
        }

        if(cardNumber.length() < 5) {
            return cardNumber;
        }

        String start = cardNumber.substring(0, 4); // 此处可能会崩溃
        String end = cardNumber.substring(cardNumber.length() - 4, cardNumber.length());

        String formatCardNumber = start + " ****** " + end;
        return formatCardNumber;
    }

    /**
     * 将卡号格式化成XXXX XXXX XXXX XXXX的形式
     *
     * @param cardNumber 卡号
     * @return 格式化后的卡号
     * <p/>
     */
    public static String formatCardNumber2(String cardNumber) {
        if (StringUtils.isEmpty(cardNumber)) {
            return "";
        }
        String rex = "(\\d{4})";
        return cardNumber.replaceAll(rex, "$1 ");
    }

    /**
     * 检测是否为12到19位数字卡号字符串(活期账户是12位)
     *
     * @param number 卡号
     * @return 检测结果
     */
    public static boolean checkCardNumber(String number) {
        if (StringUtils.isEmpty(number)) {
            return false;
        }
        Pattern pattern = Pattern.compile("\\b\\d{12,19}\\b");
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    /**
     * 6********4 格式化账号( 6 8 4类型的)
     *
     * @param nativeString 要格式化的身份证号
     * @return 格式化后的身份证号
     */
    public static String formatCardFor6Eight4(String nativeString) {
        if (nativeString == null) {
            return "-";
        }
        if (nativeString.length() < 10) {
            // 长度小于10直接返回，避免越界
            return nativeString;
        }
        StringBuffer sbShow = new StringBuffer(nativeString.subSequence(0, 6));
        int otherLen = nativeString.length() - 10;
        for (int i = 0; i < otherLen; i++) {
            sbShow.append("*");
        }
        sbShow.append(nativeString.substring(nativeString.length() - 4,
                nativeString.length()));
        return sbShow.toString();
    }


    /**
     * 将显示的条形码内容格式化成 XXXX XXXX XXXX XXXXXX的形式
     * 仅用于格式化条形码
     * @param number
     * @return
     */
    public static String formatBarCodeNumber(String number) {
        if (StringUtils.isEmpty(number)){
            return "";
        }
        StringBuilder numSB = new StringBuilder(number);
        if (number.length() <= 8){
            return number;
        }
        if (number.length() <= 12){
            return numSB.insert(8, " ").insert(4, " ").toString();
        }
        if (number.length() <= 16){
            return numSB.insert(12, " ").insert(8, " ").insert(4, " ").toString();
        }
        return numSB.insert(16, " ").insert(12, " ").insert(8, " ").insert(4, " ").toString();
    }

    /**
     * 随着手机号码的输入长度从1到11位，动态地格式化手机号码
     *
     * @param mobile
     * @return
     */
    public static String formatMobileNumberDynamically(String mobile) {
        String str = null;

        // 第1种情况，传入的字符串为空
        if (StringUtils.isEmpty(mobile)) {
            str = mobile;
        } else {
            mobile = mobile.trim().replace(" ", "");
            if (mobile.length() <= 3) {
                str = mobile;
            } else if (mobile.length() > 3 && mobile.length() <= 7) {
                StringBuffer buffer = new StringBuffer(mobile);
                buffer = buffer.insert(3, " ");
                str = buffer.toString();
            } else if (mobile.length() > 7) {
                StringBuffer buffer = new StringBuffer(mobile);
                buffer = buffer.insert(3, " ").insert(8, " ");
                str = buffer.toString();
            }
        }

        return str;
    }
//    ==================605-合并606 新增方法====2016-09-14 09:37:32==
    /**
     * 将身份证格式化成X **************** X的形式
     * author:cq
     *
     * @param idNumber 卡号
     * @return 格式化后的身份证
     */
    public static String formatIDNumber(String idNumber) {
        if (StringUtils.isEmpty(idNumber)) {
            return "";
        }
        String start = idNumber.substring(0, 1);
        String end = idNumber.substring(idNumber.length() - 1, idNumber.length());

        String formatIDNumber = start + " **************** " + end;
        return formatIDNumber;
    }

    /**
     * BidDecimal 的值为空时，用默认值替代
     * @param value
     * @param defaultValue
     * @return
     */
    public static String nvlBigDecimal(BigDecimal value, String defaultValue){
        if (value == null) {
            return defaultValue;
        } else {
            return value.toString();
        }
    }

    /**
     * 将卡号格式化成XXXX ****** XXXX的形式(增强版)
     * 当卡号小于等于8时，直接返回，大于8时，格式化
     * author:cff
     *
     * @param cardNumber 卡号
     * @return 格式化后的卡号
     */
    public static String formatStringNumber(String cardNumber) {
        try {
            if (StringUtils.isEmptyOrNull(cardNumber)) {
                return "";
            }

            if(cardNumber.length() <= 8) {
                return cardNumber;
            }

            String start = cardNumber.substring(0, 4);
            String end = cardNumber.substring(cardNumber.length() - 4, cardNumber.length());

            String formatCardNumber = start + " ****** " + end;
            return formatCardNumber;
        } catch (Exception e) {
            return cardNumber;
        }

    }

    /**
     * 检查输入数据是否溢出
     * @param num 输入的数字
     * @param maxLeftNumber 小数点左侧位数
     * @param maxRightNumber 小数点右侧位数
     * @return
     */
    public static boolean checkNumberLength(String num, int maxLeftNumber, int maxRightNumber) {
        boolean ret = true;
        String left = "";
        String right = "";
        int index = num.indexOf('.');
        if (index > 0) {
            left = num.substring(0, num.indexOf('.'));
            if (index < num.length() - 1) {
                right = num.substring(num.indexOf('.') + 1);
            }
        } else {
            left = num;
        }

        if (left.length() > maxLeftNumber) {
            char point = num.charAt(maxLeftNumber);
            if (point != '.') {
                ret = false;
            } else if (right.length() > maxRightNumber) {
                ret = false;
            }
        } else if (right.length() > maxRightNumber) {
            ret = false;
        }
        return ret;
    }
}
