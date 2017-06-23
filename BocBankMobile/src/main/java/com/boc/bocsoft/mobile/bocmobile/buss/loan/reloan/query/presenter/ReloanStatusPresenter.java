package com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.presenter;

import android.text.TextUtils;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepayAccountDetailQuery.PsnDrawingDetailParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepayAccountDetailQuery.PsnDrawingDetailResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanMinAmountQuery.PsnLOANCycleLoanMinAmountQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanDrawDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.model.ReloanUseRecordsViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.ui.ReloanStatusContract;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.ui.ReloanUseRecordsContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * 分次动用贷款-详情查询 通信逻辑处理
 * Created by liuzc on 2016/8/16.
 */
public class ReloanStatusPresenter extends RxPresenter implements ReloanStatusContract.Presenter {

    private ReloanStatusContract.View mContractView;
    private PsnLoanService mLoanService;
    /**
     * 公共service
     */
    private GlobalService globalService;

    /**
     * 查询转账记录service
     */

    public ReloanStatusPresenter(ReloanStatusContract.View loanOnlineQryView){
        mContractView = loanOnlineQryView;
        mContractView.setPresenter(this);

        globalService = new GlobalService();
        mLoanService = new PsnLoanService();
    }

    /**
     *007用款详情查询接口
     *
     */
    @Override
    public void queryDrawingDetail(String loanActNum) {
//        PsnDrawingDetailResult result = new PsnDrawingDetailResult();
//        result.setLoanRepayPeriod("01");
//        mContractView.eDrawingDetailSuccess(result);

        PsnDrawingDetailParams psnDrawingDetailParams = builElaonDrawingParams(loanActNum);
        mLoanService.psnDrawingDetail(psnDrawingDetailParams)
                .compose(this.<PsnDrawingDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnDrawingDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnDrawingDetailResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mContractView.eDrawingDetailFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnDrawingDetailResult psnDrawingDetailResult) {
                        mContractView.eDrawingDetailSuccess(psnDrawingDetailResult);
                    }
                });
    }

    @Override
    public void queryLoanCycleMinAmount(final PsnLOANCycleLoanMinAmountQueryParams params) {
        globalService.psnCreatConversation(new PSNCreatConversationParams())
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(
                            String conversationId) {
                        params.setConversationId(conversationId);
                        return mLoanService.psnLOANCycleLoanMinAmountQuery(params);
                    }
                })
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<String>() {
                    //重写请求失败弹窗处理
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorMessage(e.getErrorMessage());
                        errorException.setErrorType(e.getErrorType());
                        mContractView.queryLoanCycleMinAmountFail(errorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(String result) {
                        mContractView.queryLoanCycleMinAmountSuccess(result);
                    }
                });
    }

    /**
     * 用款详情查询上送参数校验
     * @param loanActNum 贷款账户
     */
    private PsnDrawingDetailParams builElaonDrawingParams(String loanActNum) {
        PsnDrawingDetailParams psnDrawingDetailParams = new PsnDrawingDetailParams();
        if (!TextUtils.isEmpty(loanActNum)) {
            psnDrawingDetailParams.setLoanAccount(loanActNum);
        }
        return psnDrawingDetailParams;
    }

}
