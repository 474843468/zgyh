package com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui;

import com.boc.bocsoft.mobile.bocmobile.base.model.FessBean;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.getexchangeoutlay.GetExchangeOutlayParams;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * Created by gwluo on 2016/8/29.
 */
public class FessContract {
    public interface View extends BaseView<Presenter> {
        void updateFessView(List<FessBean> resultList);
        void updateFail();
    }

    public interface Presenter extends BasePresenter {
        void getFessList(GetExchangeOutlayParams params);
    }
}
