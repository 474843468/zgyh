package com.boc.bocsoft.mobile.bocmobile.buss.login.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.login.model.LoginViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * Created by feibin on 2016/5/21.
 * 登录接口类
 */
public class LoginContract{

    public interface View  {
        /**查询登录前随机数成功回调*/
        LoginViewModel randomPreSuccess(String random);
        /**登录失败回调*/
        void loginFail(BiiResultErrorException biiResultErrorException);
        /**登录成功回调*/
        void loginSuccess(LoginViewModel loginModel);
    }

    public interface Presenter extends BasePresenter {
        /**登录*/
        void queryLogin();
    }
}
