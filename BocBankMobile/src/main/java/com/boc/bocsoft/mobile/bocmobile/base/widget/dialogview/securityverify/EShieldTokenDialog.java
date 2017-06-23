package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify;

import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.eshield.PESACaPasswordEditText;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.eshield.PassKeybroadPop;

/**
 * Created by wangtong on 2016/5/28.
 */
public class EShieldTokenDialog extends SecurityVerifyDialog implements PassKeybroadPop.InputCompleteListener {

    private PESACaPasswordEditText pesaCaPasswordEditText;
    private boolean isPasswordComplete = false;

    public EShieldTokenDialog(Activity activity) {
        super(activity);
    }

    @Override
    protected View onAddContentView() {
        rootView = inflater.inflate(R.layout.boc_etoken_input, null);
        return rootView;
    }

    @Override
    protected void initView() {
        pesaCaPasswordEditText = (PESACaPasswordEditText) rootView.findViewById(R.id.etoken_pesac);
    }

    @Override
    protected void initData() {
        super.initData();
        pesaCaPasswordEditText.setSecurityKeyboardListener(this);
    }

    @Override
    protected void setListener() {
        super.setListener();
        pesaCaPasswordEditText.setInputCompleteListener(this);
        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                EShieldVerify.notifyThread();
            }
        });
    }

    public boolean isPasswordComplete() {
        return isPasswordComplete;
    }

    public String getEncryptText(String randomId, String sessionId) {
        return pesaCaPasswordEditText.getEncryptText(randomId, sessionId);
    }

    @Override
    public void onInputComplete() {
        isPasswordComplete = true;
        cancel();
    }

    @Override
    public void textChanged(String value) {
        pesaCaPasswordEditText.onInputPassword(value);
    }

    @Override
    public void show() {
        super.show();
        pesaCaPasswordEditText.showKeyBroad();
    }
}
