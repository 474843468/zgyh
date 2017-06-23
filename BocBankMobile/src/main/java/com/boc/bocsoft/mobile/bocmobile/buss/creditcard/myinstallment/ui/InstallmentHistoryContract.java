package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.model.InstallmentHistoryViewModel;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by yangle on 2016/11/18.
 */
public interface InstallmentHistoryContract {

     interface View extends BaseView<Presenter>{

         InstallmentHistoryViewModel getViewModel();

         void showLoading();

         void closeLoading();

         void showAndUpdateRecords();

         void loadFailed();

         void showNoRecord();

     }


    interface Presenter {

        /**
         * 加载分期记录
         */
        void loadDividedPayRecords();

    }
}
