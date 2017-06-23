package com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * 可点击的组件Span
 * Created by xintong on 2016/6/12.
 */
public class MClickableSpan extends ClickableSpan {
    //传入的文字内容
    private String string;
    private Context context;
    //传入的颜色值
    private int color;
    private OnClickSpanListener listener;

    public MClickableSpan(String string, Context context) {
        super();
        this.string = string;
        this.context = context;
    }

    public MClickableSpan() {

    }

    public void setString(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    /**
     * 更新颜色状态
     */
    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(getColor());
    }

    /**
     * 获取颜色
     *
     * @return 颜色值
     */
    public int getColor() {
        return color;
    }

    /**
     * 设置颜色
     *
     * @param color 颜色值
     */
    public void setColor(int color) {
        this.color = color;
    }

    //为组件设置点击事件
    public void setListener(OnClickSpanListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View widget) {
        if (listener != null) {
            listener.onClickSpan();
        }
    }

    public interface OnClickSpanListener {
        public void onClickSpan();
    }
}
