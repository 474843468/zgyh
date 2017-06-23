package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundposition.presenter;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryFundBalance.PsnFundQueryFundBalanceParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryFundBalance.PsnFundQueryFundBalanceResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryFloatProfitAndLoss.PsnQueryFloatProfitAndLossParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryFloatProfitAndLoss.PsnQueryFloatProfitAndLossResult;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundposition.model.FundFloatingProfileLossModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundposition.model.FundPositionModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundposition.ui.FundPositionContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taoyongzhen on 2016/11/29.
 */

public class FundPositionPresenter extends RxPresenter implements FundPositionContract.Presenter {

    private FundService fundService;

    private FundPositionContract.FundPositionView fundPositionView;

    public FundPositionPresenter(FundPositionContract.FundPositionView fundPositionView) {
        this.fundPositionView = fundPositionView;
        fundPositionView.setPresenter(this);
        fundService = new FundService();

    }

    @Override
    public void queryFloatingProfitLoss(FundFloatingProfileLossModel model) {
        PsnQueryFloatProfitAndLossParams params = new PsnQueryFloatProfitAndLossParams();
        params.setFundCode(model.getFundCode());
        params.setStartDate(model.getStartDate());
        params.setEndDate(model.getEndDate());
        fundService.psnQueryFloatProfitAndLoss(params)
                .compose(this.<List<PsnQueryFloatProfitAndLossResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnQueryFloatProfitAndLossResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnQueryFloatProfitAndLossResult>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        fundPositionView.queryFloatingProfitLossFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(List<PsnQueryFloatProfitAndLossResult> psnQueryFloatProfitAndLossResults) {
                        FundFloatingProfileLossModel model = getProfileLossModel(psnQueryFloatProfitAndLossResults);
                        fundPositionView.queryFloatingProfitLossSuccess(model);
                    }
                });
    }

    /**
     * 查询持仓信息
     *
     * @param model
     */
    @Override
    public void QueryFundBalance(FundPositionModel model) {
        PsnFundQueryFundBalanceParams params = new PsnFundQueryFundBalanceParams();
        fundService.psnFundQueryFundBalance(params)
                .compose(this.<PsnFundQueryFundBalanceResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFundQueryFundBalanceResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundQueryFundBalanceResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        fundPositionView.queryFundBalanceFail(biiResultErrorException);
//                        fundPositionView.queryFundBalanceSuccess(getTest());
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundQueryFundBalanceResult result) {
                        fundPositionView.queryFundBalanceSuccess(getModel(result));
                    }
                });
//        fundPositionView.queryFundBalanceSuccess(getTest());
    }

    //test
    FundPositionModel getTest(){
        FundPositionModel model = new FundPositionModel();
        List<FundPositionModel.FundBalanceBean> beanList = new ArrayList<>();
        for (int i=0;i<6;i++){
            FundPositionModel.FundBalanceBean modelBean = new FundPositionModel.FundBalanceBean();
            modelBean.setFundCode("00"+i);
            modelBean.setCurrentCapitalisation("2345"+i);
            modelBean.setBonusType("1");
            modelBean.setTotalBalance("123");
            modelBean.setTotalAvailableBalance("34");
            modelBean.setTotalFrozenBalance("334");
            FundPositionModel.FundBalanceBean.FundInfoBean fundInfoBean = new FundPositionModel.FundBalanceBean.FundInfoBean();
            fundInfoBean.setAccountType("122");
            fundInfoBean.setCashFlag("RNT");
            fundInfoBean.setIsBonusMod("Y");
            fundInfoBean.setCurrency("00"+(i+1));
            fundInfoBean.setCanModBonus(true);
            fundInfoBean.setFundCode("12"+i);
            fundInfoBean.setFundName("wode"+i);
            modelBean.setFundInfo(fundInfoBean);
            beanList.add(modelBean);
        }
        model.setFundBalance(beanList);
        return model;
    }

    private FundPositionModel getModel(PsnFundQueryFundBalanceResult result) {
        FundPositionModel model = new FundPositionModel();
        if (result == null || result.getFundBalance() == null || result.getFundBalance().size() <=0){
            return model;
        }

        List<FundPositionModel.FundBalanceBean> beanList = new ArrayList<>();
        for (PsnFundQueryFundBalanceResult.FundBalanceBean bean : result.getFundBalance()){
            FundPositionModel.FundBalanceBean modelBean = new FundPositionModel.FundBalanceBean();
            modelBean.setFundCode(bean.getFundCode());
            modelBean.setCurrentCapitalisation(bean.getCurrentCapitalisation());
            modelBean.setBonusType(bean.getBonusType());
            modelBean.setTotalBalance(bean.getTotalBalance());
            modelBean.setTotalAvailableBalance(bean.getTotalAvailableBalance());
            modelBean.setTotalFrozenBalance(bean.getTotalAvailableBalance());
            FundPositionModel.FundBalanceBean.FundInfoBean fundInfoBean = BeanConvertor.toBean(bean.getFundInfo(),new FundPositionModel.FundBalanceBean.FundInfoBean());
            modelBean.setFundInfo(fundInfoBean);
            beanList.add(modelBean);
        }
        model.setFundBalance(beanList);
        return model;
    }

    private FundFloatingProfileLossModel getProfileLossModel(List<PsnQueryFloatProfitAndLossResult> psnQueryFloatProfitAndLossResults) {
        FundFloatingProfileLossModel model = new FundFloatingProfileLossModel();
        if (psnQueryFloatProfitAndLossResults == null ) {
            return model;
        }
        if (model.getResultList() == null) {
            List<FundFloatingProfileLossModel.ResultListBean> beans = new ArrayList<>();
            model.setResultList(beans);
        }
        for (PsnQueryFloatProfitAndLossResult bean : psnQueryFloatProfitAndLossResults) {
            FundFloatingProfileLossModel.ResultListBean modelBean = BeanConvertor.toBean(bean,new FundFloatingProfileLossModel.ResultListBean());
            model.getResultList().add(modelBean);
        }
        return model;
    }


}
