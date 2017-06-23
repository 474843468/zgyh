package com.boc.bocsoft.mobile.bocmobile.buss.login.presenter;

import com.boc.bocsoft.mobile.bii.bus.login.PsnAccBocnetQryLoginInfo.PsnAccBocnetQryLoginInfoParams;
import com.boc.bocsoft.mobile.bii.bus.login.PsnAccBocnetQryLoginInfo.PsnAccBocnetQryLoginInfoResult;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetCreateConversationPre.PsnAccBocnetCreateConversationPreParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetGetRandomPre.PsnAccBocnetGetRandomPreParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetLogin.PsnAccBocnetLoginParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetLogin.PsnAccBocnetLoginResult;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetQryCardType.PsnAccBocnetQryCardTypeParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetQryCardType.PsnAccBocnetQryCardTypeResult;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetQryCrcdPoint.PsnAccBocnetQryCrcdPointParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetQryCrcdPoint.PsnAccBocnetQryCrcdPointResult;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetQryDebitDetail.PsnAccBocnetQryDebitDetailParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetQryDebitDetail.PsnAccBocnetQryDebitDetailResult;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetQueryGeneralInfo.PsnAccBocnetQueryGeneralInfoParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetQueryGeneralInfo.PsnAccBocnetQueryGeneralInfoResult;
import com.boc.bocsoft.mobile.bii.bus.login.service.PsnLoginService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.login.model.CardBalanceLoginViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.login.model.CreditCardDetailViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.login.model.DebitCardDetailViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.login.ui.CardBalanceQueryContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by feib on 16/8/2.
 */
public class CardBalanceQueryPresenter implements CardBalanceQueryContract.Presenter {

    private CardBalanceQueryContract.View cardBalanceView;
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
     * 卡号登录view Model
     */
    private CardBalanceLoginViewModel mViewModel;
    /**
     * 信用卡信息
     */
    private CreditCardDetailViewModel creditCardDetailViewModel;
    /**
     * 借记卡信息
     */
    private DebitCardDetailViewModel debitCardDetailViewModel;

    public CardBalanceQueryPresenter(CardBalanceQueryContract.View balanceView) {
        cardBalanceView = balanceView;
        cardBalanceView.setPresenter(this);
        psnLoginService = new PsnLoginService();
        mRxLifecycleManager = new RxLifecycleManager();
        creditCardDetailViewModel = new CreditCardDetailViewModel();
        debitCardDetailViewModel = new DebitCardDetailViewModel();
    }

    /**
     * 卡号登录
     */
    @Override
    public void queryLogin() {

        //请求登录前会话ID
        PsnAccBocnetCreateConversationPreParams conversationPreParams = new PsnAccBocnetCreateConversationPreParams();
        psnLoginService.psnAccBocnetCreateConversationPre(conversationPreParams)
                //将任务绑定到mRxLifecycleManager上，调用将任务绑定到mRxLifecycleManager.onDestroy即可取消任务
                .compose(mRxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversation) {
                        conversationPre = conversation;
                        //请求登录前随机数
                        PsnAccBocnetGetRandomPreParams randomPreParams = new PsnAccBocnetGetRandomPreParams();
                        randomPreParams.setConversationId(conversationPre);
                        return psnLoginService.psnAccBocnetGetRandomPre(randomPreParams);
                    }
                })
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .map(new Func1<String, CardBalanceLoginViewModel>() {
                    @Override
                    public CardBalanceLoginViewModel call(String random) {
                        randomPre = random;
                        return cardBalanceView.randomPreSuccess(randomPre);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<CardBalanceLoginViewModel, Observable<PsnAccBocnetLoginResult>>() {
                    @Override
                    public Observable<PsnAccBocnetLoginResult> call(CardBalanceLoginViewModel cardBalanceLoginViewModel) {
                        mViewModel = cardBalanceLoginViewModel;
                        PsnAccBocnetLoginParams psnAccBocnetLoginParams = new PsnAccBocnetLoginParams();
                        psnAccBocnetLoginParams.setConversationId(conversationPre);
                        BeanConvertor.toBean(cardBalanceLoginViewModel,psnAccBocnetLoginParams);
                        return psnLoginService.psnAccBocnetLogin(psnAccBocnetLoginParams);
                    }
                })
                .flatMap(new Func1<PsnAccBocnetLoginResult, Observable<PsnAccBocnetQryLoginInfoResult>>() {
                    @Override
                    public Observable<PsnAccBocnetQryLoginInfoResult> call(PsnAccBocnetLoginResult psnAccBocnetLoginResult) {
                        PsnAccBocnetQryLoginInfoParams mParams = new PsnAccBocnetQryLoginInfoParams();
                        return psnLoginService.psnAccBocnetQryLoginInfo(mParams);
                    }
                })
                //设定subscribeOn为io线程，observeOn为主线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<PsnAccBocnetQryLoginInfoResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        cardBalanceView.loginInforfail();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnAccBocnetQryLoginInfoResult psnAccBocnetQryLoginInfoResult) {
                        BeanConvertor.toBean(psnAccBocnetQryLoginInfoResult,mViewModel);
                        cardBalanceView.loginInforSuccess(mViewModel);
                    }
                });
    }

    /**
     * 查询卡类型
     * @param accountNum
     */
    @Override
    public void queryCardType(String accountNum) {

        PsnAccBocnetQryCardTypeParams mParams = new PsnAccBocnetQryCardTypeParams();
        mParams.setAccountNumber(accountNum);
        psnLoginService.psnAccBocnetQryCardType(mParams)
                .compose(mRxLifecycleManager.<PsnAccBocnetQryCardTypeResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnAccBocnetQryCardTypeResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnAccBocnetQryCardTypeResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnAccBocnetQryCardTypeResult psnAccBocnetQryCardTypeResult) {

                        cardBalanceView.cardTypeSuccess(psnAccBocnetQryCardTypeResult.getAccountType());
                    }
                });
    }

    /**
     * 获取信用卡信息
     */
    @Override
    public void queryCreditCardInfor(final String accountSeq) {
        PsnAccBocnetQryCrcdPointParams mParams = new PsnAccBocnetQryCrcdPointParams();
        mParams.setAccountSeq(accountSeq);
        psnLoginService.psnAccBocnetQryCrcdPoint(mParams)
                .compose(mRxLifecycleManager.<PsnAccBocnetQryCrcdPointResult>bindToLifecycle())
                .flatMap(new Func1<PsnAccBocnetQryCrcdPointResult, Observable<PsnAccBocnetQueryGeneralInfoResult>>() {
                    @Override
                    public Observable<PsnAccBocnetQueryGeneralInfoResult> call(PsnAccBocnetQryCrcdPointResult psnAccBocnetQryCrcdPointResult) {
                        creditCardDetailViewModel.setConsumptionPoint(psnAccBocnetQryCrcdPointResult.getConsumptionPoint());
                        PsnAccBocnetQueryGeneralInfoParams mParams = new PsnAccBocnetQueryGeneralInfoParams();
                        mParams.setAccountSeq(accountSeq);
                        return psnLoginService.psnAccBocnetQueryGeneralInfo(mParams);
                    }
                })
                .compose(SchedulersCompat.<PsnAccBocnetQueryGeneralInfoResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnAccBocnetQueryGeneralInfoResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnAccBocnetQueryGeneralInfoResult psnAccBocnetQueryGeneralInfoResult) {

                        BeanConvertor.toBean(psnAccBocnetQueryGeneralInfoResult,creditCardDetailViewModel);
                        cardBalanceView.creditCardInforSuccess(creditCardDetailViewModel);
                    }
                });
    }
    /**
     * 获取借记卡信息
     */
    @Override
    public void queryDebitCardInfor(String accountSeq) {
        PsnAccBocnetQryDebitDetailParams mParams = new PsnAccBocnetQryDebitDetailParams();
        mParams.setAccountSeq(accountSeq);
        psnLoginService.psnAccBocnetQryDebitDetail(mParams)
                .compose(mRxLifecycleManager.<PsnAccBocnetQryDebitDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnAccBocnetQryDebitDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnAccBocnetQryDebitDetailResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnAccBocnetQryDebitDetailResult psnAccBocnetQryDebitDetailResult) {
                        BeanConvertor.toBean(psnAccBocnetQryDebitDetailResult,debitCardDetailViewModel);
                        cardBalanceView.debitCardInforSuccess(debitCardDetailViewModel);
                    }
                });
    }


    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        // 防止外界已经销毁，而后台线程的任务还在执行
        mRxLifecycleManager.onDestroy();
    }
}
