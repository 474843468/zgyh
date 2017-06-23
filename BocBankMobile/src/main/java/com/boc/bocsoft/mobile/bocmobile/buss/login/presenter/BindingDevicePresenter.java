package com.boc.bocsoft.mobile.bocmobile.buss.login.presenter;

import android.util.Log;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetRandom.PSNGetRandomParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.login.model.Logout.LogoutParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.Logout.LogoutResult;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.PsnSvrRegisterDevicePreParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.PsnSvrRegisterDevicePreResult;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDeviceSubmit.PsnSvrRegisterDeviceSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDeviceSubmit.PsnSvrRegisterDeviceSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.login.service.PsnLoginService;
import com.boc.bocsoft.mobile.bii.bus.setting.service.PsnSettingService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.login.ui.BindingDeviceContrct;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by feibin on 16/8/15.
 */
public class BindingDevicePresenter implements BindingDeviceContrct.Presenter{

    private RxLifecycleManager mRxLifecycleManager;
    private BindingDeviceContrct.View mBindingDeviceView;
    /**
     * 登录service
     */
    private PsnLoginService psnLoginService;
    /**
     * 个人设定service
     */
    private PsnSettingService psnSettingService;
    /**
     * 公共service
     */
    private GlobalService globalService;
    /**
     * 登录前会话
     */
    private String conversationPre;
    /**
     * 会话
     */
    private String conversation;
    /**
     * 登录前随机数
     */
    private String randomPre;
    /**
     * 随机数
     */
    private String random;

    /**
     * 选择的安全认证方式
     */
    private CombinListBean mSelectCombinListBean;

    public BindingDevicePresenter(BindingDeviceContrct.View mView) {
        mBindingDeviceView = mView;
        psnLoginService = new PsnLoginService();
        psnSettingService = new PsnSettingService();
        globalService = new GlobalService();
        mRxLifecycleManager = new RxLifecycleManager();

    }

    /**
     * 获取安全因子
     */
    @Override
    public void querySecurityFactor() {

        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnGetSecurityFactorResult>>() {
                    @Override
                    public Observable<PsnGetSecurityFactorResult> call(String conversationRes) {
                        conversation = conversationRes;
                        PsnGetSecurityFactorParams mSecurityFactorParams = new PsnGetSecurityFactorParams();
                        mSecurityFactorParams.setConversationId(conversation);
                        //硬件绑定服务码
                        mSecurityFactorParams.setServiceId("PB107");
                        return globalService.psnGetSecurityFactor(mSecurityFactorParams);
                    }
                })
                .compose(SchedulersCompat.<PsnGetSecurityFactorResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnGetSecurityFactorResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        Log.i("feib","-----------handleExceptionWealth-获取安全因子------------");
                    }

                    @Override
                    public void onCompleted() {
                        Log.i("feib","-----------onCompleted-获取安全因子------------");
                    }

                    @Override
                    public void onNext(PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
                        Log.i("feib","-----------onNext-获取安全因子------------");
                        mBindingDeviceView.securityFactorSuccess(psnGetSecurityFactorResult);
                    }
                });

    }

    /**
     * 获取随机数
     */
    @Override
    public void queryRandom() {
        PSNGetRandomParams psnGetRandomParams = new PSNGetRandomParams();
        psnGetRandomParams.setConversationId(conversation);
        globalService.psnGetRandom(psnGetRandomParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<String>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onNext(String random) {
                        SecurityVerity.getInstance().setConversationId(conversation);
                        mBindingDeviceView.randomSuccess(random);
                    }
                });
    }

    /**
     * 设备注册预交易
     * 预交易会话和获取安全因子会话必须一致
     */
    @Override
    public void queryPsnSvrRegisterDevicePre( CombinListBean mSecurityFactorResult) {
        mSelectCombinListBean = mSecurityFactorResult;
        //设备注册预交易参数封装
        PsnSvrRegisterDevicePreParams psnSvrRegisterDevicePreParams = new PsnSvrRegisterDevicePreParams();
        psnSvrRegisterDevicePreParams.setConversationId(conversation);
        psnSvrRegisterDevicePreParams.set_combinId(mSelectCombinListBean.getId());
        psnLoginService.psnSvrRegisterDevicePre(psnSvrRegisterDevicePreParams)
                .compose(mRxLifecycleManager.<PsnSvrRegisterDevicePreResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnSvrRegisterDevicePreResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnSvrRegisterDevicePreResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnSvrRegisterDevicePreResult psnSvrRegisterDevicePreResult) {
                        mBindingDeviceView.svrRegisterDevicePreSuccess(psnSvrRegisterDevicePreResult);
                    }
                });
    }


    /**
     * 硬件绑定提交交易
     * @param mParams
     */
    @Override
    public void queryPsnSvrRegisterDeviceSubmit(PsnSvrRegisterDeviceSubmitParams mParams) {

        final PsnSvrRegisterDeviceSubmitParams mDeviceSubmitParams = mParams;
        //获取token
        PSNGetTokenIdParams mTokenIdParams = new PSNGetTokenIdParams();
        mTokenIdParams.setConversationId(conversation);
        globalService.psnGetTokenId(mTokenIdParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnSvrRegisterDeviceSubmitResult>>() {
                    @Override
                    public Observable<PsnSvrRegisterDeviceSubmitResult> call(String token) {
                        mDeviceSubmitParams.setConversationId(conversation);
                        mDeviceSubmitParams.setToken(token);
                        return psnLoginService.psnSvrRegisterDeviceSubmit(mDeviceSubmitParams);
                    }
                })
                .compose(SchedulersCompat.<PsnSvrRegisterDeviceSubmitResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnSvrRegisterDeviceSubmitResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                    	mBindingDeviceView.svrRegisterDeviceSubmitFail();

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnSvrRegisterDeviceSubmitResult psnSvrRegisterDeviceSubmitResult) {
                        Log.i("feib", "---------硬件绑定成功present------->");
                        mBindingDeviceView.svrRegisterDeviceSubmitSuccess();
                    }
                });

    }


    /**
     * 退出
     */
    @Override
    public void queryLogout() {
        LogoutParams logoutParams = new LogoutParams();
        psnLoginService.logout(logoutParams)
                .compose(mRxLifecycleManager.<LogoutResult>bindToLifecycle())
                .compose(SchedulersCompat.<LogoutResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<LogoutResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(LogoutResult logoutResult) {
                        mBindingDeviceView.logoutSuccess();
                    }
                });
    }


    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}
