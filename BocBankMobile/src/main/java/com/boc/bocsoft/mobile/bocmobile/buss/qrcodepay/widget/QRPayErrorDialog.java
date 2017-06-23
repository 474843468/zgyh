package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.widget;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.BaseDialog;


public class QRPayErrorDialog extends BaseDialog {
    /****
     * 错误提示对话框
     */
    private View contentView;
    private Button enterBtn;
    private TextView contentTv;

    public QRPayErrorDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected View onAddContentView() {
        contentView = inflateView(R.layout.qrpay_dialog_error);
        return contentView;
    }

    @Override
    protected void initView() {
        contentTv = (TextView) contentView
                .findViewById(R.id.tv_dialog_error_content);
        enterBtn = (Button) contentView
                .findViewById(R.id.btn_dialog_error_enter);
        setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
    }

    @Override
    protected void initData() {
        // 关闭按钮是否显示
        // showCloseOutside(false);
    }

    @Override
    protected void setListener() {

        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                cancel();
                if (mListener != null) {
                    mListener.onBottomViewClick();
                }
            }
        });
    }


    /**
     * 设置提示信息
     *
     * @param msg
     */
    public QRPayErrorDialog setErrorData(String msg) {
        // 内容
        contentTv.setText(msg);
        return this;
    }

    /**
     * 设置按钮文字
     *
     * @param btnText
     */
    public QRPayErrorDialog setBtnText(String btnText) {
        // 按钮文字
        enterBtn.setText(btnText);
        return this;
    }

    private OnBottomViewClickListener mListener;

    /**
     * 底部按钮view的回调接口监听方法实现
     */
    public void setOnBottomViewClickListener(
            OnBottomViewClickListener mListener) {
        this.mListener = mListener;
    }

    /**
     * 点击事件回调接口监听
     */
    public interface OnBottomViewClickListener {
        /**
         * 点击事件传递
         */
        abstract void onBottomViewClick();

    }

}