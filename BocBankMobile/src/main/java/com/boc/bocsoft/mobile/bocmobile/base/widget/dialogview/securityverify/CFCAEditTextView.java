package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import cfca.mobile.sip.BocSipBox;
import cfca.mobile.sip.CFCASipDelegator;
import cfca.mobile.sip.SipBox;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.cfca.mobile.log.CodeException;

import java.util.ArrayList;
import java.util.List;

/**
 * CFCA 安全键盘 E-Token 密码输入界面
 */
public class CFCAEditTextView extends LinearLayout implements CFCASipDelegator {

    public static final int INPUT_STATUS_NULL = 0;
    public static final int INPUT_STATUS_NOT_COMPLETE = 1;
    public static final int INPUT_STATUS_COMPLETE = 2;

    private BocSipBox mEditText;
    private List<TextView> editeViews;
    private View contentView;
    private LinearLayout editsContainer;
    private Context mContext;
    private boolean inPasswordputType;
    private int passwordLength;
    private String mLengthStr = "";
    private StringBuilder contentText;

    public SecurityEditCompleteListener securityEditCompleteListener;
    public SecurityKeyboardListener securityKeyboardListener;

    public interface SecurityEditCompleteListener {
        /**
         * @param random   加密后的随机数
         * @param num      加密后的口令
         * @param mVersion CFCA版本号
         */
        public void onNumCompleted(String random, String num, String mVersion);

        public void onErrorMessage(boolean isShow);

        /***
         * 点击完成报错信息
         */
        public void onCompleteClicked(String inputString);
    }

    public interface SecurityKeyboardListener {
        public void onKeyBoardShow(int keyBoardHeight);

        public void onKeyBoardHidden();
    }

    public CFCAEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.psdTextView);
        inPasswordputType = a.getBoolean(R.styleable.psdTextView_isPasswordAppear, true);
        passwordLength = a.getInteger(R.styleable.psdTextView_passwordLength, 0);
        a.recycle();
        if (passwordLength > 0) {
            initWidget();
        }
    }

    private void initWidget() {
        contentView = inflate(getContext(), R.layout.cfca_etoken_password_edit_text, null);
        editsContainer = (LinearLayout) contentView.findViewById(R.id.edit_group);
        mEditText = (BocSipBox) contentView.findViewById(R.id.sdk2_pwd_edit_simple);
        mEditText.setInputType(InputType.TYPE_NULL);
        mEditText.setRawInputType(InputType.TYPE_NULL);
        editeViews = new ArrayList<>();
        addEditText();
        addView(contentView);
        contentText = new StringBuilder();
        setViewInit();
    }

    private void addEditText() {
        for (int i = 0; i < passwordLength; i++) {
            TextView editText = null;
            if (inPasswordputType) {
                editText = (TextView) View.inflate(mContext, R.layout.boc_item_edittext_password, null);
            } else {
                editText = (TextView) View.inflate(mContext, R.layout.boc_item_edittext_text, null);
            }
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            params.weight = 1;
            params.setMargins(2, 2, 2, 2);
            params.gravity = Gravity.CENTER;
            editText.setLayoutParams(params);
            editText.setEnabled(false);
            editsContainer.addView(editText);
            editeViews.add(editText);

            if (i < passwordLength - 1) {
                View devider = new View(mContext);
                LayoutParams deviderParams = new LayoutParams(1, LayoutParams.MATCH_PARENT);
                devider.setLayoutParams(deviderParams);
                devider.setBackgroundColor(mContext.getResources().getColor(R.color.boc_text_color_gray));
                editsContainer.addView(devider);
            }
        }
    }

    /**
     *  设置CFCA监听
     */
    private void setViewInit() {
        final OnFocusChangeListener focusChangeListener = mEditText.getOnFocusChangeListener();
        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    contentView.setBackgroundResource(R.drawable.boc_bg_cfca_edit);
                } else {
                    contentView.setBackgroundResource(R.drawable.boc_button_bg_gray);
                }
                hiddenCfcaKeybard();
                if (focusChangeListener != null) {
                    focusChangeListener.onFocusChange(v, hasFocus);
                }
            }
        });
        mEditText.setMaxEms(passwordLength);
        mEditText.setOutputValueType(2);
        mEditText.setPasswordMinLength(passwordLength - 1);
        mEditText.setPasswordMaxLength(passwordLength);
        mEditText.setSipDelegator(this);
        mEditText.setKeyBoardType(SipBox.NUMBER_KEYBOARD);
        mEditText.addTextChangedListener(mTextWatcher);
        mEditText.setCursorVisible(false);
        mEditText.setFocusableInTouchMode(true);
        mEditText.setFocusable(true);
        mEditText.setClickable(true);
        mEditText.onClickDone();
        hiddenCfcaKeybard();
        hiddenKeyboard(getContext(), mEditText);

    }

    public void setOutputValueType(int type) {
        mEditText.setOutputValueType(type);
    }

    public void showCFCAKeyPop() {
        hiddenCfcaKeybard();
        hiddenKeyboard(getContext(), mEditText);
        mEditText.performClick();
    }


    TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            mLengthStr = s.toString();
            if (inPasswordputType) {
                updateEditePassTexts();
                if (mLengthStr.length() == passwordLength) {
                    char end = mLengthStr.charAt(mLengthStr.length() - 1);
                    if (end == '•') {
                        setTextValue();
                    }
                }
            } else {
                char end = 0;
                if (mLengthStr.length() == 0) {
                    contentText.deleteCharAt(0);
                    updateEditeNumTexts();
                } else {
                    end = mLengthStr.charAt(mLengthStr.length() - 1);
                    if (end != '•' && mLengthStr.length() > contentText.length()) {
                        contentText.append(end);
                    } else if (mLengthStr.length() < contentText.length()) {
                        contentText.deleteCharAt(contentText.length() - 1);
                    }
                    updateEditeNumTexts();
                }

                if (mLengthStr.length() == passwordLength && end == '•') {
                    setTextValue();
                }
            }
        }
    };

    private void updateEditePassTexts() {
        for (int i = 0; i < passwordLength; i++) {
            if (i < mLengthStr.length()) {
                editeViews.get(i).setText("1");
            } else {
                editeViews.get(i).setText("");
            }
        }
    }

    private void updateEditeNumTexts() {
        for (int i = 0; i < passwordLength; i++) {
            if (i < contentText.length()) {
                editeViews.get(i).setText(contentText.charAt(i) + "");
            } else {
                editeViews.get(i).setText("");
            }
        }
    }

    private void setTextValue() {
        if (securityEditCompleteListener != null) {
            try {
                String encryptRandomNum = mEditText.getValue().getEncryptRandomNum();
                String encryptPassword = mEditText.getValue().getEncryptPassword();
                securityEditCompleteListener.onNumCompleted(encryptRandomNum, encryptPassword, mEditText.getVersion() + "");
                hiddenCfcaKeybard();
            } catch (CodeException e) {
                securityEditCompleteListener.onErrorMessage(true);
                e.printStackTrace();
            }
        }
    }

    public String getEncryptRandomNum() {
        String encryptRandomNum = "";
        try {
            encryptRandomNum = mEditText.getValue().getEncryptRandomNum();
        } catch (CodeException e) {
            if (securityEditCompleteListener != null) {
                securityEditCompleteListener.onErrorMessage(true);
            }
            e.printStackTrace();
        }
        return encryptRandomNum;
    }

    public String getEncryptPassword() {
        String encryptPassword = "";
        try {
            encryptPassword = mEditText.getValue().getEncryptPassword();
        } catch (CodeException e) {
            if (securityEditCompleteListener != null) {
                securityEditCompleteListener.onErrorMessage(true);
            }
            e.printStackTrace();
        }
        return encryptPassword;
    }


    public void setSecurityEditCompleListener(SecurityEditCompleteListener lis) {
        securityEditCompleteListener = lis;
    }

    public void setSecurityKeyboardListener(SecurityKeyboardListener securityKeyboardListener) {
        this.securityKeyboardListener = securityKeyboardListener;
    }

    public String getText() {
        return contentText.toString();
    }

    public int getInputStatus() {
        if (TextUtils.isEmpty(mLengthStr)) {
            return INPUT_STATUS_NULL;
        } else if (mLengthStr.length() < passwordLength) {
            return INPUT_STATUS_NOT_COMPLETE;
        } else {
            return INPUT_STATUS_COMPLETE;
        }
    }

    public void setCFCARandom(String mRandom) {
        if (mRandom != null && !"".equalsIgnoreCase(mRandom)) {
            mEditText.setRandomKey_S(mRandom);
        }
    }

    @Override
    public void beforeKeyboardShow(SipBox var1, final int var2) {
        if (securityKeyboardListener != null) {
            securityKeyboardListener.onKeyBoardShow(var2);
        }
    }

    @Override
    public void afterKeyboardHidden(SipBox var1, int var2) {
        if (securityKeyboardListener != null) {
            securityKeyboardListener.onKeyBoardHidden();
        }
    }

    /**
     * @Description:点击键盘上的完成按钮会掉
     */
    @Override
    public void afterClickDown(SipBox var1) {
        if (securityEditCompleteListener != null) {
            securityEditCompleteListener.onCompleteClicked(mLengthStr);
        }
    }

    public void hiddenCfcaKeybard() {
        mEditText.hideSecurityKeyBoard();
    }

    private void hiddenKeyboard(Context mContext, View view) {
        if (view == null || view.getWindowToken() == null || mContext == null)
            return;
        InputMethodManager manger = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manger == null) {
            return;
        }
        manger.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void setText(String text) {
        if (contentText.length() > 0) {
            contentText.delete(0, contentText.length());
        }
        contentText = contentText.append(text);
        mEditText.setText(text);
    }

    public SipBox getCFCA() {
        return mEditText;
    }

    public void setSipBoxIsInDialogNeedReLocation(boolean is) {
        mEditText.setIsInDialogNeedRelocation(is);
    }
}

















