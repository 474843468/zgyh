package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRow;

/**
 * Created by yangle on 2016/11/28.
 */
public class DetailHeaderView extends LinearLayout {

    private View mRootView;
    private TextView mTvTitle;
    private TextView mTvValue;
    private LinearLayout mLayout_header;

    public DetailHeaderView(Context context) {
        this(context,null);
    }

    public DetailHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DetailHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.boc_crcd_detail_header_view, this);
        mLayout_header = (LinearLayout) mRootView.findViewById(R.id.layout_detail_header);
        mTvTitle = (TextView) mRootView.findViewById(R.id.tv_title);
        mTvValue = (TextView) mRootView.findViewById(R.id.tv_value);
    }

    public void setTitleAndValue(String title, String value) {
        mTvTitle.setText(title);
        mTvValue.setText(value);
    }

    private DetailTableRow addDetailRowView() {
        DetailTableRow detailTableRow = new DetailTableRow(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayout_header.addView(detailTableRow,params);
        return detailTableRow;
    }

    public void addRowDetail(String name, String value) {
        DetailTableRow detailTableRow = addDetailRowView();
        detailTableRow.updateData(name, value);
    }

    public void setAlignLeft(boolean isAlignLeft) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mTvValue.getLayoutParams();
        if (isAlignLeft) {
            layoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.boc_space_between_20px);
            //layoutParams.topMargin = getResources().getDimensionPixelSize(R.dimen.boc_space_between_20px);
            layoutParams.leftMargin = getResources().getDimensionPixelSize(R.dimen.boc_space_between_28px);
        } else {
            layoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.boc_space_between_50px);
            layoutParams.topMargin = getResources().getDimensionPixelSize(R.dimen.boc_space_between_44px);
            layoutParams.leftMargin = getResources().getDimensionPixelSize(R.dimen.boc_space_between_62px);
        }
        mTvValue.setLayoutParams(layoutParams);
    }

     private  int dip2px(float dipValue) {
         return Math.round(dipValue * getContext().getResources().getDisplayMetrics().density);
    }




}
