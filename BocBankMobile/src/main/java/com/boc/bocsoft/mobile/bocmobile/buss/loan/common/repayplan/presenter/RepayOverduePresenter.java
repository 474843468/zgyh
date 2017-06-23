package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANOverdueQuery.PsnEOverdueQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANOverdueQuery.PsnLOANOverdueQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANOverdueQuery.PsnLOANOverdueQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.model.RepayOverdueViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.ui.repayoverdue.RepayOverdueContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * 还款计划-逾期还款 通信逻辑处理
 * Created by liuzc on 2016/8/12.
 */
public class RepayOverduePresenter extends RxPresenter implements RepayOverdueContract.Presenter {

    private RepayOverdueContract.View mRepayOverdueView;
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

    public RepayOverduePresenter(RepayOverdueContract.View repayOverdueView){
        mRepayOverdueView = repayOverdueView;
        mRepayOverdueView.setPresenter(this);
        globalService = new GlobalService();
        mLoanService = new PsnLoanService();
    }

    @Override
    public void queryRepayOverdueList(final RepayOverdueViewModel repayOverdueViewModel) {
        //测试代码
//        PsnLOANOverdueQueryResult psnLOANOverdueResult = new PsnLOANOverdueQueryResult();
//
//        /**
//         * overdueAmountSum : 800
//         * overdueCapitalSum : 100000
//         * overdueForfeitSum : 660
//         * overdueInterestSum : 1160
//         * overdueIssueSum : 24
//         */
//        psnLOANOverdueResult.setOverdueAmountSum(new BigDecimal(800));
//        psnLOANOverdueResult.setOverdueCapitalSum(new BigDecimal(100000));
//        psnLOANOverdueResult.setOverdueInterestSum(new BigDecimal(1160));
//        psnLOANOverdueResult.setOverdueIssueSum(24);
//
//        List<PsnLOANOverdueQueryResult.ListBean> list = new LinkedList<PsnLOANOverdueQueryResult.ListBean>();
//        for(int i = 0; i < 6; i ++){
//            PsnLOANOverdueQueryResult.ListBean bean = new PsnLOANOverdueQueryResult.ListBean();
//
//            bean.setOverdueAmount(new BigDecimal(10000));
//            bean.setOverdueCapital(new BigDecimal(600000));
//            bean.setOverdueInterest(new BigDecimal(2460));
//            bean.setPymtDate("2011/03/18");
//            list.add(bean);
//        }
//        psnLOANOverdueResult.setList(list);
//        RepayOverdueViewModel viewModel = buildViewModelFromResult(psnLOANOverdueResult);
//        mRepayOverdueView.queryRepayOverdueListSuccess(viewModel);

    	if(conversationID == null){
    		queryWithoutConversation(repayOverdueViewModel);
    	}
    	else{
    		queryWithConversation(repayOverdueViewModel);
    	}
    
    }
    
    private void queryWithConversation(final RepayOverdueViewModel repayOverdueViewModel){
    	PsnLOANOverdueQueryParams params = buildParamsFromView(repayOverdueViewModel);
    	params.setConversationId(conversationID);
    	params.set_refresh("false");
    	mLoanService.psnRepayOverdueReturnInfo(params)
        .compose(this.<PsnLOANOverdueQueryResult>bindToLifecycle())
        .compose(SchedulersCompat.<PsnLOANOverdueQueryResult>applyIoSchedulers())
        .subscribe(new BIIBaseSubscriber<PsnLOANOverdueQueryResult>() {
        	//重写请求失败弹窗处理
            @Override
            public void commonHandleException(BiiResultErrorException biiResultErrorException) {
            	
            }
        	
        	@Override
            public void onCompleted() {
        		
            }

        	@Override
            public void onNext(PsnLOANOverdueQueryResult psnLOANOverdueResult) {
                RepayOverdueViewModel viewModel = buildViewModelFromResult(psnLOANOverdueResult);
                mRepayOverdueView.queryRepayOverdueListSuccess(viewModel);
            }

            @Override
            public void handleException(BiiResultErrorException biiResultErrorException) {
            	mRepayOverdueView.queryRepayOverdueListFail(biiResultErrorException);
            }
        });
    }
    
    private void queryWithoutConversation(final RepayOverdueViewModel repayOverdueViewModel){
    	globalService.psnCreatConversation(new PSNCreatConversationParams())
        .compose(this.<String>bindToLifecycle())
        .flatMap(new Func1<String, Observable<PsnLOANOverdueQueryResult>>() {
            @Override
            public Observable<PsnLOANOverdueQueryResult> call(
                    String conversationId) {
            	conversationID = conversationId;
                PsnLOANOverdueQueryParams params =
                        buildParamsFromView(repayOverdueViewModel);
                params.setConversationId(conversationId);
                return mLoanService.psnRepayOverdueReturnInfo(params);
            }
        })
        .compose(SchedulersCompat.<PsnLOANOverdueQueryResult>applyIoSchedulers())
        .subscribe(new BIIBaseSubscriber<PsnLOANOverdueQueryResult>() {

        	 //重写请求失败弹窗处理
        	
            @Override
            public void commonHandleException(BiiResultErrorException biiResultErrorException) {
            	
            }
        	
        	@Override
            public void onCompleted() {
        		
            }

            @Override
            public void onNext(PsnLOANOverdueQueryResult psnLOANOverdueResult) {
                RepayOverdueViewModel viewModel = buildViewModelFromResult(psnLOANOverdueResult);
                mRepayOverdueView.queryRepayOverdueListSuccess(viewModel);
            }

            @Override
            public void handleException(BiiResultErrorException biiResultErrorException) {
            	mRepayOverdueView.queryRepayOverdueListFail(biiResultErrorException);
            }
        });
    }

    //根据屏幕数据构造请求参数
    private PsnLOANOverdueQueryParams buildParamsFromView(RepayOverdueViewModel repayOverdueViewModel){
        PsnLOANOverdueQueryParams result = new PsnLOANOverdueQueryParams();
        result.setCurrentIndex(repayOverdueViewModel.getCurrentIndex());
        result.set_refresh(repayOverdueViewModel.is_refresh());
        result.setActNum(repayOverdueViewModel.getActNum());
        result.setPageSize(repayOverdueViewModel.getPageSize());
        return result;
    }

    //根据网络返回结果构造屏幕显示数据
    private RepayOverdueViewModel buildViewModelFromResult(PsnLOANOverdueQueryResult psnLOANOverdueResult){
        RepayOverdueViewModel result = new RepayOverdueViewModel();
        
        PsnLOANOverdueQueryResult.OverdueLoanObj overdue = psnLOANOverdueResult.getOverdueLoanObj();
        
      
//        result.setOverdueAmountSum(psnLOANOverdueResult.getOverdueAmountSum());
//        result.setOverdueCapitalSum(psnLOANOverdueResult.getOverdueCapitalSum());
//        result.setOverdueInterestSum(psnLOANOverdueResult.getOverdueInterestSum());
//        result.setOverdueIssueSum(psnLOANOverdueResult.getOverdueIssueSum());
//        
        
        result.setOverdueAmountSum(overdue.getOverdueAmountSum());
        result.setOverdueCapitalSum(overdue.getOverdueCapitalSum());
        result.setOverdueInterestSum(overdue.getOverdueInterestSum());
        result.setOverdueIssueSum(overdue.getOverdueIssueSum());

        List<RepayOverdueViewModel.ListBean> viewList = new LinkedList<RepayOverdueViewModel.ListBean>();
       // List<PsnLOANOverdueQueryResult.ListBean> returnList = psnLOANOverdueResult.getList();
        
        List<PsnLOANOverdueQueryResult.OverdueLoanObj.OverdueLoanBean> returnList = psnLOANOverdueResult.getOverdueLoanObj().getOverdueList();
        if(returnList != null){
            for(int i = 0; i < returnList.size(); i ++){
                RepayOverdueViewModel.ListBean viewBean = new RepayOverdueViewModel.ListBean();
               // PsnLOANOverdueQueryResult.ListBean returnBean = returnList.get(i);
                PsnLOANOverdueQueryResult.OverdueLoanObj.OverdueLoanBean returnBean
                = psnLOANOverdueResult.getOverdueLoanObj().getOverdueList().get(i);
                viewBean.setOverdueAmount(returnBean.getOverdueAmount());
                viewBean.setOverdueCapital(returnBean.getOverdueCapital());
                viewBean.setOverdueInterest(returnBean.getOverdueInterest());
                viewBean.setPymtDate(returnBean.getPymtDate());

                viewList.add(viewBean);
            }
        }
        result.setList(viewList);

        return result;
    }
}
