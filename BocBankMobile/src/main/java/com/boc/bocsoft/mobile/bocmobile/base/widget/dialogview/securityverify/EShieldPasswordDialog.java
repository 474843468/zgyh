package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify;

import android.app.Activity;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.eshield.PESAPModifyPassEdittext;

/**
 * Created by wangtong on 2016/6/1.
 */
public class EShieldPasswordDialog extends SecurityVerifyDialog {

    private PESAPModifyPassEdittext newPassword;
    private PESAPModifyPassEdittext againPassword;
    private boolean isChangePassowrd = false;

    public EShieldPasswordDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected View onAddContentView() {
        rootView = inflater.inflate(R.layout.boc_eshield_password_change, null);
        return rootView;
    }

    @Override
    protected void initView() {
        newPassword = (PESAPModifyPassEdittext) rootView.findViewById(R.id.psd_new);
        againPassword = (PESAPModifyPassEdittext) rootView.findViewById(R.id.psd_ok);
    }

    @Override
    protected void initData() {
        super.initData();
        newPassword.setSecurityKeyboardListener(this);
        againPassword.setSecurityKeyboardListener(this);
    }

    @Override
    protected void setListener() {
        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                EShieldVerify.notifyThread();
            }
        });
        findViewById(R.id.btn_dialog_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInputs();
            }
        });

        findViewById(R.id.btn_dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChangePassowrd = false;
                cancel();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        newPassword.showKeyBroad();
    }

    public void checkInputs() {
        String newPass = newPassword.getInputText();
        String againPass = againPassword.getInputText();
        if (!TextUtils.isEmpty(newPass) && !TextUtils.isEmpty(againPass)) {
            if (!newPassword.isRegex(newPass)) {
                Toast.makeText(activity, activity.getString(R.string.boc_tips_ca_modify_pwdnew_error), Toast.LENGTH_SHORT).show();
            } else if (!againPassword.isRegex(againPass)) {
                Toast.makeText(activity, activity.getString(R.string.boc_tips_ca_modify_pwdold_error), Toast.LENGTH_SHORT).show();
            } else if (newPass.equals(againPass)) {
                isChangePassowrd = true;
                cancel();
            } else {
                Toast.makeText(activity, activity.getString(R.string.boc_tips_ca_modify_pwd_not_same), Toast.LENGTH_SHORT).show();
            }
        } else if (TextUtils.isEmpty(newPass)) {
            Toast.makeText(activity, activity.getString(R.string.boc_tips_ca_input_newpad_err), Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(againPass)) {
            Toast.makeText(activity, activity.getString(R.string.boc_tips_ca_input_oldpsd_err), Toast.LENGTH_SHORT).show();
        }

    }

    public boolean isChangePassowrd() {
        return isChangePassowrd;
    }

    public void setChangePassowrd(boolean changePassowrd) {
        isChangePassowrd = changePassowrd;
    }

    public String getEncryptPassword(String random, String sessionId) {
        return newPassword.getEncryptText(random, sessionId);
    }

}
