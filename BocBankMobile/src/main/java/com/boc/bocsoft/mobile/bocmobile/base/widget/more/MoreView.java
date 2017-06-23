package com.boc.bocsoft.mobile.bocmobile.base.widget.more;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * 更多组件
 * Created by niuguobin on 2016/6/24.
 */
public class MoreView extends LinearLayout {

    protected LinearLayout llContent;

    private OnClickListener clickListener;

    public MoreView(Context context) {
        this(context, null, 0);
    }

    public MoreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVIew();
    }

    public void initVIew() {
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        llContent = new LinearLayout(getContext());
        llContent.setLayoutParams(lp);
        llContent.setOrientation(LinearLayout.VERTICAL);
        this.addView(llContent);
    }

    public MoreView addMoreView(final String id, String name) {
        return addMoreView(id, name, false);
    }

    /**
     * 添加item
     *
     * @param id
     * @param name
     * @param isMarginTop
     */
    public MoreView addMoreView(final String id, String name, boolean isMarginTop) {
        MoreItemView moreItem = new MoreItemView(getContext());
        moreItem.setData(name);
        moreItem.setTag(id);
        moreItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null)
                    clickListener.onClick(id);
            }
        });

        if (isMarginTop) {
            View line = new View(getContext());
            LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getContext().getResources().getDimensionPixelOffset(R.dimen.boc_space_between_1px));
            params.setMargins(0, getContext().getResources().getDimensionPixelOffset(R.dimen.boc_space_between_20px), 0, 0);
            line.setLayoutParams(params);
            line.setBackgroundColor(getContext().getResources().getColor(R.color.boc_divider_line_color));
            llContent.addView(line);
        }
        llContent.addView(moreItem);
        return this;
    }

    public <T extends CharSequence>void setContentById(String id, T content) {
        ((MoreItemView) llContent.findViewWithTag(id)).setContent(content,R.color.boc_text_color_red);
    }

    public <T extends CharSequence>void setContentById(String id, T content,int textColor) {
        ((MoreItemView) llContent.findViewWithTag(id)).setContent(content,textColor);
    }

    public interface OnClickListener {
        void onClick(String id);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.clickListener = onClickListener;
    }


}
