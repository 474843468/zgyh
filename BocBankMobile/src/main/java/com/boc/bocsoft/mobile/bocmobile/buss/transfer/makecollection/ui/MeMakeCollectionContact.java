package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.PsnTransActCollectionVerifyViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contact：我要收款
 * Created by zhx on 2016/9/13
 */
public class MeMakeCollectionContact {
    public interface View extends BaseView<Presenter> {
        /**
         * 成功回调：主动收款预交易（单人）
         */
        void collectionVerifySuccess(PsnTransActCollectionVerifyViewModel viewModel);

        /**
         * 失败回调：主动收款预交易（单人）
         */
        void collectionVerifyFailed(BiiResultErrorException biiResultErrorException);
    }


    public interface Presenter extends BasePresenter {
        /**
         * 主动收款预交易（单人）
         */
        void collectionVerify(PsnTransActCollectionVerifyViewModel viewModel);
    }
}
