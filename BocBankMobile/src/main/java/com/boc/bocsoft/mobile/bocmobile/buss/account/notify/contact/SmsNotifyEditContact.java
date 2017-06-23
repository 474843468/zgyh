package com.boc.bocsoft.mobile.bocmobile.buss.account.notify.contact;

import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.model.SmsNotifyEditModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by wangtong on 2016/6/25.
 */
public class SmsNotifyEditContact {
    public interface View extends BaseView<Presenter> {
        //短信验证码发送失败
        void smsSendReturned();

        //获取UI model
        SmsNotifyEditModel getUiModel();

        void psnSsmMessageAddReturned();
    }

    public interface Presenter extends BasePresenter {

        //发送短信验证码
        void psnSsmSend(boolean isSign);

        //添加短息开通服务
        void psnSsmMessageAdd();
    }
}
