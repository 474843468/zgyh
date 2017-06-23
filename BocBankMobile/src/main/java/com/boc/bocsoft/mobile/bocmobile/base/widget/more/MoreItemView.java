package com.boc.bocsoft.mobile.bocmobile.base.widget.more;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * Created by niuguobin on 2016/6/24.
 */
public class MoreItemView extends LinearLayout {
    /**
     * 图标
     */
    protected ImageView ivRight;
    /**
     * 标题
     */
    protected TextView tvTitle;
    /**
     * 更多
     */
    protected TextView tvContent;

    public MoreItemView(Context context) {
        this(context, null, 0);
    }

    public MoreItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoreItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.boc_view_account_more_item, this);
        ivRight = (ImageView) view.findViewById(R.id.iv_right);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvContent = (TextView) view.findViewById(R.id.tv_content);
    }

    /**
     * 设置功能
     *
     * @param title
     */
    public void setData(String title) {
        tvTitle.setText(title);
    }

    public <T extends CharSequence> void setContent(T content){
        setContent(content,R.color.boc_text_color_red);
    }

    public <T extends CharSequence> void setContent(T content, int textColor){
        tvContent.setTextColor(getResources().getColor(textColor));
        tvContent.setVisibility(VISIBLE);
        tvContent.setText(content);
    }

    public TextView getTvContent(){
        tvContent.setVisibility(VISIBLE);
        return tvContent;
    }
}
