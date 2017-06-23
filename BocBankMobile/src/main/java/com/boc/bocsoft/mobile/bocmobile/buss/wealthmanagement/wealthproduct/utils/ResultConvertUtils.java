package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.utils;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.model.PurchaseModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.WealthConst;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthDetailsBean;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;

/**
 * 响应字段的转化
 * Created by liuweidong on 2016/10/8.
 */
public class ResultConvertUtils {

    /**
     * 产品类型
     */
    public static String convertProductType(String productType) {
        String convertStr = "";
        if (productType == null) {
            return "";
        }
        switch (productType) {
            case WealthConst.PRODUCT_TYPE_1:
                convertStr = "现金管理类产品";
                break;
            case WealthConst.PRODUCT_TYPE_2:
                convertStr = "净值开放类产品";
                break;
            case WealthConst.PRODUCT_TYPE_3:
                convertStr = "固定期限产品";
                break;
            default:
                break;
        }
        return convertStr;
    }

    /**
     * 利率
     *
     * @param rate
     * @param rateMax
     * @return
     */
    public static String convertRate(String rate, String rateMax) {
        String convertStr = "";
        BigDecimal bigDecimal = new BigDecimal(rateMax);
        BigDecimal bigDecimal0 = new BigDecimal("0");
        if (StringUtils.isEmptyOrNull(rateMax) || bigDecimal0.compareTo(bigDecimal) == 0) {
            convertStr = rate + "%";
        } else {
            convertStr = rate + "-" + rateMax + "%";
        }
        return convertStr;
    }

    /**
     * 产品期限
     *
     * @param date
     * @param isLockPeriod
     * @param termType
     * @return
     */
    public static String convertDate(String productKind, String date, String isLockPeriod, String termType) {
        String convertStr = "";
        if ("0".equals(productKind) && !StringUtils.isEmptyOrNull(isLockPeriod) && !WealthConst.IS_LOCK_PERIOD_0.equals(isLockPeriod)) {// 业绩基准（结构性理财产品）
            if (!StringUtils.isEmptyOrNull(date)) {
                if (WealthConst.IS_LOCK_PERIOD_1.equals(isLockPeriod)) {// 业绩基准-锁定期转低收益
                    convertStr = "最低持有" + date + "天";
                } else {// 业绩基准-锁定期后入账 业绩基准-锁定期周期滚续
                    convertStr = date + "天";
                }
            }
        } else if (WealthConst.TERM_TYPE_3.equals(termType)) {// 无限开放式
            convertStr = "无固定期限";
        } else {
            if (!StringUtils.isEmptyOrNull(date)) {
                convertStr = date + "天";
            }
        }
        return convertStr;
    }

    /**
     * 产品销售状态
     */
    public static String convertProductStatus(String status) {
        String convertStr = "";
        if (status == null) {
            return "";
        }
        switch (status) {
            case WealthConst.PRODUCT_STA_1:
                convertStr = "在售产品";
                break;
            case WealthConst.PRODUCT_STA_2:
                convertStr = "停售产品";
                break;
            default:
                break;
        }
        return convertStr;
    }

    /**
     * 风险级别
     *
     * @return
     */
    public static String convertRiskLevel(String riskLevel) {
        String convertStr = "";
        if (riskLevel == null) {
            return "";
        }
        switch (riskLevel) {
            case WealthConst.RISK_LEVEL_0:
                convertStr = "低风险";
                break;
            case WealthConst.RISK_LEVEL_1:
                convertStr = "中低风险";
                break;
            case WealthConst.RISK_LEVEL_2:
                convertStr = "中等风险";
                break;
            case WealthConst.RISK_LEVEL_3:
                convertStr = "中高风险";
                break;
            case WealthConst.RISK_LEVEL_4:
                convertStr = "高风险";
                break;
            default:
                break;
        }
        return convertStr;
    }

    /**
     * 风险级别
     *
     * @return
     */
    public static String convertRiskLevelNew(String riskLevel) {
        String convertStr = "";
        if (riskLevel == null) {
            return "";
        }
        switch (riskLevel) {
            case WealthConst.RISK_LEVEL_0:
                convertStr = "低风险";
                break;
            case WealthConst.RISK_LEVEL_1:
                convertStr = "风险等级为稳健型（含）以上";
                break;
            case WealthConst.RISK_LEVEL_2:
                convertStr = "风险等级为平衡型（含）以上";
                break;
            case WealthConst.RISK_LEVEL_3:
                convertStr = "风险等级为成长型（含）以上";
                break;
            case WealthConst.RISK_LEVEL_4:
                convertStr = "风险等级为进取型";
                break;
            default:
                break;
        }
        return convertStr;
    }

    /**
     * 产品适用的客户等级
     *
     * @return
     */
    public static String convertCustLevelSale(String custLevelSale) {
        String convertStr = "";
        if (StringUtils.isEmptyOrNull(custLevelSale)) {
            return "";
        }
        switch (custLevelSale) {
            case "0":
                convertStr = "普通客户（含）以上";
                break;
            case "1":
                convertStr = "客户等级在理财级（含）以上";
                break;
            case "2":
                convertStr = "客户等级在财富级（含）以上";
                break;
            case "3":
                convertStr = "客户等级为私行级";
                break;
            default:
                break;
        }
        return convertStr;
    }

    /**
     * 风险类型
     *
     * @return
     */
    public static String convertRiskType(String riskType) {
        String convertStr = "";
        if (riskType == null) {
            return "";
        }
        switch (riskType) {
            case WealthConst.RISK_TYPE_1:
                convertStr = "保本固定收益";
                break;
            case WealthConst.RISK_TYPE_2:
                convertStr = "保本浮动收益";
                break;
            case WealthConst.RISK_TYPE_3:
                convertStr = "非保本浮动收益";
                break;
            default:
                break;
        }
        return convertStr;
    }

    /**
     * 适用对象
     *
     * @return
     */
    public static String convertApplyObj(WealthDetailsBean detailsBean) {
        String convertStr = "";
        if (StringUtils.isEmptyOrNull(detailsBean.getApplyObj())) {
            return "";
        }

        if (WealthConst.APPLY_OBJ_1.equals(detailsBean.getApplyObj())) {// 无投资经验
            if ("0".equals(detailsBean.getCustLevelSale())) {// 产品适用的客户等级
                if ("0".equals(detailsBean.getProdRisklvl())) {
                    convertStr = "适用于全部客户";
                } else {
                    convertStr = "适用于" + convertRiskLevelNew(detailsBean.getProdRisklvl()) + "的客户";
                }
            } else {
                if ("0".equals(detailsBean.getProdRisklvl())) {
                    convertStr = "适用于" + convertCustLevelSale(detailsBean.getCustLevelSale()) + "的客户";
                } else {
                    convertStr = "适用于" + convertCustLevelSale(detailsBean.getCustLevelSale()) + "、" + convertRiskLevelNew(detailsBean.getProdRisklvl()) + "的客户";
                }
            }
        } else if (WealthConst.APPLY_OBJ_0.equals(detailsBean.getApplyObj())) {// 有投资经验
            if ("0".equals(detailsBean.getCustLevelSale())) {
                if ("0".equals(detailsBean.getProdRisklvl())) {
                    convertStr = "适用于" + convertApplyObj(detailsBean.getApplyObj()) + "的客户";
                } else {
                    convertStr = "适用于" + convertApplyObj(detailsBean.getApplyObj()) + "、" + convertRiskLevelNew(detailsBean.getProdRisklvl()) + "的客户";
                }
            } else {
                if ("0".equals(detailsBean.getProdRisklvl())) {
                    convertStr = "适用于" + convertApplyObj(detailsBean.getApplyObj()) + "、" + convertCustLevelSale(detailsBean.getCustLevelSale()) + "的客户";
                } else {
                    convertStr = "适用于" + convertApplyObj(detailsBean.getApplyObj()) + "、" + convertCustLevelSale(detailsBean.getCustLevelSale()) + "、" + convertRiskLevelNew(detailsBean.getProdRisklvl()) + "的客户";
                }
            }
        }
        return convertStr;
    }

    private static String convertApplyObj(String applyObj) {
        String convertStr = "";
        if (StringUtils.isEmptyOrNull(applyObj)) {
            return "";
        }
        switch (applyObj) {
            case WealthConst.APPLY_OBJ_0:
                convertStr = "有投资经验";
                break;
            case WealthConst.APPLY_OBJ_1:
                convertStr = "无投资经验";
                break;
            default:
                break;
        }
        return convertStr;
    }

    /**
     * 交易渠道
     *
     * @return
     */
    public static String convertWay(WealthDetailsBean detailsBean) {
        String convertStr = "";
        StringBuffer sb = new StringBuffer();
        if (WealthConst.YES_1.equals(detailsBean.getIsBancs())) {
            sb.append("柜台");
        }
        if (WealthConst.YES_1.equals(detailsBean.getIsSMS())) {
            sb.append("，短信");
        }
        if (WealthConst.YES_1.equals(detailsBean.getSellOnline())) {
            sb.append("，网银");
        }
        if (WealthConst.YES_1.equals(detailsBean.getSellMobile())) {
            sb.append("，手机银行");
        }
        if (WealthConst.YES_1.equals(detailsBean.getSellHomeBanc())) {
            sb.append("，家居银行");
        }
        if (WealthConst.YES_1.equals(detailsBean.getSellAutoBanc())) {
            sb.append("，自助终端");
        }
        if (WealthConst.YES_1.equals(detailsBean.getSellTelphone())) {
            sb.append("，电话银行自助");
        }
        if (WealthConst.YES_1.equals(detailsBean.getSellTelByPeple())) {
            sb.append("，电话银行人工");
        }
        if (WealthConst.YES_1.equals(detailsBean.getSellWeChat())) {
            sb.append("，微信银行");
        }
        if (WealthConst.NO_0.equals(detailsBean.getIsBancs())) {
            convertStr = sb.toString().substring(1, sb.toString().length());
        } else {
            convertStr = sb.toString();
        }
        return convertStr;
    }

    /**
     * 付息规则
     *
     * @param couponpayFreq
     * @return
     */
    public static String convertCouponpayFreq(String couponpayFreq, String interestDate) {
        String convertStr = "";
        if ("-1".equals(couponpayFreq)) {
            convertStr = "随本金一起到账";
        } else {
            char temp = couponpayFreq.charAt(couponpayFreq.length() - 1);
            String tempStr = couponpayFreq.substring(0, couponpayFreq.length() - 1);
            switch (temp) {
                case 'D':
                    convertStr = "每" + tempStr + "天付息一次，最近一次付息日预计是" + interestDate;
                    break;
                case 'W':
                    convertStr = "每" + tempStr + "周付息一次，最近一次付息日预计是" + interestDate;
                    break;
                case 'M':
                    convertStr = "每" + tempStr + "月付息一次，最近一次付息日预计是" + interestDate;
                    break;
                case 'Y':
                    convertStr = "每" + tempStr + "年付息一次，最近一次付息日预计是" + interestDate;
                    break;
                default:
                    break;
            }
        }
        return convertStr;
    }

    /**
     * 获取风险级别的资源图片
     *
     * @return
     */
    public static int getRiskLevelRes(String riskLevel) {
        int resId = 0;
        switch (riskLevel) {
            case WealthConst.RISK_LEVEL_0:
                resId = R.drawable.icon_risk_l;
                break;
            case WealthConst.RISK_LEVEL_1:
                resId = R.drawable.icon_risk_m_l;
                break;
            case WealthConst.RISK_LEVEL_2:
                resId = R.drawable.icon_risk_m;
                break;
            case WealthConst.RISK_LEVEL_3:
                resId = R.drawable.icon_risk_m_h;
                break;
            case WealthConst.RISK_LEVEL_4:
                resId = R.drawable.icon_risk_h;
                break;
            default:
                break;
        }
        return resId;
    }

    /**
     * 获取风险类型的资源图片
     *
     * @return
     */
    public static int getRiskTypeRes(String riskType) {
        int resId = 0;
        switch (riskType) {
            case WealthConst.RISK_TYPE_1:
                resId = R.drawable.icon_protect_fixed;
                break;
            case WealthConst.RISK_TYPE_2:
                resId = R.drawable.icon_protect_float;
                break;
            case WealthConst.RISK_TYPE_3:
                resId = R.drawable.icon_protect_no;
                break;
            default:
                break;
        }
        return resId;
    }

    /**
     * 购买开放规则
     */
    public static String convertBuyType(WealthDetailsBean detailsBean) {
        String convertStr = "";
        if (WealthConst.BUY_TYPE_00.equals(detailsBean.getBuyType())) {// 关闭
            convertStr = "不开放";
        } else if (WealthConst.BUY_TYPE_01.equals(detailsBean.getBuyType())) {// 开放期购买
            if (WealthConst.NO_0.equals(detailsBean.getBidHoliday())) {
                convertStr = detailsBean.getBidStartDate() + "至" + detailsBean.getBidEndDate() + "工作日开放申购";
            } else if (WealthConst.YES_1.equals(detailsBean.getBidHoliday())) {
                convertStr = detailsBean.getBidStartDate() + "至" + detailsBean.getBidEndDate() + "每日开放申购";
            }
        } else if (WealthConst.BUY_TYPE_02.equals(detailsBean.getBuyType())) {// 周期开放购买
            if (detailsBean.getBidPeriodMode().contains("w") || detailsBean.getBidPeriodMode().contains("W")) {// 周
                convertStr = "每周的星期" + detailsBean.getBidPeriodStartDate() + "至星期" + detailsBean.getBidPeriodEndDate() + "开放申购";
            } else if (detailsBean.getBidPeriodMode().contains("m") || detailsBean.getBidPeriodMode().contains("M")) {// 月
                convertStr = "每月的" + detailsBean.getBidPeriodStartDate() + "号至" + detailsBean.getBidPeriodEndDate() + "号开放申购";
            } else if (detailsBean.getBidPeriodMode().contains("s") || detailsBean.getBidPeriodMode().contains("S")) {// 季
                convertStr = "每季的第一个月的" + detailsBean.getBidPeriodStartDate() + "号至" + detailsBean.getBidPeriodEndDate() + "号开放申购";
            }
        } else if (WealthConst.BUY_TYPE_03.equals(detailsBean.getBuyType())) {// 起息后每日申购
            if (WealthConst.NO_0.equals(detailsBean.getBidHoliday())) {
                convertStr = "工作日开放申购";
            } else if (WealthConst.YES_1.equals(detailsBean.getBidHoliday())) {
                convertStr = "每日开放申购";
            }
        } else {
            convertStr = "-";
        }
        return convertStr;
    }

    /**
     * 本金到帐规则
     *
     * @param redPaymentMode
     * @return
     */
    public static String convertRedPaymentMode(String redPaymentMode, String dateModeType, String redPaymentDate,
                                               String isLockPeriod, String paymentDate, String datesPaymentOffset) {
        String convertStr = "";
        if (StringUtils.isEmptyOrNull(redPaymentMode)) {
            return "";
        }
        if ("0".equals(redPaymentMode)) {// 实时返还
            convertStr = "T日赎回，本金实时到账";
        } else if ("1".equals(redPaymentMode)) {// T+N返还
            if (StringUtils.isEmptyOrNull(dateModeType))
                return "";
            if ("0".equals(dateModeType)) {
                convertStr = "T日赎回，本金T＋" + redPaymentDate + "日到账";
            } else if ("1".equals(dateModeType)) {
                convertStr = "T日赎回，本金T＋" + redPaymentDate + "日到账（若遇节假日顺延至下一工作日）";
            } else if ("2".equals(dateModeType)) {
                convertStr = "T日赎回，本金T＋" + redPaymentDate + "日到账（若遇节假日顺延至下一工作日，如顺延日期跨月，则在当月最后一个工作日到账）";
            } else if ("3".equals(dateModeType)) {
                convertStr = "T日赎回，本金T＋" + redPaymentDate + "日到账（若遇节假日自动向前调整）";
            } else if ("4".equals(dateModeType)) {
                convertStr = "T日赎回，本金T＋" + redPaymentDate + "日到账（若遇节假日自动向前调整，如顺延日期跨月，则在当月第一个工作日到账）";
            }
        } else if ("2".equals(redPaymentMode)) {// 期末返还
            if (isLockPeriod == null)
                return "";
            if ("1".equals(isLockPeriod)) {// 业绩基准-锁定期转低收益
                convertStr = "预计赎回后" + redPaymentDate + "日内到账";
            } else if ("2".equals(isLockPeriod) || "3".equals(isLockPeriod)) {// 业绩基准-锁定期后入账 业绩基准-锁定期周期滚续
                convertStr = "预计到期后" + datesPaymentOffset + "日内到账";
            } else {// 非业绩基准产品
                convertStr = "预计" + paymentDate + "左右到账";
            }
        }
        return convertStr;
    }

    /**
     * 赎回收益到账规则
     *
     * @param profitMode
     * @return
     */
    public static String convertProfitMode(String profitMode, String dateModeType, String profitDate,
                                           String isLockPeriod, String redPayDate, String datesPaymentOffset) {
        String convertStr = "";
        if (StringUtils.isEmptyOrNull(profitMode)) {
            return "";
        }
        if ("1".equals(profitMode)) {// T+N返还
            if (StringUtils.isEmptyOrNull(dateModeType))
                return "";
            if ("0".equals(dateModeType)) {
                convertStr = "T日赎回，收益T＋" + profitDate + "日到账";
            } else if ("1".equals(dateModeType)) {
                convertStr = "T日赎回，收益T＋" + profitDate + "日到账（若遇节假日顺延至下一工作日）";
            } else if ("2".equals(dateModeType)) {
                convertStr = "T日赎回，收益T＋" + profitDate + "日到账（若遇节假日顺延至下一工作日，如顺延日期跨月，则在当月最后一个工作日到账）";
            } else if ("3".equals(dateModeType)) {
                convertStr = "T日赎回，收益T＋" + profitDate + "日到账（若遇节假日自动向前调整）";
            } else if ("4".equals(dateModeType)) {
                convertStr = "T日赎回，收益T＋" + profitDate + "日到账（若遇节假日自动向前调整，如顺延日期跨月，则在当月第一个工作日到账）";
            }
        } else if ("2".equals(profitMode)) {// 期末返还
            if (isLockPeriod == null)
                return "";
            if ("1".equals(isLockPeriod)) {// 业绩基准-锁定期转低收益
                convertStr = "预计赎回后" + profitDate + "日内到账";
            } else if ("2".equals(isLockPeriod) || "3".equals(isLockPeriod)) {// 业绩基准-锁定期后入账 业绩基准-锁定期周期滚续
                if (StringUtils.isEmptyOrNull(datesPaymentOffset)) {
                    convertStr = "";
                } else {
                    convertStr = "预计到期后" + datesPaymentOffset + "日内到账";
                }
            } else {// 非业绩基准产品
                convertStr = "预计" + redPayDate + "左右到账";
            }
        }
        return convertStr;
    }

    /**
     * 赎回开放规则
     */
    public static String convertRedeemType(WealthDetailsBean detailsBean) {
        String convertStr = "";
        if (WealthConst.SELL_TYPE_00.equals(detailsBean.getSellType())) {// 不允许赎回
            convertStr = "不开放主动赎回";
        } else if (WealthConst.SELL_TYPE_01.equals(detailsBean.getSellType())) {// 开放期赎回
            if (WealthConst.NO_0.equals(detailsBean.getRedEmptionHoliday())) {
                convertStr = detailsBean.getRedEmptionStartDate() + "至" + detailsBean.getRedEmptionEndDate() + "工作日开放赎回";
            } else if (WealthConst.YES_1.equals(detailsBean.getRedEmptionHoliday())) {
                convertStr = detailsBean.getRedEmptionStartDate() + "至" + detailsBean.getRedEmptionEndDate() + "每日开放赎回";
            }
        } else if (WealthConst.SELL_TYPE_02.equals(detailsBean.getSellType())) {// 付息日赎回
            char temp = detailsBean.getCouponpayFreq().charAt(detailsBean.getCouponpayFreq().length() - 1);
            String tempStr = detailsBean.getCouponpayFreq().substring(0, detailsBean.getCouponpayFreq().length() - 1);
            switch (temp) {
                case 'D':
                    convertStr = "每" + tempStr + "天开放赎回，最近一次是" + detailsBean.getInterestDate();
                    break;
                case 'W':
                    convertStr = "每" + tempStr + "周开放赎回，最近一次是" + detailsBean.getInterestDate();
                    break;
                case 'M':
                    convertStr = "每" + tempStr + "月开放赎回，最近一次是" + detailsBean.getInterestDate();
                    break;
                case 'Y':
                    convertStr = "每" + tempStr + "年开放赎回，最近一次是" + detailsBean.getInterestDate();
                    break;
                default:
                    break;
            }
        } else if (WealthConst.SELL_TYPE_03.equals(detailsBean.getSellType())) {// 起息后每日赎回
            if (WealthConst.NO_0.equals(detailsBean.getRedEmptionHoliday())) {
                convertStr = "工作日开放赎回";
            } else if (WealthConst.YES_1.equals(detailsBean.getRedEmptionHoliday())) {
                convertStr = "每日开放赎回";
            }
        } else if (WealthConst.SELL_TYPE_04.equals(detailsBean.getSellType())) {// 周期开放赎回
            if (detailsBean.getRedEmperiodfReq().contains("w") || detailsBean.getRedEmperiodfReq().contains("W")) {// 周
                convertStr = "每周的星期" + detailsBean.getRedEmperiodStart() + "至星期" + detailsBean.getRedEmperiodEnd() + "开放赎回";
            } else if (detailsBean.getRedEmperiodfReq().contains("m") || detailsBean.getRedEmperiodfReq().contains("M")) {// 月
                convertStr = "每月的" + detailsBean.getRedEmperiodStart() + "号至" + detailsBean.getRedEmperiodEnd() + "号开放赎回";
            } else if (detailsBean.getRedEmperiodfReq().contains("s") || detailsBean.getRedEmperiodfReq().contains("S")) {// 季
                convertStr = "每季的第一个月的" + detailsBean.getRedEmperiodStart() + "号至" + detailsBean.getRedEmperiodEnd() + "号开放赎回";
            }
        } else {
            convertStr = "-";
        }
        return convertStr;
    }

    /**
     * 赎回开放规则
     */
    public static String convertBuyType(PurchaseModel purchaseModel) {
        String convertStr = "";
        if (WealthConst.SELL_TYPE_00.equals(purchaseModel.getSellType())) {// 不允许赎回
            convertStr = "不开放主动赎回";
        } else if (WealthConst.SELL_TYPE_01.equals(purchaseModel.getSellType())) {// 开放期赎回
            if (WealthConst.NO_0.equals(purchaseModel.getRedEmptionHoliday())) {
                convertStr = purchaseModel.getRedEmptionStartDate() + "至" + purchaseModel.getRedEmptionEndDate() + "工作日开放赎回";
            } else if (WealthConst.YES_1.equals(purchaseModel.getRedEmptionHoliday())) {
                convertStr = purchaseModel.getRedEmptionStartDate() + "至" + purchaseModel.getRedEmptionEndDate() + "每日开放赎回";
            }
        } else if (WealthConst.SELL_TYPE_02.equals(purchaseModel.getSellType())) {// 付息日赎回
            char temp = purchaseModel.getCouponpayFreq().charAt(purchaseModel.getCouponpayFreq().length() - 1);
            String tempStr = purchaseModel.getCouponpayFreq().substring(0, purchaseModel.getCouponpayFreq().length() - 1);
            switch (temp) {
                case 'D':
                    convertStr = "每" + tempStr + "天开放赎回，最近一次是" + purchaseModel.getInterestDate();
                    break;
                case 'W':
                    convertStr = "每" + tempStr + "周开放赎回，最近一次是" + purchaseModel.getInterestDate();
                    break;
                case 'M':
                    convertStr = "每" + tempStr + "月开放赎回，最近一次是" + purchaseModel.getInterestDate();
                    break;
                case 'Y':
                    convertStr = "每" + tempStr + "年开放赎回，最近一次是" + purchaseModel.getInterestDate();
                    break;
                default:
                    break;
            }
        } else if (WealthConst.SELL_TYPE_03.equals(purchaseModel.getSellType())) {// 起息后每日赎回
            if (WealthConst.NO_0.equals(purchaseModel.getRedEmptionHoliday())) {
                convertStr = "工作日开放赎回";
            } else if (WealthConst.YES_1.equals(purchaseModel.getRedEmptionHoliday())) {
                convertStr = "每日开放赎回";
            }
        } else if (WealthConst.SELL_TYPE_04.equals(purchaseModel.getSellType())) {// 周期开放赎回
            if (purchaseModel.getRedEmperiodfReq().contains("w") || purchaseModel.getRedEmperiodfReq().contains("W")) {// 周
                convertStr = "每周的星期" + purchaseModel.getRedEmperiodStart() + "至星期" + purchaseModel.getRedEmperiodEnd() + "开放赎回";
            } else if (purchaseModel.getRedEmperiodfReq().contains("m") || purchaseModel.getRedEmperiodfReq().contains("M")) {// 月
                convertStr = "每月的" + purchaseModel.getRedEmperiodStart() + "号至" + purchaseModel.getRedEmperiodEnd() + "号开放赎回";
            } else if (purchaseModel.getRedEmperiodfReq().contains("s") || purchaseModel.getRedEmperiodfReq().contains("S")) {// 季
                convertStr = "每季的第一个月的" + purchaseModel.getRedEmperiodStart() + "号至" + purchaseModel.getRedEmperiodEnd() + "号开放赎回";
            }
        } else {
            convertStr = "-";
        }
        return convertStr;
    }

    /**
     * 是否周期性产品
     *
     * @param periodical
     * @return
     */
    public static boolean convertPeriodical(String periodical) {
        if (WealthConst.NO_0.equals(periodical)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 产品到期日
     *
     * @param prodEnd
     * @return
     */
    public static String convertProdEnd(String prodEnd, String productKind, String isLockPeriod, String productTermType) {
        String convertStr = "";
        if ("0".equals(productKind) && !StringUtils.isEmptyOrNull(isLockPeriod) && !"0".equals(isLockPeriod)) {// 业绩基准
            if (!StringUtils.isEmptyOrNull(prodEnd)) {
                convertStr = prodEnd;
            }
        } else if (!StringUtils.isEmptyOrNull(productTermType) && "3".equals(productTermType)) {
            convertStr = "长期";
        } else {
            if (!StringUtils.isEmptyOrNull(prodEnd)) {
                convertStr = prodEnd;
            }
        }
        return convertStr;
    }

    /**
     * 到期本金到账日
     *
     * @return
     */
    public static String convertPaymentDate(String paymentDate, String isLockPeriod, String redPaymentDate, String datesPaymentOffset) {
        String convertStr = "";
        if (!StringUtils.isEmptyOrNull(paymentDate)) {
            convertStr = "预计" + paymentDate + "左右到账";
        }
        if ("1".equals(isLockPeriod)) {
            if (StringUtils.isEmptyOrNull(redPaymentDate)) {
                convertStr = "";
            } else {
                convertStr = "预计赎回后" + redPaymentDate + "日内到账";
            }
        } else if ("2".equals(isLockPeriod) || "3".equals(isLockPeriod)) {
            if (StringUtils.isEmptyOrNull(datesPaymentOffset)) {
                convertStr = "";
            } else {
                convertStr = "预计到期后" + datesPaymentOffset + "日内到账";
            }
        }
        return convertStr;
    }
}
