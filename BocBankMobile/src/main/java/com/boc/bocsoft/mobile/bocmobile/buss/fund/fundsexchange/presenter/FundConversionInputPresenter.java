package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundsexchange.presenter;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundConversionInput.PsnFundConversionInputParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundConversionInput.PsnFundConversionInputResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnGetFundDetail.PsnGetFundDetailParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnGetFundDetail.PsnGetFundDetailResult;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundsexchange.model.FundConversionInputModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundsexchange.ui.FundConversionContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taoyongzhen on 2016/12/13.
 */

public class FundConversionInputPresenter extends RxPresenter implements FundConversionContract.Presenter{

    private FundConversionContract.FundConversionInputQueryView queryView;
    private FundService fundService;

    public FundConversionInputPresenter(FundConversionContract.FundConversionInputQueryView queryView) {
        this.queryView = queryView;
        queryView.setPresenter(this);
        fundService = new FundService();
    }

    @Override
    public void fundConversionInputQuery(FundConversionInputModel parmas) {
        PsnFundConversionInputParams psnFundConversionInputParams = new PsnFundConversionInputParams();
        psnFundConversionInputParams.setFromFundCode(parmas.getFromFundCode());
        fundService.psnFundcoversionInput(psnFundConversionInputParams)
                .compose(this.<List<PsnFundConversionInputResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnFundConversionInputResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnFundConversionInputResult>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        queryView.fundConversionInputQueryFail(biiResultErrorException);
//                        queryView.fundConversionInputQuerySuccess(test());
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(List<PsnFundConversionInputResult> psnFundConversionInputResults) {
                        FundConversionInputModel model = buildModelByResult(psnFundConversionInputResults);
                        queryView.fundConversionInputQuerySuccess(model);
                    }
                });

    }

    @Override
    public void queryFundInfo(PsnGetFundDetailParams params) {
        fundService.psnGetFundDetail(params)
                .compose(this.<PsnGetFundDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnGetFundDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnGetFundDetailResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnGetFundDetailResult result) {
                        queryView.queryFundInfoSuccess(result);

                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        queryView.queryFundInfoFail(biiResultErrorException);
//                        queryView.queryFundInfoSuccess(test2());
                    }
                });
    }

    private FundConversionInputModel buildModelByResult(List<PsnFundConversionInputResult> psnFundConversionInputResults) {
        FundConversionInputModel model = new FundConversionInputModel();
        if (psnFundConversionInputResults == null || psnFundConversionInputResults.size() <= 0){
            return model;
        }
        ArrayList<FundConversionInputModel.InputBean> lists = new ArrayList<>();
        for (PsnFundConversionInputResult result:psnFundConversionInputResults){
            FundConversionInputModel.InputBean bean = new FundConversionInputModel.InputBean();
            bean.setCanBuy(result.getCanBuy());
            bean.setFundCode(result.getFundCode());
            bean.setFundName(result.getFundName());
            bean.setTranState(result.getTranState());
            lists.add(bean);
        }
        model.setListBeans(lists);
        return model;

    }
    private FundConversionInputModel test(){
        FundConversionInputModel model = new FundConversionInputModel();
        ArrayList<FundConversionInputModel.InputBean> lists = new ArrayList<>();
        for (int i=0;i<5;i++){
            FundConversionInputModel.InputBean bean = new FundConversionInputModel.InputBean();
            bean.setCanBuy("y");
            bean.setFundCode("1233"+i);
            bean.setFundName("myte"+i);
            bean.setTranState("1");
            lists.add(bean);
        }
        model.setListBeans(lists);
        return model;
    }

    private PsnGetFundDetailResult test2(){
        PsnGetFundDetailResult result = new PsnGetFundDetailResult();
//        result.setDayIncomeRatio("0.05");
        result.setEndDate("2016/06/12");
        result.setFundIncomeRatio("0.6");
        result.setNetPrice("123");
        return result;
    }



}
