package com.boc.bocsoft.mobile.bii.bus.loan.service;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnCityDistrictCodeQuery.PsnCityDistrictCodeQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnCityDistrictCodeQuery.PsnCityDistrictCodeQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnCityDistrictCodeQuery.PsnCityDistrictCodeQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnCommonQuerySystemDateTime.PsnQuerySystemDateTimeParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnCommonQuerySystemDateTime.PsnQuerySystemDateTimeResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnCommonQuerySystemDateTime.PsnQuerySystemDateTimeResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnCreditContractQuery.PsnCreditContractQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnCreditContractQuery.PsnCreditContractQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnCreditContractQuery.PsnCreditContractQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANBatchRepaySubmit.PsnELOANBatchRepaySubmitParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANBatchRepaySubmit.PsnELOANBatchRepaySubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANBatchRepaySubmit.PsnELOANBatchRepaySubmitResultBean;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANCycleLoanAccountListQuery.PsnCycleLoanAccountEQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANCycleLoanAccountListQuery.PsnCycleLoanAccountEQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANCycleLoanAccountListQuery.PsnCycleLoanAccountEQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANHistoryQuery.PsnEHistoryQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANHistoryQuery.PsnEHistoryQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANHistoryQuery.PsnEHistoryQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANOverdueQuery.PsnEOverdueQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANOverdueQuery.PsnEOverdueQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANOverdueQuery.PsnEOverdueQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANQuoteDetailQuery.PsnEQuoteDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANQuoteDetailQuery.PsnEQuoteDetailQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANQuoteDetailQuery.PsnEQuoteDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANRemainQuery.PsnERemainQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANRemainQuery.PsnERemainQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANRemainQuery.PsnERemainQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANRepayCount.PsnELOANRepayCountParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANRepayCount.PsnELOANRepayCountResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANRepayCount.PsnELOANRepayCountResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANSingleRepaySubmit.PsnELOANSingleRepaySubmitParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANSingleRepaySubmit.PsnELOANSingleRepaySubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANSingleRepaySubmit.PsnELOANSingleRepaySubmitResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELoanCredit.PsnLoanCreditParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELoanCredit.PsnLoanCreditResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELoanCredit.PsnLoanCreditResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAccountListAndDetailQuery.PsnLOANAccountListAndDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAccountListAndDetailQuery.PsnLOANAccountListAndDetailQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAccountListAndDetailQuery.PsnLOANAccountListAndDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepayAccountDetailQuery.PsnDrawingDetailParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepayAccountDetailQuery.PsnDrawingDetailResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepayAccountDetailQuery.PsnDrawingDetailResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepaySubmit.PsnLOANAdvanceRepaySubmitParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepaySubmit.PsnLOANAdvanceRepaySubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepaySubmit.PsnLOANAdvanceRepaySubmitResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepayVerify.PsnLOANAdvanceRepayVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepayVerify.PsnLOANAdvanceRepayVerifyResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepayVerify.PsnLOANAdvanceRepayVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeLoanERepayAccountSubmit.PsnLOANChangeLoanERepayAccountSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeLoanERepayAccountSubmit.PsnLOANChangeLoanERepayAccountSubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeLoanERepayAccountSubmit.PsnLOANChangeLoanERepayAccountSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeLoanERepayAccountVerify.PsnLOANChangeLoanERepayAccountVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeLoanERepayAccountVerify.PsnLOANChangeLoanERepayAccountVerifyResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeLoanERepayAccountVerify.PsnLOANChangeLoanERepayAccountVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeLoanRepayAccountSubmit.PsnLOANChangeLoanRepayAccountSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeLoanRepayAccountSubmit.PsnLOANChangeLoanRepayAccountSubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeLoanRepayAccountSubmit.PsnLOANChangeLoanRepayAccountSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeLoanRepayAccountVerify.PsnLOANChangeLoanRepayAccountVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeLoanRepayAccountVerify.PsnLOANChangeLoanRepayAccountVerifyResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeLoanRepayAccountVerify.PsnLOANChangeLoanRepayAccountVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeSignedSubmit.PsnLOANChangeSignedSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeSignedSubmit.PsnLOANChangeSignedSubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeSignedVerify.PsnLOANChangeSignedVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeSignedVerify.PsnLOANChangeSignedVerifyResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANChangeSignedVerify.PsnLOANChangeSignedVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanApplySubmit.PsnLOANCycleLoanApplySubmitParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanApplySubmit.PsnLOANCycleLoanApplySubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanApplySubmit.PsnLOANCycleLoanApplySubmitResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanApplyVerify.PsnLOANCycleLoanApplyVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanApplyVerify.PsnLOANCycleLoanApplyVerifyResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanApplyVerify.PsnLOANCycleLoanApplyVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanEApplySubmit.PsnLOANCycleLoanEApplySubmitParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanEApplySubmit.PsnLOANCycleLoanEApplySubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanEApplySubmit.PsnLOANCycleLoanEApplySubmitResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanEApplyVerify.PsnLOANCycleLoanEApplyVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanEApplyVerify.PsnLOANCycleLoanEApplyVerifyResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanEApplyVerify.PsnLOANCycleLoanEApplyVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanMinAmountQuery.PsnLOANCycleLoanMinAmountQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanMinAmountQuery.PsnLOANCycleLoanMinAmountQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANHistoryQuery.PsnLOANHistoryQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANHistoryQuery.PsnLOANHistoryQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANHistoryQuery.PsnLOANHistoryQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANMultipleQuery.PsnLOANMultipleQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANMultipleQuery.PsnLOANMultipleQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANMultipleQuery.PsnLOANMultipleQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANOverdueQuery.PsnLOANOverdueQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANOverdueQuery.PsnLOANOverdueQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANOverdueQuery.PsnLOANOverdueQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANOverdueStatusQuery.PsnLOANOverdueStatusQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANOverdueStatusQuery.PsnLOANOverdueStatusQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANOverdueStatusQuery.PsnLOANOverdueStatusQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayeeAcountCheck.PsnLOANPayeeAcountCheckParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayeeAcountCheck.PsnLOANPayeeAcountCheckResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayeeAcountCheck.PsnLOANPayeeAcountCheckResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayerAcountCheck.PsnLOANPayerAcountCheckParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayerAcountCheck.PsnLOANPayerAcountCheckResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayerAcountCheck.PsnLOANPayerAcountCheckResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPaymentInfoQuery.PsnLOANPaymentInfoQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPaymentInfoQuery.PsnLOANPaymentInfoQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPaymentInfoQuery.PsnLOANPaymentInfoQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPledgeSubmit.PsnLOANPledgeSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPledgeSubmit.PsnLOANPledgeSubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPledgeSubmit.PsnLOANPledgeSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPledgeVerify.PsnLOANPledgeVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPledgeVerify.PsnLOANPledgeVerifyResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPledgeVerify.PsnLOANPledgeVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANQuotaQuery.PsnLOANQuotaQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANQuotaQuery.PsnLOANQuotaQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANQuotaQuery.PsnLOANQuotaQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANRemainQuery.PsnLOANRemainQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANRemainQuery.PsnLOANRemainQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANRemainQuery.PsnLOANRemainQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANSignAccountCheck.PsnLOANSignAccountCheckParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANSignAccountCheck.PsnLOANSignAccountCheckResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANSignAccountCheck.PsnLOANSignAccountCheckResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANUseRecordsQuery.PsnLOANUseRecordsQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANUseRecordsQuery.PsnLOANUseRecordsQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANUseRecordsQuery.PsnLOANUseRecordsQueryResultBean;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanContractQuery.PsnLoanContractQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanContractQuery.PsnLoanContractQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanContractQuery.PsnLoanContractQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentSignContractQuery.PsnLoanPaymentSignContractQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentSignContractQuery.PsnLoanPaymentSignContractQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentSignContractQuery.PsnLoanPaymentSignContractQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentSignSubmit.PsnLoanPaymentSignSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentSignSubmit.PsnLoanPaymentSignSubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentSignVerify.PsnLoanPaymentSignVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentSignVerify.PsnLoanPaymentSignVerifyResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentSignVerify.PsnLoanPaymentSignVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentUnsignSubmit.PsnLoanPaymentUnsignSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentUnsignSubmit.PsnLoanPaymentUnsignSubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentUnsignVerify.PsnLoanPaymentUnsignVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentUnsignVerify.PsnLoanPaymentUnsignVerifyResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentUnsignVerify.PsnLoanPaymentUnsignVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPledgeAvaAccountQuery.PsnLoanPledgeAvaAccountQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPledgeAvaAccountQuery.PsnLoanPledgeAvaAccountQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPledgeAvaAccountQuery.PsnLoanPledgeAvaAccountQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanRateQuery.PsnLoanRateQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanRateQuery.PsnLoanRateQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanRegisterSumbitInterfaces.PsnLoanRegisterSumbitInterfacesParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanRegisterSumbitInterfaces.PsnLoanRegisterSumbitInterfacesResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanRegisterSumbitInterfaces.PsnLoanRegisterSumbitInterfacesResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanRegisterVerifyInterfaces.PsnLoanRegisterVerifyInterfacesParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanRegisterVerifyInterfaces.PsnLoanRegisterVerifyInterfacesResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanRegisterVerifyInterfaces.PsnLoanRegisterVerifyInterfacesResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanRepaymentPlan.PsnLoanRepaymentPlanParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanRepaymentPlan.PsnLoanRepaymentPlanResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanRepaymentPlan.PsnLoanRepaymentPlanResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeParameterQry.PsnLoanXpadPledgeParameterQryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeParameterQry.PsnLoanXpadPledgeParameterQryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeParameterQry.PsnLoanXpadPledgeParameterQryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeProductQry.PsnLoanXpadPledgeProductQryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeProductQry.PsnLoanXpadPledgeProductQryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeProductQry.PsnLoanXpadPledgeProductQryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeQry.PsnLoanXpadPledgeQryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeQry.PsnLoanXpadPledgeQryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeQry.PsnLoanXpadPledgeQryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeRateQry.PsnLoanXpadPledgeRateQryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeRateQry.PsnLoanXpadPledgeRateQryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeSubmit.PsnLoanXpadPledgeSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeSubmit.PsnLoanXpadPledgeSubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeSubmit.PsnLoanXpadPledgeSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeVerify.PsnLoanXpadPledgeVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeVerify.PsnLoanXpadPledgeVerifyResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeVerify.PsnLoanXpadPledgeVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanAppliedQry.PsnOnLineLoanAppliedQryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanAppliedQry.PsnOnLineLoanAppliedQryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanAppliedQry.PsnOnLineLoanAppliedQryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanBranchQry.PsnOnLineLoanBranchQryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanBranchQry.PsnOnLineLoanBranchQryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanBranchQry.PsnOnLineLoanBranchQryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanCityQry.PsnOnLineLoanCityQryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanCityQry.PsnOnLineLoanCityQryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanCityQry.PsnOnLineLoanCityQryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanDetailQry.PsnOnLineLoanDetailQryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanDetailQry.PsnOnLineLoanDetailQryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanDetailQry.PsnOnLineLoanDetailQryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanFieldQry.PsnOnLineLoanFieldQryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanFieldQry.PsnOnLineLoanFieldQryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanFieldQry.PsnOnLineLoanFieldQryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanProductQry.PsnOnLineLoanProductParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanProductQry.PsnOnLineLoanProductResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanProductQry.PsnOnLineLoanProductResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanProvinceQry.PsnOnLineLoanProvinceQryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanProvinceQry.PsnOnLineLoanProvinceQryResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanProvinceQry.PsnOnLineLoanProvinceQryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanSubmit.PsnOnLineLoanSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanSubmit.PsnOnLineLoanSubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanSubmit.PsnOnLineLoanSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnQueryCardNumByAcctNum.PsnQueryCardNumByAcctNumParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnQueryCardNumByAcctNum.PsnQueryCardNumByAcctNumResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnQueryPersonalTimeAccount.PsnQueryPersonalTimeAccountParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnQueryPersonalTimeAccount.PsnQueryPersonalTimeAccountResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnQueryPersonalTimeAccount.PsnQueryPersonalTimeAccountResult;
import com.boc.bocsoft.mobile.bii.common.client.BIIClient;
import java.util.List;
import rx.Observable;

/**
 * Created by xintong on 2016/6/13.
 */
public class PsnLoanService {
    ////申請

    /**
     * PsnCityDistrictCodeQuery省市区联动查询
     */
    public Observable<List<PsnCityDistrictCodeQueryResult>> psnCityDistrictCodeQuery(
            PsnCityDistrictCodeQueryParams params) {
        return BIIClient.instance.post("PsnCityDistrictCodeQuery", params,
                PsnCityDistrictCodeQueryResponse.class);
    }

    /**
     * PsnCreditContractQuery查询征信授权协议模板
     */
    public Observable<PsnCreditContractQueryResult> psnCreditContractQuery(
            PsnCreditContractQueryParams params) {
        return BIIClient.instance.post("PsnCreditContractQuery", params,
                PsnCreditContractQueryResponse.class);
    }

    /**
     * PsnLoanContractQuery查询贷款合同模板
     */
    public Observable<PsnLoanContractQueryResult> psnLoanContractQuery(
            PsnLoanContractQueryParams params) {
        return BIIClient.instance.post("PsnLoanContractQuery", params,
                PsnLoanContractQueryResponse.class);
    }

    /**
     * PsnLoanRegisterVerifyInterfaces额度签约申请预交易
     */
    public Observable<PsnLoanRegisterVerifyInterfacesResult> psnLoanRegisterVerifyInterfaces(
            PsnLoanRegisterVerifyInterfacesParams params) {
        return BIIClient.instance.post("PsnLoanRegisterVerifyInterfaces", params,
                PsnLoanRegisterVerifyInterfacesResponse.class);
    }

    /**
     * PsnLoanRegisterSumbitInterfaces额度签约申请提交交易
     */
    public Observable<PsnLoanRegisterSumbitInterfacesResult> psnLoanRegisterSumbitInterfaces(
            PsnLoanRegisterSumbitInterfacesParams params) {
        return BIIClient.instance.post("PsnLoanRegisterSumbitInterfaces", params,
                PsnLoanRegisterSumbitInterfacesResponse.class);
    }

    ////提款

    /**
     * 贷款管理-中银E贷-提款-还款计划试算,PsnLoanRepaymentPlan还款计划试算
     */
    public Observable<PsnLoanRepaymentPlanResult> psnLoanRepaymentPlan(
            PsnLoanRepaymentPlanParams params) {
        return BIIClient.instance.post("PsnLoanRepaymentPlan", params,
                PsnLoanRepaymentPlanResponse.class);
    }

    /**
     * 贷款管理-中银E贷-提款-中银E贷用款预交易,PsnLOANCycleLoanEApplyVerify中银E贷用款预交易
     */
    public Observable<PsnLOANCycleLoanEApplyVerifyResult> psnLOANCycleLoanEApplyVerify(
            PsnLOANCycleLoanEApplyVerifyParams params) {
        return BIIClient.instance.post("PsnLOANCycleLoanEApplyVerify", params,
                PsnLOANCycleLoanEApplyVerifyResponse.class);
    }

    /**
     * 贷款管理-中银E贷-提款-中银E贷用款提交交易,PsnLOANCycleLoanEApplySubmit 中银E贷用款提交交易
     */
    public Observable<PsnLOANCycleLoanEApplySubmitResult> psnLOANCycleLoanEApplySubmit(
            PsnLOANCycleLoanEApplySubmitParams params) {
        return BIIClient.instance.post("PsnLOANCycleLoanEApplySubmit", params,
                PsnLOANCycleLoanEApplySubmitResponse.class);
    }

    ////提前还款

    /**
     * 贷款管理-中银E贷-提前还款预交易,PsnLOANAdvanceRepayVerify提前还款预交易
     */
    public Observable<PsnLOANAdvanceRepayVerifyResult> psnLOANAdvanceRepayVerify(
            PsnLOANAdvanceRepayVerifyParams params) {
        return BIIClient.instance.post("PsnLOANAdvanceRepayVerify", params,
                PsnLOANAdvanceRepayVerifyResponse.class);
    }

    /**
     * 贷款管理-中银E贷-提前还款提交交易,PsnLOANAdvanceRepaySubmit提前还款提交交易
     */
    public Observable<PsnLOANAdvanceRepaySubmitResult> psnLOANAdvanceRepaySubmit(
            PsnLOANAdvanceRepaySubmitParams params) {
        return BIIClient.instance.post("PsnLOANAdvanceRepaySubmit", params,
                PsnLOANAdvanceRepaySubmitResponse.class);
    }

    ////中银E贷变更还款账户

    /**
     * PsnLOANPayeeAcountCheck收款账户检查
     */
    public Observable<PsnLOANPayeeAcountCheckResult> psnLOANPayeeAcountCheck(
            PsnLOANPayeeAcountCheckParams params) {
        return BIIClient.instance.post("PsnLOANPayeeAcountCheck", params,
                PsnLOANPayeeAcountCheckResponse.class);
    }

    /**
     * PsnLOANPayerAcountCheck还款账户检查
     */
    public Observable<PsnLOANPayerAcountCheckResult> psnLOANPayerAcountCheck(
            PsnLOANPayerAcountCheckParams params) {
        return BIIClient.instance.post("PsnLOANPayerAcountCheck", params,
                PsnLOANPayerAcountCheckResponse.class);
    }

    /**
     * 贷款管理-中银E贷-变更中E贷还款账户预交易,PsnLOANChangeLoanERepayAccountVerify变更中E贷还款账户预交易
     */
    public Observable<PsnLOANChangeLoanERepayAccountVerifyResult> psnLOANChangeLoanERepayAccountVerify(
            PsnLOANChangeLoanERepayAccountVerifyParams params) {
        return BIIClient.instance.post("PsnLOANChangeLoanERepayAccountVerify", params,
                PsnLOANChangeLoanERepayAccountVerifyResponse.class);
    }

    /**
     * 贷款管理-中银E贷-变变更中E贷还款账户提交交易,PsnLOANChangeLoanERepayAccountSubmit变变更中E贷还款账户提交交易
     */
    public Observable<PsnLOANChangeLoanERepayAccountSubmitResult> psnLOANChangeLoanERepayAccountSubmit(
            PsnLOANChangeLoanERepayAccountSubmitParams params) {
        return BIIClient.instance.post("PsnLOANChangeLoanERepayAccountSubmit", params,
                PsnLOANChangeLoanERepayAccountSubmitResponse.class);
    }

    /**
     * 贷款管理-循环非循环-变更还款账户预交易,PsnLOANChangeLoanRepayAccountVerify变更还款账户预交易
     */
    public Observable<PsnLOANChangeLoanERepayAccountVerifyResult> psnLOANChangeLoanRepayAccountVerify(
            PsnLOANChangeLoanERepayAccountVerifyParams params) {
        return BIIClient.instance.post("PsnLOANChangeLoanRepayAccountVerify", params,
                PsnLOANChangeLoanERepayAccountVerifyResponse.class);
    }

    /**
     * 贷款管理-循环非循环-变更还款账户提交交易,PsnLOANChangeLoanRepayAccountSubmit变更还款账户提交交易
     */
    public Observable<PsnLOANChangeLoanERepayAccountSubmitResult> psnLOANChangeLoanRepayAccountSubmit(
            PsnLOANChangeLoanERepayAccountSubmitParams params) {
        return BIIClient.instance.post("PsnLOANChangeLoanRepayAccountSubmit", params,
                PsnLOANChangeLoanERepayAccountSubmitResponse.class);
    }

    ////quary

    /**
     * 查询客户签约中银E贷额度列表
     *
     * @return 返回值
     */
    public Observable<List<PsnEQuoteDetailQueryResult>> psnEQuoteDetailQueryList(
            PsnEQuoteDetailQueryParams params) {
        return BIIClient.instance.post("PsnLOANQuoteDetailEQuery", params,
                PsnEQuoteDetailQueryResponse.class);
    }

    /**
     * 查询个人循环贷款列表
     *
     * @return 返回值
     */
    public Observable<List<PsnCycleLoanAccountEQueryResult>> psnCycleLoanAccountEQueryList(
            PsnCycleLoanAccountEQueryParams params) {
        return BIIClient.instance.post("PsnLOANCycleLoanAccountListQuery", params,
                PsnCycleLoanAccountEQueryResponse.class);
    }

    /**
     * 查询授信额度
     *
     * @return 返回值
     */
    public Observable<PsnLoanCreditResult> psnLoanCredit(PsnLoanCreditParams params) {
        return BIIClient.instance.post("PsnLoanApplyInterfaces", params,
                PsnLoanCreditResponse.class);
    }

    /**
     * 分条件查询贷款账户列表
     *
     * @return 返回值
     */
    public Observable<PsnLOANListEQueryResult> psnLOANListEQueryList(
            PsnLOANListEQueryParams params) {
        return BIIClient.instance.post("PsnLOANListEQuery", params,
                PsnLOANListEQueryResponse.class);
    }

    /**
     * 用款记录查询
     *
     * @return 返回值
     */
    public Observable<List<PsnLOANUseRecordsQueryResultBean>> psnLOANUseRecordsQueryList(
            PsnLOANUseRecordsQueryParams params) {
        return BIIClient.instance.post("PsnLOANUseRecordsQuery", params,
                PsnLOANUseRecordsQueryResponse.class);
    }

    /**
     * 用款详情查询
     *
     * @return 返回值
     */
    public Observable<PsnDrawingDetailResult> psnDrawingDetail(PsnDrawingDetailParams params) {
        return BIIClient.instance.post("PsnLOANAdvanceRepayAccountDetailQuery", params,
                PsnDrawingDetailResponse.class);
    }

    /**
     * 查询历史还款信息
     *
     * @return 返回值
     */
    public Observable<PsnEHistoryQueryResult> psnEHistoryQueryList(PsnEHistoryQueryParams params) {
        return BIIClient.instance.post("PsnLOANHistoryQuery", params,
                PsnEHistoryQueryResponse.class);
    }

    /**
     * 查询逾期还款信息
     *
     * @return 返回值
     */
    public Observable<PsnEOverdueQueryResult> psnEOverdueQueryList(PsnEOverdueQueryParams params) {
        return BIIClient.instance.post("PsnLOANOverdueQuery", params,
                PsnEOverdueQueryResponse.class);
    }

    /**
     * 查询剩余还款信息
     *
     * @return 返回值
     */
    public Observable<PsnERemainQueryResult> psnERemainQueryList(PsnERemainQueryParams params) {
        return BIIClient.instance.post("PsnLOANRemainQuery", params, PsnERemainQueryResponse.class);
    }

    /**
     * 获取后台服务时间
     *
     * @return 返回值
     */
    public Observable<PsnQuerySystemDateTimeResult> psnQuerySystemDateTime(
            PsnQuerySystemDateTimeParams params) {
        return BIIClient.instance.post("PsnCommonQuerySystemDateTime", params,
                PsnQuerySystemDateTimeResponse.class);
    }

    public Observable<List<PsnLOANQuotaQueryResult>> psnLOANQuotaQuery(
            PsnLOANQuotaQueryParams params) {
        return BIIClient.instance.post("PsnLOANQuotaQuery", params,
                PsnLOANQuotaQueryResponse.class);
    }

    public Observable<PsnLOANAccountListAndDetailQueryResult> psnLOANAccountListAndDetailQuery(
            PsnLOANAccountListAndDetailQueryParams params) {
        return BIIClient.instance.post("PsnLOANAccountListAndDetailQuery", params,
                PsnLOANAccountListAndDetailQueryResponse.class);
    }

    /**
     * 查询支持贷款的省份列表
     *
     * @param params 参数
     * @return 支持贷款的省份列表
     */
    public Observable<PsnOnLineLoanProvinceQryResult> psnOnLineLoanProvinceQry(
            PsnOnLineLoanProvinceQryParams params) {
        return BIIClient.instance.post("PsnOnLineLoanProvinceQry", params,
                PsnOnLineLoanProvinceQryResponse.class);
    }

    /**
     * 查询支持贷款的城市列表
     *
     * @param params 参数
     * @return 支持贷款的城市列表
     */
    public Observable<PsnOnLineLoanCityQryResult> psnOnLineLoanCityQry(
            PsnOnLineLoanCityQryParams params) {
        return BIIClient.instance.post("PsnOnLineLoanCityQry", params,
                PsnOnLineLoanCityQryResponse.class);
    }

    /**
     * 查询贷款产品列表
     *
     * @param params 参数
     * @return 贷款产品列表
     */
    public Observable<PsnOnLineLoanProductResult> psnOnLineLoanProductQry(
            PsnOnLineLoanProductParams params) {
        return BIIClient.instance.post("PsnOnLineLoanProductQry", params,
                PsnOnLineLoanProductResponse.class);
    }

    /**
     * 查询贷款产品申请栏位
     *
     * @param params 参数
     * @return 款产品申请栏位
     */
    public Observable<PsnOnLineLoanFieldQryResult> psnOnLineLoanFieldQry(
            PsnOnLineLoanFieldQryParams params) {
        return BIIClient.instance.post("PsnOnLineLoanFieldQry", params,
                PsnOnLineLoanFieldQryResponse.class);
    }

    /**
     * 查询网点列表
     *
     * @param params 网点查询参数
     */
    public Observable<PsnOnLineLoanBranchQryResult> psnOnLineLoanBranchQry(
            PsnOnLineLoanBranchQryParams params) {
        return BIIClient.instance.post("PsnOnLineLoanBranchQry", params,
                PsnOnLineLoanBranchQryResponse.class);
    }

    /**
     * 贷款申请提交交易
     */
    public Observable<PsnOnLineLoanSubmitResult> psnOnLineLoanSubmit(
            PsnOnLineLoanSubmitParams params) {
        return BIIClient.instance.post("PsnOnLineLoanSubmit", params,
                PsnOnLineLoanSubmitResponse.class);
    }

    /**
     * 查询账户详情
     *
     * @param params 参数
     * @return 账户详情数据
     */
    public Observable<PsnAccountQueryAccountDetailResult> psnAccountQueryAccountDetail(
            PsnAccountQueryAccountDetailParams params) {
        return BIIClient.instance.post("PsnAccountQueryAccountDetail", params,
                PsnAccountQueryAccountDetailResponse.class);
    }

    /**
     * 查询理财质押贷款相关参数
     */
    public Observable<PsnLoanXpadPledgeParameterQryResult> psnLoanXpadPledgeParameterQry(
            PsnLoanXpadPledgeParameterQryParams params) {
        return BIIClient.instance.post("PsnLoanXpadPledgeParameterQry", params,
                PsnLoanXpadPledgeParameterQryResponse.class);
    }

    /**
     * 查询可做理财质押贷款的理财产品列表
     */
    public Observable<List<PsnLoanXpadPledgeProductQryResult>> psnLoanXpadPledgeProductQry(
            PsnLoanXpadPledgeProductQryParams params) {
        return BIIClient.instance.post("PsnLoanXpadPledgeProductQry", params,
                PsnLoanXpadPledgeProductQryResponse.class);
    }

    /**
     * 理财质押贷款申请结果查询
     */
    public Observable<PsnLoanXpadPledgeQryResult> psnLoanXpadPledgeQry(
            PsnLoanXpadPledgeQryParams params) {
        return BIIClient.instance.post("PsnLoanXpadPledgeQry", params,
                PsnLoanXpadPledgeQryResponse.class);
    }

    /**
     * 理财质押贷款预交易
     */
    public Observable<PsnLoanXpadPledgeVerifyResult> psnLoanXpadPledgeVerify(
            PsnLoanXpadPledgeVerifyParams params) {
        return BIIClient.instance.post("PsnLoanXpadPledgeVerify", params,
                PsnLoanXpadPledgeVerifyResponse.class);
    }

    /**
     * 理财质押贷款提交交易
     */
    public Observable<PsnLoanXpadPledgeSubmitResult> psnLoanXpadPledgeSubmit(
            PsnLoanXpadPledgeSubmitParams params) {
        return BIIClient.instance.post("PsnLoanXpadPledgeSubmit", params,
                PsnLoanXpadPledgeSubmitResponse.class);
    }

    /**
     * 可执行存款质押贷款的定一本账户查询
     */
    public Observable<List<PsnLoanPledgeAvaAccountQueryResult>> psnLoanPledgeAvaAccountQuery(
            PsnLoanPledgeAvaAccountQueryParams params) {
        return BIIClient.instance.post("PsnLoanPledgeAvaAccountQuery", params,
                PsnLoanPledgeAvaAccountQueryResponse.class);
    }

    /**
     * 询定一本下存单信息
     */
    public Observable<List<PsnQueryPersonalTimeAccountResult>> psnQueryPersonalTimeAccount(
            PsnQueryPersonalTimeAccountParams params) {
        return BIIClient.instance.post("PsnQueryPersonalTimeAccount", params,
                PsnQueryPersonalTimeAccountResponse.class);
    }

    /**
     * 综合查询（查询汇率、质押率、贷款期限上下限、单笔限额上下限）
     */
    public Observable<PsnLOANMultipleQueryResult> psnLOANMultipleQuery(
            PsnLOANMultipleQueryParams params) {
        return BIIClient.instance.post("PsnLOANMultipleQuery", params,
                PsnLOANMultipleQueryResponse.class);
    }

    /**
     * 贷款利率查询
     */
    public Observable<String> psnLoanRateQuery(PsnLoanRateQueryParams params) {
        return BIIClient.instance.post("PsnLoanRateQuery", params, PsnLoanRateQueryResponse.class);
    }

    /**
     * 理财质押贷款利率查询
     */
    public Observable<String> psnLoanXpadPledgeRateQry(PsnLoanXpadPledgeRateQryParams params) {
        return BIIClient.instance.post("PsnLoanXpadPledgeRateQry", params,
                PsnLoanXpadPledgeRateQryResponse.class);
    }

    /**
     * 存款质押贷款预交易
     */
    public Observable<PsnLOANPledgeVerifyResult> psnLOANPledgeVerify(
            PsnLOANPledgeVerifyParams params) {
        return BIIClient.instance.post("PsnLOANPledgeVerify", params,
                PsnLOANPledgeVerifyResponse.class);
    }

    /**
     * 存款质押贷款预交易
     */
    public Observable<PsnLOANPledgeSubmitResult> psnLOANPledgeSubmit(
            PsnLOANPledgeSubmitParams params) {
        return BIIClient.instance.post("PsnLOANPledgeSubmit", params,
                PsnLOANPledgeSubmitResponse.class);
    }

    /**
     * 其他类型贷款 还款计划 剩余还款查询
     */
    public Observable<PsnLOANRemainQueryResult> psnRepayRemainReturnInfo(
            PsnLOANRemainQueryParams params) {
        return BIIClient.instance.post("PsnLOANRemainQuery", params,
                PsnLOANRemainQueryResponse.class);
    }

    /**
     * 其他类型贷款 还款计划 历史还款查询
     */
    public Observable<PsnLOANHistoryQueryResult> psnRepayHistoryReturnInfo(
            PsnLOANHistoryQueryParams params) {
        return BIIClient.instance.post("PsnLOANHistoryQuery", params,
                PsnLOANHistoryQueryResponse.class);
    }

    /**
     * 其他类型贷款 还款计划 逾期还款查询
     */
    public Observable<PsnLOANOverdueQueryResult> psnRepayOverdueReturnInfo(
            PsnLOANOverdueQueryParams params) {
        return BIIClient.instance.post("PsnLOANOverdueQuery", params,
                PsnLOANOverdueQueryResponse.class);
    }

    /**
     * 查询其他类型贷款申请进度
     */
    public Observable<PsnOnLineLoanAppliedQryResult> psnOnLineLoanAppliedQry(
            PsnOnLineLoanAppliedQryParams params) {
        return BIIClient.instance.post("PsnOnLineLoanAppliedQry", params,
                PsnOnLineLoanAppliedQryResponse.class);
    }

    /**
     * 查询其他类型贷款申请--单笔贷款记录详情查询
     */
    public Observable<PsnOnLineLoanDetailQryResult> psnOnLineLoanDetailQry(
            PsnOnLineLoanDetailQryParams params) {
        return BIIClient.instance.post("PsnOnLineLoanDetailQry", params,
                PsnOnLineLoanDetailQryResponse.class);
    }

    /**
     * 个人循环贷款预交易(非中银E贷)
     */
    public Observable<PsnLOANCycleLoanApplyVerifyResult> psnLOANCycleLoanApplyVerify(
            PsnLOANCycleLoanApplyVerifyParams params) {
        return BIIClient.instance.post("PsnLOANCycleLoanApplyVerify", params,
                PsnLOANCycleLoanApplyVerifyResponse.class);
    }

    /**
     * 个人循环贷款提交交易（非中银E贷）
     */
    public Observable<PsnLOANCycleLoanApplySubmitResult> psnLOANCycleLoanApplySubmit(
            PsnLOANCycleLoanApplySubmitParams params) {
        return BIIClient.instance.post("PsnLOANCycleLoanApplySubmit", params,
                PsnLOANCycleLoanApplySubmitResponse.class);
    }

    /**
     * 变更还款账户预交易（非中银E贷）
     */
    public Observable<PsnLOANChangeLoanRepayAccountVerifyResult> psnLOANChangeLoanRepayAccountVerify(
            PsnLOANChangeLoanRepayAccountVerifyParams params) {
        return BIIClient.instance.post("PsnLOANChangeLoanRepayAccountVerify", params,
                PsnLOANChangeLoanRepayAccountVerifyResponse.class);
    }

    /**
     * 变更还款账户提交交易
     */
    public Observable<PsnLOANChangeLoanRepayAccountSubmitResult> psnLOANChangeLoanRepayAccountSubmit(
            PsnLOANChangeLoanRepayAccountSubmitParams params) {
        return BIIClient.instance.post("PsnLOANChangeLoanRepayAccountSubmit", params,
                PsnLOANChangeLoanRepayAccountSubmitResponse.class);
    }

    /**
     * 查询账户是否支持签约
     */
    public Observable<PsnLOANSignAccountCheckResult> psnLOANSignAccountCheck(
            PsnLOANSignAccountCheckParams params) {
        return BIIClient.instance.post("PsnLOANSignAccountCheck", params,
                PsnLOANSignAccountCheckResponse.class);
    }

    /**
     * 中银E贷，单笔批量提前还款
     */
    public Observable<PsnELOANSingleRepaySubmitResult> psnELOANSingleRepaySubmit(
            PsnELOANSingleRepaySubmitParams params) {
        return BIIClient.instance.post("PsnELOANSingleRepaySubmit", params,
                PsnELOANSingleRepaySubmitResponse.class);
    }

    /**
     * 中银E贷，批量提前还款
     */
    public Observable<List<PsnELOANBatchRepaySubmitResultBean>> psnELOANBatchRepaySubmit(
            PsnELOANBatchRepaySubmitParams params) {
        return BIIClient.instance.post("PsnELOANBatchRepaySubmit", params,
                PsnELOANBatchRepaySubmitResponse.class);
    }

    /**
     * 中银E贷，中银E贷提前还款手续费试算
     */
    public Observable<PsnELOANRepayCountResult> psnELOANRepayCount(
            PsnELOANRepayCountParams params) {
        return BIIClient.instance.post("PsnELOANRepayCount", params,
                PsnELOANRepayCountResponse.class);
    }

    /**
     * 中银E贷，支付签约预交易
     */
    public Observable<PsnLoanPaymentSignVerifyResult> psnLoanPaymentSignVerify(
            PsnLoanPaymentSignVerifyParams params) {
        return BIIClient.instance.post("PsnLoanPaymentSignVerify", params,
                PsnLoanPaymentSignVerifyResponse.class);
    }

    /**
     * 中银E贷，支付签约提交交易
     */
    public Observable<String> psnLoanPaymentSignSubmit(PsnLoanPaymentSignSubmitParams params) {
        return BIIClient.instance.post("PsnLoanPaymentSignSubmit", params,
                PsnLoanPaymentSignSubmitResponse.class);
    }

    /**
     * 中银E贷，支付解约预交易
     */
    public Observable<PsnLoanPaymentUnsignVerifyResult> psnLoanPaymentUnsignVerify(
            PsnLoanPaymentUnsignVerifyParams params) {
        return BIIClient.instance.post("PsnLoanPaymentUnsignVerify", params,
                PsnLoanPaymentUnsignVerifyResponse.class);
    }

    /**
     * 中银E贷，支付解约提交交易
     */
    public Observable<String> psnLoanPaymentUnsignSubmit(PsnLoanPaymentUnsignSubmitParams params) {
        return BIIClient.instance.post("PsnLoanPaymentUnsignSubmit", params,
                PsnLoanPaymentUnsignSubmitResponse.class);
    }

    /**
     * 中银E贷，支付签约修改预交易
     */
    public Observable<PsnLOANChangeSignedVerifyResult> psnLOANChangeSignedVerify(
            PsnLOANChangeSignedVerifyParams params) {
        return BIIClient.instance.post("PsnLOANChangeSignedVerify", params,
                PsnLOANChangeSignedVerifyResponse.class);
    }

    /**
     * 中银E贷，支付签约修改提交交易
     */
    public Observable<String> psnLOANChangeSignedSubmit(PsnLOANChangeSignedSubmitParams params) {
        return BIIClient.instance.post("PsnLOANChangeSignedSubmit", params,
                PsnLOANChangeSignedSubmitResponse.class);
    }

    /**
     * 单贷款账户支付签约信息查询
     */
    public Observable<PsnLOANPaymentInfoQueryResult> psnLOANPaymentInfoQuery(
            PsnLOANPaymentInfoQueryParams params) {
        return BIIClient.instance.post("PsnLOANPaymentInfoQuery", params,
                PsnLOANPaymentInfoQueryResponse.class);
    }

    /**
     * 查询逾期信息
     */
    public Observable<PsnLOANOverdueStatusQueryResult> psnLOANOverdueStatusQuery(
            PsnLOANOverdueStatusQueryParams params) {
        return BIIClient.instance.post("PsnLOANOverdueStatusQuery", params,
                PsnLOANOverdueStatusQueryResponse.class);
    }

    /**
     * 查询支付协议模板
     */
    public Observable<PsnLoanPaymentSignContractQueryResult> psnLoanPaymentSignContractQuery(
            PsnLoanPaymentSignContractQueryParams params) {
        return BIIClient.instance.post("PsnLoanPaymentSignContractQuery", params,
                PsnLoanPaymentSignContractQueryResponse.class);
    }

    /**
     * 查询个人循环贷款最低放款金额
     */
    public Observable<String> psnLOANCycleLoanMinAmountQuery(
            PsnLOANCycleLoanMinAmountQueryParams params) {
        return BIIClient.instance.post("PsnLOANCycleLoanMinAmountQuery", params,
                PsnLOANCycleLoanMinAmountQueryResponse.class);
    }

    /**
     * 通过账户查询借记卡号
     */
    public Observable<String> psnQueryCardNumByAcctNumQuery(PsnQueryCardNumByAcctNumParams params) {
        return BIIClient.instance.post("PsnQueryCardNumByAcctNum", params,
                PsnQueryCardNumByAcctNumResponse.class);
    }
}
