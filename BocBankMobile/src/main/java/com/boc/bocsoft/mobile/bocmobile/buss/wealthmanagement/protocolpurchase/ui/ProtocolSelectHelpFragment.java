package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;

/**
 * 选择投资协议--帮助
 * Created by liuweidong on 2016/11/5.
 */
public class ProtocolSelectHelpFragment extends BussFragment {
    private View rootView;
    private LinearLayout llParent;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = View.inflate(mContext, R.layout.boc_fragment_protocol_help, null);
        return rootView;
    }

    @Override
    public void initView() {
        llParent = (LinearLayout) rootView.findViewById(R.id.ll_parent);
    }

    @Override
    public void initData() {
        showHeadListView();
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return mContext.getString(R.string.boc_wealth_protocol_help);
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    private void showHeadListView() {
        llParent.removeAllViews();
        String[] title = {"周期连续协议", "周期不连续协议", "多次购买协议", "多次赎回协议", "定时定额投资", "周期滚续协议", "余额理财投资"};
        String[] type = {"智能投资", "智能投资", "智能投资", "智能投资", "定时定额投资", "周期滚续协议", "余额理财投资"};
        String[] values = getResources().getStringArray(R.array.boc_protocol_purchase_content_1);// 协议介绍
        for (int i = 0; i < title.length; i++) {
            View view = View.inflate(mContext, R.layout.boc_fragment_protocol_help_item, null);
            TextView txtTitle = (TextView) view.findViewById(R.id.txt_title);
            TextView txtType = (TextView) view.findViewById(R.id.txt_type);
            TextView txtValue = (TextView) view.findViewById(R.id.txt_content);
            txtTitle.setText(title[i]);
            txtType.setText(type[i]);
            txtValue.setText(values[i]);
            llParent.addView(view);
        }
    }

}
