package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * Created by wangtong on 2016/8/16.
 */
public class PasswordDialog extends SecurityVerifyDialog implements CFCAEditTextView.SecurityEditCompleteListener {

    protected TextView title;
    private CFCAEditTextView cfcaEditText;
    private TextView passwordTip;
    private String passwordType;

    public PasswordDialog(Activity activity, String passwordType) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected View onAddContentView() {
        rootView = inflater.inflate(R.layout.boc_dialog_security_password, null);
        return rootView;
    }

    @Override
    protected void initView() {
        super.initView();
        cfcaEditText = (CFCAEditTextView) rootView.findViewById(R.id.etoken_cfca);
        cfcaEditText.setSipBoxIsInDialogNeedReLocation(true);
        passwordTip = (TextView) rootView.findViewById(R.id.password_tip);
        title = (TextView) rootView.findViewById(R.id.title);
    }

    @Override
    protected void initData() {
        super.initData();
        cfcaEditText.setSecurityEditCompleListener(this);
        cfcaEditText.setCFCARandom(SecurityVerity.getInstance(activity).getRandomNum());
        cfcaEditText.setSecurityKeyboardListener(this);
    }

    public void setPasswordType(String type) {
        passwordType = type;
        if (passwordType.equals("1") || passwordTip.equals("3")) {
            title.setText(getContext().getString(R.string.security_password_trans));
            passwordTip.setText(getContext().getString(R.string.security_password_trans_tip));
        } else if (passwordType.equals("2")) {
            title.setText(getContext().getString(R.string.security_password_query));
            passwordTip.setText(getContext().getString(R.string.security_password_query_tip));
        }
    }

    @Override
    protected void hiddenSecurityKeyboard() {
        cfcaEditText.hiddenCfcaKeybard();
    }

    @Override
    public void onNumCompleted(String random, String num, String mVersion) {
        if (SecurityVerity.getInstance().verifyCodeResult != null) {
            String[] encryptRandom = new String[]{cfcaEditText.getEncryptRandomNum()};
            String[] encryptPassword = new String[]{cfcaEditText.getEncryptPassword()};
            SecurityVerity.getInstance().verifyCodeResult.onEncryptDataReturned("-1", encryptRandom, encryptPassword);
        }
        cancel();
    }

    @Override
    public void onErrorMessage(boolean isShow) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, R.string.security_verify_failed, Toast.LENGTH_SHORT).show();
                PasswordDialog.this.cancel();
            }
        });
    }

    @Override
    public void onCompleteClicked(final String inputString) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (inputString.length() < 6) {
                    Toast.makeText(activity, R.string.security_verify_password_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
