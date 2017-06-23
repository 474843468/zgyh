package com.boc.bocsoft.mobile.bocmobile.base.widget.operation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.SpannableString;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailRow;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wangyang
 *         2016/11/25 10:55
 *         结果页详情
 */
public class ResultDetail extends LinearLayout implements View.OnClickListener {

    // 详情按钮名称
    private SpannableString tvDetail, tvHint;
    private LinearLayout llDetail, llDetailTop, llDetailBottom;

    private OnDetailClickListener listener;

    public ResultDetail(Context context) {
        this(context, null);
    }

    public ResultDetail(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.boc_view_result_detail, this);
        tvDetail = (SpannableString) view.findViewById(R.id.tv_detail);
        tvHint = (SpannableString) view.findViewById(R.id.tv_hint);
        llDetail = (LinearLayout) view.findViewById(R.id.ll_detail);
        llDetailTop = (LinearLayout) view.findViewById(R.id.ll_detail_top);
        llDetailBottom = (LinearLayout) view.findViewById(R.id.ll_detail_bottom);
        tvDetail.setOnClickListener(this);
    }

    public SpannableString getDetail() {
        return tvDetail;
    }

    public <T extends CharSequence> void updateDetail(T detail) {
        tvDetail.setText(detail);
    }

    public SpannableString getHint() {
        return tvHint;
    }

    public <T extends CharSequence> void setHint(T hint) {
        tvHint.setVisibility(VISIBLE);
        tvHint.setText(hint);
    }

    public void addTopDetail(LinkedHashMap<String, ? extends CharSequence> map) {
        llDetailTop.setVisibility(VISIBLE);
        for (Map.Entry<String, ? extends CharSequence> entry : map.entrySet()) {
            DetailRow row = new DetailRow(getContext());
            row.setMinimumHeight(getResources().getDimensionPixelOffset(R.dimen.boc_space_between_80px));
            row.showDividerLine(View.VISIBLE);
            row.updateData(entry.getKey(), entry.getValue());
            llDetailTop.addView(row);
        }
    }

    public void addTopDetail(View view) {
        llDetailTop.setVisibility(VISIBLE);
        llDetailTop.addView(view);
    }

    public void addDetail(LinkedHashMap<String, ? extends CharSequence> map) {
        llDetail.setVisibility(VISIBLE);
        for (Map.Entry<String, ? extends CharSequence> entry : map.entrySet()) {
            DetailRow row = new DetailRow(getContext());
            row.setMinimumHeight(getResources().getDimensionPixelOffset(R.dimen.boc_space_between_80px));
            row.showDividerLine(View.VISIBLE);
            row.updateData(entry.getKey(), entry.getValue());
            llDetailBottom.addView(row);
        }
    }

    public void addDetail(View view) {
        llDetail.setVisibility(VISIBLE);
        llDetailBottom.addView(view);
    }

    @Override
    public void onClick(View v) {
        if (v == tvDetail) {
            llDetail.setVisibility(GONE);
            llDetailBottom.setVisibility(VISIBLE);
            if (listener != null)
                listener.onDetailClick();
        }
    }

    public void setOnDetailClickListener(OnDetailClickListener listener) {
        this.listener = listener;
    }

    public interface OnDetailClickListener {
        void onDetailClick();
    }
}
