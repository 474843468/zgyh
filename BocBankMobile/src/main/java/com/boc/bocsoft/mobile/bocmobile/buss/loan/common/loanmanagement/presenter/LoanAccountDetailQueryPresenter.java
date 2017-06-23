package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.presenter;

import android.text.TextUtils;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepayAccountDetailQuery.PsnDrawingDetailParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepayAccountDetailQuery.PsnDrawingDetailResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.ui.LoanMangerContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

/**
 * Created by taoyongzhen on 2016/11/4.
 */

public class LoanAccountDetailQueryPresenter extends RxPresenter implements LoanMangerContract.AccountDetailQuaryPresenter{

    private LoanMangerContract.AccountDetailQuaryView accountDetailQuaryView;
    private PsnLoanService mPsnLoanService;

    public LoanAccountDetailQueryPresenter(LoanMangerContract.AccountDetailQuaryView accountDetailQuaryView) {
        this.accountDetailQuaryView = accountDetailQuaryView;
        mPsnLoanService = new PsnLoanService();
    }

    @Override
    public void queryAccountDetail(String loanAccount) {

        PsnDrawingDetailParams psnDrawingDetailParams = builElaonDrawingParams(loanAccount);
        mPsnLoanService.psnDrawingDetail(psnDrawingDetailParams)
                .compose(this.<PsnDrawingDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnDrawingDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnDrawingDetailResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        accountDetailQuaryView.quaryAccountDetailFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnDrawingDetailResult psnDrawingDetailResult) {
                        accountDetailQuaryView.quaryAccounDetailSuccess(psnDrawingDetailResult);
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
