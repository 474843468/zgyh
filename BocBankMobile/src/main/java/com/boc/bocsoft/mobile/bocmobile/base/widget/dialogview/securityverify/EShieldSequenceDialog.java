package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.BaseDialog;

/**
 * Created by wangtong on 2016/5/27.
 */
public class EShieldSequenceDialog extends BaseDialog {

    private View contentView;

    private EditText sequence;

    public EShieldSequenceDialog(Context context) {
        super(context);
    }

    @Override
    protected View onAddContentView() {
        contentView = inflater.inflate(R.layout.boc_eshield_sequence, null);
        return contentView;
    }

    @Override
    protected void initView() {
        sequence = (EditText) contentView.findViewById(R.id.sequence);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                EShieldVerify.notifyThread();
            }
        });

        contentView.findViewById(R.id.btn_dialog_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        contentView.findViewById(R.id.btn_dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    public String getSequence() {
        return sequence.getText().toString();
    }
}
