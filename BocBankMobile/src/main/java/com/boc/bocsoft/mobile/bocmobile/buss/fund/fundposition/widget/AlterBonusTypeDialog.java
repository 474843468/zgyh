package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundposition.widget;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.BaseDialog;

/**
 * Created by pactera on 2016/12/21.
 */

public class AlterBonusTypeDialog extends BaseDialog{

    protected TextView fromBonus;
    protected TextView toBonus;
    protected Button btnLeft;
    protected Button btnRight;
    private View rootView;
    private View.OnClickListener leftListener, rightListener;

    public AlterBonusTypeDialog(Context context) {
        super(context);
    }

    @Override
    protected View onAddContentView() {
        rootView = View.inflate(mContext, R.layout.boc_alter_bonus_type_dialog, null);
        return rootView;
    }

    @Override
    protected void initView() {
        fromBonus = (TextView) findViewById(R.id.tv_from_bonus);
        toBonus = (TextView) findViewById(R.id.tv_to_bonus);
        btnLeft = (Button) findViewById(R.id.btn_left);
        btnRight = (Button) findViewById(R.id.btn_right);

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
    public void showDialog(String fromText,String toText) {
        fromBonus.setText(mContext.getString(R.string.boc_fund_position_from_bonus_type,fromText));
        toBonus.setText(mContext.getString(R.string.boc_fund_position_to_bonus_type,toText));
        show();
    }
}
