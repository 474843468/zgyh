package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.presenter;

import android.util.Log;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainTranSetConfirm.PsnCrcdAppertainTranSetConfirmParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainTranSetConfirm.PsnCrcdAppertainTranSetConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery.PsnCrcdCurrencyQueryParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery.PsnCrcdCurrencyQueryResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAppertainAndMess.PsnCrcdQueryAppertainAndMessParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAppertainAndMess.PsnCrcdQueryAppertainAndMessResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.service.CrcdService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.model.AttCardSetUpModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.model.AttCardTradeFlowModel;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.functions.Func1;

/**
 * Name: liukai
 * Time：2016/12/8 21:36.
 * Created by lk7066 on 2016/12/8.
 * It's used to
 */

public class AttCardTradeFlowPresenter implements AttCardTradeFlowContract.AttCardTradeFlowPresenter {

    private RxLifecycleManager mRxLifecycleManager;
    private int flag = 0;//单币种查询一次，标志位用来区分是单人民币，单外币，还是多币种人民币查询
    private int secondflag = 0;//仅仅用来给多币种外币查询需要第二次调用相同接口的标志位

    /**
     * 信用卡service
     */
    private CrcdService crcdService;

    private GlobalService globalService;

    public static String tradeFlowConversationId = "";

    private AttCardTradeFlowContract.AttCardTradeFlowView attCardTradeFlowView;



    public AttCardTradeFlowPresenter(AttCardTradeFlowContract.AttCardTradeFlowView view) {
        attCardTradeFlowView = view;
        attCardTradeFlowView.setPresenter(this);
        crcdService = new CrcdService();
        globalService = new GlobalService();
        mRxLifecycleManager= new RxLifecycleManager();
    }

    /**
     * 查询短信和流量
     * 参数：一个model，一个币种
     * */
    @Override
    public void queryAppertainAndMess(AttCardSetUpModel attCardSetUpModel, String currency, int mCurrencyStatus) {

        if(1 == mCurrencyStatus){
            //单人民币情况
            flag = 0;
        } else if(0 == mCurrencyStatus) {
            //单外币情况
            flag = 1;
        } else {
            //多币种人民币流量查询
            flag = 2;
        }

        PsnCrcdQueryAppertainAndMessParams params = new PsnCrcdQueryAppertainAndMessParams();
        params.setCardNo(attCardSetUpModel.getSubCrcdNo());
        params.setCurrency(currency);

        crcdService.psnCrcdQueryAppertainAndMess(params)
                .compose(mRxLifecycleManager.<PsnCrcdQueryAppertainAndMessResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdQueryAppertainAndMessResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdQueryAppertainAndMessResult>() {

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        Log.d("--liukai--对象查询", "失败");
                        attCardTradeFlowView.appertainAndMessFailed(biiResultErrorException, flag);
                    }

                    @Override
                    public void onCompleted() {
                        Log.d("--liukai--对象查询", "完成");
                    }

                    @Override
                    public void onNext(PsnCrcdQueryAppertainAndMessResult psnCrcdQueryAppertainAndMessResult) {
                        Log.d("--liukai--对象查询", "成功");
                        attCardTradeFlowView.appertainAndMessSuccess(psnCrcdQueryAppertainAndMessResult, flag);
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {

                    }

                });


    }

    /**
     * 查询短信和流量
     * 参数：一个model，一个币种
     * 多币种第二次请求接口
     * */
    @Override
    public void queryAppertainAndMessSecond(AttCardSetUpModel attCardSetUpModel, String currency, int mCurrencyStatus) {

        //多币种外币查询标志位
        secondflag = 3;

        PsnCrcdQueryAppertainAndMessParams params = new PsnCrcdQueryAppertainAndMessParams();
        params.setCardNo(attCardSetUpModel.getSubCrcdNo());
        params.setCurrency(currency);

        crcdService.psnCrcdQueryAppertainAndMess(params)
                .compose(mRxLifecycleManager.<PsnCrcdQueryAppertainAndMessResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdQueryAppertainAndMessResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdQueryAppertainAndMessResult>() {

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        Log.d("--liukai--对象查询", "失败");
                        attCardTradeFlowView.appertainAndMessSecondFailed(biiResultErrorException, secondflag);
                    }

                    @Override
                    public void onCompleted() {
                        Log.d("--liukai--对象查询", "完成");
                    }

                    @Override
                    public void onNext(PsnCrcdQueryAppertainAndMessResult psnCrcdQueryAppertainAndMessResult) {
                        Log.d("--liukai--对象查询", "成功");
                        attCardTradeFlowView.appertainAndMessSecondSuccess(psnCrcdQueryAppertainAndMessResult, secondflag);
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {

                    }

                });


    }

    /**
     * 获取安全因子
     * */
    @Override
    public void querySecurityFactor() {

        PsnGetSecurityFactorParams params = new PsnGetSecurityFactorParams();
        params.setConversationId(AttCardTradeDetailPresenter.attCardConversationId);
        params.setServiceId("PB057C2");

        globalService.psnGetSecurityFactor(params)
                .compose(mRxLifecycleManager.<PsnGetSecurityFactorResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnGetSecurityFactorResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnGetSecurityFactorResult>() {

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        Log.d("--liukai--获取安全因子", "失败 ");
                        attCardTradeFlowView.querySecurityFactorFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                        Log.d("--liukai--获取安全因子", "完成");
                    }

                    @Override
                    public void onNext(PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
                        Log.d("--liukai--获取安全因子", "成功");
                        SecurityFactorModel securityFactorModel = ModelUtil.generateSecurityFactorModel(psnGetSecurityFactorResult);
                        attCardTradeFlowView.querySecurityFactorSuccess(securityFactorModel);
                    }

                });

    }

    /**
     * 交易流量确认
     * */
    @Override
    public void setAppertainTranConfirm(AttCardTradeFlowModel model) {

        //BigDecimal money = new BigDecimal(model.getAmount());
        PsnCrcdAppertainTranSetConfirmParams params = new PsnCrcdAppertainTranSetConfirmParams();
        params.setAccountId(model.getAccountId());
        params.setCurrencyCode(model.getCurrency());
        params.setConversationId(AttCardTradeDetailPresenter.attCardConversationId);
        params.set_combinId(model.get_combinId());
        params.setAmount(model.getAmount());

        crcdService.psnCrcdAppertainTranSetConfirm(params)
                .compose(mRxLifecycleManager.<PsnCrcdAppertainTranSetConfirmResult>bindToLifecycle())
                .map(new Func1<PsnCrcdAppertainTranSetConfirmResult, VerifyBean>() {

                    @Override
                    public VerifyBean call(PsnCrcdAppertainTranSetConfirmResult psnCrcdAppertainTranSetConfirmResult) {
                        return BeanConvertor.toBean(psnCrcdAppertainTranSetConfirmResult, new VerifyBean());
                    }

                })
                .compose(SchedulersCompat.<VerifyBean>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<VerifyBean>() {

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        Log.d("--liukai--流量确认", "失败");
                        attCardTradeFlowView.setAppertainTranConfirmFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                        Log.d("--liukai--流量确认", "完成");
                    }

                    @Override
                    public void onNext(VerifyBean verifyBean) {
                        Log.d("--liukai--流量确认", "成功");
                        attCardTradeFlowView.setAppertainTranConfirmSuccess(verifyBean);
                    }

                });

    }

    /**
     * 信用卡币种查询
     */
    @Override
    public void queryCrcdCurrency(String s){

        PsnCrcdCurrencyQueryParams mCurrencyParams = new PsnCrcdCurrencyQueryParams();
        mCurrencyParams.setAccountNumber(s);//上送accountnumber

        crcdService.psnCrcdCurrencyQuery(mCurrencyParams)
                .compose(mRxLifecycleManager.<PsnCrcdCurrencyQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdCurrencyQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdCurrencyQueryResult>() {

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        Log.d("--liukai信用卡币种查询--", "异常情况");
                        attCardTradeFlowView.crcdCurrencyQueryFailed(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                        Log.d("--liukai信用卡币种查询--", "完成情况");
                    }

                    @Override
                    public void onNext(PsnCrcdCurrencyQueryResult psnCrcdCurrencyQueryResult) {
                        Log.d("--liukai信用卡币种查询--", "" + psnCrcdCurrencyQueryResult);
                        attCardTradeFlowView.crcdCurrencyQuerySuccess(psnCrcdCurrencyQueryResult);
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
