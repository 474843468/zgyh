package com.boc.bocsoft.mobile.bocmobile.buss.fund.cancelorder.presenter;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryTransOntran.PsnFundQueryTransOntranParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryTransOntran.PsnFundQueryTransOntranResult;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.cancelorder.model.PsnFundQueryTransOntranModel;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by taoyongzhen on 2016/11/21.
 */

public class PsnFundQueryTransOntranPresenter extends RxPresenter implements CancelOrderContract.PsnFundQueryTransOntranPresenter {

    private FundService fundService;
    private GlobalService globalService;
    private String conversationID = null;


    private CancelOrderContract.PsnFundQueryTransOntranView queryTransOntranView;

    public PsnFundQueryTransOntranPresenter(CancelOrderContract.PsnFundQueryTransOntranView queryTransOntranView) {
        this.queryTransOntranView = queryTransOntranView;
        fundService = new FundService();
        globalService = new GlobalService();
    }

    @Override
    public void psnFundQueryTransOntran(PsnFundQueryTransOntranModel model) {
        if (StringUtil.isNullOrEmpty(conversationID)) {
            queryTransOntranWithoutConversationID(model);
        }else {
            queryTransOntranWithConversationID(model);
        }

    }

    private void queryTransOntranWithConversationID(PsnFundQueryTransOntranModel model){
        final PsnFundQueryTransOntranParams params = getParams(model);
        params.setConversationId(conversationID);
        params.setRefresh("false");
        fundService.psnFundQueryTransOntran(params)
                .compose(SchedulersCompat.<PsnFundQueryTransOntranResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundQueryTransOntranResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        queryTransOntranView.psnFundQueryTransOntranFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundQueryTransOntranResult psnFundQueryTransOntranResult) {
                        PsnFundQueryTransOntranModel model = getModel(psnFundQueryTransOntranResult);
                        queryTransOntranView.psnFundQueryTransOntranSuccess(model);
                    }
                });

    }

    private void queryTransOntranWithoutConversationID(PsnFundQueryTransOntranModel model){
        final PsnFundQueryTransOntranParams params = getParams(model);
        globalService.psnCreatConversation(new PSNCreatConversationParams())
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnFundQueryTransOntranResult>>(){
                    @Override
                    public Observable<PsnFundQueryTransOntranResult> call(String s) {
                        conversationID = s;
                        params.setConversationId(conversationID);
                        return fundService.psnFundQueryTransOntran(params);
                    }
                }).compose(SchedulersCompat.<PsnFundQueryTransOntranResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundQueryTransOntranResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
//                        queryTransOntranView.psnFundQueryTransOntranFail(biiResultErrorException);
                        queryTransOntranView.psnFundQueryTransOntranSuccess(getTest());

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundQueryTransOntranResult psnFundQueryTransOntranResult) {
                        PsnFundQueryTransOntranModel model = getModel(psnFundQueryTransOntranResult);
                        queryTransOntranView.psnFundQueryTransOntranSuccess(getTest());
                    }
                });

    }
    private PsnFundQueryTransOntranParams getParams(PsnFundQueryTransOntranModel model){
        PsnFundQueryTransOntranParams params = new PsnFundQueryTransOntranParams();
        params.setRefresh(model.getRefresh());
        params.setCurrentIndex(model.getCurrentIndex());
        params.setPageSize(model.getPageSize());
        return params;

    }

    private PsnFundQueryTransOntranModel getModel(PsnFundQueryTransOntranResult psnFundQueryTransOntranResult) {
        PsnFundQueryTransOntranModel model = new PsnFundQueryTransOntranModel();
        if (psnFundQueryTransOntranResult == null) {
            model.setRecordNumber(0);
            return model;
        }
        model.setRecordNumber(psnFundQueryTransOntranResult.getRecordNumber());
        if (psnFundQueryTransOntranResult.getList() == null){
            return model;
        }
        List<PsnFundQueryTransOntranModel.ListBean> listBeans = new ArrayList<>();
        for (PsnFundQueryTransOntranResult.ListBean bean : psnFundQueryTransOntranResult.getList()) {
            PsnFundQueryTransOntranModel.ListBean modelBean = new PsnFundQueryTransOntranModel.ListBean();
            modelBean.setFundName(bean.getFundName());
            modelBean.setTransStatus(bean.getTransStatus());
            modelBean.setCurrencyCode(bean.getCurrencyCode());
            modelBean.setPaymentDate(bean.getPaymentDate());
            modelBean.setFundTranType(bean.getFundTranType());
            modelBean.setTransAmount(bean.getTransAmount());
            modelBean.setTransCount(bean.getTransCount());
            listBeans.add(modelBean);
        }
        return model;
    }

    private PsnFundQueryTransOntranModel getTest(){
        PsnFundQueryTransOntranModel model = new PsnFundQueryTransOntranModel();
        model.setRecordNumber(12);
        List<PsnFundQueryTransOntranModel.ListBean> listBeans = new ArrayList<>();
        model.setList(listBeans);
        for (int i=0;i<5;i++){
            PsnFundQueryTransOntranModel.ListBean modelBean = new PsnFundQueryTransOntranModel.ListBean();
            modelBean.setFundName("黄西键");
            modelBean.setTransStatus("2");
            modelBean.setCurrencyCode("002");
            modelBean.setPaymentDate("2016/02/08");
            modelBean.setFundTranType("0");
            modelBean.setTransAmount("1234");
            modelBean.setTransCount("34");
            listBeans.add(modelBean);
        }

        return model;
    }


}
