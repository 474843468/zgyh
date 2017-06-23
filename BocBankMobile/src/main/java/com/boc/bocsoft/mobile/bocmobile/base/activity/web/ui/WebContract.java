package com.boc.bocsoft.mobile.bocmobile.base.activity.web.ui;

import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

public class WebContract {

    public interface View {
    }

    public interface Presenter extends BasePresenter {

        /**
         *
         * @param heartBeatPeriod 心跳包周期
         */
        void qryHeartBeat(int heartBeatPeriod);
    }
}