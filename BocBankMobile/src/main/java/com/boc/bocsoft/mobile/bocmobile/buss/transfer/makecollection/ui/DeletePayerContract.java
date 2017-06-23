package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.DeletePayerViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contract:删除付款人
 * Created by zhx on 2016/7/5
 */
public class DeletePayerContract {
    public interface View extends BaseView<Presenter> {

        /**
         * 成功回调：
         * 删除付款人
         */
        void deletePayerSuccess(DeletePayerViewModel deletePayerViewModel);

        /**
         * 失败回调：
         * 删除付款人
         */
        void deletePayerFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 删除付款人
         */
        void modifyPayerMobile(DeletePayerViewModel deletePayerViewModel);
    }
}
