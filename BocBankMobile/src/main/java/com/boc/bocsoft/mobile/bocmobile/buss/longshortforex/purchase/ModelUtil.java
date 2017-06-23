package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase;

import android.content.Context;

import com.boc.bocsoft.mobile.bii.bus.account.model.PublicParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailAccountInfoListQuery.PsnVFGBailAccountInfoListQueryResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTradeInfoQuery.PsnVFGTradeInfoQueryParams;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.model.BailAccount;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.model.PurchaseModel;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;
import com.boc.bocsoft.mobile.wfss.buss.forexandnoblemetal.model.querysingelquotation.WFSSQuerySingelQuotationParams;
import com.boc.bocsoft.mobile.wfss.buss.forexandnoblemetal.model.querysingelquotation.WFSSQuerySingelQuotationResult;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangyang
 *         2016/12/19 14:23
 *         Model转换类
 */
public class ModelUtil {

    /***************************** 公共模块 *****************************/
    /**
     * 设置请求公共参数,不包含安全因子
     *
     * @param params
     * @param conversationId
     * @param token
     */
    private static void setPublicParamsWithOutSecurity(PublicParams params, String conversationId, String token) {
        params.setConversationId(conversationId);
        params.setToken(token);
    }

    /***************************** 双向宝购买模块 *****************************/

    /**
     * 生成查询外汇/贵金属详情
     *
     * @param model
     * @return
     */
    public static WFSSQuerySingelQuotationParams generateWFSSQuerySingelQuotationParams(PurchaseModel model) {
        WFSSQuerySingelQuotationParams params = new WFSSQuerySingelQuotationParams();
        params.setCardType(model.getCardType());
        params.setCcygrp(model.getSourceCurrency() + model.getTargetCurrency());
        params.setCardClass("M");
        return params;
    }

    /**
     * 根据外汇/贵金属详情,生成买入信息
     *
     * @param model
     * @param result
     * @return
     */
    public static PurchaseModel generatePurchaseModel(PurchaseModel model, WFSSQuerySingelQuotationResult result) {
        model.setBuyRate(result.getBuyRate());
        model.setSellRate(result.getSellRate());
        model.setUpdateTime(LocalDateTime.now());
        return model;
    }

    /**
     * 生成保证金账户列表
     *
     * @param accountResult
     * @return
     */
    public static List<BailAccount> generateBailAccount(PsnVFGBailAccountInfoListQueryResult accountResult) {
        List<BailAccount> accounts = new ArrayList<>();
        if(accountResult == null)
            return accounts;

        for (PsnVFGBailAccountInfoListQueryResult.VFGBailAccountInfo accountInfo : accountResult.getList()) {
            BailAccount account = new BailAccount();
            account.setCashBanlance(accountInfo.getCashBanlance());
            account.setAlarmFlag(accountInfo.getAlarmFlag());
            account.setCurrency(accountInfo.getSettleCurrency().getCode());
            account.setMarginAccountNo(accountInfo.getMarginAccountNo());
            account.setMaxDrawAmount(accountInfo.getMaxDrawAmount());
            account.setMaxTradeAmount(accountInfo.getMaxTradeAmount());
            account.setMarginFund(accountInfo.getMarginFund());
            accounts.add(account);
        }
        return accounts;
    }

    /**
     * 生成币种列表
     *
     * @param accounts
     * @param model
     * @return
     */
    public static List<String> generateCurrencyList(Context context, List<BailAccount> accounts, PurchaseModel model) {
        List<String> currencyList = new ArrayList<>();
        for (BailAccount account : accounts)
            currencyList.add(PublicCodeUtils.getCurrency(context, account.getCurrency()));

        if (model.isForeignCurrency() && currencyList.size() < 7)
            currencyList.add("添加其他结算币种");

        if (!model.isForeignCurrency() && currencyList.size() < 2)
            currencyList.add("添加其他结算币种");

        return currencyList;
    }

    /**
     * 生成币种列表
     *
     * @param list
     * @return
     */
    public static List<String> generateCurrencyList(PurchaseModel model, List<String> list) {
        List<String> currencyList = new ArrayList<>();
        Context context = ActivityManager.getAppManager().currentActivity();
        for (String code : list)
            currencyList.add(PublicCodeUtils.getCurrency(context, code));

        if (model.isForeignCurrency() && currencyList.size() < 7)
            currencyList.add("添加其他结算币种");

        if (!model.isForeignCurrency() && currencyList.size() < 2)
            currencyList.add("添加其他结算币种");

        return currencyList;
    }

    /**
     * 根据选择的币种获取账户信息
     *
     * @param currency
     * @param accounts
     * @return
     */
    public static BailAccount generateBailAccount(String currency, List<BailAccount> accounts) {
        for (BailAccount account : accounts) {
            if (currency.equals(account.getCurrency()))
                return account;
        }
        return null;
    }

    /**
     * 生成查询平仓交易参数
     * @return
     */
    public static PsnVFGTradeInfoQueryParams generatePsnVFGTradeInfoQueryParams(String currency) {
        PsnVFGTradeInfoQueryParams params = new PsnVFGTradeInfoQueryParams();
        params.setQueryType(PurchaseModel.TRANS_TYPE_DELETE);
        params.setCurrencyCode(currency);
        return params;
    }
}
