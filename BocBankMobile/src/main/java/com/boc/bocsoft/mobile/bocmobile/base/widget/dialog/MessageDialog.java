package com.boc.bocsoft.mobile.bocmobile.base.widget.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.BaseDialog;

/**
 * 消息提示对话框
 * Created by wangtong on 2016/9/18.
 */
public class MessageDialog extends BaseDialog {

    protected TextView txtMessage;
    protected TextView txtHint;
    protected Button btnLeft;
    protected Button btnRight;
    private View rootView;

    private View.OnClickListener leftListener, rightListener;

    public MessageDialog(Context context) {
        super(context);
    }

    @Override
    protected View onAddContentView() {
        rootView = View.inflate(mContext, R.layout.boc_message_dialog, null);
        return rootView;
    }

    @Override
    protected void initView() {
        txtMessage = (TextView) rootView.findViewById(R.id.message);
        txtHint = (TextView) rootView.findViewById(R.id.message_hint);
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
                if (leftListener == null)
                    dismiss();
                else
                    leftListener.onClick(v);
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightListener == null)
                    dismiss();
                else
                    rightListener.onClick(v);
            }
        });
    }

    public void setLeftButtonClickListener(View.OnClickListener listener) {
        this.leftListener = listener;
    }

    public void setRightButtonClickListener(View.OnClickListener listener) {
        this.rightListener = listener;
    }

    /**
     * 设置左边按钮文字
     *
     * @param text
     */
    public void setLeftButtonText(String text) {
        btnLeft.setText(text);
    }

    /**
     * 设置右边按钮文字
     *
     * @param text
     */
    public void setRightButtonText(String text) {
        btnRight.setText(text);
    }

    /**
     * 设置弹窗的文本
     *
     * @param text
     */
    public void showDialog(String text) {
        txtMessage.setText(text);
        show();
    }

    /**
     * 设置弹窗的文本
     *
     * @param message
     * @param messageHint
     */
    public void showDialog(String message, CharSequence messageHint) {
        txtHint.setVisibility(View.VISIBLE);
        txtMessage.setText(message);
        txtHint.setText(messageHint);
        show();
    }
}
