package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * 确认对话框
 */
public class ConfirmDialog extends BaseDialog {
    /****
     * 错误提示对话框
     */
    private View contentView;
    private TextView tvTitle;
    private Button btLeft;
    private Button btRight;
    private TextView tvMsg;

    public ConfirmDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected View onAddContentView() {
        contentView = inflateView(R.layout.boc_dialog_warn);
        return contentView;
    }

    @Override
    protected void initView() {
        tvTitle = (TextView) contentView.findViewById(R.id.tv_title);
        tvMsg = (TextView) contentView
                .findViewById(R.id.tv_msg);
        btLeft = (Button) contentView
                .findViewById(R.id.bt_left);
        btRight = (Button) contentView
            .findViewById(R.id.bt_right);

        setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
    }

    @Override
    protected void initData() {
        // 关闭按钮是否显示
        // showCloseOutside(false);
    }

    @Override
    protected void setListener() {

        btLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                cancel();
                if (mListener != null) {
                    mListener.onLeftClick(ConfirmDialog.this);
                }
            }
        });


        btRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                cancel();
                if (mListener != null) {
                    mListener.onRightClick(ConfirmDialog.this);
                }
            }
        });
    }


    /**
     * 设置提示信息
     *
     * @param msg
     */
    public ConfirmDialog setMessage(String msg) {
        // 内容
        tvMsg.setText(msg);
        return this;
    }

    /**
     * 设置左侧按钮文字
     *
     * @param btnText
     */
    public ConfirmDialog setLeftButton(String btnText) {
        // 按钮文字
        btLeft.setText(btnText);
        return this;
    }

    /**
     * 设置右侧按钮文字
     *
     * @param btnText
     */
    public ConfirmDialog setRightButton(String btnText) {
        // 按钮文字
        btRight.setText(btnText);
        return this;
    }


    private OnButtonClickInterface mListener;

    /**
     * 底部按钮view的回调接口监听方法实现
     */
    public void setButtonClickInterface(OnButtonClickInterface mListener) {
        this.mListener = mListener;
    }

    /**
     * 点击事件回调接口监听
     */
    public interface  OnButtonClickInterface{
         void onLeftClick(ConfirmDialog warnDialog);
         void onRightClick(ConfirmDialog warnDialog);

    }

}