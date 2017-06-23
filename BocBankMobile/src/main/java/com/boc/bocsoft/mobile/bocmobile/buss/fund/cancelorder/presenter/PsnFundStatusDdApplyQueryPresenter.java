package com.boc.bocsoft.mobile.bocmobile.buss.fund.cancelorder.presenter;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundStatusDdApplyQuery.PsnFundStatusDdApplyQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundStatusDdApplyQuery.PsnFundStatusDdApplyQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.cancelorder.model.PsnFundStatusDdApplyQueryModel;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by taoyongzhen on 2016/11/23.
 */

public class PsnFundStatusDdApplyQueryPresenter extends RxPresenter implements CancelOrderContract.PsnFundStatusDdApplyQueryPresenter{
    private FundService fundService;
    private GlobalService globalService;
    private String conversationID = null;

    private CancelOrderContract.PsnFundStatusDdApplyQueryView queryView;

    public PsnFundStatusDdApplyQueryPresenter(CancelOrderContract.PsnFundStatusDdApplyQueryView queryView) {
        this.queryView = queryView;
        fundService = new FundService();
        globalService = new GlobalService();
    }

    @Override
    public void psnFundStatusDdApplyQuery(PsnFundStatusDdApplyQueryModel model) {
        if (StringUtil.isNullOrEmpty(conversationID)){
            queryTransOntranWithoutConversationID(model);
        }else {
            queryStatusDdApplyWithConversationID(model);
        }

    }

    private void queryStatusDdApplyWithConversationID(PsnFundStatusDdApplyQueryModel model){
        final PsnFundStatusDdApplyQueryParams params = getParams(model);
        params.setConversationId(conversationID);
        params.set_refresh("false");
        fundService.psnFundStatusDdApplyQuery(params)
                .compose(SchedulersCompat.<PsnFundStatusDdApplyQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundStatusDdApplyQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        queryView.psnFundStatusDdApplyQueryFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundStatusDdApplyQueryResult psnFundStatusDdApplyQueryResult) {
                        PsnFundStatusDdApplyQueryModel model = getModel(psnFundStatusDdApplyQueryResult);
                        queryView.psnFundStatusDdApplyQuerySuccess(model);
                    }
                });

    }

    private void queryTransOntranWithoutConversationID(PsnFundStatusDdApplyQueryModel model){
        final PsnFundStatusDdApplyQueryParams params = getParams(model);
        globalService.psnCreatConversation(new PSNCreatConversationParams())
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnFundStatusDdApplyQueryResult>>(){
                    @Override
                    public Observable<PsnFundStatusDdApplyQueryResult> call(String s) {
                        conversationID = s;
                        params.setConversationId(conversationID);
                        return fundService.psnFundStatusDdApplyQuery(params);
                    }
                }).compose(SchedulersCompat.<PsnFundStatusDdApplyQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundStatusDdApplyQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
//                        queryView.psnFundStatusDdApplyQueryFail(biiResultErrorException);
                        queryView.psnFundStatusDdApplyQuerySuccess(getTest());
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundStatusDdApplyQueryResult psnFundStatusDdApplyQueryResult) {
                        PsnFundStatusDdApplyQueryModel model = getModel(psnFundStatusDdApplyQueryResult);
                        queryView.psnFundStatusDdApplyQuerySuccess(getTest());
                    }
                });

    }
    private PsnFundStatusDdApplyQueryParams getParams(PsnFundStatusDdApplyQueryModel model){
        //fundCode : dfadfa
        //* dtFlag : 0  由上一级传入
        PsnFundStatusDdApplyQueryParams params = new PsnFundStatusDdApplyQueryParams();
        params.set_refresh(model.getRefresh());
        params.setCurrentIndex(model.getCurrentIndex());
        params.setPageSize(model.getPageSize());
        params.setFundCode(model.getFundCode());
        params.setDtFlag(model.getDtFlag());
        return params;

    }

    private PsnFundStatusDdApplyQueryModel getModel(PsnFundStatusDdApplyQueryResult  psnFundStatusDdApplyQueryResult) {
        PsnFundStatusDdApplyQueryModel model = new PsnFundStatusDdApplyQueryModel();
        if (psnFundStatusDdApplyQueryResult == null) {
            model.setRecordNumber(0);
            return model;
        }
        model.setRecordNumber(psnFundStatusDdApplyQueryResult.getRecordNumber());
        if (psnFundStatusDdApplyQueryResult.getList() == null){
            return model;
        }
        List<PsnFundStatusDdApplyQueryModel.ResultListBean> listBeans = new ArrayList<>();
        for (PsnFundStatusDdApplyQueryResult.ListBean bean : psnFundStatusDdApplyQueryResult.getList()) {
            PsnFundStatusDdApplyQueryModel.ResultListBean modelBean = new PsnFundStatusDdApplyQueryModel.ResultListBean();
            modelBean.setFundName(bean.getFundName());
            modelBean.setRecordStatus(bean.getRecordStatus());
            modelBean.setApplyDate(bean.getApplyDate());
            modelBean.setApplyAmount(bean.getApplyAmount());
            modelBean.setTransType(bean.getTransType());
            modelBean.setCurrency(bean.getCurrency());
            modelBean.setFundCode(bean.getFundCode());
            listBeans.add(modelBean);
        }
        return model;
    }

    private PsnFundStatusDdApplyQueryModel getTest(){
        PsnFundStatusDdApplyQueryModel model = new PsnFundStatusDdApplyQueryModel();
        model.setRecordNumber(13);
        List<PsnFundStatusDdApplyQueryModel.ResultListBean> listBeans = new ArrayList<>();
        for (int i=0;i<5;i++){
            PsnFundStatusDdApplyQueryModel.ResultListBean modelBean = new PsnFundStatusDdApplyQueryModel.ResultListBean();
            modelBean.setFundName("化学");
            modelBean.setFundCode("1233");
            modelBean.setRecordStatus("2");
            modelBean.setApplyDate("2015/01/01");
            modelBean.setApplyAmount("2119");
            modelBean.setTransType("0");
            modelBean.setCurrency("002");
            listBeans.add(modelBean);
        }
        model.setResultList(listBeans);
        return model;
    }



}
