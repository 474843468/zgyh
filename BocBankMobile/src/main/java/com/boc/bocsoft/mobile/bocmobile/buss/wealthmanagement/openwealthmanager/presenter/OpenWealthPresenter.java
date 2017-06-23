package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.openwealthmanager.presenter;


import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetRandom.PSNGetRandomParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvestmentManageCancel.PsnInvestmentManageCancelParam;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvestmentManageCancel.PsnInvestmentManageCancelResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvestmentManageOpen.PsnInvestmentManageOpenParam;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvestmentManageOpen.PsnInvestmentManageOpenResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvestmentManageOpenConfirm.PsnInvestmentManageOpenConfirmParam;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvestmentManageOpenConfirm.PsnInvestmentManageOpenConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ServiceIdCodeConst;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.openwealthmanager.model.OpenWealthModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.openwealthmanager.ui.OpenWealthManagerContact;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.openwealthmanager.ui.OpenWealthManagerFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.WealthConst;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.presenter.WealthResponseResult;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 开通投资理财服务
 * Created by wangtong on 2016/10/20.
 * Modified by liuweidong on 2016/12/2.
 */
public class OpenWealthPresenter implements OpenWealthManagerContact.Presenter {

    private RxLifecycleManager mRxLifecycleManager;
    private GlobalService globalService;// 公共
    private WealthManagementService mWealthService;// 理财
    private BussFragment bussFragment;
    private OpenWealthModel uiModel;
    private static String conversationId;// 会话ID

    public OpenWealthManagerContact.OpenStatusView openStatusView;// 开通理财公共页面
    public OpenWealthManagerContact.OpenView openView;// 开通投资理财服务
    public OpenWealthManagerContact.CloseView closeView;

    public OpenWealthPresenter(OpenWealthManagerContact.OpenStatusView view) {// 开通理财公共页面
        openStatusView = view;
        bussFragment = (BussFragment) view;
        initPresenter();
    }

    public OpenWealthPresenter(OpenWealthManagerContact.OpenView view) {// 开通投资理财服务
        openView = view;
        bussFragment = (BussFragment) view;
        uiModel = bussFragment.findFragment(OpenWealthManagerFragment.class).mViewModel;
        initPresenter();
    }

    public OpenWealthPresenter(OpenWealthManagerContact.CloseView view) {
        closeView = view;
        bussFragment = (BussFragment) view;
        uiModel = bussFragment.findFragment(OpenWealthManagerFragment.class).mViewModel;
        initPresenter();
    }

    /**
     * 初始化
     */
    private void initPresenter() {
        mRxLifecycleManager = new RxLifecycleManager();
        mWealthService = new WealthManagementService();
        globalService = new GlobalService();
    }

    /**
     * 查询客户理财账户信息
     */
    @Override
    public void queryFinanceAccountInfo() {
        bussFragment.showLoadingDialog(false);
        PsnXpadAccountQueryParams params = new PsnXpadAccountQueryParams();
        params.setQueryType("1");
        params.setXpadAccountSatus(WealthConst.YES_1);
        mWealthService.psnXpadAccountQuery(params)
                .compose(mRxLifecycleManager.<PsnXpadAccountQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnXpadAccountQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadAccountQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        bussFragment.closeProgressDialog();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnXpadAccountQueryResult result) {
                        WealthResponseResult.copyResponseAccount(result);
                        openStatusView.getAccountSuccess();
                    }
                });
    }

    /**
     * 查询安全因子与随机数
     */
    @Override
    public void psnGetSecurityFactor() {
        final OpenWealthModel viewModel = new OpenWealthModel();
        PSNCreatConversationParams conversationPreParams = new PSNCreatConversationParams();
        bussFragment.showLoadingDialog(false);
        globalService.psnCreatConversation(conversationPreParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnGetSecurityFactorResult>>() {
                    @Override
                    public Observable<PsnGetSecurityFactorResult> call(String s) {
                        PsnGetSecurityFactorParams mSecurityFactorParams = new PsnGetSecurityFactorParams();
                        conversationId = s;
                        mSecurityFactorParams.setConversationId(s);
                        mSecurityFactorParams.setServiceId(ServiceIdCodeConst.SERVICE_ID_INVEST_TREATY);
                        return globalService.psnGetSecurityFactor(mSecurityFactorParams);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<PsnGetSecurityFactorResult, Observable<String>>() {
                    @Override
                    public Observable<String> call(PsnGetSecurityFactorResult result) {
                        SecurityFactorModel factor = new SecurityFactorModel(result);
                        // 安全组件设置会话ID
                        SecurityVerity.getInstance(bussFragment.getActivity()).setConversationId(conversationId);
                        viewModel.setFactorModel(factor);// 设置安全因子
                        PSNGetRandomParams params = new PSNGetRandomParams();
                        params.setConversationId(conversationId);
                        return globalService.psnGetRandom(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<String>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        bussFragment.closeProgressDialog();
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(String s) {
                        viewModel.setRandomNum(s);// 设置随机数
                        openStatusView.psnGetSecurityFactorReturned(viewModel);
                        bussFragment.closeProgressDialog();
                    }
                });
    }

    /**
     * 开通投资服务确认
     */
    @Override
    public void psnInvestmentManageOpenConfirm() {
        bussFragment.showLoadingDialog(false);
        PsnInvestmentManageOpenConfirmParam param = new PsnInvestmentManageOpenConfirmParam();
        param.setConversationId(conversationId);
        param.set_combinId(uiModel.selectedFactorId);
        mWealthService.psnInvestmentManageOpenConfirm(param)
                .compose(mRxLifecycleManager.<PsnInvestmentManageOpenConfirmResult>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnInvestmentManageOpenConfirmResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        bussFragment.closeProgressDialog();
                        bussFragment.showErrorDialog(biiResultErrorException.getErrorMessage());
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnInvestmentManageOpenConfirmResult result) {
                        uiModel.preFactorList = result.getFactorList();
                        uiModel.mPlainData = result.get_plainData();
                        openView.psnInvestmentManageOpenConfirmReturned();
                        bussFragment.closeProgressDialog();
                    }
                });
    }

    @Override
    public void psnInvestmentManageOpen() {
        PSNGetTokenIdParams params = new PSNGetTokenIdParams();
        params.setConversationId(conversationId);
        bussFragment.showLoadingDialog(false);
        globalService.psnGetTokenId(params)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnInvestmentManageOpenResult>>() {
                    @Override
                    public Observable<PsnInvestmentManageOpenResult> call(String s) {
                        PsnInvestmentManageOpenParam param = new PsnInvestmentManageOpenParam();
                        param.setConversationId(conversationId);
                        param.setToken(s);
                        if (uiModel.selectedFactorId.equals("8")) {
                            param.setOtp(uiModel.encryptPasswords[0]);
                            param.setOtp_RC(uiModel.encryptRandomNums[0]);
                        } else if (uiModel.selectedFactorId.equals("32")) {
                            param.setSmc(uiModel.encryptPasswords[0]);
                            param.setSmc_RC(uiModel.encryptRandomNums[0]);
                        } else if (uiModel.selectedFactorId.equals("40")) {
                            param.setOtp(uiModel.encryptPasswords[0]);
                            param.setOtp_RC(uiModel.encryptRandomNums[0]);
                            param.setSmc(uiModel.encryptPasswords[1]);
                            param.setSmc_RC(uiModel.encryptRandomNums[1]);
                        } else if (uiModel.selectedFactorId.equals("96")) {
                            param.setSmc(uiModel.encryptPasswords[0]);
                            param.setSmc_RC(uiModel.encryptRandomNums[0]);
                            DeviceInfoModel info = DeviceInfoUtils.getDeviceInfo(bussFragment.getActivity(),
                                    uiModel.getRandomNum());
                            param.setDeviceInfo(info.getDeviceInfo());
                            param.setDeviceInfo_RC(info.getDeviceInfo_RC());
                            param.setDevicePrint(DeviceInfoUtils.getDevicePrint(bussFragment.getActivity()));
                        } else if (uiModel.selectedFactorId.equals("4")) {
                            param.set_signedData(uiModel.mSignData);
                        }
                        param.setActiv(SecurityVerity.getInstance().getCfcaVersion());
                        param.setState(SecurityVerity.SECURITY_VERIFY_STATE);
                        return mWealthService.psnInvestmentManageOpen(param);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnInvestmentManageOpenResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        bussFragment.closeProgressDialog();
                        bussFragment.showErrorDialog(biiResultErrorException.getErrorMessage());
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnInvestmentManageOpenResult psnInvestmentManageOpenResult) {
                        openView.psnInvestmentManageOpenReturned();
                        bussFragment.closeProgressDialog();
                    }
                });
    }

    @Override
    public void psnInvestmentManageCancel() {
        PSNGetTokenIdParams params = new PSNGetTokenIdParams();
        bussFragment.showLoadingDialog(false);
        globalService.psnGetTokenId(params)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<PsnInvestmentManageCancelResult>>() {
                    @Override
                    public Observable<PsnInvestmentManageCancelResult> call(String s) {
                        PsnInvestmentManageCancelParam param = new PsnInvestmentManageCancelParam();
                        param.setToken(s);
                        return mWealthService.psnInvestmentManageCancel(param);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnInvestmentManageCancelResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        bussFragment.closeProgressDialog();
                        bussFragment.showErrorDialog(biiResultErrorException.getErrorMessage());
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnInvestmentManageCancelResult psnInvestmentManageOpenResult) {
                        closeView.psnInvestmentManageCancelReturned();
                        bussFragment.closeProgressDialog();
                    }
                });
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
