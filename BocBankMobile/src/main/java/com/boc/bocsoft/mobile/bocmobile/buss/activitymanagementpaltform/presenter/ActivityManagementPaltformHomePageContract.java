package com.boc.bocsoft.mobile.bocmobile.buss.activitymanagementpaltform.presenter;

import com.boc.bocsoft.mobile.bocmobile.base.activity.web.ui.WebContract;
import com.boc.bocsoft.mobile.bocmobile.buss.activitymanagementpaltform.model.psnActivityInfoQuery.PsnActivityInfoQueryResModel;

/**
 * Created by yx on 2016/12/19.
 */

public class ActivityManagementPaltformHomePageContract {
    public interface WeChatLuckDrawView extends WebContract.View {

        //3.36 036 PsnActivityInfoQuery 活动管理平台取票 成功
        void onPsnActivityInfoQuerySuccess(PsnActivityInfoQueryResModel psnActivityInfoQueryResModel);

        //3.36 036 PsnActivityInfoQuery 活动管理平台取票 失败
        void onPsnActivityInfoQueryFailed(String errorMessage);
    }

    public interface WeChatLuckDrawPresenter extends WebContract.Presenter {
        /**
         * 3.36 036 PsnActivityInfoQuery 活动管理平台取票
         */
        void getPsnActivityInfoQuery();

        /**
         * @param heartBeatPeriod 心跳包周期
         */
        void qryHeartBeat(int heartBeatPeriod);
    }
}
