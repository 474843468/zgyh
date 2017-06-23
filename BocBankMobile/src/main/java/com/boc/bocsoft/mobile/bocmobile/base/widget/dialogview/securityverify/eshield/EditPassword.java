package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.eshield;

import android.app.Activity;

import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldPasswordDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.device.key.BOCMAKeyPinSecurity;
import com.boc.device.key.KeyConst;
import com.boc.keydriverinterface.MEBKeyDriverCommonModel;
import com.boc.keydriverinterface.MEBKeyDriverInterface;

/**
 * Created by wangtong on 2016/7/6.
 */
public class EditPassword implements EShieldVerify.Command {

    private static final String OLD_PIN = "88888888";
    private Activity activity;

    private boolean isCommandSucceed = true;

    private EShieldPasswordDialog dialog;

    public EditPassword(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean execute() {
        if (EShieldVerify.getInstance(activity).isNeedChangePassword()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog = new EShieldPasswordDialog(activity);
                    dialog.show();
                }
            });

            try {
                EShieldVerify.waitThread();
            } catch (InterruptedException e) {
                e.printStackTrace();
                isCommandSucceed = false;
            }

            if (dialog.isChangePassowrd()) {
                String mRandomId = MEBKeyDriverInterface.random() + "";
                String mSessionId = "1234567";
                String newPassword = dialog.getEncryptPassword(mRandomId, mSessionId);
                String oldPassword = new String(BOCMAKeyPinSecurity.encryptWithKeyPin(OLD_PIN, mRandomId, mSessionId));
                PESABOCCallBackAdapter callBack = EShieldVerify.getInstance(activity).getmBOCCallBack();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        EShieldVerify.getInstance(activity).showLoadingDialog();
                    }
                });
                MEBKeyDriverInterface driver = EShieldVerify.getInstance(activity).getKeyDriverInterface();
                if (driver != null) {
                    final MEBKeyDriverCommonModel model = driver.modifyPIN(activity, oldPassword, newPassword, mRandomId, mSessionId, callBack);
                    // 判断连接状态
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (model.mError.getErrorId() != KeyConst.BOC_SUCCESS) {
                                // 连接不正常
                                EShieldVerify.getInstance(activity).closeProgressDialog();
                                EShieldVerify.getInstance(activity).showErrorDialog(model.mError.getErrorMessage());
                                isCommandSucceed = false;
                            }
                        }
                    });
                } else {
                    isCommandSucceed = false;
                }
            } else {
                isCommandSucceed = false;
            }
        }
        return isCommandSucceed;
    }

    @Override
    public void stop() {
        isCommandSucceed = false;
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }
}
