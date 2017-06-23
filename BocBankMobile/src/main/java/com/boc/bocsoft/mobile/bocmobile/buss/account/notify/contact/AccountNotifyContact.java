package com.boc.bocsoft.mobile.bocmobile.buss.account.notify.contact;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.model.AccountNotifyModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by wangtong on 2016/6/25.
 */
public class AccountNotifyContact {
    public interface View extends BaseView<Presenter> {

        //请求失败
        void requestFailed(BiiResultErrorException biiResultErrorException);

        //短信开通详情请求成功
        void psnSmsQueryReturned();

        //获取UI数据模型
        AccountNotifyModel getModel();

    }

    public interface Presenter extends BasePresenter {
        //查询短信通知开通详情
        void psnSmsQuery();
    }
}
