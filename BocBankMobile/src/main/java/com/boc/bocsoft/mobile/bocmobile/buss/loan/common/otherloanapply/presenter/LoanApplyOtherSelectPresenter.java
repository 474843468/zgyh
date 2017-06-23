package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.presenter;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanProductQry.PsnOnLineLoanProductParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanProductQry.PsnOnLineLoanProductResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.OnLineLoanProductBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.ui.LoanApplyOtherSelectContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieDu on 2016/7/21.
 */
public class LoanApplyOtherSelectPresenter extends RxPresenter
        implements LoanApplyOtherSelectContract.Presenter {

    private LoanApplyOtherSelectContract.View mLoanApplyOtherSelectView;
    private PsnLoanService mLoanService;

    public LoanApplyOtherSelectPresenter(LoanApplyOtherSelectContract.View view) {
        mLoanApplyOtherSelectView = view;
        mLoanService = new PsnLoanService();
    }

    @Override
    public void getOnlineLoanProducts(String cityCode) {
        PsnOnLineLoanProductParams params = new PsnOnLineLoanProductParams();
        params.setCityCode(cityCode);
        mLoanService.psnOnLineLoanProductQry(params)
                    .compose(this.<PsnOnLineLoanProductResult>bindToLifecycle())
                    .compose(SchedulersCompat.<PsnOnLineLoanProductResult>applyIoSchedulers())
                    .subscribe(new BIIBaseSubscriber<PsnOnLineLoanProductResult>() {
                        @Override
                        public void handleException(
                                BiiResultErrorException biiResultErrorException) {

                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(PsnOnLineLoanProductResult psnOnLineLoanProductResult) {
                            List<OnLineLoanProductBean> products = null;
                            if (psnOnLineLoanProductResult != null) {
                                products = new ArrayList<>();
                                for (PsnOnLineLoanProductResult.PsnOnLineLoanProductBean bean : psnOnLineLoanProductResult
                                        .getList()) {
                                    products.add(BeanConvertor.toBean(bean,
                                            new OnLineLoanProductBean()));
                                }
                            }
                            mLoanApplyOtherSelectView.onLoadProductsSuccess(products);
                        }
                    });
    }
}
