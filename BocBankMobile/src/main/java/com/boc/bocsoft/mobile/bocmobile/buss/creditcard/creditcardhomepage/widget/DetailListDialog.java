package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.widget;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.BaseDialog;


public class DetailListDialog extends BaseDialog {
    private View contentView;

    private LinearLayout ll_dialog_content;
    private TextView btn_bottom;

    private OnBottomViewClickListener mListener;
    private String[] names;
    private String[] values;

    public DetailListDialog(Context context, String[] names, String[] values) {
        super(context);
        this.mContext = context;
        this.names = names;
        this.values = values;
        showDetailListView(names, values);
    }

    @Override
    protected View onAddContentView() {
        contentView = inflateView(R.layout.boc_dialog_layout_view);
        return contentView;
    }

    @Override
    protected void initView() {
        ll_dialog_content = (LinearLayout) contentView.findViewById(R.id.ll_dialog_content);
        btn_bottom = (TextView) contentView.findViewById(R.id.btn_bottom);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        btn_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
                if (mListener != null) {
                    mListener.onBottomViewClick(v);
                }
            }
        });
    }

    /**
     * 设置详情数据
     *
     * @param names
     * @param values
     */
    private void showDetailListView(String[] names, String[] values) {
        ll_dialog_content.removeAllViews();
        for (int i = 0; i < names.length; i++) {
            View view = View.inflate(mContext, R.layout.boc_item_dialog_child, null);
            TextView txtName = (TextView) view.findViewById(R.id.tv_item_name);
            TextView txtValue = (TextView) view.findViewById(R.id.tv_item_value);
            txtName.setText(names[i]);
            txtValue.setText(values[i]);
            ll_dialog_content.addView(view);
        }
    }

    /**
     * 设置按钮文字
     *
     * @param btnText
     */
    public DetailListDialog setBtnText(String btnText) {
        // 按钮文字
        btn_bottom.setText(btnText);
        return this;
    }

    /**
     * 底部按钮view的回调接口监听方法实现
     */
    public void setOnBottomViewClickListener(OnBottomViewClickListener mListener) {
        this.mListener = mListener;
    }

    /**
     * 点击事件回调接口监听
     */
    public interface OnBottomViewClickListener {
        /**
         * 点击事件传递
         */
        void onBottomViewClick(View v);
    }

}