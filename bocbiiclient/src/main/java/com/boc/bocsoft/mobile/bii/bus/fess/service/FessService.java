package com.boc.bocsoft.mobile.bii.bus.fess.service;

import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessBuyExchangeHibs.PsnFessBuyExchangeHibsParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessBuyExchangeHibs.PsnFessBuyExchangeHibsResponse;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessBuyExchangeHibs.PsnFessBuyExchangeHibsResult;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessGetUpperLimitOfForeignCurr.PsnFessGetUpperLimitOfForeignCurrParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessGetUpperLimitOfForeignCurr.PsnFessGetUpperLimitOfForeignCurrResponse;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessGetUpperLimitOfForeignCurr.PsnFessGetUpperLimitOfForeignCurrResult;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryAccount.PsnFessQueryAccountParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryAccount.PsnFessQueryAccountResponse;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryAccount.PsnFessQueryAccountResult;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryAccountBalance.PsnFessQueryAccountBalanceParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryAccountBalance.PsnFessQueryAccountBalanceResponse;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryAccountBalance.PsnFessQueryAccountBalanceResult;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryExchangeRate.PsnFessQueryExchangeRateParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryExchangeRate.PsnFessQueryExchangeRateResponse;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryExchangeRate.PsnFessQueryExchangeRateResult;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryExchangeRebateRate.PsnFessQueryExchangeRebateRateParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryExchangeRebateRate.PsnFessQueryExchangeRebateRateResponse;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryExchangeRebateRate.PsnFessQueryExchangeRebateRateResult;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryForErrormesg.PsnFessQueryForErrormesgParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryForErrormesg.PsnFessQueryForErrormesgResponse;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryForErrormesg.PsnFessQueryForErrormesgResult;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryForLimit.PsnFessQueryForLimitParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryForLimit.PsnFessQueryForLimitResponse;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryForLimit.PsnFessQueryForLimitResult;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryHibsExchangeTrans.PsnFessQueryHibsExchangeTransParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryHibsExchangeTrans.PsnFessQueryHibsExchangeTransResponse;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryHibsExchangeTrans.PsnFessQueryHibsExchangeTransResult;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryHibsExchangeTransDetail.PsnFessQueryHibsExchangeTransDetailParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryHibsExchangeTransDetail.PsnFessQueryHibsExchangeTransDetailResponse;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryHibsExchangeTransDetail.PsnFessQueryHibsExchangeTransDetailResult;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryQuotePrice.PsnFessQueryQuotePriceParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryQuotePrice.PsnFessQueryQuotePriceResponse;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryQuotePrice.PsnFessQueryQuotePriceResult;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessSellExchangeHibs.PsnFessSellExchangeHibsParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessSellExchangeHibs.PsnFessSellExchangeHibsResponse;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessSellExchangeHibs.PsnFessSellExchangeHibsResult;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessSignConfirmation4Focus.PsnFessSignConfirmation4FocusParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessSignConfirmation4Focus.PsnFessSignConfirmation4FocusResponse;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessSignConfirmation4Focus.PsnFessSignConfirmation4FocusResult;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnGetExchangeOutlay.PsnGetExchangeOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnGetExchangeOutlay.PsnGetExchangeOutlayResponse;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnGetExchangeOutlay.PsnGetExchangeOutlayResult;
import com.boc.bocsoft.mobile.bii.common.client.BIIClient;
import com.boc.bocsoft.mobile.bii.common.client.BIIClientConfig;

import java.util.List;

import rx.Observable;

/**
 * 结售汇service
 * Created by lxw on 2016/8/24 0024.
 */
public class FessService {

    /**
     * 登录前查询结售汇行情列
     * added by xdy 20160603
     *
     * @param params 参数
     */
    public Observable<List<PsnGetExchangeOutlayResult>> psnGetExchangeOutlay(PsnGetExchangeOutlayParams params) {
        return BIIClient.instance.post(BIIClientConfig.getBPMSUrl(), "PsnGetExchangeOutlay", params, PsnGetExchangeOutlayResponse.class);
    }

    /**
     * I49 4.1 001 PsnFessQueryAccount查询结售汇账户列表
     *
     * @param params
     * @return
     */
    public Observable<PsnFessQueryAccountResult> psnFessQueryAccount(PsnFessQueryAccountParams params) {
        return BIIClient.instance.post("PsnFessQueryAccount", params, PsnFessQueryAccountResponse.class);
    }

    /**
     * I49 4.6 006PsnFessQueryExchangeRate查询结购汇当前参考牌价
     * 701批次废弃 使用新接口024PsnFessQueryExchangeRebateRate查询结购汇优惠后牌价信息
     *
     * @param params
     * @return
     */
    public Observable<PsnFessQueryExchangeRateResult> psnFessQueryExchangeRate(PsnFessQueryExchangeRateParams params) {
        return BIIClient.instance.post("PsnFessQueryExchangeRate", params, PsnFessQueryExchangeRateResponse.class);
    }

    /**
     * I49 4.2 002 PsnFessQueryAccountBalance查询结售汇帐户余额
     *
     * @param params
     * @return
     */
    public Observable<List<PsnFessQueryAccountBalanceResult>> psnFessQueryAccountBalance(PsnFessQueryAccountBalanceParams params) {
        return BIIClient.instance.post("PsnFessQueryAccountBalance", params, PsnFessQueryAccountBalanceResponse.class);
    }

    /**
     * I49 4.9 009PsnFessQueryQuotePrice查询结汇购汇牌价列表
     *
     * @param params
     * @return
     */
    public Observable<PsnFessQueryQuotePriceResult> psnFessQueryQuotePrice(PsnFessQueryQuotePriceParams params) {
        return BIIClient.instance.post("PsnFessQueryQuotePrice", params, PsnFessQueryQuotePriceResponse.class);
    }

    /**
     * 4.13 013PsnFessQueryForLimit查询个人结售汇额度
     * Added by wzn7074 on 2016/11/16.
     *
     * @param params
     * @return
     */
    public Observable<PsnFessQueryForLimitResult> psnFessQueryForLimit(PsnFessQueryForLimitParams params) {
        return BIIClient.instance.post("PsnFessQueryForLimit", params, PsnFessQueryForLimitResponse.class);
    }

    /**
     * 4.14 014 PsnFessSignConfirmation4Focus 重点关注对象确认书签署
     * Added by lgw
     *
     * @param params
     * @return
     */
    public Observable<PsnFessSignConfirmation4FocusResult> psnFessSignConfirmation4FocusResult(PsnFessSignConfirmation4FocusParams params) {
        return BIIClient.instance.post("PsnFessSignConfirmation4Focus", params, PsnFessSignConfirmation4FocusResponse.class);
    }

    /**
     * I49 4.15 015PsnFessGetUpperLimitOfForeignCurr可结售汇金额上限试算
     * Added by wzn7074 on 2016/11/16.
     *
     * @param params
     * @return
     */
    public Observable<PsnFessGetUpperLimitOfForeignCurrResult> psnFessGetUpperLimitOfForeignCurr(PsnFessGetUpperLimitOfForeignCurrParams params) {
        return BIIClient.instance.post("PsnFessGetUpperLimitOfForeignCurr", params, PsnFessGetUpperLimitOfForeignCurrResponse.class);
    }

    /**
     * I49 4.16 016PsnFessBuyExchangeHibs	购汇(HIBS新)
     * Added by wzn7074 on 2016/11/16.
     *
     * @param params
     * @return
     */
    public Observable<PsnFessBuyExchangeHibsResult> psnFessBuyExchangeHibs(PsnFessBuyExchangeHibsParams params) {
        return BIIClient.instance.post("PsnFessBuyExchangeHibs", params, PsnFessBuyExchangeHibsResponse.class);
    }

    /**
     * 4.17 017PsnFessSellExchangeHibs	结汇（HIBS新)
     * create by lgw
     *
     * @param params
     * @return
     */
    public Observable<PsnFessSellExchangeHibsResult> psnFessSellExchangeHibs(PsnFessSellExchangeHibsParams params) {
        return BIIClient.instance.post("PsnFessSellExchangeHibs", params, PsnFessSellExchangeHibsResponse.class);
    }

    /**
     * I49 4.18 018PsnFessQueryHibsExchangeTrans查询全渠道机会购汇交易列表
     * Added by wzn7074 on 2016/12/02.
     *
     * @param params
     * @return
     */
    public Observable<PsnFessQueryHibsExchangeTransResult> psnFessQueryHibsExchangeTrans(PsnFessQueryHibsExchangeTransParams params) {
        return BIIClient.instance.post("PsnFessQueryHibsExchangeTrans", params, PsnFessQueryHibsExchangeTransResponse.class);
    }


    /**
     * 4.19 019PsnFessQueryHibsExchangeTransDetail查询全渠道结购汇交易详情
     * Added by wzn7074 on 2016/12/02.
     *
     * @param params
     * @return
     */
    public Observable<PsnFessQueryHibsExchangeTransDetailResult> psnFessQueryHibsExchangeTransDetail(PsnFessQueryHibsExchangeTransDetailParams params) {
        return BIIClient.instance.post("PsnFessQueryHibsExchangeTransDetail", params, PsnFessQueryHibsExchangeTransDetailResponse.class);
    }

    /**
     * I49 4.20 020PsnFessQueryForErrormesg查询交易失败原因信息
     * Added by wzn7074 on 2016/11/16.
     *
     * @param params
     * @return
     */
    public Observable<PsnFessQueryForErrormesgResult> psnFessQueryForErrormesg(PsnFessQueryForErrormesgParams params) {
        return BIIClient.instance.post("PsnFessQueryForErrormesg", params, PsnFessQueryForErrormesgResponse.class);
    }

    /**
     * I49 4.21 024PsnFessQueryExchangeRebateRate查询结购汇优惠后牌价信息
     * Added by wzn7074 on 2016/11/16.
     *
     * @param params
     * @return
     */
    public Observable<PsnFessQueryExchangeRebateRateResult> psnFessQueryExchangeRebateRate(PsnFessQueryExchangeRebateRateParams params) {
        return BIIClient.instance.post("PsnFessQueryExchangeRebateRate", params, PsnFessQueryExchangeRebateRateResponse.class);
    }
}

