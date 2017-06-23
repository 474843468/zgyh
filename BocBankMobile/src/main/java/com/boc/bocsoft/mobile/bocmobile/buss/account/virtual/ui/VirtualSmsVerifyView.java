package com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.ui;

import android.content.Context;
import android.util.AttributeSet;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SmsVerifyView;

/**
 * @author wangyang
 *         16/9/18 10:48
 */
public class VirtualSmsVerifyView extends SmsVerifyView {

    public VirtualSmsVerifyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected String getButtonText() {
        return getContext().getString(R.string.boc_virtual_account_detail_button_password);
    }
}
