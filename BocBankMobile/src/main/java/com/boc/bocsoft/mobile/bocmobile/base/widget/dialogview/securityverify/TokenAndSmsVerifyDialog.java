package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * @Description:短信和动态口令选择框
 * @author: wangtong
 * @time: 2016/5/26
 */
public class TokenAndSmsVerifyDialog extends SecurityVerifyDialog implements SmsVerifyView.SmsActionListener,
        CFCAEditTextView.SecurityEditCompleteListener {

    protected TextView lebelMessage;
    private CFCAEditTextView cfcaTokenText;
    private CFCAEditTextView cfcaSmsText;

    private String phoneNumber;
    private SmsVerifyView tryAgain;

    public TokenAndSmsVerifyDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected View onAddContentView() {
        rootView = inflater.inflate(R.layout.boc_token_sms_dialog, null);
        return rootView;
    }

    @Override
    protected void initView() {
        cfcaTokenText = (CFCAEditTextView) rootView.findViewById(R.id.etoken_cfca);
        cfcaSmsText = (CFCAEditTextView) rootView.findViewById(R.id.sms_cfca);
        cfcaTokenText.setSipBoxIsInDialogNeedReLocation(true);
        cfcaSmsText.setSipBoxIsInDialogNeedReLocation(true);
        tryAgain = (SmsVerifyView) rootView.findViewById(R.id.security_retry);
        lebelMessage = (TextView) rootView.findViewById(R.id.lebel_message);
    }

    @Override
    protected void initData() {
        //P606 取电话号码
        phoneNumber = ApplicationContext.getInstance().getUser().getMobile();
        //P605 取电话号码
        //phoneNumber = SpUtils.getSpString(getContext(), EloanConst.LOAN_MOBILE, null);
        tryAgain.setOnSmsActionListener(this);
        tryAgain.startCountDown();
        if (phoneNumber.length() != 11) {
            Toast.makeText(mContext, "手机号格式不正确", Toast.LENGTH_SHORT).show();
        }
        cfcaTokenText.setCFCARandom(SecurityVerity.getInstance(activity).getRandomNum());
        cfcaSmsText.setCFCARandom(SecurityVerity.getInstance(activity).getRandomNum());
        cfcaSmsText.setSecurityKeyboardListener(this);
        cfcaTokenText.setSecurityKeyboardListener(this);
        cfcaSmsText.setSecurityEditCompleListener(this);
        cfcaTokenText.setSecurityEditCompleListener(this);
    }

    @Override
    protected void setListener() {
        super.setListener();

        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                tryAgain.stopCountDown();
            }
        });
    }

    @Override
    protected void hiddenSecurityKeyboard() {
        cfcaTokenText.hiddenCfcaKeybard();
        cfcaSmsText.hiddenCfcaKeybard();
    }

    @Override
    protected boolean getEncryptResults() {
        if (cfcaTokenText.getInputStatus() == CFCAEditTextView.INPUT_STATUS_NULL) {
            Toast.makeText(activity, R.string.security_verify_token_null, Toast.LENGTH_SHORT).show();
            return false;
        } else if (cfcaTokenText.getInputStatus() == CFCAEditTextView.INPUT_STATUS_NOT_COMPLETE) {
            Toast.makeText(activity, R.string.security_verify_token_failed, Toast.LENGTH_SHORT).show();
            return false;
        } else if (cfcaSmsText.getInputStatus() == CFCAEditTextView.INPUT_STATUS_NULL) {
            Toast.makeText(activity, R.string.security_verify_sms_null, Toast.LENGTH_SHORT).show();
            return false;
        } else if (cfcaSmsText.getInputStatus() == CFCAEditTextView.INPUT_STATUS_NOT_COMPLETE) {
            Toast.makeText(activity, R.string.security_verify_sms_failed, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            encryptRandomNums = new String[]{cfcaTokenText.getEncryptRandomNum(), cfcaSmsText.getEncryptRandomNum()};
            encryptPasswords = new String[]{cfcaTokenText.getEncryptPassword(), cfcaSmsText.getEncryptPassword()};
            return true;
        }
    }

    @Override
    public void sendSmsResult(boolean isSucceed) {
        if (isSucceed) {
            if (phoneNumber.length() == 11) {
                String text = getContext().getString(R.string.security_send_tip_two2, phoneNumber.substring(7, 11));
                lebelMessage.setText(text);
            } else {
                lebelMessage.setText(phoneNumber);
            }

        } else {
            tryAgain.stopCountDown();
        }
    }

    @Override
    public void sendSms() {
        lebelMessage.setText(getContext().getString(R.string.security_message_code));
        SecurityVerity.getInstance().psnSendSMSCodeToMobile();
    }

    @Override
    public void onSmsReceived(String code) {
        cfcaSmsText.setText(code);
    }

    @Override
    public void onNumCompleted(String random, String num, String mVersion) {

    }

    @Override
    public void onErrorMessage(boolean isShow) {

    }

    @Override
    public void onCompleteClicked(String inputString) {
        inputCompleted();
    }
}
