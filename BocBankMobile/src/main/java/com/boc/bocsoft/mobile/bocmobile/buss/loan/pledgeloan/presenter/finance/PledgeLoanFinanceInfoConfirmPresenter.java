package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.presenter.finance;

import android.support.annotation.NonNull;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeSubmit.PsnLoanXpadPledgeSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeSubmit.PsnLoanXpadPledgeSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeVerify.PsnLoanXpadPledgeVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeVerify.PsnLoanXpadPledgeVerifyResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.BaseSubmitBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmContract;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.finance.PledgeFinanceInfoFillViewModel;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import rx.Observable;
import rx.functions.Func1;

/**
 * 作者：XieDu
 * 创建时间：2016/8/20 15:25
 * 描述：
 */
public class PledgeLoanFinanceInfoConfirmPresenter
        extends BaseConfirmPresenter<PledgeFinanceInfoFillViewModel, String> {

    public PledgeLoanFinanceInfoConfirmPresenter(BaseConfirmContract.View<String> view) {
        super(view);
    }

    @NonNull
    @Override
    protected Observable<VerifyBean> getVerifyBean(PledgeFinanceInfoFillViewModel fillInfo) {
        PsnLoanXpadPledgeVerifyParams verifyParams =
                BeanConvertor.fromBean(fillInfo, new PsnLoanXpadPledgeVerifyParams());
        verifyParams.setToAcctNum(fillInfo.getToActNum());
        verifyParams.setToAccountId(fillInfo.getToAccount());
        verifyParams.setLoanRepayAccountId(fillInfo.getPayAccount());
        return mLoanService.psnLoanXpadPledgeVerify(verifyParams)
                           .compose(this.<PsnLoanXpadPledgeVerifyResult>bindToLifecycle())
                           .map(new Func1<PsnLoanXpadPledgeVerifyResult, VerifyBean>() {
                               @Override
                               public VerifyBean call(
                                       PsnLoanXpadPledgeVerifyResult psnLoanXpadPledgeVerifyResult) {
                                   return BeanConvertor.toBean(psnLoanXpadPledgeVerifyResult,
                                           new VerifyBean());
                               }
                           });
    }

    @Override
    public void submit(final PledgeFinanceInfoFillViewModel fillInfo,
            final BaseSubmitBean submitBean) {
        PsnLoanXpadPledgeSubmitParams params =
                BeanConvertor.fromBean(fillInfo, new PsnLoanXpadPledgeSubmitParams());
        params = BeanConvertor.fromBean(submitBean, params);
        params.setToAcctNum(fillInfo.getToActNum());
        params.setToAccountId(fillInfo.getToAccount());
        params.setLoanRepayAccountId(fillInfo.getPayAccount());
        params.setPledgeRate(fillInfo.getPledgeRate());
        mLoanService.psnLoanXpadPledgeSubmit(params)
                    .compose(this.<PsnLoanXpadPledgeSubmitResult>bindToLifecycle())
                    .compose(SchedulersCompat.<PsnLoanXpadPledgeSubmitResult>applyIoSchedulers())
                    .subscribe(new BIIBaseSubscriber<PsnLoanXpadPledgeSubmitResult>() {
                        @Override
                        public void handleException(
                                BiiResultErrorException biiResultErrorException) {

                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(
                                PsnLoanXpadPledgeSubmitResult psnLoanXpadPledgeSubmitResult) {
                            mView.onSubmitSuccess(psnLoanXpadPledgeSubmitResult.getTransId());
                        }
                    });
    }
}
