package com.boc.bocsoft.mobile.bocmobile.base.widget.details;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 *
 *
 * 添加带分组的item,
 * Created by niuguobin on 2016/5/21.
 */

public class DetailContentGroupView extends LinearLayout {
    private TextView tvHead;
    private View vHead;

    private Context mContext;
    private View root_view;
    private DetailContentView bodyView;

    public DetailContentGroupView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DetailContentGroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        root_view = LayoutInflater.from(mContext).inflate(R.layout.boc_view_detail_group_content, this);
        tvHead = (TextView)root_view.findViewById(R.id.tv_head);
        vHead = (View)root_view.findViewById(R.id.v_head);
        bodyView = (DetailContentView) root_view.findViewById(R.id.body_view);
    }

    /**
     * 设置分组标题
     * @param title
     */
    public void setTitleText(String title){
        tvHead.setText(title);
    }

/**
     * 添加详情item。
     *
     * @param name
     * @param value
     */

    public void addDetailRow(String name, String value) {
        bodyView.addDetailRow(name, value);
    }

    public void addGroupDetailNotLine(String name,String value){
        bodyView.addDetailRowNotLine(name,value);
    }

/**
     * 添加详情item,显示文字按钮
     *
     * @param name:左侧详情标题
     * @param value:右侧详情数据
     * @param text:按钮文字
     * @param textColor:按钮文字颜色
     */

    public void addTextBtn(String name, String value, String text, int textColor) {
        DetailTableRowButton tableBtn = new DetailTableRowButton(mContext);
        tableBtn.addTextBtn(name, value, text, textColor);
        bodyView.addView(tableBtn);
    }

    /**
     * 添加详情item,显示文字按钮
     * @param name
     * @param value
     * @param text
     */
    public void addTextBtn(String name, String value, String text) {
        DetailTableRowButton tableBtn = new DetailTableRowButton(mContext);
        tableBtn.addTextBtn(name, value, text);
        bodyView.addView(tableBtn);
    }


/**
     * 添加详情item,显示图片按钮
     *
     * @param name：左侧详情标题
     * @param value：右侧详情数据
     * @param bgImg：图片按钮背景图，,图片来自Drawable
     */

    public void addImgBtn(String name, String value, int bgImg) {
        DetailTableRowButton tableBtn = new DetailTableRowButton(mContext);
        tableBtn.addImgBtn(name, value, bgImg);
        bodyView.addView(tableBtn);
    }


/**
     * 添加详情item，文字按钮和图片按钮同时显示
     *
     * @param name：左侧详情标题
     * @param value：右侧详情数据
     * @param text：按钮文字
     * @param textColor：文字按钮字体颜色
     * @param textBgColor：文字按钮背景颜色
     * @param bgImg：图片按钮背景图，,图片来自Drawable
     */

    public void addTextAndImgBtn(String name, String value, String text, int textColor, int textBgColor, int bgImg) {
        DetailTableRowButton tableBtn = new DetailTableRowButton(mContext);
        tableBtn.addTextAndImgBtn(name, value, text, textColor, textBgColor, bgImg);
        bodyView.addView(tableBtn);
    }

}
