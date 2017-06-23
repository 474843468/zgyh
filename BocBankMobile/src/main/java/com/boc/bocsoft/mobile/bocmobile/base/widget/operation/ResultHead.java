package com.boc.bocsoft.mobile.bocmobile.base.widget.operation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.SpannableString;

/**
 * 操作结果头部组件
 * Created by wangyang on 2016/11/24.
 */
public class ResultHead extends LinearLayout {

    private ImageView ivStatus;
    private TextView tvStatus;
    private SpannableString tvTitle;
    private LinearLayout llHeader;

    public enum Status {
        SUCCESS, FAIL, INPROGRESS
    }

    public ResultHead(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ResultHead(Context context) {
        super(context);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.boc_view_result_head, this);
        ivStatus = (ImageView) view.findViewById(R.id.iv_status);
        tvStatus = (TextView) view.findViewById(R.id.tv_status);
        tvTitle = (SpannableString) view.findViewById(R.id.tv_title);
        llHeader = (LinearLayout) view.findViewById(R.id.ll_header);
    }

    /**
     * 操作结果头部信息，不同结果切换相应图片
     *
     * @param status :操作状态
     *               1.SUCCESS 成功
     *               2.FAIL 失败
     *               3.INPROGRESS 处理中
     * @param Content   :操作结果提示信息
     */
    public void addStatus(Status status, String Content) {
        tvStatus.setText(Content);
        Drawable drawable;
        switch (status) {
            case SUCCESS:
                drawable = getResources().getDrawable(R.drawable.boc_operator_succeed);
                ivStatus.setImageDrawable(drawable);
                break;
            case FAIL:
                drawable = getResources().getDrawable(R.drawable.boc_operator_fail);
                ivStatus.setImageDrawable(drawable);
                break;
            case INPROGRESS:
                drawable = getResources().getDrawable(R.drawable.boc_operator_inprogress);
                ivStatus.setImageDrawable(drawable);
                break;
        }
    }

    public <T extends CharSequence>void addTitle(T title){
        tvTitle.setVisibility(VISIBLE);
        tvTitle.setText(title);
    }

    public SpannableString getTitle(){
        tvTitle.setVisibility(VISIBLE);
        return tvTitle;
    }

    public void addHeadView(View view){
        llHeader.setVisibility(VISIBLE);
        llHeader.addView(view);
    }

}
