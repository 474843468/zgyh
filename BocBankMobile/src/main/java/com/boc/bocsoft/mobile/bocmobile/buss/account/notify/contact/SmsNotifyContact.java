package com.boc.bocsoft.mobile.bocmobile.buss.account.notify.contact;

import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.model.SmsNotifyModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by pactera on 2016/7/12.
 */
public class SmsNotifyContact {
    public interface View extends BaseView<Presenter> {
        //解除签约返回
        void ssmMessageDeleteReturned();

        //批量删除短息
        void psnSsmDeleteReturned();

        //获取UI model
        SmsNotifyModel getUiModel();

    }

    public interface Presenter extends BasePresenter {
        //解除短信签约
        void psnSsmMessageDelete();

        //批量删除短息
        void psnSsmDelete();
    }
}
