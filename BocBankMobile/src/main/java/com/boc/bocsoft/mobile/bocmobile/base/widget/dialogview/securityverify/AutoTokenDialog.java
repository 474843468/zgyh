package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.R;


/**
 * Created by wangtong on 2016/5/25.
 */
public class AutoTokenDialog extends SecurityVerifyDialog implements CFCAEditTextView.SecurityEditCompleteListener {

    //输入的动态口令
    private String inputToken;
    //CFCA键盘
    private CFCAEditTextView cfcaEditText;

    public AutoTokenDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected View onAddContentView() {
        rootView = inflater.inflate(R.layout.boc_dialog_auto_token, null);
        return rootView;
    }

    @Override
    protected void initView() {
        cfcaEditText = (CFCAEditTextView) rootView.findViewById(R.id.etoken_cfca);
    }

    @Override
    protected void initData() {
        cfcaEditText.setSipBoxIsInDialogNeedReLocation(true);
        cfcaEditText.setSecurityEditCompleListener(this);
        cfcaEditText.setCFCARandom(SecurityVerity.getInstance(activity).getRandomNum());
        cfcaEditText.setSecurityKeyboardListener(this);
    }

    public String getToken() {
        return inputToken;
    }

    @Override
    public void onNumCompleted(String random, String num, String mVersion) {
        inputToken = num;
        if (SecurityVerity.getInstance().verifyCodeResult != null) {
            String[] encryptRandom = new String[]{cfcaEditText.getEncryptRandomNum()};
            String[] encryptPassword = new String[]{cfcaEditText.getEncryptPassword()};
            String id = SecurityVerity.getInstance().getCurrentSecurityVerifyTypeId();
            SecurityVerity.getInstance().verifyCodeResult.onEncryptDataReturned(id, encryptRandom, encryptPassword);
        }
        cancel();
    }

    @Override
    public void onErrorMessage(boolean isShow) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, R.string.security_verify_failed, Toast.LENGTH_SHORT).show();
                AutoTokenDialog.this.dismiss();
            }
        });
    }

    @Override
    public void onCompleteClicked(final String inputString) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(inputString)) {
                    Toast.makeText(activity, R.string.security_verify_token_null, Toast.LENGTH_SHORT).show();
                } else if (inputString.length() < 6) {
                    Toast.makeText(activity, R.string.security_verify_token_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void show() {
        super.show();
        cfcaEditText.showCFCAKeyPop();
    }

    protected void hiddenSecurityKeyboard() {
        cfcaEditText.hiddenCfcaKeybard();
    }

}
