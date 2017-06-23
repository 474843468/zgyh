package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.presenter;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcd3DQueryCertifInfo.PsnCrcd3DQueryCertifInfoParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcd3DQueryCertifInfo.PsnCrcd3DQueryCertifInfoResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayBillSetInput.PsnCrcdDividedPayBillSetInputParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayBillSetInput.PsnCrcdDividedPayBillSetInputResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryGeneralInfo.PsnCrcdQueryGeneralInfoParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryGeneralInfo.PsnCrcdQueryGeneralInfoResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQuerySettingsInfo.PsnCrcdQuerySettingsInfoParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQuerySettingsInfo.PsnCrcdQuerySettingsInfoResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnQueryCrcdBillIsExist.PsnQueryCrcdBillIsExistParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnQueryCrcdBillIsExist.PsnQueryCrcdBillIsExistResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnQueryCrcdPoint.PsnQueryCrcdPointParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnQueryCrcdPoint.PsnQueryCrcdPointResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.service.CrcdService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.model.CrcdModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.model.CrcdSettingsInfoBean;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.ui.CrcdContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.functions.Func1;

/**
 * 信用卡首页与详情网络请求
 * Created by liuweidong on 2016/11/22.
 */
public class CrcdPresenter implements CrcdContract.Presenter {
    private RxLifecycleManager mRxLifecycleManager;
    private GlobalService globalService;// 公共
    private CrcdService crcdService;// 信用卡

    private BussFragment bussFragment;
    private CrcdContract.HomeView homeView;// 首页
    private CrcdContract.MenuView menuView;// 详情

    public CrcdPresenter() {
        mRxLifecycleManager = new RxLifecycleManager();
        globalService = new GlobalService();
        crcdService = new CrcdService();
    }

    public CrcdPresenter(CrcdContract.HomeView homeView) {// 首页
        this();
        bussFragment = (BussFragment) homeView;
        this.homeView = homeView;
    }

    public CrcdPresenter(CrcdContract.MenuView menuView) {// 详情
        this();
        bussFragment = (BussFragment) menuView;
        this.menuView = menuView;
    }

    /**
     * 信用卡综合信息查询
     */
    @Override
    public void queryCrcdGeneralInfo(final CrcdModel item) {
        bussFragment.showLoadingDialog(false);
        PsnCrcdQueryGeneralInfoParams params = new PsnCrcdQueryGeneralInfoParams();
        params.setAccountId(item.getAccountBean().getAccountId());
        crcdService.psnCrcdQueryGeneralInfo(params)
                .compose(mRxLifecycleManager.<PsnCrcdQueryGeneralInfoResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdQueryGeneralInfoResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdQueryGeneralInfoResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        bussFragment.closeProgressDialog();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCrcdQueryGeneralInfoResult result) {
                        CrcdResponseResult.copyResultCrcdInfo(item, result);
//                        BeanConvertor.toBean(result, item);
                        homeView.queryCrcdGeneralInfoSuccess();
                    }
                });
    }

    /**
     * 信用卡当月是否已出账单查询
     *
     * @param item
     */
    @Override
    public void queryCrcdBillsExist(final CrcdModel item) {
        PsnQueryCrcdBillIsExistParams params = new PsnQueryCrcdBillIsExistParams();
        params.setAccountId(item.getAccountBean().getAccountId());
        crcdService.psnQueryCrcdBillIsExist(params)
                .compose(mRxLifecycleManager.<PsnQueryCrcdBillIsExistResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnQueryCrcdBillIsExistResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnQueryCrcdBillIsExistResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        bussFragment.closeProgressDialog();
                        homeView.queryCrcdBillsExistFail();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnQueryCrcdBillIsExistResult result) {
                        bussFragment.closeProgressDialog();
                        item.setIsBillExist(result.getIsBillExist());
                        homeView.queryCrcdBillsExistSuccess();
                    }
                });
    }

    /**
     * 4.103 103信用卡积分查询PsnQueryCrcdPoint
     *
     * @param item
     */
    @Override
    public void queryCrcdPoint(final CrcdModel item) {
        bussFragment.showLoadingDialog();
        PsnQueryCrcdPointParams params = new PsnQueryCrcdPointParams();
        params.setAccountId(item.getAccountBean().getAccountId());
        crcdService.psnQueryCrcdPoint(params)
                .compose(mRxLifecycleManager.<PsnQueryCrcdPointResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnQueryCrcdPointResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnQueryCrcdPointResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        bussFragment.closeProgressDialog();
                        homeView.queryCrcdPointFail();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnQueryCrcdPointResult result) {
                        item.setCrcdPoint(result.getCrcdPoint());
                        homeView.queryCrcdPointSuccess();
                        bussFragment.closeProgressDialog();
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }
                });
    }

    /**
     * 4.1 001还款方式查询 PsnCrcdQueryCrcdPaymentWay
     *
     * @param accountID
     */
    @Override
    public void queryCrcdPaymentWay(String accountID) {

    }

    /**
     * 4.67 067全球交易人民币记账功能查询 PsnCrcdChargeOnRMBAccountQuery
     *
     * @param accountID
     */
    @Override
    public void queryChargeOnRMBAccount(String accountID) {

    }

    /**
     * 4.95 095信用卡设置类信息查询 PsnCrcdQuerySettingsInfo
     *
     * @param accountID
     */
    @Override
    public void querySettingsInfo(final String accountID) {
        //查询之前请求会话
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnCrcdQuerySettingsInfoResult>>() {
                    @Override
                    public Observable<PsnCrcdQuerySettingsInfoResult> call(String conversation) {
                        // 信用卡设置类信息查询
                        PsnCrcdQuerySettingsInfoParams params = new PsnCrcdQuerySettingsInfoParams();
                        params.setConversationId(conversation);
                        params.setAccountId(accountID);
                        return crcdService.psnCrcdQuerySettingsInfo(params);
                    }
                })
                .compose(SchedulersCompat.<PsnCrcdQuerySettingsInfoResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdQuerySettingsInfoResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCrcdQuerySettingsInfoResult result) {
                        CrcdSettingsInfoBean settingsInfoBean = new CrcdSettingsInfoBean();
                        BeanConvertor.toBean(result, settingsInfoBean);
                        menuView.querySettingsInfoSuccess(settingsInfoBean);
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }
                });
    }


    /**
     * 4.106 106 3D安全认证信息查询 PsnCrcd3DQueryCertifInfo
     *
     * @param accountID
     */
    @Override
    public void query3DCertifInfo(String accountID) {
        // 3D安全认证信息查询
        PsnCrcd3DQueryCertifInfoParams params = new PsnCrcd3DQueryCertifInfoParams();
        params.setAccountId(accountID);
        crcdService.psnCrcd3DQueryCertifInfo(params)
                .compose(mRxLifecycleManager.<PsnCrcd3DQueryCertifInfoResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcd3DQueryCertifInfoResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcd3DQueryCertifInfoResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCrcd3DQueryCertifInfoResult result) {
                        menuView.query3DCertifInfoSuccess(result.getOpenFlag());
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }
                });
    }

    /**
     * 4.29 029办理账单分期输入PsnCrcdDividedPayBillSetInput
     *
     * @param accountID
     */
    @Override
    public void queryBillInput(String accountID) {
        PsnCrcdDividedPayBillSetInputParams params = new PsnCrcdDividedPayBillSetInputParams();
        params.setAccountId(accountID);
        params.setCurrencyCode(ApplicationConst.CURRENCY_CNY);
        bussFragment.showLoadingDialog();
        crcdService.psnCrcdDividedPayBillSetInput(params)
                .compose(mRxLifecycleManager.<PsnCrcdDividedPayBillSetInputResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdDividedPayBillSetInputResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdDividedPayBillSetInputResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        bussFragment.closeProgressDialog();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCrcdDividedPayBillSetInputResult result) {
                        bussFragment.closeProgressDialog();
                        homeView.queryBillInputSuccess(result.getUpInstmtAmount(), result.getLowInstmtAmount());
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
