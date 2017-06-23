package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.model.SecurityViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model.QRPayChangePwdViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model.QRPayResetPwdViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model.QRPaySetPwdViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * 密码相关
 * Created by wangf on 2016/8/31.
 */
public class QRPayPwdContract {

    public interface QrPaySecurityFactorView{
        /*** 查询安全因子成功 */
        void loadPwdSecurityFactorSuccess(SecurityViewModel securityViewModel);

        /*** 查询安全因子失败 */
        void loadPwdSecurityFactorFail(BiiResultErrorException biiResultErrorException);
    }

    public interface QrPaySetPwdView {
        /*** 设置支付密码预交易成功 */
        void loadQRPaySetPassPreSuccess(QRPaySetPwdViewModel payPwdPassPreViewModel);

        /*** 设置支付密码预交易失败 */
        void loadQRPaySetPassPreFail(BiiResultErrorException biiResultErrorException);

        /*** 设置支付密码成功 */
        void loadQRPaySetPassSuccess();

        /*** 设置支付密码失败*/
        void loadQRPaySetPassFail(BiiResultErrorException biiResultErrorException);

    }

    public interface QrPayChangePwdView {
//        /*** 修改支付密码预交易成功 */
//        void loadQRPayChangePassPreSuccess();
//
//        /*** 修改支付密码预交易失败 */
//        void loadQRPayChangePassPreFail(BiiResultErrorException biiResultErrorException);

        /*** 修改支付密码成功 */
        void loadQRPayChangePassSuccess();

        /*** 修改支付密码失败*/
        void loadQRPayChangePassFail(BiiResultErrorException biiResultErrorException);

    }


    public interface QrPayResetPwdView {
        /*** 重置支付密码预交易成功 */
        void loadQRPayResetPassPreSuccess(QRPayResetPwdViewModel resetPwdViewModel);

        /*** 重置支付密码预交易失败 */
        void loadQRPayResetPassPreFail(BiiResultErrorException biiResultErrorException);

        /*** 重置支付密码成功 */
        void loadQRPayResetPassSuccess();

        /*** 重置支付密码失败*/
        void loadQRPayResetPassFail(BiiResultErrorException biiResultErrorException);

    }

    public interface QrPayPwdPresenter extends BasePresenter {

        /*** 查询安全因子 */
        void loadPwdSecurityFactor();

        /*** 设置支付密码预交易 */
        void loadQRPaySetPassPre(QRPaySetPwdViewModel pwdViewModel);

        /*** 设置支付密码 */
        void loadQRPaySetPass(QRPaySetPwdViewModel payPwdViewModel);

        /*** 修改支付密码预交易 */
//        void loadQRPayChangePassPre(QRPayChangePwdViewModel pwdViewModel);

        /*** 修改支付密码 */
        void loadQRPayChangePass(QRPayChangePwdViewModel pwdViewModel);

        /*** 重置支付密码预交易 */
        void loadQRPayResetPassPre(QRPayResetPwdViewModel resetPwdViewModel);

        /*** 重置支付密码 */
        void loadQRPayResetPass(QRPayResetPwdViewModel resetPwdViewModel);
    }
}
