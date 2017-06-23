package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.presenter;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanCityQry.PsnOnLineLoanCityQryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanCityQry.PsnOnLineLoanCityQryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanProvinceQry.PsnOnLineLoanProvinceQryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanProvinceQry.PsnOnLineLoanProvinceQryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.OnLineLoanCityBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.OnLineLoanProvinceBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.ui.LoanDistrictSelectContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieDu on 2016/7/19.
 */
public class LoanDistrictSelectPresenter extends RxPresenter
        implements LoanDistrictSelectContract.Presenter {

    private LoanDistrictSelectContract.View mLoanApplyOtherChooseView;
    private PsnLoanService mLoanService;

    public LoanDistrictSelectPresenter(LoanDistrictSelectContract.View view) {
        mLoanApplyOtherChooseView = view;
        mLoanService = new PsnLoanService();
    }

    @Override
    public void getOnlineLoanProvinces() {
        mLoanService.psnOnLineLoanProvinceQry(new PsnOnLineLoanProvinceQryParams())
                    .compose(this.<PsnOnLineLoanProvinceQryResult>bindToLifecycle())
                    .compose(SchedulersCompat.<PsnOnLineLoanProvinceQryResult>applyIoSchedulers())
                    .subscribe(new BIIBaseSubscriber<PsnOnLineLoanProvinceQryResult>() {
                        @Override
                        public void handleException(
                                BiiResultErrorException biiResultErrorException) {
                            mLoanApplyOtherChooseView.onLoadProvincesFail(biiResultErrorException);

                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(
                                PsnOnLineLoanProvinceQryResult psnOnLineLoanProvinceQryResult) {
                            List<OnLineLoanProvinceBean> provinces = null;
                            if (psnOnLineLoanProvinceQryResult != null) {
                                provinces = new ArrayList<>();
                                for (PsnOnLineLoanProvinceQryResult.PsnOnLineLoanProvinceBean bean : psnOnLineLoanProvinceQryResult
                                        .getList()) {
                                    provinces.add(BeanConvertor.toBean(bean,
                                            new OnLineLoanProvinceBean()));
                                }
                            }
                            mLoanApplyOtherChooseView.onLoadProvincesSuccess(provinces);
                        }
                    });
    }

    @Override
    public void getOnlineLoanCities(final String provinceCode) {
        PsnOnLineLoanCityQryParams params = new PsnOnLineLoanCityQryParams();
        params.setProvinceCode(provinceCode);
        mLoanService.psnOnLineLoanCityQry(params)
                    .compose(this.<PsnOnLineLoanCityQryResult>bindToLifecycle())
                    .compose(SchedulersCompat.<PsnOnLineLoanCityQryResult>applyIoSchedulers())
                    .subscribe(new BIIBaseSubscriber<PsnOnLineLoanCityQryResult>() {
                        @Override
                        public void handleException(
                                BiiResultErrorException biiResultErrorException) {
                            mLoanApplyOtherChooseView.onLoadCitiesFail(biiResultErrorException);

                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(PsnOnLineLoanCityQryResult psnOnLineLoanCityQryResult) {
                            List<OnLineLoanCityBean> cities = null;
                            if (psnOnLineLoanCityQryResult != null) {
                                cities = new ArrayList<>();
                                for (PsnOnLineLoanCityQryResult.PsnOnLineLoanCityQryBean bean : psnOnLineLoanCityQryResult
                                        .getList()) {
                                    cities.add(
                                            BeanConvertor.toBean(bean, new OnLineLoanCityBean()));
                                }
                            }
                            mLoanApplyOtherChooseView.onLoadCitiesSuccess(provinceCode, cities);
                        }
                    });
    }
}
