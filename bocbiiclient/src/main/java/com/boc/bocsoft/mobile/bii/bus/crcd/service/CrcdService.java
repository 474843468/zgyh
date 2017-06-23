package com.boc.bocsoft.mobile.bii.bus.crcd.service;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcd3DQueryCertifInfo.PsnCrcd3DQueryCertifInfoParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcd3DQueryCertifInfo.PsnCrcd3DQueryCertifInfoResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcd3DQueryCertifInfo.PsnCrcd3DQueryCertifInfoResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainMessSetResult.PsnCrcdAppertainMessSetResultParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainMessSetResult.PsnCrcdAppertainMessSetResultResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainMessSetResult.PsnCrcdAppertainMessSetResultResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainTranQuery.PsnCrcdAppertainTranQueryParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainTranQuery.PsnCrcdAppertainTranQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainTranQuery.PsnCrcdAppertainTranQueryResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainTranSetConfirm.PsnCrcdAppertainTranSetConfirmParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainTranSetConfirm.PsnCrcdAppertainTranSetConfirmResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainTranSetConfirm.PsnCrcdAppertainTranSetConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainTranSetResult.PsnCrcdAppertainTranSetResultParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainTranSetResult.PsnCrcdAppertainTranSetResultResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainTranSetResult.PsnCrcdAppertainTranSetResultResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdApplyCashDiv.PsnCrcdApplyCashDivParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdApplyCashDiv.PsnCrcdApplyCashDivResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdApplyCashDiv.PsnCrcdApplyCashDivResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdApplyCashDivPre.PsnCrcdApplyCashDivPreParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdApplyCashDivPre.PsnCrcdApplyCashDivPreResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdApplyCashDivPre.PsnCrcdApplyCashDivPreResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdChargeOnRMBAccountQuery.PsnCrcdChargeOnRMBAccountQueryParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdChargeOnRMBAccountQuery.PsnCrcdChargeOnRMBAccountQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdChargeOnRMBAccountQuery.PsnCrcdChargeOnRMBAccountQueryResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCheckPaymentInfo.PsnCrcdCheckPaymentInfoParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCheckPaymentInfo.PsnCrcdCheckPaymentInfoResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCheckPaymentInfo.PsnCrcdCheckPaymentInfoResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery.PsnCrcdCurrencyQueryParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery.PsnCrcdCurrencyQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery.PsnCrcdCurrencyQueryResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayAdvanceConfirm.PsnCrcdDividedPayAdvanceConfirmParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayAdvanceConfirm.PsnCrcdDividedPayAdvanceConfirmResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayAdvanceConfirm.PsnCrcdDividedPayAdvanceConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayAdvanceResult.PsnCrcdDividedPayAdvanceResultParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayAdvanceResult.PsnCrcdDividedPayAdvanceResultResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayAdvanceResult.PsnCrcdDividedPayAdvanceResultResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayBillSetConfirm.PsnCrcdDividedPayBillSetConfirmParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayBillSetConfirm.PsnCrcdDividedPayBillSetConfirmResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayBillSetConfirm.PsnCrcdDividedPayBillSetConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayBillSetInput.PsnCrcdDividedPayBillSetInputParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayBillSetInput.PsnCrcdDividedPayBillSetInputResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayBillSetInput.PsnCrcdDividedPayBillSetInputResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayBillSetResult.PsnCrcdDividedPayBillSetResultParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayBillSetResult.PsnCrcdDividedPayBillSetResultResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayBillSetResult.PsnCrcdDividedPayBillSetResultResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayConsumeConfirm.PsnCrcdDividedPayConsumeConfirmParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayConsumeConfirm.PsnCrcdDividedPayConsumeConfirmResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayConsumeConfirm.PsnCrcdDividedPayConsumeConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayConsumeQry.PsnCrcdDividedPayConsumeQryParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayConsumeQry.PsnCrcdDividedPayConsumeQryResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayConsumeQry.PsnCrcdDividedPayConsumeQryResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayConsumeResult.PsnCrcdDividedPayConsumeResultParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayConsumeResult.PsnCrcdDividedPayConsumeResultResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayConsumeResult.PsnCrcdDividedPayConsumeResultResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayHisQry.PsnCrcdDividedPayHisQryParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayHisQry.PsnCrcdDividedPayHisQryResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayHisQry.PsnCrcdDividedPayHisQryResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdForeignPayOff.PsnCrcdForeignPayOffParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdForeignPayOff.PsnCrcdForeignPayOffResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdForeignPayOff.PsnCrcdForeignPayOffResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdForeignPayOffFee.PsnCrcdForeignPayOffFeeParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdForeignPayOffFee.PsnCrcdForeignPayOffFeeResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdForeignPayOffFee.PsnCrcdForeignPayOffFeeResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdForeignPayQuery.PsnCrcdForeignPayQueryParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdForeignPayQuery.PsnCrcdForeignPayQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdForeignPayQuery.PsnCrcdForeignPayQueryResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdGetCashDivCommissionCharge.PsnCrcdGetCashDivCommissionChargeParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdGetCashDivCommissionCharge.PsnCrcdGetCashDivCommissionChargeResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdGetCashDivCommissionCharge.PsnCrcdGetCashDivCommissionChargeResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdPaymentWaySetup.PsnCrcdPaymentWaySetupParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdPaymentWaySetup.PsnCrcdPaymentWaySetupRespons;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdPaymentWaySetup.PsnCrcdPaymentWaySetupResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAppertainAndMess.PsnCrcdQueryAppertainAndMessParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAppertainAndMess.PsnCrcdQueryAppertainAndMessResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAppertainAndMess.PsnCrcdQueryAppertainAndMessResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryBilledTrans.PsnCrcdQueryBilledTransParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryBilledTrans.PsnCrcdQueryBilledTransResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryBilledTrans.PsnCrcdQueryBilledTransResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryBilledTransDetail.PsnCrcdQueryBilledTransDetailParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryBilledTransDetail.PsnCrcdQueryBilledTransDetailResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryBilledTransDetail.PsnCrcdQueryBilledTransDetailResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryCashDivBalance.PsnCrcdQueryCashDivBalanceParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryCashDivBalance.PsnCrcdQueryCashDivBalanceResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryCashDivBalance.PsnCrcdQueryCashDivBalanceResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryCrcdPaymentWay.PsnCrcdQueryCrcdPaymentWayParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryCrcdPaymentWay.PsnCrcdQueryCrcdPaymentWayResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryCrcdPaymentWay.PsnCrcdQueryCrcdPaymentWayResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryForeignPayOff.PsnCrcdQueryForeignPayOffParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryForeignPayOff.PsnCrcdQueryForeignPayOffResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryForeignPayOff.PsnCrcdQueryForeignPayOffResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryFutureBill.PsnCrcdQueryFutureBillParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryFutureBill.PsnCrcdQueryFutureBillResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryFutureBill.PsnCrcdQueryFutureBillResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryFutureBillTotalIncome.PsnCrcdQueryFutureBillTotalIncomeParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryFutureBillTotalIncome.PsnCrcdQueryFutureBillTotalIncomeResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryFutureBillTotalIncome.PsnCrcdQueryFutureBillTotalIncomeResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryGeneralInfo.PsnCrcdQueryGeneralInfoParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryGeneralInfo.PsnCrcdQueryGeneralInfoResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryGeneralInfo.PsnCrcdQueryGeneralInfoResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryMasterAndSupplInfo.PsnCrcdQueryMasterAndSupplInfoParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryMasterAndSupplInfo.PsnCrcdQueryMasterAndSupplInfoResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryMasterAndSupplInfo.PsnCrcdQueryMasterAndSupplInfoResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQuerySettingsInfo.PsnCrcdQuerySettingsInfoParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQuerySettingsInfo.PsnCrcdQuerySettingsInfoResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQuerySettingsInfo.PsnCrcdQuerySettingsInfoResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryUnauthorizedTrans.PsnCrcdQueryUnauthorizedTransParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryUnauthorizedTrans.PsnCrcdQueryUnauthorizedTransResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryUnauthorizedTrans.PsnCrcdQueryUnauthorizedTransResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryUnauthorizedTransTotal.PsnCrcdQueryUnauthorizedTransTotalParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryUnauthorizedTransTotal.PsnCrcdQueryUnauthorizedTransTotalResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryUnauthorizedTransTotal.PsnCrcdQueryUnauthorizedTransTotalResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdTransferPayOff.PsnCrcdTransferPayOffParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdTransferPayOff.PsnCrcdTransferPayOffResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdTransferPayOff.PsnCrcdTransferPayOffResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnQueryCrcdBillIsExist.PsnQueryCrcdBillIsExistParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnQueryCrcdBillIsExist.PsnQueryCrcdBillIsExistResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnQueryCrcdBillIsExist.PsnQueryCrcdBillIsExistResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnQueryCrcdPoint.PsnQueryCrcdPointParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnQueryCrcdPoint.PsnQueryCrcdPointResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnQueryCrcdPoint.PsnQueryCrcdPointResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnQueryCrcdRTBill.PsnQueryCrcdRTBillParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnQueryCrcdRTBill.PsnQueryCrcdRTBillResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnQueryCrcdRTBill.PsnQueryCrcdRTBillResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnReSetEmailPaperCheck.PsnReSetEmailPaperCheckParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnReSetEmailPaperCheck.PsnReSetEmailPaperCheckResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnReSetEmailPaperCheck.PsnReSetEmailPaperCheckResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnReSetSmsPaperCheck.PsnReSetSmsPaperCheckParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnReSetSmsPaperCheck.PsnReSetSmsPaperCheckResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnReSetSmsPaperCheck.PsnReSetSmsPaperCheckResult;
import com.boc.bocsoft.mobile.bii.common.client.BIIClient;

import java.util.List;

import rx.Observable;

/**
 * Created by xdy4486 on 2016/6/25.
 */
public class CrcdService {
    /**
     * 查询信用卡币种
     * added by xdy 20160603
     *
     * @param params 参数
     */
    public Observable<PsnCrcdCurrencyQueryResult> psnCrcdCurrencyQuery(PsnCrcdCurrencyQueryParams params) {
        return BIIClient.instance.post("PsnCrcdCurrencyQuery", params, PsnCrcdCurrencyQueryResponse.class);
    }

    /**
     * 查询信用卡详情
     * added by xdy 20160603
     *
     * @param params 参数
     */
    public Observable<PsnCrcdQueryAccountDetailResult> psnCrcdQueryAccountDetailResult(PsnCrcdQueryAccountDetailParams params) {
        return BIIClient.instance.post("PsnCrcdQueryAccountDetail", params, PsnCrcdQueryAccountDetailResponse.class);
    }

    /***
     * 信用卡综合信息查询
     * added by wangf on 2016-11-22 16:04:39
     *
     * @param params
     * @return
     */
    public Observable<PsnCrcdQueryGeneralInfoResult> psnCrcdQueryGeneralInfo(PsnCrcdQueryGeneralInfoParams params) {
        return BIIClient.instance.post("PsnCrcdQueryGeneralInfo", params, PsnCrcdQueryGeneralInfoResponse.class);
    }

    /**
     * 信用卡积分查询
     * added by wangf on 2016-11-22 16:07:03
     *
     * @param params
     * @return
     */
    public Observable<PsnQueryCrcdPointResult> psnQueryCrcdPoint(PsnQueryCrcdPointParams params) {
        return BIIClient.instance.post("PsnQueryCrcdPoint", params, PsnQueryCrcdPointResponse.class);
    }

    /**
     * 查询信用卡已出账单
     * added by wangf on 2016-11-22 16:08:05
     *
     * @param params
     * @return
     */
    public Observable<PsnCrcdQueryBilledTransResult> psnCrcdQueryBilledTrans(PsnCrcdQueryBilledTransParams params) {
        return BIIClient.instance.post("PsnCrcdQueryBilledTrans", params, PsnCrcdQueryBilledTransResponse.class);
    }

    /**
     * 办理账单分期输入
     * added by wangf on 2016-11-22 16:10:46
     *
     * @param params
     * @return
     */
    public Observable<PsnCrcdDividedPayBillSetInputResult> psnCrcdDividedPayBillSetInput(PsnCrcdDividedPayBillSetInputParams params) {
        return BIIClient.instance.post("PsnCrcdDividedPayBillSetInput", params, PsnCrcdDividedPayBillSetInputResponse.class);
    }

    /**
     * 信用卡当月是否已出账单查询
     * added by wangf on 2016-11-22 16:11:38
     *
     * @param params
     * @return
     */
    public Observable<PsnQueryCrcdBillIsExistResult> psnQueryCrcdBillIsExist(PsnQueryCrcdBillIsExistParams params) {
        return BIIClient.instance.post("PsnQueryCrcdBillIsExist", params, PsnQueryCrcdBillIsExistResponse.class);
    }

    /**
     * 还款方式查询
     * added by wangf on 2016-11-22 16:12:38
     *
     * @param params
     * @return
     */
    public Observable<PsnCrcdQueryCrcdPaymentWayResult> psnCrcdQueryCrcdPaymentWay(PsnCrcdQueryCrcdPaymentWayParams params) {
        return BIIClient.instance.post("PsnCrcdQueryCrcdPaymentWay", params, PsnCrcdQueryCrcdPaymentWayResponse.class);
    }

    /**
     * 信用卡设置类信息查询
     * added by wangf on 2016-11-22 16:14:12
     *
     * @param params
     * @return
     */
    public Observable<PsnCrcdQuerySettingsInfoResult> psnCrcdQuerySettingsInfo(PsnCrcdQuerySettingsInfoParams params) {
        return BIIClient.instance.post("PsnCrcdQuerySettingsInfo", params, PsnCrcdQuerySettingsInfoResponse.class);
    }

    /**
     * 3D安全认证信息查询
     * added by wangf on 2016-11-22 16:15:31
     *
     * @param params
     * @return
     */
    public Observable<PsnCrcd3DQueryCertifInfoResult> psnCrcd3DQueryCertifInfo(PsnCrcd3DQueryCertifInfoParams params) {
        return BIIClient.instance.post("PsnCrcd3DQueryCertifInfo", params, PsnCrcd3DQueryCertifInfoResponse.class);
    }

    /**
     * 全球交易人民币记账功能查询
     * added by wangf on 2016-11-22 16:16:21
     *
     * @param params
     * @return
     */
    public Observable<PsnCrcdChargeOnRMBAccountQueryResult> psnCrcdChargeOnRMBAccountQuery(PsnCrcdChargeOnRMBAccountQueryParams params) {
        return BIIClient.instance.post("PsnCrcdChargeOnRMBAccountQuery", params, PsnCrcdChargeOnRMBAccountQueryResponse.class);
    }

    /**
     * 查询信用卡实时账
     * @param params
     * @return
     */
    public Observable<PsnQueryCrcdRTBillResult> psnQueryCrcdRTBill(PsnQueryCrcdRTBillParams params) {
        return BIIClient.instance.post("PsnQueryCrcdRTBill", params, PsnQueryCrcdRTBillResponse.class);
    }

    /**
     * 信用卡还款信息校验
     */
    public Observable<PsnCrcdCheckPaymentInfoResult> psnCrcdCheckPaymentInfo(PsnCrcdCheckPaymentInfoParams params){
        return BIIClient.instance.post("PsnCrcdCheckPaymentInfo",params, PsnCrcdCheckPaymentInfoResponse.class);
    }

    /**
     * 关联信用卡还款确认
     */
    public Observable<PsnCrcdTransferPayOffResult> psnCrcdTransferPayOff(PsnCrcdTransferPayOffParams params){
        return BIIClient.instance.post("PsnCrcdTransferPayOffResult",params, PsnCrcdTransferPayOffResponse.class);
    }


    /**
     * 购汇还款信用卡外币账户列表查询
     */
    public Observable<List<PsnCrcdForeignPayQueryResult>> psnCrcdForeignPayQuery(PsnCrcdForeignPayQueryParams params){
        return BIIClient.instance.post("PsnCrcdForeignPayQuery",params, PsnCrcdForeignPayQueryResponse.class);
    }


    /**
     * 信用卡查询购汇还款信息
     */
    public Observable<PsnCrcdQueryForeignPayOffResult> psnCrcdQueryForeignPayOff(PsnCrcdQueryForeignPayOffParams params){
        return BIIClient.instance.post("PsnCrcdQueryForeignPayOff",params, PsnCrcdQueryForeignPayOffResponse.class);
    }
    /**
     * 购汇还款费用试算
     */
    public Observable<PsnCrcdForeignPayOffFeeResult> psnCrcdForeignPayOffFee(PsnCrcdForeignPayOffFeeParams params){
        return BIIClient.instance.post("PsnCrcdForeignPayOffFee",params, PsnCrcdForeignPayOffFeeResponse.class);
    }


    /**
     * 信用卡购汇还款提交
     */
    public Observable<PsnCrcdForeignPayOffResult> psnCrcdForeignPayOff(PsnCrcdForeignPayOffParams params){
        return BIIClient.instance.post("PsnCrcdForeignPayOffResult",params, PsnCrcdForeignPayOffResponse.class);
    }

    /**
     * 信用卡账单分期预交易
     *added by lq7090 2016/11/17
     * @param params 参数
     */
    public  Observable<PsnCrcdDividedPayBillSetConfirmResult> psnCrcdDividedPayBillSetConfirm(PsnCrcdDividedPayBillSetConfirmParams params) {
        return BIIClient.instance.post("PsnCrcdDividedPayBillSetConfirm", params, PsnCrcdDividedPayBillSetConfirmResponse.class);
    }



    /**
     * 信用卡账单分期交易
     *added by lq7090 2016/11/17
     * @param params 参数
     */
    public  Observable<PsnCrcdDividedPayBillSetResultResult> psnCrcdDividedPayBillSetResult(PsnCrcdDividedPayBillSetResultParams params) {
        return BIIClient.instance.post("PsnCrcdDividedPayBillSetResult", params, PsnCrcdDividedPayBillSetResultResponse.class);
    }


    /**
     * 信用卡消费账单查询
     *added by lq7090 2016/12/27
     * @param params 参数
     */
    public  Observable<PsnCrcdDividedPayConsumeQryResult> psnCrcdDividedPayConsumeQry(PsnCrcdDividedPayConsumeQryParams params) {
        return BIIClient.instance.post("PsnCrcdDividedPayConsumeQry", params, PsnCrcdDividedPayConsumeQryResponse.class);
    }


    /**
     * 信用卡消费分期预交易
     *added by lq7090 2016/11/17
     * @param params 参数
     */
    public  Observable<PsnCrcdDividedPayConsumeConfirmResult> psnCrcdDividedPayConsumeConfirm(PsnCrcdDividedPayConsumeConfirmParams params) {
        return BIIClient.instance.post("PsnCrcdDividedPayConsumeConfirm", params, PsnCrcdDividedPayConsumeConfirmResponse.class);
    }

    /**
     * 信用卡消费分期交易
     *added by lq7090 2016/11/17
     * @param params 参数
     */
    public  Observable<PsnCrcdDividedPayConsumeResultResult> psnCrcdDividedPayConsumeResult(PsnCrcdDividedPayConsumeResultParams params) {
        return BIIClient.instance.post("PsnCrcdDividedPayConsumeResult", params, PsnCrcdDividedPayConsumeResultResponse.class);
    }

    /**
     * 还款方式设定
     * added by liukai 20161122
     * @param params 参数
     */
    public Observable<PsnCrcdPaymentWaySetupResult> psnCrcdPaymentWaySetupResult(PsnCrcdPaymentWaySetupParams params) {
        return BIIClient.instance.post("PsnCrcdPaymentWaySetup", params, PsnCrcdPaymentWaySetupRespons.class);
    }

    /**
     * 分期历史查询
     * added by yangle 20161124
     * @param params 参数
     */
    public Observable<PsnCrcdDividedPayHisQryResult> psnCrcdDividedPayHisQry(PsnCrcdDividedPayHisQryParams params) {
        return BIIClient.instance.post("PsnCrcdDividedPayHisQry", params, PsnCrcdDividedPayHisQryResponse.class);
    }

    /**
     * 提前结清入账确认
     * added by yangle 20161124
     * @param params 参数
     */
    public Observable<PsnCrcdDividedPayAdvanceConfirmResult> psnCrcdDividedPayAdvanceConfirm(PsnCrcdDividedPayAdvanceConfirmParams params) {
        return BIIClient.instance.post("PsnCrcdDividedPayAdvanceConfirm", params, PsnCrcdDividedPayAdvanceConfirmResponse.class);
    }

    /**
     * 提前结清入账结果
     * added by yangle 20161124
     * @param params 参数
     */
    public Observable<PsnCrcdDividedPayAdvanceResultResult> psnCrcdDividedPayAdvanceResult(PsnCrcdDividedPayAdvanceResultParams params) {
        return BIIClient.instance.post("PsnCrcdDividedPayAdvanceResult", params, PsnCrcdDividedPayAdvanceResultResponse.class);
    }

    /**
     * 现金分期申请
     * @param params
     * @return
     */
    public  Observable<PsnCrcdApplyCashDivResult> psnCrcdApplyCashDiv(PsnCrcdApplyCashDivParams params) {
        return BIIClient.instance.post("PsnCrcdApplyCashDiv", params, PsnCrcdApplyCashDivResponse.class);
    }

    /**
     * 现金分期申请预交易
     * @param params
     * @return
     */
    public  Observable<PsnCrcdApplyCashDivPreResult> psnCrcdApplyCashDivPre(PsnCrcdApplyCashDivPreParams params) {
        return BIIClient.instance.post("PsnCrcdApplyCashDivPre", params, PsnCrcdApplyCashDivPreResponse.class);
    }

    /**
     * 现金分期费用试算
     * @param params
     * @return
     */
    public  Observable<PsnCrcdGetCashDivCommissionChargeResult> psnCrcdGetCashDivCommissionCharge(PsnCrcdGetCashDivCommissionChargeParams params) {
        return BIIClient.instance.post("PsnCrcdGetCashDivCommissionCharge", params, PsnCrcdGetCashDivCommissionChargeResponse.class);
    }

    /**
     * 现金分期可用额度查询
     * @param params
     * @return
     */
    public  Observable<PsnCrcdQueryCashDivBalanceResult> psnCrcdQueryCashDivBalance(PsnCrcdQueryCashDivBalanceParams params) {
        return BIIClient.instance.post("PsnCrcdQueryCashDivBalance", params, PsnCrcdQueryCashDivBalanceResponse.class);
    }

    /**
     * 信用卡主附卡信息查询
     * added by liukai 20161202
     * @param params 参数
     */
    public Observable<List<PsnCrcdQueryMasterAndSupplInfoResult>> psnCrcdQueryMasterAndSupplInfo(PsnCrcdQueryMasterAndSupplInfoParams params) {
        return BIIClient.instance.post("PsnCrcdQueryMasterAndSupplInfo", params, PsnCrcdQueryMasterAndSupplInfoResponse.class);
    }

    /**
     * 信用卡查询附属卡交易流量与短信发送对象
     * added by liukai 20161202
     * @param params 参数
     */
    public Observable<PsnCrcdQueryAppertainAndMessResult> psnCrcdQueryAppertainAndMess(PsnCrcdQueryAppertainAndMessParams params) {
        return BIIClient.instance.post("PsnCrcdQueryAppertainAndMess", params, PsnCrcdQueryAppertainAndMessResponse.class);
    }

    /**
     * 附属卡短信设置处理
     * added by liukai 20161202
     * @param params 参数
     */
    public Observable<PsnCrcdAppertainMessSetResultResult> psnCrcdAppertainMessSetResult(PsnCrcdAppertainMessSetResultParams params) {
        return BIIClient.instance.post("PsnCrcdAppertainMessSetResult", params, PsnCrcdAppertainMessSetResultResponse.class);
    }

    /**
     * 附属卡交易明细查询
     * added by liukai 20161202
     * @param params 参数
     */
    public Observable<PsnCrcdAppertainTranQueryResult> psnCrcdAppertainTranQuery(PsnCrcdAppertainTranQueryParams params) {
        return BIIClient.instance.post("PsnCrcdAppertainTranQuery", params, PsnCrcdAppertainTranQueryResponse.class);
    }

    /**
     * 附属卡流量确认
     * added by liukai 20161202
     * @param params 参数
     */
    public Observable<PsnCrcdAppertainTranSetConfirmResult> psnCrcdAppertainTranSetConfirm(PsnCrcdAppertainTranSetConfirmParams params) {
        return BIIClient.instance.post("PsnCrcdAppertainTranSetConfirm", params, PsnCrcdAppertainTranSetConfirmResponse.class);
    }

    /**
     * 附属卡流量处理
     * added by liukai 20161202
     * @param params 参数
     */
    public Observable<PsnCrcdAppertainTranSetResultResult> psnCrcdAppertainTranSetResult(PsnCrcdAppertainTranSetResultParams params) {
        return BIIClient.instance.post("PsnCrcdAppertainTranSetResult", params, PsnCrcdAppertainTranSetResultResponse.class);
    }

    /**
     * 4.4 004查询信用卡未出账单PsnCrcdQueryFutureBill
     *
     * @param params
     * @return
     */
    public Observable<PsnCrcdQueryFutureBillResult> psnCrcdQueryFutureBill(PsnCrcdQueryFutureBillParams params) {
        return BIIClient.instance.post("PsnCrcdQueryFutureBill", params, PsnCrcdQueryFutureBillResponse.class);
    }

    /**
     * 4.112 112查询信用卡未入账待授权交易PsnCrcdQueryUnauthorizedTrans
     *
     * @param params
     * @return
     */
    public Observable<PsnCrcdQueryUnauthorizedTransResult> psnCrcdQueryUnauthorizedTrans(PsnCrcdQueryUnauthorizedTransParams params) {
        return BIIClient.instance.post("PsnCrcdQueryUnauthorizedTrans", params, PsnCrcdQueryUnauthorizedTransResponse.class);
    }

    /**
     * 4.5 005查询信用卡未出账单合计PsnCrcdQueryFutureBillTotalIncome
     *
     * @param params
     * @return
     */
    public Observable<List<PsnCrcdQueryFutureBillTotalIncomeResult>> psnCrcdQueryFutureBillTotalIncome(PsnCrcdQueryFutureBillTotalIncomeParams params) {
        return BIIClient.instance.post("PsnCrcdQueryFutureBillTotalIncome", params, PsnCrcdQueryFutureBillTotalIncomeResponse.class);
    }

    /**
     * 4.113 113查询信用卡未入账待授权交易合计PsnCrcdQueryUnauthorizedTransTotal
     *
     * @param params
     * @return
     */
    public Observable<List<PsnCrcdQueryUnauthorizedTransTotalResult>> psnCrcdQueryUnauthorizedTransTotal(PsnCrcdQueryUnauthorizedTransTotalParams params) {
        return BIIClient.instance.post("PsnCrcdQueryUnauthorizedTransTotal", params, PsnCrcdQueryUnauthorizedTransTotalResponse.class);
    }

    /**
     * 4.8 008查询信用卡已出账单交易明细PsnCrcdQueryBilledTransDetail
     *
     * @param params
     * @return
     */
    public Observable<PsnCrcdQueryBilledTransDetailResult> psnCrcdQueryBilledTransDetail(PsnCrcdQueryBilledTransDetailParams params) {
        return BIIClient.instance.post("PsnCrcdQueryBilledTransDetail", params, PsnCrcdQueryBilledTransDetailResponse.class);
    }


    /**
     * 4.79 079 重置邮件电子对账单PsnReSetEmailPaperCheck
     *
     * @param params
     * @return
     */
    public Observable<PsnReSetEmailPaperCheckResult> psnReSetEmailPaperCheck(PsnReSetEmailPaperCheckParams params) {
        return BIIClient.instance.post("PsnReSetEmailPaperCheck", params, PsnReSetEmailPaperCheckResponse.class);
    }
  /**
     * 4.78 078重置手机推入式对账单
     *
     * @param params
     * @return
     */
    public Observable<PsnReSetSmsPaperCheckResult> psnReSetSmsPaperCheck(PsnReSetSmsPaperCheckParams params) {
        return BIIClient.instance.post("PsnReSetSmsPaperCheck", params, PsnReSetSmsPaperCheckResponse.class);
    }



}
