package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service;


import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PnsXpadInvestAgreementModifyVerify.PsnXpadInvestAgreementModifyVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PnsXpadInvestAgreementModifyVerify.PsnXpadInvestAgreementModifyVerifyResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PnsXpadInvestAgreementModifyVerify.PsnXpadInvestAgreementModifyVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvestmentManageCancel.PsnInvestmentManageCancelParam;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvestmentManageCancel.PsnInvestmentManageCancelResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvestmentManageCancel.PsnInvestmentManageCancelResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvestmentManageIsOpen.PsnInvestmentManageIsOpenParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvestmentManageIsOpen.PsnInvestmentManageIsOpenResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvestmentManageOpen.PsnInvestmentManageOpenParam;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvestmentManageOpen.PsnInvestmentManageOpenResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvestmentManageOpen.PsnInvestmentManageOpenResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvestmentManageOpenConfirm.PsnInvestmentManageOpenConfirmParam;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvestmentManageOpenConfirm.PsnInvestmentManageOpenConfirmResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvestmentManageOpenConfirm.PsnInvestmentManageOpenConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvtEvaluationInit.PsnInvtEvaluationInitParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvtEvaluationInit.PsnInvtEvaluationInitResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvtEvaluationInit.PsnInvtEvaluationInitResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvtEvaluationResult.PsnInvtEvaluationResultParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvtEvaluationResult.PsnInvtEvaluationResultResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvtEvaluationResult.PsnInvtEvaluationResultResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOFAAccountStateQuery.PsnOFAAccountStateQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOFAAccountStateQuery.PsnOFAAccountStateQueryRespons;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOFAAccountStateQuery.PsnOFAAccountStateQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOFADisengageBind.PsnOFADisengageBindParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOFADisengageBind.PsnOFADisengageBindResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOFADisengageBind.PsnOFADisengageBindResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOFAIncreaseBind.PsnOFAIncreaseBindParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOFAIncreaseBind.PsnOFAIncreaseBindRespons;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOFAIncreaseBind.PsnOFAIncreaseBindResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOcrmProductQuery.PsnOcrmProductQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOcrmProductQuery.PsnOcrmProductQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOcrmProductQuery.PsnOcrmProductQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountCancel.PsnXpadAccountCancelParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountCancel.PsnXpadAccountCancelResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountCancel.PsnXpadAccountCancelResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAgreementInfoQuery.PsnXpadAgreementInfoQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAgreementInfoQuery.PsnXpadAgreementInfoQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAgreementInfoQuery.PsnXpadAgreementInfoQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAgreementModifyResult.PsnXpadAgreementModifyResultParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAgreementModifyResult.PsnXpadAgreementModifyResultResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAgreementModifyResult.PsnXpadAgreementModifyResultResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadApplyAgreementResult.PsnXpadApplyAgreementResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadApplyAgreementResult.PsnXpadApplyAgreementResultParam;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadApplyAgreementResult.PsnXpadApplyAgreementResultResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAptitudeTreatyApplyCommit.PsnXpadAptitudeTreatyApplyCommitParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAptitudeTreatyApplyCommit.PsnXpadAptitudeTreatyApplyCommitResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAptitudeTreatyApplyCommit.PsnXpadAptitudeTreatyApplyCommitResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAptitudeTreatyApplyVerify.PsnXpadAptitudeTreatyApplyVerifyParam;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAptitudeTreatyApplyVerify.PsnXpadAptitudeTreatyApplyVerifyResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAptitudeTreatyApplyVerify.PsnXpadAptitudeTreatyApplyVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAptitudeTreatyDetailQuery.PsnXpadAptitudeTreatyDetailQueryParam;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAptitudeTreatyDetailQuery.PsnXpadAptitudeTreatyDetailQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAptitudeTreatyDetailQuery.PsnXpadAptitudeTreatyDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAutComTradStatus.PsnXpadAutComTradStatusParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAutComTradStatus.PsnXpadAutComTradStatusReponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAutComTradStatus.PsnXpadAutComTradStatusResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAutTradStatus.PsnXpadAutTradStatusParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAutTradStatus.PsnXpadAutTradStatusResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAutTradStatus.PsnXpadAutTradStatusResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAutomaticAgreementMaintainResult.PsnXpadAutomaticAgreementMaintainResultParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAutomaticAgreementMaintainResult.PsnXpadAutomaticAgreementMaintainResultResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAutomaticAgreementMaintainResult.PsnXpadAutomaticAgreementMaintainResultResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadBenchmarkMaintainResult.PsnXpadBenchmarkMaintainResultParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadBenchmarkMaintainResult.PsnXpadBenchmarkMaintainResultResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadBenchmarkMaintainResult.PsnXpadBenchmarkMaintainResultResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadCapacityQuery.PsnXpadCapacityQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadCapacityQuery.PsnXpadCapacityQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadCapacityQuery.PsnXpadCapacityQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadCapacityTransList.PsnXpadCapacityTransListParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadCapacityTransList.PsnXpadCapacityTransListResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadCapacityTransList.PsnXpadCapacityTransListResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadDelegateCancel.PsnXpadDelegateCancelParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadDelegateCancel.PsnXpadDelegateCancelResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadDelegateCancel.PsnXpadDelegateCancelResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadDueProductProfitQuery.PsnXpadDueProductProfitQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadDueProductProfitQuery.PsnXpadDueProductProfitQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadDueProductProfitQuery.PsnXpadDueProductProfitQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadExpectYieldQuery.PsnXpadExpectYieldQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadExpectYieldQuery.PsnXpadExpectYieldQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadExpectYieldQuery.PsnXpadExpectYieldQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadExpectYieldQueryOutlay.PsnXpadExpectYieldQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadExpectYieldQueryOutlay.PsnXpadExpectYieldQueryOutlayResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadExpectYieldQueryOutlay.PsnXpadExpectYieldQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadGuarantyBuyResult.PsnXpadGuarantyBuyResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadGuarantyBuyResult.PsnXpadGuarantyBuyResultParam;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadGuarantyBuyResult.PsnXpadGuarantyBuyResultResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHisTradStatus.PsnXpadHisTradStatusParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHisTradStatus.PsnXpadHisTradStatusResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHisTradStatus.PsnXpadHisTradStatusResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHoldProductAndRedeem.PsnXpadHoldProductAndRedeemParms;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHoldProductAndRedeem.PsnXpadHoldProductAndRedeemResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHoldProductAndRedeem.PsnXpadHoldProductAndRedeemResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHoldProductQueryList.PsnXpadHoldProductQueryListParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHoldProductQueryList.PsnXpadHoldProductQueryListResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHoldProductQueryList.PsnXpadHoldProductQueryListResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHoldProductRedeemVerify.PsnXpadHoldProductRedeemVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHoldProductRedeemVerify.PsnXpadHoldProductRedeemVerifyResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHoldProductRedeemVerify.PsnXpadHoldProductRedeemVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadInvestAgreementCancel.PsnXpadInvestAgreementCancelParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadInvestAgreementCancel.PsnXpadInvestAgreementCancelResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadInvestAgreementCancel.PsnXpadInvestAgreementCancelResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadInvestAgreementModifyCommit.PsnXpadInvestAgreementModifyCommitParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadInvestAgreementModifyCommit.PsnXpadInvestAgreementModifyCommitResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadInvestAgreementModifyCommit.PsnXpadInvestAgreementModifyCommitResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadNetHistoryQuery.PsnXpadNetHistoryQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadNetHistoryQuery.PsnXpadNetHistoryQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadNetHistoryQuery.PsnXpadNetHistoryQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadNetHistoryQueryOutlay.PsnXpadNetHistoryQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadNetHistoryQueryOutlay.PsnXpadNetHistoryQueryOutlayResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadNetHistoryQueryOutlay.PsnXpadNetHistoryQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductBalanceQuery.PsnXpadProductBalanceQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductBalanceQuery.PsnXpadProductBalanceQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductBalanceQuery.PsnXpadProductBalanceQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductBuyPre.PsnXpadProductBuyPreParam;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductBuyPre.PsnXpadProductBuyPreResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductBuyPre.PsnXpadProductBuyPreResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductBuyResult.PsnXpadProductBuyResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductBuyResult.PsnXpadProductBuyResultParam;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductBuyResult.PsnXpadProductBuyResultResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductDetailQuery.PsnXpadProductDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductDetailQuery.PsnXpadProductDetailQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductDetailQuery.PsnXpadProductDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductInvestTreatyQuery.PsnXpadProductInvestTreatyQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductInvestTreatyQuery.PsnXpadProductInvestTreatyQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductInvestTreatyQuery.PsnXpadProductInvestTreatyQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductListQuery.PsnXpadProductListQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductListQuery.PsnXpadProductListQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductListQuery.PsnXpadProductListQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductQueryAndBuyInit.PsnXpadProductQueryAndBuyInitParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductQueryAndBuyInit.PsnXpadProductQueryAndBuyInitResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductQueryAndBuyInit.PsnXpadProductQueryAndBuyInitResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProfitCount.PsnXpadProfitCountParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProfitCount.PsnXpadProfitCountResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProfitCount.PsnXpadProfitCountResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProgressQuery.PsnXpadProgressQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProgressQuery.PsnXpadProgressQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProgressQuery.PsnXpadProgressQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProgressQueryOutlay.PsnXpadProgressQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProgressQueryOutlay.PsnXpadProgressQueryOutlayResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProgressQueryOutlay.PsnXpadProgressQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQuantityDetail.PsnXpadQuantityDetailParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQuantityDetail.PsnXpadQuantityDetailResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQuantityDetail.PsnXpadQuantityDetailResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryGuarantyProductList.PsnXpadQueryGuarantyProductListParam;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryGuarantyProductList.PsnXpadQueryGuarantyProductListResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryGuarantyProductList.PsnXpadQueryGuarantyProductListResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryGuarantyProductResult.PsnXpadQueryGuarantyProductResultParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryGuarantyProductResult.PsnXpadQueryGuarantyProductResultResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryGuarantyProductResult.PsnXpadQueryGuarantyProductResultResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryHisGuarantyProductListResult.PsnXpadQueryHisGuarantyProductListResultParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryHisGuarantyProductListResult.PsnXpadQueryHisGuarantyProductListResultResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryHisGuarantyProductListResult.PsnXpadQueryHisGuarantyProductListResultResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryRiskMatch.PsnXpadQueryRiskMatchParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryRiskMatch.PsnXpadQueryRiskMatchResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryRiskMatch.PsnXpadQueryRiskMatchResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadRecentAccountQuery.PsnXpadRecentAccountQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadRecentAccountQuery.PsnXpadRecentAccountQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadRecentAccountQuery.PsnXpadRecentAccountQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadRecentAccountUpdate.PsnXpadRecentAccountUpdateParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadRecentAccountUpdate.PsnXpadRecentAccountUpdateResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadRecentAccountUpdate.PsnXpadRecentAccountUpdateResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadReferProfitDetailQuery.PsnXpadReferProfitDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadReferProfitDetailQuery.PsnXpadReferProfitDetailQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadReferProfitDetailQuery.PsnXpadReferProfitDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadReferProfitQuery.PsnXpadReferProfitQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadReferProfitQuery.PsnXpadReferProfitQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadReferProfitQuery.PsnXpadReferProfitQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadRemoveGuarantyProductResult.PsnXpadRemoveGuarantyProductResultParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadRemoveGuarantyProductResult.PsnXpadRemoveGuarantyProductResultResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadRemoveGuarantyProductResult.PsnXpadRemoveGuarantyProductResultResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadReset.PsnXpadResetParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadReset.PsnXpadResetResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadReset.PsnXpadResetResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadResult.PsnXpadResultParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadResult.PsnXpadResultResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadResult.PsnXpadResultResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadSetBonusMode.PsnXpadSetBonusModeParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadSetBonusMode.PsnXpadSetBonusModeResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadSetBonusMode.PsnXpadSetBonusModeResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadShareTransitionCommit.PsnXpadShareTransitionCommitParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadShareTransitionCommit.PsnXpadShareTransitionCommitResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadShareTransitionCommit.PsnXpadShareTransitionCommitResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadShareTransitionVerify.PsnXpadShareTransitionVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadShareTransitionVerify.PsnXpadShareTransitionVerifyResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadShareTransitionVerify.PsnXpadShareTransitionVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadSignInit.PsnXpadSignInitParam;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadSignInit.PsnXpadSignInitResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadSignInit.PsnXpadSignInitResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadSignResult.PsnXpadSignResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadSignResult.PsnXpadSignResultParam;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadSignResult.PsnXpadSignResultResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadTransInfoDetailQuery.PsnXpadTransInfoDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadTransInfoDetailQuery.PsnXpadTransInfoDetailQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadTransInfoDetailQuery.PsnXpadTransInfoDetailQueryResult;
import com.boc.bocsoft.mobile.bii.common.client.BIIClient;
import com.boc.bocsoft.mobile.bii.common.client.BIIClientConfig;

import java.util.List;

import rx.Observable;

/**
 * /**
 *
 * @author yx
 * @description 中银理财-service
 * @date 2016/9/06
 */
public class WealthManagementService {

    /**
     * I42-4.40 040产品详情查询  PsnXpadProductDetailQuery
     *
     * @param params
     * @return
     * @author yx
     * @date 2016-9-7 16:51:15
     */
    public Observable<PsnXpadProductDetailQueryResult> psnXpadProductDetailQuery(PsnXpadProductDetailQueryParams params) {

        return BIIClient.instance.post("PsnXpadProductDetailQuery", params, PsnXpadProductDetailQueryResponse.class);
    }

    /**
     * I42-4.37 037 查询客户理财账户信息PsnXpadAccountQuery
     *
     * @param params
     * @return
     * @author zhx
     */
    public Observable<PsnXpadAccountQueryResult> psnXpadAccountQuery(PsnXpadAccountQueryParams params) {
        return BIIClient.instance.post("PsnXpadAccountQuery", params, PsnXpadAccountQueryResponse.class);
    }

    /**
     * 投资协议列表查询psnXpadCapacityQuery
     *
     * @param params
     * @return
     * @author guokai
     */
    public Observable<PsnXpadCapacityQueryResult> psnXpadCapacityQuery(PsnXpadCapacityQueryParams params) {
        return BIIClient.instance.post("PsnXpadCapacityQuery", params, PsnXpadCapacityQueryResponse.class);
    }

    /**
     * I42-4.33 033持有产品赎回预交易  PsnXpadHoldProductRedeemVerify
     *
     * @param params
     * @return
     * @author cff
     */
    public Observable<PsnXpadHoldProductRedeemVerifyResult> psnXpadHoldProductRedeemVerify(PsnXpadHoldProductRedeemVerifyParams params) {
        return BIIClient.instance.post("PsnXpadHoldProductRedeemVerify", params, PsnXpadHoldProductRedeemVerifyResponse.class);
    }

    /**
     * I42-4.13 013持有产品赎回
     *
     * @param params
     * @return
     * @author cff
     */
    public Observable<PsnXpadHoldProductAndRedeemResult> psnXpadHoldProductAndRedeem(PsnXpadHoldProductAndRedeemParms params) {
        return BIIClient.instance.post("PsnXpadHoldProductAndRedeem", params, PsnXpadHoldProductAndRedeemResponse.class);
    }

    /**
     * I42-4.12 012持有产品查询与赎回
     *
     * @param params
     * @return
     * @author cff
     */
    public Observable<List<PsnXpadHoldProductQueryListResult>> psnXpadHoldProductQueryList(PsnXpadHoldProductQueryListParams params) {
        return BIIClient.instance.post("PsnXpadHoldProductQueryList", params, PsnXpadHoldProductQueryListResponse.class);
    }

    /**
     * 持仓管理-持仓列表页 PsnInvestmentManageIsOpen
     *
     * @param params
     * @return
     * @author cff
     */
    public Observable<Boolean> psnInvestmentManageIsOpen(PsnInvestmentManageIsOpenParams params) {
        return BIIClient.instance.post("PsnInvestmentManageIsOpen", params, PsnInvestmentManageIsOpenResponse.class);
    }

    /**
     * PsnXpadProductBalanceQuery
     *
     * @param params
     * @return
     * @author yx
     * @date 2016-09-23 10:05:26
     */
    public Observable<List<PsnXpadProductBalanceQueryResult>> psnXpadProductBalanceQuery(PsnXpadProductBalanceQueryParams params) {
        return BIIClient.instance.post("PsnXpadProductBalanceQuery", params, PsnXpadProductBalanceQueryResponse.class);
    }

    /**
     * I42-4.21 021 历史常规交易状况查询 PsnXpadHisTradStatus
     *
     * @param params
     * @return
     * @author zhx
     */
    public Observable<PsnXpadHisTradStatusResult> psnXpadHisTradStatus(PsnXpadHisTradStatusParams params) {
        return BIIClient.instance.post("PsnXpadHisTradStatus", params, PsnXpadHisTradStatusResponse.class);
    }

    /**
     * I42-4.42 42 查询客户最近操作的理财账号 PsnXpadRecentAccountQuery
     *
     * @param params
     * @return
     * @author zhx
     */
    public Observable<PsnXpadRecentAccountQueryResult> psnXpadRecentAccountQuery(PsnXpadRecentAccountQueryParams params) {
        return BIIClient.instance.post("PsnXpadRecentAccountQuery", params, PsnXpadRecentAccountQueryResponse.class);
    }

    /**
     * 4.70 070   锁定期份额转换预交易    PsnXpadShareTransitionVerify
     *
     * @author zn
     * @date 2016-9-12
     */
    public Observable<PsnXpadShareTransitionVerifyResult> PsnXpadShareTransitionVerify(PsnXpadShareTransitionVerifyParams params) {
        return BIIClient.instance.post("PsnXpadShareTransitionVerify", params, PsnXpadShareTransitionVerifyResponse.class);
    }

    /**
     * 4.71 071   锁定期份额转换成交交易  PsnXpadShareTransitionCommit
     *
     * @author zn
     * @date 2016-9-12
     */
    public Observable<PsnXpadShareTransitionCommitResult> PsnXpadShareTransitionCommit(PsnXpadShareTransitionCommitParams params) {
        return BIIClient.instance.post("PsnXpadShareTransitionCommit", params, PsnXpadShareTransitionCommitResponse.class);
    }

    /**
     * 客户投资协议明细查询 PsnXpadAgreementInfoQuery
     *
     * @param params
     * @return
     */
    public Observable<PsnXpadAgreementInfoQueryResult> psnXpadAgreementInfoQuery(PsnXpadAgreementInfoQueryParams params) {
        return BIIClient.instance.post("PsnXpadAgreementInfoQuery", params, PsnXpadAgreementInfoQueryResponse.class);
    }

    /**
     * 终止协议
     *
     * @param params
     * @return
     */
    public Observable<PsnXpadInvestAgreementCancelResult> psnXpadInvestAgreementCancel(PsnXpadInvestAgreementCancelParams params) {
        return BIIClient.instance.post("PsnXpadInvestAgreementCancel", params, PsnXpadInvestAgreementCancelResponse.class);
    }

    /**
     * 产品查询与购买
     *
     * @param params
     * @return
     */
    public Observable<PsnXpadProductListQueryResult> psnXpadProductListQuery(PsnXpadProductListQueryParams params) {
        return BIIClient.instance.post("PsnXpadProductListQuery", params, PsnXpadProductListQueryResponse.class);
    }

    /**
     * 更新客户最近操作的理财账号
     *
     * @param params
     * @return
     */
    public Observable<PsnXpadRecentAccountUpdateResult> psnXpadRecentAccountUpdate(PsnXpadRecentAccountUpdateParams params) {
        return BIIClient.instance.post("PsnXpadRecentAccountUpdate", params, PsnXpadRecentAccountUpdateResponse.class);
    }

    /**
     * 风险评估查询
     *
     * @param params
     * @return
     */
    public Observable<PsnInvtEvaluationInitResult> psnInvtEvaluationInit(PsnInvtEvaluationInitParams params) {
        return BIIClient.instance.post("PsnInvtEvaluationInit", params, PsnInvtEvaluationInitResponse.class);
    }

    /**
     * I42-4.56 056 到期产品查询列表 PsnXpadDueProductProfitQuery
     *
     * @param params
     * @return
     * @author zhx
     */
    public Observable<PsnXpadDueProductProfitQueryResult> psnXpadDueProductProfitQuery(PsnXpadDueProductProfitQueryParams params) {
        return BIIClient.instance.post("PsnXpadDueProductProfitQuery", params, PsnXpadDueProductProfitQueryResponse.class);
    }

    /**
     * I42-4.75 075 委托常规交易状况查询 PsnXpadAutTradStatus
     *
     * @param params
     * @return
     * @author zhx
     */
    public Observable<PsnXpadAutTradStatusResult> psnXpadAutTradStatus(PsnXpadAutTradStatusParams params) {
        return BIIClient.instance.post("PsnXpadAutTradStatus", params, PsnXpadAutTradStatusResponse.class);
    }


    /**
     * I42-4.57 057 交易状况详细信息查询 PsnXpadTransInfoDetailQuery
     *
     * @param params
     * @return
     * @author zhx
     */
    public Observable<PsnXpadTransInfoDetailQueryResult> psnXpadTransInfoDetailQuery(PsnXpadTransInfoDetailQueryParams params) {
        return BIIClient.instance.post("PsnXpadTransInfoDetailQuery", params, PsnXpadTransInfoDetailQueryResponse.class);
    }

    /**
     * 查询持有产品列表
     *
     * @param params
     * @return
     */
    public Observable<PsnXpadQueryGuarantyProductListResult> psnXpadQueryGuarantyProductList(PsnXpadQueryGuarantyProductListParam params) {
        return BIIClient.instance.post("PsnXpadQueryGuarantyProductList", params, PsnXpadQueryGuarantyProductListResponse.class);
    }


    /**
     * I42-029 查询组合历史产品列表PsnXpadQueryHisGuarantyProductListResult
     *
     * @param params
     * @return
     * @author zc
     */
    public Observable<PsnXpadQueryHisGuarantyProductListResultResult> psnXpadQueryHisGuarantyProductState(PsnXpadQueryHisGuarantyProductListResultParams params) {
        return BIIClient.instance.post("PsnXpadQueryHisGuarantyProductListResult", params, PsnXpadQueryHisGuarantyProductListResultResponse.class);
    }


    /**
     * I42-4.76 076 委托组合交易状况查询PsnXpadAutComTradStatus
     *
     * @param params
     * @return
     * @author zhx
     */
    public Observable<PsnXpadAutComTradStatusResult> psnXpadAutComTradStatus(PsnXpadAutComTradStatusParams params) {
        return BIIClient.instance.post("PsnXpadAutComTradStatus", params, PsnXpadAutComTradStatusReponse.class);
    }

    /**
     * I42-4.31 031 组合购买已押押品查询 PsnXpadQueryGuarantyProductResult
     *
     * @param params
     * @return
     * @author zhx
     */
    public Observable<List<PsnXpadQueryGuarantyProductResultResult>> psnXpadQueryGuarantyProductResult(PsnXpadQueryGuarantyProductResultParams params) {
        return BIIClient.instance.post("PsnXpadQueryGuarantyProductResult", params, PsnXpadQueryGuarantyProductResultResponse.class);
    }

    /**
     * 客户投资协议交易明细查询
     *
     * @param params
     * @return
     * @author guokai
     */
    public Observable<List<PsnXpadCapacityTransListResult>> psnXpadCapacityTransList(PsnXpadCapacityTransListParams params) {
        return BIIClient.instance.post("PsnXpadCapacityTransList", params, PsnXpadCapacityTransListResponse.class);
    }

    /**
     * 查询客户风险等级与产品风险等级是否匹配
     *
     * @param params
     * @return
     * @author guokai
     */
    public Observable<PsnXpadQueryRiskMatchResult> psnXpadQueryRiskMatch(PsnXpadQueryRiskMatchParams params) {
        return BIIClient.instance.post("PsnXpadQueryRiskMatch", params, PsnXpadQueryRiskMatchResponse.class);
    }

    /**
     * 组合购买
     *
     * @param params
     * @return
     * @author wangtong
     */
    public Observable<PsnXpadGuarantyBuyResult> psnXpadGuarantyBuyResult(PsnXpadGuarantyBuyResultParam params) {
        return BIIClient.instance.post("PsnXpadGuarantyBuyResult", params, PsnXpadGuarantyBuyResultResponse.class);
    }

    /**
     * I42-4.31 031 常规委托交易撤单 PsnXpadDelegateCancel
     *
     * @param params
     * @return
     * @author zhx
     */
    public Observable<PsnXpadDelegateCancelResult> psnXpadDelegateCancel(PsnXpadDelegateCancelParams params) {
        return BIIClient.instance.post("PsnXpadDelegateCancel", params, PsnXpadDelegateCancelResponse.class);
    }

    /**
     * 参考收益汇总查询  请求Model
     *
     * @param params PsnXpadReferProfitQuery
     * @return
     * @author zn
     */
    public Observable<PsnXpadReferProfitQueryResult> psnXpadReferProfitQuery(PsnXpadReferProfitQueryParams params) {
        return BIIClient.instance.post("PsnXpadReferProfitQuery", params, PsnXpadReferProfitQueryResponse.class);
    }

    /**
     * 4.59 059 参考收益详情查询 PsnXpadReferProfitDetailQuery
     *
     * @param params cff  2016-9-22
     * @return
     */
    public Observable<PsnXpadReferProfitDetailQueryResult> psnXpadReferProfitDetailQuery(PsnXpadReferProfitDetailQueryParams params) {
        return BIIClient.instance.post("PsnXpadReferProfitDetailQuery", params, PsnXpadReferProfitDetailQueryResponse.class);
    }

    /**
     * 4.14 014修改分红方式交易 PsnXpadSetBonusMode
     *
     * @param params
     * @return
     * @author yx
     */
    public Observable<PsnXpadSetBonusModeResult> psnXpadSetBonusMode(PsnXpadSetBonusModeParams params) {
        return BIIClient.instance.post("PsnXpadSetBonusMode", params, PsnXpadSetBonusModeResponse.class);
    }

    /**
     * I42-4.30 030 组合购买解除  PsnXpadRemoveGuarantyProductResult
     *
     * @param params
     * @return
     * @author zhx
     */
    public Observable<PsnXpadRemoveGuarantyProductResultResult> psnXpadRemoveGuarantyProductResult(PsnXpadRemoveGuarantyProductResultParams params) {
        return BIIClient.instance.post("PsnXpadRemoveGuarantyProductResult", params, PsnXpadRemoveGuarantyProductResultResponse.class);
    }

    /**
     * I42-4.2 002理财账号重新登记(资金账户列表)PsnXpadReset
     *
     * @param params
     * @return
     * @author Wan mengxin
     */
    public Observable<List<PsnXpadResetResult>> psnXpadReset(PsnXpadResetParams params) {
        return BIIClient.instance.post("PsnXpadReset", params, PsnXpadResetResponse.class);
    }

    /**
     * I42-4.3 003理财账号登记结果PsnXpadResult
     *
     * @param params
     * @return
     * @author Wan mengxin
     */
    public Observable<PsnXpadResultResult> psnXpadResult(PsnXpadResultParams params) {
        return BIIClient.instance.post("PsnXpadResult", params, PsnXpadResultResponse.class);
    }

    /**
     * 产品查询与购买初始化(产品种类查询)
     *
     * @param params
     * @return
     */
    public Observable<List<PsnXpadProductQueryAndBuyInitResult>> psnXpadProductQueryAndBuyInit(PsnXpadProductQueryAndBuyInitParams params) {
        return BIIClient.instance.post("PsnXpadProductQueryAndBuyInit", params, PsnXpadProductQueryAndBuyInitResponse.class);
    }

    /**
     * 风险评估提交
     *
     * @param params
     * @return
     */
    public Observable<PsnInvtEvaluationResultResult> psnInvtEvaluationResult(PsnInvtEvaluationResultParams params) {
        return BIIClient.instance.post("PsnInvtEvaluationResult", params, PsnInvtEvaluationResultResponse.class);
    }

    /**
     * 4.68 068份额明细查询 PsnXpadQuantityDetail
     *
     * @param params
     * @return
     */
    public Observable<PsnXpadQuantityDetailResult> psnXpadQuantityDetail(PsnXpadQuantityDetailParams params) {
        return BIIClient.instance.post("PsnXpadQuantityDetail", params, PsnXpadQuantityDetailResponse.class);
    }

    /**
     * 投资协议修改预交易
     *
     * @param params
     * @return
     */
    public Observable<PsnXpadInvestAgreementModifyVerifyResult> psnXpadInvestAgreementModifyVerify(PsnXpadInvestAgreementModifyVerifyParams params) {
        return BIIClient.instance.post("PsnXpadInvestAgreementModifyVerify", params, PsnXpadInvestAgreementModifyVerifyResponse.class);
    }

    /**
     * 投资协议修改提交
     *
     * @param params
     * @return
     */
    public Observable<PsnXpadInvestAgreementModifyCommitResult> psnXpadInvestAgreementModifyCommit(PsnXpadInvestAgreementModifyCommitParams params) {
        return BIIClient.instance.post("PsnXpadInvestAgreementModifyCommit", params, PsnXpadInvestAgreementModifyCommitResponse.class);
    }

    /**
     * 协议修改结果
     *
     * @param params
     * @return
     */
    public Observable<PsnXpadAgreementModifyResultResult> psnXpadAgreementModifyResult(PsnXpadAgreementModifyResultParams params) {
        return BIIClient.instance.post("PsnXpadAgreementModifyResult", params, PsnXpadAgreementModifyResultResponse.class);
    }

    /**
     * 协议维护
     *
     * @param params
     * @return
     */
    public Observable<PsnXpadAutomaticAgreementMaintainResultResult> psnXpadAutomaticAgreementMaintainResult(PsnXpadAutomaticAgreementMaintainResultParams params) {
        return BIIClient.instance.post("PsnXpadAutomaticAgreementMaintainResult", params, PsnXpadAutomaticAgreementMaintainResultResponse.class);
    }

    /**
     * 指令交易产品查询  PsnOcrmProductQuery
     *
     * @param params
     * @author Wan mengxin
     */
    public Observable<PsnOcrmProductQueryResult> psnOcrmProductQuery(PsnOcrmProductQueryParams params) {
        return BIIClient.instance.post("PsnOcrmProductQuery", params, PsnOcrmProductQueryResponse.class);
    }

    /**
     * 业绩基准周期滚续协议修改
     *
     * @param params
     * @author guokai
     */
    public Observable<PsnXpadBenchmarkMaintainResultResult> psnXpadBenchmarkMaintainResult(PsnXpadBenchmarkMaintainResultParams params) {
        return BIIClient.instance.post("PsnXpadBenchmarkMaintainResult", params, PsnXpadBenchmarkMaintainResultResponse.class);
    }

    /**
     * 4.46 046 网上专属理财账户状态查询 PsnOFAAccountStateQuery
     *
     * @param params
     * @author Wan mengxin
     */
    public Observable<PsnOFAAccountStateQueryResult> psnOFAAccountStateQuery(PsnOFAAccountStateQueryParams params) {
        return BIIClient.instance.post("PsnOFAAccountStateQuery", params, PsnOFAAccountStateQueryRespons.class);
    }

    /**
     * 4.49 049重新绑定理财账户PsnOFAIncreaseBind
     *
     * @param params
     * @author Wan mengxin
     */
    public Observable<PsnOFAIncreaseBindResult> psnOFAIncreaseBind(PsnOFAIncreaseBindParams params) {
        return BIIClient.instance.post("PsnOFAIncreaseBind", params, PsnOFAIncreaseBindRespons.class);
    }

    /**
     * 产品购买预处理
     *
     * @param params
     * @author wangtong
     */
    public Observable<PsnXpadProductBuyPreResult> psnXpadProductBuyPre(PsnXpadProductBuyPreParam params) {
        return BIIClient.instance.post("PsnXpadProductBuyPre", params, PsnXpadProductBuyPreResponse.class);
    }

    /**
     * 产品购买处理结果
     *
     * @param params
     * @author wangtong
     */
    public Observable<PsnXpadProductBuyResult> psnXpadProductBuyResult(PsnXpadProductBuyResultParam params) {
        return BIIClient.instance.post("PsnXpadProductBuyResult", params, PsnXpadProductBuyResultResponse.class);
    }

    /**
     * 1.1.4.3  PsnXpadAccountCancel  理财账户解除登记
     *
     * @param params
     * @author Wan mengxin
     */
    public Observable<PsnXpadAccountCancelResult> PsnXpadAccountCancel(PsnXpadAccountCancelParams params) {
        return BIIClient.instance.post("PsnXpadAccountCancel", params, PsnXpadAccountCancelResponse.class);
    }

    /**
     * 1.1.4.5 PsnOFADisengageBind 解除理财账户绑定
     *
     * @param params
     * @author Wan mengxin
     */
    public Observable<PsnOFADisengageBindResult> PsnOFADisengageBind(PsnOFADisengageBindParams params) {
        return BIIClient.instance.post("PsnOFADisengageBind", params, PsnOFADisengageBindResponse.class);
    }

    /**
     * 取消开通投资服务
     *
     * @param params
     * @author wangtong
     */
    public Observable<PsnInvestmentManageCancelResult> psnInvestmentManageCancel(PsnInvestmentManageCancelParam params) {
        return BIIClient.instance.post("PsnInvestmentManageCancel", params, PsnInvestmentManageCancelResponse.class);
    }

    /**
     * 开通投资服务
     *
     * @param params
     * @author wangtong
     */
    public Observable<PsnInvestmentManageOpenResult> psnInvestmentManageOpen(PsnInvestmentManageOpenParam params) {
        return BIIClient.instance.post("PsnInvestmentManageOpen", params, PsnInvestmentManageOpenResponse.class);
    }

    /**
     * 开通投资服务确认
     *
     * @param params
     * @author wangtong
     */
    public Observable<PsnInvestmentManageOpenConfirmResult> psnInvestmentManageOpenConfirm(PsnInvestmentManageOpenConfirmParam params) {
        return BIIClient.instance.post("PsnInvestmentManageOpenConfirm", params, PsnInvestmentManageOpenConfirmResponse.class);
    }

    /**
     * 4.34 034累进产品收益率查询PsnXpadProgressQuery
     *
     * @param params
     * @return
     */
    public Observable<PsnXpadProgressQueryResult> PsnXpadProgressQuery(PsnXpadProgressQueryParams params) {
        return BIIClient.instance.post("PsnXpadProgressQuery", params, PsnXpadProgressQueryResponse.class);
    }
    /**
     * 4.53 053 PsnXpadProgressQueryOutlay 登录前累进产品收益率查询
     *
     * @param params
     * @return
     */
    public Observable<PsnXpadProgressQueryOutlayResult> PsnXpadProgressQueryOutlay(PsnXpadProgressQueryOutlayParams params) {
        return BIIClient.instance.post(BIIClientConfig.getBPMSUrl(),"PsnXpadProgressQueryOutlay", params, PsnXpadProgressQueryOutlayResponse.class);
    }
    /**
     * 收益试算
     *
     * @param params
     * @return
     */
    public Observable<PsnXpadProfitCountResult> psnXpadProfitCount(PsnXpadProfitCountParams params) {
        return BIIClient.instance.post("PsnXpadProfitCount", params, PsnXpadProfitCountResponse.class);
    }

    /**
     * 历史净值查询
     *
     * @param params
     * @return
     */
    public Observable<PsnXpadNetHistoryQueryResult> psnXpadNetHistoryQuery(PsnXpadNetHistoryQueryParams params) {
        return BIIClient.instance.post("PsnXpadNetHistoryQuery", params, PsnXpadNetHistoryQueryResponse.class);
    }

    /**
     * 历史净值查询（登录前）
     *
     * @param params
     * @return
     */
    public Observable<PsnXpadNetHistoryQueryOutlayResult> psnXpadNetHistoryQueryOutlay(PsnXpadNetHistoryQueryOutlayParams params) {
        return BIIClient.instance.post("PsnXpadNetHistoryQueryOutlay", params, PsnXpadNetHistoryQueryOutlayResponse.class);
    }

    /**
     * 产品投资协议查询
     *
     * @param params
     * @return
     */
    public Observable<PsnXpadProductInvestTreatyQueryResult> psnXpadProductInvestTreatyQuery(PsnXpadProductInvestTreatyQueryParams params) {
        return BIIClient.instance.post("PsnXpadProductInvestTreatyQuery", params, PsnXpadProductInvestTreatyQueryResponse.class);
    }

    /**
     * 产品投资协议查询
     *
     * @param params
     * @return
     */
    public Observable<PsnXpadApplyAgreementResult> psnXpadApplyAgreementResult(PsnXpadApplyAgreementResultParam params) {
        return BIIClient.instance.post("PsnXpadApplyAgreementResult", params, PsnXpadApplyAgreementResultResponse.class);
    }

    /**
     * 智能协议申请预交易
     *
     * @param params
     * @return
     */
    public Observable<PsnXpadAptitudeTreatyApplyVerifyResult> psnXpadAptitudeTreatyApplyVerify(PsnXpadAptitudeTreatyApplyVerifyParam params) {
        return BIIClient.instance.post("PsnXpadAptitudeTreatyApplyVerify", params, PsnXpadAptitudeTreatyApplyVerifyResponse.class);
    }

    /**
     * 智能协议申请提交交易
     *
     * @param params
     * @return
     */
    public Observable<PsnXpadAptitudeTreatyApplyCommitResult> psnXpadAptitudeTreatyApplyCommit(PsnXpadAptitudeTreatyApplyCommitParams params) {
        return BIIClient.instance.post("PsnXpadAptitudeTreatyApplyCommit", params, PsnXpadAptitudeTreatyApplyCommitResponse.class);
    }

    /**
     * 智能协议详情查询
     *
     * @param params
     * @return
     */
    public Observable<PsnXpadAptitudeTreatyDetailQueryResult> psnXpadAptitudeTreatyDetailQuery(PsnXpadAptitudeTreatyDetailQueryParam params) {
        return BIIClient.instance.post("PsnXpadAptitudeTreatyDetailQuery", params, PsnXpadAptitudeTreatyDetailQueryResponse.class);
    }

    /**
     * 周期性产品续约协议签约/签约初始化
     *
     * @param params
     * @return
     */
    public Observable<PsnXpadSignInitResult> psnXpadSignInit(PsnXpadSignInitParam params) {
        return BIIClient.instance.post("PsnXpadSignInit", params, PsnXpadSignInitResponse.class);
    }

    /**
     * 周期性产品续约协议签约/签约结束
     *
     * @param params
     * @return
     */
    public Observable<PsnXpadSignResult> psnXpadSignResult(PsnXpadSignResultParam params) {
        return BIIClient.instance.post("PsnXpadSignResult", params, PsnXpadSignResultResponse.class);
    }

    /**
     * 4.72 072业绩基准产品预计年收益率查询 PsnXpadExpectYieldQuery
     *
     * @param params
     * @return
     */
    public Observable<PsnXpadExpectYieldQueryResult> PsnXpadExpectYieldQuery(PsnXpadExpectYieldQueryParams params) {
        return BIIClient.instance.post("PsnXpadExpectYieldQuery", params, PsnXpadExpectYieldQueryResponse.class);
    }

    /**
     * 4.73 073登录前业绩基准产品预计年收益率查询 PsnXpadExpectYieldQueryOutlay
     *
     * @param params
     * @return
     */
    public Observable<PsnXpadExpectYieldQueryOutlayResult> PsnXpadExpectYieldQueryOutlay(PsnXpadExpectYieldQueryOutlayParams params) {
        return BIIClient.instance.post(BIIClientConfig.getBPMSUrl(),"PsnXpadExpectYieldQueryOutlay", params, PsnXpadExpectYieldQueryOutlayResponse.class);
    }

}
