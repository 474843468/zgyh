package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify;

import android.app.Activity;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * @Description: 短信验证码认证
 * @author: WangTong
 * @time: 2016/5/23
 */
public class SmsVerifyDialog extends SecurityVerifyDialog implements SmsVerifyView.SmsActionListener
        , CFCAEditTextView.SecurityEditCompleteListener {

    private TextView phoneText;

    private CFCAEditTextView cfcaEditText;
    private String phoneNumber;
    // 获取手机验证码按钮
    protected SmsVerifyView btnVerifyCode;

    public SmsVerifyDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected View onAddContentView() {
        rootView = inflater.inflate(R.layout.boc_dialog_sms_verify, null);
        return rootView;
    }

    @Override
    protected void initView() {
        cfcaEditText = (CFCAEditTextView) rootView.findViewById(R.id.etoken_cfca);
        cfcaEditText.setSipBoxIsInDialogNeedReLocation(true);
        phoneText = (TextView) rootView.findViewById(R.id.security_phone);
        btnVerifyCode = (SmsVerifyView) rootView.findViewById(R.id.security_retry);
    }

    @Override
    protected void initData() {
        phoneNumber = ApplicationContext.getInstance().getUser().getMobile();
        //phoneNumber = SpUtils.getSpString(getContext(), EloanConst.LOAN_MOBILE, null);
        btnVerifyCode.setOnSmsActionListener(this);
        btnVerifyCode.startCountDown();
        if (phoneNumber.length() == 11) {
            String text = getContext().getString(R.string.security_send_tip_two, phoneNumber.substring(7, 11));
            phoneText.setText(text);
        } else {
            phoneText.setText(phoneNumber);
        }

        cfcaEditText.setSecurityEditCompleListener(this);
        cfcaEditText.setCFCARandom(SecurityVerity.getInstance(activity).getRandomNum());
        cfcaEditText.setSecurityKeyboardListener(this);
    }

    @Override
    protected void setListener() {
        super.setListener();

        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                btnVerifyCode.stopCountDown();
            }
        });
    }

    @Override
    protected void hiddenSecurityKeyboard() {
        cfcaEditText.hiddenCfcaKeybard();
    }

    @Override
    public void sendSms() {
        phoneText.setVisibility(View.INVISIBLE);
        SecurityVerity.getInstance(activity).psnSendSMSCodeToMobile();
    }

    @Override
    public void onSmsReceived(String code) {
        cfcaEditText.setText(code);
    }

    @Override
    public void sendSmsResult(boolean isSucceed) {
        if (isSucceed) {
            phoneText.setVisibility(View.VISIBLE);
        } else {
            btnVerifyCode.stopCountDown();
        }
    }

    @Override
    public void onNumCompleted(String random, String num, String mVersion) {
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
                SmsVerifyDialog.this.cancel();
            }
        });
    }

    @Override
    public void onCompleteClicked(final String inputString) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(inputString)) {
                    Toast.makeText(activity, R.string.security_verify_sms_null, Toast.LENGTH_SHORT).show();
                } else if (inputString.length() < 6) {
                    Toast.makeText(activity, R.string.security_verify_sms_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void show() {
        super.show();
        cfcaEditText.showCFCAKeyPop();
    }
}
