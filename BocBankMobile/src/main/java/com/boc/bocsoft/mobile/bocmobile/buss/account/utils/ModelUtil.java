package com.boc.bocsoft.mobile.bocmobile.buss.account.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.boc.bocsoft.mobile.bii.bus.account.model.FactorAndCaResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.FactorListBean;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountModifyAccountAlias.PsnAccountModifyAccountAliasParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQryRecentTransDetail.PsnAccountQryRecentTransDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnApplyTermDepositeConfirm.PsnApplyTermDepositeConfirmParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnApplyTermDepositeResult.PsnApplyTermDepositeParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnApplyTermDepositeResult.PsnApplyTermDepositeResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdQueryVIRCardInfo.PsnCrcdQueryVIRCardInfoResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardApplyConfirm.PsnCrcdVirtualCardApplyConfirmParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardApplyInit.PsnCrcdVirtualCardApplyInitResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardApplySubmit.PsnCrcdVirtualCardApplySubmitParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardApplySubmit.PsnCrcdVirtualCardApplySubmitResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardCancel.PsnCrcdVirtualCardCancelParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardFunctionSetConfirm.PsnCrcdVirtualCardFunctionSetConfirmParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardFunctionSetSubmit.PsnCrcdVirtualCardFunctionSetSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardFunctionSetSubmit.PsnCrcdVirtualCardFunctionSetSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardQuery.PsnCrcdVirtualCardQueryParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardQuery.PsnCrcdVirtualCardQueryResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardQuery.VirCardInfo;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardSettledbillQuery.CrcdTransaction;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardSettledbillQuery.PsnCrcdVirtualCardSettledbillQueryParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardSettledbillQuery.PsnCrcdVirtualCardSettledbillQueryResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardUnsettledbillQuery.PsnCrcdVirtualCardUnsettledbillQueryParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardUnsettledbillSum.PsnCrcdVirtualCardUnsettledbillSumResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnDebitCardQryQuota.PsnDebitCardQryQuotaResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnDebitCardSetQuota.PsnDebitCardSetQuotaParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnDebitCardSetQuotaPre.PsnDebitCardSetQuotaPreParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICAccount.PsnFinanceICAccountResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICAccountDetail.PsnFinanceICAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICSignCancel.PsnFinanceICSignCancelParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICSignCreat.PsnFinanceICSignCreatParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICSignCreatRes.PsnFinanceICSignCreatResParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICTransfer.PsnFinanceICTransferParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICTransfer.PsnFinanceICTransferResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICTransferDetail.PsnFinanceICTransferDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICTransferDetail.TransDetail;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnICTransferNoRelevance.PsnICTransferNoRelevanceParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnICTransferNoRelevanceRes.PsnICTransferNoRelevanceResParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnICTransferNoRelevanceRes.PsnICTransferNoRelevanceResResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnMedicalInsurAcctDetailQuery.PsnMedicalInsurAcctDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayClosePre.PsnNcpayClosePreParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayCloseSubmit.PsnNcpayCloseSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayOpenPre.PsnNcpayOpenPreParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayOpenSubmit.PsnNcpayOpenSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayQuotaModifyPre.PsnNcpayQuotaModifyPreParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayQuotaModifySubmit.PsnNcpayQuotaModifySubmitParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayServiceChoose.PsnNcpayServiceChooseResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSVRCancelAccRelation.PsnSVRCancelAccRelationParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmQuery.PsnSsmQueryParam;
import com.boc.bocsoft.mobile.bii.bus.account.model.PublicParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PublicSecurityParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MapUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.model.AccountListItemViewModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.Content;
import com.boc.bocsoft.mobile.bocmobile.base.widget.transactionlist.TransactionBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.transactionlist.TransactionView;
import com.boc.bocsoft.mobile.bocmobile.buss.account.SecurityModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.apply.model.ApplyAccountModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model.FinanceModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model.TransDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model.TransferModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model.TransferResultModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.model.LimitModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.model.LimitType;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.model.QuotaModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.model.AccountInfoBean;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.model.MedicalModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.model.TermlyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.ui.MoreAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model.TransDetailViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.model.VirtualBillModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.model.VirtualBillTransModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.model.VirtualCardModel;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Item;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Menu;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleCode;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wangyang
 *         16/6/23 17:24
 *         接口Result转换界面Model与Model转换数据
 */
public class ModelUtil {

    public static final String PAGE_SIZE = "10";
    private static final int BEFORE_HALF_YEAR = 6;

    /***************************** 公共模块 *****************************/
    /**
     * 生成安全因子SecurityFactorModel
     *
     * @param securityFactorResult
     * @return
     */
    public static SecurityFactorModel generateSecurityFactorModel(PsnGetSecurityFactorResult securityFactorResult) {
        SecurityFactorModel securityFactorModel = new SecurityFactorModel(securityFactorResult);
        return securityFactorModel;
    }

    /**
     * 生成安全因子SecurityModel,预交易返回的安全因子
     *
     * @param result
     * @return
     */
    public static SecurityModel generateSecurityModel(FactorAndCaResult result) {
        SecurityModel securityModel = new SecurityModel();
        securityModel.setCertDN(result.getCertDN());
        securityModel.setPlainData(result.get_plainData());
        securityModel.setSmcTrigerInterval(result.getSmcTrigerInterval());
        //生成Factor
        for (FactorListBean factorListBean : result.getFactorList())
            securityModel.addFactorModel(generateFactorModel(factorListBean));

        return securityModel;
    }

    private static com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean generateFactorModel(FactorListBean factorListBean) {
        com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean factorModel = new com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean();
        com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean.FieldBean fieldBean = new com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean.FieldBean();
        fieldBean.setType(factorListBean.getField().getType());
        fieldBean.setName(factorListBean.getField().getName());
        factorModel.setField(fieldBean);
        return factorModel;
    }

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

    /**
     * 预交易参数
     *
     * @param params
     * @param conversationId
     * @param token
     * @param factorId
     */
    private static void setPreTransactionPublicParams(PublicParams params, String conversationId, String token, String factorId) {
        params.setConversationId(conversationId);
        params.setToken(token);
        params.set_combinId(factorId);
    }

    /**
     * @param params
     * @param conversationId
     * @param token
     * @param factorId
     * @param deviceInfoModel
     * @param randomNums
     * @param encryptPasswords
     */
    private static void setPublicSecurityParams(PublicSecurityParams params, String conversationId, String token, String factorId, DeviceInfoModel deviceInfoModel, String[] randomNums, String[] encryptPasswords) {
        setPreTransactionPublicParams(params, conversationId, token, factorId);

        switch (Integer.parseInt(factorId)) {
            //只传Smc
            case SecurityVerity.SECURITY_VERIFY_SMS:
                params.setSmc(encryptPasswords[0]);
                params.setSmc_RC(randomNums[0]);
                break;
            //只传Otp
            case SecurityVerity.SECURITY_VERIFY_TOKEN:
                params.setOtp(encryptPasswords[0]);
                params.setOtp_RC(randomNums[0]);
                break;
            //传Otp和Smc
            case SecurityVerity.SECURITY_VERIFY_SMS_AND_TOKEN:
                params.setOtp(encryptPasswords[0]);
                params.setOtp_RC(randomNums[0]);
                params.setSmc(encryptPasswords[1]);
                params.setSmc_RC(randomNums[1]);
                break;
            //只传Smc
            case SecurityVerity.SECURITY_VERIFY_DEVICE:
                params.setSmc(encryptPasswords[0]);
                params.setSmc_RC(randomNums[0]);

                //deviceInfo
                params.setDeviceInfo(deviceInfoModel.getDeviceInfo());
                params.setDeviceInfo_RC(deviceInfoModel.getDeviceInfo_RC());
                break;
            case SecurityVerity.SECURITY_VERIFY_E_TOKEN:
                params.set_signedData(randomNums[0]);
                break;
        }
    }

    /***************************** 账户概览 *****************************/
    /**
     * 生成AccountListItemViewModel
     *
     * @param context
     * @param accountList
     * @return
     */
    public static List<AccountListItemViewModel> generateAccountListItemViewModels(Context context, List<AccountBean> accountList) {
        List<AccountListItemViewModel> models = new ArrayList<>();
        for (AccountBean accountBean : accountList) {
            AccountListItemViewModel model = getAccountListItemViewModel(context, accountBean);

            models.add(model);
        }
        return models;
    }

    @NonNull
    public static AccountListItemViewModel getAccountListItemViewModel(Context context, AccountBean accountBean) {
        AccountListItemViewModel model = new AccountListItemViewModel();
        model.setAccountBean(accountBean);

        String title;
        switch (accountBean.getAccountType()) {
            //信用卡,显示为为可用额度
            case ApplicationConst.ACC_TYPE_ZHONGYIN:
            case ApplicationConst.ACC_TYPE_GRE:
            case ApplicationConst.ACC_TYPE_SINGLEWAIBI:
                title = context == null ? "实时余额" : context.getString(R.string.boc_overview_item_amount_title_credit);
                break;
            //电子现金账户,显示补登余额
            case ApplicationConst.ACC_TYPE_ECASH:
                title = context == null ? "补登余额" : context.getString(R.string.boc_finance_account_balance_title);
                break;
            //其他显示为为账面余额
            default:
                title = context == null ? "账面余额" : context.getString(R.string.boc_overview_item_amount_title_account);
                break;
        }
        model.setAmountTitle(title);
        return model;
    }

    /**
     * 生成有余额信息的AccountListItemViewModel 普通账户
     *
     * @param accountBean
     * @param result
     * @return
     */
    public static AccountListItemViewModel generateAccountListItemViewModel(AccountBean accountBean, PsnAccountQueryAccountDetailResult result) {
        AccountListItemViewModel model = getAccountListItemViewModel(null, accountBean);

        if (result == null)
            return model;

        model.getAccountBean().setAccountStatus(result.getAccountStatus());
        model.setAccOpenBank(result.getAccOpenBank());
        model.setAccOpenDate(result.getAccOpenDate());

        List<AccountListItemViewModel.CardAmountViewModel> cardModels = new ArrayList<>();
        for (PsnAccountQueryAccountDetailResult.AccountDetaiListBean detailBean : result.getAccountDetaiList()) {
            AccountListItemViewModel.CardAmountViewModel cardModel = new AccountListItemViewModel.CardAmountViewModel();
            cardModel.setCurrencyCode(detailBean.getCurrencyCode());
            cardModel.setCashRemit(detailBean.getCashRemit());
            cardModel.setBookBalance(detailBean.getBookBalance().toString());
            cardModel.setAmount(detailBean.getAvailableBalance().toString());
            cardModel.setAccountType(accountBean.getAccountType());
            cardModels.add(cardModel);
        }
        model.setCardAmountViewModelList(cardModels);
        return model;
    }

    /**
     * 生成查询信用卡详情参数
     *
     * @param accountId
     * @param code
     * @return
     */
    public static PsnCrcdQueryAccountDetailParams generateCrcdQueryAccountDetailParams(String accountId, String code) {
        PsnCrcdQueryAccountDetailParams params = new PsnCrcdQueryAccountDetailParams();
        params.setAccountId(accountId);
        params.setCurrency(code);
        return params;
    }

    /**
     * 生成有余额信息的AccountListItemViewModel 信用卡
     *
     * @param accountBean
     * @param results
     * @return
     */
    public static AccountListItemViewModel generateAccountListItemViewModel(AccountBean accountBean, List<PsnCrcdQueryAccountDetailResult> results) {
        AccountListItemViewModel model = getAccountListItemViewModel(null, accountBean);

        if (results == null || results.isEmpty())
            return model;

        List<AccountListItemViewModel.CardAmountViewModel> cardModels = new ArrayList<>();
        for (PsnCrcdQueryAccountDetailResult result : results) {
            for (PsnCrcdQueryAccountDetailResult.CrcdAccountDetailListBean detailBean : result.getCrcdAccountDetailList()) {
                AccountListItemViewModel.CardAmountViewModel cardModel = new AccountListItemViewModel.CardAmountViewModel();
                cardModel.setCurrencyCode(detailBean.getCurrency());
                cardModel.setBookBalance(detailBean.getLoanBalanceLimit().toString());
                cardModel.setAccountType(accountBean.getAccountType());
                cardModel.setLoanBalanceLimitFlag(detailBean.getLoanBalanceLimitFlag());
                cardModels.add(cardModel);
            }
        }
        model.setCardAmountViewModelList(cardModels);
        return model;
    }

    /**
     * 生成跳转联龙信用卡界面参数
     *
     * @return
     */
    public static List<Map<String, Object>> generateCredit2Map() {
        List<Map<String, Object>> list = new ArrayList<>();

        for (AccountBean accountBean : ApplicationContext.getInstance().getChinaBankAccountList(AccountTypeUtil.getCreditType()))
            list.add(MapUtils.clzzField2Map(accountBean));

        return list;
    }

    /**
     * 生成有余额信息的AccountListItemViewModel 电子现金账户
     *
     * @param accountBean
     * @param result
     * @return
     */
    public static AccountListItemViewModel generateAccountListItemViewModel(AccountBean accountBean, PsnFinanceICAccountDetailResult result) {
        AccountListItemViewModel model = getAccountListItemViewModel(null, accountBean);

        if (result == null)
            return model;

        model.getAccountBean().setAccountStatus(result.getStatus());

        //补登余额不为空,设置补登余额
        if (result.getSupplyBalance() != null) {
            List<AccountListItemViewModel.CardAmountViewModel> amountModels = new ArrayList<>();

            //设置补登余额,币种
            AccountListItemViewModel.CardAmountViewModel amountModel = new AccountListItemViewModel.CardAmountViewModel();
            amountModel.setBookBalance(result.getSupplyBalance().toString());
            amountModel.setCurrencyCode(ApplicationConst.CURRENCY_CNY);
            amountModels.add(amountModel);

            //添加至AccountListItemViewModel
            model.setCardAmountViewModelList(amountModels);
        }
        return model;
    }

    /**
     * 生成电子现金账户信息
     *
     * @param accountBean
     * @param accountBean
     * @return
     */
    public static FinanceModel generateFinanceModel(AccountBean accountBean) {
        FinanceModel financeModel = new FinanceModel();
        financeModel.setAccountId(accountBean.getAccountId());
        financeModel.setFinanICNumber(accountBean.getAccountNumber());
        financeModel.setNickName(accountBean.getNickName());
        financeModel.setCurrency(accountBean.getCurrencyCode());
        financeModel.setFinanICType(accountBean.getAccountType());
        financeModel.setAccountState(accountBean.getAccountStatus());

        return financeModel;
    }

    /**
     * 根据交易明细生成TransactionView需要的TransactionBean
     *
     * @param result
     * @return
     */
    public static List<TransactionBean> generateTransactionBean(PsnAccountQryRecentTransDetailResult result) {
        List<TransactionBean> transactionBeans = null;

        if (result == null || result.getRecordNumber() == 0)
            return transactionBeans;

        transactionBeans = new ArrayList<>();
        for (PsnAccountQryRecentTransDetailResult.TransDetail transDetail : result.getTransDetails()) {
            TransactionBean transactionBean = new TransactionBean();
            transactionBean.setTitleID(TransactionView.TITLE_DATE_TYPE1);
            transactionBean.setChangeColor(true);
            transactionBean.setTime(transDetail.getPaymentDate());
            transactionBean.setContentLeftAbove(transDetail.getBusinessDigest());
            transactionBean.setContentRightBelow(PublicCodeUtils.getCurrency(ActivityManager.getAppManager().currentActivity(), transDetail.getCurrency()));
            transactionBean.setContentRightAbove(MoneyUtils.transMoneyFormat(transDetail.getAmount().toString().trim(), transDetail.getCurrency()));

            transactionBeans.add(transactionBean);
        }
        return transactionBeans;
    }

    /**
     * 转换为交易明细界面需要的ListBean
     *
     * @param transDetail
     * @return
     */
    public static TransDetailViewModel.ListBean generateTransDetailListBean(PsnAccountQryRecentTransDetailResult.TransDetail transDetail) {
        TransDetailViewModel.ListBean detail = new TransDetailViewModel.ListBean();
        detail.setAmount(transDetail.getAmount().toString());
        detail.setBalance(transDetail.getBalance().toString());
        detail.setBusinessDigest(transDetail.getBusinessDigest());
        detail.setCashRemit(transDetail.getCashRemit());
        detail.setChargeBack(transDetail.isChargeBack());
        detail.setChnlDetail(transDetail.getChnlDetail());
        detail.setCurrency(transDetail.getCurrency());
        detail.setFurInfo(transDetail.getFurInfo());
        detail.setPayeeAccountName(transDetail.getPayeeAccountName());
        detail.setPayeeAccountNumber(transDetail.getPayeeAccountNumber());
        detail.setPaymentDate(transDetail.getPaymentDate());
        detail.setTransChnl(transDetail.getTransChnl());
        return detail;
    }

    /**
     * 生成TermlyViewModel
     *
     * @param accountBean
     * @param result
     * @return
     */
    public static List<TermlyViewModel> generateTermlyViewModels(AccountBean accountBean, PsnAccountQueryAccountDetailResult result) {
        List<TermlyViewModel> models = null;

        if (result == null)
            return models;

        accountBean.setAccountStatus(result.getAccountStatus());
        models = new ArrayList<>();
        for (PsnAccountQueryAccountDetailResult.AccountDetaiListBean detailBean : result.getAccountDetaiList()) {
            TermlyViewModel model = new TermlyViewModel();
            model.setAccountId(accountBean.getAccountId());
            model.setType(detailBean.getType());
            model.setStatus(detailBean.getStatus());
            model.setAvailableBalance(detailBean.getAvailableBalance());
            model.setCashRemit(detailBean.getCashRemit());
            model.setCdPeriod(detailBean.getCdPeriod());
            model.setCurrencyCode(detailBean.getCurrencyCode());
            model.setInterestStartsDate(detailBean.getInterestStartsDate());
            model.setInterestEndDate(detailBean.getInterestEndDate());
            model.setInterestRate(detailBean.getInterestRate());
            model.setOpenDate(detailBean.getOpenDate());
            model.setConvertType(detailBean.getConvertType());
            model.setVolumeNumber(detailBean.getVolumeNumber());
            model.setCdNumber(detailBean.getCdNumber());
            model.setPingNo(detailBean.getPingNo());
            model.setSettlementDate(detailBean.getSettlementDate());
            model.setAppointStatus(detailBean.getAppointStatus());
            models.add(model);
        }
        return models;
    }

    /**
     * 生成开户行信息
     *
     * @param result
     * @return
     */
    public static AccountInfoBean generateAccountInfoBean(PsnAccountQueryAccountDetailResult result) {
        AccountInfoBean accountInfoBean = new AccountInfoBean();
        accountInfoBean.setAccOpenBank(result.getAccOpenBank());
        accountInfoBean.setAccOpenDate(result.getAccOpenDate());

        if (ApplicationConst.ACC_TYPE_ORD.equals(result.getAccountType()))
            accountInfoBean.setCashRemit(result.getAccountDetaiList().get(0).getCashRemit());
        return accountInfoBean;
    }

    /**
     * 生成开户行信息
     *
     * @param itemViewModel
     * @return
     */
    public static AccountInfoBean generateAccountInfoBean(AccountListItemViewModel itemViewModel) {
        AccountInfoBean accountInfoBean = new AccountInfoBean();
        accountInfoBean.setAccOpenBank(itemViewModel.getAccOpenBank());
        accountInfoBean.setAccOpenDate(itemViewModel.getAccOpenDate());

        if (ApplicationConst.ACC_TYPE_ORD.equals(itemViewModel.getAccountBean().getAccountType()) && itemViewModel.getCardAmountViewModelList() != null && !itemViewModel.getCardAmountViewModelList().isEmpty())
            accountInfoBean.setCashRemit(itemViewModel.getCardAmountViewModelList().get(0).getCashRemit());
        return accountInfoBean;
    }

    /**
     * 获取账户概览更多界面Model
     *
     * @return
     */
    public static List<Item> generateOverviewMoreItems() {
        List<Item> items = new ArrayList<>();

        Menu menu = ApplicationContext.getInstance().getMenu();
        //交易明细
        items.add(menu.findItemById(ModuleCode.MODULE_ACCOUNT_0200));
        //冻结/挂失
        items.add(menu.findItemById(ModuleCode.MODULE_ACCOUNT_0500));
        //申请定期/活期
        items.add(menu.findItemById(ModuleCode.MODULE_ACCOUNT_0300));
        //账户动户通知
        items.add(menu.findItemById(ModuleCode.MODULE_ACCOUNT_0400));
        //我的电子现金账户
        items.add(menu.findItemById(ModuleCode.MODULE_ACCOUNT_0700));
        //虚拟银行卡
        items.add(menu.findItemById(ModuleCode.MODULE_ACCOUNT_0900));
        //医保账户
        items.add(menu.findItemById(ModuleCode.MODULE_ACCOUNT_1000));
        return items;
    }

    /**
     * 获取账户更多列表
     *
     * @param accountBean
     * @return
     */
    public static List<Item> generateAccountMoreItems(Resources res, AccountBean accountBean) {
        List<Item> items = new ArrayList<>();
        //账户详情
        items.add(new Item(res.getString(R.string.boc_account_detail), MoreAccountFragment.MORE_DETAIL));
        //申请定期账户
        if (ApplicationConst.ACC_TYPE_BRO.equals(accountBean.getAccountType()))
            items.add(new Item(res.getString(R.string.boc_account_detail_apply), MoreAccountFragment.MORE_APPLY));
        //冻结/挂失
        if (AccountTypeUtil.getCanFrozen().contains(accountBean.getAccountType()))
            items.add(new Item(res.getString(R.string.boc_overview_account_more_frozen), MoreAccountFragment.MORE_FROZEN));
        //显示完整账号
        if (!ApplicationConst.ACC_TYPE_BOCINVT.equals(accountBean.getAccountType()))
            items.add(new Item(res.getString(R.string.boc_overview_account_more_full), MoreAccountFragment.MORE_FULL));
        //账户动户通知
        if (AccountTypeUtil.getCanNotify().contains(accountBean.getAccountType()))
            items.add(new Item(res.getString(R.string.boc_account_title), MoreAccountFragment.MORE_NOTIFY));
        //取消关联
        items.add(new Item(res.getString(R.string.boc_overview_account_more_cancel_relation), MoreAccountFragment.MORE_CANCEL));
        //限额设置
        items.add(new Item(res.getString(R.string.boc_overview_account_more_limit), MoreAccountFragment.MORE_LIMIT));

        return items;
    }

    public static PsnSsmQueryParam generatepsnSsmQueryParams(String accountId, String conversationId) {
        PsnSsmQueryParam param = new PsnSsmQueryParam();
        param.setAccountId(accountId);
        param.setConversationId(conversationId);
        return param;
    }

    /**
     * 生成修改账户别名请求参数
     *
     * @param conversationId
     * @param token
     * @param accountId
     * @param nickName
     * @return
     */
    public static PsnAccountModifyAccountAliasParams generateModifyAccountAliasParams(String conversationId, String token, String accountId, String nickName) {
        PsnAccountModifyAccountAliasParams params = new PsnAccountModifyAccountAliasParams();
        setPublicParamsWithOutSecurity(params, conversationId, token);
        params.setAccountId(accountId);
        params.setAccountNickName(nickName);
        return params;
    }

    /**
     * 生成医保账户Model
     *
     * @param accountId
     * @param result
     * @return
     */
    public static MedicalModel generateMedicalModel(String accountId, PsnMedicalInsurAcctDetailQueryResult result) {
        MedicalModel medicalModel = new MedicalModel();
        medicalModel.setAccountId(accountId);
        medicalModel.setAccOpenBank(result.getAccOpenBank());
        medicalModel.setAccOpenDate(result.getAccOpenDate());
        medicalModel.setAccountStatus(result.getAccountStatus());
        medicalModel.setAccountType(result.getAccountType());

        List<MedicalModel.MedicalDetail> medicalDetailList = new ArrayList<>();
        for (int i = 0; i < result.getAccountDetaiList().size(); i++) {
            MedicalModel.MedicalDetail medicalDetail = new MedicalModel.MedicalDetail();
            medicalDetail.setCurrencyCode(result.getAccountDetaiList().get(i).getCurrencyCode());
            medicalDetail.setCashRemit(result.getAccountDetaiList().get(i).getCashRemit());
            medicalDetail.setBookBalance(result.getAccountDetaiList().get(i).getBookBalance());
            medicalDetail.setAvailableBalance(result.getAccountDetaiList().get(i).getAvailableBalance());
            medicalDetail.setOpenDate(result.getAccountDetaiList().get(i).getOpenDate());
            medicalDetail.setStatus(result.getAccountDetaiList().get(i).getStatus());
            medicalDetail.setSettlementDate(result.getAccountDetaiList().get(i).getSettlementDate());
            medicalDetail.setHoldAmount(result.getAccountDetaiList().get(i).getHoldAmount());
            medicalDetailList.add(medicalDetail);
        }
        medicalModel.setMedicalDetailList(medicalDetailList);
        return medicalModel;
    }

    /**
     * 根据电子现金账户查询AccountBean
     *
     * @param financeModel
     * @return
     */
    public static AccountBean generateAccountBean(FinanceModel financeModel) {
        AccountBean findBean = new AccountBean();
        findBean.setAccountId(financeModel.getAccountId());
        List<AccountBean> accountBeans = ApplicationContext.getInstance().getChinaBankAccountList(null);
        int index = accountBeans.indexOf(findBean);

        findBean = accountBeans.get(index);
        findBean.setAccountStatus(financeModel.getStatus());
        return findBean;
    }

    /**
     * 生成虚拟卡详情
     *
     * @param accountBean
     * @param result
     * @return
     */
    public static VirtualCardModel generateVirtualCardModel(AccountBean accountBean, PsnCrcdQueryVIRCardInfoResult result) {
        VirtualCardModel model = new VirtualCardModel();
        model.setAccountId(accountBean.getAccountId());
        model.setVirAccountId(accountBean.getAccountId());
        model.setCurrencyCode(ApplicationConst.CURRENCY_CNY);
        model.setAccountName(accountBean.getAccountName());
        model.setAccountIbkNum(accountBean.getAccountNumber());
        model.setAccountType(accountBean.getAccountType());
        model.setNickName(accountBean.getNickName());
        if (result != null) {
            model.setAccountIbkNum(result.getVirtualCardNo());
            model.setAccountNumber(result.getCreditCardNo());
            model.setStartDate(result.getStartDate());
            model.setEndDate(result.getEndDate());
            model.setAccountStatus(result.getvCardStatus());
            model.setSignleLimit(result.getSingleLimit());
            model.setTotalLimit(result.getTotalLimit());
            model.setAtotalLimit(result.getaTotalLimit());
            model.setChannel(result.getApplyChannel());
        }
        return model;
    }

    /**
     * 生成取消关联虚拟卡AccountBean
     *
     * @param model
     * @return
     */
    public static AccountBean generateAccountBean(VirtualCardModel model) {
        AccountBean accountBean = new AccountBean();
        accountBean.setAccountId(model.getVirAccountId());
        accountBean.setAccountNumber(model.getAccountIbkNum());
        accountBean.setAccountType(model.getAccountType());
        return accountBean;
    }

    /***************************** 电子现金账户 *****************************/
    /**
     * 生成FinanceModel,设置账户账号,别名,币种
     *
     * @param results
     * @return
     */
    public static List<FinanceModel> generateFinanceModel(List<PsnFinanceICAccountResult> results) {
        List<FinanceModel> financeModelList = new ArrayList<>();
        for (PsnFinanceICAccountResult result : results) {
            FinanceModel financeModel = new FinanceModel();
            financeModel.setAccountId(result.getAccountId());
            financeModel.setFinanICNumber(result.getFinanICNumber());
            financeModel.setNickName(result.getNickName());
            financeModel.setCurrency(result.getCurrencyCode());
            financeModel.setFinanICType(result.getFinanICType());
            financeModelList.add(financeModel);
        }
        return financeModelList;
    }


    /**
     * 生成FinanceModel,设置账户状态,电子现金账户余额,补登账户余额,单笔金额上限,卡片余额上限
     *
     * @param financeModel
     * @param detailResult
     * @return
     */
    public static FinanceModel generateFinanceModel(FinanceModel financeModel, PsnFinanceICAccountDetailResult detailResult) {
        if (financeModel == null)
            return null;

        financeModel.setAccountState(detailResult.getStatus());
        financeModel.seteCashUpperLimit(detailResult.geteCashUpperLimit());
        financeModel.setSingleLimit(detailResult.getSingleLimit());
        financeModel.setTotalBalance(detailResult.getTotalBalance());
        financeModel.setSupplyBalance(detailResult.getSupplyBalance());
        financeModel.setMaxLoadingAmount(detailResult.getMaxLoadingAmount());
        return financeModel;
    }

    /**
     * 生成AccountListItemView需要的Model
     *
     * @param financeModel
     * @return
     */
    public static AccountListItemViewModel generateAccountListItemViewModel(FinanceModel financeModel) {
        AccountListItemViewModel model = new AccountListItemViewModel();
        AccountBean accountBean = new AccountBean();
        //设置账户账号,别名,余额标题
        accountBean.setAccountNumber(financeModel.getFinanICNumber());
        accountBean.setAccountName(financeModel.getNickName());
        accountBean.setNickName(financeModel.getNickName());
        accountBean.setAccountType(financeModel.getFinanICType());
        model.setAccountBean(accountBean);
        model.setAmountTitle("补登余额");

        //补登余额不为空,设置补登余额
        if (financeModel.getSupplyBalance() != null) {
            List<AccountListItemViewModel.CardAmountViewModel> amountModels = new ArrayList<>();

            //设置补登余额,币种
            AccountListItemViewModel.CardAmountViewModel amountModel = new AccountListItemViewModel.CardAmountViewModel();
            amountModel.setBookBalance(financeModel.getSupplyBalance().toString());
            amountModel.setCurrencyCode(financeModel.getCurrency());
            amountModels.add(amountModel);

            //添加至AccountListItemViewModel
            model.setCardAmountViewModelList(amountModels);
        }
        return model;
    }

    /**
     * 生成查询电子现金交易明细参数
     *
     * @param accountId
     * @return
     */
    public static PsnFinanceICTransferDetailParams generatePsnFinanceICTransferDetailParams(String accountId, String conversationId) {
        PsnFinanceICTransferDetailParams params = new PsnFinanceICTransferDetailParams(accountId);
        String endDate = ApplicationContext.getInstance().getCurrentSystemDate().format(DateFormatters.dateFormatter1);
        String startDate = ApplicationContext.getInstance().getCurrentSystemDate().minusMonths(3).format(DateFormatters.dateFormatter1);
        params.setStartDate(startDate);
        params.setEndDate(endDate);
        params.setCurrentIndex(1);
        params.setConversationId(conversationId);
        return params;
    }

    /**
     * 根据交易明细生成TransactionView需要的TransactionBean
     *
     * @param transDetail
     * @return
     */
    public static TransactionBean generateTransactionBean(TransDetail transDetail) {
        TransactionBean transactionBean = new TransactionBean();
        transactionBean.setTitleID(TransactionView.TITLE_DATE_TYPE);
        transactionBean.setChangeColor(true);
        transactionBean.setContentLeftAbove(transDetail.getTransferTypeString());

        //设置收入/支出金额
        String content = "";
        if (transDetail.isAmountFlag())
            content = content + "-";
        content = content + MoneyUtils.transMoneyFormat(transDetail.getAmount(), ApplicationConst.CURRENCY_CNY);
        transactionBean.setContentRightAbove(content);

        //将交易时间转换为LocalDate
        transactionBean.setTime(transDetail.getReturnDate());
        return transactionBean;
    }

    /**
     * 生成交易明细Model 金额,转入/转出标识,币种,交易日期,业务摘要
     *
     * @param transDetail
     * @return
     */
    public static TransDetailModel generateTransDetailModel(TransDetail transDetail) {
        TransDetailModel detailModel = new TransDetailModel();
        detailModel.setAmount(transDetail.getAmount());
        detailModel.setAmountFlag(transDetail.isAmountFlag());
        detailModel.setCurrency(transDetail.getCurrency());
        detailModel.setReturnDate(transDetail.getReturnDate());
        detailModel.setTransferType(transDetail.getTransferType());
        return detailModel;
    }

    /**
     * 根据交易信息与Token生成充值参数
     *
     * @param transferModel
     * @param token
     * @return
     */
    public static PsnFinanceICTransferParams generateTransferparams(TransferModel transferModel, String conversationId, String token) {
        PsnFinanceICTransferParams params = new PsnFinanceICTransferParams();
        params.setBankAccountId(Integer.parseInt(transferModel.getBankAccountId()));
        params.setFinanceICAccountId(transferModel.getFinanceICAccountId());
        params.setAmount(Double.parseDouble(transferModel.getAmount()));
        params.setConversationId(conversationId);
        params.setCashRemit(ApplicationConst.CURRENCY_CNY);
        params.setToken(token);
        return params;
    }

    /**
     * 生成给自己充值结果Model 网银交易序号,转出账户,转入账户,金额
     *
     * @param result
     * @return
     */
    public static TransferResultModel generateTransSelfResultModel(PsnFinanceICTransferResult result) {
        if (result == null)
            return null;

        TransferResultModel model = new TransferResultModel();
        model.setTransactionId(result.getTransactionId());
        model.setBankAccountNum(result.getBankAccountNum());
        model.setIcCardNum(result.getIcCardNum());
        model.setAmount(result.getAmount());
        model.setStatus(result.getStatus());
        model.setSelf(true);
        return model;
    }

    /**
     * 生成给他人充值结果Model 网银交易序号,转出账户,转入账户,金额
     *
     * @param result
     * @return
     */
    public static TransferResultModel generateTransOtherResultModel(PsnICTransferNoRelevanceResResult result) {
        TransferResultModel model = new TransferResultModel();
        model.setTransactionId(result.getTransactionId());
        model.setBankAccountNum(result.getBankAccountNum());
        model.setIcCardNum(result.getIcCardNum());
        model.setAmount(result.getAmount());
        model.setStatus(result.getStatus());
        model.setSelf(false);
        return model;
    }

    /**
     * 生成生成给他人充值预交易参数
     *
     * @param model
     * @param conversationId
     * @param token
     * @param combinId       @return
     */
    public static PsnICTransferNoRelevanceParams generateTransferOtherPreParams(TransferModel model, String conversationId, String token, String combinId) {
        PsnICTransferNoRelevanceParams params = new PsnICTransferNoRelevanceParams();
        params.setBankAccountId(Integer.parseInt(model.getBankAccountId()));
        params.setFinanceICAccountNumber(model.getFinanceICNumber());
        params.setPayeeName(model.getPayeeName());
        params.setAmount(Double.parseDouble(model.getAmount()));
        setPreTransactionPublicParams(params, conversationId, token, combinId);
        return params;
    }

    /**
     * 生成给他人充值交易参数
     *
     * @param model
     * @param conversationId
     * @param token            @return
     * @param factorId
     * @param deviceInfoModel
     * @param randomNums
     * @param encryptPasswords
     */
    public static PsnICTransferNoRelevanceResParams generateTransferOtherParams(TransferModel model, String conversationId, String token, String factorId, DeviceInfoModel deviceInfoModel, String[] randomNums, String[] encryptPasswords) {
        PsnICTransferNoRelevanceResParams params = new PsnICTransferNoRelevanceResParams();
        setPublicSecurityParams(params, conversationId, token, factorId, deviceInfoModel, randomNums, encryptPasswords);

        params.setBankAccountId(Integer.parseInt(model.getBankAccountId()));
        params.setFinanceICAccountNumber(model.getFinanceICNumber());
        params.setPayeeName(model.getPayeeName());
        params.setAmount(model.getAmount());
        return params;
    }

    /**
     * 生成一个只有FinanICNumber的FinanceModel,因为FinanICNumber是FinanceModel的唯一标识
     *
     * @param result
     * @return
     */
    public static FinanceModel generateFinanceModelOnlyICNumber(PsnFinanceICAccountDetailResult result) {
        if (result == null)
            return null;

        FinanceModel financeModel = new FinanceModel();
        financeModel.setFinanICNumber(result.getCardNumber());
        return financeModel;
    }

    /**
     * 生成充值转入账户信息
     *
     * @param financeModel
     * @return
     */
    public static AccountBean generateIncomeAccountBean(FinanceModel financeModel) {
        AccountBean accountBean = new AccountBean();
        accountBean.setAccountId(financeModel.getAccountId());
        accountBean.setAccountName(financeModel.getNickName());
        accountBean.setNickName(financeModel.getNickName());
        accountBean.setAccountType(financeModel.getFinanICType());
        accountBean.setAccountNumber(financeModel.getFinanICNumber());
        accountBean.setAccountStatus(financeModel.getAccountState());
        return accountBean;
    }

    /**
     * 生成给他人充值Model
     *
     * @param expendAccount
     * @param financeICNumber
     * @param payeeName
     * @param amount
     * @return
     */
    public static TransferModel generateTransferModelOther(AccountBean expendAccount, String financeICNumber, String payeeName, String amount) {
        TransferModel transferModel = new TransferModel();
        transferModel.setBankAccountId(expendAccount.getAccountId());
        transferModel.setBankAccountNumber(expendAccount.getAccountNumber());
        transferModel.setFinanceICNumber(financeICNumber);
        transferModel.setPayeeName(payeeName);
        transferModel.setAmount(amount);
        transferModel.setSelf(false);
        return transferModel;
    }

    /**
     * 给自己充值Model
     *
     * @param expendAccount
     * @param financeModel
     * @param amount
     * @return
     */
    public static TransferModel generateTransferModelSelf(AccountBean expendAccount, FinanceModel financeModel, String amount) {
        TransferModel transferModel = new TransferModel();
        transferModel.setBankAccountId(expendAccount.getAccountId());
        transferModel.setBankAccountNumber(expendAccount.getAccountNumber());
        transferModel.setFinanceICAccountId(financeModel.getAccountId());
        transferModel.setFinanceICNumber(financeModel.getFinanICNumber());
        transferModel.setAmount(amount);
        transferModel.setSelf(true);
        return transferModel;
    }

    /**
     * 生成签约预交易参数
     *
     * @param financeModel
     * @param conversationId
     * @param token
     * @param factorId       @return
     */
    public static PsnFinanceICSignCreatParams generatePsnFinanceICSignCreatParams(FinanceModel financeModel, String conversationId, String token, String factorId) {
        PsnFinanceICSignCreatParams params = new PsnFinanceICSignCreatParams();
        setPreTransactionPublicParams(params, conversationId, token, factorId);
        params.setFinanceICAccountId(financeModel.getAccountId());
        params.setBankAccountId(financeModel.getBankAccountId());
        params.setBankAccountNumber(financeModel.getBankAccountNumber());
        params.setFinanceICAccountNumber(financeModel.getFinanICNumber());
        return params;
    }

    /**
     * 生成新建签约关系参数
     *
     * @param financeModel
     * @param conversationId
     * @param token
     * @param deviceInfoModel
     * @param factorId
     * @param randomNums
     * @param encryptPasswords
     * @return
     */
    public static PsnFinanceICSignCreatResParams generatePsnFinanceICSignCreatResParams(FinanceModel financeModel, String conversationId, String token, DeviceInfoModel deviceInfoModel, String factorId, String[] randomNums, String[] encryptPasswords) {
        PsnFinanceICSignCreatResParams params = new PsnFinanceICSignCreatResParams();
        params.setFinanceICAccountId(financeModel.getAccountId());
        params.setBankAccountId(financeModel.getBankAccountId());
        params.setBankAccountNumber(financeModel.getBankAccountNumber());
        params.setFinanceICAccountNumber(financeModel.getFinanICNumber());
        setPublicSecurityParams(params, conversationId, token, factorId, deviceInfoModel, randomNums, encryptPasswords);
        return params;
    }

    /**
     * 生成删除签约关系参数
     *
     * @param financeModel
     * @param conversationId
     * @param token
     * @return
     */
    public static PsnFinanceICSignCancelParams generatePsnFinanceICSignCancelParams(FinanceModel financeModel, String conversationId, String token) {
        PsnFinanceICSignCancelParams params = new PsnFinanceICSignCancelParams();
        params.setToken(token);
        params.setFinanceICAccountNumber(financeModel.getFinanICNumber());
        params.setConversationId(conversationId);
        List<AccountBean> accountBeanList = ApplicationContext.getInstance().getChinaBankAccountList(null);
        for (AccountBean accountBean : accountBeanList) {
            if (accountBean.getAccountNumber().equals(financeModel.getBankAccountNumber())) {
                params.setBankAccountId(accountBean.getAccountId());
                break;
            }
        }
        return params;
    }

    /***************************** 取消关联 *****************************/
    /**
     * 生成取消关联请求参数
     *
     * @param accountBean
     * @param conversationId
     * @param token          @return
     */
    public static PsnSVRCancelAccRelationParams generateRelationParams(AccountBean accountBean, String conversationId, String token) {
        PsnSVRCancelAccRelationParams params = new PsnSVRCancelAccRelationParams();
        params.setToken(token);
        params.setAccountId(accountBean.getAccountId());
        params.setConversationId(conversationId);
        params.setAccountNumber(accountBean.getAccountNumber());
        return params;
    }

    /***************************** 申请定期/活期 ******************************/

    /**
     * 生成账户用途
     *
     * @param context
     * @return
     */
    public static List<Content> generatePurposeContent(Context context) {
        List<Content> purposeList = new ArrayList<>();
        Map<String, String> accountPurposeMap = PublicCodeUtils.getAccountPurposeMap(context);
        for (Map.Entry<String, String> entry : accountPurposeMap.entrySet()) {
            Content content = new Content();
            content.setContentNameID(entry.getKey());
            content.setName(entry.getValue());

            //默认选中储蓄
            if (content.getContentNameID().equals("01"))
                content.setSelected(true);

            purposeList.add(content);
        }
        //将加载的数据进行排序
        Collections.sort(purposeList);

        return purposeList;
    }

    /**
     * 生成在中国开立账户原因
     *
     * @return
     */
    public static List<Content> generateReasonContent() {
        List<Content> reasonList = new ArrayList<>();
        Content content = new Content();
        content.setContentNameID("01");
        content.setName("移民");
        reasonList.add(content);
        return reasonList;
    }

    /**
     * 生成ApplyAccountModel
     *
     * @param context
     * @param accountBean
     * @param purposeCode
     * @param purpose
     * @param isChina
     * @param applyType
     * @return
     */
    public static ApplyAccountModel generateApplyAccountModel(Context context, AccountBean accountBean, String purposeCode, String purpose, boolean isChina, int applyType) {
        ApplyAccountModel model = new ApplyAccountModel();

        model.setName(ApplicationContext.getInstance().getUser().getCustomerName());

        //设置绑定账户信息
        model.setAccountId(accountBean.getAccountId());
        model.setAccountNumber(accountBean.getAccountNumber());

        //设置用途
        model.setAccountPurpose(purposeCode);
        model.setAccountPurposeString(purpose.substring(0, purpose.length() - 1));

        //设置账户类型
        switch (applyType) {
            case ApplyAccountModel.APPLY_TYPE_CURRENT:
                model.setAccountType(ApplicationConst.ACC_TYPE_RAN);
                break;
            case ApplyAccountModel.APPLY_TYPE_REGULAR:
                model.setAccountType(ApplicationConst.ACC_TYPE_REG);
                break;
        }
        model.setAccountTypeSMS(PublicCodeUtils.getAccountType(context, model.getAccountType()));

        //设置开户原因
        model.setOpeningReason(isChina ? Content.NOT_SELECTED : Content.SELECTED);
        model.setOpeningReasonString(isChina ? null : "移民");
        return model;
    }

    /**
     * 生成申请定期/活期预交易参数
     *
     * @param applyAccountModel
     * @param conversationId
     * @param factorId
     * @return
     */
    public static PsnApplyTermDepositeConfirmParams generateApplyTermDepositeConfirmParams(ApplyAccountModel applyAccountModel, String conversationId, String factorId) {
        PsnApplyTermDepositeConfirmParams params = new PsnApplyTermDepositeConfirmParams();
        params.setAccountId(applyAccountModel.getAccountId());
        params.setName(applyAccountModel.getName());
        params.setOpeningReason(applyAccountModel.getOpeningReason());
        params.setAccountPurpose(applyAccountModel.getAccountPurpose());
        params.setAccountType(applyAccountModel.getAccountType());
        params.setAccountTypeSMS(applyAccountModel.getAccountTypeSMS());
        setPreTransactionPublicParams(params, conversationId, null, factorId);
        return params;
    }

    /**
     * 生成申请定期/活期交易参数
     *
     * @param applyAccountModel
     * @param conversationId
     * @param token
     * @param deviceInfoModel
     * @param factorId
     * @param randomNums
     * @param encryptPasswords
     * @return
     */
    public static PsnApplyTermDepositeParams generateApplyTermDepositeParams(ApplyAccountModel applyAccountModel, String conversationId, String token, DeviceInfoModel deviceInfoModel, String factorId, String[] randomNums, String[] encryptPasswords) {
        PsnApplyTermDepositeParams params = new PsnApplyTermDepositeParams();
        params.setAccountId(applyAccountModel.getAccountId());
        params.setName(applyAccountModel.getName());
        params.setAccountType(applyAccountModel.getAccountType());
        params.setAccountTypeSMS(applyAccountModel.getAccountTypeSMS());
        setPublicSecurityParams(params, conversationId, token, factorId, deviceInfoModel, randomNums, encryptPasswords);
        return params;
    }

    /**
     * 生成交易成功后ApplyAccountModel
     *
     * @param applyAccountModel
     * @param result
     * @return
     */
    public static ApplyAccountModel generateApplyAccountModel(ApplyAccountModel applyAccountModel, PsnApplyTermDepositeResult result) {
        applyAccountModel.setApplyStatus(result.getApplyStatus());
        applyAccountModel.setAccountNewNumber(result.getAccountNum());
        applyAccountModel.setLinkStatus(result.getLinkStatus());
        return applyAccountModel;
    }

    /***************************** 虚拟银行卡服务 *****************************/

    /**
     * 生成请求虚拟银行卡列表参数
     *
     * @param conversationId
     * @param accountId
     * @return
     */
    public static PsnCrcdVirtualCardQueryParams generateQueryVirCardParams(String conversationId, String accountId) {
        PsnCrcdVirtualCardQueryParams params = new PsnCrcdVirtualCardQueryParams();
        params.setAccountId(accountId);
        params.setCurrentIndex(0);
        params.setPageSize(20);
        params.set_refresh("false");
        params.setConversationId(conversationId);
        return params;
    }

    /**
     * 生成VirCardModel集合
     *
     * @param accountBean
     * @param result
     */
    public static List<VirtualCardModel> generateVirCardModelList(AccountBean accountBean, PsnCrcdVirtualCardQueryResult result) {
        List<VirtualCardModel> models = new ArrayList<>();

        if (result == null || result.getVirCardList() == null)
            return models;

        for (VirCardInfo virCard : result.getVirCardList()) {
            VirtualCardModel model = new VirtualCardModel();
            model.setAccountId(accountBean.getAccountId());
            model.setVirAccountId(accountBean.getAccountId());
            model.setAccountIbkNum(virCard.getVirtualCardNo());
            model.setStartDate(LocalDate.parse(DateUtil.format(virCard.getStartDate()), DateFormatters.dateFormatter1));
            model.setEndDate(LocalDate.parse(DateUtil.format(virCard.getEndDate()), DateFormatters.dateFormatter1));
            model.setAccountStatus(virCard.getStatus());
            model.setMaxSingleLimit(result.getMaxSingLeamt());
            // model.setCurrencyCode(accountBean.getCurrencyCode());
            model.setCurrencyCode(ApplicationConst.CURRENCY_CNY);
            model.setSignleLimit(virCard.getSingLeamt());
            model.setTotalLimit(virCard.getTotaLeamt());
            model.setAtotalLimit(virCard.getAtotaLeamt());
            model.setChannel(virCard.getCreatChannel());
            model.setAccountName(accountBean.getAccountName());
            model.setAccountNumber(accountBean.getAccountNumber());
            model.setLastUpdates(virCard.getLastUpdates());
            model.setLastUpdateUser(virCard.getLastUpdateUser());
            models.add(model);
        }
        return models;
    }

    /**
     * 生成申请初始化Model
     *
     * @param creditAccountBean
     * @param result
     * @return
     */
    public static VirtualCardModel generateVirtualModel(AccountBean creditAccountBean, PsnCrcdVirtualCardApplyInitResult result) {
        VirtualCardModel model = null;

        if (result == null)
            return model;

        model = new VirtualCardModel();
        model.setAccountNumber(creditAccountBean.getAccountNumber());
        model.setAccountName(creditAccountBean.getAccountName());
        model.setStartDate(result.getStartDate());
        model.setEndDate(result.getEndDate());
        model.setCurrencyCode(creditAccountBean.getCurrencyCode());
        model.setAccountId(creditAccountBean.getAccountId());
        model.setMaxSingleLimit(result.getMaxSingLeamt());
        return model;
    }

    /**
     * 生成申请虚拟银行卡预交易参数
     *
     * @param conversationId
     * @param factorId
     * @param model
     * @return
     */
    public static PsnCrcdVirtualCardApplyConfirmParams generateVirtualCardApplyConfirmParams(String conversationId, String factorId, VirtualCardModel model) {
        PsnCrcdVirtualCardApplyConfirmParams params = new PsnCrcdVirtualCardApplyConfirmParams();
        params.setAccountId(model.getAccountId());
        params.setVirCardAccountName(model.getAccountName());
        params.setVirCardStartDate(model.getStartDate().format(DateFormatters.dateFormatter1));
        params.setVirCardEndDate(model.getEndDate().format(DateFormatters.dateFormatter1));
        params.setSingLeamt(model.getSignleLimit().toString());
        params.setTotaLeamt(model.getTotalLimit().toString());
        params.setVirCardCurrency(model.getCurrencyCode());
        params.setVirCardCustomerId(ApplicationContext.getInstance().getUser().getCustomerId());
        params.setVirCardCurrentDate(ApplicationContext.getInstance().getCurrentSystemDate().format(DateFormatters.dateFormatter1));
        setPreTransactionPublicParams(params, conversationId, null, factorId);
        return params;
    }

    /**
     * 申请虚拟卡提交参数
     *
     * @param model
     * @param conversationId
     * @param token
     * @param deviceInfoModel
     * @param factorId
     * @param randomNums
     * @param encryptPasswords
     * @return
     */
    public static PsnCrcdVirtualCardApplySubmitParams generateVirtualCardApplySubmitParams(VirtualCardModel model, String conversationId, String token, DeviceInfoModel deviceInfoModel, String factorId, String[] randomNums, String[] encryptPasswords) {
        PsnCrcdVirtualCardApplySubmitParams params = new PsnCrcdVirtualCardApplySubmitParams();
        params.setAccountId(model.getAccountId());
        params.setVirCardAccountName(model.getAccountName());
        params.setVirCardStartDate(model.getStartDate().format(DateFormatters.dateFormatter1));
        params.setVirCardEndDate(model.getEndDate().format(DateFormatters.dateFormatter1));
        params.setSingLeamt(model.getSignleLimit().toString());
        params.setTotaLeamt(model.getTotalLimit().toString());
        params.setVirCardCurrency(model.getCurrencyCode());
        params.setVirCardCustomerId(ApplicationContext.getInstance().getUser().getCustomerId());
        params.setVirCardCurrentDate(ApplicationContext.getInstance().getCurrentSystemDate().format(DateFormatters.dateFormatter1));
        setPublicSecurityParams(params, conversationId, token, factorId, deviceInfoModel, randomNums, encryptPasswords);
        return params;
    }

    /**
     * 生成结果页Model
     *
     * @param model
     * @param result
     * @return
     */
    public static VirtualCardModel generateVirtualModel(VirtualCardModel model, PsnCrcdVirtualCardApplySubmitResult result) {
        model.setAccountIbkNum(result.getvCardInfo().getVirtualCardNo());
        model.setVirAccountId(result.getVirCardAccountId());
        model.setLastUpdates(result.getvCardInfo().getLastUpdates());
        model.setLastUpdateUser(result.getvCardInfo().getLastUpdateUser());
        model.setChannel(result.getvCardInfo().getCreatChannel());
        model.setAtotalLimit(result.getvCardInfo().getAtotaLeamt());
        return model;
    }

    /**
     * 生成未出账单Model
     *
     * @param results
     * @return
     */
    public static List<VirtualBillModel> generateVirtualBillModels(List<PsnCrcdVirtualCardUnsettledbillSumResult> results) {
        List<VirtualBillModel> models = new ArrayList<>();

        if (results == null || results.isEmpty())
            return models;

        for (PsnCrcdVirtualCardUnsettledbillSumResult result : results) {
            VirtualBillModel model = new VirtualBillModel();
            model.setTotalIn(result.getCreditSum());
            model.setTotalOut(result.getDebitSum());
            model.setCurrency(result.getCurrency());
            models.add(model);
        }
        return models;
    }

    /**
     * 按时间顺序生成已出账单Map
     *
     * @param isHadUnsettled
     * @return
     */
    public static Map<LocalDate, List<VirtualBillModel>> generateVirtualCardSettledMap(boolean isHadUnsettled) {
        Map<LocalDate, List<VirtualBillModel>> map = new LinkedHashMap<>();

        int dateLength = 5;
        if (isHadUnsettled)
            dateLength = 4;

        for (int i = 0; i < dateLength; i++) {
            LocalDateTime curDate = ApplicationContext.getInstance().getCurrentSystemDate();
            map.put(curDate.minusMonths(i).toLocalDate(), null);
        }
        return map;
    }

    /**
     * 生成请求已出账单月份列表
     *
     * @param dateSet
     * @return
     */
    public static List<String> generateVirtualCardSettledDate(Set<LocalDate> dateSet) {
        List<String> dates = new ArrayList<>();

        for (LocalDate localDate : dateSet)
            dates.add(localDate.format(DateFormatters.monthFormatter2));
        return dates;
    }

    /**
     * 生成查询已出账单参数
     *
     * @param model
     * @param date
     * @return
     */
    public static PsnCrcdVirtualCardSettledbillQueryParams generateVirtualCardSettledbillQueryParams(VirtualCardModel model, String date) {
        PsnCrcdVirtualCardSettledbillQueryParams params = new PsnCrcdVirtualCardSettledbillQueryParams();
        params.setAccountName(model.getAccountName());
        params.setVirtualCardNo(model.getAccountIbkNum());
        params.setStatementMonth(date);
        return params;
    }

    /**
     * 生成已出账账单Model
     *
     * @param results
     * @return
     */
    public static List<VirtualBillModel> generateVirtualBillModel(List<PsnCrcdVirtualCardSettledbillQueryResult> results) {
        List<VirtualBillModel> models = new ArrayList<>();

        for (PsnCrcdVirtualCardSettledbillQueryResult result : results) {
            VirtualBillModel model = new VirtualBillModel();

            //账单信息
            model.setTotalIn(result.getTotalIn());
            model.setTotalOut(result.getTotalOut());
            model.setCurrency(result.getCurrency());
            model.setDueDate(result.getDueDate());
            model.setCurrentBalance(result.getCurrentBalance());
            model.setBillDate(result.getBillDate());
            model.setLastBalance(result.getLastBalance());
            model.setMinPaymentAmount(result.getMinPaymentAmount());
            model.setTransModels(generateBillTransModels(result.getCrcdTransInfo()));
            models.add(model);
        }
        return models;
    }

    public static List<VirtualBillTransModel> generateBillTransModels(List<CrcdTransaction> transactionList) {
        //交易明细
        List<VirtualBillTransModel> transModels = new ArrayList<>();
        for (CrcdTransaction transaction : transactionList) {
            VirtualBillTransModel transModel = new VirtualBillTransModel();
            transModel.setRemark(transaction.getRemark());
            transModel.setTransDate(transaction.getTransDate());
            transModel.setTransCode(transaction.getTransCode());
            transModel.setBookCurrency(transaction.getBookCurrency());
            transModel.setDebitCreditFlag(transaction.getDebitCreditFlag());
            transModel.setBookAmount(transaction.getBookAmount());
            transModel.setBookDate(transaction.getBookDate());
            transModel.setCardNumberTail(transaction.getCardNumberTail());
            transModel.setTranCurrency(transaction.getTranCurrency());
            transModel.setTranAmount(transaction.getTranAmount());
            transModels.add(transModel);
        }
        return transModels;
    }

    /**
     * 生成修改限额预交易参数
     *
     * @author wangyang
     * @time 16/9/6 19:43
     */
    public static PsnCrcdVirtualCardFunctionSetConfirmParams generateVirtualCardFunctionSetConfirmParams(String conversationId, VirtualCardModel model, String factorId) {
        PsnCrcdVirtualCardFunctionSetConfirmParams params = new PsnCrcdVirtualCardFunctionSetConfirmParams();
        params.setAccountId(model.getVirAccountId());
        params.setVirtualCardNo(model.getAccountIbkNum());
        params.setVirCardAccountName(model.getAccountName());
        params.setVirCardStartDate(model.getStartDate().format(DateFormatters.dateFormatter1));
        params.setVirCardEndDate(model.getEndDate().format(DateFormatters.dateFormatter1));
        params.setSingLeamt(model.getSignleLimit().toString());
        params.setTotaLeamt(model.getTotalLimit().toString());
        params.setAtotaLeamt(model.getAtotalLimit().toString());
        params.setVirCardCurrency(model.getCurrencyCode());
        params.setLastUpdates(model.getLastUpdates());
        params.setLastUpdateUser(model.getLastUpdateUser());
        params.setVirCardCustomerId(ApplicationContext.getInstance().getUser().getCustomerId());
        params.setCustomerId(ApplicationContext.getInstance().getUser().getCustomerId());
        setPreTransactionPublicParams(params, conversationId, null, factorId);
        return params;
    }

    /**
     * 生成修改限额交易参数
     *
     * @param conversationId
     * @param token
     * @param model
     * @param deviceInfoModel
     * @param factorId
     * @param randomNums
     * @param encryptPasswords
     * @return
     */
    public static PsnCrcdVirtualCardFunctionSetSubmitParams generateVirtualCardFunctionSetParams(String conversationId, String token, VirtualCardModel model, DeviceInfoModel deviceInfoModel, String factorId, String[] randomNums, String[] encryptPasswords) {
        PsnCrcdVirtualCardFunctionSetSubmitParams params = new PsnCrcdVirtualCardFunctionSetSubmitParams();
        params.setAccountId(model.getVirAccountId());
        params.setVirtualCardNo(model.getAccountIbkNum());
        params.setVirCardAccountName(model.getAccountName());
        params.setVirCardStartDate(model.getStartDate().format(DateFormatters.dateFormatter1));
        params.setVirCardEndDate(model.getEndDate().format(DateFormatters.dateFormatter1));
        params.setSingLeamt(model.getSignleLimit().toString());
        params.setTotaLeamt(model.getTotalLimit().toString());
        params.setAtotaLeamt(model.getAtotalLimit().toString());
        params.setLastUpdates(model.getLastUpdates());
        params.setLastUpdateUser(model.getLastUpdateUser());
        params.setVirCardCurrency(model.getCurrencyCode());
        params.setVirCardCustomerId(ApplicationContext.getInstance().getUser().getCustomerId());
        params.setCustomerId(ApplicationContext.getInstance().getUser().getCustomerId());
        setPublicSecurityParams(params, conversationId, token, factorId, deviceInfoModel, randomNums, encryptPasswords);
        return params;
    }

    /**
     * 生成注销虚拟卡参数
     *
     * @param conversationId
     * @param token
     * @param model
     * @return
     */
    public static PsnCrcdVirtualCardCancelParams generateVirtualCardCancelParams(String conversationId, String token, VirtualCardModel model) {
        PsnCrcdVirtualCardCancelParams params = new PsnCrcdVirtualCardCancelParams();
        params.setAccountId(model.getAccountId());
        params.setVirtualCardNo(model.getAccountIbkNum());
        params.setVirCardStartDate(model.getStartDate().format(DateFormatters.dateFormatter1));
        params.setVirCardEndDate(model.getEndDate().format(DateFormatters.dateFormatter1));
        params.setSingLeamt(model.getSignleLimit().toString());
        params.setTotaLeamt(model.getTotalLimit().toString());
        params.setLastUpdates(model.getLastUpdates());
        params.setLastUpdateUser(model.getLastUpdateUser());
        setPublicParamsWithOutSecurity(params, conversationId, token);
        return params;
    }

    /**
     * 生成查询未出账单参数
     *
     * @param conversationId
     * @param model
     * @param currentPage
     * @return
     */
    public static PsnCrcdVirtualCardUnsettledbillQueryParams generateUnsettledBillParams(String conversationId, VirtualCardModel model, int currentPage) {
        PsnCrcdVirtualCardUnsettledbillQueryParams params = new PsnCrcdVirtualCardUnsettledbillQueryParams();
        params.setAccountName(model.getAccountName());
        params.setVirtualCardNo(model.getAccountIbkNum());
        if (currentPage == 1)
            params.set_refresh("true");
        else
            params.set_refresh("false");
        params.setPageSize(50);

        int currentIndex = (currentPage - 1) * 50;
        params.setCurrentIndex(currentIndex);
        setPublicParamsWithOutSecurity(params, conversationId, null);
        return params;
    }

    /**
     * 生成虚拟卡交易明细
     *
     * @param context
     * @param transModels
     * @param isUnsettled
     * @return
     */
    public static List<TransactionBean> generateTransactionBean(Context context, List<VirtualBillTransModel> transModels, boolean isUnsettled) {
        List<TransactionBean> transactionBeans = new ArrayList<>();
        for (VirtualBillTransModel transModel : transModels) {
            TransactionBean transactionBean = new TransactionBean();
            transactionBean.setChangeColor(true);
            transactionBean.setTime(transModel.getTransDate());
            transactionBean.setContentLeftAbove(transModel.getRemark());

            if (isUnsettled) {
                transactionBean.setTitleID(TransactionView.TITLE_BILL_TYPE);
                transactionBean.setContentRightBelow(PublicCodeUtils.getCurrency(context, transModel.getTranCurrency()));
                if (transModel.getBookDate() == null)
                    transactionBean.setContentLeftBelow(context.getString(R.string.boc_virtual_account_bill_detail_no_book));
            } else {
                transactionBean.setTitleID(TransactionView.TITLE_BILL_TYPE1);
            }

            String amount = MoneyUtils.transMoneyFormat(transModel.getTranAmount(), transModel.getTranCurrency());
            if (VirtualBillTransModel.FLAG_DEBT.equals(transModel.getDebitCreditFlag()))
                amount = "-" + amount;
            transactionBean.setContentRightAbove(amount);
            transactionBeans.add(transactionBean);
        }
        return transactionBeans;
    }

    /**
     * 生成修改交易限额Model
     *
     * @param model
     * @param result
     * @return
     */
    public static VirtualCardModel generateVirtualCardModel(VirtualCardModel model, PsnCrcdVirtualCardFunctionSetSubmitResult result) {
        if (result == null) {
            model.setUpdateLimitSuccess(false);
            return model;
        }

        model.setUpdateLimitSuccess(true);
        model.setSignleLimit(result.getVirCard().getSingLeamt());
        model.setTotalLimit(result.getVirCard().getTotaLeamt());
        model.setAtotalLimit(result.getVirCard().getAtotaLeamt());
        return model;
    }

    /**
     * 生成虚拟卡更多列表
     *
     * @return
     */
    public static List<Item> generateVirtualAccountMoreItems(Resources res) {
        List<Item> items = new ArrayList<>();
        //账户详情
        items.add(new Item(res.getString(R.string.boc_account_detail), MoreAccountFragment.MORE_DETAIL));
        //注销
        items.add(new Item(res.getString(R.string.boc_account_cancel), MoreAccountFragment.MORE_CANCEL));
        return items;
    }

    /***************************** 交易限额设置 *****************************/

    /**
     * 生成限额Model
     *
     * @param accountBean
     * @param result
     * @param serviceType
     * @return
     */
    public static LimitModel generateLimitModel(AccountBean accountBean, PsnNcpayServiceChooseResult result, String serviceType) {
        LimitModel limitModel = new LimitModel();
        limitModel.setCardBrand(result.getCardBrand());
        limitModel.setQuota(result.getQuota());
        limitModel.setAccountStatus(result.getStatus());
        switch (serviceType) {
            case LimitType.LIMIT_BORDER:
                limitModel.setLimitType(LimitType.BORDER);
                break;
            case LimitType.LIMIT_PASSWORD:
                if (limitModel.isUnion())
                    limitModel.setLimitType(LimitType.SMALL);
                else
                    limitModel.setLimitType(LimitType.PASSWORD);
                break;
        }
        limitModel.setNickName(accountBean.getNickName());
        limitModel.setAccountType(ApplicationConst.ACC_TYPE_BRO);
        limitModel.setAccountId(accountBean.getAccountId());
        limitModel.setAccountNumber(accountBean.getAccountNumber());
        return limitModel;
    }

    /**
     * 生成QuotaModel
     *
     * @param accountBean
     * @return
     */
    public static QuotaModel generateQuotaModel(AccountBean accountBean) {
        QuotaModel quotaModel = new QuotaModel();
        quotaModel.setAccountId(accountBean.getAccountId());
        quotaModel.setAccountNumber(accountBean.getAccountNumber());
        return quotaModel;
    }

    /**
     * 生成QuotaModel
     *
     * @param limitModel
     * @return
     */
    public static QuotaModel generateQuotaModel(LimitModel limitModel) {
        QuotaModel quotaModel = new QuotaModel();
        quotaModel.setAccountId(limitModel.getAccountId());
        quotaModel.setAccountNumber(limitModel.getAccountNumber());
        return quotaModel;
    }

    /**
     * 生成查询所有限额Model
     *
     * @param quotaModel
     * @param result
     * @return
     */
    public static QuotaModel generateQuotaModel(QuotaModel quotaModel, PsnDebitCardQryQuotaResult result) {
        quotaModel.setAllDayPOS(result.getAllDayPOS());
        quotaModel.setCashDayATM(result.getCashDayATM());
        quotaModel.setCashDayForeignATM(result.getCashDayForeignATM());
        quotaModel.setCurrency(result.getCurrency());
        quotaModel.setConsumeForeignPOS(result.getConsumeForeignPOS());
        quotaModel.setTransDay(result.getTransDay());
        return quotaModel;
    }

    /**
     * 生成借记卡交易限额设置参数
     *
     * @param quotaModel
     * @param conversationId
     * @param combinId
     * @return
     */
    public static PsnDebitCardSetQuotaPreParams generateDebitCardSetQuotaParams(QuotaModel quotaModel, String conversationId, String combinId) {
        PsnDebitCardSetQuotaPreParams params = new PsnDebitCardSetQuotaPreParams();
        params.setAccountId(quotaModel.getAccountId());
        params.setTransDay(quotaModel.getTransDay().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        params.setAllDayPOS(quotaModel.getAllDayPOS().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        params.setCashDayATM(quotaModel.getCashDayATM().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        params.setCashDayForeignATM(quotaModel.getCashDayForeignATM().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        params.setConsumeForeignPOS(quotaModel.getConsumeForeignPOS().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        setPreTransactionPublicParams(params, conversationId, null, combinId);
        return params;
    }

    /**
     * 生成设置借记卡交易限额参数
     *
     * @param quotaModel
     * @param conversationId
     * @param token
     * @param deviceInfoModel
     * @param factorId
     * @param randomNums
     * @param encryptPasswords
     * @return
     */
    public static PsnDebitCardSetQuotaParams generateRelationParams(QuotaModel quotaModel, String conversationId, String token, DeviceInfoModel deviceInfoModel, String factorId, String[] randomNums, String[] encryptPasswords) {
        PsnDebitCardSetQuotaParams params = new PsnDebitCardSetQuotaParams();
        params.setAccountId(quotaModel.getAccountId());
        params.setTransDay(quotaModel.getTransDay().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        params.setAllDayPOS(quotaModel.getAllDayPOS().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        params.setCashDayATM(quotaModel.getCashDayATM().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        params.setCashDayForeignATM(quotaModel.getCashDayForeignATM().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        params.setConsumeForeignPOS(quotaModel.getConsumeForeignPOS().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        setPublicSecurityParams(params, conversationId, token, factorId, deviceInfoModel, randomNums, encryptPasswords);
        return params;
    }

    /**
     * 生成关闭服务预交易
     *
     * @param limitModel
     * @param conversationId
     * @return
     */
    public static PsnNcpayClosePreParams generateNcpayClosePreParams(LimitModel limitModel, String conversationId) {
        PsnNcpayClosePreParams params = new PsnNcpayClosePreParams();
        params.setConversationId(conversationId);
        params.setAccountId(limitModel.getAccountId());
        params.setServiceType(limitModel.getLimitType().getServiceType());
        return params;
    }

    /**
     * 生成关闭服务交易
     *
     * @param limitModel
     * @param conversationId
     * @param token
     * @return
     */
    public static PsnNcpayCloseSubmitParams generateNcpayCloseParams(LimitModel limitModel, String conversationId, String token) {
        PsnNcpayCloseSubmitParams params = new PsnNcpayCloseSubmitParams();
        params.setAccountId(limitModel.getAccountId());
        params.setServiceType(limitModel.getLimitType().getServiceType());
        params.setCurrentQuota(limitModel.getQuota().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        setPublicParamsWithOutSecurity(params, conversationId, token);
        return params;
    }

    /**
     * 生成修改限额Model
     *
     * @param mainModel
     * @param quota
     * @return
     */
    public static LimitModel generateLimitModel(LimitModel mainModel, double quota) {
        LimitModel limitModel = new LimitModel();
        limitModel.setCardBrand(mainModel.getCardBrand());
        limitModel.setQuota(new BigDecimal(quota));
        limitModel.setAccountStatus(mainModel.getAccountStatus());
        limitModel.setLimitType(mainModel.getLimitType());
        limitModel.setAccountId(mainModel.getAccountId());
        limitModel.setAccountNumber(mainModel.getAccountNumber());
        limitModel.setNickName(mainModel.getNickName());
        limitModel.setAccountType(ApplicationConst.ACC_TYPE_BRO);
        return limitModel;
    }

    /**
     * 修改限额预交易参数
     *
     * @param limitModel
     * @param conversationId
     * @param factorId
     * @return
     */
    public static PsnNcpayQuotaModifyPreParams generateQuotaModifyPreParams(LimitModel limitModel, String conversationId, String factorId) {
        PsnNcpayQuotaModifyPreParams params = new PsnNcpayQuotaModifyPreParams();
        params.setAccountId(limitModel.getAccountId());
        params.setServiceType(limitModel.getLimitType().getServiceType());
        params.setCustomerName(ApplicationContext.getInstance().getUser().getCustomerName());
        params.setAccountNumber(limitModel.getAccountNumber());
        params.setCurrentQuota(limitModel.getQuota().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        params.setTranDate(ApplicationContext.getInstance().getCurrentSystemDate().format(DateFormatters.dateFormatter1));
        setPreTransactionPublicParams(params, conversationId, null, factorId);
        return params;
    }

    /**
     * 修改限额交易参数
     *
     * @param limitModel
     * @param conversationId
     * @param token
     * @param deviceInfoModel
     * @param factorId
     * @param randomNums
     * @param encryptPasswords @return
     */
    public static PsnNcpayQuotaModifySubmitParams generateQuotaModifySubmitParams(LimitModel limitModel, String conversationId, String token, DeviceInfoModel deviceInfoModel, String factorId, String[] randomNums, String[] encryptPasswords) {
        PsnNcpayQuotaModifySubmitParams params = new PsnNcpayQuotaModifySubmitParams();
        params.setAccountId(limitModel.getAccountId());
        params.setServiceType(limitModel.getLimitType().getServiceType());
        params.setAccountNumber(limitModel.getAccountNumber());
        params.setCustomerName(ApplicationContext.getInstance().getUser().getCustomerName());
        params.setCurrentQuota(limitModel.getQuota().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        params.setTranDate(ApplicationContext.getInstance().getCurrentSystemDate().format(DateFormatters.dateFormatter1));
        setPublicSecurityParams(params, conversationId, token, factorId, deviceInfoModel, randomNums, encryptPasswords);
        return params;
    }

    /**
     * 生成开通限额预交易参数
     *
     * @param limitModel
     * @param conversationId
     * @param factorId
     * @return
     */
    public static PsnNcpayOpenPreParams generateQuotaOpenPreParams(LimitModel limitModel, String conversationId, String factorId) {
        PsnNcpayOpenPreParams params = new PsnNcpayOpenPreParams();
        params.setAccountId(limitModel.getAccountId());
        params.setServiceType(limitModel.getLimitType().getServiceType());
        params.setAccountNumber(limitModel.getAccountNumber());
        params.setCustomerName(ApplicationContext.getInstance().getUser().getCustomerName());
        params.setCurrentQuota(limitModel.getQuota().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        params.setTranDate(ApplicationContext.getInstance().getCurrentSystemDate().format(DateFormatters.dateFormatter1));
        setPreTransactionPublicParams(params, conversationId, null, factorId);
        return params;
    }

    /**
     * 生成开通限额交易参数
     *
     * @param limitModel
     * @param conversationId
     * @param token
     * @param deviceInfoModel
     * @param factorId
     * @param randomNums
     * @param encryptPasswords
     * @return
     */
    public static PsnNcpayOpenSubmitParams generateQuotaOpenSubmitParams(LimitModel limitModel, String conversationId, String token, DeviceInfoModel deviceInfoModel, String factorId, String[] randomNums, String[] encryptPasswords) {
        PsnNcpayOpenSubmitParams params = new PsnNcpayOpenSubmitParams();
        params.setAccountId(limitModel.getAccountId());
        params.setServiceType(limitModel.getLimitType().getServiceType());
        params.setCustomerName(ApplicationContext.getInstance().getUser().getCustomerName());
        params.setAccountNumber(limitModel.getAccountNumber());
        params.setCurrentQuota(limitModel.getQuota().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        params.setTranDate(ApplicationContext.getInstance().getCurrentSystemDate().format(DateFormatters.dateFormatter1));
        setPublicSecurityParams(params, conversationId, token, factorId, deviceInfoModel, randomNums, encryptPasswords);
        return params;
    }

    /**
     * 生成跨行订购LimitModel
     *
     * @param accountBeans
     * @param result
     * @return
     */
    public static LimitModel generateLimitModel(List<AccountBean> accountBeans, PsnNcpayServiceChooseResult result) {
        LimitModel limitModel = new LimitModel();
        limitModel.setCardBrand(result.getCardBrand());
        limitModel.setQuota(result.getQuota());
        limitModel.setAccountStatus(LimitModel.STATUS_OPEN);
        limitModel.setAccountType(ApplicationConst.ACC_TYPE_BRO);
        limitModel.setLimitType(LimitType.ACROSS);

        for (AccountBean accountBean : accountBeans) {
            if (accountBean.getAccountNumber().equals(result.getAcctNum())) {
                limitModel.setAccountId(accountBean.getAccountId());
                limitModel.setAccountNumber(accountBean.getAccountNumber());
                limitModel.setNickName(accountBean.getNickName());
                break;
            }
        }
        return limitModel;
    }

    /**
     * @param accountBeans
     * @param openModels
     * @return
     */
    public static List<LimitModel> generateLimitModel(List<AccountBean> accountBeans, List<LimitModel> openModels) {
        List<LimitModel> closeModels = new ArrayList<>();

        accountBeans.removeAll(openModels);
        for (AccountBean accountBean : accountBeans) {
            if (openModels.contains(accountBean))
                continue;

            LimitModel limitModel = new LimitModel();
            limitModel.setAccountId(accountBean.getAccountId());
            limitModel.setAccountNumber(accountBean.getAccountNumber());
            limitModel.setNickName(accountBean.getNickName());
            limitModel.setAccountStatus(LimitModel.STATUS_CLOSED);
            limitModel.setAccountType(ApplicationConst.ACC_TYPE_BRO);
            limitModel.setLimitType(LimitType.ACROSS);

            closeModels.add(limitModel);
        }
        return closeModels;
    }
}
