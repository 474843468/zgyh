package com.boc.bocsoft.mobile.bii.bus.longshortforex.service;

import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnGetAllExchangeRatesOutlay.PsnGetAllExchangeRatesOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnGetAllExchangeRatesOutlay.PsnGetAllExchangeRatesOutlayResponse;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnGetAllExchangeRatesOutlay.PsnGetAllExchangeRatesOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnQuerySingleQuotation.PsnQuerySingleQuotationParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnQuerySingleQuotation.PsnQuerySingleQuotationResponse;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnQuerySingleQuotation.PsnQuerySingleQuotationResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailAccountInfoListQuery.PsnVFGBailAccountInfoListQueryParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailAccountInfoListQuery.PsnVFGBailAccountInfoListQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailAccountInfoListQuery.PsnVFGBailAccountInfoListQueryResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailDetailQuery.PsnVFGBailDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailDetailQuery.PsnVFGBailDetailQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailDetailQuery.PsnVFGBailDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailListQuery.PsnVFGBailListQueryParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailListQuery.PsnVFGBailListQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailListQuery.PsnVFGBailListQueryResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailProductQuery.PsnVFGBailProductQueryParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailProductQuery.PsnVFGBailProductQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailProductQuery.PsnVFGBailProductQueryResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailTransfer.PsnVFGBailTransferParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailTransfer.PsnVFGBailTransferResponse;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailTransfer.PsnVFGBailTransferResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGCancelContract.PsnVFGCancelContractParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGCancelContract.PsnVFGCancelContractResponse;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGCancelContract.PsnVFGCancelContractResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGCancelOrder.PsnVFGCancelOrderParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGCancelOrder.PsnVFGCancelOrderResponse;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGCancelOrder.PsnVFGCancelOrderResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGChangeContract.PsnVFGChangeContractParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGChangeContract.PsnVFGChangeContractResponse;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGChangeContract.PsnVFGChangeContractResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGFilterDebitCard.PsnVFGFilterDebitCardParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGFilterDebitCard.PsnVFGFilterDebitCardResponse;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGFilterDebitCard.PsnVFGFilterDebitCardResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGGetBindAccount.PsnVFGGetBindAccountParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGGetBindAccount.PsnVFGGetBindAccountResponse;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGGetBindAccount.PsnVFGGetBindAccountResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGGetRegCurrency.PsnVFGGetRegCurrencyParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGGetRegCurrency.PsnVFGGetRegCurrencyResponse;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGPositionInfo.PsnVFGPositionInfoParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGPositionInfo.PsnVFGPositionInfoResponse;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGPositionInfo.PsnVFGPositionInfoResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGQueryMaxAmount.PsnVFGQueryMaxAmountParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGQueryMaxAmount.PsnVFGQueryMaxAmountResponse;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGQueryMaxAmount.PsnVFGQueryMaxAmountResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGSetTradeAccount.PsnVFGSetTradeAccountParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGSetTradeAccount.PsnVFGSetTradeAccountResponse;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGSetTradeAccount.PsnVFGSetTradeAccountResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGSignPre.PsnVFGSignPreParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGSignPre.PsnVFGSignPreResponse;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGSignPre.PsnVFGSignPreResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGSignSubmit.PsnVFGSignSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGSignSubmit.PsnVFGSignSubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGSignSubmit.PsnVFGSignSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTrade.PsnVFGTradeParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTrade.PsnVFGTradeResponse;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTrade.PsnVFGTradeResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTradeConfirm.PsnVFGTradeConfirmParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTradeConfirm.PsnVFGTradeConfirmResponse;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTradeDetailQuery.PsnVFGTradeDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTradeDetailQuery.PsnVFGTradeDetailQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTradeDetailQuery.PsnVFGTradeDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTradeInfoQuery.PsnVFGTradeInfoQueryParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTradeInfoQuery.PsnVFGTradeInfoQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTradeInfoQuery.PsnVFGTradeInfoQueryResult;
import com.boc.bocsoft.mobile.bii.common.client.BIIClient;
import com.boc.bocsoft.mobile.bii.common.client.BIIClientConfig;

import java.math.BigDecimal;
import java.util.List;

import rx.Observable;

/**
 * Created by zhangc7067 on 2016/11/17.
 * <p/>
 * 双向宝 service  I50  and  WFSS、 I43
 */
public class LongShortForexService {

    /**
     * I50 012 PsnVFGTradeInfoQuery 交易查询
     *
     * @param params
     * @return
     * @author zhangc
     */
    public Observable<PsnVFGTradeInfoQueryResult> psnXpadTradeInfoQuery(PsnVFGTradeInfoQueryParams params) {
        return BIIClient.instance.post("PsnVFGTradeInfoQuery", params, PsnVFGTradeInfoQueryResponse.class);
    }

    /**
     * I50 014 PsnVFGCancelOrder 委托撤单
     *
     * @param params
     * @return
     * @author zhangc
     */
    public Observable<PsnVFGCancelOrderResult> psnXpadVFGCancelOrder(PsnVFGCancelOrderParams params) {
        return BIIClient.instance.post("PsnVFGCancelOrder", params, PsnVFGCancelOrderResponse.class);
    }

    /**
     * I50 026 PsnVFGTradeDetailQuery 保证金单笔交易明细查询
     *
     * @param params
     * @return
     * @author zhangc
     */
    public Observable<PsnVFGTradeDetailQueryResult> psnXpadVFGTradeDetailQuery(PsnVFGTradeDetailQueryParams params) {
        return BIIClient.instance.post("PsnVFGTradeDetailQuery", params, PsnVFGTradeDetailQueryResponse.class);
    }

    /**
     * I50 002 PsnVFGGetRegCurrency 获取结算币种
     *
     * @param params
     * @return
     * @author zhangc
     */
    public Observable<List<String>> psnXpadGetRegCurrency(PsnVFGGetRegCurrencyParams params) {
        return BIIClient.instance.post("PsnVFGGetRegCurrency", params, PsnVFGGetRegCurrencyResponse.class);
    }

    /**
     * I50 004 PsnVFGPositionInfo 双向宝持仓信息
     *
     * @param params
     * @return
     * @author zhangc
     */
    public Observable<List<PsnVFGPositionInfoResult>> psnXpadVFGPositionInfo(PsnVFGPositionInfoParams params) {
        return BIIClient.instance.post("PsnVFGGetRegCurrency", params, PsnVFGPositionInfoResponse.class);
    }

    /**
     * I50 028 PsnVFGBailAccountInfoListQuery 双向宝保证金账户基本信息多笔查询
     *
     * @param params
     * @return
     * @author zhx
     */
    public Observable<PsnVFGBailAccountInfoListQueryResult> psnVFGBailAccountInfoListQuery(PsnVFGBailAccountInfoListQueryParams params) {
        return BIIClient.instance.post("PsnVFGBailAccountInfoListQuery", params, PsnVFGBailAccountInfoListQueryResponse.class);
    }

    /**
     * I50 022 PsnVFGBailDetailQuery 查询保证金账户详情
     *
     * @param params
     * @return
     * @author zhx
     */
    public Observable<PsnVFGBailDetailQueryResult> psnVFGBailDetailQuery(PsnVFGBailDetailQueryParams params) {
        return BIIClient.instance.post("PsnVFGBailDetailQuery", params, PsnVFGBailDetailQueryResponse.class);
    }

    /**
     * I50 021 PsnVFGBailListQuery 查询保证金账户列表
     *
     * @param params
     * @return
     * @author zhx
     */
    public Observable<List<PsnVFGBailListQueryResult>> psnVFGBailListQuery(PsnVFGBailListQueryParams params) {
        return BIIClient.instance.post("PsnVFGBailListQuery", params, PsnVFGBailListQueryResponse.class);
    }

    /**
     * I50 023 PsnVFGBailProductQuery 查询可签约保证金产品
     *
     * @param params
     * @return
     * @author zhx
     */
    public Observable<PsnVFGBailProductQueryResult> psnVFGBailProductQuery(PsnVFGBailProductQueryParams params) {
        return BIIClient.instance.post("PsnVFGBailProductQuery", params, PsnVFGBailProductQueryResponse.class);
    }

    /**
     * I50 019 PsnVFGCancelContract 双向宝解约
     *
     * @param params
     * @return
     * @author zhx
     */
    public Observable<PsnVFGCancelContractResult> psnVFGCancelContract(PsnVFGCancelContractParams params) {
        return BIIClient.instance.post("PsnVFGCancelContract", params, PsnVFGCancelContractResponse.class);
    }

    /**
     * I50 020 PsnVFGChangeContract 变更签约账户
     *
     * @param params
     * @return
     * @author zhx
     */
    public Observable<PsnVFGChangeContractResult> psnVFGChangeContract(PsnVFGChangeContractParams params) {
        return BIIClient.instance.post("PsnVFGChangeContract", params, PsnVFGChangeContractResponse.class);
    }

    /**
     * I50 005 PsnVFGGetBindAccount 双向宝交易账户
     *
     * @param params
     * @return
     * @author zhx
     */
    public Observable<PsnVFGGetBindAccountResult> psnVFGGetBindAccount(PsnVFGGetBindAccountParams params) {
        return BIIClient.instance.post("PsnVFGGetBindAccount", params, PsnVFGGetBindAccountResponse.class);
    }

    /**
     * I50 001 PsnVFGSetTradeAccount 首次/重新设定双向宝账户
     *
     * @param params
     * @return
     * @author zhx
     */
    public Observable<PsnVFGSetTradeAccountResult> psnVFGSetTradeAccount(PsnVFGSetTradeAccountParams params) {
        return BIIClient.instance.post("PsnVFGSetTradeAccount", params, PsnVFGSetTradeAccountResponse.class);
    }

    /**
     * I50 017 PsnVFGSignPre 双向宝签约预交易
     *
     * @param params
     * @return
     * @author zhx
     */
    public Observable<PsnVFGSignPreResult> psnVFGSignPre(PsnVFGSignPreParams params) {
        return BIIClient.instance.post("PsnVFGSignPre", params, PsnVFGSignPreResponse.class);
    }

    /**
     * I50 018 PsnVFGSignSubmit 双向宝签约提交交易
     *
     * @param params
     * @return
     * @author zhx
     */
    public Observable<PsnVFGSignSubmitResult> psnVFGSignSubmit(PsnVFGSignSubmitParams params) {
        return BIIClient.instance.post("PsnVFGSignSubmit", params, PsnVFGSignSubmitResponse.class);
    }


    /**
     * I50 022 PsnVFGPositionInfo 查询
     *
     * @param params
     * @return
     * @author gengjunying
     */
    public Observable<List<PsnVFGPositionInfoResult>> PsnVFGPositionInfo(PsnVFGPositionInfoParams params) {
        return BIIClient.instance.post("PsnVFGPositionInfo", params, PsnVFGPositionInfoResponse.class);
    }

    /**
     * I50 025 PsnVFGQueryMaxAmount 保证金账户可转出最大金额查询
     *
     * @param params
     * @return
     * @author zhx
     */
    public Observable<PsnVFGQueryMaxAmountResult> psnVFGQueryMaxAmount(PsnVFGQueryMaxAmountParams params) {
        return BIIClient.instance.post("PsnVFGQueryMaxAmount", params, PsnVFGQueryMaxAmountResponse.class);
    }

    /**
     * I50 010 PsnVFGBailTransfer 保证金存入/转出
     *
     * @param params
     * @return
     * @author hty
     */
    public Observable<PsnVFGBailTransferResult> psnVFGBailTransfer(PsnVFGBailTransferParams params) {
        return BIIClient.instance.post("PsnVFGBailTransfer", params, PsnVFGBailTransferResponse.class);
    }

    /**
     * querySingelQuotation 外汇、贵金属单笔行情查询
     *
     * @param params
     * @return
     */
    public Observable<PsnQuerySingleQuotationResult> querySingleQuotation(PsnQuerySingleQuotationParams params) {
        return BIIClient.instance.post("querySingelQuotation", params, PsnQuerySingleQuotationResponse.class);
    }

    /**
     * I50 016 PsnVFGTradeConfirm 买卖交易确认
     *
     * @param params
     * @return
     */
    public Observable<BigDecimal> psnVFGTradeConfirm(PsnVFGTradeConfirmParams params) {
        return BIIClient.instance.post("PsnVFGTradeConfirm", params, PsnVFGTradeConfirmResponse.class);
    }

    /**
     * I50 011 PsnVFGTrade 买卖交易
     *
     * @param params
     * @return
     */
    public Observable<PsnVFGTradeResult> psnVFGTradeResult(PsnVFGTradeParams params) {
        return BIIClient.instance.post("PsnVFGTrade", params, PsnVFGTradeResponse.class);
    }

    /**
     * I50 009 PsnVFGFilterDebitCard 过滤出符合条件的借记卡账户
     *
     * @param params
     * @return
     */
    public Observable<List<PsnVFGFilterDebitCardResult>> psnVFGFilterDebitCard(PsnVFGFilterDebitCardParams params) {
        return BIIClient.instance.post("PsnVFGFilterDebitCard", params, PsnVFGFilterDebitCardResponse.class);
    }

    /**
     * I43 双向宝-买入卖出价数据：PsnGetAllExchangeRatesOutlay
     * {I43-4.18 018 PsnGetAllExchangeRatesOutlay登录前贵金属、外汇、双向宝行情查询}
     *
     * @param params
     * @return
     * @author yx
     * @date 2016-12-15 11:29:33
     */
    public Observable<List<PsnGetAllExchangeRatesOutlayResult>> psnGetAllExchangeRatesOutlay(PsnGetAllExchangeRatesOutlayParams params) {
        return BIIClient.instance.post(BIIClientConfig.getBPMSUrl(), "PsnGetAllExchangeRatesOutlay", params, PsnGetAllExchangeRatesOutlayResponse.class);
    }

}
