package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.presenter;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryFloatProfitAndLoss.PsnQueryFloatProfitAndLossParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryFloatProfitAndLoss.PsnQueryFloatProfitAndLossResult;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.model.FundFloatingProfileLossModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui.FundFloatingProfileLossConst;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taoyongzhen on 2016/11/30.
 */

public class FundFloatingProfileLossPresenter extends RxPresenter implements FundFloatingProfileLossConst.Prrsenter{
    FundService fundService;
    FundFloatingProfileLossConst.queryFloatingProfitLossView profitLossView;

    public FundFloatingProfileLossPresenter(FundFloatingProfileLossConst.queryFloatingProfitLossView profitLossView) {
        this.profitLossView = profitLossView;
        profitLossView.setPresenter(this);
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
                        profitLossView.queryFloatingProfitLossFail(biiResultErrorException);
//                        FundFloatingProfileLossModel model = getModel(getTest());
//                        profitLossView.queryFloatingProfitLossSuccess(model);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(List<PsnQueryFloatProfitAndLossResult> psnQueryFloatProfitAndLossResults) {

                        FundFloatingProfileLossModel model = getModel(psnQueryFloatProfitAndLossResults);
                        profitLossView.queryFloatingProfitLossSuccess(model);
                    }
                });


    }

    private FundFloatingProfileLossModel getModel(List<PsnQueryFloatProfitAndLossResult> psnQueryFloatProfitAndLossResults){
        FundFloatingProfileLossModel model = new FundFloatingProfileLossModel();
        if (psnQueryFloatProfitAndLossResults == null){
            return model;
        }
        if (model.getResultList() == null){
            List<FundFloatingProfileLossModel.ResultListBean> beans = new ArrayList<>();
            model.setResultList(beans);
        }
        for (PsnQueryFloatProfitAndLossResult bean : psnQueryFloatProfitAndLossResults){
            FundFloatingProfileLossModel.ResultListBean modelBean = BeanConvertor.toBean(bean,new FundFloatingProfileLossModel.ResultListBean());
            model.getResultList().add(modelBean);
        }
        return model;
    }
    /**
     * 数据展示规则：
     * 1. 总盈亏：已实现盈亏(resultFloat)+持仓盈亏(endFloat)
     * 2. 持仓盈亏:endFloat
     * 3. 已实现盈亏:resultFloat
     * 4. 期末市值:endCost
     * 5. 期初市值:startCost
     * 6. 卖出净金额:卖出金额(hsAmount)-手续费用(jyAmount)
     * 7. 买入总金额:买入金额（trAmount）
     */
    /*
    PsnQueryFloatProfitAndLossResult getTest(){
        PsnQueryFloatProfitAndLossResult result = new PsnQueryFloatProfitAndLossResult();
        result.setRecordNumber(6);
        List<PsnQueryFloatProfitAndLossResult.ResultListBean> lists = new ArrayList<>();
        result.setResultList(lists);
        for (int i =0;i<6;i++){
            PsnQueryFloatProfitAndLossResult.ResultListBean  bean = new PsnQueryFloatProfitAndLossResult.ResultListBean();
            bean.setFundCode("1222"+i);
            bean.setFundName("中国"+i);
            bean.setTrAmount("2334"+i);
            bean.setCurceny("001");
            bean.setCashFlag("CAS");
            bean.setEndCost("1223");
            bean.setEndFloat("1234");
            bean.setJyAmount("2");
            bean.setResultFloat("12233");
            bean.setStartCost("122");
            bean.setHsAmount("222");
            lists.add(bean);
        }

        PsnQueryFloatProfitAndLossResult.ResultListBean  bean = new PsnQueryFloatProfitAndLossResult.ResultListBean();
        bean.setFundCode("合计数");
        bean.setFundName("中国");
        bean.setTrAmount("2334");
        bean.setCurceny("001");
        bean.setCashFlag("CAS");
        bean.setCashFlag("CAS");
        bean.setEndCost("1223");
        bean.setEndFloat("1234");
        bean.setJyAmount("2");
        bean.setResultFloat("12233");
        bean.setStartCost("122");
        bean.setHsAmount("222");
        lists.add(bean);

        PsnQueryFloatProfitAndLossResult.ResultListBean  bean1 = new PsnQueryFloatProfitAndLossResult.ResultListBean();
        bean1.setFundCode("合计数");
        bean1.setFundName("中国s");
        bean1.setTrAmount("233667");
        bean1.setCurceny("014");
        bean1.setCashFlag("CAS");
        bean1.setEndCost("1223");
        bean1.setEndFloat("1234");
        bean1.setJyAmount("2");
        bean1.setResultFloat("12233");
        bean1.setStartCost("122");
        bean1.setHsAmount("222");
        lists.add(bean1);
        PsnQueryFloatProfitAndLossResult.ResultListBean  bean2= new PsnQueryFloatProfitAndLossResult.ResultListBean();
        bean2.setFundCode("合计数");
        bean2.setFundName("中国s");
        bean2.setTrAmount("233667");
        bean2.setCurceny("015");
        bean2.setCashFlag("CAS");
        bean2.setEndCost("1223");
        bean2.setEndFloat("1234");
        bean2.setJyAmount("2");
        bean2.setResultFloat("12233");
        bean2.setStartCost("122");
        bean2.setHsAmount("222");
        lists.add(bean2);
        PsnQueryFloatProfitAndLossResult.ResultListBean  bean3 = new PsnQueryFloatProfitAndLossResult.ResultListBean();
        bean3.setFundCode("合计数");
        bean3.setFundName("中国s");
        bean3.setTrAmount("233667");
        bean3.setCurceny("016");
        bean3.setCashFlag("CAS");
        bean3.setEndCost("1223");
        bean3.setEndFloat("1234");
        bean3.setJyAmount("2");
        bean3.setResultFloat("12233");
        bean3.setStartCost("122");
        bean3.setHsAmount("222");
        lists.add(bean3);
        PsnQueryFloatProfitAndLossResult.ResultListBean  bean4 = new PsnQueryFloatProfitAndLossResult.ResultListBean();
        bean4.setFundCode("合计数");
        bean4.setFundName("中国s");
        bean4.setTrAmount("233667");
        bean4.setCurceny("017");
        bean4.setCashFlag("CAS");
        bean4.setEndCost("1223");
        bean4.setEndFloat("1234");
        bean4.setJyAmount("2");
        bean4.setResultFloat("12233");
        bean4.setStartCost("122");
        bean4.setHsAmount("222");
        lists.add(bean4);
        PsnQueryFloatProfitAndLossResult.ResultListBean  bean5 = new PsnQueryFloatProfitAndLossResult.ResultListBean();
        bean5.setFundCode("合计数");
        bean5.setFundName("中国s");
        bean5.setTrAmount("233667");
        bean5.setCurceny("018");
        bean5.setCashFlag("CAS");
        bean5.setEndCost("1223");
        bean5.setEndFloat("1234");
        bean5.setJyAmount("2");
        bean5.setResultFloat("12233");
        bean5.setStartCost("122");
        bean5.setHsAmount("222");
        lists.add(bean5);
        PsnQueryFloatProfitAndLossResult.ResultListBean  bean6 = new PsnQueryFloatProfitAndLossResult.ResultListBean();
        bean6.setFundCode("合计数");
        bean6.setFundName("中国s");
        bean6.setTrAmount("233667");
        bean6.setCurceny("024");
        bean6.setCashFlag("CAS");
        bean6.setEndCost("1223");
        bean6.setEndFloat("1234");
        bean6.setJyAmount("2");
        bean6.setResultFloat("12233");
        bean6.setStartCost("122");
        bean6.setHsAmount("222");
        lists.add(bean6);

        return result;
    }*/


}
