package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.model.FacilityInquiryViewModel;

/**
 * Created by XieDu on 2016/7/11.
 */
public class FacilityInquiryContact {
    public interface View{
        void onLoadSuccess(FacilityInquiryViewModel viewModel);
        void onLoadFailed();

    }
    public interface  Presenter{
        void requestPsnLOANQuotaQuery();
    }
}
