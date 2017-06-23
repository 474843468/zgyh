package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bocmobile.R;

import cfca.mobile.sip.SipBox;

/**
 * Created by wangtong on 2016/6/2.
 */
public class CFCAInputView extends LinearLayout {

    private View contentView;
    private SipBox sipBox;
    private EditText editText;

    private StringBuilder contentText;

    public CFCAInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWidget();
    }

    private void initWidget() {
        contentView = inflate(getContext(), R.layout.boc_cfca_input, null);
        sipBox = (SipBox) contentView.findViewById(R.id.input_security);
        editText = (EditText) contentView.findViewById(R.id.input_message);
        addView(contentView);

        contentText = new StringBuilder();
        initSipBox();
    }

    /**
     *  设置CFCA监听
     */
    private void initSipBox() {
        sipBox.setMaxEms(6);
        sipBox.setOutputValueType(2);
        sipBox.setPasswordMinLength(6);
        sipBox.setPasswordMaxLength(6);
        sipBox.setRandomKey_S(SecurityVerity.getInstance().getRandomNum());
        sipBox.setKeyBoardType(SipBox.NUMBER_KEYBOARD);
        sipBox.addTextChangedListener(textWatcher);
        sipBox.setCursorVisible(false);
        sipBox.onClickDone();
        sipBox.requestFocus();
        sipBox.setFocusableInTouchMode(true);
        sipBox.setFocusable(true);
        sipBox.performClick();
        hiddenKeyboard(getContext(), sipBox);

    }

    TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String str = s.toString();
            if (str.length() == 0) {
                contentText.deleteCharAt(0);
                editText.setText("");
            } else {
                char end = str.charAt(str.length() - 1);
                if (end != '•') {
                    contentText.append(end);
                } else if (str.length() < contentText.length()) {
                    contentText.deleteCharAt(contentText.length() - 1);
                }
                editText.setText(contentText.substring(0, str.length()));
            }
        }
    };

    /**
     * @Description:设置随机数
     */
    public void setCFCARandom(String mRandom) {
        if (mRandom != null && !"".equalsIgnoreCase(mRandom)) {
            sipBox.setRandomKey_S(mRandom);
        }
    }

    /**
     * @Description:获取密文随机数
     */
    public String getEncryptRandomNum() {
        String ret = "";
        try {
            ret = sipBox.getValue().getEncryptRandomNum();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * @Description:获取密文
     */
    public String getEncryptPassword() {
        String ret = "";
        try {
            ret = sipBox.getValue().getEncryptPassword();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public void setText(String text) {
        if (contentText.length() > 0) {
            contentText.delete(0, contentText.length());
        }
        contentText = contentText.append(text);
        editText.setText(text);
    }

    public String getText(){
        return contentText.toString();
    }

    public void hiddenCfcaKeybard() {
        sipBox.hideSecurityKeyBoard();
    }

    public void hiddenKeyboard(Context mContext, View view) {
        if (view == null || view.getWindowToken() == null || mContext == null)
            return;
        InputMethodManager manger = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manger == null) {
            return;
        }
        manger.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}






















