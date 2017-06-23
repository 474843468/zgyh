package com.boc.bocsoft.mobile.bocmobile.buss.login.ui;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.PsnSvrRegisterDevicePreResult;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDeviceSubmit.PsnSvrRegisterDeviceSubmitParams;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * Created by feibin on 16/8/15.
 */
public class BindingDeviceContrct {
    public interface View{
        /**获取安全因子成功回调*/
        void securityFactorSuccess(PsnGetSecurityFactorResult psnGetSecurityFactorResult);
        /**设备注册预交易成功回调*/
        void svrRegisterDevicePreSuccess(PsnSvrRegisterDevicePreResult psnSvrRegisterDevicePreResult);
        /**获取随机数成功回调*/
        void randomSuccess(String random);
        /**设备注册提交交易成功回调*/
        void svrRegisterDeviceSubmitSuccess();
        /**设备注册提交交易失败回调*/
        void svrRegisterDeviceSubmitFail();
        /**退出成功回调*/
        void logoutSuccess();
    }
    public interface Presenter extends BasePresenter {
        /**获取安全因子*/
        void querySecurityFactor();
        /**获取随机数*/
        void queryRandom();
        /**设备注册预交易*/
        void queryPsnSvrRegisterDevicePre(CombinListBean mSecurityFactorResult);
        /**设备注册提交交易*/
        void queryPsnSvrRegisterDeviceSubmit(PsnSvrRegisterDeviceSubmitParams mParams);
        /**退出*/
        void queryLogout();
    }
}
