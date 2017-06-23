package com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui;

import com.boc.bocsoft.mobile.bocmobile.base.model.GoldBean;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.getallexchangeratesoutlay.GetAllExchangeRatesOutlayParams;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * Created by gwluo on 2016/8/29.
 */
public class GoldContract {
    public interface View extends BaseView<Presenter> {
        void updateGoldView(List<GoldBean> resultList);

        void onFail();
    }

    public interface Presenter extends BasePresenter {
        void getGoldList(GetAllExchangeRatesOutlayParams params);
    }
}
