package com.boc.bocsoft.mobile.bocmobile.base.utils;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 金额工具类
 *
 * @author xianwei
 */
public class MoneyUtils {

    public final static DecimalFormat format_nopoint = new DecimalFormat("###,##0");
    public final static DecimalFormat format_rmb = new DecimalFormat("###,##0.00");
    public final static DecimalFormat format_rmb_financial_net_value =
            new DecimalFormat("###,##0.0000");

    /**
     * 无精度损失的金额格式化
     */
    public static String transMoneyFormatNoLossAccuracy(String money, String currency) {
        if (StringUtils.isEmpty(money)) {
            return "";
        }
        BigDecimal bigDecimal;
        try {
            bigDecimal = new BigDecimal(money);
        } catch (NumberFormatException exp) {
            return money;
        }
        return transMoneyFormatNoLossAccuracy(bigDecimal, currency);
    }

    public static String transMoneyFormatNoLossAccuracy(BigDecimal money, String currency) {
        if (money == null) {
            return "";
        }

        //非日元币种先格式化整数部分，再格式化小数部分
        money = money.stripTrailingZeros();
        BigInteger integerValue = money.toBigInteger();
        BigDecimal fractionalValue =
                money.subtract(new BigDecimal(integerValue)).stripTrailingZeros();

        //日元币种直接格式化
        if (fractionalValue.compareTo(BigDecimal.ZERO) == 0 && isCurrencyCodeNoPoint(currency)) {
            return format_nopoint.format(money);
        }
        //整数部分格式化
        String integerFormatString = format_nopoint.format(integerValue);
        String fractionValueFormatString = fractionalValue.toPlainString();
        //如果不为日元且0.XX的小数部分长度小于2，即整个小数字符串长度小于4，需要补0至2位
        if (!isCurrencyCodeNoPoint(currency) && fractionValueFormatString.length() < 4) {
            fractionValueFormatString = new DecimalFormat("0.00").format(fractionalValue);
        }
        fractionValueFormatString = fractionValueFormatString.substring(1);
        return integerFormatString + fractionValueFormatString;
    }

    /**
     * 格式化金额，超过小数最大位数的话，默认四舍五入
     *
     * @param money 待格式化的金额
     * @param maxIntegerDigits 整数最大位数
     * @param maxFractionDigits 小数最大位数
     * @param tripZero 是否滤0。如果为true,格式化后的金额尾部的0将会被去除；如果为false,则末尾的0将会被添加至小数最大位数
     */
    public static String transMoneyFormat(String money, int maxIntegerDigits, int maxFractionDigits,
            boolean tripZero) {
        return transMoneyFormat(money, RoundingMode.HALF_UP, maxIntegerDigits, maxFractionDigits,
                tripZero);
    }

    /**
     * 格式化金额，超过小数最大位数的话，按照指定roundingMode来处理
     *
     * @param money 待格式化的金额
     * @param roundingMode 小数位数过多时的规则
     * @param maxIntegerDigits 整数最大位数
     * @param maxFractionDigits 小数最大位数
     * @param tripZero 是否滤0。如果为true,格式化后的金额尾部的0将会被去除；如果为false,则末尾的0将会被添加至小数最大位数
     */
    public static String transMoneyFormat(String money, RoundingMode roundingMode,
            int maxIntegerDigits, int maxFractionDigits, boolean tripZero) {
        if (StringUtils.isEmpty(money)) {
            return "";
        }
        try {
            double moneyNumber = Double.parseDouble(money);
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setRoundingMode(roundingMode);
            numberFormat.setMaximumIntegerDigits(maxIntegerDigits);
            numberFormat.setMaximumFractionDigits(maxFractionDigits);
            if (!tripZero) {
                numberFormat.setMinimumFractionDigits(maxFractionDigits);
            }
            return numberFormat.format(moneyNumber);
        } catch (NumberFormatException exp) {
            return money;
        }
    }

    /**
     * 将传入金额保留小数点后两位，四舍五入，没有小数的加.00.如果是日元则没有小数
     *
     * @param money money数字符串
     * @param currency 币种code
     */

    public static String transMoneyFormat(String money, String currency) {
        if ("null".equals(money)) {
            return "-";
        }
        if (StringUtils.isEmpty(money)) {
            return "";
        }
        // 逻辑 ： 日元不带小数位置 其他保留两位小数 整数部分 3位一个千分
        BigDecimal bigDecimal;
        try {
            bigDecimal = new BigDecimal(money);
        } catch (NumberFormatException exp) {
            return money;
        }
        return transMoneyFormat(bigDecimal, currency);
    }

    public static String transMoneyFormat(BigDecimal money, String currency) {
        if (money == null) {
            return "";
        }
        DecimalFormat format;
        if (isCurrencyCodeNoPoint(currency)) {
            format = format_nopoint;
        } else {
            format = format_rmb;
        }
        format.setRoundingMode(RoundingMode.HALF_UP);//格式化舍弃多余小数时四舍五入
        try {
            return format.format(money);
        } catch (IllegalArgumentException exp) {
            return money.toPlainString();
        }
    }

    /**
     * 理财净值型-单位净值显示规则  小数点4位数
     *
     * @author yx
     */
    public static String financialNetValueTransMoneyFormat(String money, String currency) {
        if ("null".equals(money)) {
            return "-";
        }
        if (StringUtils.isEmpty(money)) {
            return "";
        }
        // 逻辑 ： 日元不带小数位置 其他保留四位小数 整数部分 3位一个千分
        BigDecimal bigDecimal;
        try {
            bigDecimal = new BigDecimal(money);
        } catch (NumberFormatException exp) {
            return money;
        }
        return financialNetValueTransMoneyFormat(bigDecimal, currency);
    }

    /**
     * 理财净值型-单位净值显示规则  小数点4位数
     *
     * @author yx
     */
    public static String financialNetValueTransMoneyFormat(BigDecimal money, String currency) {
        if (money == null) {
            return "";
        }
        DecimalFormat format;
        if (isCurrencyCodeNoPoint(currency)) {
            format = format_nopoint;
        } else {
            format = format_rmb_financial_net_value;
        }
        format.setRoundingMode(RoundingMode.HALF_UP);//格式化舍弃多余小数时四舍五入
        try {
            return format.format(money);
        } catch (IllegalArgumentException exp) {
            return money.toPlainString();
        }
    }

    /**
     * 将传入金额保留小数点后两位，没有小数的加.00.如果是日元则没有小数,多余的四舍五入
     *
     * @param money money数字符串
     * @param currency 币种code
     */

    public static String transRoundMoneyFormat(String money, String currency) {
        if ("null".equals(money)) {
            return "-";
        }
        if (StringUtils.isEmpty(money)) {
            return "";
        }
        // 逻辑 ： 日元不带小数位置 其他保留两位小数 整数部分 3位一个千分
        BigDecimal bigDecimal;
        try {
            bigDecimal = new BigDecimal(money);
        } catch (NumberFormatException exp) {
            return money;
        }
        return transRoundMoneyFormat(bigDecimal, currency);
    }

    /**
     * 金额格式化-四舍五人
     */
    public static String transRoundMoneyFormat(BigDecimal money, String currency) {
        if (money == null) {
            return "";
        }
        DecimalFormat format;
        if (isCurrencyCodeNoPoint(currency)) {
            format = format_nopoint;
        } else {
            format = format_rmb;
        }
        format.setRoundingMode(RoundingMode.HALF_UP);//格式化舍弃多余小数时四舍五入
        try {
            return format.format(money);
        } catch (IllegalArgumentException exp) {
            return money.toPlainString();
        }
    }

    public static String getNormalMoneyFormat(String money) {
        if (StringUtils.isEmptyOrNull(money)) {
            return "";
        }
        return money.replace(",", "");
    }

    public static boolean isZero(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        //
        double tmp = Double.parseDouble(str);
        return tmp != 0.0;
    }

    /**
     * 获取指定小数位精度的数
     *
     * @param round 指定小数位数
     */
    public static String getRoundNumber(String number, int round) {
        BigDecimal big;
        if (StringUtils.isEmpty(number)) {
            return "";
        }
        try {
            big = new BigDecimal(number);
            big = big.setScale(round, BigDecimal.ROUND_DOWN);
        } catch (NumberFormatException e) {
            return "";
        }
        return big.toPlainString();
    }

    /**
     * 获取指定小数位精度的数
     *
     * @param round 指定小数位数
     */
    public static String getRoundNumber(String number, int round, int roundingMode) {
        BigDecimal big;
        if (StringUtils.isEmpty(number)) {
            return "";
        }
        try {
            big = new BigDecimal(number);
            big = big.setScale(round, roundingMode);
        } catch (NumberFormatException e) {
            return "";
        }
        return big.toPlainString();
    }

    /**
     * 将利率直接舍弃末尾的零
     */
    public static String transRateFormat(String rate) {
        return StringUtils.subZeroAndDot(rate);
    }

    /**
     * 将利率格式化成百分比两位小数的形式——1.23%
     */
    public static String transRatePercentTypeFormat(String rate) {
        if (StringUtils.isEmpty(rate) || StringUtils.isEmpty(rate.trim())) {
            return "";
        }
        String rateForFormat;
        StringBuilder result = new StringBuilder();
        if (rate.endsWith("%")) {
            rateForFormat = rate.substring(0, rate.lastIndexOf("%"));
            result.append(getRoundNumber(rateForFormat, 2)).append("%");
        } else {
            try {
                BigDecimal big = new BigDecimal(rate);
                result.append(big.setScale(2, BigDecimal.ROUND_DOWN).toPlainString()).append("%");
            } catch (NumberFormatException e) {
                return "";
            }
        }
        return result.toString();
    }

    /**
     * 将利率格式化成百分比的形式——1.23456%，小数不舍弃
     */
    public static String transRatePercentTypeFormatTotal(String rate) {
        if (StringUtils.isEmpty(rate)) {
            return rate;
        }
        return rate.endsWith("%") ? rate : rate + "%";
    }

    /**
     * 贷款额度显示
     *
     * @param loanAmount 贷款额度
     * @return 如果小于一万，显示XX元。
     * 如果不小于一万，且显示成万后不多于两位小数，则显示XX.XX万。小数部分过滤掉最后面的0
     * 如果不小于一万，且显示成万后多于两位小数，则显示XX元
     */
    public static String getLoanAmountShownRMB(String loanAmount) {
        if (StringUtils.isEmpty(loanAmount)) {
            return loanAmount;
        }
        try {
            BigDecimal money = new BigDecimal(loanAmount);
            return getLoanAmountShownRMB(money);
        } catch (Exception e) {
            return "";
        }
    }

    // 中银理财（交易查询和撤单用）
    public static String getLoanAmountShownRMB1(String loanAmount, String currency) {
        if (StringUtils.isEmpty(loanAmount)) {
            return loanAmount;
        }
        try {
            BigDecimal money = new BigDecimal(loanAmount);
            return getLoanAmountShownRMB1(money, currency);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getLoanAmountShownRMB(BigDecimal money) {
        if (money == null) {
            return "";
        }
        BigDecimal wan = new BigDecimal(10000);
        //小于1万显示为元
        if (money.compareTo(wan) < 0) {
            return transMoneyFormat(money, ApplicationConst.CURRENCY_CNY);
        }
        //大于1万时
        BigDecimal divideResult = money.movePointLeft(4).stripTrailingZeros();
        BigInteger integerValue = divideResult.toBigInteger();
        String fractionalValue = divideResult.subtract(new BigDecimal(integerValue))
                                             .stripTrailingZeros()
                                             .toPlainString();
        //如果0.XX的小数部分长度超过2，即0.XX的长度超过4，则显示为元
        if (fractionalValue.length() > 4) {
            return transMoneyFormat(money, ApplicationConst.CURRENCY_CNY);
        }
        return divideResult.toPlainString() + "万";
    }

    public static String convertNumberWanOrYi(String number) {
        BigDecimal bigDecimal;
        try {
            bigDecimal = new BigDecimal(number);
        } catch (NumberFormatException exp) {
            return number;
        }
        return convertNumberWanOrYi(bigDecimal);
    }

    public static String convertNumberWanOrYi(BigDecimal number) {
        String[] result = convertNumberWanOrYiSplit(number);
        if (result == null) {
            return "";
        }
        if (result.length == 1) {
            return result[0];
        } else {
            return result[0] + result[1];
        }
    }

    public static String[] convertNumberWanOrYiSplit(String number) {
        BigDecimal bigDecimal;
        try {
            bigDecimal = new BigDecimal(number);
        } catch (NumberFormatException exp) {
            return null;
        }
        return convertNumberWanOrYiSplit(bigDecimal);
    }

    public static String[] convertNumberWanOrYiSplit(BigDecimal number) {
        String[] result = null;
        if (number == null) {
            return result;
        }
        DecimalFormat format = format_rmb;
        format.setRoundingMode(RoundingMode.DOWN);
        BigDecimal wan = new BigDecimal(10000);
        BigDecimal yi = new BigDecimal(100000000);
        //小于1万显示为元
        if (number.compareTo(wan) < 0) {
            result = new String[1];
            result[0] = StringUtils.subZeroAndDot(format.format(number));
            return result;
        }
        result = new String[2];
        int pointLeft;
        String unit;
        if (number.compareTo(wan) >= 0 && number.compareTo(yi) < 0) {
            pointLeft = 4;
            unit = "万";
        } else {
            pointLeft = 8;
            unit = "亿";
        }
        result[0] = StringUtils.subZeroAndDot(format.format(number.movePointLeft(pointLeft)));
        result[1] = unit;
        return result;
    }

    // 中银理财（交易查询和撤单用）
    public static String getLoanAmountShownRMB1(BigDecimal money, String currency) {
        if (money == null) {
            return "";
        }
        BigDecimal wan = new BigDecimal(10000);
        //小于1万显示为元
        if (money.compareTo(wan) < 0) {
            return transMoneyFormat(money, currency);
        }
        //大于1万时
        BigDecimal divideResult = money.movePointLeft(4).stripTrailingZeros();
        BigInteger integerValue = divideResult.toBigInteger();
        String fractionalValue = divideResult.subtract(new BigDecimal(integerValue))
                                             .stripTrailingZeros()
                                             .toPlainString();
        //如果0.XX的小数部分长度超过2，即0.XX的长度超过4，则显示为元
        if (fractionalValue.length() > 4) {
            return transMoneyFormat(money, currency);
        }
        String str = divideResult.toPlainString();
        int i = str.indexOf('.');
        int pointRightLength = str.substring(i + 1).length();
        String buwei;
        if (pointRightLength == 1) {
            if (i != -1) {
                buwei = "0";
            } else {
                buwei = "";
            }
        } else {
            buwei = "";
        }
        return divideResult.toPlainString() + buwei + "万";
    }

    /**
     * 除以100，转换成BigDecimal
     */
    public static BigDecimal transRate(String rate) {
        if (StringUtils.isEmpty(rate)) {
            return null;
        }
        return new BigDecimal(rate).movePointLeft(2);
    }

    /**
     * 比较两个数值的大小
     *
     * @return -1表示小于；0代表等于，1代表大于；-5代表传递的值为“”或者null;-10代表转换格式异常
     */
    public static int compareTo(String v1, String v2) {
        if (StringUtils.isEmpty(v1) && StringUtils.isEmpty(v2)) {
            return -5;
        }
        BigDecimal b1, b2;
        try {
            b1 = new BigDecimal(v1);
            b2 = new BigDecimal(v2);
        } catch (Exception e) {
            e.printStackTrace();
            return -10;
        }

        return b1.compareTo(b2);
    }

    /**
     * 大于10000转为1万，不保留小数
     */
    public static String formatMoney(String money) {
        if (StringUtils.isEmptyOrNull(money)) {
            return "";
        }
        String str = new BigDecimal(money).toPlainString();
        BigDecimal value = new BigDecimal(str);
        BigDecimal limit = BigDecimal.valueOf(10000);
        if (value.compareTo(limit) == 1) {
            return value.divide(limit, 0, BigDecimal.ROUND_DOWN).toPlainString() + "万";
        } else {
            return value.setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
        }
    }

    public static boolean isCurrencyCodeNoPoint(String currencyCode) {
        return ApplicationConst.currencyCodeNoPoint.contains(currencyCode);
    }

    /**
     * 去掉金额前后的0
     * 00123.450>>123.45
     */
    public static String trimAmountZero(String amount) {
        String noFormatAmount = getNormalMoneyFormat(amount);
        if (StringUtils.isEmpty(noFormatAmount)) {
            return "";
        }
        if (new BigDecimal(noFormatAmount).compareTo(new BigDecimal("0")) == 0) {
            return "0";
        }
        return new BigDecimal(noFormatAmount).stripTrailingZeros().toPlainString();
    }
}
