package com.boc.bocsoft.mobile.bocmobile.buss.easybuss.ui;

import com.boc.bocsoft.mobile.bocmobile.base.activity.web.ui.WebContract;
import com.boc.bocsoft.mobile.bocmobile.buss.easybuss.model.RedirectEzucBean;

public class EasyBussContract {

    public interface View extends WebContract.View {
        void onQryRedirectEzucSuccess(RedirectEzucBean redirectEzucBean);
        void onQryRedirectEzucFailed(String errorMessage);

        void onQryInvestOpenStateFailed(String errorMessage);
        void onQryInvestOpenStateSuccess(Boolean result);

    }

    public interface Presenter extends WebContract.Presenter {

        /**
         * 查询开通投资理财服务
         */
        void qryInvestOpenState();

        /**
         * 获取E商互信登录数据
         */
        void onQryRedirectEzuc();

        /**
         * @param heartBeatPeriod 心跳包周期
         */
        void qryHeartBeat(int heartBeatPeriod);
    }
}