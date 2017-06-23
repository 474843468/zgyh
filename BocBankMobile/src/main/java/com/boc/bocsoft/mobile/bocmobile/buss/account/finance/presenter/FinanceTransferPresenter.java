package com.boc.bocsoft.mobile.bocmobile.buss.account.finance.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICAccountDetail.PsnFinanceICAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICAccountDetail.PsnFinanceICAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICTransfer.PsnFinanceICTransferResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnICTransferNoRelevance.PsnICTransferNoRelevanceResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnICTransferNoRelevanceRes.PsnICTransferNoRelevanceResResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnTransQuotaQuery.PsnTransQuotaQueryParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnTransQuotaQuery.PsnTransQuotaQueryResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.account.service.FinanceService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseTransactionPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model.FinanceModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model.TransferModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author wangyang
 *         16/6/28 14:13
 *         电子现金账户充值业务逻辑
 */
public class FinanceTransferPresenter extends BaseTransactionPresenter implements FinanceContract.TransferPresenter {

    /**
     * 充值界面View
     */
    private FinanceContract.FinanceAccountRechargeView financeAccountRechargeView;
    /**
     * 充值信息输入界面
     */
    private FinanceContract.FinanceAccountRechargeInputView financeAccountRechargeInputView;
    /**
     * 电子现金账户Service
     */
    private FinanceService financeService;
    /**
     * 账户Service
     */
    private AccountService accountService;

    public FinanceTransferPresenter(FinanceContract.FinanceAccountRechargeView financeAccountRechargeView) {
        super(financeAccountRechargeView);
        this.financeAccountRechargeView = financeAccountRechargeView;
        financeService = new FinanceService();
    }

    public FinanceTransferPresenter(FinanceContract.FinanceAccountRechargeInputView financeAccountRechargeInputView) {
        super(financeAccountRechargeInputView);
        this.financeAccountRechargeInputView = financeAccountRechargeInputView;
        financeService = new FinanceService();
        accountService = new AccountService();
    }

    /**
     * 给自己充值交易
     *
     * @param transferModel
     */
    @Override
    public void psnFinanceTransferSelf(final TransferModel transferModel) {
        //生成tokenId,调用充值接口
        getToken().flatMap(new Func1<String, Observable<PsnFinanceICTransferResult>>() {
            @Override
            public Observable<PsnFinanceICTransferResult> call(String token) {
                return financeService.psnFinanceICTransfer(ModelUtil.generateTransferparams(transferModel, getConversationId(), token));
            }
        })
                .compose(this.<PsnFinanceICTransferResult>bindToLifecycle())
                //设置线程
                .compose(SchedulersCompat.<PsnFinanceICTransferResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnFinanceICTransferResult>() {
                    @Override
                    public void onNext(PsnFinanceICTransferResult result) {
                        //回调界面
                        clearConversation();
                        financeAccountRechargeView.psnFinanceTransferSuccess(ModelUtil.generateTransSelfResultModel(result));
                    }
                });
    }

    /**
     * 给其他人充值预交易
     *
     * @param transferModel
     * @param combinId
     */
    @Override
    public void psnFinanceTransferOtherPre(final TransferModel transferModel, final String combinId) {
        //获取Token发送预交易请求
        getToken().flatMap(new Func1<String, Observable<PsnICTransferNoRelevanceResult>>() {
            @Override
            public Observable<PsnICTransferNoRelevanceResult> call(String token) {
                return financeService.psnICTransferNoRelevance(ModelUtil.generateTransferOtherPreParams(transferModel, getConversationId(), token, combinId));
            }
        })
                .compose(this.<PsnICTransferNoRelevanceResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnICTransferNoRelevanceResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnICTransferNoRelevanceResult>() {
                    @Override
                    public void onNext(PsnICTransferNoRelevanceResult result) {
                        //回调界面
                        preTransactionSuccess(ModelUtil.generateSecurityModel(result));
                    }
                });
    }

    /**
     * 给其他人充值交易
     *
     * @param transferModel
     * @param deviceInfoModel
     * @param factorId
     * @param randomNums
     * @param encryptPasswords
     */
    @Override
    public void psnFinanceTransferOther(final TransferModel transferModel, final DeviceInfoModel deviceInfoModel, final String factorId, final String[] randomNums, final String[] encryptPasswords) {
        //获取Token,发送交易请求
        getToken().flatMap(new Func1<String, Observable<PsnICTransferNoRelevanceResResult>>() {
            @Override
            public Observable<PsnICTransferNoRelevanceResResult> call(String token) {
                return financeService.psnICTransferNoRelevanceRes(ModelUtil.generateTransferOtherParams(transferModel, getConversationId(), token, factorId, deviceInfoModel, randomNums, encryptPasswords));
            }
        })
                .compose(this.<PsnICTransferNoRelevanceResResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnICTransferNoRelevanceResResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnICTransferNoRelevanceResResult>() {
                    @Override
                    public void onNext(PsnICTransferNoRelevanceResResult result) {
                        //回调界面,清除会话
                        financeAccountRechargeView.psnFinanceTransferSuccess(ModelUtil.generateTransOtherResultModel(result));
                        clearConversation();
                    }
                });
    }

    /**
     * 根据电子现金账户账号获取余额
     *
     * @param financeModel
     */
    @Override
    public void psnFinanceAccount(final FinanceModel financeModel) {
        financeService.psnFinanceICAccountDetail(new PsnFinanceICAccountDetailParams(financeModel.getAccountId()))
                .compose(this.<PsnFinanceICAccountDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFinanceICAccountDetailResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnFinanceICAccountDetailResult>() {
                    @Override
                    public void onNext(PsnFinanceICAccountDetailResult result) {
                        //回调界面
                        financeAccountRechargeInputView.psnFinanceAccount(ModelUtil.generateFinanceModel(financeModel, result));
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void handleException(BiiResultErrorException error) {
                        financeAccountRechargeInputView.psnFinanceAccount(null);
                    }
                });
    }

    /**
     * 根据账户Id获取有人民币的银行卡账户余额
     *
     * @param accountId
     */
    @Override
    public void psnBankAccount(final String accountId) {
        //发送请求获取账户余额
        PsnAccountQueryAccountDetailParams params = new PsnAccountQueryAccountDetailParams();
        params.setAccountId(accountId);
        accountService.psnAccountQueryAccountDetail(params)
                .compose(this.<PsnAccountQueryAccountDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnAccountQueryAccountDetailResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnAccountQueryAccountDetailResult>() {

                    @Override
                    public void handleException(BiiResultErrorException error) {
                        financeAccountRechargeInputView.psnFinanceExpendAccount(null, null);
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }

                    @Override
                    public void onNext(PsnAccountQueryAccountDetailResult result) {
                        List<PsnAccountQueryAccountDetailResult.AccountDetaiListBean> details = result.getAccountDetaiList();

                        //遍历账户余额列表
                        for (PsnAccountQueryAccountDetailResult.AccountDetaiListBean detail : details) {
                            if (!ApplicationConst.CURRENCY_CNY.equals(detail.getCurrencyCode()))
                                continue;
                            //人民币余额,回调界面
                            financeAccountRechargeInputView.psnFinanceExpendAccount(detail.getAvailableBalance(), detail.getCurrencyCode());
                            return;
                        }
                        financeAccountRechargeInputView.psnFinanceExpendAccount(details.get(0).getAvailableBalance(), details.get(0).getCurrencyCode());
                    }
                });
    }

    @Override
    public void psnTransQuotaQuery() {
        accountService.psnTransQuotaQuery(new PsnTransQuotaQueryParams())
                .compose(this.<PsnTransQuotaQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnTransQuotaQueryResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnTransQuotaQueryResult>() {
                    @Override
                    public void onNext(PsnTransQuotaQueryResult result) {
                        if (result != null)
                            financeAccountRechargeInputView.psnTransQuotaQuery(result.getQuotaAmount());
                        else
                            financeAccountRechargeInputView.psnTransQuotaQuery(null);
                    }

                    @Override
                    public void handleException(BiiResultErrorException error) {
                        financeAccountRechargeInputView.psnTransQuotaQuery(null);
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }
                });
    }
}
