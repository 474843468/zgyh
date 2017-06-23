package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.utils;

import android.content.Context;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnGetAllExchangeRatesOutlay.PsnGetAllExchangeRatesOutlayResult;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleCode;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.model.customModel.MoneyForOrderModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.model.customModel.CustomLoginBeforeModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.model.customModel.MoneyForOrderModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.model.psnGetAllExchangeRatesOutlay.PsnGetAllExchangeRatesOutlayReqModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.model.psnGetAllExchangeRatesOutlay.PsnGetAllExchangeRatesOutlayResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.model.wfssQueryMultipleQuotation.WFSSQueryMultipleQuotationReqModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.model.wfssQueryMultipleQuotation.WFSSQueryMultipleQuotationResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.ui.LongShortForexHomeFragment;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.wfss.buss.forexandnoblemetal.model.querymultiplequotation.WFSSQueryMultipleQuotationResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 双向宝首页 model 转换工具
 * Created by yx on 2016/12/15.
 */

public class LongShortForexHomeCodeModelUtil {
    /**
     * 双向宝-能否进入功能-三大步骤判断
     *
     * @param moduleId
     * @return
     */
    public static boolean[] needsLongShortForexStatus(String moduleId) {
        boolean[] needs = new boolean[3];
        switch (moduleId) {
            case ModuleCode.MODULE_ISFOREXSTORAGECASH_0100://双向宝-双向宝持仓
                needs[0] = true;
                needs[1] = true;
                needs[2] = true;
                break;
            case ModuleCode.MODULE_ISFOREXSTORAGECASH_0200://  双向宝-账户管理
                needs[0] = true;
                needs[1] = false;
                needs[2] = true;
                break;
            case ModuleCode.MODULE_ISFOREXSTORAGECASH_0300:// 双向宝-委托交易查询
                needs[0] = true;
                needs[1] = false;
                needs[2] = true;
                break;
            case ModuleCode.MODULE_ISFOREXSTORAGECASH_0400:// 双向宝-交易记录
                needs[0] = true;
                needs[1] = false;
                needs[2] = true;
                break;
            case ModuleCode.MODULE_ISFOREXSTORAGECASH_0500:// 双向宝-保证金存入/转出
                needs[0] = true;
                needs[1] = true;
                needs[2] = false;
                break;
            case ModuleCode.MODULE_ISFOREXSTORAGECASH_0600://双向宝-帮助
                needs[0] = true;
                needs[1] = true;
                needs[2] = true;
                break;
            default:
                break;
        }
        return needs;
    }

    //比较金额大小
    public static int compareBigDecimal(String s1, String s2) {
        if (StringUtils.isEmptyOrNull(s1) || StringUtils.isEmptyOrNull(s2)) {
            return 0;
        }
        try {
            BigDecimal b1;
            BigDecimal b2;
            if (s1.contains("+") && s2.contains("+")) {
                b1 = new BigDecimal(s1.replaceAll("%", "").replaceAll("\\+", ""));
                b2 = new BigDecimal(s2.replaceAll("%", "").replaceAll("\\+", ""));
                LogUtils.i("yx--------b1----" + b1 + "----b2---" + b2);
                LogUtils.i("yx--------" + b2.compareTo(b1));
                return b2.compareTo(b1);
            } else if (s1.contains("-") && s2.contains("-")) {
                b1 = new BigDecimal(s1.replaceAll("%", "").replaceAll("\\-", ""));
                b2 = new BigDecimal(s2.replaceAll("%", "").replaceAll("\\-", ""));
                LogUtils.i("yx--------b1----" + b1 + "----b2---" + b2);
                LogUtils.i("yx--------" + b2.compareTo(b1));
                return -b2.compareTo(b1);
            } else if (s1.contains("+") && s2.contains("-")) {
                return -1;
            } else if (s1.contains("-") && s2.contains("+")) {
                return 1;
            } else {
                b1 = new BigDecimal(s1.replaceAll("%", ""));
                b2 = new BigDecimal(s2.replaceAll("%", ""));
                LogUtils.i("yx--------b1----" + b1 + "----b2---" + b2);
                LogUtils.i("yx--------" + b2.compareTo(b1));
                return b2.compareTo(b1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //贵金属-----8对货币对顺序数据
    public static List<MoneyForOrderModel> getPreciousMetalsData(Context context) {
        List<MoneyForOrderModel> preciousMetalsList = new ArrayList<MoneyForOrderModel>();
        // 贵金属 货币对
        String[] currencyPairs = context.getResources().getStringArray(
                R.array.precious_metal_currency_pairs);
        //贵金属 有效小数位
        String[] effectiveDecimalPlace = context.getResources().getStringArray(
                R.array.precious_metal_effective_decimal_place);
        //贵金属 货币对名称
        String[] currencyPairsName = context.getResources().getStringArray(
                R.array.precious_metal_currency_pairs_name);
        //贵金属 国际货币符号
        String[] internationalCurrencySign = context.getResources().getStringArray(
                R.array.precious_metal_international_currency_sign);
        for (int i = 0; i < currencyPairs.length; i++) {
            MoneyForOrderModel mMoneyForOrderModel = new MoneyForOrderModel();
            mMoneyForOrderModel.setOrder(i);
            mMoneyForOrderModel.setCurrentyToNum(currencyPairs[i]);
            mMoneyForOrderModel.setCurrentyTocharacter(internationalCurrencySign[i]);
            mMoneyForOrderModel.setCurrencyName(currencyPairsName[i]);
            mMoneyForOrderModel.setEffectivDecimalPlace(effectiveDecimalPlace[i]);
            preciousMetalsList.add(mMoneyForOrderModel);
        }
        return preciousMetalsList;
    }

    //外汇-----37对货币对顺序数据
    public static List<MoneyForOrderModel> getForegnCurrencyData(Context context) {
        List<MoneyForOrderModel> foregnCurrencyList = new ArrayList<MoneyForOrderModel>();
        // 外汇 货币对
        String[] currencyPairs = context.getResources().getStringArray(
                R.array.foreign_exchange_currency_pairs);
        //外汇 有效小数位
        String[] effectiveDecimalPlace = context.getResources().getStringArray(
                R.array.foreign_exchange_effective_decimal_place);
        //外汇 货币对名称
        String[] currencyPairsName = context.getResources().getStringArray(
                R.array.foreign_exchange_currency_pairs_name);
        //外汇 国际货币符号
        String[] internationalCurrencySign = context.getResources().getStringArray(
                R.array.foreign_exchange_international_currency_sign);
        for (int i = 0; i < currencyPairs.length; i++) {
            MoneyForOrderModel mMoneyForOrderModel = new MoneyForOrderModel();
            mMoneyForOrderModel.setOrder(i);
            mMoneyForOrderModel.setCurrentyToNum(currencyPairs[i]);
            mMoneyForOrderModel.setCurrentyTocharacter(internationalCurrencySign[i]);
            mMoneyForOrderModel.setCurrencyName(currencyPairsName[i]);
            mMoneyForOrderModel.setEffectivDecimalPlace(effectiveDecimalPlace[i]);
            foregnCurrencyList.add(mMoneyForOrderModel);
        }
        return foregnCurrencyList;
    }

    //----------------------------------登录前接口处理----------start------

    /**
     * WFSS-2.1 外汇、贵金属多笔行情查询-转换请求model{涨跌幅/值}
     * 概述
     * 查询外汇实盘、外汇虚盘、贵金属实盘、贵金属虚盘行情、详情基本信息。
     * 上送的URL
     * http://[ip]:[port]/mobileplatform/forex/queryMultipleQuotation
     *
     * @param psort 涨跌幅排序 UP - 升序
     *              DN - 降序
     *              为空按优先级排序
     * @param type  0代表 自选，1代表贵金属，2代表外汇
     * @return 请求model 不同类型
     */
    public static WFSSQueryMultipleQuotationReqModel buildWFSSQueryMultipleQuotationParams(String psort, LongShortForexHomeFragment.LongShorForeStatus type) {
        WFSSQueryMultipleQuotationReqModel mParams = new WFSSQueryMultipleQuotationReqModel();
        if (type == LongShortForexHomeFragment.LongShorForeStatus.ONE) {//？？？？？暂时不定是否使用
            mParams.setPSort(psort + "");
            mParams.setCardClass("");
            mParams.setCardType("");
        } else if (type == LongShortForexHomeFragment.LongShorForeStatus.TWO) {//贵金属
            mParams.setPSort(psort + "");
            mParams.setCardClass("M");
            mParams.setCardType("G");
        } else if (type == LongShortForexHomeFragment.LongShorForeStatus.THREE) {//外汇
            mParams.setPSort(psort + "");
            mParams.setCardClass("M");
            mParams.setCardType("F");
        }
        return mParams;
    }

    /**
     * I-43->4.18 018 PsnGetAllExchangeRatesOutlay 登录前贵金属、外汇、双向宝行情查询
     *
     * @param ibknum       省行联行号
     * @param paritiesType 汇率类型  -外汇：F；黄金：G
     * @return
     */

    public static PsnGetAllExchangeRatesOutlayReqModel buildPsnGetAllExchangeRatesOutlayParams(String ibknum, String paritiesType) {
        PsnGetAllExchangeRatesOutlayReqModel mParams = new PsnGetAllExchangeRatesOutlayReqModel();

        mParams.setIbknum(ibknum + "");//省行联行号
        mParams.setOfferType("M");//实虚盘标识	String	M	实盘：R ;虚盘（双向宝）：M
        mParams.setParitiesType("" + paritiesType);//汇率类型  -外汇：F；黄金：G
        return mParams;
    }

    /**
     * WFSS-2.1 外汇、贵金属多笔行情查询-转换响应model{涨跌幅/值}
     *
     * @param wfssQueryMultipleQuotationResult
     * @return
     */
    public static WFSSQueryMultipleQuotationResModel transverterWFSSQueryMultipleQuotationResModel(WFSSQueryMultipleQuotationResult wfssQueryMultipleQuotationResult) {
        WFSSQueryMultipleQuotationResModel mResModel = new WFSSQueryMultipleQuotationResModel();

        if (wfssQueryMultipleQuotationResult != null) {
            List<WFSSQueryMultipleQuotationResModel.ItemsEntity> itemsEntityList = new ArrayList<WFSSQueryMultipleQuotationResModel.ItemsEntity>();
            for (int i = 0; i < wfssQueryMultipleQuotationResult.getItems().size(); i++) {
                WFSSQueryMultipleQuotationResModel.ItemsEntity itemsEntity = new WFSSQueryMultipleQuotationResModel.ItemsEntity();
                itemsEntity.setCcygrpNm(wfssQueryMultipleQuotationResult.getItems().get(i).getCcygrpNm());
                itemsEntity.setBuyRate(wfssQueryMultipleQuotationResult.getItems().get(i).getBuyRate());
                itemsEntity.setSellRate(wfssQueryMultipleQuotationResult.getItems().get(i).getSellRate());
                itemsEntity.setCurrPercentDiff(wfssQueryMultipleQuotationResult.getItems().get(i).getCurrPercentDiff());
                itemsEntity.setCurrDiff(wfssQueryMultipleQuotationResult.getItems().get(i).getCurrDiff());
                itemsEntity.setPriceTime(wfssQueryMultipleQuotationResult.getItems().get(i).getPriceTime());
                itemsEntity.setTranCode(wfssQueryMultipleQuotationResult.getItems().get(i).getTranCode());
                itemsEntity.setSortPriority(wfssQueryMultipleQuotationResult.getItems().get(i).getSortPriority());
                itemsEntity.setReferPrice(wfssQueryMultipleQuotationResult.getItems().get(i).getReferPrice());
                itemsEntity.setSourceCurrencyCode(wfssQueryMultipleQuotationResult.getItems().get(i).getSourceCurrencyCode());
                itemsEntity.setTargetCurrencyCode(wfssQueryMultipleQuotationResult.getItems().get(i).getTargetCurrencyCode());
                itemsEntityList.add(itemsEntity);
            }
            mResModel.setItems(itemsEntityList);
            mResModel.setRcdcnt(wfssQueryMultipleQuotationResult.getRcdcnt());
            return mResModel;
        } else {
            return null;
        }
    }

    /**
     * I-43->4.18 018 PsnGetAllExchangeRatesOutlay 登录前贵金属、外汇、双向宝行情查询
     *
     * @param psnGetAllExchangeRatesOutlayResultList
     * @return
     */
    public static List<PsnGetAllExchangeRatesOutlayResModel> transverterPsnGetAllExchangeRatesOutlayResModel(List<PsnGetAllExchangeRatesOutlayResult> psnGetAllExchangeRatesOutlayResultList) {

        if (!PublicUtils.isEmpty(psnGetAllExchangeRatesOutlayResultList)) {
            List<PsnGetAllExchangeRatesOutlayResModel> mListResModel = new ArrayList<PsnGetAllExchangeRatesOutlayResModel>();
            for (int i = 0; i < psnGetAllExchangeRatesOutlayResultList.size(); i++) {
                PsnGetAllExchangeRatesOutlayResModel mResModel = new PsnGetAllExchangeRatesOutlayResModel();
                mResModel.setState(psnGetAllExchangeRatesOutlayResultList.get(i).getState());
                mResModel.setType(psnGetAllExchangeRatesOutlayResultList.get(i).getType());
                mResModel.setVfgType(psnGetAllExchangeRatesOutlayResultList.get(i).getVfgType());
                mResModel.setCreateDate(psnGetAllExchangeRatesOutlayResultList.get(i).getCreateDate());
                mResModel.setSellRate(psnGetAllExchangeRatesOutlayResultList.get(i).getSellRate());
                mResModel.setRate(psnGetAllExchangeRatesOutlayResultList.get(i).getRate());
                mResModel.setBuyRate(psnGetAllExchangeRatesOutlayResultList.get(i).getBuyRate());
                mResModel.setSourceCurrencyCode(psnGetAllExchangeRatesOutlayResultList.get(i).getSourceCurrencyCode());
                mResModel.setFlag(psnGetAllExchangeRatesOutlayResultList.get(i).getFlag());
                mResModel.setIbkNum(psnGetAllExchangeRatesOutlayResultList.get(i).getIbkNum());
                PsnGetAllExchangeRatesOutlayResModel.SourceCurrencyEntity sourceCurrencyEntity = new PsnGetAllExchangeRatesOutlayResModel.SourceCurrencyEntity();
                sourceCurrencyEntity.setCode(psnGetAllExchangeRatesOutlayResultList.get(i).getSourceCurrency().getCode());
                sourceCurrencyEntity.setFraction(psnGetAllExchangeRatesOutlayResultList.get(i).getSourceCurrency().getFraction());
                sourceCurrencyEntity.setI18nId(psnGetAllExchangeRatesOutlayResultList.get(i).getSourceCurrency().getI18nId());
                mResModel.setSourceCurrency(sourceCurrencyEntity);
                PsnGetAllExchangeRatesOutlayResModel.TargetCurrencyEntity trgetCurrencyEntity = new PsnGetAllExchangeRatesOutlayResModel.TargetCurrencyEntity();
                trgetCurrencyEntity.setCode(psnGetAllExchangeRatesOutlayResultList.get(i).getTargetCurrency().getCode());
                trgetCurrencyEntity.setFraction(psnGetAllExchangeRatesOutlayResultList.get(i).getTargetCurrency().getFraction());
                trgetCurrencyEntity.setI18nId(psnGetAllExchangeRatesOutlayResultList.get(i).getTargetCurrency().getI18nId());
                mResModel.setTargetCurrency(trgetCurrencyEntity);
                mResModel.setUpdateDate(psnGetAllExchangeRatesOutlayResultList.get(i).getUpdateDate());
                mResModel.setTargetCurrencyCode(psnGetAllExchangeRatesOutlayResultList.get(i).getTargetCurrencyCode());
                mResModel.setSpotRate(psnGetAllExchangeRatesOutlayResultList.get(i).getSpotRate());
                mResModel.setBaseAmt(psnGetAllExchangeRatesOutlayResultList.get(i).getBaseAmt());
                mResModel.setBuyNoteRate(psnGetAllExchangeRatesOutlayResultList.get(i).getBuyNoteRate());
                mResModel.setSellNoteRate(psnGetAllExchangeRatesOutlayResultList.get(i).getSellNoteRate());
                mListResModel.add(mResModel);
            }
            return mListResModel;
        } else {
            return null;
        }
    }

    /**
     * /**
     * 登录前-合并后处理 顺序 model
     *
     * @param psnGetAllExchangeRatesOutlayResModel I43-4.18 登录前 买入卖出价
     * @param wfssQueryMultipleQuotationResModel   wfss 2.1 涨跌幅/值
     * @param isSort                               是否进行 涨跌幅排序
     * @param isPreciousMetals                     是否是贵金属，true 贵金属，false 外汇
     * @return
     */
    public static List<CustomLoginBeforeModel> transverterCustomLoginBeforeModel(Context context, List<PsnGetAllExchangeRatesOutlayResModel> psnGetAllExchangeRatesOutlayResModel, WFSSQueryMultipleQuotationResModel wfssQueryMultipleQuotationResModel, boolean isSort, boolean isPreciousMetals) {
        LogUtil.d("yx-------------登录前-----合并数据处理");
        ArrayList<PsnGetAllExchangeRatesOutlayResModel> psnGetAllExchangeRatesOutlayResModelTwo;
        psnGetAllExchangeRatesOutlayResModelTwo = (ArrayList) ((ArrayList) psnGetAllExchangeRatesOutlayResModel).clone();
        //------------------------------------------------------------
        if (!PublicUtils.isEmpty(psnGetAllExchangeRatesOutlayResModel)) {
            LogUtil.d("yx-------------登录前-----合并数据处理--------bii数据不为空");
            List<CustomLoginBeforeModel> mSumListResModel = new ArrayList<CustomLoginBeforeModel>();//匹配和无匹配的总数据
            List<CustomLoginBeforeModel> mYListResModel = new ArrayList<CustomLoginBeforeModel>();//有匹配的数据
            List<CustomLoginBeforeModel> mNListResModel = new ArrayList<CustomLoginBeforeModel>();//无匹配的数据
            List<MoneyForOrderModel> mMoneyForOrderListModel = new ArrayList<MoneyForOrderModel>();
            if (!PublicUtils.isEmpty(mSumListResModel)) {
                mSumListResModel.clear();
            }
            if (!PublicUtils.isEmpty(mNListResModel)) {
                mNListResModel.clear();
            }
            //---------------------------没有进行排序的时候逻辑判断------------start---
            if (!isSort) {   //默认固定顺序
                if (isPreciousMetals) {//贵金属固定顺序数据
                    LogUtil.d("yx-------------登录前-----贵金属固定顺序数据");
                    mMoneyForOrderListModel = LongShortForexHomeCodeModelUtil.getPreciousMetalsData(context);
                } else {//外汇固定顺序数据
                    LogUtil.d("yx-------------登录前-----外汇固定顺序数据");
                    mMoneyForOrderListModel = LongShortForexHomeCodeModelUtil.getForegnCurrencyData(context);
                }
                mYListResModel = getChangelessAndBeforLoginBiiMatchData(mMoneyForOrderListModel, psnGetAllExchangeRatesOutlayResModel, psnGetAllExchangeRatesOutlayResModelTwo);
                if (PublicUtils.isEmpty(psnGetAllExchangeRatesOutlayResModelTwo)) {
                    mSumListResModel.addAll(mYListResModel);
                } else {//无匹配的数据 Bii
                    for (int w = 0; w < psnGetAllExchangeRatesOutlayResModelTwo.size(); w++) {
                        CustomLoginBeforeModel customLoginBeforeNModel = new CustomLoginBeforeModel();
                        customLoginBeforeNModel.setPsnGetAllExchangeRatesOutlayResModel(psnGetAllExchangeRatesOutlayResModelTwo.get(w));
                        mNListResModel.add(customLoginBeforeNModel);
                    }
                    mSumListResModel.addAll(mYListResModel);
                    mSumListResModel.addAll(mNListResModel);
                }
                //---------------用规定的顺序和Bii已经匹配过的数据进行和 wfss 涨跌幅做比较 进行匹配值
                if (wfssQueryMultipleQuotationResModel != null && !PublicUtils.isEmpty(wfssQueryMultipleQuotationResModel.getItems())) {//有涨跌幅值
                    for (int i = 0; i < mSumListResModel.size(); i++) {
                        String sourceCurrencyCodeBii = mSumListResModel.get(i).getPsnGetAllExchangeRatesOutlayResModel().getSourceCurrencyCode();
                        String targetCurrencyCodeBii = mSumListResModel.get(i).getPsnGetAllExchangeRatesOutlayResModel().getTargetCurrencyCode();
                        for (int j = 0; j < wfssQueryMultipleQuotationResModel.getItems().size(); j++) {//匹配wfss 数据
                            String sourceCurrencyCodeWfss = wfssQueryMultipleQuotationResModel.getItems().get(j).getSourceCurrencyCode();
                            String targetCurrencyCodeWfss = wfssQueryMultipleQuotationResModel.getItems().get(j).getTargetCurrencyCode();
                            if ((sourceCurrencyCodeWfss + targetCurrencyCodeWfss + "").equalsIgnoreCase(sourceCurrencyCodeBii + targetCurrencyCodeBii + "")) {
                                CustomLoginBeforeModel customLoginBeforeModel = new CustomLoginBeforeModel();
                                customLoginBeforeModel.setItemsEntity(wfssQueryMultipleQuotationResModel.getItems().get(j));
                                customLoginBeforeModel.setPsnGetAllExchangeRatesOutlayResModel(mSumListResModel.get(i).getPsnGetAllExchangeRatesOutlayResModel());
                                mSumListResModel.set(i, customLoginBeforeModel);
                                break;
                            }
                        }
                    }
                    return mSumListResModel;
                } else {//涨跌幅没有返回数据-直接返回{规定的顺序和Bii已经匹配过的数据}
                    return mSumListResModel;
                }
                //---------------------------没有进行排序的时候逻辑判断------------end---
            } else {   //涨跌幅排序 以涨跌幅为准的顺序
                //---------------------------有排序的时候逻辑判断------------start---
                if (wfssQueryMultipleQuotationResModel != null && !PublicUtils.isEmpty(wfssQueryMultipleQuotationResModel.getItems())) {//wfss 值不为空时候
                    LogUtil.d("yx-------------登录前-----合并数据处理--------涨跌幅排序为准");
                    mYListResModel = getWfssAndBeforLoginBiiMatchData(wfssQueryMultipleQuotationResModel.getItems(), psnGetAllExchangeRatesOutlayResModel, psnGetAllExchangeRatesOutlayResModelTwo);
                    if (PublicUtils.isEmpty(psnGetAllExchangeRatesOutlayResModelTwo)) {

                        mSumListResModel.addAll(mYListResModel);
                        LogUtil.d("yx-------------登录前-----合并数据处理-------1-涨跌幅和bii数据都匹配上的数据大小"+mYListResModel.size());
                    } else {  //无匹配的数据 Bii
                        for (int q = 0; q < psnGetAllExchangeRatesOutlayResModelTwo.size(); q++) {
                            CustomLoginBeforeModel customLoginBeforeNModel = new CustomLoginBeforeModel();
                            customLoginBeforeNModel.setPsnGetAllExchangeRatesOutlayResModel(psnGetAllExchangeRatesOutlayResModelTwo.get(q));
                            mNListResModel.add(customLoginBeforeNModel);
                        }
                        mSumListResModel.addAll(mYListResModel);
                        mSumListResModel.addAll(mNListResModel);
                        LogUtil.d("yx-------------登录前-----合并数据处理-------2-涨跌幅和bii数据都匹配上的数据大小"+mYListResModel.size());
                        LogUtil.d("yx-------------登录前-----合并数据处理--------涨跌幅和bii数据没有匹配上的数据大小"+mNListResModel.size());
                    }
                } else {   //wfss 值 为空的时候 显示 bii 数据
                    LogUtil.d("yx-------------登录前-----合并数据处理--------wfss数据为空，以bii数据为准");
                    for (int m = 0; m < psnGetAllExchangeRatesOutlayResModel.size(); m++) {
                        CustomLoginBeforeModel NcustomLoginBeforeModel = new CustomLoginBeforeModel();
                        NcustomLoginBeforeModel.setPsnGetAllExchangeRatesOutlayResModel(psnGetAllExchangeRatesOutlayResModel.get(m));
                        mNListResModel.add(NcustomLoginBeforeModel);
                    }
                    mSumListResModel.addAll(mNListResModel);
                }

            }
            return mSumListResModel;
            //---------------------------有排序的时候逻辑判断------------end---
        } else {
            LogUtil.d("yx-------------登录前-----合并数据处理--------bii数据没有返回");
            return null;
        }
    }

    /**
     * 默认顺序和 登录前bii数据 匹配上的数据
     *
     * @param changelessListData 固定顺序 8条（贵金属）或者37条（外汇）
     * @param biiDataOne         登录前Biii数据 买入卖出价1
     * @param biiDataTwo         登录前Biii数据 买入卖出价2
     * @return 返回按照前端规定的默认顺序匹配上的bii数据
     */
    private static List<CustomLoginBeforeModel> getChangelessAndBeforLoginBiiMatchData
    (List<MoneyForOrderModel> changelessListData, List<PsnGetAllExchangeRatesOutlayResModel> biiDataOne, List<PsnGetAllExchangeRatesOutlayResModel> biiDataTwo) {
        List<CustomLoginBeforeModel> mListHaveMatchData = new ArrayList<CustomLoginBeforeModel>();//有匹配的数据
        for (int i = 0; i < changelessListData.size(); i++) {//以固定顺序进行匹配
            String currentyToNum = changelessListData.get(i).getCurrentyToNum();
            CustomLoginBeforeModel customLoginBeforeModel = new CustomLoginBeforeModel();
            for (int j = 0; j < biiDataOne.size(); j++) {//循环判断是否有匹配数据
                String sourceCurrencyCodeBii = biiDataOne.get(j).getSourceCurrencyCode();
                String targetCurrencyCodeBii = biiDataOne.get(j).getTargetCurrencyCode();
                if (currentyToNum.equalsIgnoreCase(sourceCurrencyCodeBii + targetCurrencyCodeBii)) {
                    //有匹配数据
                    customLoginBeforeModel.setPsnGetAllExchangeRatesOutlayResModel(biiDataOne.get(j));
                    mListHaveMatchData.add(customLoginBeforeModel);
                    removeItemFormList(currentyToNum, biiDataTwo);
                }
            }
        }
        return mListHaveMatchData;
    }

    /**
     * Wfss和 登录前bii数据 匹配上的数据
     *
     * @param wfssListData Wfss 涨跌幅数据
     * @param biiDataOne   登录前Biii数据 买入卖出价1
     * @param biiDataTwo   登录前Biii数据 买入卖出价2
     * @return 返回按照前端规定的默认顺序匹配上的bii数据
     */
    private static List<CustomLoginBeforeModel> getWfssAndBeforLoginBiiMatchData
    (List<WFSSQueryMultipleQuotationResModel.ItemsEntity> wfssListData, List<PsnGetAllExchangeRatesOutlayResModel> biiDataOne, List<PsnGetAllExchangeRatesOutlayResModel> biiDataTwo) {
        List<CustomLoginBeforeModel> mListHaveMatchData = new ArrayList<CustomLoginBeforeModel>();//有匹配的数据
        for (int i = 0; i < wfssListData.size(); i++) {//以固定顺序进行匹配
            String sourceCurrencyCodeWfss = wfssListData.get(i).getSourceCurrencyCode();
            String targetCurrencyCodeWfss = wfssListData.get(i).getTargetCurrencyCode();
            String currentyToNum = sourceCurrencyCodeWfss + targetCurrencyCodeWfss + "";
            CustomLoginBeforeModel customLoginBeforeModel = new CustomLoginBeforeModel();
            for (int j = 0; j < biiDataOne.size(); j++) {//循环判断是否有匹配数据
                String sourceCurrencyCodeBii = biiDataOne.get(j).getSourceCurrencyCode();
                String targetCurrencyCodeBii = biiDataOne.get(j).getTargetCurrencyCode();
                if (currentyToNum.equalsIgnoreCase(sourceCurrencyCodeBii + targetCurrencyCodeBii)) {
                    customLoginBeforeModel.setItemsEntity(wfssListData.get(i));
                    //有匹配数据
                    customLoginBeforeModel.setPsnGetAllExchangeRatesOutlayResModel(biiDataOne.get(j));
                    LogUtil.d("yx-----------biiDataTwo--"+biiDataTwo.size());
                    mListHaveMatchData.add(customLoginBeforeModel);
                    removeItemFormList(currentyToNum, biiDataTwo);
                }
            }
        }
        return mListHaveMatchData;
    }

    /**
     * 用于处理 没有匹配的Bii数据
     *
     * @param str      货币对
     * @param listData bii返回数据
     */

    private static void removeItemFormList(String str, List<PsnGetAllExchangeRatesOutlayResModel> listData) {
        for (int t = 0; t < listData.size(); t++) {
            String str1 = listData.get(t).getSourceCurrencyCode();
            String str2 = listData.get(t).getTargetCurrencyCode();
            if (str.equalsIgnoreCase(str1 + str2 + "")) {
                listData.remove(t);
            }
        }
    }
    //----------------------------------登录前接口处理----------end------
}
