package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.presenter;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANQuotaQuery.PsnLOANQuotaQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANQuotaQuery.PsnLOANQuotaQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.model.FacilityInquiryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.ui.FacilityInquiryContact;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by XieDu on 2016/7/11.
 */
public class FacilityInquiryPrensenter extends RxPresenter
        implements FacilityInquiryContact.Presenter {
    private PsnLoanService mPsnLoanService;
    private FacilityInquiryContact.View mFacilityInquiryView;

    public FacilityInquiryPrensenter(FacilityInquiryContact.View view) {
        this.mFacilityInquiryView = view;
        mPsnLoanService = new PsnLoanService();
    }

    @Override
    public void requestPsnLOANQuotaQuery() {

        PsnLOANQuotaQueryParams params = new PsnLOANQuotaQueryParams();
        params.setActType(ApplicationConst.LOAN_ACTTYPE);
        params.setQueryType(ApplicationConst.LOAN_QUERYTYPE);
        mPsnLoanService.psnLOANQuotaQuery(params)
                       .compose(this.<List<PsnLOANQuotaQueryResult>>bindToLifecycle())
                       .compose(SchedulersCompat.<List<PsnLOANQuotaQueryResult>>applyIoSchedulers())
                       .subscribe(new BIIBaseSubscriber<List<PsnLOANQuotaQueryResult>>() {
                           @Override
                           public void handleException(
                                   BiiResultErrorException biiResultErrorException) {
                               mFacilityInquiryView.onLoadFailed();
                           }

                           @Override
                           public void onCompleted() {

                           }

                           @Override
                           public void onNext(
                                   List<PsnLOANQuotaQueryResult> psnLOANQuotaQueryResults) {
                               mFacilityInquiryView.onLoadSuccess(
                                       convertViewModel(psnLOANQuotaQueryResults));
                           }
                       });
    }

    private FacilityInquiryViewModel convertViewModel(
            List<PsnLOANQuotaQueryResult> psnLOANQuotaQueryResults) {
        if (psnLOANQuotaQueryResults == null) {
            return null;
        }
        FacilityInquiryViewModel viewModel = new FacilityInquiryViewModel();
        List<FacilityInquiryViewModel.FacilityInquiryBean> facilityInquiryList = new ArrayList<>();
        for (PsnLOANQuotaQueryResult result : psnLOANQuotaQueryResults) {
            FacilityInquiryViewModel.FacilityInquiryBean bean =
                    new FacilityInquiryViewModel.FacilityInquiryBean();
            bean.setAvailableQuota(result.getAvailableQuota());
            bean.setCurrencyCode(result.getCurrencyCode());
            bean.setLoanToDate(result.getLoanToDate());
            bean.setLoanType(result.getLoanType());
            bean.setAvailableQuota(result.getAvailableQuota());
            bean.setQuota(result.getQuota());
            bean.setQuotaNumber(result.getQuotaNumber());
            bean.setQuotaStatus(result.getQuotaStatus());
            bean.setQuotaUsed(result.getQuotaUsed());
            facilityInquiryList.add(bean);
        }
        viewModel.setFacilityInquiryList(facilityInquiryList);
        return viewModel;
    }
}
