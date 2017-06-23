package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.SavePayerViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contract:主动收款保存常用付款人
 * Created by zhx on 2016/7/5
 */
public class SavePayerContract {
    public interface View extends BaseView<Presenter> {

        /**
         * 成功回调：
         * 主动收款保存常用付款人
         */
        void savePayerSuccess(SavePayerViewModel savePayerViewModel);

        /**
         * 失败回调：
         * 主动收款保存常用付款人
         */
        void savePayerFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 主动收款保存常用付款人
         */
        void savePayer(SavePayerViewModel savePayerViewModel);
    }
}
