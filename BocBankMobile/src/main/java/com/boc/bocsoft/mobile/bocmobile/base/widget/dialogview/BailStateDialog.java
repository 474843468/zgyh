package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * Dialog：双向宝-账户管理-保证金转出对话框
 * Created by zhx on 2016/12/5
 */
public class BailStateDialog extends BaseDialog {
    private LinearLayout rootView;
    private TextView tv_title;
    private LinearLayout ll_bail_state_1;
    private TextView tv_amount_1;
    private ImageView iv_state_1;
    private LinearLayout ll_bail_state_2;
    private TextView tv_amount_2;
    private ImageView iv_state_2;
    private ImageView iv_bail_cancel_state;
    private TextView tv_notice;
    private TextView tv_ok;

    public BailStateDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected View onAddContentView() {
        rootView = (LinearLayout) View.inflate(mContext,
                R.layout.boc_dialog_bail_state, null);
        return rootView;
    }

    @Override
    protected void initView() {
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);

        ll_bail_state_1 = (LinearLayout) rootView.findViewById(R.id.ll_bail_state_1);
        tv_amount_1 = (TextView) rootView.findViewById(R.id.tv_amount_1);
        iv_state_1 = (ImageView) rootView.findViewById(R.id.iv_state_1);

        ll_bail_state_2 = (LinearLayout) rootView.findViewById(R.id.ll_bail_state_2);
        tv_amount_2 = (TextView) rootView.findViewById(R.id.tv_amount_2);
        iv_state_2 = (ImageView) rootView.findViewById(R.id.iv_state_2);

        iv_bail_cancel_state = (ImageView) rootView.findViewById(R.id.iv_bail_cancel_state);

        tv_notice = (TextView) rootView.findViewById(R.id.tv_notice);

        tv_ok = (TextView) rootView.findViewById(R.id.tv_ok);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        tv_title.setText(title);
    }

    /**
     * @param amount
     * @param state  0表示成功，1表示失败，2表示不存在
     */
    public void updateBailState1(String amount, int state) {
        switch (state) {
            case 0:
                tv_amount_1.setText(amount);
                iv_state_1.setImageResource(R.drawable.ic_success);
                break;
            case 1:
                tv_amount_1.setText(amount);
                iv_state_1.setImageResource(R.drawable.ic_fail);
                tv_notice.setVisibility(View.VISIBLE);
                break;
            case 2:
                ll_bail_state_1.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * @param amount
     * @param state  0表示成功，1表示失败，2表示不存在
     */
    public void updateBailState2(String amount, int state) {
        switch (state) {
            case 0:
                tv_amount_2.setText(amount);
                iv_state_2.setImageResource(R.drawable.ic_success);
            break;
            case 1:
                tv_amount_2.setText(amount);
                iv_state_2.setImageResource(R.drawable.ic_fail);
                tv_notice.setVisibility(View.VISIBLE);
                break;
            case 2:
                ll_bail_state_2.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * @param state 0表示成功，1表示失败
     */
    public void updateCancelContractState(int state) {
        switch (state) {
            case 0:
                iv_bail_cancel_state.setImageResource(R.drawable.ic_success);
                break;
            case 1:
                iv_bail_cancel_state.setImageResource(R.drawable.ic_fail);
                break;
        }
    }


    @Override
    protected void initData() {
    }

    @Override
    protected void setListener() {
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BailStateDialog.this.dismiss();
                if (onConfirmListener != null) {
                    onConfirmListener.onConfirm();
                }
            }
        });
    }

    public interface OnConfirmListener {
        void onConfirm();
    }

    private OnConfirmListener onConfirmListener;

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }
}
