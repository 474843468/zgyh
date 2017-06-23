package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.presenter;

import android.util.Log;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANRemainQuery.PsnLOANRemainQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANRemainQuery.PsnLOANRemainQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.model.RepayRemainViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.ui.repayremain.RepayRemainContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * 还款计划-剩余还款 通信逻辑处理
 * Created by liuzc on 2016/8/12.
 */
public class RepayRemainPresenter extends RxPresenter implements RepayRemainContract.Presenter {

    private RepayRemainContract.View mRepayRemainView;
    private PsnLoanService mLoanService;

    /**
     * 会话ID
     */
    private String conversationID = null;
    /**
     * 公共service
     */
    private GlobalService globalService;
    /**
     * 查询转账记录service
     */
    
    public RepayRemainPresenter(RepayRemainContract.View repayRemainView){
        mRepayRemainView = repayRemainView;
        mRepayRemainView.setPresenter(this);
        globalService = new GlobalService();
        mLoanService = new PsnLoanService();
    }

    @Override
    public void queryRepayRemainList(final RepayRemainViewModel repayRemainViewModel) {
        //测试代码
//        PsnLOANRemainQueryResult psnLOANRemainResult = new PsnLOANRemainQueryResult();
//
//        psnLOANRemainResult.setRecordNumber(20);
//
//        List<PsnLOANRemainQueryResult.ListBean> list = new LinkedList<PsnLOANRemainQueryResult.ListBean>();
//        for(int i = 0; i < 6; i ++){
//            PsnLOANRemainQueryResult.ListBean bean = new PsnLOANRemainQueryResult.ListBean();
//            bean.setRepayDate("2015/05/06");
//            bean.setRemainAmount(new BigDecimal(949));
//            bean.setRemainCapital(new BigDecimal(245));
//            bean.setLoanId("1");
//            bean.setRemainInterest(new BigDecimal(704));
//            list.add(bean);
//        }
//        psnLOANRemainResult.setList(list);
//        RepayRemainViewModel viewModel = buildViewModelFromResult(psnLOANRemainResult);
////        mRepayRemainView.queryRepayRemainListSuccess(viewModel);
//        mRepayRemainView.queryRepayRemainListFail(null);

    	if(conversationID == null){
    		queryWithoutConversation(repayRemainViewModel);
    	}
    	else{
    		queryWithConversation(repayRemainViewModel);
    	}
        
    }
    
    private void queryWithConversation(final RepayRemainViewModel repayRemainViewModel){
    	PsnLOANRemainQueryParams params = buildParamsFromView(repayRemainViewModel);
    	params.setConversationId(conversationID);
    	params.set_refresh("false");
    	mLoanService.psnRepayRemainReturnInfo(params)
        .compose(this.<PsnLOANRemainQueryResult>bindToLifecycle())
        .compose(SchedulersCompat.<PsnLOANRemainQueryResult>applyIoSchedulers())
        .subscribe(new BIIBaseSubscriber<PsnLOANRemainQueryResult>() {
        	//重写请求失败弹窗处理
            @Override
            public void commonHandleException(BiiResultErrorException biiResultErrorException) {
            	
            }
        	
        	@Override
            public void onCompleted() {
        		
            }

            @Override
            public void onNext(PsnLOANRemainQueryResult psnLOANRemainResult) {
                RepayRemainViewModel viewModel = buildViewModelFromResult(psnLOANRemainResult);
                mRepayRemainView.queryRepayRemainListSuccess(viewModel);
            }

            @Override
            public void handleException(BiiResultErrorException biiResultErrorException) {
                mRepayRemainView.queryRepayRemainListFail(biiResultErrorException);
            }
        });
    }
    
    private void queryWithoutConversation(final RepayRemainViewModel repayRemainViewModel){
    	globalService.psnCreatConversation(new PSNCreatConversationParams())
        .compose(this.<String>bindToLifecycle())
        .flatMap(new Func1<String, Observable<PsnLOANRemainQueryResult>>() {
            @Override
            public Observable<PsnLOANRemainQueryResult> call(
                    String conversationId) {
            	conversationID = conversationId;
                PsnLOANRemainQueryParams params =
                        buildParamsFromView(repayRemainViewModel);
                params.setConversationId(conversationId);
                return mLoanService.psnRepayRemainReturnInfo(params);
            }
        })
        .compose(SchedulersCompat.<PsnLOANRemainQueryResult>applyIoSchedulers())
        .subscribe(new BIIBaseSubscriber<PsnLOANRemainQueryResult>() {
        	//重写请求失败弹窗处理
            @Override
            public void commonHandleException(BiiResultErrorException biiResultErrorException) {
            	
            }
        	
        	@Override
            public void onCompleted() {
        		
            }

            @Override
            public void onNext(PsnLOANRemainQueryResult psnLOANRemainResult) {
                RepayRemainViewModel viewModel = buildViewModelFromResult(psnLOANRemainResult);
                mRepayRemainView.queryRepayRemainListSuccess(viewModel);
            }

            @Override
            public void handleException(BiiResultErrorException biiResultErrorException) {
                mRepayRemainView.queryRepayRemainListFail(biiResultErrorException);
            }
        });
    }

    //根据屏幕数据构造请求参数
    private PsnLOANRemainQueryParams buildParamsFromView(RepayRemainViewModel repayRemainViewModel){
        PsnLOANRemainQueryParams result = new PsnLOANRemainQueryParams();
        result.setCurrentIndex(repayRemainViewModel.getCurrentIndex());
        result.set_refresh(repayRemainViewModel.get_refresh());
        result.setActNum(repayRemainViewModel.getActNum());
        result.setPageSize(repayRemainViewModel.getPageSize());
        return result;
    }

    //根据网络返回结果构造屏幕显示数据
    private RepayRemainViewModel buildViewModelFromResult(PsnLOANRemainQueryResult psnLOANRemainResult){
        RepayRemainViewModel result = new RepayRemainViewModel();
        result.setRecordNumber(psnLOANRemainResult.getRecordNumber());

        List<RepayRemainViewModel.ListBean> viewList = new LinkedList<RepayRemainViewModel.ListBean>();
        List<PsnLOANRemainQueryResult.ListBean> returnList = psnLOANRemainResult.getList();
        if(returnList != null){
            for(int i = 0; i < returnList.size(); i ++){
                RepayRemainViewModel.ListBean viewBean = new RepayRemainViewModel.ListBean();
                PsnLOANRemainQueryResult.ListBean returnBean = returnList.get(i);
             
                viewBean.setRepayDate(returnBean.getRepayDate());
                viewBean.setRemainAmount(returnBean.getRemainAmount());
                viewBean.setRemainCapital(returnBean.getRemainCapital());
                viewBean.setRemainInterest(returnBean.getRemainInterest());
                viewBean.setLoanId(returnBean.getLoanId());

                viewList.add(viewBean);
            }
        }
        result.setList(viewList);

        return result;
    }
}
