package com.boc.bocsoft.mobile.bii.bus.account.service;

import com.boc.bocsoft.mobile.bii.bus.account.model.FactorAndCaResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountLossReportConfirm.PsnAccountLossReportConfirmParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountLossReportConfirm.PsnAccountLossReportConfirmResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountLossReportConfirm.PsnAccountLossReportConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountLossReportResult.PsnAccountLossReportResultParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountLossReportResult.PsnAccountLossReportResultResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountLossReportResult.PsnAccountLossReportResultResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountModifyAccountAlias.PsnAccountModifyAccountAliasParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountModifyAccountAlias.PsnAccountModifyAccountAliasResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQryRecentTransDetail.PsnAccountQryRecentTransDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQryRecentTransDetail.PsnAccountQryRecentTransDetailResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQryRecentTransDetail.PsnAccountQryRecentTransDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryTransferDetail.PsnAccountQueryTransferDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryTransferDetail.PsnAccountQueryTransferDetailResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryTransferDetail.PsnAccountQueryTransferDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnApplyTermDepositeConfirm.PsnApplyTermDepositeConfirmParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnApplyTermDepositeConfirm.PsnApplyTermDepositeConfirmResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnApplyTermDepositeConfirm.PsnApplyTermDepositeConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnApplyTermDepositeResult.PsnApplyTermDepositeParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnApplyTermDepositeResult.PsnApplyTermDepositeResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnApplyTermDepositeResult.PsnApplyTermDepositeResultResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCommonQueryOprLoginInfo.PsnQueryOprLoginInfoParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCommonQueryOprLoginInfo.PsnQueryOprLoginInfoResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCommonQueryOprLoginInfo.PsnQueryOprLoginInfoResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCountryCodeQueryApplyPre.PsnCountryCodeQueryApplyPreParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCountryCodeQueryApplyPre.PsnCountryCodeQueryApplyPreResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCountryCodeQueryApplyPre.PsnCountryCodeQueryApplyPreResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdQueryCardholderAddress.PsnCrcdQueryCardholderAddressParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdQueryCardholderAddress.PsnCrcdQueryCardholderAddressResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdQueryCardholderAddress.PsnCrcdQueryCardholderAddressResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdReportLossConfirm.PsnCrcdReportLossConfirmParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdReportLossConfirm.PsnCrcdReportLossConfirmResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdReportLossConfirm.PsnCrcdReportLossConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdReportLossFee.PsnCrcdReportLossFeeParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdReportLossFee.PsnCrcdReportLossFeeResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdReportLossFee.PsnCrcdReportLossFeeResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdReportLossResult.PsnCrcdReportLossResultParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdReportLossResult.PsnCrcdReportLossResultResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdReportLossResult.PsnCrcdReportLossResultResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdReportLossResultReinforce.PsnCrcdReportLossResultReinforceParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdReportLossResultReinforce.PsnCrcdReportLossResultReinforceResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdReportLossResultReinforce.PsnCrcdReportLossResultReinforceResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnDebitCardQryQuota.PsnDebitCardQryQuotaParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnDebitCardQryQuota.PsnDebitCardQryQuotaResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnDebitCardQryQuota.PsnDebitCardQryQuotaResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnDebitCardSetQuota.PsnDebitCardSetQuotaParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnDebitCardSetQuota.PsnDebitCardSetQuotaResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnDebitCardSetQuotaPre.PsnDebitCardSetQuotaPreParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnDebitcardLossReportConfirm.PsnDebitcardLossReportConfirmParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnDebitcardLossReportConfirm.PsnDebitcardLossReportConfirmResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnDebitcardLossReportConfirm.PsnDebitcardLossReportConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnDebitcardLossReportResult.PsnDebitcardLossReportResultParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnDebitcardLossReportResult.PsnDebitcardLossReportResultResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnDebitcardLossReportResult.PsnDebitcardLossReportResultResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICAccountDetail.PsnFinanceICAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICAccountDetail.PsnFinanceICAccountDetailResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICAccountDetail.PsnFinanceICAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnInquiryRangeQuery.PsnInquiryRangeQueryParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnInquiryRangeQuery.PsnInquiryRangeQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnInquiryRangeQuery.PsnInquiryRangeQueryResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnMedicalInsurAcctDetailQuery.PsnMedicalInsurAcctDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnMedicalInsurAcctDetailQuery.PsnMedicalInsurAcctDetailQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnMedicalInsurAcctDetailQuery.PsnMedicalInsurAcctDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnMedicalInsurAcctTransferDetailQuery.PsnMedicalInsurAcctTransferDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnMedicalInsurAcctTransferDetailQuery.PsnMedicalInsurAcctTransferDetailQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnMedicalInsurAcctTransferDetailQuery.PsnMedicalInsurAcctTransferDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnQueryInvtBindingInfo.PsnQueryInvtBindingInfoParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnQueryInvtBindingInfo.PsnQueryInvtBindingInfoResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnQueryInvtBindingInfo.PsnQueryInvtBindingInfoResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSVRCancelAccRelation.PsnSVRCancelAccRelationParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSVRCancelAccRelation.PsnSVRCancelAccRelationResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSendSMSCodeToMobileApplyPre.PsnSendSMSCodeToMobileApplyPreParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSendSMSCodeToMobileApplyPre.PsnSendSMSCodeToMobileApplyPreResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSendSMSCodeToMobileApplyPre.PsnSendSMSCodeToMobileApplyPreResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmAccountChange.PsnSsmAccountChangeParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmAccountChange.PsnSsmAccountChangeResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmAccountChange.PsnSsmAccountChangeResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmAccountChangePre.PsnSsmAccountChangePreParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmAccountChangePre.PsnSsmAccountChangePreResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmAccountChangePre.PsnSsmAccountChangePreResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmDelete.PsnSsmDeleteParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmDelete.PsnSsmDeleteResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmDelete.PsnSsmDeleteResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmDiscountmodel.PsnSsmDiscountmodelParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmDiscountmodel.PsnSsmDiscountmodelResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmDiscountmodel.PsnSsmDiscountmodelResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmFeeQuery.PsnSsmFeeQueryParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmFeeQuery.PsnSsmFeeQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmFeeQuery.PsnSsmFeeQueryResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmMade.PsnSsmMadeParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmMade.PsnSsmMadeResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmMadePre.PsnSsmMadePreParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmMadePre.PsnSsmMadePreResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmMadePre.PsnSsmMadePreResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmMessageAdd.PsnSsmMessageAddParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmMessageAdd.PsnSsmMessageAddResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmMessageAdd.PsnSsmMessageAddResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmMessageDelete.PsnSsmMessageDeleteParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmMessageDelete.PsnSsmMessageDeleteResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmMessageDelete.PsnSsmMessageDeleteResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmQuery.PsnSsmQueryParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmQuery.PsnSsmQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmQuery.PsnSsmQueryResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmSend.PsnSsmSendParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmSend.PsnSsmSendResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmSend.PsnSsmSendResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmSign.PsnSsmSignParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmSign.PsnSsmSignResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmSign.PsnSsmSignResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmSignPre.PsnSsmSignPreParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmSignPre.PsnSsmSignPreResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmSignPre.PsnSsmSignPreResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnTransQuotaQuery.PsnTransQuotaQueryParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnTransQuotaQuery.PsnTransQuotaQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnTransQuotaQuery.PsnTransQuotaQueryResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PublicSecurityResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.psnSsmMessageChange.PsnSsmMessageChangeParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.psnSsmMessageChange.PsnSsmMessageChangeResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.psnSsmMessageChange.PsnSsmMessageChangeResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.psnSsmMessageChangePre.PsnSsmMessageChangePreParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.psnSsmMessageChangePre.PsnSsmMessageChangePreResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.psnSsmMessageChangePre.PsnSsmMessageChangePreResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery.PsnCrcdCurrencyQueryParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery.PsnCrcdCurrencyQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery.PsnCrcdCurrencyQueryResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailResponse;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnOFAAccountStateQuery.PsnOFAAccountStateQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnOFAAccountStateQuery.PsnOFAAccountStateQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnOFAAccountStateQuery.PsnOFAAccountStateQueryResult;
import com.boc.bocsoft.mobile.bii.common.client.BIIClient;

import rx.Observable;

/**
 * 银行账户管理
 * Created by lxw4566 on 2016/6/28.
 */
public class AccountService {

    /**
     * 申请账户前创建回话
     *
     * @param params
     * @return
     */
//    public Observable<String> PSNCreatConversation(PSNCreatConversationParams params) {
//
//        return BIIClient.instance.post("PSNCreatConversation", params, String.class);
//    }


    /**
     * 查询个人客户国籍信息
     *
     * @param params
     * @return
     */
    public Observable<PsnCountryCodeQueryApplyPreResult> psnCountryCodeQuery(PsnCountryCodeQueryApplyPreParams params) {

        return BIIClient.instance.post("PsnCountryCodeQuery", params, PsnCountryCodeQueryApplyPreResponse.class);
    }


    /**
     * 申请定期活期账户预交易
     *
     * @param params
     * @return
     */
    public Observable<PsnApplyTermDepositeConfirmResult> psnApplyTermDepositeConfirm(PsnApplyTermDepositeConfirmParams params) {

        return BIIClient.instance.post("PsnApplyTermDepositeConfirm", params, PsnApplyTermDepositeConfirmResponse.class);
    }


    /**
     * 获取并发送手机交易码
     *
     * @param params
     * @return
     */
    public Observable<PsnSendSMSCodeToMobileApplyPreResult> PsnSendSMSCodeToMobile(PsnSendSMSCodeToMobileApplyPreParams params) {

        return BIIClient.instance.post("PsnSendSMSCodeToMobile", params, PsnSendSMSCodeToMobileApplyPreResponse.class);
    }


    /**
     * 申请定期活期账户结果
     *
     * @param params
     * @return
     */
    public Observable<PsnApplyTermDepositeResult> psnApplyTermDepositeResult(PsnApplyTermDepositeParams params) {

        return BIIClient.instance.post("PsnApplyTermDepositeResult", params, PsnApplyTermDepositeResultResponse.class);
    }

    /**
     * 获取账号通知信息
     *
     * @param params
     * @return
     */
    public Observable<PsnSsmQueryResult> PsnSsmQuery(PsnSsmQueryParam params) {

        return BIIClient.instance.post("PsnSsmQuery", params, PsnSsmQueryResponse.class);
    }

    /**
     * 获取手机验证码
     *
     * @param params
     * @return
     */
    public Observable<PsnSsmSendResult> PsnSsmSend(PsnSsmSendParam params) {

        return BIIClient.instance.post("PsnSsmSend", params, PsnSsmSendResponse.class);
    }

    /**
     * 提交短信通知
     *
     * @param params
     * @return
     */
    public Observable<String> PsnSsmMade(PsnSsmMadeParam params) {

        return BIIClient.instance.post("PsnSsmMade", params, PsnSsmMadeResponse.class);
    }

    /**
     * 活期一本通挂失预交易
     *
     * @param params
     * @return
     */
    public Observable<PsnAccountLossReportConfirmResult> psnAccountLossReportConfirm(PsnAccountLossReportConfirmParams params) {
        return BIIClient.instance.post("PsnAccountLossReportConfirm", params, PsnAccountLossReportConfirmResponse.class);
    }

    /**
     * 活期一本通提交交易
     *
     * @param params
     * @return
     */
    public Observable<PsnAccountLossReportResultResult> psnAccountLossReportResult(PsnAccountLossReportResultParams params) {
        return BIIClient.instance.post("PsnAccountLossReportResult", params, PsnAccountLossReportResultResponse.class);
    }

    /**
     * 借记卡挂失预交易
     *
     * @param params
     * @return
     */
    public Observable<PsnDebitcardLossReportConfirmResult> psnDebitcardLossReportConfirm(PsnDebitcardLossReportConfirmParams params) {
        return BIIClient.instance.post("PsnDebitcardLossReportConfirm", params, PsnDebitcardLossReportConfirmResponse.class);
    }

    /**
     * 借记卡提交交易
     *
     * @param params
     * @return
     */
    public Observable<PsnDebitcardLossReportResultResult> psnDebitcardLossReportResult(PsnDebitcardLossReportResultParams params) {
        return BIIClient.instance.post("PsnDebitcardLossReportResult", params, PsnDebitcardLossReportResultResponse.class);
    }

    /**
     * 信用卡挂失及补卡手续费查询
     *
     * @param params
     * @return
     */
    public Observable<PsnCrcdReportLossFeeResult> psnCrcdReportLossFee(PsnCrcdReportLossFeeParams params) {
        return BIIClient.instance.post("PsnCrcdReportLossFee", params, PsnCrcdReportLossFeeResponse.class);
    }

    /**
     * 信用卡补卡寄送地址查询
     *
     * @param params
     * @return
     */
    public Observable<PsnCrcdQueryCardholderAddressResult> psnCrcdQueryCardholderAddress(PsnCrcdQueryCardholderAddressParams params) {
        return BIIClient.instance.post("PsnCrcdQueryCardholderAddress", params, PsnCrcdQueryCardholderAddressResponse.class);
    }

    /**
     * 信用卡挂失及补卡确认
     *
     * @param params
     * @return
     */
    public Observable<PsnCrcdReportLossConfirmResult> psnCrcdReportLossConfirm(PsnCrcdReportLossConfirmParams params) {
        return BIIClient.instance.post("PsnCrcdReportLossConfirm", params, PsnCrcdReportLossConfirmResponse.class);
    }

    /**
     * 信用卡挂失及补卡结果
     *
     * @param params
     * @return
     */
    public Observable<PsnCrcdReportLossResultResult> psnCrcdReportLossResult(PsnCrcdReportLossResultParams params) {
        return BIIClient.instance.post("PsnCrcdReportLossResult", params, PsnCrcdReportLossResultResponse.class);
    }

    /**
     * 信用卡挂失及补卡结果加强认证
     *
     * @param params
     * @return
     */
    public Observable<PsnCrcdReportLossResultReinforceResult> psnCrcdReportLossResultReinforce(PsnCrcdReportLossResultReinforceParams params) {
        return BIIClient.instance.post("PsnCrcdReportLossResultReinforce", params, PsnCrcdReportLossResultReinforceResponse.class);
    }

    /**
     * 医保账户详情查询
     *
     * @param params
     * @return
     */
    public Observable<PsnMedicalInsurAcctDetailQueryResult> psnMedicalInsurAcctDetailQuery(PsnMedicalInsurAcctDetailQueryParams params) {
        return BIIClient.instance.post("PsnMedicalInsurAcctDetailQuery", params, PsnMedicalInsurAcctDetailQueryResponse.class);
    }

    /**
     * 医保账户交易明细查询
     *
     * @param params
     * @return
     */
    public Observable<PsnMedicalInsurAcctTransferDetailQueryResult> psnMedicalInsurAcctTransferDetailQuery(PsnMedicalInsurAcctTransferDetailQueryParams params) {
        return BIIClient.instance.post("PsnMedicalInsurAcctTransferDetailQuery", params, PsnMedicalInsurAcctTransferDetailQueryResponse.class);
    }

    /**
     * 交易明细
     *
     * @param params
     * @return
     */
    public Observable<PsnAccountQueryTransferDetailResult> psnAccountQueryTransferDetail(PsnAccountQueryTransferDetailParams params) {
        return BIIClient.instance.post("PsnAccountQueryTransferDetail", params, PsnAccountQueryTransferDetailResponse.class);
    }

    /**
     * 交易明细 -- 查询最大跨度和最长时间范围
     *
     * @param params
     * @return
     */
    public Observable<PsnInquiryRangeQueryResult> psnInquiryRangeQuery(PsnInquiryRangeQueryParams params) {
        return BIIClient.instance.post("PsnInquiryRangeQuery", params, PsnInquiryRangeQueryResponse.class);
    }

    /**
     * 查询账户详情
     *
     * @param params 参数
     * @return 账户详情数据
     */
    public Observable<PsnAccountQueryAccountDetailResult> psnAccountQueryAccountDetail(PsnAccountQueryAccountDetailParams params) {
        return BIIClient.instance.post("PsnAccountQueryAccountDetail", params, PsnAccountQueryAccountDetailResponse.class);

    }

    /**
     * 获取账号通知信息
     *
     * @param params
     * @return
     */
    public Observable<PsnSsmQueryResult> psnSsmQuery(PsnSsmQueryParam params) {

        return BIIClient.instance.post("PsnSsmQuery", params, PsnSsmQueryResponse.class);
    }

    /**
     * 费用信息查询
     *
     * @param params
     * @return
     */
    public Observable<PsnSsmFeeQueryResult> psnSsmFeeQuery(PsnSsmFeeQueryParam params) {

        return BIIClient.instance.post("PsnSsmFeeQuery", params, PsnSsmFeeQueryResponse.class);
    }

    /**
     * 查询优惠期
     *
     * @param params
     * @return
     */
    public Observable<PsnSsmDiscountmodelResult> psnSsmDiscountmodel(PsnSsmDiscountmodelParam params) {

        return BIIClient.instance.post("PsnSsmDiscountmodel", params, PsnSsmDiscountmodelResponse.class);
    }

    /**
     * 获取手机验证码
     *
     * @param params
     * @return
     */
    public Observable<PsnSsmSendResult> psnSsmSend(PsnSsmSendParam params) {

        return BIIClient.instance.post("PsnSsmSend", params, PsnSsmSendResponse.class);
    }

    /**
     * 提交短信通知
     *
     * @param params
     * @return
     */
    public Observable<String> psnSsmMade(PsnSsmMadeParam params) {

        return BIIClient.instance.post("PsnSsmMade", params, PsnSsmMadeResponse.class);
    }

    /**
     * 定制短信预交易
     *
     * @param params
     * @return
     */
    public Observable<PsnSsmMadePreResult> psnSsmMadePre(PsnSsmMadePreParam params) {

        return BIIClient.instance.post("PsnSsmMadePre", params, PsnSsmMadePreResponse.class);
    }

    /**
     * 短信服务信息修改
     *
     * @param params
     * @return
     */
    public Observable<PsnSsmMessageChangeResult> psnSsmMessageChange(
            PsnSsmMessageChangeParam params) {

        return BIIClient.instance.post("PsnSsmMessageChange", params, PsnSsmMessageChangeResponse.class);
    }

    /**
     * 短信服务信息修改预交易
     *
     * @param params
     * @return
     */
    public Observable<PsnSsmMessageChangePreResult> psnSsmMessageChangePre(PsnSsmMessageChangePreParam params) {

        return BIIClient.instance.post("PsnSsmMessageChangePre", params, PsnSsmMessageChangePreResponse.class);
    }

    /**
     * 查询账户信息
     *
     * @return 返回值
     */
    public Observable<PsnQueryOprLoginInfoResult> psnQueryOprLoginInfo(PsnQueryOprLoginInfoParams params) {
        return BIIClient.instance.post("PsnCommonQueryOprLoginInfo", params,
                PsnQueryOprLoginInfoResponse.class);
    }

    /**
     * 查询信用卡币种
     *
     * @param params
     * @return
     */
    public Observable<PsnCrcdCurrencyQueryResult> psnCrcdCurrencyQuery(PsnCrcdCurrencyQueryParams params) {
        return BIIClient.instance.post("PsnCrcdCurrencyQuery", params,
                PsnCrcdCurrencyQueryResponse.class);
    }

    /**
     * 信用卡帐户详情查询
     *
     * @param params
     * @return
     */
    public Observable<PsnCrcdQueryAccountDetailResult> psnCrcdQueryAccountDetail(PsnCrcdQueryAccountDetailParams params) {
        return BIIClient.instance.post("PsnCrcdQueryAccountDetail", params, PsnCrcdQueryAccountDetailResponse.class);
    }

    /**
     * 查询最近交易明细新接口  --支持账户类型借记卡119（包括医保账户）、活期一本通188、普通活期101、零存整取150、教育储蓄152、存本取息140、网上理财账户190、优汇通专户199
     * @param params
     * @return
     */
    public Observable<PsnAccountQryRecentTransDetailResult> psnAccountQryRecentTransDetail(PsnAccountQryRecentTransDetailParams params){
        return BIIClient.instance.post("PsnAccountQryRecentTransDetail",params, PsnAccountQryRecentTransDetailResponse.class);
    }

    /**
     * 修改别名
     *
     * @param params
     * @return
     */
    public Observable<String> psnAccountModifyAccountAlias(PsnAccountModifyAccountAliasParams params) {
        return BIIClient.instance.post("PsnAccountModifyAccountAlias", params, PsnAccountModifyAccountAliasResponse.class);
    }

    /**
     * 取消关联
     *
     * @param params
     * @return
     */
    public Observable<String> psnSVRCancelAccRelation(PsnSVRCancelAccRelationParams params) {
        return BIIClient.instance.post("PsnSVRCancelAccRelation", params, PsnSVRCancelAccRelationResponse.class);
    }

    /**
     * 删除签约信息
     *
     * @param params
     * @return
     */
    public Observable<PsnSsmMessageDeleteResult> psnSsmMessageDelete(PsnSsmMessageDeleteParam params) {
        return BIIClient.instance.post("PsnSsmMessageDelete", params, PsnSsmMessageDeleteResponse.class);
    }

    /**
     * 电子现金账户详情及余额
     *
     * @param params
     * @return
     */
    public Observable<PsnFinanceICAccountDetailResult> psnFinanceICAccountDetail(PsnFinanceICAccountDetailParams params) {
        return BIIClient.instance.post("PsnFinanceICAccountDetail", params, PsnFinanceICAccountDetailResponse.class);
    }

    /**
     * 修改付费账户预交易
     *
     * @param params
     * @return
     */
    public Observable<PsnSsmAccountChangePreResult> psnSsmAccountChangePre(PsnSsmAccountChangePreParam params) {
        return BIIClient.instance.post("PsnSsmAccountChangePre", params, PsnSsmAccountChangePreResponse.class);
    }

    /**
     * 修改付费账户
     *
     * @param params
     * @return
     */
    public Observable<PsnSsmAccountChangeResult> psnSsmAccountChange(PsnSsmAccountChangeParam params) {
        return BIIClient.instance.post("PsnSsmAccountChange", params, PsnSsmAccountChangeResponse.class);
    }

    /**
     * 添加签约信息
     *
     * @param params
     * @return
     */
    public Observable<PsnSsmMessageAddResult> psnSsmMessageAdd(PsnSsmMessageAddParam params) {
        return BIIClient.instance.post("PsnSsmMessageAdd", params, PsnSsmMessageAddResponse.class);
    }

    /**
     * 签约短信通预交易
     *
     * @param params
     * @return
     */
    public Observable<PsnSsmSignPreResult> snSsmSignPre(PsnSsmSignPreParam params) {
        return BIIClient.instance.post("PsnSsmSignPre", params, PsnSsmSignPreResponse.class);
    }

    /**
     * 签约短信通
     *
     * @param params
     * @return
     */
    public Observable<PsnSsmSignResult> psnSsmSign(PsnSsmSignParam params) {
        return BIIClient.instance.post("PsnSsmSign", params, PsnSsmSignResponse.class);
    }

    /**
     * 批量删除短信
     *
     * @param params
     * @return
     */
    public Observable<PsnSsmDeleteResult> psnSsmDelete(PsnSsmDeleteParam params) {
        return BIIClient.instance.post("PsnSsmDelete", params, PsnSsmDeleteResponse.class);
    }

    /**
     * 网上理财账户查询
     *2016-9-12 wangy
     * @param params
     * @return
     */
    public Observable<PsnOFAAccountStateQueryResult> queryPsnOFAAccountState(PsnOFAAccountStateQueryParams params) {
        return BIIClient.instance.post("PsnOFAAccountStateQuery", params, PsnOFAAccountStateQueryResponse.class);
    }

    /**
     * 查询已设置交易限额
     * @author wangyang
     * @time 2016/10/10 20:55
     */
    public Observable<PsnDebitCardQryQuotaResult> psnDebitCardQryQuota(PsnDebitCardQryQuotaParams params) {
        return BIIClient.instance.post("PsnDebitCardQryQuota", params, PsnDebitCardQryQuotaResponse.class);
    }

    /**
     * 设置交易限额预交易
     * @author wangyang
     * @time 2016/10/10 20:55
     */
    public Observable<FactorAndCaResult> psnDebitCardSetQuotaPre(PsnDebitCardSetQuotaPreParams params) {
        return BIIClient.instance.post("PsnDebitCardSetQuotaPre", params, PublicSecurityResponse.class);
    }

    /**
     * 设置交易限额交易
     * @author wangyang
     * @time 2016/10/10 20:55
     */
    public Observable<String> psnDebitCardSetQuota(PsnDebitCardSetQuotaParams params) {
        return BIIClient.instance.post("PsnDebitCardSetQuota", params, PsnDebitCardSetQuotaResponse.class);
    }

    /**
     * 查询限额
     * @param params PsnTransQuotaQueryParams
     * @return
     */
    public Observable<PsnTransQuotaQueryResult> psnTransQuotaQuery(PsnTransQuotaQueryParams params) {
        return BIIClient.instance.post("PsnTransQuotaQuery", params, PsnTransQuotaQueryResponse.class);
    }


    /**
     * 查询投资交易账号绑定信息
     * @param params
     * @return
     */
    public Observable<PsnQueryInvtBindingInfoResult> psnQueryInvtBindingInfo(PsnQueryInvtBindingInfoParams params){
        return BIIClient.instance.post("QueryInvtBindingInfo", params, PsnQueryInvtBindingInfoResponse.class);
    }
}
