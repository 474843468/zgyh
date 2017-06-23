package com.boc.bocsoft.mobile.bii.bus.transfer.service;

import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnAccountInfoDecipher.PsnAccountInfoDecipherParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnAccountInfoDecipher.PsnAccountInfoDecipherResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnAccountInfoDecipher.PsnAccountInfoDecipherResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnAccountInfoEncrypt.PsnAccountInfoEncryptParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnAccountInfoEncrypt.PsnAccountInfoEncryptResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnAccountInfoEncrypt.PsnAccountInfoEncryptResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnBatchTransActCollectionSubmit.PsnBatchTransActCollectionSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnBatchTransActCollectionSubmit.PsnBatchTransActCollectionSubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnBatchTransActCollectionSubmit.PsnBatchTransActCollectionSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnBatchTransActCollectionVerify.PsnBatchTransActCollectionVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnBatchTransActCollectionVerify.PsnBatchTransActCollectionVerifyResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnBatchTransActCollectionVerify.PsnBatchTransActCollectionVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnCardQueryBindInfo.PsnCardQueryBindInfoParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnCardQueryBindInfo.PsnCardQueryBindInfoResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnCardQueryBindInfo.PsnCardQueryBindInfoResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnCrcdChargeOnRMBAccountQuery.PsnCrcdChargeOnRMBAccountQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnCrcdChargeOnRMBAccountQuery.PsnCrcdChargeOnRMBAccountQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnCrcdChargeOnRMBAccountQuery.PsnCrcdChargeOnRMBAccountQueryResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocNationalTransferVerify.PsnDirTransBocNationalTransferVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocNationalTransferVerify.PsnDirTransBocNationalTransferVerifyResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocNationalTransferVerify.PsnDirTransBocNationalTransferVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocTransferSubmit.PsnDirTransBocTransferSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocTransferSubmit.PsnDirTransBocTransferSubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocTransferSubmit.PsnDirTransBocTransferSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocTransferSubmitReinforce.PsnDirTransBocTransferSubmitReinforceParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocTransferSubmitReinforce.PsnDirTransBocTransferSubmitReinforceResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocTransferSubmitReinforce.PsnDirTransBocTransferSubmitReinforceResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocTransferVerify.PsnDirTransBocTransferVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocTransferVerify.PsnDirTransBocTransferVerifyResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocTransferVerify.PsnDirTransBocTransferVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankAddPayee.PsnDirTransCrossBankAddPayeeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankAddPayee.PsnDirTransCrossBankAddPayeeResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankAddPayee.PsnDirTransCrossBankAddPayeeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankTransfer.PsnDirTransCrossBankTransferParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankTransfer.PsnDirTransCrossBankTransferResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankTransfer.PsnDirTransCrossBankTransferResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankTransferSubmit.PsnDirTransCrossBankTransferSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankTransferSubmit.PsnDirTransCrossBankTransferSubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankTransferSubmit.PsnDirTransCrossBankTransferSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransNationalAddPayee.PsnDirTransNationalAddPayeeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransNationalAddPayee.PsnDirTransNationalAddPayeeResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransNationalAddPayee.PsnDirTransNationalAddPayeeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransNationalTransferSubmit.PsnDirTransNationalTransferSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransNationalTransferSubmit.PsnDirTransNationalTransferSubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransNationalTransferSubmit.PsnDirTransNationalTransferSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransQueryPayeeList.PsnDirTransQueryPayeeListParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransQueryPayeeList.PsnDirTransQueryPayeeListResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransQueryPayeeList.PsnDirTransQueryPayeeListResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentConfirm.PsnEbpsRealTimePaymentConfirmParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentConfirm.PsnEbpsRealTimePaymentConfirmResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentConfirm.PsnEbpsRealTimePaymentConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentSavePayee.PsnEbpsRealTimePaymentSavePayeeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentSavePayee.PsnEbpsRealTimePaymentSavePayeeResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentSavePayee.PsnEbpsRealTimePaymentSavePayeeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentTransfer.PsnEbpsRealTimePaymentTransferParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentTransfer.PsnEbpsRealTimePaymentTransferResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentTransfer.PsnEbpsRealTimePaymentTransferResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileAgentQuery.PsnMobileAgentQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileAgentQuery.PsnMobileAgentQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileAgentQuery.PsnMobileAgentQueryResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileCancelTrans.PsnMobileCancelTransParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileCancelTrans.PsnMobileCancelTransResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileCancelTrans.PsnMobileCancelTransResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileRemitDetailsQuery.PsnMobileRemitDetailsQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileRemitDetailsQuery.PsnMobileRemitDetailsQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileRemitDetailsQuery.PsnMobileRemitDetailsQueryResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileRemitPre.PsnMobileRemitPreParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileRemitPre.PsnMobileRemitPreResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileRemitPre.PsnMobileRemitPreResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileRemitQuery.PsnMobileRemitQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileRemitQuery.PsnMobileRemitQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileRemitQuery.PsnMobileRemitQueryResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileRemitSubmit.PsnMobileRemitSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileRemitSubmit.PsnMobileRemitSubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileRemitSubmit.PsnMobileRemitSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileResetSendSms.PsnMobileResetSendSmsParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileResetSendSms.PsnMobileResetSendSmsResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileResetSendSms.PsnMobileResetSendSmsResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileTransferPre.PsnMobileTransferPreParam;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileTransferPre.PsnMobileTransferPreResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileTransferPre.PsnMobileTransferPreResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileTransferQueryUnSubmitTrans.PsnMobileTransferQueryUnSubmitTransParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileTransferQueryUnSubmitTrans.PsnMobileTransferQueryUnSubmitTransResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileTransferSubmit.PsnMobileTransferSubmitParam;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileTransferSubmit.PsnMobileTransferSubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileTransferSubmit.PsnMobileTransferSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileWithdrawal.PsnMobileWithdrawalParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileWithdrawal.PsnMobileWithdrawalResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileWithdrawal.PsnMobileWithdrawalResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileWithdrawalDetails.PsnMobileWithdrawalDetailsParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileWithdrawalDetails.PsnMobileWithdrawalDetailsResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileWithdrawalDetails.PsnMobileWithdrawalDetailsResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileWithdrawalDetailsQuery.PsnMobileWithdrawalDetailsQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileWithdrawalDetailsQuery.PsnMobileWithdrawalDetailsQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileWithdrawalQuery.PsnMobileWithdrawalQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileWithdrawalQuery.PsnMobileWithdrawalQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileWithdrawalQuery.PsnMobileWithdrawalQueryResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnOFAFinanceTransfer.PsnOFAFinanceTransferParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnOFAFinanceTransfer.PsnOFAFinanceTransferResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnOFAFinanceTransfer.PsnOFAFinanceTransferResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnOtherBankQueryForTransToAccount.PsnOtherBankQueryForTransToAccountParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnOtherBankQueryForTransToAccount.PsnOtherBankQueryForTransToAccountResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnOtherBankQueryForTransToAccount.PsnOtherBankQueryForTransToAccountResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitFreeCancel.PsnPasswordRemitFreeCancelParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitFreeCancel.PsnPasswordRemitFreeCancelResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitFreeDetailQuery.PsnPasswordRemitFreeDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitFreeDetailQuery.PsnPasswordRemitFreeDetailQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitFreeDetailQuery.PsnPasswordRemitFreeDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitFreeTranQuery.PsnPasswordRemitFreeTranQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitFreeTranQuery.PsnPasswordRemitFreeTranQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitFreeTranQuery.PsnPasswordRemitFreeTranQueryResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitPayment.PsnPasswordRemitPaymentParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitPayment.PsnPasswordRemitPaymentResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitPayment.PsnPasswordRemitPaymentResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitPaymentPre.PsnPasswordRemitPaymentPreParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitPaymentPre.PsnPasswordRemitPaymentPreResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitPaymentPre.PsnPasswordRemitPaymentPreResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryActTypeByActNum.PsnQueryActTypeByActNumParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryActTypeByActNum.PsnQueryActTypeByActNumResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryActTypeByActNum.PsnQueryActTypeByActNumResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryBankInfobyCardBin.PsnQueryBankInfobyCardBinParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryBankInfobyCardBin.PsnQueryBankInfobyCardBinResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryBankInfobyCardBin.PsnQueryBankInfobyCardBinResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryChinaBankAccount.PsnQueryChinaBankAccountParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryChinaBankAccount.PsnQueryChinaBankAccountResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryChinaBankAccount.PsnQueryChinaBankAccountResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryRecentPayeeInfo.PsnQueryRecentPayeeInfoBean;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryRecentPayeeInfo.PsnQueryRecentPayeeInfoParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryRecentPayeeInfo.PsnQueryRecentPayeeInfoResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnRemitReturnInfo.PsnRemitReturnInfoParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnRemitReturnInfo.PsnRemitReturnInfoResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnRemitReturnInfo.PsnRemitReturnInfoResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnSingleTransQueryTransferRecord.PsnSingleTransQueryTransferRecordParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnSingleTransQueryTransferRecord.PsnSingleTransQueryTransferRecordResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnSingleTransQueryTransferRecord.PsnSingleTransQueryTransferRecordResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActCollectionSubmit.PsnTransActCollectionSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActCollectionSubmit.PsnTransActCollectionSubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActCollectionSubmit.PsnTransActCollectionSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActCollectionVerify.PsnTransActCollectionVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActCollectionVerify.PsnTransActCollectionVerifyResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActCollectionVerify.PsnTransActCollectionVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActDeletePayer.PsnTransActDeletePayerParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActDeletePayer.PsnTransActDeletePayerResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActDeletePayer.PsnTransActDeletePayerResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActModifyPayerMobile.PsnTransActModifyPayerMobileParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActModifyPayerMobile.PsnTransActModifyPayerMobileResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActModifyPayerMobile.PsnTransActModifyPayerMobileResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentOrderDetail.PsnPaymentOrderDetailParam;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentOrderDetail.PsnPaymentOrderDetailResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentOrderDetail.PsnPaymentOrderDetailResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentOrderDetail.PsnTransActPaymentOrderDetailParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentOrderDetail.PsnTransActPaymentOrderDetailResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentOrderDetail.PsnTransActPaymentOrderDetailResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentSubmit.PsnTransActPaymentSubmitParam;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentSubmit.PsnTransActPaymentSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentSubmit.PsnTransActPaymentSubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentSubmit.PsnTransActPaymentSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentVerify.PsnTransActPaymentVerifyParam;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentVerify.PsnTransActPaymentVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentVerify.PsnTransActPaymentVerifyResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActPaymentVerify.PsnTransActPaymentVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryPayerList.PsnTransActQueryPayerListParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryPayerList.PsnTransActQueryPayerListResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryPayerList.PsnTransActQueryPayerListResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryPaymentOrderList.PsnPaymentOrderListParam;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryPaymentOrderList.PsnPaymentOrderListResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryPaymentOrderList.PsnPaymentOrderListResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryPaymentOrderList.PsnTransActQueryPaymentOrderListParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryPaymentOrderList.PsnTransActQueryPaymentOrderListResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryPaymentOrderList.PsnTransActQueryPaymentOrderListResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryReminderOrderList.PsnReminderOrderListParam;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryReminderOrderList.PsnReminderOrderListResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryReminderOrderList.PsnReminderOrderListResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryReminderOrderList.PsnTransActQueryReminderOrderListParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryReminderOrderList.PsnTransActQueryReminderOrderListResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryReminderOrderList.PsnTransActQueryReminderOrderListResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActReminderOrderDetail.PsnReminderOrderDetailParam;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActReminderOrderDetail.PsnReminderOrderDetailResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActReminderOrderDetail.PsnReminderOrderDetailResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActReminderOrderDetail.PsnTransActReminderOrderDetailParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActReminderOrderDetail.PsnTransActReminderOrderDetailResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActReminderOrderDetail.PsnTransActReminderOrderDetailResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActReminderSms.PsnTransActReminderSmsParam;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActReminderSms.PsnTransActReminderSmsResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActReminderSms.PsnTransActReminderSmsResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActSavePayer.PsnTransActSavePayerParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActSavePayer.PsnTransActSavePayerResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActSavePayer.PsnTransActSavePayerResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActUndoReminderOrder.PsnTransActUndoReminderOrderResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActUndoReminderOrder.PsnTransActUndoReminderOrderResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocAddPayee.PsnTransBocAddPayeeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocAddPayee.PsnTransBocAddPayeeResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocAddPayee.PsnTransBocAddPayeeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocNationalTransferVerify.PsnTransBocNationalTransferVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocNationalTransferVerify.PsnTransBocNationalTransferVerifyResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocNationalTransferVerify.PsnTransBocNationalTransferVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferSubmit.PsnTransBocTransferSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferSubmit.PsnTransBocTransferSubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferSubmit.PsnTransBocTransferSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferVerify.PsnTransBocTransferVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferVerify.PsnTransBocTransferVerifyResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferVerify.PsnTransBocTransferVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetBocTransferCommissionCharge.PsnTransGetBocTransferCommissionChargeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetBocTransferCommissionCharge.PsnTransGetBocTransferCommissionChargeResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetBocTransferCommissionCharge.PsnTransGetBocTransferCommissionChargeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetNationalTransferCommissionCharge.PsnTransGetNationalTransferCommissionChargeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetNationalTransferCommissionCharge.PsnTransGetNationalTransferCommissionChargeResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetNationalTransferCommissionCharge.PsnTransGetNationalTransferCommissionChargeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransIsSamePayee.PsnTransIsSamePayeeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransIsSamePayee.PsnTransIsSamePayeeResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransIsSamePayee.PsnTransIsSamePayeeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransLinkTransferSubmit.PsnTransLinkTransferSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransLinkTransferSubmit.PsnTransLinkTransferSubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransLinkTransferSubmit.PsnTransLinkTransferSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransManagePayeeDeletePayee.PsnTransManagePayeeDeletePayeeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransManagePayeeDeletePayee.PsnTransManagePayeeDeletePayeeResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransManagePayeeDeletePayee.PsnTransManagePayeeDeletePayeeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransManagePayeeModifyAlias.PsnTransManagePayeeModifyAliasParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransManagePayeeModifyAlias.PsnTransManagePayeeModifyAliasResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransManagePayeeModifyAlias.PsnTransManagePayeeModifyAliasResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransManagePayeeModifyMobile.PsnTransManagePayeeModifyMobileParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransManagePayeeModifyMobile.PsnTransManagePayeeModifyMobileResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransManagePayeeModifyMobile.PsnTransManagePayeeModifyMobileResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransManagePayeeQueryPayeeList.PsnTransManagePayeeQueryPayeeListParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransManagePayeeQueryPayeeList.PsnTransManagePayeeQueryPayeeListResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransManagePayeeQueryPayeeList.PsnTransManagePayeeQueryPayeeListResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalAddPayee.PsnTransNationalAddPayeeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalAddPayee.PsnTransNationalAddPayeeResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalAddPayee.PsnTransNationalAddPayeeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalChangeBooking.PsnTransNationalChangeBookingParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalChangeBooking.PsnTransNationalChangeBookingResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalChangeBooking.PsnTransNationalChangeBookingResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalTransferSubmit.PsnTransNationalTransferSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalTransferSubmit.PsnTransNationalTransferSubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalTransferSubmit.PsnTransNationalTransferSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalTransferSubmitReinforce.PsnTransNationalTransferSubmitReinforceParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalTransferSubmitReinforce.PsnTransNationalTransferSubmitReinforceResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalTransferSubmitReinforce.PsnTransNationalTransferSubmitReinforceResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPayeeListqueryForDim.PsnTransPayeeListqueryForDimParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPayeeListqueryForDim.PsnTransPayeeListqueryForDimResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPayeeListqueryForDim.PsnTransPayeeListqueryForDimResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPreRecordDelete.PsnTransPreRecordDeleteParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPreRecordDelete.PsnTransPreRecordDeleteResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPreRecordDelete.PsnTransPreRecordDeleteResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPreRecordDetailQuery.PsnTransPreRecordDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPreRecordDetailQuery.PsnTransPreRecordDetailQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPreRecordDetailQuery.PsnTransPreRecordDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPreRecordQuery.PsnTransPreRecordQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPreRecordQuery.PsnTransPreRecordQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPreRecordQuery.PsnTransPreRecordQueryResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryExternalBankInfo.PsnTransQueryExternalBankInfoParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryExternalBankInfo.PsnTransQueryExternalBankInfoResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryExternalBankInfo.PsnTransQueryExternalBankInfoResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryTransferRecord.PsnTransQueryTransferRecordParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryTransferRecord.PsnTransQueryTransferRecordResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryTransferRecord.PsnTransQueryTransferRecordResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryTransferRecordDetail.PsnTransQueryTransferRecordDetailParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryTransferRecordDetail.PsnTransQueryTransferRecordDetailResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryTransferRecordDetail.PsnTransQueryTransferRecordDetailResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryTransferRecordsNew.PsnTransQueryTransferRecordsNewParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryTransferRecordsNew.PsnTransQueryTransferRecordsNewResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryTransferRecordsNew.PsnTransQueryTransferRecordsNewResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQuotaQuery.PsnTransQuotaQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQuotaQuery.PsnTransQuotaQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQuotaQuery.PsnTransQuotaQueryResult;
import com.boc.bocsoft.mobile.bii.common.client.BIIClient;

import java.util.List;

import rx.Observable;

/**
 * Created by lxw4566 on 2016/6/28.
 */
public class TransferService {

    /**
     * 查询账户详情
     *
     * @param params
     * @return
     */
    //    public Observable<PsnAccountQueryAccountDetailResult> psnAccountQueryAccountDetail(PsnAccountQueryAccountDetailParams params) {
    //
    //        return BIIClient.instance.post("PsnAccountQueryAccountDetail", params, PsnAccountQueryAccountDetailResult.class);
    //    }

    /**
     * ATM无卡取款查询
     *
     * @param params
     * @return
     */
    public Observable<PsnPasswordRemitFreeTranQueryResult> psnPasswordRemitFreeTranQuery(PsnPasswordRemitFreeTranQueryParams params) {

        return BIIClient.instance.post("PsnPasswordRemitFreeTranQuery", params, PsnPasswordRemitFreeTranQueryResponse.class);
    }

    /**
     * ATM无卡取款详情
     *
     * @param params
     * @return
     */
    public Observable<PsnPasswordRemitFreeDetailQueryResult> psnPasswordRemitFreeDetailQuery(PsnPasswordRemitFreeDetailQueryParams params) {

        return BIIClient.instance.post("PsnPasswordRemitFreeDetailQuery", params, PsnPasswordRemitFreeDetailQueryResponse.class);
    }

    /**
     * ATM无卡取款撤销
     *
     * @param params
     * @return
     */
    public Observable<String> psnPasswordRemitFreeCancel(PsnPasswordRemitFreeCancelParams params) {

        return BIIClient.instance.post("PsnPasswordRemitFreeCancel", params, PsnPasswordRemitFreeCancelResponse.class);
    }

    /**
     * 查询收款人列表（模糊查询)
     *
     * @param params
     * @return
     */
    public static Observable<PsnTransPayeeListqueryForDimResult> PsnTransPayeeListqueryForDim(PsnTransManagePayeeQueryPayeeListParams params) {

        return BIIClient.instance.post("PsnTransPayeeListqueryForDim", params, PsnTransPayeeListqueryForDimResponse.class);
    }

    /**
     * 手机号转账查询
     *
     * @param params
     * @return
     */
    public Observable<com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileTransferQueryUnSubmitTrans.PsnMobileTransferQueryUnSubmitTransResult> psnMobileTransferQueryUnSubmitTrans(PsnMobileTransferQueryUnSubmitTransParams params) {

        return BIIClient.instance.post("PsnMobileTransferQueryUnSubmitTrans", params, PsnMobileTransferQueryUnSubmitTransResponse.class);
    }

    /**
     * 账户信息解密
     *
     * @param params
     */
    public Observable<PsnAccountInfoDecipherResult> psnAccountInfoDecipher(PsnAccountInfoDecipherParams params) {
        return BIIClient.instance.post("PsnAccountInfoDecipher", params, PsnAccountInfoDecipherResponse.class);
    }


    /**
     * 账户信息解密
     *
     * @param params
     */
    public Observable<PsnAccountInfoEncryptResult> psnAccountInfoEncrypt(PsnAccountInfoEncryptParams params) {
        return BIIClient.instance.post("PsnAccountInfoEncrypt", params, PsnAccountInfoEncryptResponse.class);
    }

    /**
     * 汇出查询 -- 列表
     *
     * @param params
     * @return
     */
    public Observable<PsnMobileRemitQueryResult> psnMobileRemitQuery(PsnMobileRemitQueryParams params) {
        return BIIClient.instance.post("PsnMobileRemitQuery", params, PsnMobileRemitQueryResponse.class);
    }

    /**
     * 汇出查询 -- 详情
     *
     * @param params
     * @return
     */
    public Observable<PsnMobileRemitDetailsQueryResult> psnMobileRemitDetailsQuery(PsnMobileRemitDetailsQueryParams params) {
        return BIIClient.instance.post("PsnMobileRemitDetailsQuery", params, PsnMobileRemitDetailsQueryResponse.class);
    }

    /**
     * 汇出查询 -- 重新发送短信
     *
     * @param params
     * @return
     */
    public Observable<PsnMobileResetSendSmsResult> psnMobileResetSendSms(PsnMobileResetSendSmsParams params) {
        return BIIClient.instance.post("PsnMobileResetSendSms", params, PsnMobileResetSendSmsResponse.class);
    }

    /**
     * 汇出查询 -- 撤销交易
     *
     * @param params
     * @return
     */
    public Observable<PsnMobileCancelTransResult> psnMobileCancelTrans(PsnMobileCancelTransParams params) {
        return BIIClient.instance.post("PsnMobileCancelTrans", params, PsnMobileCancelTransResponse.class);
    }


    /**
     * 预约交易查询 -- 预约管理列表
     *
     * @param params
     * @return
     */
    public Observable<PsnTransPreRecordQueryResult> psnTransPreRecordQuery(PsnTransPreRecordQueryParams params) {
        return BIIClient.instance.post("PsnTransPreRecordQuery", params, PsnTransPreRecordQueryResponse.class);
    }

    /**
     * 预约管理详情查询
     *
     * @param params
     * @return
     */
    public Observable<PsnTransPreRecordDetailQueryResult> psnTransPreRecordDetailQuery(PsnTransPreRecordDetailQueryParams params) {
        return BIIClient.instance.post("PsnTransPreRecordDetailQuery", params, PsnTransPreRecordDetailQueryResponse.class);
    }


    /**
     * 预约交易删除
     *
     * @param params
     * @return
     */
    public Observable<PsnTransPreRecordDeleteResult> psnTransPreRecordDelete(PsnTransPreRecordDeleteParams params) {
        return BIIClient.instance.post("PsnTransPreRecordDelete", params, PsnTransPreRecordDeleteResponse.class);
    }


    /**
     * 查询收款人
     *
     * @param params
     * @return
     */
    public Observable<List<PsnTransManagePayeeQueryPayeeListResult>> getPsnTransManagePayeeQueryPayeeList(PsnTransManagePayeeQueryPayeeListParams params) {
        return BIIClient.instance.post("PsnTransManagePayeeQueryPayeeList", params, PsnTransManagePayeeQueryPayeeListResponse.class);
    }

    /**
     * 查询近期收款人
     * @param params PsnQueryRecentPayeeInfoParams
     * @return
     */
    public Observable<List<PsnQueryRecentPayeeInfoBean>> psnQueryRecentPayeeInfo(PsnQueryRecentPayeeInfoParams params) {
        return BIIClient.instance.post("PsnQueryRecentPayeeInfo", params, PsnQueryRecentPayeeInfoResponse.class);
    }

    /**
     * c查询转账限额
     * @param params PsnTransQuotaQueryParams
     * @return
     */
    public Observable<PsnTransQuotaQueryResult> psnTransQuotaQuery(PsnTransQuotaQueryParams params) {
        return BIIClient.instance.post("PsnTransQuotaQuery", params, PsnTransQuotaQueryResponse.class);
    }



    /**
     * 中行内转账预交易
     *
     * @param params
     * @return
     */
    public Observable<PsnTransBocTransferVerifyResult> psnTransBocTransferVerify(PsnTransBocTransferVerifyParams params) {

        return BIIClient.instance.post("PsnTransBocTransferVerify", params, PsnTransBocTransferVerifyResponse.class);
    }

    /**
     * 行内转账提交交易
     *
     * @param params
     * @return
     */
    public Observable<PsnTransBocTransferSubmitResult> psnTransBocTransferSubmit(PsnTransBocTransferSubmitParams params) {

        return BIIClient.instance.post("PsnTransBocTransferSubmit", params, PsnTransBocTransferSubmitResponse.class);
    }

    /**
     * 查询定向收款人列表
     *
     * @param params
     * @return
     */
    public Observable<List<PsnDirTransQueryPayeeListResult>> getPsnDirTransQueryPayeeList(PsnDirTransQueryPayeeListParams params) {

        return BIIClient.instance.post("PsnDirTransQueryPayeeList", params, PsnDirTransQueryPayeeListResponse.class);
    }

    /**
     * 中行内定向转账预交易
     *
     * @param params
     * @return
     */
    public Observable<PsnDirTransBocTransferVerifyResult> psnDirTransBocTransferVerify(PsnDirTransBocTransferVerifyParams params) {

        return BIIClient.instance.post("PsnDirTransBocTransferVerify", params, PsnDirTransBocTransferVerifyResponse.class);
    }

    /**
     * 中行内定向转账提交交易
     *
     * @param params
     * @return
     */
    public Observable<PsnDirTransBocTransferSubmitResult> psndirTransBocTransferSubmit(PsnDirTransBocTransferSubmitParams params) {

        return BIIClient.instance.post("PsnDirTransBocTransferSubmit", params, PsnDirTransBocTransferSubmitResponse.class);
    }

    /**
     * 中行定向转账加强认证交易
     *
     * @param params
     * @return
     */
    public Observable<PsnDirTransBocTransferSubmitReinforceResult> psnDirTransBocTransferSubmitReinforce(PsnDirTransBocTransferSubmitReinforceParams params) {

        return BIIClient.instance.post("PsnDirTransBocTransferSubmitReinforce", params, PsnDirTransBocTransferSubmitReinforceResponse.class);
    }


    /**
     * 跨行转账预交易
     *
     * @param params
     * @return
     */
    public Observable<PsnTransBocNationalTransferVerifyResult> psnTransferNationalVerify(PsnTransBocNationalTransferVerifyParams params) {

        return BIIClient.instance.post("PsnTransBocNationalTransferVerify", params, PsnTransBocNationalTransferVerifyResponse.class);
    }

    /**
     * 跨行转账提交交易
     *
     * @param params
     * @return
     */
    public Observable<PsnTransNationalTransferSubmitResult> psnTransNationalSubmit(PsnTransNationalTransferSubmitParams params) {

        return BIIClient.instance.post("PsnTransNationalTransferSubmit", params, PsnTransNationalTransferSubmitResponse.class);
    }

    /**
     * 跨行转账加强认证交易
     *
     * @param params
     * @return
     */
    public Observable<PsnTransNationalTransferSubmitReinforceResult> psnTransSubmitReinforce(PsnTransNationalTransferSubmitReinforceParams params) {

        return BIIClient.instance.post("PsnTransNationalTransferSubmitReinforce", params, PsnTransNationalTransferSubmitReinforceResponse.class);
    }




    /**
     * 跨行定向转账预交易
     *
     * @param params
     * @return
     */
    public Observable<PsnDirTransBocNationalTransferVerifyResult> psnDirTransNationalVerify(PsnDirTransBocNationalTransferVerifyParams params) {

        return BIIClient.instance.post("PsnDirTransBocNationalTransferVerify", params, PsnDirTransBocNationalTransferVerifyResponse.class);
    }

    /**
     * 跨行定向转账提交交易
     *
     * @param params
     * @return
     */
    public Observable<PsnDirTransNationalTransferSubmitResult> psnDirTransNationalSubmit(PsnDirTransNationalTransferSubmitParams params) {

        return BIIClient.instance.post("PsnDirTransNationalTransferSubmit", params, PsnDirTransNationalTransferSubmitResponse.class);
    }

    /**
     * 跨行定向转账加强认证交易
     *
     * @param params
     * @return
     */
    //    public Observable<PsnDirTransNationalTransferSubmitReinforceResult> psnDirTransNationalSubmitReinforce(PsnDirTransNationalTransferSubmitReinforceParams params) {
    //
    //        return BIIClient.instance.post("PsnDirTransBocTransferSubmitReinforce", params, PsnDirTransNationalTransferSubmitReinforceResponse.class);
    //    }
    /**
     * 查询单笔交易转账记录
     *
     * @param params
     * @return
     */
    public Observable<PsnSingleTransQueryTransferRecordResult> psnSingleTransQueryTransferRecord(PsnSingleTransQueryTransferRecordParams params) {

        return BIIClient.instance.post("PsnSingleTransQueryTransferRecord", params, PsnSingleTransQueryTransferRecordResponse.class);
    }


    /**
     * 跨行实时转账预交易
     *
     * @param params
     * @return
     */
    public Observable<PsnEbpsRealTimePaymentConfirmResult> psnTransNationalRealtimeVerify(PsnEbpsRealTimePaymentConfirmParams params) {

        return BIIClient.instance.post("PsnEbpsRealTimePaymentConfirm", params, PsnEbpsRealTimePaymentConfirmResponse.class);
    }

    /**
     * 跨行实时转账提交交易
     *
     * @param params
     * @return
     */
    public Observable<PsnEbpsRealTimePaymentTransferResult> psnTransNationalRealtimeSubmit(PsnEbpsRealTimePaymentTransferParams params) {

        return BIIClient.instance.post("PsnEbpsRealTimePaymentTransfer", params, PsnEbpsRealTimePaymentTransferResponse.class);
    }


    /**
     * 跨行定向实时转账预交易
     *
     * @param params
     * @return
     */
    public Observable<PsnDirTransCrossBankTransferResult> psnDirTransNationalRealtimeVerify(PsnDirTransCrossBankTransferParams params) {

        return BIIClient.instance.post("PsnDirTransCrossBankTransfer", params, PsnDirTransCrossBankTransferResponse.class);
    }

    /**
     * 跨行定向实时转账提交交易
     *
     * @param params
     * @return
     */
    public Observable<PsnDirTransCrossBankTransferSubmitResult> psnDirTransNationalRealtimeSubmit(PsnDirTransCrossBankTransferSubmitParams params) {

        return BIIClient.instance.post("PsnDirTransCrossBankTransferSubmit", params, PsnDirTransCrossBankTransferSubmitResponse.class);
    }


    /**
     * 查询收款人列表 ，支持模糊查询
     *
     * @param params
     * @return
     */
    public Observable<PsnTransPayeeListqueryForDimResult> getPsnTransPayeeListqueryForDim(PsnTransPayeeListqueryForDimParams params) {

        return BIIClient.instance.post("PsnTransPayeeListqueryForDim", params, PsnTransPayeeListqueryForDimResponse.class);
    }


    /**
     * 增加收款人
     *
     * @param params
     * @return
     */
    public Observable<PsnTransBocAddPayeeResult> psnTransBocAddPayee(PsnTransBocAddPayeeParams params) {

        return BIIClient.instance.post("PsnTransBocAddPayee", params, PsnTransBocAddPayeeResponse.class);
    }

    /**
     * 关联账户转账提交交易
     *
     * @param params
     * @return
     */
    public Observable<PsnTransLinkTransferSubmitResult> psnTransLinkTransferSubmit(PsnTransLinkTransferSubmitParams params) {

        return BIIClient.instance.post("PsnTransLinkTransferSubmit", params, PsnTransLinkTransferSubmitResponse.class);
    }
/**
     * 理财账户资金划转
     * @param params
     * @return
     */
    public Observable<PsnOFAFinanceTransferResult> psnOFAFinanceTransfer(PsnOFAFinanceTransferParams params) {

        return BIIClient.instance.post("PsnOFAFinanceTransfer", params, PsnOFAFinanceTransferResponse.class);
    }


    /**
     * 全球交易人民币记账功能查询
     * @param params
     * @return
     */
    public Observable<PsnCrcdChargeOnRMBAccountQueryResult> psnCrcdChargeOnRMBAccountQuery(PsnCrcdChargeOnRMBAccountQueryParams params) {
        return BIIClient.instance.post("PsnCrcdChargeOnRMBAccountQuery", params, PsnCrcdChargeOnRMBAccountQueryResponse.class);
    }



    /**
     * 转账记录 查询
     *
     * @param params
     * @return
     */
    public Observable<PsnTransQueryTransferRecordResult> psnTransQueryTransferRecord(PsnTransQueryTransferRecordParams params) {
        return BIIClient.instance.post("PsnTransQueryTransferRecord", params, PsnTransQueryTransferRecordResponse.class);
    }

    /**
     * 转账记录 查询 - 新接口
     *
     * @param params
     * @return
     */
    public Observable<PsnTransQueryTransferRecordsNewResult> psnTransQueryTransferRecordsNew(PsnTransQueryTransferRecordsNewParams params) {
        return BIIClient.instance.post("PsnTransQueryTransferRecordsNew", params, PsnTransQueryTransferRecordsNewResponse.class);
    }

    /**
     * 转账记录详情 查询
     *
     * @param params
     * @return
     */
    public Observable<PsnTransQueryTransferRecordDetailResult> psnTransQueryTransferRecordDetail(PsnTransQueryTransferRecordDetailParams params) {
        return BIIClient.instance.post("PsnTransQueryTransferRecordDetail", params, PsnTransQueryTransferRecordDetailResponse.class);
    }

    /**
     * 转账记录 退汇信息 查询
     *
     * @param params
     * @return
     */
    public Observable<PsnRemitReturnInfoResult> psnRemitReturnInfo(PsnRemitReturnInfoParams params) {
        return BIIClient.instance.post("PsnRemitReturnInfo", params, PsnRemitReturnInfoResponse.class);
    }

    /**
     * 手机取款-取款查询
     *
     * @param params
     * @return
     */
    public Observable<PsnMobileWithdrawalQueryResult> psnMobileWithdrawalQuery(PsnMobileWithdrawalQueryParams params) {
        return BIIClient.instance.post("PsnMobileWithdrawalQuery", params, PsnMobileWithdrawalQueryResponse.class);
    }

    /**
     * 手机取款 -- 取款详情
     *
     * @param params
     * @return
     */
    public Observable<String> psnMobileWithdrawalDetailsQuery(PsnMobileWithdrawalDetailsQueryParams params) {
        return BIIClient.instance.post("PsnMobileWithdrawalDetailsQuery", params, PsnMobileWithdrawalDetailsQueryResponse.class);
    }

    /**
     * 汇款解付详细信息
     *
     * @param params
     * @return
     */
    public Observable<PsnMobileWithdrawalDetailsResult> psnMobileWithdrawalDetails(PsnMobileWithdrawalDetailsParams params) {
        return BIIClient.instance.post("PsnMobileWithdrawalDetails", params, PsnMobileWithdrawalDetailsResponse.class);
    }

    /**
     * 汇款解付
     *
     * @param params
     * @return
     */
    public Observable<PsnMobileWithdrawalResult> psnMobileWithdrawal(PsnMobileWithdrawalParams params) {
        return BIIClient.instance.post("PsnMobileWithdrawal", params, PsnMobileWithdrawalResponse.class);
    }


    /**
     * 主动收款保存常用付款人
     *
     * @param params
     * @return
     */
    public Observable<PsnTransActSavePayerResult> psnTransActSavePayer(PsnTransActSavePayerParams params) {
        return BIIClient.instance.post("PsnTransActSavePayer", params, PsnTransActSavePayerResponse.class);
    }

    /**
     * 催款指令查询
     * Created by zhx on 2016/7/5
     *
     * @param params
     * @return
     */
    public Observable<PsnTransActQueryReminderOrderListResult> psnTransActQueryReminderOrderList(PsnTransActQueryReminderOrderListParams params) {
        return BIIClient.instance.post("PsnTransActQueryReminderOrderList", params, PsnTransActQueryReminderOrderListResponse.class);
    }

    /**
     * 催款指令详情
     *
     * @param params
     * @return
     */
    public Observable<PsnTransActReminderOrderDetailResult> psnTransActReminderOrderDetail(PsnTransActReminderOrderDetailParams params) {
        return BIIClient.instance.post("PsnTransActReminderOrderDetail", params, PsnTransActReminderOrderDetailResponse.class);
    }

    /**
     * 撤消催款指令
     *
     * @param params
     * @return
     */
    public Observable<PsnTransActUndoReminderOrderResult> psnTransActUndoReminderOrder(PsnTransActReminderOrderDetailParams params) {
        return BIIClient.instance.post("PsnTransActUndoReminderOrder", params, PsnTransActUndoReminderOrderResponse.class);
    }

    /**
     * 查询付款人列表
     *
     * @param params
     * @return
     */
    public Observable<List<PsnTransActQueryPayerListResult>> psnTransActQueryPayerList(PsnTransActQueryPayerListParams params) {
        return BIIClient.instance.post("PsnTransActQueryPayerList", params, PsnTransActQueryPayerListResponse.class);
    }

    /**
     * 修改付款人手机
     *
     * @param params
     * @return
     */
    public Observable<PsnTransActModifyPayerMobileResult> psnTransActModifyPayerMobile(PsnTransActModifyPayerMobileParams params) {
        return BIIClient.instance.post("PsnTransActModifyPayerMobile", params, PsnTransActModifyPayerMobileResponse.class);
    }

    /**
     * 删除付款人
     *
     * @param params
     * @return
     */
    public Observable<PsnTransActDeletePayerResult> psnTransActDeletePayer(PsnTransActDeletePayerParams params) {
        return BIIClient.instance.post("PsnTransActDeletePayer", params, PsnTransActDeletePayerResponse.class);
    }

    /**
     * 付款指令查询
     *
     * @param params
     * @return
     */
    public Observable<PsnTransActQueryPaymentOrderListResult> psnTransActQueryPaymentOrderList(PsnTransActQueryPaymentOrderListParams params) {
        return BIIClient.instance.post("PsnTransActQueryPaymentOrderList", params, PsnTransActQueryPaymentOrderListResponse.class);
    }

    /**
     * 付款指令记录详情
     *
     * @param params
     * @return
     */
    public Observable<PsnTransActPaymentOrderDetailResult> psnTransActPaymentOrderDetail(PsnTransActPaymentOrderDetailParams params) {
        return BIIClient.instance.post("PsnTransActPaymentOrderDetail", params, PsnTransActPaymentOrderDetailResponse.class);
    }

    /**
     * 主动收款付款预交易
     *
     * @param params
     * @return
     */
    public Observable<PsnTransActPaymentVerifyResult> psnTransActPaymentVerify(PsnTransActPaymentVerifyParams params) {
        return BIIClient.instance.post("PsnTransActPaymentVerify", params, PsnTransActPaymentVerifyResponse.class);
    }

    /**
     * 主动收款付款提交
     *
     * @param params
     * @return
     */
    public Observable<PsnTransActPaymentSubmitResult> psnTransActPaymentSubmit(PsnTransActPaymentSubmitParams params) {
        return BIIClient.instance.post("PsnTransActPaymentSubmit", params, PsnTransActPaymentSubmitResponse.class);
    }

    /**
     * 主动收款预交易(与32 主动收款提交相对应)
     *
     * @param params
     * @return
     */
    public Observable<PsnTransActCollectionVerifyResult> psnTransActCollectionVerify(PsnTransActCollectionVerifyParams params) {
        return BIIClient.instance.post("PsnTransActCollectionVerify", params, PsnTransActCollectionVerifyResponse.class);
    }

    /**
     * 主动收款提交
     *
     * @param params
     * @return
     */
    public Observable<PsnTransActCollectionSubmitResult> psnTransActCollectionSubmit(PsnTransActCollectionSubmitParams params) {
        return BIIClient.instance.post("PsnTransActCollectionSubmit", params, PsnTransActCollectionSubmitResponse.class);
    }

    /**
     * 催款指令查询
     *
     * @param params
     * @return
     */
    public Observable<PsnReminderOrderListResult> psnTransActQueryReminderOrderList(PsnReminderOrderListParam params) {

        return BIIClient.instance.post("PsnTransActQueryReminderOrderList", params, PsnReminderOrderListResponse.class);
    }

    /**
     * 中行内转账费用试算
     */
    public Observable<PsnTransGetBocTransferCommissionChargeResult> psnTransGetBocTransferCommissionCharge(
            PsnTransGetBocTransferCommissionChargeParams params) {
        return BIIClient.instance.post("PsnTransGetBocTransferCommissionCharge", params,
                PsnTransGetBocTransferCommissionChargeResponse.class);
    }

    /**
     * 跨行转账费用试算
     */
    public Observable<PsnTransGetNationalTransferCommissionChargeResult> psnTransGetNationalTransferCommissionCharge(
            PsnTransGetNationalTransferCommissionChargeParams params) {
        return BIIClient.instance.post("PsnTransGetNationalTransferCommissionCharge", params,
                PsnTransGetNationalTransferCommissionChargeResponse.class);
    }


    /**
     * 查询付款人列表
     *
     * @param params
     * @return
     */
    public Observable<PsnPaymentOrderListResult> psnTransActQueryPaymentOrderList(PsnPaymentOrderListParam params) {

        return BIIClient.instance.post("PsnTransActQueryPaymentOrderList", params, PsnPaymentOrderListResponse.class);
    }

    /**
     * 查询收款人详情
     *
     * @param params
     * @return
     */
    public Observable<PsnReminderOrderDetailResult> psnTransActReminderOrderDetail(PsnReminderOrderDetailParam params) {

        return BIIClient.instance.post("PsnTransActReminderOrderDetail", params, PsnReminderOrderDetailResponse.class);
    }

    /**
     * 查询付款人详情
     *
     * @param params
     * @return
     */
    public Observable<PsnPaymentOrderDetailResult> psnTransActPaymentOrderDetail(PsnPaymentOrderDetailParam params) {

        return BIIClient.instance.post("PsnTransActPaymentOrderDetail", params, PsnPaymentOrderDetailResponse.class);
    }

    /**
     * 主动收款付款预交易
     *
     * @param params
     * @return
     */
    public Observable<PsnTransActPaymentVerifyResult> psnTransActPaymentVerify(PsnTransActPaymentVerifyParam params) {

        return BIIClient.instance.post("PsnTransActPaymentVerify", params, PsnTransActPaymentVerifyResponse.class);
    }

    /**
     * 主动收款付款提交
     *
     * @param params
     * @return
     */
    public Observable<PsnTransActPaymentSubmitResult> psnTransActPaymentSubmit(PsnTransActPaymentSubmitParam params) {

        return BIIClient.instance.post("PsnTransActPaymentSubmit", params, PsnTransActPaymentSubmitResponse.class);
    }

    /**
     * ATM无卡取款预交易
     *
     * @param params
     * @return
     */
    public Observable<PsnPasswordRemitPaymentPreResult> psnPasswordRemitPaymentPre(PsnPasswordRemitPaymentPreParams params) {
        return BIIClient.instance.post("PsnPasswordRemitPaymentPre", params, PsnPasswordRemitPaymentPreResponse.class);
    }

    /**
     * ATM无卡取款提交交易
     *
     * @param params
     * @return
     */
    public Observable<PsnPasswordRemitPaymentResult> psnPasswordRemitPayment(PsnPasswordRemitPaymentParams params) {
        return BIIClient.instance.post("PsnPasswordRemitPayment", params, PsnPasswordRemitPaymentResponse.class);
    }

    /**
     * 代理点查询
     *
     * @param params
     * @return
     */
    public Observable<PsnMobileAgentQueryResult> psnMobileAgentQuery(PsnMobileAgentQueryParams params) {
        return BIIClient.instance.post("PsnMobileAgentQuery", params, PsnMobileAgentQueryResponse.class);
    }

    /**
     * 汇往取款人预交易
     *
     * @param params
     * @return
     */
    public Observable<PsnMobileRemitPreResult> psnMobileRemitPre(PsnMobileRemitPreParams params) {
        return BIIClient.instance.post("PsnMobileRemitPre", params, PsnMobileRemitPreResponse.class);
    }

    /**
     * 汇往取款人提交交易
     *
     * @param params
     * @return
     */
    public Observable<PsnMobileRemitSubmitResult> psnMobileRemitSubmit(PsnMobileRemitSubmitParams params) {
        return BIIClient.instance.post("PsnMobileRemitSubmit", params, PsnMobileRemitSubmitResponse.class);
    }

    // 查询银行列表
    public Observable<PsnOtherBankQueryForTransToAccountResult> psnOtherBankQueryForTransToAccount(PsnOtherBankQueryForTransToAccountParams params) {
        return BIIClient.instance.post("PsnOtherBankQueryForTransToAccount", params, PsnOtherBankQueryForTransToAccountResponse.class);
    }

    // 转账银行开户行查询
    public Observable<PsnTransQueryExternalBankInfoResult> psnTransQueryExternalBankInfo(PsnTransQueryExternalBankInfoParams params) {
        return BIIClient.instance.post("PsnTransQueryExternalBankInfo", params, PsnTransQueryExternalBankInfoResponse.class);
    }

    // 判断是否存在相同收款人
    public Observable<PsnTransIsSamePayeeResult> psnTransIsSamePayee(PsnTransIsSamePayeeParams params) {
        return BIIClient.instance.post("PsnTransIsSamePayee", params, PsnTransIsSamePayeeResponse.class);
    }

    // 国内跨行汇款：新增收款人
    public Observable<PsnTransNationalAddPayeeResult> psnTransNationalAddPayee(PsnTransNationalAddPayeeParams params) {
        return BIIClient.instance.post("PsnTransNationalAddPayee", params, PsnTransNationalAddPayeeResponse.class);
    }
    // 国内定向跨行汇款：新增收款人
    public Observable<PsnDirTransNationalAddPayeeResult> psnDirTransNationalAddPayee(PsnDirTransNationalAddPayeeParams params) {
        return BIIClient.instance.post("PsnDirTransNationalAddPayee", params, PsnDirTransNationalAddPayeeResponse.class);
    }
    // 国内定向跨行汇款：新增收款人
    public Observable<PsnDirTransCrossBankAddPayeeResult> psnDirTransCrossBankAddPayee(PsnDirTransCrossBankAddPayeeParams params) {
        return BIIClient.instance.post("PsnDirTransCrossBankAddPayee", params, PsnDirTransCrossBankAddPayeeResponse.class);
    }

    // 修改收款人手机号
    public Observable<PsnTransManagePayeeModifyMobileResult> psnTransManagePayeeModifyMobile(PsnTransManagePayeeModifyMobileParams params) {
        return BIIClient.instance.post("PsnTransManagePayeeModifyMobile", params, PsnTransManagePayeeModifyMobileResponse.class);
    }

    // 修改收款人别名
    public Observable<PsnTransManagePayeeModifyAliasResult> psnTransManagePayeeModifyAlias(PsnTransManagePayeeModifyAliasParams params) {
        return BIIClient.instance.post("PsnTransManagePayeeModifyAlias", params, PsnTransManagePayeeModifyAliasResponse.class);
    }

    // 删除收款人
    public Observable<PsnTransManagePayeeDeletePayeeResult> psnTransManagePayeeDeletePayee(PsnTransManagePayeeDeletePayeeParams params) {
        return BIIClient.instance.post("PsnTransManagePayeeDeletePayee", params, PsnTransManagePayeeDeletePayeeResponse.class);
    }

    // 实时付款保存收款人
    public Observable<PsnEbpsRealTimePaymentSavePayeeResult> psnEbpsRealTimePaymentSavePayee(PsnEbpsRealTimePaymentSavePayeeParams params) {
        return BIIClient.instance.post("PsnEbpsRealTimePaymentSavePayee", params, PsnEbpsRealTimePaymentSavePayeeResponse.class);
    }

    // 实时付款保存收款人
    public Observable<PsnQueryActTypeByActNumResult> psnQueryActTypeByActNum(PsnQueryActTypeByActNumParams params) {
        return BIIClient.instance.post("PsnQueryActTypeByActNum", params, PsnQueryActTypeByActNumResponse.class);
    }

    /**
     * 手机号转账预交易
     *
     * @param params
     * @return
     */
    public Observable<PsnMobileTransferPreResult> psnMobileTransferPre(PsnMobileTransferPreParam params) {
        return BIIClient.instance.post("PsnMobileTransferPre", params, PsnMobileTransferPreResponse.class);
    }

    /**
     * 手机号转账提交交易
     *
     * @param params
     * @return
     */
    public Observable<PsnMobileTransferSubmitResult> psnMobileTransferSubmit(PsnMobileTransferSubmitParam params) {
        return BIIClient.instance.post("PsnMobileTransferSubmit", params, PsnMobileTransferSubmitResponse.class);
    }

    /**
     * 批量主动收款催收短信
     *
     * @param params
     * @return
     */
    public Observable<PsnTransActReminderSmsResult> psnTransActReminderSms(PsnTransActReminderSmsParam params) {
        return BIIClient.instance.post("PsnTransActReminderSms", params, PsnTransActReminderSmsResponse.class);
    }

    public Observable<PsnQueryChinaBankAccountResult> psnQueryLinkedAccount(PsnQueryChinaBankAccountParams params) {
        return BIIClient.instance.post("PsnQueryChinaBankAccount", params, PsnQueryChinaBankAccountResponse.class);
    }

    /**
     * 通过卡号查询卡信息
     * @param params
     * @return
     */
    public Observable<PsnQueryBankInfobyCardBinResult> psnQueryBankInfobyCardBin(PsnQueryBankInfobyCardBinParams params) {
        return BIIClient.instance.post("PsnQueryBankInfobyCardBin", params, PsnQueryBankInfobyCardBinResponse.class);
    }
    /**
     * 通过zhanghuId查询电子卡绑定信息
     * @param params
     * @return
     */
    public Observable<PsnCardQueryBindInfoResult> psnCardQueryBindInfo(PsnCardQueryBindInfoParams params) {
        return BIIClient.instance.post("PsnCardQueryBindInfo", params, PsnCardQueryBindInfoResponse.class);
    }

    /**
     * 非交易时间转预约
     */
    public Observable<PsnTransNationalChangeBookingResult> psnTransChangeBooking(PsnTransNationalChangeBookingParams params) {
        return BIIClient.instance.post("PsnTransNationalChangeBooking", params, PsnTransNationalChangeBookingResponse.class);
    }

    /**
     * 批量主动收款预交易
     */
    public Observable<PsnBatchTransActCollectionVerifyResult> psnBatchTransActCollectionVerify(PsnBatchTransActCollectionVerifyParams params) {
        return BIIClient.instance.post("PsnBatchTransActCollectionVerify", params, PsnBatchTransActCollectionVerifyResponse.class);
    }

    /**
     * 批量主动收款提交
     */
    public Observable<PsnBatchTransActCollectionSubmitResult> psnBatchTransActCollectionSubmit(PsnBatchTransActCollectionSubmitParams params) {
        return BIIClient.instance.post("PsnBatchTransActCollectionSubmit", params, PsnBatchTransActCollectionSubmitResponse.class);
    }

    /**
     * 查询收款人列表（模糊查询)
     *
     * @param params
     * @return
     */
    public static Observable<PsnTransPayeeListqueryForDimResult> psnTransPayeeListqueryForDim(PsnTransPayeeListqueryForDimParams params) {

        return BIIClient.instance.post("PsnTransPayeeListqueryForDim", params, PsnTransPayeeListqueryForDimResponse.class);
    }
}
