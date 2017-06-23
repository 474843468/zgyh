package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.eshield;

import android.app.Activity;
import android.text.TextUtils;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldTokenDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.device.key.KeyConst;
import com.boc.keydriverinterface.MEBKeyDriverCommonModel;
import com.boc.keydriverinterface.MEBKeyDriverInterface;

/**
 * Created by wangtong on 2016/7/6.
 */
public class InputPassword implements EShieldVerify.Command {

    private boolean isCommandSucceed = true;

    private Activity activity;

    private EShieldTokenDialog dialog;

    public InputPassword(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean execute() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog = new EShieldTokenDialog(activity);
                dialog.show();
            }
        });

        try {
            EShieldVerify.waitThread();
        } catch (InterruptedException e) {
            e.printStackTrace();
            isCommandSucceed = false;
        }

        if (dialog.isPasswordComplete()) {
            startSign();
        }

        return isCommandSucceed;
    }

    /**
     * 开始签名
     */
    public void startSign() {
        String mRandomId = MEBKeyDriverInterface.random() + "";
        String mSessionId = "1234567";

        String mPin = dialog.getEncryptText(mRandomId, mSessionId);

        /**
         * 校验弱pin
         */
        if (mPin == null || mPin.trim().length() == 0) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    EShieldVerify.getInstance(activity).showErrorDialog(activity.getString(R.string.boc_tips_ca_modify_pwdold_error));
                }
            });
            isCommandSucceed = false;
            return;
        }

        String plainData = EShieldVerify.getInstance(activity).getmPlainData();
        if (!TextUtils.isEmpty(plainData)) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    EShieldVerify.getInstance(activity).showLoadingDialog();
                }
            });
            connectUSBKey(mRandomId, mSessionId, mPin, plainData);
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    EShieldVerify.getInstance(activity).showErrorDialog(activity.getString(R.string.boc_ca_sign_cancel));
                }
            });
        }
    }

    /**
     * 连接音频key进行签名
     *
     * @param random     随机数
     * @param session    session
     * @param pin        音频key密码
     * @param _plainData 加密数据
     */
    private void connectUSBKey(final String random, final String session,
                               final String pin, final String _plainData) {

        MEBKeyDriverInterface driver = EShieldVerify.getInstance(activity).getKeyDriverInterface();
        if (driver != null) {
            final MEBKeyDriverCommonModel model = driver.tradeSign(activity, _plainData, pin, random, session,
                    EShieldVerify.getInstance(activity).getmBOCCallBack());

            // 判断连接状态
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (model.mError.getErrorId() != KeyConst.BOC_SUCCESS) {
                        // 连接不正常
                        EShieldVerify.getInstance(activity).showErrorDialog(model.mError.getErrorMessage());
                        isCommandSucceed = false;
                    }
                    EShieldVerify.getInstance(activity).closeProgressDialog();
                }
            });


        } else {
            isCommandSucceed = false;
        }
    }

    @Override
    public void stop() {
        isCommandSucceed = false;
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }
}
