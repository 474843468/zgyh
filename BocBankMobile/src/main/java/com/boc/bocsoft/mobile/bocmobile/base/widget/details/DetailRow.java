package com.boc.bocsoft.mobile.bocmobile.base.widget.details;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.SpannableString;


/**
 * 最新单条Item(详情,确认页,结果页)
 * Created by wangyang on 2016/11/25.
 */
public class DetailRow extends LinearLayout implements ViewTreeObserver.OnGlobalLayoutListener {

    private SpannableString tvName, tvValue;
    private View viewLine;
    private String textName;
    private String textValue;
    private boolean isShowDivider;

    public DetailRow(Context context) {
        this(context, null, 0);
    }

    public DetailRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initView();
    }

    public DetailRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.detailsTextView);
        textName = a.getString(R.styleable.detailsTextView_textName);
        textValue = a.getString(R.styleable.detailsTextView_textValue);
        isShowDivider = a.getBoolean(R.styleable.detailsTextView_fullLine, false);
        a.recycle();
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.boc_view_detail_row_widget, this);
        tvName = (SpannableString) view.findViewById(R.id.tv_name);
        tvValue = (SpannableString) view.findViewById(R.id.tv_value);
        viewLine = view.findViewById(R.id.view_line);
        if (isShowDivider)
            viewLine.setVisibility(VISIBLE);
        updateTitle(textName);
        updateValue(textValue);
    }

    public void showDividerLine(int visibility) {
        viewLine.setVisibility(visibility);
    }

    /**
     * 改变详情标题
     *
     * @param name：左侧标题
     */
    public <T extends CharSequence> void updateTitle(T name) {
        tvName.setText(name);
    }

    /**
     * 改变详情内容
     *
     * @param value：右侧数据
     */
    public <T extends CharSequence> void updateValue(T value) {
        tvValue.setText(value);
    }

    /**
     * 改变详情标题和内容
     *
     * @param name
     * @param value
     */
    public void updateData(CharSequence name, CharSequence value) {
        updateTitle(name);
        updateValue(value);
    }

    public SpannableString getName() {
        return tvName;
    }

    public SpannableString getValue() {
        return tvValue;
    }

    @Override
    public void onGlobalLayout() {
        if (!isShown())
            return;

        boolean isNewLine = false;

        if (tvName.getPaint().measureText(tvName.getText().toString()) > tvName.getWidth())
            isNewLine = true;

        if (tvValue.getPaint().measureText(tvValue.getText().toString()) > tvValue.getWidth())
            isNewLine = true;

        if (!isNewLine)
            return;

        tvName.setGravity(Gravity.TOP);
        tvValue.setGravity(Gravity.TOP);
    }
}
