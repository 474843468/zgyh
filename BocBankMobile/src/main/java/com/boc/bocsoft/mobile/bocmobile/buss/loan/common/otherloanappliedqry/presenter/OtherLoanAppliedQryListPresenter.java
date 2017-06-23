package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanappliedqry.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanAppliedQry.PsnOnLineLoanAppliedQryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanAppliedQry.PsnOnLineLoanAppliedQryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanappliedqry.OtherLoanAppliedQryConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanappliedqry.model.OtherLoanAppliedQryListViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanappliedqry.ui.OtherLoanAppliedQryListContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * 其他类型贷款-进度查询 通信逻辑处理
 * Created by liuzc on 2016/8/16.
 */
public class OtherLoanAppliedQryListPresenter extends RxPresenter implements OtherLoanAppliedQryListContract.Presenter {

    private OtherLoanAppliedQryListContract.View mLoanOnlineView;
    private PsnLoanService mLoanService;
    private String conversationID = null; //会话ID

    private GlobalService globalService;
    /**
     * 查询转账记录service
     */

    public OtherLoanAppliedQryListPresenter(OtherLoanAppliedQryListContract.View loanOnlineQryView){
        mLoanOnlineView = loanOnlineQryView;
        mLoanOnlineView.setPresenter(this);
        globalService = new GlobalService();
        mLoanService = new PsnLoanService();
    }

    public String getConversationID(){
        return conversationID;
    }

    @Override
    public void queryOtherLoanOnlineList(final OtherLoanAppliedQryListViewModel viewModel) {
//        //测试代码
//        OtherLoanAppliedQryListViewModel resViewModel = new OtherLoanAppliedQryListViewModel();
//        resViewModel.setRecordNumber(20);
//        List<OtherLoanAppliedQryListViewModel.ListBean> list = new LinkedList<>();
//        for(int i = 0; i < 6; i ++){
//            OtherLoanAppliedQryListViewModel.ListBean viewBean = new OtherLoanAppliedQryListViewModel.ListBean();
//
//            viewBean.setLoanNumber("124444");
//            viewBean.setProductCode("0001");
//            viewBean.setProductName("微型企业贷款");
//            viewBean.setName("temp");
//            viewBean.setLoanAmount(new BigDecimal(2000));
//            viewBean.setLoanTerm(12);
//            viewBean.setApplyDate("2016/01/01");
//            viewBean.setCurrency(i + "");
//            viewBean.setLoanStatus(i + "");
//            viewBean.setRefuseReason(i + "");
//            list.add(viewBean);
//        }
//        resViewModel.setList(list);
//        mLoanOnlineView.queryOtherLoanOnlineListSuccess(resViewModel);


        if(conversationID == null){
            requestWithoutConversation(viewModel);
        }
        else{
            requestWithConversation(viewModel);
        }
    }

    /**
     * 用已有的会话ID去请求数据
     * @param viewModel
     */
    private void requestWithConversation(final OtherLoanAppliedQryListViewModel viewModel){
        PsnOnLineLoanAppliedQryParams params =
                buildParamsFromView(viewModel);
        params.setConversationId(conversationID);
        params.set_refresh("false");
        mLoanService.psnOnLineLoanAppliedQry(params)
                .compose(this.<PsnOnLineLoanAppliedQryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnOnLineLoanAppliedQryResult>applyIoSchedulers())
                .subscribe(genSubscriber());
    }

    /**
     * 先请求会话ID，再请求数据
     * @param viewModel
     */
    private void requestWithoutConversation(final OtherLoanAppliedQryListViewModel viewModel){
        globalService.psnCreatConversation(new PSNCreatConversationParams())
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnOnLineLoanAppliedQryResult>>() {
                    @Override
                    public Observable<PsnOnLineLoanAppliedQryResult> call(
                            String conversationId) {
                        conversationID = conversationId;
                        PsnOnLineLoanAppliedQryParams params =
                                buildParamsFromView(viewModel);
                        params.setConversationId(conversationId);
                        return mLoanService.psnOnLineLoanAppliedQry(params);
                    }
                })
                .compose(SchedulersCompat.<PsnOnLineLoanAppliedQryResult>applyIoSchedulers())
                .subscribe(genSubscriber());
    }

    private BIIBaseSubscriber genSubscriber(){
        return new BIIBaseSubscriber<PsnOnLineLoanAppliedQryResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(PsnOnLineLoanAppliedQryResult psnLOANOverdueResult) {
                OtherLoanAppliedQryListViewModel viewModel = buildViewModelFromResult(psnLOANOverdueResult);
                mLoanOnlineView.queryOtherLoanOnlineListSuccess(viewModel);
            }

            @Override
            public void handleException(BiiResultErrorException biiResultErrorException) {
                mLoanOnlineView.queryOtherLoanOnlineListFail(biiResultErrorException);
            }
        };
    }

    //根据屏幕数据构造请求参数
    private PsnOnLineLoanAppliedQryParams buildParamsFromView(OtherLoanAppliedQryListViewModel viewModel){
        PsnOnLineLoanAppliedQryParams result = new PsnOnLineLoanAppliedQryParams();

        result.setName(viewModel.getName());
        result.setAppPhone(viewModel.getAppPhone());
        result.setAppEmail(viewModel.getAppEmail());
        result.setPageSize(String.valueOf(viewModel.getPageSize()));
        result.setCurrentIndex(String.valueOf(viewModel.getCurrentIndex()));

        String sRefresh = viewModel.is_refresh()? "true": "false";
        result.set_refresh(sRefresh);

        return result;
    }

    //根据网络返回结果构造屏幕显示数据
    private OtherLoanAppliedQryListViewModel buildViewModelFromResult(PsnOnLineLoanAppliedQryResult qryResult){
        OtherLoanAppliedQryListViewModel result = new OtherLoanAppliedQryListViewModel();
        result.setRecordNumber(qryResult.getRecordNumber());

        List<OtherLoanAppliedQryListViewModel.ListBean> viewList = new LinkedList<>();
        List<PsnOnLineLoanAppliedQryResult.ListBean> returnList = qryResult.getList();
        if(returnList != null){
            for(int i = 0; i < returnList.size(); i ++){
                OtherLoanAppliedQryListViewModel.ListBean viewBean = new OtherLoanAppliedQryListViewModel.ListBean();
                PsnOnLineLoanAppliedQryResult.ListBean returnBean = returnList.get(i);

                viewBean.setLoanNumber(returnBean.getLoanNumber());
                viewBean.setProductCode(returnBean.getProductCode());
                viewBean.setProductName(returnBean.getProductName());
                viewBean.setName(returnBean.getName());
                viewBean.setLoanAmount(returnBean.getLoanAmount());
                viewBean.setLoanTerm(returnBean.getLoanTerm());
                viewBean.setApplyDate(returnBean.getApplyDate());
                viewBean.setCurrency(returnBean.getCurrency());
                viewBean.setLoanStatus(returnBean.getLoanStatus());
                viewBean.setRefuseReason(returnBean.getRefuseReason());

                viewList.add(viewBean);
            }
        }
        result.setList(viewList);

        return result;
    }

}
