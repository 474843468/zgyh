package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * Dialog：新增并变更对话框
 * Created by zhx on 2016/12/23
 */
public class AddAndChangDialog extends BaseDialog{
    private View rootView;
    private ImageView iv_add_new_bail;
    private ProgressBar pb_add_new_bail;
    private ImageView iv_change_trade_state;
    private ProgressBar pb_change_trade_state;
    private TextView tv_ok;
    private TextView tv_title;

    public AddAndChangDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected View onAddContentView() {
        rootView = View.inflate(mContext,
                R.layout.dialog_add_and_change, null);
        return rootView;
    }

    @Override
    protected void initView() {
        iv_add_new_bail = (ImageView) rootView.findViewById(R.id.iv_add_new_bail);
        pb_add_new_bail = (ProgressBar) rootView.findViewById(R.id.pb_add_new_bail);
        iv_change_trade_state = (ImageView) rootView.findViewById(R.id.iv_change_trade_state);
        pb_change_trade_state = (ProgressBar) rootView.findViewById(R.id.pb_change_trade_state);
        tv_ok = (TextView) rootView.findViewById(R.id.tv_ok);
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
    }

    @Override
    protected void initData() {

    }

    public TextView getOKBtn() {
        return tv_ok;
    }

    // 2表示成功，3表示失败
    public void setAddBailSate(int state) {
        switch (state) {
            case 2:
                pb_add_new_bail.setVisibility(View.GONE);
                iv_add_new_bail.setVisibility(View.VISIBLE);
                iv_add_new_bail.setImageResource(R.drawable.ic_success);
                break;
            case 3:
                pb_add_new_bail.setVisibility(View.GONE);
                iv_add_new_bail.setVisibility(View.VISIBLE);
                iv_add_new_bail.setImageResource(R.drawable.ic_fail);
                break;
        }
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }

    // 2表示成功，3表示失败
    public void setChangeTradeSate(int state) {
        switch (state) {
            case 2:
                pb_change_trade_state.setVisibility(View.GONE);
                iv_change_trade_state.setVisibility(View.VISIBLE);
                iv_change_trade_state.setImageResource(R.drawable.ic_success);
                break;
            case 3:
                pb_change_trade_state.setVisibility(View.GONE);
                iv_change_trade_state.setVisibility(View.VISIBLE);
                iv_change_trade_state.setImageResource(R.drawable.ic_fail);
                break;
        }
    }

    @Override
    protected void setListener() {
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAndChangDialog.this.dismiss();
                if (onCloseListener != null) {
                    onCloseListener.onClose();
                }

            }
        });
    }

    private OnCloseListener onCloseListener;

    public void setOnCloseListener(OnCloseListener onCloseListener) {
        this.onCloseListener = onCloseListener;
    }

    public interface OnCloseListener {
        void onClose();
    }
}
