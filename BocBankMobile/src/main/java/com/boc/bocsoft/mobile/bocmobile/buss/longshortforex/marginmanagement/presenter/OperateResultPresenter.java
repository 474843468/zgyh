package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.presenter;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.ui.OperateResultContract;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

/**
 * @author hty7062
 */

public class OperateResultPresenter extends RxPresenter implements OperateResultContract.Presenter{

        private OperateResultContract.View  mOperateResultView;

        public OperateResultPresenter(OperateResultContract.View view) {
        mOperateResultView = view;
    }

}