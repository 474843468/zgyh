package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hceaddcard;

import android.content.Context;

import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.AddNewCardViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.HceTransactionViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * Created by yangle on 2016/12/15.
 * 描述:
 */
public interface HceAddNewCardContract {

     interface View {

         AddNewCardViewModel getModel();

         Context getContext();

         void showLoading();

         void closeLoading();

         void showError(String msg);

         void applyHceSuccess(HceTransactionViewModel transactionViewModel);

         void applyHceFailure();
    }

     interface Present extends BasePresenter{
        // 闪付卡申请
        void applyHce();
    }
}
