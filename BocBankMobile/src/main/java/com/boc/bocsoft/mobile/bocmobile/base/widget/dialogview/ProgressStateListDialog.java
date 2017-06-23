package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * Dialog：进度状态列表对话框
 * Created by zhx on 2016/12/16
 */
public class ProgressStateListDialog extends BaseDialog {
    private View rootView;
    private TextView tv_ok;
    private ListView lv_list;
    private TextView tv_title;
    private ImageView iv_change_trade_state;
    private ProgressBar pb_change_trade_state;
    private View bottomView;
    private LinearLayout bottomViewContainer;

    public ProgressStateListDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected View onAddContentView() {
        rootView = View.inflate(mContext,
                R.layout.dialog_progress_state_list, null);
        return rootView;
    }

    public TextView getOKBtn() {
        return tv_ok;
    }

    public TextView getTvTitle() {
        return tv_title;
    }

    @Override
    protected void initView() {
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        tv_ok = (TextView) rootView.findViewById(R.id.tv_ok);
        lv_list = (ListView) rootView.findViewById(R.id.lv_list);

        bottomView = View.inflate(mContext, R.layout.dialog_progress_state_list_header, null);
        iv_change_trade_state = (ImageView) bottomView.findViewById(R.id.iv_change_trade_state);
        pb_change_trade_state = (ProgressBar) bottomView.findViewById(R.id.pb_change_trade_state);
        bottomViewContainer = (LinearLayout) bottomView.findViewById(R.id.ll_container);
        lv_list.addFooterView(bottomView);
    }

    public ListView getListView() {
        return lv_list;
    }

    public View getBottomView() {
        return bottomView;
    }

    /**
     * 隐藏掉listView的footer
     */
    public void setbottomViewContainerGone() {
        bottomViewContainer.setVisibility(View.GONE);
    }

    // 2表示成功，3表示失败
    public void setBottomSate(int state) {
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

    public void setAdapter(ListAdapter adapter) {
        lv_list.setAdapter(adapter);
    }

    public ImageView getChangeTradeStateImageVIew() {
        return iv_change_trade_state;
    }

    public void setIv_change_trade_state(ImageView iv_change_trade_state) {
        this.iv_change_trade_state = iv_change_trade_state;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void setListener() {
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressStateListDialog.this.dismiss();
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
