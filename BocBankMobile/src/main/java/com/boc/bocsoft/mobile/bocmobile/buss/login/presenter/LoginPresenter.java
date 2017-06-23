package com.boc.bocsoft.mobile.bocmobile.buss.login.presenter;


import android.util.Log;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.login.model.LoginWP7.LoginWP7Params;
import com.boc.bocsoft.mobile.bii.bus.login.model.LoginWP7.LoginWP7Result;
import com.boc.bocsoft.mobile.bii.bus.login.model.PSNCreatConversationLoginPre.PSNCreatConversationLoginPreParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.PSNGetRandomLoginPre.PSNGetRandomLoginPreParams;
import com.boc.bocsoft.mobile.bii.bus.login.service.PsnLoginService;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.login.model.LoginViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.login.ui.LoginContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by feibin on 2016/5/17.
 * 登录BII通信逻辑处理
 */
public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mLoginView;
    private RxLifecycleManager mRxLifecycleManager;
    /**
     * 登录service
     */
    private PsnLoginService psnLoginService;
    /**
     * 登录前会话
     */
    private String conversationPre;

    /**
     * 登录前随机数
     */
    private String randomPre;

    /**
     * 登录UI层model
     */
    private LoginViewModel mLoginModel;
    /**
     * 选择的安全认证方式
     */
    private CombinListBean mSelectCombinListBean;

    public LoginPresenter(LoginContract.View loginView) {
        mLoginView = loginView;
        psnLoginService = new PsnLoginService();
        mRxLifecycleManager = new RxLifecycleManager();

    }

    /**
     * 获取随机数
     */
    @Override
    public void queryLogin() {
        //请求登录前会话ID
        PSNCreatConversationLoginPreParams conversationPreParams = new PSNCreatConversationLoginPreParams();
        psnLoginService.pSNCreatConversationLoginPre(conversationPreParams)
                //将任务绑定到mRxLifecycleManager上，调用将任务绑定到mRxLifecycleManager.onDestroy即可取消任务
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversation) {
                        conversationPre = conversation;
                        //请求登录前随机数
                        PSNGetRandomLoginPreParams randomPreParams = new PSNGetRandomLoginPreParams();
                        randomPreParams.setConversationId(conversationPre);
                        return psnLoginService.pSNGetRandomLoginPre(randomPreParams);
                    }
                })
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .map(new Func1<String, LoginViewModel>() {
                    @Override
                    public LoginViewModel call(String random) {
                        randomPre = random;
                        LoginViewModel model = mLoginView.randomPreSuccess(randomPre);
                        return model;
                    }
                })
                .filter(new Func1<LoginViewModel, Boolean>() {
                    @Override
                    public Boolean call(LoginViewModel loginViewModel) {
                           return loginViewModel != null;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<LoginViewModel, Observable<LoginWP7Result>>() {
                    @Override
                    public Observable<LoginWP7Result> call(LoginViewModel loginModel) {
                        //存储UI层model
                        mLoginModel = loginModel;
                        LoginWP7Params loginWP7Params = buildLoginParams(loginModel);
                        return psnLoginService.loginWP7(loginWP7Params);
                    }
                })
                //设定subscribeOn为io线程，observeOn为主线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<LoginWP7Result>() {
                    @Override
                    public void onCompleted() {
                        Log.i("feib", "----------onCompleted-------------");
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        Log.i("feib", "-----------handleException-------------");
                        mLoginView.loginFail(biiResultErrorException);
                    }

                    @Override
                    public void onNext(LoginWP7Result loginWP7ResultBean) {
                        Log.i("feib", "-----------onNext-------------");
                        copyLoginResult2UIModel(loginWP7ResultBean);
                        mLoginView.loginSuccess(mLoginModel);
                    }
                });
    }

    /**
     * 封装请求登录参数
     *
     * @param loginModel
     */
    private LoginWP7Params buildLoginParams(LoginViewModel loginModel) {
        LoginWP7Params loginWP7Params = new LoginWP7Params();
        loginWP7Params.setConversationId(conversationPre);
        loginWP7Params.setWp7LoginType("2");
        loginWP7Params.setSegment("1");
        loginWP7Params.setPassword(loginModel.getPassword());
        loginWP7Params.setPassword_RC(loginModel.getPassword_RC());
        loginWP7Params.setValidationChar(loginModel.getNeedValidationChars());
        loginWP7Params.setLoginName(loginModel.getLoginName());
        loginWP7Params.setActiv(loginModel.getActiv());
        loginWP7Params.setState(loginModel.getState());
        return loginWP7Params;
    }

    /**
     * 转换登录结果数据到UI层model
     */
    private void copyLoginResult2UIModel(LoginWP7Result params) {
        mLoginModel.setName(params.getName());
        mLoginModel.setCombinStatus(params.getCombinStatus());
        mLoginModel.setNeedValidationChars(params.getNeedValidationChars());
        mLoginModel.setSegment(params.getSegmentId());
        mLoginModel.setCustomerId(params.getCustomerId());
        mLoginModel.setRegtype(params.getRegtype());
        mLoginModel.setMobile(params.getMobile());
        mLoginModel.setLoginHint(params.getLoginHint());
        mLoginModel.setIdentityNumber(params.getIdentityNumber());
        mLoginModel.setUserId(params.getUserId());
        mLoginModel.setLoginStatus(params.getLoginStatus());
    }

    @Override
    public void subscribe() {
        //onResume时需要做的工作
    }

    @Override
    public void unsubscribe() {
        //防止外界已经销毁，而后台线程的任务还在执行
        mRxLifecycleManager.onDestroy();
    }
}
