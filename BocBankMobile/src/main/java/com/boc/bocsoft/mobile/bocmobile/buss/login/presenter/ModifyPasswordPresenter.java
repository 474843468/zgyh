package com.boc.bocsoft.mobile.bocmobile.buss.login.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetRandom.PSNGetRandomParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.login.model.Logout.LogoutParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.Logout.LogoutResult;
import com.boc.bocsoft.mobile.bii.bus.login.service.PsnLoginService;
import com.boc.bocsoft.mobile.bii.bus.setting.model.NamePasswordModMobile.NamePasswordModMobileParams;
import com.boc.bocsoft.mobile.bii.bus.setting.model.NamePasswordModMobile.NamePasswordModMobileResult;
import com.boc.bocsoft.mobile.bii.bus.setting.service.PsnSettingService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.login.model.LoginViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.login.model.ModifyPasswordViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.login.ui.ModifyPasswordContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by feib on 16/7/14.
 */
public class ModifyPasswordPresenter implements ModifyPasswordContract.Presenter{

    private ModifyPasswordContract.View mModifyPasswordView;
    private RxLifecycleManager mRxLifecycleManager;
    /**
     * 会话
     */
    private String conversation;
    /**
     * 随机数
     */
    private String random;
    /**
     * 修改密码请求Model
     */
    private NamePasswordModMobileParams namePasswordModMobileParams;
    /**
     * 公共service
     */
    private GlobalService globalService;
    /**
     * 个人设定service
     */
    private PsnSettingService psnSettingService;
    /**
     * 登录service
     */
    private PsnLoginService psnLoginService;

    public ModifyPasswordPresenter(ModifyPasswordContract.View modifyPasswordView) {
        mModifyPasswordView = modifyPasswordView;
        mModifyPasswordView.setPresenter(this);
        globalService = new GlobalService();
        psnSettingService = new PsnSettingService();
        mRxLifecycleManager = new RxLifecycleManager();
        namePasswordModMobileParams = new NamePasswordModMobileParams();
        psnLoginService = new PsnLoginService();
    }
    @Override
    public void queryNamePasswordModMobile() {

        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversationResult) {
                        conversation = conversationResult;
                        PSNGetRandomParams psnGetRandomParams = new PSNGetRandomParams();
                        psnGetRandomParams.setConversationId(conversation);
                        return globalService.psnGetRandom(psnGetRandomParams);
                    }
                })
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .map(new Func1<String, ModifyPasswordViewModel>() {
                    @Override
                    public ModifyPasswordViewModel call(String randomReu) {
                        random = randomReu;
                        return mModifyPasswordView.randomSuccess(random);
                    }
                })
                .filter(new Func1<ModifyPasswordViewModel, Boolean>() {
                    @Override
                    public Boolean call(ModifyPasswordViewModel modifyPasswordView) {
                        return modifyPasswordView != null;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<ModifyPasswordViewModel, Observable<NamePasswordModMobileResult>>() {
                    @Override
                    public Observable<NamePasswordModMobileResult> call(ModifyPasswordViewModel modifyPasswordViewModel) {
                        namePasswordModMobileParams.setConversationId(conversation);
                        copyModifyPasswordViewModel2BII(modifyPasswordViewModel);
                        return psnSettingService.namePasswordModMobile(namePasswordModMobileParams);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<NamePasswordModMobileResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(NamePasswordModMobileResult namePasswordModMobileResult) {

                        mModifyPasswordView.namePasswordModMobileSuccess();
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
                        mModifyPasswordView.logoutSuccess();
                    }
                });
    }

    /**
     * 转换登录结果数据到UI层model
     */
    private void copyModifyPasswordViewModel2BII(ModifyPasswordViewModel params){
        namePasswordModMobileParams.setActiv(params.getActiv());
        namePasswordModMobileParams.setCardNoSixLast(params.getCardNoSixLast());
        namePasswordModMobileParams.setCombinId(params.getCombinId());
        namePasswordModMobileParams.setLoginName(params.getLoginName());
        namePasswordModMobileParams.setNewPass(params.getNewPass());
        namePasswordModMobileParams.setNewPass2(params.getNewPass2());
        namePasswordModMobileParams.setNewPass_RC(params.getNewPass_RC());
        namePasswordModMobileParams.setNewPass2_RC(params.getNewPass2_RC());
        namePasswordModMobileParams.setOldPass(params.getOldPass());
        namePasswordModMobileParams.setOldPass_RC(params.getOldPass_RC());
        namePasswordModMobileParams.setState(params.getState());

    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        //防止外界已经销毁，而后台线程的任务还在执行
        mRxLifecycleManager.onDestroy();
    }
}
