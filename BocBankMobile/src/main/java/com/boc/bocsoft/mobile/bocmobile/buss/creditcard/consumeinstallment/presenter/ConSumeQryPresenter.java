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
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.ui.ConsumeQryContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * 作者：lq7090
 * 创建时间：2016/12/27.
 * 用途：可分期消费查询
 */
public class ConSumeQryPresenter extends RxPresenter implements ConsumeQryContract.Presenter {

    private ConsumeQryContract.BaseView mConsumeQryView;
    /**
     * 会话
     */
    private String conversationId;

    private final String serviceId = "PB057C1";//信用卡消费分期，账单分期服务码

    /**
     * 公用service
     */
    private GlobalService globalService;
    /**
     * 信用卡service
     */
    private CrcdService crcdService;

    private RxLifecycleManager rxLifecycleManager;

    public ConSumeQryPresenter(ConsumeQryContract.BaseView view) {
        mConsumeQryView = view;
        globalService=new GlobalService();
        rxLifecycleManager = new RxLifecycleManager();
        crcdService = new CrcdService();
    }

    /**
     *
     * 消费账单查询
     */
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
                        mConsumeQryView.crcdDividedPayConsumeQryFailed(biiResultErrorException);
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
                        mConsumeQryView.crcdDividedPayConsumeQrySuccess(transModel);
                    }
                });

    }
}
