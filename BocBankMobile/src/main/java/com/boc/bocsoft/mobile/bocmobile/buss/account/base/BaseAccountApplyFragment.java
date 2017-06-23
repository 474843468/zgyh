package com.boc.bocsoft.mobile.bocmobile.buss.account.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * @author wangyang
 *         16/8/29 22:50
 *         申请界面基类
 */
public abstract class BaseAccountApplyFragment extends BaseAccountFragment {

    /**
     * 根布局
     */
    private ViewGroup llContent;

    /**
     * 初始化布局
     *
     * @param mInflater
     * @return
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_base_account_apply, null);
    }

    /**
     * 初始化控件
     */
    @Override
    public void initView() {
        llContent = (ViewGroup) mContentView.findViewById(R.id.ll_content);
    }

    public void addModule(String title, String content, final int tag) {
        LinearLayout linearLayout = (LinearLayout) getLayoutInflater(null).inflate(R.layout.boc_fragment_base_account_apply_item, null);
        ((TextView) linearLayout.findViewById(R.id.tv_title)).setText(title);
        ((TextView) linearLayout.findViewById(R.id.tv_content)).setText(content);
        linearLayout.findViewById(R.id.btn_apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseAccountApplyFragment.this.onClick(tag);
            }
        });
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = getResources().getDimensionPixelSize(R.dimen.boc_space_between_18px);
        lp.setMargins(margin, margin, margin, margin);
        llContent.addView(linearLayout,lp);
    }

    protected abstract void onClick(int tag);
}
