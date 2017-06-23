package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify;

import android.content.Context;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.BaseDialog;

/**
 * Created by wangtong on 2016/5/27.
 */
public class EShieldTipDialog extends BaseDialog {

    private View contentView;

    public EShieldTipDialog(Context context) {
        super(context);
    }

    @Override
    protected View onAddContentView() {
        contentView = inflater.inflate(R.layout.boc_eshield_tip_dialog, null);
        return contentView;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        contentView.findViewById(R.id.btn_dialog_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }
}
