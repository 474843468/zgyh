package com.boc.bocsoft.mobile.bocmobile.buss.loan.nonreloan.query.presenter;

import android.text.TextUtils;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepayAccountDetailQuery.PsnDrawingDetailParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepayAccountDetailQuery.PsnDrawingDetailResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.nonreloan.query.ui.NonReloanQryContract;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.ui.ReloanStatusContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

/**
 * 分次动用贷款-详情查询 通信逻辑处理
 * Created by liuzc on 2016/8/16.
 */
public class NonReloanQryPresenter extends RxPresenter implements NonReloanQryContract.Presenter {

    private NonReloanQryContract.View mContractView;
    private PsnLoanService mLoanService;

    /**
     * 查询转账记录service
     */

    public NonReloanQryPresenter(NonReloanQryContract.View loanOnlineQryView){
        mContractView = loanOnlineQryView;
        mContractView.setPresenter(this);
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
