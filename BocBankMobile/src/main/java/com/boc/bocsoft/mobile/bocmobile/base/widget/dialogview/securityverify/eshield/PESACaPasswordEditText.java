package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.eshield;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.CFCAEditTextView;
import com.boc.bocsoft.mobile.framework.ui.BaseFragment;
import com.boc.device.key.BOCMAKeyPinSecurity;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * 音频key密码输入键盘
 */
public class PESACaPasswordEditText extends LinearLayout  {

    private Context mContext;
    private boolean isKeyBroadShow;
    private PassKeybroadPop keyPop;

    private ArrayList<PESACaPassEditiItem> items;
    protected BaseFragment baseFragment;
    private PESACaPassEditiItem mItem;

    private String pinText = "";

    public PESACaPasswordEditText(Context context) {
        super(context);
        init(context);
    }

    public PESACaPasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PESACaPasswordEditText(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public String getPinValue() {
        return keyPop.getContentText();
    }

    public void clear() {
        pinText = "";
        keyPop.clear();
    }

    public void init(Context context) {
        mContext = context;
        items = new ArrayList<PESACaPassEditiItem>();
        View item = View.inflate(mContext, R.layout.pesap_ca_input_item_view, null);
        mItem = (PESACaPassEditiItem) item.findViewById(R.id.tv_ca_input_item);
        addView(item);

        keyPop = new PassKeybroadPop(context);

        this.setLongClickable(false);

        this.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                showKeyBroad();
            }
        });

    }

    public void setInputCompleteListener(PassKeybroadPop.InputCompleteListener inputCompleteListener) {
        keyPop.setInputCompleteListener(inputCompleteListener);
    }

    public void setSecurityKeyboardListener(CFCAEditTextView.SecurityKeyboardListener securityKeyboardListener) {
        keyPop.setSecurityKeyboardListener(securityKeyboardListener);
    }

    /**
     * 弹出数字键盘
     */
    public void showKeyBroad() {
        keyPop.showKeyBroad();
    }

    public void onInputPassword(String value) {
        if (value == null) {
            value = "";
        }
        int count = value.length();
        mItem.drawCircle(count);
    }

    public String getEncryptText(String mRandomId, String mSessionId) {
        pinText = keyPop.getContentText();

        if (isRegex(pinText)) {
            return new String(BOCMAKeyPinSecurity.encryptWithKeyPin(pinText, mRandomId, mSessionId));
        } else {
            return "";
        }
    }

    private boolean isRegex(String text) {
        if (text.length() != 8)
            return false;
        if (Pattern.matches("^[a-zA-Z]+$", text)) {
            return false;
        }
        if (Pattern.matches("^[0-9]+$", text)) {
            return false;
        }
        return true;
    }

    /**
     * 设置当前 fragment实例 主要用户显示提示和loading
     *
     * @param baseFragment
     */
    public void setFragment(BaseFragment baseFragment) {
        this.baseFragment = baseFragment;
    }

}
