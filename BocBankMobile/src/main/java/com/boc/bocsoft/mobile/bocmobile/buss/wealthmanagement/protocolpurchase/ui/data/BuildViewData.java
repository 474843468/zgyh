package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui.data;

import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.ProtocolIntelligentDetailsBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.ProtocolModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.utils.ProtocolConvertUtils;

/**
 * Created by liuweidong on 2016/11/8.
 */
public class BuildViewData {
    /**
     * 周期连续协议
     *
     * @return
     */
    public static String[] buildPeriodSeriesName() {
        String[] names = new String[7];
        int index = 0;
        names[index++] = "协议名称";
        names[index++] = "协议总期数";
        names[index++] = "当前期数";
        names[index++] = "协议投资周期";
        names[index++] = "单周期投资\n期限";
        names[index++] = "下次购买日期";
        names[index++] = "申购起点金额";
        return names;
    }

    /**
     * 周期连续协议
     *
     * @return
     */
    public static String[] buildPeriodSeriesValue(ProtocolIntelligentDetailsBean detailsBean, ProtocolModel viewModel) {
        String[] values = new String[7];
        int index = 0;
        values[index++] = detailsBean.getAgrName();
        values[index++] = detailsBean.getAgrPeriod();
        values[index++] = detailsBean.getAgrCurrPeriod();
        values[index++] = ProtocolConvertUtils.convertPeriodAgr(detailsBean.getPeriodAgr());
        values[index++] = ProtocolConvertUtils.convertPeriodAgr(detailsBean.getSingleInvestPeriod());
        values[index++] = detailsBean.getFirstDatePur();
        values[index++] = MoneyUtils.transMoneyFormat(viewModel.getSubAmount(), viewModel.getCurCode());
        return values;
    }

    /**
     * 周期不连续协议
     *
     * @return
     */
    public static String[] buildPeriodNoSeriesName() {
        String[] names = new String[10];
        int index = 0;
        names[index++] = "协议名称";
        names[index++] = "协议总期数";
        names[index++] = "当前期数";
        names[index++] = "协议投资周期";
        names[index++] = "单周期投资\n期限";
        names[index++] = "购买频率";
        names[index++] = "下次购买日期";
        names[index++] = "赎回频率";
        names[index++] = "下次赎回日期";
        names[index++] = "申购起点金额";
        return names;
    }

    /**
     * 周期不连续协议
     *
     * @return
     */
    public static String[] buildPeriodNoSeriesValue(ProtocolIntelligentDetailsBean detailsBean, ProtocolModel viewModel) {
        String[] values = new String[10];
        int index = 0;
        values[index++] = detailsBean.getAgrName();
        values[index++] = detailsBean.getAgrPeriod();
        values[index++] = detailsBean.getAgrCurrPeriod();
        values[index++] = ProtocolConvertUtils.convertPeriodAgr(detailsBean.getPeriodAgr());
        values[index++] = ProtocolConvertUtils.convertPeriodAgr(detailsBean.getSingleInvestPeriod());
        values[index++] = "每" + ProtocolConvertUtils.convertPeriodAgr(detailsBean.getPeriodPur()) + "申购";
        values[index++] = detailsBean.getFirstDatePur();
        values[index++] = "每" + ProtocolConvertUtils.convertPeriodAgr(detailsBean.getPeriodRed()) + "赎回";
        values[index++] = detailsBean.getFirstDateRed();
        values[index++] = MoneyUtils.transMoneyFormat(viewModel.getSubAmount(), viewModel.getCurCode());
        return values;
    }

    /**
     * 多次购买协议
     *
     * @return
     */
    public static String[] buildBuyName(String isNeedRed) {
        String[] names = new String[9];
        int index = 0;
        names[index++] = "协议名称";
        names[index++] = "协议总期数";
        names[index++] = "当前期数";
        names[index++] = "协议投资周期";
        names[index++] = "购买频率";
        names[index++] = "下次购买日期";
        names[index++] = "是否赎回";
        if ("0".equals(isNeedRed)) {
            names[index++] = "下次赎回日期";
        }
        names[index++] = "申购起点金额";
        return names;
    }

    /**
     * 多次购买协议
     *
     * @return
     */
    public static String[] buildBuyValue(ProtocolIntelligentDetailsBean detailsBean, ProtocolModel viewModel) {
        String[] names = new String[9];
        int index = 0;
        names[index++] = detailsBean.getAgrName();
        names[index++] = detailsBean.getAgrPeriod();
        names[index++] = detailsBean.getAgrCurrPeriod();
        names[index++] = ProtocolConvertUtils.convertPeriodAgr(detailsBean.getPeriodAgr());
        names[index++] = "每" + ProtocolConvertUtils.convertPeriodAgr(detailsBean.getPeriodPur()) + "申购";
        names[index++] = detailsBean.getFirstDatePur();
        names[index++] = ProtocolConvertUtils.convertIsNeedRed(detailsBean.getIsNeedRed());
        if ("0".equals(detailsBean.getIsNeedRed())) {
            names[index++] = detailsBean.getFirstDateRed();
        }
        names[index++] = MoneyUtils.transMoneyFormat(viewModel.getSubAmount(), viewModel.getCurCode());
        return names;
    }

    /**
     * 多次赎回协议
     *
     * @return
     */
    public static String[] buildRedeemName(String isNeedPur) {
        String[] names = new String[9];
        int index = 0;
        names[index++] = "协议名称";
        names[index++] = "协议总期数";
        names[index++] = "当前期数";
        names[index++] = "协议投资周期";
        names[index++] = "赎回频率";
        names[index++] = "下次赎回日期";
        names[index++] = "是否申购";
        if ("0".equals(isNeedPur)) {
            names[index++] = "下次购买日期";
        }
        names[index++] = "赎回起点份额";
        return names;
    }

    /**
     * 多次赎回协议
     *
     * @return
     */
    public static String[] buildRedeemValue(ProtocolIntelligentDetailsBean detailsBean, ProtocolModel viewModel) {
        String[] names = new String[9];
        int index = 0;
        names[index++] = detailsBean.getAgrName();
        names[index++] = detailsBean.getAgrPeriod();
        names[index++] = detailsBean.getAgrCurrPeriod();
        names[index++] = ProtocolConvertUtils.convertPeriodAgr(detailsBean.getPeriodAgr());
        names[index++] = "每" + ProtocolConvertUtils.convertPeriodAgr(detailsBean.getPeriodRed()) + "赎回";
        names[index++] = detailsBean.getFirstDateRed();
        names[index++] = ProtocolConvertUtils.convertIsNeedPur(detailsBean.getIsNeedPur());
        if ("0".equals(detailsBean.getIsNeedPur())) {
            names[index++] = detailsBean.getFirstDatePur();
        }
        names[index++] = MoneyUtils.transMoneyFormat(viewModel.getLowLimitAmount(), viewModel.getCurCode());
        return names;
    }


    /**
     * 周期滚续投资
     *
     * @return
     */
    public static String[] buildContinueName() {
        String[] names = new String[3];
        int index = 0;
        names[index++] = "产品系列名称";
        names[index++] = "认购起点金额";
        names[index++] = "追加认申购起点金额";
        return names;
    }


    /**
     * 周期滚续投资
     *
     * @return
     */
    public static String[] buildContinueValue(String getSerialName, String getSubAmount, String getAddAmount) {
        String[] names = new String[3];
        int index = 0;
        names[index++] = getSerialName;
        names[index++] = getSubAmount;
        names[index++] = getAddAmount;
        return names;
    }

}
