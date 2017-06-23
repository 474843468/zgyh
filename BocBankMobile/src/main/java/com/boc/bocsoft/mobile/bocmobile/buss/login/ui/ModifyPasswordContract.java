package com.boc.bocsoft.mobile.bocmobile.buss.login.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.login.model.ModifyPasswordViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by feib on 16/7/14.
 * 登录修改密码接口类
 */
public class ModifyPasswordContract {

    public interface View extends BaseView<Presenter> {
        /**查询随机数成功回调*/
        ModifyPasswordViewModel randomSuccess(String random);
        /**修改密码成功回调*/
        void namePasswordModMobileSuccess();
        /**退出成功回调*/
        void logoutSuccess();
    }

    public interface Presenter extends BasePresenter {
        /**修改密码*/
        void queryNamePasswordModMobile();
        /**退出*/
        void queryLogout();
    }
}
