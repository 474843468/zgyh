package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.presenter;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayConsumeQry.PsnCrcdDividedPayConsumeQryParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayConsumeQry.PsnCrcdDividedPayConsumeQryResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.service.CrcdService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.model.ConsumeTransModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.model.CrcdConsumeQueryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.ui.ConsumeInstallmentMainContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Name: liukai
 * Time：2017/1/3 15:02.
 * Created by lk7066 on 2017/1/3.
 * It's used to
 */

public class ConsumeInstallmentMainPresenter implements ConsumeInstallmentMainContract.ConsumeInstallmentMainPresenter {

    private RxLifecycleManager rxLifecycleManager;
    /**
     * 公用service
     */
    private GlobalService globalService;

    /**
     * 信用卡service
     */
    private CrcdService crcdService;
    private ConsumeInstallmentMainContract.ConsumeInstallmentMainView mainView;

    public ConsumeInstallmentMainPresenter(ConsumeInstallmentMainContract.ConsumeInstallmentMainView view){
        mainView = view;
        crcdService = new CrcdService();
        globalService = new GlobalService();
        rxLifecycleManager = new RxLifecycleManager();
    }


    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void crcdDividedPayConsumeQry(final CrcdConsumeQueryModel queryModel) {
        final PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(rxLifecycleManager.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnCrcdDividedPayConsumeQryResult>>() {
                    @Override
                    public Observable<PsnCrcdDividedPayConsumeQryResult> call(String conversationId) {

                        PsnCrcdDividedPayConsumeQryParams params=new PsnCrcdDividedPayConsumeQryParams();
                        params.setAccountId(queryModel.getAccountId());
                        params.setConversationId(conversationId);
                        params.setCurrencyCode(queryModel.getCurrencyCode());
                        params.setCurrentIndex(queryModel.getCurrentIndex());
                        params.setPageSize(queryModel.getPageSize());
                        return crcdService.psnCrcdDividedPayConsumeQry(params);
                    }
                })
                .compose(SchedulersCompat.<PsnCrcdDividedPayConsumeQryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdDividedPayConsumeQryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mainView.crcdDividedPayConsumeQryFailed(biiResultErrorException);
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnCrcdDividedPayConsumeQryResult result) {
                        ConsumeTransModel transModel=new ConsumeTransModel();
                        transModel.setRecordNumber(result.getRecordNumber());
                        List<ConsumeTransModel.ConsumeBean> consumeBeanList=new ArrayList<ConsumeTransModel.ConsumeBean>();
                        for (PsnCrcdDividedPayConsumeQryResult.ConsumeBean bean:result.getList()){
                            ConsumeTransModel.ConsumeBean consumeBean=new ConsumeTransModel.ConsumeBean();
                            consumeBeanList.add(BeanConvertor.toBean(bean,consumeBean));
                        }
                        transModel.setList(consumeBeanList);
                        mainView.crcdDividedPayConsumeQrySuccess(transModel);
                    }
                });

    }



}
