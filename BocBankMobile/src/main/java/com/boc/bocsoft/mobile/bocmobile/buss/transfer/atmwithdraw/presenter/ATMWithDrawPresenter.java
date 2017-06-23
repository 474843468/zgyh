package com.boc.bocsoft.mobile.bocmobile.buss.transfer.atmwithdraw.presenter;

import android.content.Context;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetRandom.PSNGetRandomParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitFreeCancel.PsnPasswordRemitFreeCancelParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitFreeDetailQuery.PsnPasswordRemitFreeDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitFreeDetailQuery.PsnPasswordRemitFreeDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitFreeTranQuery.PsnPasswordRemitFreeTranQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitFreeTranQuery.PsnPasswordRemitFreeTranQueryResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitPayment.PsnPasswordRemitPaymentParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitPayment.PsnPasswordRemitPaymentResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitPaymentPre.PsnPasswordRemitPaymentPreParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitPaymentPre.PsnPasswordRemitPaymentPreResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQuotaQuery.PsnTransQuotaQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQuotaQuery.PsnTransQuotaQueryResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.ServiceIdCodeConst;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.CombinBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.FactorBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.SecurityViewModel;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.atmwithdraw.model.ATMWithDrawDetailsViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.atmwithdraw.model.ATMWithDrawQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.atmwithdraw.model.ATMWithDrawViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.atmwithdraw.ui.ATMWithDrawContract;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.atmwithdraw.ui.ATMWithDrawFragment;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * ATM无卡取款网络通信
 * Created by liuweidong on 2016/6/25.
 */
public class ATMWithDrawPresenter implements ATMWithDrawContract.Presenter {

    private RxLifecycleManager mRxLifecycleManager;
    private ATMWithDrawContract.View mView;// ATM无卡取款查询
    private ATMWithDrawContract.CancelView mCancelView;// ATM无卡取款撤销
    private ATMWithDrawContract.BeforeView mBeforeView;// ATM无卡取款
    private ATMWithDrawContract.ResultView mResultView;
    private GlobalService globalService;// 公共Service
    private AccountService accountService;
    private TransferService transferService;// 转账汇款Service
    public static String conversationID = "";// 会话ID
    public static String randomID = "";// 随机数ID
    private boolean isConfirmView = false;

    private static final String REMIT_TYPE_ATM_WITHDRAW = "1";// 汇款类型 1：ATM无卡取现；0：密码汇款
    private static final String ATM_WITHDRAW_YES = "1";// 1：是，2：否
    private static final String ATM_WITHDRAW_NO = "2";

    public ATMWithDrawPresenter() {
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        accountService = new AccountService();
        transferService = new TransferService();
    }

    public ATMWithDrawPresenter(ATMWithDrawContract.View view) {
        this();
        mView = view;
    }

    public ATMWithDrawPresenter(ATMWithDrawContract.CancelView cancelView) {
        this();
        mCancelView = cancelView;
    }

    public ATMWithDrawPresenter(ATMWithDrawContract.BeforeView beforeView, boolean isConfirmView) {
        this();
        this.isConfirmView = isConfirmView;
        mBeforeView = beforeView;
    }

    public ATMWithDrawPresenter(ATMWithDrawContract.ResultView resultView, boolean isConfirmView) {// 确认页view
        this();
        this.isConfirmView = isConfirmView;
        mResultView = resultView;
    }

    /**
     * ATM无卡取款查询
     *
     * @param atmWithDrawQueryViewModel
     */
    @Override
    public void queryATMWithDraw(final ATMWithDrawQueryViewModel atmWithDrawQueryViewModel) {
        // 获取会话ID
        PSNCreatConversationParams params = new PSNCreatConversationParams();
        globalService.psnCreatConversation(params)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnPasswordRemitFreeTranQueryResult>>() {
                    @Override
                    public Observable<PsnPasswordRemitFreeTranQueryResult> call(String s) {
                        // ATM无卡取款查询
                        PsnPasswordRemitFreeTranQueryParams params = buildPsnPasswordRemitFreeTranQueryParams(atmWithDrawQueryViewModel);
                        params.setConversationId(s);
                        return transferService.psnPasswordRemitFreeTranQuery(params);
                    }
                })
                .compose(SchedulersCompat.<PsnPasswordRemitFreeTranQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnPasswordRemitFreeTranQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.queryATMWithDrawFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnPasswordRemitFreeTranQueryResult psnPasswordRemitFreeTranQueryResult) {
                        mView.queryATMWithDrawSuccess(resultModelToViewModel(psnPasswordRemitFreeTranQueryResult));
                    }
                });
    }

    /**
     * ATM无卡取款查询详情
     */
    @Override
    public void queryATMWithDrawDetails(ATMWithDrawDetailsViewModel atmWithDrawDetailsViewModel) {
        PsnPasswordRemitFreeDetailQueryParams params = buildPsnPasswordRemitFreeDetailQueryParams(atmWithDrawDetailsViewModel);

        transferService.psnPasswordRemitFreeDetailQuery(params)
                .compose(mRxLifecycleManager.<PsnPasswordRemitFreeDetailQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnPasswordRemitFreeDetailQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnPasswordRemitFreeDetailQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mView.queryATMWithDrawDetailsFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnPasswordRemitFreeDetailQueryResult psnPasswordRemitFreeDetailQueryResult) {
                        mView.queryATMWithDrawDetailsSuccess(resultDetailsModelToViewModel(psnPasswordRemitFreeDetailQueryResult));
                    }
                });
    }

    /**
     * ATM无卡取款撤销
     */
    @Override
    public void cancelATMWithDraw(final ATMWithDrawDetailsViewModel detailsInfo) {
        // 获取会话ID
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        conversationID = s;
                        // 查询TokenID
                        PSNGetTokenIdParams params = new PSNGetTokenIdParams();
                        params.setConversationId(conversationID);
                        return globalService.psnGetTokenId(params);
                    }
                })
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .map(new Func1<String, PsnPasswordRemitFreeCancelParams>() {
                    @Override
                    public PsnPasswordRemitFreeCancelParams call(String s) {
                        PsnPasswordRemitFreeCancelParams params = buildPsnPasswordRemitFreeCancelParams(detailsInfo);
                        params.setToken(s);
                        params.setConversationId(conversationID);
                        return params;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<PsnPasswordRemitFreeCancelParams, Observable<String>>() {
                    @Override
                    public Observable<String> call(PsnPasswordRemitFreeCancelParams psnPasswordRemitFreeCancelParams) {
                        // ATM无卡取款撤销
                        return transferService.psnPasswordRemitFreeCancel(psnPasswordRemitFreeCancelParams);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<String>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mCancelView.cancelATMWithDrawFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(String result) {
                        mCancelView.cancelATMWithDrawSuccess();
                    }
                });
    }

    /**
     * 查询账户详情
     *
     * @param accountID
     */
    @Override
    public void queryAccountDetails(String accountID) {
        PsnAccountQueryAccountDetailParams params = new PsnAccountQueryAccountDetailParams();
        params.setAccountId(accountID);

        accountService.psnAccountQueryAccountDetail(params)
                .compose(mRxLifecycleManager.<PsnAccountQueryAccountDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnAccountQueryAccountDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnAccountQueryAccountDetailResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        if (isConfirmView) {
                            mResultView.queryAccountDetailsFail();
                        } else {
                            mBeforeView.queryAccountDetailsFail();
                        }
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnAccountQueryAccountDetailResult result) {
                        if (isConfirmView) {
                            mResultView.queryAccountDetailsSuccess(resultModelToViewModel(result));
                        } else {
                            mBeforeView.queryAccountDetailsSuccess(resultModelToViewModel(result));
                        }
                    }
                });
    }

    /**
     * 查询安全因子
     */
    @Override
    public void querySecurityFactor() {
        conversationID = "";
        randomID = "";
        // 创建会话
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        conversationID = s;
                        // 查询随机数
                        PSNGetRandomParams psnGetRandomParams = new PSNGetRandomParams();
                        psnGetRandomParams.setConversationId(conversationID);
                        return globalService.psnGetRandom(psnGetRandomParams);
                    }
                })
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .map(new Func1<String, PsnGetSecurityFactorParams>() {
                    @Override
                    public PsnGetSecurityFactorParams call(String random) {
                        randomID = random;
                        // 安全因子
                        PsnGetSecurityFactorParams psnGetSecurityFactorParams = new PsnGetSecurityFactorParams();
                        psnGetSecurityFactorParams.setConversationId(conversationID);
                        psnGetSecurityFactorParams.setServiceId(ServiceIdCodeConst.SERVICE_ID_ATM_WITHDRAW);
                        return psnGetSecurityFactorParams;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<PsnGetSecurityFactorParams, Observable<PsnGetSecurityFactorResult>>() {
                    @Override
                    public Observable<PsnGetSecurityFactorResult> call(PsnGetSecurityFactorParams params) {
                        return globalService.psnGetSecurityFactor(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnGetSecurityFactorResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mBeforeView.querySecurityFactorFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
                        mBeforeView.querySecurityFactorSuccess(resultModelToViewModel(psnGetSecurityFactorResult));
                    }
                });
    }

    /**
     * ATM无卡取款预交易
     *
     * @param accountID
     * @param combinID
     * @param money
     */
    @Override
    public void atmWithDrawConfirm(String accountID, String combinID, String money) {
        // 封装ATM无卡取款预交易请求参数
        PsnPasswordRemitPaymentPreParams params = buildPsnPasswordRemitPaymentPreParams(accountID, combinID, money);
        transferService.psnPasswordRemitPaymentPre(params)
                .compose(mRxLifecycleManager.<PsnPasswordRemitPaymentPreResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnPasswordRemitPaymentPreResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnPasswordRemitPaymentPreResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        if (isConfirmView) {
                            mResultView.atmWithDrawConfirmFail(biiResultErrorException);
                        } else {
                            mBeforeView.atmWithDrawConfirmFail(biiResultErrorException);
                        }
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnPasswordRemitPaymentPreResult result) {
                        resultModelToViewModel(result);
                        if (isConfirmView) {
                            mResultView.atmWithDrawConfirmSuccess();
                        } else {
                            mBeforeView.atmWithDrawConfirmSuccess();
                        }
                    }
                });
    }

    /**
     * ATM无卡取款提交交易
     */
    @Override
    public void atmWithDrawResult(final String accountID, final String[] randomNums,
                                  final String[] encryptPasswords, final int curCombinID, final Context context, final String devicePrint) {
        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
        psnGetTokenIdParams.setConversationId(conversationID);// 会话ID
        globalService.psnGetTokenId(psnGetTokenIdParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnPasswordRemitPaymentResult>>() {
                    @Override
                    public Observable<PsnPasswordRemitPaymentResult> call(String tokenID) {
                        PsnPasswordRemitPaymentParams params = buildPsnPasswordRemitPaymentParams(accountID,
                                randomNums, encryptPasswords, curCombinID, context, devicePrint);
                        params.setToken(tokenID);// tokenID
                        return transferService.psnPasswordRemitPayment(params);
                    }
                }).compose(SchedulersCompat.<PsnPasswordRemitPaymentResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnPasswordRemitPaymentResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mResultView.atmWithDrawResultFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnPasswordRemitPaymentResult result) {
                        ATMWithDrawFragment.getViewModel().setDueDate(result.getDueDate());
                        ATMWithDrawFragment.getViewModel().setStatus(result.getStatus());
                        ATMWithDrawFragment.getViewModel().setRemitNo(result.getRemitNo());
                        mResultView.atmWithDrawResultSuccess();
                    }
                });
    }

    @Override
    public void psnTransQuotaquery() {
        PsnTransQuotaQueryParams params = new PsnTransQuotaQueryParams();
        transferService.psnTransQuotaQuery(params)
                .compose(mRxLifecycleManager.<PsnTransQuotaQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnTransQuotaQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTransQuotaQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mBeforeView.psnTransQuotaqueryFail();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnTransQuotaQueryResult result) {
                        mBeforeView.psnTransQuotaquerySuccess(result.getQuotaAmount());
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {

                    }
                });
    }

    /**
     * 将安全因子响应数据复制到ViewModel中
     *
     * @param psnGetSecurityFactorResult
     * @return
     */
    private SecurityViewModel resultModelToViewModel(PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
        SecurityViewModel viewModel = new SecurityViewModel();

        if (psnGetSecurityFactorResult.get_defaultCombin() != null) {
            CombinBean defaultCombin = new CombinBean();
            defaultCombin.setName(psnGetSecurityFactorResult.get_defaultCombin().getName());
            defaultCombin.setId(psnGetSecurityFactorResult.get_defaultCombin().getId());
            viewModel.set_defaultCombin(defaultCombin);
        }

        List<CombinBean> list = new ArrayList<CombinBean>();
        for (CombinListBean item : psnGetSecurityFactorResult.get_combinList()) {
            CombinBean bean = new CombinBean();
            bean.setId(item.getId());
            bean.setName(item.getName());
            list.add(bean);
        }
        viewModel.set_combinList(list);
        return viewModel;
    }

    /**
     * 封装ATM无卡取款查询请求参数
     */
    private PsnPasswordRemitFreeTranQueryParams buildPsnPasswordRemitFreeTranQueryParams(ATMWithDrawQueryViewModel viewModel) {
        PsnPasswordRemitFreeTranQueryParams params = new PsnPasswordRemitFreeTranQueryParams();
        params.setAccountId(viewModel.getAccountId());// 账户ID
        params.setFreeRemitTrsType(viewModel.getFreeRemitTrsType());// 交易类型
        params.setStartDate(viewModel.getStartDate());// 开始日期
        params.setEndDate(viewModel.getEndDate());// 结束日期
        params.setCurrentIndex(viewModel.getCurrentIndex());// 索引
        params.setPageSize(viewModel.getPageSize());// 条数
        params.set_refresh(viewModel.get_refresh());
        return params;
    }

    /**
     * 将ATM无卡取款查询数据复制到ViewModel中
     *
     * @param psnPasswordRemitFreeTranQueryResult
     * @return
     */
    private ATMWithDrawQueryViewModel resultModelToViewModel(PsnPasswordRemitFreeTranQueryResult psnPasswordRemitFreeTranQueryResult) {
        ATMWithDrawQueryViewModel atmWithDrawQueryViewModel = new ATMWithDrawQueryViewModel();
        List<ATMWithDrawQueryViewModel.ListBean> list = new ArrayList<ATMWithDrawQueryViewModel.ListBean>();

        atmWithDrawQueryViewModel.setRecordNumber(psnPasswordRemitFreeTranQueryResult.getRecordNumber());
        for (int i = 0; i < psnPasswordRemitFreeTranQueryResult.getList().size(); i++) {
            PsnPasswordRemitFreeTranQueryResult.ListBean item = psnPasswordRemitFreeTranQueryResult.getList().get(i);
            ATMWithDrawQueryViewModel.ListBean listBean = new ATMWithDrawQueryViewModel.ListBean();

            // 查询页面展示字段
            listBean.setPaymentDate(item.getPaymentDate());// 付款日期
            listBean.setStatus(item.getStatus());// 状态
            listBean.setPaymentAmount(item.getPaymentAmount());// 付款金额

            // 详情请求需要字段
            listBean.setRemitNumber(item.getRemitNumber());// 汇款编号
            listBean.setFromName(item.getFromName());// 汇款人姓名
            listBean.setToName(item.getToName());// 收款人姓名

            list.add(listBean);
        }
        atmWithDrawQueryViewModel.setList(list);
        return atmWithDrawQueryViewModel;
    }

    /**
     * 封装ATM无卡取款查询详情请求参数
     *
     * @param atmWithDrawDetailsViewModel
     * @return
     */
    private PsnPasswordRemitFreeDetailQueryParams buildPsnPasswordRemitFreeDetailQueryParams(ATMWithDrawDetailsViewModel atmWithDrawDetailsViewModel) {
        PsnPasswordRemitFreeDetailQueryParams params = new PsnPasswordRemitFreeDetailQueryParams();
        params.setRemitNo(atmWithDrawDetailsViewModel.getRemitNo());// 汇款编号
        params.setPayerName(atmWithDrawDetailsViewModel.getPayerName());// 汇款人姓名
        params.setPayeeName(atmWithDrawDetailsViewModel.getPayeeName());// 收款人姓名
        params.setFreeRemitTrsType(atmWithDrawDetailsViewModel.getFreeRemitTrsType());// 交易类型
        return params;
    }

    /**
     * 将ATM无卡取款详情数据复制到ViewModel中
     *
     * @param result
     * @return
     */
    private ATMWithDrawDetailsViewModel resultDetailsModelToViewModel(PsnPasswordRemitFreeDetailQueryResult result) {
        ATMWithDrawDetailsViewModel viewModel = new ATMWithDrawDetailsViewModel();
        viewModel.setRemitNumber(result.getRemitNumber());// 汇款编号
        viewModel.setPaymentAmount(result.getPaymentAmount());// 取款金额
        viewModel.setPaymentCode(result.getPaymentCode());// 取款币种
        viewModel.setFromName(result.getFromName());// 付款人姓名
        viewModel.setToName(result.getToName());// 收款人姓名
        viewModel.setRemitNumber(result.getRemitNumber());// 交易编号
        viewModel.setToMobile(result.getToMobile());// 收款人手机号
        viewModel.setDueDate(result.getDueDate());// 有效期至
        viewModel.setComment(result.getComment());// 附言
        viewModel.setStatus(result.getStatus());// 交易状态
        viewModel.setFromActNumber(result.getFromActNumber());// 付款人账号
        viewModel.setPaymentDate(result.getPaymentDate());// 付款日期
        return viewModel;
    }

    /**
     * 封装ATM无卡取款撤销请求参数
     *
     * @param atmWithDrawDetailsViewModel
     * @return
     */
    private PsnPasswordRemitFreeCancelParams buildPsnPasswordRemitFreeCancelParams(ATMWithDrawDetailsViewModel atmWithDrawDetailsViewModel) {
        PsnPasswordRemitFreeCancelParams params = new PsnPasswordRemitFreeCancelParams();
        params.setRemitNo(atmWithDrawDetailsViewModel.getRemitNumber());// 汇款编号
        params.setFreeRemitType(REMIT_TYPE_ATM_WITHDRAW);
        params.setAmount(atmWithDrawDetailsViewModel.getPaymentAmount());// 转账金额
        params.setCurrencyCode(atmWithDrawDetailsViewModel.getPaymentCode());// 币种
        params.setPayeeName(atmWithDrawDetailsViewModel.getToName());// 收款人姓名
        params.setPayeeMobile(atmWithDrawDetailsViewModel.getToMobile());// 收款人手机号
        params.setFromActNumber(atmWithDrawDetailsViewModel.getFromActNumber());// 付款人账号
        return params;
    }

    /**
     * 封装ATM无卡取款预交易请求参数
     *
     * @return
     */
    private PsnPasswordRemitPaymentPreParams buildPsnPasswordRemitPaymentPreParams(String accountID, String combinID, String money) {
        PsnPasswordRemitPaymentPreParams params = new PsnPasswordRemitPaymentPreParams();
        params.setConversationId(conversationID);// 会话ID
        params.setFromAccountId(accountID);// 账户ID
        params.setPayeeName(ApplicationContext.getInstance().getUser().getCustomerName());// 收款人姓名
        params.setPayeeMobile(ApplicationContext.getInstance().getUser().getMobile());// 收款人手机号
        params.setAmount(money);// 取款金额
        params.setCurrencyCode(ApplicationConst.CURRENCY_CNY);// 币种
        params.setFreeRemitType(REMIT_TYPE_ATM_WITHDRAW);// 汇款类型
        params.set_combinId(combinID);// 安全因子
        return params;
    }

    /**
     * 封装ATM无卡取款提交交易
     *
     * @return
     */
    private PsnPasswordRemitPaymentParams buildPsnPasswordRemitPaymentParams(String accountID,
                                                                             String[] randomNums,
                                                                             String[] encryptPasswords, int curCombinID, Context context, String devicePrint) {
        PsnPasswordRemitPaymentParams params = new PsnPasswordRemitPaymentParams();
        ATMWithDrawViewModel viewModel = ATMWithDrawFragment.getViewModel();
        params.setConversationId(conversationID);// 会话ID
        params.setFromAccountId(accountID);// 账户ID
        params.setPayeeName(ApplicationContext.getInstance().getUser().getCustomerName());// 姓名
        params.setPayeeMobile(ApplicationContext.getInstance().getUser().getMobile());// 手机号
        params.setAmount(viewModel.getMoney().toString());// 取款金额
        params.setCurrencyCode(ApplicationConst.CURRENCY_CNY);// 币种
        params.setFreeRemitType(REMIT_TYPE_ATM_WITHDRAW);// 汇款类型
        params.setATMWithdraw(ATM_WITHDRAW_YES);
        params.setObligatePassword(viewModel.getObligatePassword());
        params.setObligatePassword_RC(viewModel.getObligatePassword_RC());
        params.setReObligatePassword(viewModel.getReObligatePassword());
        params.setReObligatePassword_RC(viewModel.getReObligatePassword_RC());
        params.setFurInf(viewModel.getFurInf());// 附言
        params.setState(SecurityVerity.SECURITY_VERIFY_STATE);
        params.setActiv(SecurityVerity.getInstance().getCfcaVersion());
        params.setDevicePrint(devicePrint);// 设备指纹
        params.setDueDate(ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().toString());// 执行日期
        switch (curCombinID) {
            case SecurityVerity.SECURITY_VERIFY_TOKEN:// 动态口令
                params.setOtp(encryptPasswords[0]);
                params.setOtp_RC(randomNums[0]);
                break;
            case SecurityVerity.SECURITY_VERIFY_SMS:// 短信
                params.setSmc(encryptPasswords[0]);
                params.setSmc_RC(randomNums[0]);
                break;
            case SecurityVerity.SECURITY_VERIFY_SMS_AND_TOKEN:// 动态口令+短信
                params.setOtp(encryptPasswords[0]);
                params.setOtp_RC(randomNums[0]);
                params.setSmc(encryptPasswords[1]);
                params.setSmc_RC(randomNums[1]);
                break;
            case SecurityVerity.SECURITY_VERIFY_DEVICE:// 手机交易码+硬件绑定
                params.setSmc(encryptPasswords[0]);
                params.setSmc_RC(randomNums[0]);
                DeviceInfoModel deviceInfoModel = DeviceInfoUtils.getDeviceInfo(context, randomID);
                params.setDeviceInfo(deviceInfoModel.getDeviceInfo());
                params.setDeviceInfo_RC(deviceInfoModel.getDeviceInfo_RC());
                break;
            case SecurityVerity.SECURITY_VERIFY_E_TOKEN:// 中银e盾
                params.set_signedData(randomNums[0]);
                break;
            default:
                break;
        }
        return params;
    }

    /**
     * 账户详情数据复制到ViewModel中
     *
     * @param result
     * @return
     */
    private ATMWithDrawViewModel resultModelToViewModel(PsnAccountQueryAccountDetailResult result) {
        ATMWithDrawViewModel viewModel = ATMWithDrawFragment.getViewModel();
        viewModel.setCurrencyCode("-1");// 默认值
        for (PsnAccountQueryAccountDetailResult.AccountDetaiListBean item : result.getAccountDetaiList()) {
            if (ApplicationConst.CURRENCY_CNY.equals(item.getCurrencyCode())) {
                viewModel.setCurrencyCode(item.getCurrencyCode());// 币种
                viewModel.setAvailableBalance(item.getAvailableBalance());// 可用余额
            }
        }
        return viewModel;
    }

    /**
     * ATM无卡取款预交易响应数据
     *
     * @param result
     * @return
     */
    private void resultModelToViewModel(PsnPasswordRemitPaymentPreResult result) {
        ATMWithDrawViewModel viewModel = ATMWithDrawFragment.getViewModel();
        viewModel.set_certDN(result.get_certDN());
        viewModel.setSmcTrigerInterval(result.getSmcTrigerInterval());
        viewModel.set_plainData(result.get_plainData());
        List<FactorBean> list = new ArrayList<FactorBean>();
        for (PsnPasswordRemitPaymentPreResult.FactorListBean item : result.getFactorList()) {
            FactorBean bean = new FactorBean();
            FactorBean.FieldBean fieldBean = new FactorBean.FieldBean();
            fieldBean.setName(item.getField().getName());
            fieldBean.setType(item.getField().getType());
            bean.setField(fieldBean);
            list.add(bean);
        }
        viewModel.setFactorList(list);
    }

    @Override
    public void subscribe() {
        mRxLifecycleManager.onStart();
    }

    @Override
    public void unsubscribe() {
        mRxLifecycleManager.onDestroy();
    }
}
