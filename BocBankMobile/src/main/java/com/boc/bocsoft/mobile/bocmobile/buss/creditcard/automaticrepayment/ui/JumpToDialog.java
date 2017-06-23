package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.automaticrepayment.ui;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.BaseDialog;

/**
 * Name: liukai
 * Time：2016/11/22 14:12.
 * Created by lk7066 on 2016/11/22.
 * It's used to 协议弹框，因为没有这个样式，所以做了一份
 */

public class JumpToDialog extends BaseDialog {

    private View contentView;
    public Button enterBtn;
    private TextView contentTv;

    public JumpToDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected View onAddContentView() {
        contentView = inflateView(R.layout.dialog_jump_to);
        return contentView;
    }

    @Override
    protected void initView() {
        contentTv = (TextView) contentView.findViewById(R.id.tv_dialog_jumpto_content);
        enterBtn = (Button) contentView.findViewById(R.id.btn_dialog_jump_to);
        setCanceledOnTouchOutside(true);// 设置点击屏幕Dialog消失
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setListener() {

        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                cancel();

            }
        });
    }


    /**
     * 设置提示信息
     *
     * @param msg
     */
    public JumpToDialog setJumpToData(String msg) {
        // 内容
        contentTv.setText(msg);
        return this;
    }

    /**
     * 设置按钮文字
     *
     * @param btnText
     */
    public JumpToDialog setBtnText(String btnText) {
        // 按钮文字
        enterBtn.setText(btnText);
        return this;
    }

}
