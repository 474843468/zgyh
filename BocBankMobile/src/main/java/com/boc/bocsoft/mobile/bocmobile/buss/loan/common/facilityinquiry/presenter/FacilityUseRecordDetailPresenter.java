package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.presenter;

import android.text.TextUtils;

import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAccountListAndDetailQuery.PsnLOANAccountListAndDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepayAccountDetailQuery.PsnDrawingDetailParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepayAccountDetailQuery.PsnDrawingDetailResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnQueryCardNumByAcctNum.PsnQueryCardNumByAcctNumParams;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.ui.FacilityUseRecDetailContact;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

/**
 * Created by liuzc on 2016/10/18.
 */
public class FacilityUseRecordDetailPresenter extends RxPresenter
        implements FacilityUseRecDetailContact.Presenter {

    private FacilityUseRecDetailContact.View mContractView;
    private PsnLoanService mPsnLoanService;


    public FacilityUseRecordDetailPresenter(FacilityUseRecDetailContact.View view) {
        mContractView = view;
        mPsnLoanService = new PsnLoanService();
    }


    @Override
    public void queryCardNum(String accountNum) {
        PsnQueryCardNumByAcctNumParams params = new PsnQueryCardNumByAcctNumParams();
        params.setAcctNum(accountNum);
        mPsnLoanService.psnQueryCardNumByAcctNumQuery(params)
                .compose(
                        this.<String>bindToLifecycle())
                .compose(
                        SchedulersCompat.<String>applyIoSchedulers())
                .subscribe(
                        new BIIBaseSubscriber<String>() {
                            @Override
                            public void handleException(
                                    BiiResultErrorException biiResultErrorException) {
                                mContractView.queryCardNumFailed();
                            }

                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onNext(
                                    String result) {
                                mContractView.queryCardNumSuccess(result);
                            }
                        });
    }

    @Override
    public void queryDrawingDetail(String loanActNum) {
        PsnDrawingDetailParams psnDrawingDetailParams = builElaonDrawingParams(loanActNum);
        mPsnLoanService.psnDrawingDetail(psnDrawingDetailParams)
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
