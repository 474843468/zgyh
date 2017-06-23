package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainTranSetConfirm.PsnCrcdAppertainTranSetConfirmParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainTranSetConfirm.PsnCrcdAppertainTranSetConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainTranSetResult.PsnCrcdAppertainTranSetResultParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainTranSetResult.PsnCrcdAppertainTranSetResultResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.service.CrcdService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.BaseSubmitBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmContract;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.model.AttCardTradeFlowModel;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import rx.Observable;
import rx.functions.Func1;

/**
 * Name: liukai
 * Time：2016/12/20 9:45.
 * Created by lk7066 on 2016/12/20.
 * It's used to
 */

public class AttCardTradeFlowSecurityPresenter extends BaseConfirmPresenter<AttCardTradeFlowModel, PsnCrcdAppertainTranSetResultResult> {

    private CrcdService crcdService;
    private RxLifecycleManager mRxLifecycleManager;

    public AttCardTradeFlowSecurityPresenter(BaseConfirmContract.View<PsnCrcdAppertainTranSetResultResult> view) {
        super(view);
        crcdService = new CrcdService();
        mRxLifecycleManager = new RxLifecycleManager();
    }

    /**
     * 预交易
     * */
    @NonNull
    @Override
    protected Observable<VerifyBean> getVerifyBean(AttCardTradeFlowModel fillInfo) {

        PsnCrcdAppertainTranSetConfirmParams confirmParams = BeanConvertor.toBean(fillInfo, new PsnCrcdAppertainTranSetConfirmParams());

        //BigDecimal money = new BigDecimal(fillInfo.getAmount());
        confirmParams.setAmount(fillInfo.getAmount());
        confirmParams.setCurrencyCode(fillInfo.getCurrency());

        return crcdService.psnCrcdAppertainTranSetConfirm(confirmParams)
                .map(new Func1<PsnCrcdAppertainTranSetConfirmResult, VerifyBean>() {

                    @Override
                    public VerifyBean call(PsnCrcdAppertainTranSetConfirmResult result) {
                        return BeanConvertor.toBean(result, new VerifyBean());
                    }

                });

    }

    /**
     * 提交交易
     * */
    @Override
    public void submit(AttCardTradeFlowModel fillInfo, BaseSubmitBean submitBean) {

        PsnCrcdAppertainTranSetResultParams resultParams = new PsnCrcdAppertainTranSetResultParams();
        resultParams = BeanConvertor.toBean(fillInfo, resultParams);
        resultParams = BeanConvertor.toBean(submitBean, resultParams);
        resultParams.setAccountId(fillInfo.getAccountId());
        resultParams.setAmount(fillInfo.getAmount());
        resultParams.setCardNo(fillInfo.getSubCrcdNo());
        resultParams.setCurrencyCode(fillInfo.getCurrency());
        resultParams.setMasterCrcdNum(fillInfo.getAccountNo());
        resultParams.setMasterCrcdType(fillInfo.getMasterCrcdType());
        resultParams.setSubCrcdNum(fillInfo.getSubCrcdNo());
        resultParams.setConversationId(AttCardTradeDetailPresenter.attCardConversationId);

        crcdService.psnCrcdAppertainTranSetResult(resultParams)
                .compose(mRxLifecycleManager.<PsnCrcdAppertainTranSetResultResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdAppertainTranSetResultResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdAppertainTranSetResultResult>() {

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        Log.d("--liukai--流量设置", "失败");
                    }

                    @Override
                    public void onCompleted() {
                        Log.d("--liukai--流量设置", "完成");
                    }

                    @Override
                    public void onNext(PsnCrcdAppertainTranSetResultResult psnCrcdAppertainTranSetResultResult) {
                        Log.d("--liukai--流量设置", "成功");
                        mView.onSubmitSuccess(psnCrcdAppertainTranSetResultResult);
                    }

                });

    }

}
