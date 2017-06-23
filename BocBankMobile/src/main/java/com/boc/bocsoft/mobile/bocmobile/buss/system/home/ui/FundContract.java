package com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.fundqueryoutlay.FundQueryOutlayParams;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by gwluo on 2016/8/29.
 */
public class FundContract {
    public interface View extends BaseView<Presenter> {
        void updateFundView(FundQueryOutlayParams fundQueryOutlay);
        void updateFail();
    }

    public interface Presenter extends BasePresenter {
        void getFundList(FundQueryOutlayParams params);
    }
}
