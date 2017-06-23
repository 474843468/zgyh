package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.ui;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAccountListAndDetailQuery.PsnLOANAccountListAndDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAccountListAndDetailQuery.PsnLOANAccountListAndDetailQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by XieDu on 2016/7/12.
 */
public class FacilityUseRecordQryContact {
    public interface View extends BaseView<Presenter>{
        void onLoadSuccess(PsnLOANAccountListAndDetailQueryResult model);
        void onLoadFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter{
        void getUseFacilityRecord(PsnLOANAccountListAndDetailQueryParams params);
    }
}
