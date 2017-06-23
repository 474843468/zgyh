package com.boc.bocsoft.mobile.bii.bus.fund.service;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnCancelFundAccount.PsnCancelFundAccountParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnCancelFundAccount.PsnCancelFundAccountResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnCancelFundAccount.PsnCancelFundAccountResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFincAccountList.PsnFincAccountListParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFincAccountList.PsnFincAccountListResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFincAccountList.PsnFincAccountListResultBean;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFincQueryQccBalance.PsnFincQueryQccBalanceParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFincQueryQccBalance.PsnFincQueryQccBalanceResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFincQueryQccBalance.PsnFincQueryQccBalanceResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAppointCancel.PsnFundAppointCancelParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAppointCancel.PsnFundAppointCancelResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAppointCancel.PsnFundAppointCancelResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAttentionAdd.PsnFundAttentionAddParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAttentionAdd.PsnFundAttentionAddResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAttentionAdd.PsnFundAttentionAddResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAttentionCancel.PsnFundAttentionCancelParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAttentionCancel.PsnFundAttentionCancelResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAttentionCancel.PsnFundAttentionCancelResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAttentionQueryList.PsnFundAttentionQueryListParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAttentionQueryList.PsnFundAttentionQueryListResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAttentionQueryList.PsnFundAttentionQueryListResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundBonusResult.PsnFundBonusParmas;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundBonusResult.PsnFundBonusResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundBonusResult.PsnFundBonusResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundBuy.PsnFundBuyParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundBuy.PsnFundBuyResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundBuy.PsnFundBuyResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCanDealDateQuery.PsnFundCanDealDateQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCanDealDateQuery.PsnFundCanDealDateQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCanDealDateQuery.PsnFundCanDealDateQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundChangeCard.PsnFundChangeCardParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundChangeCard.PsnFundChangeCardResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundChangeCard.PsnFundChangeCardResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCompanyInfoQuery.PsnFundCompanyInfoQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCompanyInfoQuery.PsnFundCompanyInfoQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCompanyInfoQuery.PsnFundCompanyInfoQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCompanyQueryOutlay.PsnFundCompanyQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCompanyQueryOutlay.PsnFundCompanyQueryOutlayResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCompanyQueryOutlay.PsnFundCompanyQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundConsignAbort.PsnFundConsignAbortParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundConsignAbort.PsnFundConsignAbortResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundConsignAbort.PsnFundConsignAbortResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundConversionInput.PsnFundConversionInputParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundConversionInput.PsnFundConversionInputResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundConversionInput.PsnFundConversionInputResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundConversionResult.PsnFundConversionParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundConversionResult.PsnFundConversionResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundConversionResult.PsnFundConversionResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundDdAbort.PsnFundDdAbortParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundDdAbort.PsnFundDdAbortResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundDdAbort.PsnFundDdAbortResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundDetailQueryOutlay.PsnFundDetailQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundDetailQueryOutlay.PsnFundDetailQueryOutlayResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundDetailQueryOutlay.PsnFundDetailQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundNightBonusResult.PsnFundNightBonusParmas;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundNightBonusResult.PsnFundNightBonusResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundNightBonusResult.PsnFundNightBonusResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundNightBuy.PsnFundNightBuyParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundNightBuy.PsnFundNightBuyResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundNightBuy.PsnFundNightBuyResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundNightConversionResult.PsnFundNightConversionParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundNightConversionResult.PsnFundNightConversionResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundNightConversionResult.PsnFundNightConversionResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundNightSell.PsnFundNightSellParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundNightSell.PsnFundNightSellResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundNightSell.PsnFundNightSellResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryFundBalance.PsnFundQueryFundBalanceParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryFundBalance.PsnFundQueryFundBalanceResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryFundBalance.PsnFundQueryFundBalanceResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryHistoryDetail.PsnFundQueryHistoryDetailParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryHistoryDetail.PsnFundQueryHistoryDetailResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryHistoryDetail.PsnFundQueryHistoryDetailResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryOutlay.PsnFundQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryOutlay.PsnFundQueryOutlayResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryOutlay.PsnFundQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryTransOntran.PsnFundQueryTransOntranParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryTransOntran.PsnFundQueryTransOntranResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryTransOntran.PsnFundQueryTransOntranResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQuickSell.PsnFundQuickSellParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQuickSell.PsnFundQuickSellResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQuickSell.PsnFundQuickSellResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundRiskEvaluationQuery.PsnFundRiskEvaluationQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundRiskEvaluationQuery.PsnFundRiskEvaluationQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundRiskEvaluationQuery.PsnFundRiskEvaluationQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundRiskEvaluationSubmit.PsnFundRiskEvaluationSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundRiskEvaluationSubmit.PsnFundRiskEvaluationSubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundRiskEvaluationSubmit.PsnFundRiskEvaluationSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduleSellCancel.PsnFundScheduleSellCancelParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduleSellCancel.PsnFundScheduleSellCancelResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduleSellCancel.PsnFundScheduleSellCancelResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledBuy.PsnFundScheduledBuyParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledBuy.PsnFundScheduledBuyResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledBuy.PsnFundScheduledBuyResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledBuyDetailQuery.PsnFundScheduledBuyDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledBuyDetailQuery.PsnFundScheduledBuyDetailQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledBuyDetailQuery.PsnFundScheduledBuyDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledBuyModify.PsnFundScheduledBuyModifyParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledBuyModify.PsnFundScheduledBuyModifyResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledBuyModify.PsnFundScheduledBuyModifyResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledBuyPauseResume.PsnFundScheduledBuyPauseResumeParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledBuyPauseResume.PsnFundScheduledBuyPauseResumeResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledBuyPauseResume.PsnFundScheduledBuyPauseResumeResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledSell.PsnFundScheduledSellParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledSell.PsnFundScheduledSellResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledSell.PsnFundScheduledSellResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledSellDetailQuery.PsnFundScheduledSellDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledSellDetailQuery.PsnFundScheduledSellDetailQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledSellDetailQuery.PsnFundScheduledSellDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledSellModify.PsnFundScheduledSellModifyParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledSellModify.PsnFundScheduledSellModifyResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledSellModify.PsnFundScheduledSellModifyResullt;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledSellPauseResume.PsnFundScheduledSellPauseResumeParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledSellPauseResume.PsnFundScheduledSellPauseResumeResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledSellPauseResume.PsnFundScheduledSellPauseResumeResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundSell.PsnFundSellParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundSell.PsnFundSellResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundSell.PsnFundSellResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundSignElectronicContract.PsnFundSignElectronicContractParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundSignElectronicContract.PsnFundSignElectronicContractResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundSignElectronicContract.PsnFundSignElectronicContractResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundStatementBalanceQuery.PsnFundStatementBalanceQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundStatementBalanceQuery.PsnFundStatementBalanceQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundStatementBalanceQuery.PsnFundStatementBalanceQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundStatusDdApplyQuery.PsnFundStatusDdApplyQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundStatusDdApplyQuery.PsnFundStatusDdApplyQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundStatusDdApplyQuery.PsnFundStatusDdApplyQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundTAAccount.PsnFundTAAccountParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundTAAccount.PsnFundTAAccountResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundTAAccount.PsnFundTAAccountResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnGetFundCompanyList.PsnGetFundCompanyListParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnGetFundCompanyList.PsnGetFundCompanyListResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnGetFundCompanyList.PsnGetFundCompanyListResultBean;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnGetFundDetail.PsnGetFundDetailParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnGetFundDetail.PsnGetFundDetailResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnGetFundDetail.PsnGetFundDetailResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnGetFundRegCompanyList.PsnGetFundRegCompanyListParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnGetFundRegCompanyList.PsnGetFundRegCompanyListResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnGetFundRegCompanyList.PsnGetFundRegCompanyListResultBean;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnPersionalTransDetailQuery.PsnPersionalTransDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnPersionalTransDetailQuery.PsnPersionalTransDetailQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnPersionalTransDetailQuery.PsnPersionalTransDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryFloatProfitAndLoss.PsnQueryFloatProfitAndLossParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryFloatProfitAndLoss.PsnQueryFloatProfitAndLossResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryFloatProfitAndLoss.PsnQueryFloatProfitAndLossResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryFundDetail.PsnQueryFundDetailParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryFundDetail.PsnQueryFundDetailResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryFundDetail.PsnQueryFundDetailResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryTaAccountDetail.PsnQueryTaAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryTaAccountDetail.PsnQueryTaAccountDetailResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryTaAccountDetail.PsnQueryTaAccountDetailResultBean;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQuickSellQuotaQuery.PsnQuickSellQuotaQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQuickSellQuotaQuery.PsnQuickSellQuotaQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQuickSellQuotaQuery.PsnQuickSellQuotaQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnScheduledFundUnavailableQuery.PsnScheduledFundUnavailableQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnScheduledFundUnavailableQuery.PsnScheduledFundUnavailableQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnScheduledFundUnavailableQuery.PsnScheduledFundUnavailableQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnTaAccountCancel.PsnTaAccountCancelParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnTaAccountCancel.PsnTaAccountCancelResponse;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnTaAccountCancel.PsnTaAccountCancelResult;
import com.boc.bocsoft.mobile.bii.common.client.BIIClient;
import com.boc.bocsoft.mobile.bii.common.client.BIIClientConfig;

import java.util.List;

import rx.Observable;

/**
 * 中银基金service
 * Created by lxw on 2016/8/4 0004.
 */
public class FundService {

    /**
     * 登录前基金详情查询
     *added by xdy 20160603
     * @param params 参数
     */
    public Observable<PsnFundDetailQueryOutlayResult> psnFundDetailQueryOutlay(PsnFundDetailQueryOutlayParams params) {
//        return BIIClient.instance.post(BIIClientConfig.getBiiUrl(), "PsnFundDetailQueryOutlay",
//                params, PsnFundDetailQueryOutlayResponse.class);

        return BIIClient.instance.post(BIIClientConfig.getBPMSUrl(), "PsnFundDetailQueryOutlay",
                params, PsnFundDetailQueryOutlayResponse.class);
    }



    /**
     * 登录前基金行情查询
     *added by xdy 20160603
     * @param params 参数
     */
    public Observable<PsnFundQueryOutlayResult> psnFundQueryOutlay (PsnFundQueryOutlayParams params) {
//        return BIIClient.instance.post(BIIClientConfig.getBiiUrl(), "PsnFundQueryOutlay", params, PsnFundQueryOutlayResponse.class);
        return BIIClient.instance.post(BIIClientConfig.getBPMSUrl(), "PsnFundQueryOutlay", params, PsnFundQueryOutlayResponse.class);
    }


    /**
     * 基金信息查询（查询基金行情
     *added by xdy 20160603
     * @param params 参数
     */
    public Observable<PsnQueryFundDetailResult> psnQueryFundDetail(PsnQueryFundDetailParams params) {
//        return BIIClient.instance.post(BIIClientConfig.getBiiUrl(), "PsnQueryFundDetail", params, PsnQueryFundDetailResponse.class);
        return BIIClient.instance.post("PsnQueryFundDetail", params, PsnQueryFundDetailResponse.class);
    }


    /**
     * 4.9 009 PsnGetFundDetail基金基本信息查询（查询基金详情）
     * added by xdy 20160603
     *
     * @param params 参数
     */
    public Observable<PsnGetFundDetailResult> psnGetFundDetail(PsnGetFundDetailParams params) {
        return BIIClient.instance.post( "PsnGetFundDetail", params, PsnGetFundDetailResponse.class);
    }

    /**
     * 查询风险评估
     *
     * @param params
     * @return
     */
    public Observable<PsnFundRiskEvaluationQueryResult> psnFundRiskEvaluationQuery(PsnFundRiskEvaluationQueryParams params) {
        return BIIClient.instance.post("PsnFundRiskEvaluationQueryResult",params, PsnFundRiskEvaluationQueryResponse.class);
    }

    /**
     * 提交风险评估结果
     *
     * @param params
     * @return
     */
    public Observable<PsnFundRiskEvaluationSubmitResult> psnFundRiskEvaluationSubmit(PsnFundRiskEvaluationSubmitParams params) {
        return BIIClient.instance.post("PsnFundRiskEvaluationSubmitResult", params, PsnFundRiskEvaluationSubmitResponse.class);
    }

    /**
     * 046 在途交易查询
     *
     * @param params
     * @return
     */
    public Observable<PsnFundQueryTransOntranResult> psnFundQueryTransOntran(PsnFundQueryTransOntranParams params) {
        return BIIClient.instance.post("PsnFundQueryTransOntran", params, PsnFundQueryTransOntranResponse.class);
    }

    /**
     * 011定期定额申请查询
     *
     * @param params
     * @return
     */
    public Observable<PsnFundStatusDdApplyQueryResult> psnFundStatusDdApplyQuery(PsnFundStatusDdApplyQueryParams params) {
        return BIIClient.instance.post("PsnFundStatusDdApplyQuery", params, PsnFundStatusDdApplyQueryResponse.class);
    }

    /**
     * 053基金对账单持仓信息查询
     *
     * @param params
     * @return
     */
    public Observable<PsnFundStatementBalanceQueryResult> psnFundStatementBalanceQuery(PsnFundStatementBalanceQueryParams params) {
        return BIIClient.instance.post("PsnFundStatementBalanceQuery", params, PsnFundStatementBalanceQueryResponse.class);
    }

    /**
     * 059交易流水信息查询
     *
     * @param params
     * @return
     */
    public Observable<List<PsnPersionalTransDetailQueryResult>> psnPersionalTransDetailQuery (PsnPersionalTransDetailQueryParams params){
        return BIIClient.instance.post("PsnPersionalTransDetailQuery", params, PsnPersionalTransDetailQueryResponse.class);
    }

    /**
     * 054定投申请明细查询
     * @param params
     * @return
     */
    public Observable<PsnFundScheduledBuyDetailQueryResult> psnFundScheduledBuyDetailQuery (PsnFundScheduledBuyDetailQueryParams params) {
        return BIIClient.instance.post("PsnFundScheduledBuyDetailQuery", params, PsnFundScheduledBuyDetailQueryResponse.class);
    }

    /**
     * 055定赎申请明细查询
     * @param params
     * @return
     */
    public Observable<PsnFundScheduledSellDetailQueryResult> psnFundScheduledSellDetailQuery (PsnFundScheduledSellDetailQueryParams params) {
        return BIIClient.instance.post("PsnFundScheduledSellDetailQuery", params, PsnFundScheduledSellDetailQueryResponse.class);
    }

    /**
     * 基金公司查询
     * @param params
     * @return
     */
    public Observable<List<PsnGetFundCompanyListResultBean>> psnGetFundCompanyList(PsnGetFundCompanyListParams params){
        return BIIClient.instance.post("PsnGetFundCompanyList", params, PsnGetFundCompanyListResponse.class);
    }
    /**
     * 015查询基金持仓信息
     * @param params
     * @return
     */
    public Observable<PsnFundQueryFundBalanceResult> psnFundQueryFundBalance (PsnFundQueryFundBalanceParams params) {
        return BIIClient.instance.post("PsnFundQueryFundBalance", params, PsnFundQueryFundBalanceResponse.class);
    }

    /**
     * 061基金公司信息查询
     * @param params
     * @return
     */
    public Observable<PsnFundCompanyInfoQueryResult> psnFundCompanyInfoQuery (PsnFundCompanyInfoQueryParams params) {
        return BIIClient.instance.post("PsnFundCompanyInfoQuery", params, PsnFundCompanyInfoQueryResponse.class);
    }

    /**
     *038基金定期定额申购修改
     * @param params
     * @return
     */
    public Observable<PsnFundScheduledBuyModifyResult> psnFundScheduledBuyModify (PsnFundScheduledBuyModifyParams params) {
        return BIIClient.instance.post("PsnFundScheduledBuyModify", params, PsnFundScheduledBuyModifyResponse.class);
    }

    /**
     *039基金定期定额赎回修改
     *@param params
     * @return
     */
    public Observable<PsnFundScheduledSellModifyResullt> psnFundScheduledSellModify (PsnFundScheduledSellModifyParams params) {
        return BIIClient.instance.post("PsnFundScheduledSellModify", params, PsnFundScheduledSellModifyResponse.class);
    }

    /**
     * 040定期定额赎回撤销
     * @param params
     * @return
     */
    public Observable<PsnFundScheduleSellCancelResult> psnFundScheduleSellCancel (PsnFundScheduleSellCancelParams params) {
        return BIIClient.instance.post("PsnFundScheduleSellCancel", params, PsnFundScheduleSellCancelResponse.class);
    }

    /**
     * 014定期定额申购撤销
     * @param params
     * @return
     *
     */
    public Observable<PsnFundDdAbortResult> psnFundDdAbort (PsnFundDdAbortParams params) {
        return BIIClient.instance.post("PsnFundDdAbort" , params, PsnFundDdAbortResponse.class);
    }

    /**
     * 056定投暂停/开通
     *
     *
     */
    public Observable<PsnFundScheduledBuyPauseResumeResult> psnFundScheduledBuyPauseResume (PsnFundScheduledBuyPauseResumeParams params) {
        return BIIClient.instance.post("PsnFundScheduledBuyPauseResume", params, PsnFundScheduledBuyPauseResumeResponse.class);
    }

    /**
     * 057定赎暂停/开通
     * @param params
     * @return
     */
    public Observable<PsnFundScheduledSellPauseResumeResult> psnFundScheduledSellPauseResume (PsnFundScheduledSellPauseResumeParams params){
        return BIIClient.instance.post("PsnFundScheduledSellPauseResume", params, PsnFundScheduledSellPauseResumeResponse.class);
    }

    /**
     * 058失效定期定额查询
     * @param params
     * @return
     */
    public Observable<PsnScheduledFundUnavailableQueryResult> psnScheduledFundUnavailableQuery (PsnScheduledFundUnavailableQueryParams params) {
        return BIIClient.instance.post("PsnScheduledFundUnavailableQuery", params, PsnScheduledFundUnavailableQueryResponse.class);
    }

    /**
     * 021基金撤单
     * @param params
     * @return
     */
    public Observable<PsnFundConsignAbortResult> psnFundConsignAbort (PsnFundConsignAbortParams params){
        return BIIClient.instance.post("PsnFundConsignAbort", params, PsnFundConsignAbortResponse.class);
    }

    /**
     * 036基金指定日期交易撤单
     * @param params
     * @return
     */
    public Observable<PsnFundAppointCancelResult> psnFundAppointCancel (PsnFundAppointCancelParams params){
        return BIIClient.instance.post("PsnFundAppointCancel", params, PsnFundAppointCancelResponse.class);
    }

    /**
     * 047查询历史交易信息
     * @param params
     * @return
     */
    public Observable<PsnFundQueryHistoryDetailResult> psnFundQueryHistoryDetail (PsnFundQueryHistoryDetailParams params){
        return BIIClient.instance.post("PsnFundQueryHistoryDetail", params, PsnFundQueryHistoryDetailResponse.class);
    }

    /**
     * 024基金买入
     * @param params
     * @return
     */
    public Observable<PsnFundBuyResult> psnFundBuy (PsnFundBuyParams params){
        return BIIClient.instance.post("PsnFundBuy", params, PsnFundBuyResponse.class);
    }

    /**
     * 025基金买入（挂单）
     * @param params
     * @return
     */
    public Observable<PsnFundNightBuyResult> psnFundNightBuy (PsnFundNightBuyParams params){
        return BIIClient.instance.post("PsnFundNightBuy", params, PsnFundNightBuyResponse.class);
    }

    /**
     * 063基金可指定日期查询
     * @param params
     * @return
     */
    public Observable<PsnFundCanDealDateQueryResult> psnFundCanDealDateQuery (PsnFundCanDealDateQueryParams params){
        return BIIClient.instance.post("PsnFundCanDealDateQuery", params, PsnFundCanDealDateQueryResponse.class);
    }

    /**
     * 010 PsnFundTAAccount登记基金TA帐户
     * @param params
     * @return
     */
    public Observable<PsnFundTAAccountResult>PsnFundTAAccount (PsnFundTAAccountParams params){
        return BIIClient.instance.post("PsnFundTAAccount", params, PsnFundTAAccountResponse.class);
    }

    /**
     * 044 PsnQueryTaAccountDetail  Ta账户信息查询
     * @param params
     * @return
     */
    public Observable<List<PsnQueryTaAccountDetailResultBean>> PsnQueryTaAccountDetail(PsnQueryTaAccountDetailParams params){
        return BIIClient.instance.post("PsnQueryTaAccountDetail", params, PsnQueryTaAccountDetailResponse.class);
    }

    /**
     * 045 PsnTaAccountCancel  Ta账户取消关联/销户
     * @param params
     * @return
     */
    public Observable<PsnTaAccountCancelResult> PsnTaAccountCancel (PsnTaAccountCancelParams params){
        return BIIClient.instance.post("PsnTaAccountCancel", params, PsnTaAccountCancelResponse.class);
    }

    /**
     * 048 PsnCancelFundAccount  基金交易账户销户
     * @param params
     * @return
     */
    public Observable<PsnCancelFundAccountResult> PsnCancelFundAccount (PsnCancelFundAccountParams params){
        return BIIClient.instance.post("PsnCancelFundAccount", params, PsnCancelFundAccountResponse.class);
    }

    /**
     * 049 PsnFundChangeCard 变更资金账户
     * @param params
     * @return
     */
    public Observable<PsnFundChangeCardResult> PsnFundChangeCard (PsnFundChangeCardParams params){
        return BIIClient.instance.post("PsnFundChangeCard", params, PsnFundChangeCardResponse.class);
    }

    /**
     * 076 PsnFincAccountList 获取资金账号列表
     * @param params
     * @return
     */
    public Observable<List<PsnFincAccountListResultBean>> PsnFincAccountList(PsnFincAccountListParams params){
        return BIIClient.instance.post("PsnFincAccountList", params, PsnFincAccountListResponse.class);
    }

    /**
     * 080 PsnFincQueryQccBalance 查询资金账户余额(QCC)
     * @param params
     * @return
     */
    public Observable<PsnFincQueryQccBalanceResult> PsnFincQueryQccBalance (PsnFincQueryQccBalanceParams params){
        return BIIClient.instance.post("PsnFincQueryQccBalance", params, PsnFincQueryQccBalanceResponse.class);
    }

    /**
     *  033 PsnGetFundRegCompanyList基金公司查询（注册登记机构查询）
     * @param params
     * @return
     */
       public Observable<List<PsnGetFundRegCompanyListResultBean>> PsnGetFundRegCompanyList (PsnGetFundRegCompanyListParams params){
           return BIIClient.instance.post("PsnGetFundRegCompanyList", params, PsnGetFundRegCompanyListResponse.class);
    }

    /**
     * 035 PsnQueryFloatProfitAndLoss浮动盈亏试算
     *
     * @param params
     * @return
     */
    public Observable<List<PsnQueryFloatProfitAndLossResult>> psnQueryFloatProfitAndLoss(PsnQueryFloatProfitAndLossParams params) {
        return BIIClient.instance.post("PsnQueryFloatProfitAndLoss", params, PsnQueryFloatProfitAndLossResponse.class);
    }

    /**
     * 004 PsnFundSignElectronicContract 签约电子合同
     *
     * @param params
     * @return
     */
    public Observable<PsnFundSignElectronicContractResult> PsnFundSignElectronicContract (PsnFundSignElectronicContractParams params){
        return BIIClient.instance.post("PsnFundSignElectronicContract", params, PsnFundSignElectronicContractResponse.class);
    }

    /**
     * 031 PsnFundBonusResult基金修改分红方式
     *
     * @param parmas
     * @return
     */
    public Observable<PsnFundBonusResult> psnFundBonusResult(PsnFundBonusParmas parmas) {
        return BIIClient.instance.post("PsnFundBonusResult", parmas, PsnFundBonusResponse.class);
    }

    /**
     * 032 PsnFundNightBonusResult基金修改分红方式(挂单)
     *
     * @param params
     * @return
     */
    public Observable<PsnFundNightBonusResult> psnFundNightBonusResult(PsnFundNightBonusParmas params) {
        return BIIClient.instance.post("PsnFundNightBonusResult", params, PsnFundNightBonusResponse.class);
    }

    /**
     * 026基金卖出
     * @param params
     * @return
     */
    public Observable<PsnFundSellResult> psnFundSell (PsnFundSellParams params){
        return BIIClient.instance.post("PsnFundSell", params, PsnFundSellResponse.class);
    }

    /**
     * 027基金卖出
     * @param params
     * @return
     */
    public Observable<PsnFundNightSellResult> psnFundNightSell (PsnFundNightSellParams params){
        return BIIClient.instance.post("PsnFundNightSell", params, PsnFundNightSellResponse.class);
    }

    /**
     * 051基金快速赎回
     * @param params
     * @return
     */
    public Observable<PsnFundQuickSellResult> psnFundQuickSell (PsnFundQuickSellParams params){
        return BIIClient.instance.post("PsnFundQuickSell", params, PsnFundQuickSellResponse.class);
    }

    /**
     * 028 PsnFundConversionInput基金转换输入
     * @param params
     * @return
     */
    public Observable<List<PsnFundConversionInputResult>> psnFundcoversionInput(PsnFundConversionInputParams params) {
        return BIIClient.instance.post("PsnFundConversionInput",params, PsnFundConversionInputResponse.class);
    }

    /**
     * 030 PsnFundNightConversionResult基金转换结果(挂单)
     * @param params
     * @return
     */
    public Observable<PsnFundNightConversionResult> psnFundNightConversionResult(PsnFundNightConversionParams params){
        return BIIClient.instance.post("PsnFundNightConversionResult",params, PsnFundNightConversionResponse.class);
    }


    /**
     *029 PsnFundNightConversionResult基金转换结果
     * @param params
     * @return
     */
    public Observable<PsnFundConversionResult> psnFundConversionResult(PsnFundConversionParams params){
        return BIIClient.instance.post("PsnFundConversionResult",params, PsnFundConversionResponse.class);
    }

    /**
     * 037 PsnFundScheduledSell基金定期定额赎回
     * @param params
     * @return
     */
    public Observable<PsnFundScheduledSellResult> psnFundScheduledSell(PsnFundScheduledSellParams params){
        return BIIClient.instance.post("PsnFundScheduledSell",params, PsnFundScheduledSellResponse.class);
    }

    /**
     * 013 PsnFundScheduledBuy基金定投
     * @param params
     * @return
     */
    public Observable<PsnFundScheduledBuyResult> psnFundScheduledBuy(PsnFundScheduledBuyParams params){
        return BIIClient.instance.post("",params, PsnFundScheduledBuyResponse.class);
    }

    /**
     * 073 登录前基金公司列表查询
     * @param params
     * @return
     */
    public Observable<List<PsnFundCompanyQueryOutlayResult>> psnFundCompanyQueryOutlay(PsnFundCompanyQueryOutlayParams params){
//        return BIIClient.instance.post("PsnFundCompanyQueryOutlay",params, PsnFundCompanyQueryOutlayResponse.class);
        return BIIClient.instance.post(BIIClientConfig.getBPMSUrl(), "PsnFundCompanyQueryOutlay",params, PsnFundCompanyQueryOutlayResponse.class);
    }

    /**
     * 064 基金快速赎回额度查询
    * */
    public Observable<PsnQuickSellQuotaQueryResult> psnFundQuickSellQuotaQuery(PsnQuickSellQuotaQueryParams params){
        return BIIClient.instance.post("PsnFundQuickSellQuotaQuery",params, PsnQuickSellQuotaQueryResponse.class);
    }

    /**
     * 065 查询我关注的基金
     */
    public Observable<PsnFundAttentionQueryListResult> psnFundAttentionQueryList(PsnFundAttentionQueryListParams params){
        return BIIClient.instance.post("PsnFundAttentionQueryList",params, PsnFundAttentionQueryListResponse.class);
    }

    /**
     * 066 增加基金关注
     */
    public Observable<PsnFundAttentionAddResult> psnFundAttentionAdd(PsnFundAttentionAddParams params){
        return BIIClient.instance.post("PsnFundAttentionAdd",params, PsnFundAttentionAddResponse.class);
    }

    /**
     * 067 取消基金关注
     */
    public Observable<PsnFundAttentionCancelResult> psnFundAttentionCancel(PsnFundAttentionCancelParams params){
        return BIIClient.instance.post("PsnFundAttentionCancel",params, PsnFundAttentionCancelResponse.class);
    }
}
