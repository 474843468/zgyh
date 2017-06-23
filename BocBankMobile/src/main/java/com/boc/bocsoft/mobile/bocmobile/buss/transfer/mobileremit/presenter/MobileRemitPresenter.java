package com.boc.bocsoft.mobile.bocmobile.buss.transfer.mobileremit.presenter;

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
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileAgentQuery.PsnMobileAgentQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileAgentQuery.PsnMobileAgentQueryResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileRemitPre.PsnMobileRemitPreParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileRemitPre.PsnMobileRemitPreResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileRemitSubmit.PsnMobileRemitSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileRemitSubmit.PsnMobileRemitSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQuotaQuery.PsnTransQuotaQueryParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQuotaQuery.PsnTransQuotaQueryResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ServiceIdCodeConst;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.CombinBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.FactorBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.ListModel;
import com.boc.bocsoft.mobile.bocmobile.base.model.SecurityViewModel;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.mobileremit.model.AgentViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.mobileremit.model.MobileRemitViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.mobileremit.ui.MobileRemitContract;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.mobileremit.ui.MobileRemitFragment;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 汇往取款人
 * <p/>
 * Created by liuweidong on 2016/7/12.
 */
public class MobileRemitPresenter implements MobileRemitContract.Presenter {
    private RxLifecycleManager mRxLifecycleManager;
    private GlobalService globalService;// 公共Service
    private AccountService accountService;
    private TransferService transferService;// 转账汇款Service

    private MobileRemitContract.View mView;
    private MobileRemitContract.ResultView mResultView;
    private MobileRemitContract.AgentView mAgentView;

    public static String conversationID;// 会话ID
    public static String randomID = "";// 随机数ID
    private boolean isConfirmView = false;

    public MobileRemitPresenter() {
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        accountService = new AccountService();
        transferService = new TransferService();
    }

    public MobileRemitPresenter(MobileRemitContract.View view, boolean isConfirmView) {
        this();
        this.isConfirmView = isConfirmView;
        mView = view;
    }

    public MobileRemitPresenter(MobileRemitContract.AgentView agentView) {
        this();
        mAgentView = agentView;
    }

    public MobileRemitPresenter(MobileRemitContract.ResultView resultView, boolean isConfirmView) {
        this();
        this.isConfirmView = isConfirmView;
        mResultView = resultView;
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
                        mView.queryAccountDetailsFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnAccountQueryAccountDetailResult result) {
                        mView.queryAccountDetailsSuccess(resultModelToViewModel(result));
                    }
                });
    }

    /**
     * 代理点查询
     *
     * @param prvIbkNum
     */
    @Override
    public void queryAgent(String prvIbkNum, String currentIndex, String pageSize) {
        PsnMobileAgentQueryParams params = new PsnMobileAgentQueryParams();
        params.setPrvIbkNum(prvIbkNum);
        params.setCurrentIndex(currentIndex);
        params.setPageSize(pageSize);
        transferService.psnMobileAgentQuery(params)
                .compose(mRxLifecycleManager.<PsnMobileAgentQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnMobileAgentQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnMobileAgentQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mAgentView.queryAgentFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnMobileAgentQueryResult result) {
                        mAgentView.queryAgentSuccess(resultModelToViewModel(result));
                    }
                });
    }

    /**
     * 查询安全因子
     */
    @Override
    public void querySecurityFactor() {
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
                        psnGetSecurityFactorParams.setServiceId(ServiceIdCodeConst.SERVICE_ID_MOBILE_REMIT);
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
                        mView.querySecurityFactorFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
                        mView.querySecurityFactorSuccess(resultModelToViewModel(psnGetSecurityFactorResult));
                    }
                });
    }

    /**
     * 汇往取款人预交易
     *
     * @param accountID
     * @param combinID
     */
    @Override
    public void mobileRemitConfirm(String accountID, String combinID) {
        PsnMobileRemitPreParams params = buildPsnMobileRemitPreParams(accountID, combinID);
        transferService.psnMobileRemitPre(params)
                .compose(mRxLifecycleManager.<PsnMobileRemitPreResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnMobileRemitPreResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnMobileRemitPreResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        if (isConfirmView) {
                            mResultView.mobileRemitConfirmFail(biiResultErrorException);
                        } else {
                            mView.mobileRemitConfirmFail(biiResultErrorException);
                        }
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnMobileRemitPreResult result) {
                        resultModelToViewModel(result);
                        if (isConfirmView) {
                            mResultView.mobileRemitConfirmSuccess();
                        } else {
                            mView.mobileRemitConfirmSuccess();
                        }
                    }
                });
    }

    /**
     * 汇往取款人提交交易
     *
     * @param accountID
     * @param randomNums
     * @param encryptPasswords
     * @param curCombinID
     * @param context
     */
    @Override
    public void mobileRemitResult(final String accountID, final String[] randomNums,
                                  final String[] encryptPasswords, final int curCombinID, final Context context) {
        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
        psnGetTokenIdParams.setConversationId(conversationID);

        globalService.psnGetTokenId(psnGetTokenIdParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnMobileRemitSubmitResult>>() {
                    @Override
                    public Observable<PsnMobileRemitSubmitResult> call(String tokenID) {
                        PsnMobileRemitSubmitParams params = buildPsnMobileRemitSubmitParams(accountID,
                                randomNums, encryptPasswords, curCombinID, context);
                        params.setToken(tokenID);
                        return transferService.psnMobileRemitSubmit(params);
                    }
                }).compose(SchedulersCompat.<PsnMobileRemitSubmitResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnMobileRemitSubmitResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mResultView.mobileRemitResultFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnMobileRemitSubmitResult result) {
                        resultModelToViewModel(result);
                        mResultView.mobileRemitResultSuccess();
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
                        mView.psnTransQuotaqueryFail();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnTransQuotaQueryResult result) {
                        mView.psnTransQuotaquerySuccess(result.getQuotaAmount());
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {

                    }
                });
    }

    /**
     * 封装汇往取款人预交易
     *
     * @return
     */
    private PsnMobileRemitPreParams buildPsnMobileRemitPreParams(String accountID, String combinID) {
        PsnMobileRemitPreParams params = new PsnMobileRemitPreParams();
        MobileRemitViewModel viewModel = MobileRemitFragment.getViewModel();
        params.setConversationId(conversationID);
        params.setAccountId(accountID);
        params.setPayeeName(viewModel.getPayeeName());// 收款人姓名
        params.setPayeeMobile(viewModel.getPayeeMobile());// 收款人手机号
        params.setRemitAmount(viewModel.getMoney().toString());// 汇款金额
        params.setRemitCurrencyCode(ApplicationConst.CURRENCY_CNY);// 币种
        params.setRemark(viewModel.getRemark());// 附言
        params.set_combinId(combinID);
        return params;
    }

    /**
     * 封装汇往取款人提交交易
     *
     * @return
     */
    private PsnMobileRemitSubmitParams buildPsnMobileRemitSubmitParams(String accountID,
                                                                       String[] randomNums,
                                                                          String[] encryptPasswords, int curCombinID, Context context) {
        PsnMobileRemitSubmitParams params = new PsnMobileRemitSubmitParams();
        MobileRemitViewModel viewModel = MobileRemitFragment.getViewModel();
        params.setConversationId(conversationID);
        params.setAccountId(accountID);
        params.setPayeeName(viewModel.getPayeeName());// 收款人姓名
        params.setPayeeMobile(viewModel.getPayeeMobile());// 收款人手机号
        params.setRemitAmount(viewModel.getMoney().toString());// 汇款金额
        params.setRemitCurrencyCode(ApplicationConst.CURRENCY_CNY);// 币种
        params.setRemark(viewModel.getRemark());// 附言
        params.setWithDrawPwd(viewModel.getWithDrawPwd());// 取款密码
        params.setWithDrawPwd_RC(viewModel.getWithDrawPwd_RC());
        params.setWithDrawPwdConf(viewModel.getWithDrawPwdConf());// 确认密码
        params.setWithDrawPwdConf_RC(viewModel.getWithDrawPwdConf_RC());

        params.setState(SecurityVerity.SECURITY_VERIFY_STATE);
        params.setActiv(SecurityVerity.getInstance().getCfcaVersion());

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
     * 账户详情响应数据
     *
     * @param result
     * @return
     */
    private MobileRemitViewModel resultModelToViewModel(PsnAccountQueryAccountDetailResult result) {
        MobileRemitViewModel viewModel = MobileRemitFragment.getViewModel();
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
     * 代理点查询响应数据
     *
     * @param result
     * @return
     */
    private AgentViewModel resultModelToViewModel(PsnMobileAgentQueryResult result){
        AgentViewModel agentViewModel = new AgentViewModel();
        agentViewModel.setRecordNumber(Integer.valueOf(result.getRecordNumber()));
        List<ListModel> list = new ArrayList<>();
        for(PsnMobileAgentQueryResult.ListBean item : result.getList()){
            ListModel listModel = new ListModel();
            listModel.setName(item.getAgentName());
            listModel.setValue(item.getAgentAddress());
            list.add(listModel);
        }
        agentViewModel.setList(list);
        return agentViewModel;
    }

    /**
     * 安全因子响应数据
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
     * 汇往取款人预交易响应数据
     *
     * @param result
     * @return
     */
    private void resultModelToViewModel(PsnMobileRemitPreResult result) {
        MobileRemitViewModel viewModel = MobileRemitFragment.getViewModel();
        viewModel.set_certDN(result.get_certDN());
        viewModel.setSmcTrigerInterval(result.getSmcTrigerInterval());
        viewModel.set_plainData(result.get_plainData());
        List<FactorBean> list = new ArrayList<FactorBean>();
        for (PsnMobileRemitPreResult.FactorListBean item : result.getFactorList()) {
            FactorBean bean = new FactorBean();
            FactorBean.FieldBean fieldBean = new FactorBean.FieldBean();
            fieldBean.setName(item.getField().getName());
            fieldBean.setType(item.getField().getType());
            bean.setField(fieldBean);
            list.add(bean);
        }
        viewModel.setFactorList(list);
    }

    /**
     * 汇往取款人交易响应数据
     *
     * @param result
     */
    private void resultModelToViewModel(PsnMobileRemitSubmitResult result){
        MobileRemitViewModel viewModel = MobileRemitFragment.getViewModel();
        viewModel.setDueDate(result.getDueDate());// 到期日期
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
