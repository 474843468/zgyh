package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANHistoryQuery.PsnLOANHistoryQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANHistoryQuery.PsnLOANHistoryQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.model.RepayHistoryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.ui.repayhistory.RepayHistoryContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * 还款计划-历史还款 通信逻辑处理
 * Created by liuzc on 2016/8/11.
 */
public class RepayHistoryPresenter extends RxPresenter implements RepayHistoryContract.Presenter {

    private RepayHistoryContract.View mRepayHistView;
    private PsnLoanService mLoanService;

    /**
     * 会话
     */
    private String conversationID = null;
    /**
     * 公共service
     */
    private GlobalService globalService;
    /**
     * 查询转账记录service
     */

    public RepayHistoryPresenter(RepayHistoryContract.View repayHistoryView){
        mRepayHistView = repayHistoryView;
        mRepayHistView.setPresenter(this);
        globalService = new GlobalService();
        mLoanService = new PsnLoanService();
    }

    @Override
    public void queryRepayHistoryList(final RepayHistoryViewModel repayHistoryViewModel) {
        //测试代码
//        PsnLOANHistoryQueryResult psnLOANHistoryResult = new PsnLOANHistoryQueryResult();
//
//        psnLOANHistoryResult.setRecordNumber(20);
//
//        List<PsnLOANHistoryQueryResult.ListBean> list = new LinkedList<PsnLOANHistoryQueryResult.ListBean>();
//        for(int i = 0; i < 6; i ++){
//            PsnLOANHistoryQueryResult.ListBean bean = new PsnLOANHistoryQueryResult.ListBean();
//            bean.setTransType("PLAB");
//            bean.setInterestForfeit(new BigDecimal(100000));
//            bean.setLoanActNum("40000102811811890");
//            bean.setRepayAmount(new BigDecimal(5462138));
//            bean.setRepayCapital(new BigDecimal(5000000));
//            bean.setRepayDate("2011/03/18");
//            bean.setRepayId("654321");
//            bean.setRepayInterest(new BigDecimal(200510));
//            bean.setRepayForfeit(new BigDecimal(200010));
//            list.add(bean);
//        }
//        psnLOANHistoryResult.setList(list);
//        RepayHistoryViewModel viewModel = buildViewModelFromResult(psnLOANHistoryResult);
//        mRepayHistView.queryRepayHistoryListSuccess(viewModel);

    	if(conversationID == null){
    		queryWithoutConversation(repayHistoryViewModel);
    	}
    	else{
    		queryWithConversation(repayHistoryViewModel);
    	}
    }
    
    private void queryWithConversation(final RepayHistoryViewModel repayRemainViewModel){
    	PsnLOANHistoryQueryParams params = buildParamsFromView(repayRemainViewModel);
    	params.setConversationId(conversationID);
    	params.set_refresh("false");
    	mLoanService.psnRepayHistoryReturnInfo(params)
        .compose(this.<PsnLOANHistoryQueryResult>bindToLifecycle())
        .compose(SchedulersCompat.<PsnLOANHistoryQueryResult>applyIoSchedulers())
        .subscribe(new BIIBaseSubscriber<PsnLOANHistoryQueryResult>() {
        	//重写请求失败弹窗处理
            @Override
            public void commonHandleException(BiiResultErrorException biiResultErrorException) {
            	
            }
        	
        	@Override
            public void onCompleted() {
        		
            }

        	@Override
            public void onNext(PsnLOANHistoryQueryResult psnLOANHistoryResult) {
                RepayHistoryViewModel viewModel = buildViewModelFromResult(psnLOANHistoryResult);
                mRepayHistView.queryRepayHistoryListSuccess(viewModel);
            }

            @Override
            public void handleException(BiiResultErrorException biiResultErrorException) {
                mRepayHistView.queryRepayHistoryListFail(biiResultErrorException);
            }
        });
    }
    
    private void queryWithoutConversation(final RepayHistoryViewModel repayRemainViewModel){
    	globalService.psnCreatConversation(new PSNCreatConversationParams())
        .compose(this.<String>bindToLifecycle())
        .flatMap(new Func1<String, Observable<PsnLOANHistoryQueryResult>>() {
            @Override
            public Observable<PsnLOANHistoryQueryResult> call(
                    String conversationId) {
            	conversationID = conversationId;
                PsnLOANHistoryQueryParams params =
                        buildParamsFromView(repayRemainViewModel);
                params.setConversationId(conversationId);
                return mLoanService.psnRepayHistoryReturnInfo(params);
            }
        })
        .compose(SchedulersCompat.<PsnLOANHistoryQueryResult>applyIoSchedulers())
        .subscribe(new BIIBaseSubscriber<PsnLOANHistoryQueryResult>() {

        	 //重写请求失败弹窗处理
        	
            @Override
            public void commonHandleException(BiiResultErrorException biiResultErrorException) {
            	
            }
        	
        	@Override
            public void onCompleted() {
        		
            }

            @Override
            public void onNext(PsnLOANHistoryQueryResult psnLOANHistoryResult) {
                RepayHistoryViewModel viewModel = buildViewModelFromResult(psnLOANHistoryResult);
                mRepayHistView.queryRepayHistoryListSuccess(viewModel);
            }

            @Override
            public void handleException(BiiResultErrorException biiResultErrorException) {
                mRepayHistView.queryRepayHistoryListFail(biiResultErrorException);
            }
        });
    }

    //根据屏幕数据构造请求参数
    private PsnLOANHistoryQueryParams buildParamsFromView(RepayHistoryViewModel repayHistoryViewModel){
        PsnLOANHistoryQueryParams result = new PsnLOANHistoryQueryParams();
        result.setCurrentIndex(repayHistoryViewModel.getCurrentIndex());
        result.set_refresh(repayHistoryViewModel.is_refresh());
        result.setActNum(repayHistoryViewModel.getActNum());
        result.setEndDate(repayHistoryViewModel.getEndDate());
        result.setPageSize(repayHistoryViewModel.getPageSize());
        return result;
    }

    //根据网络返回结果构造屏幕显示数据
    private RepayHistoryViewModel buildViewModelFromResult(PsnLOANHistoryQueryResult psnLOANHistoryResult){
        RepayHistoryViewModel result = new RepayHistoryViewModel();
        result.setRecordNumber(psnLOANHistoryResult.getRecordNumber());

        List<RepayHistoryViewModel.ListBean> viewList = new LinkedList<RepayHistoryViewModel.ListBean>();
        List<PsnLOANHistoryQueryResult.ListBean> returnList = psnLOANHistoryResult.getList();
        if(returnList != null){
            for(int i = 0; i < returnList.size(); i ++){
                RepayHistoryViewModel.ListBean viewBean = new RepayHistoryViewModel.ListBean();
                PsnLOANHistoryQueryResult.ListBean returnBean = returnList.get(i);

                viewBean.setTransType(returnBean.getTransType());
                viewBean.setInterestForfeit(returnBean.getInterestForfeit());
                viewBean.setLoanActNum(returnBean.getLoanActNum());
                viewBean.setRepayAmount(returnBean.getRepayAmount());
                viewBean.setRepayCapital(returnBean.getRepayCapital());
                viewBean.setRepayDate(returnBean.getRepayDate());
                viewBean.setRepayId(returnBean.getRepayId());
                viewBean.setRepayInterest(returnBean.getRepayInterest());
                viewBean.setRepayForfeit(returnBean.getRepayForfeit());

                viewList.add(viewBean);
            }
        }
        result.setList(viewList);

        return result;
    }
}
