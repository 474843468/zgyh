package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.ui;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.BaseDialog;

/**
 * Created by wangtong on 2016/9/18.
 */
public class EditDialog extends BaseDialog {

    protected EditText editContent;
    protected Button btnLeft;
    protected Button btnRight;
    private View rootView;

    public EditDialog(Context context) {
        super(context);
    }

    @Override
    protected View onAddContentView() {
        rootView = View.inflate(mContext, R.layout.boc_purchase_edit_dialog, null);
        return rootView;
    }

    @Override
    protected void initView() {
        editContent = (EditText) rootView.findViewById(R.id.edit_content);
        btnLeft = (Button) rootView.findViewById(R.id.btn_left);
        btnRight = (Button) rootView.findViewById(R.id.btn_right);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setRightButtonListener(View.OnClickListener listener) {
        btnRight.setOnClickListener(listener);
    }

    public void setInputText(String content) {
        editContent.setText(content);
        editContent.setSelection(content.length());
    }

    public void showDialog() {
        show();
    }

    public String getInputText() {
        return editContent.getText().toString();
    }
}
