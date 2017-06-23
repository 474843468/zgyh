package com.boc.bocsoft.mobile.bocmobile.base.widget.details;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;


/**
 * 详情组件item项，带分组标题
 * Created by niuguobin on 2016/5/19.
 */
public class DetailTableRowGroup extends LinearLayout {

    private View root_view;
    private TextView name;
    private LinearLayout lyDetails;
    private Context mContext;

    public DetailTableRowGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);
        root_view = LayoutInflater.from(context).inflate(R.layout.boc_layout_detail_title, this);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DetailTableRowGroup2);
        String titleString = a.getString(R.styleable.DetailTableRowGroup2_groupTitle);
        a.recycle();

        name = (TextView) root_view.findViewById(R.id.tv_head);
        lyDetails = (LinearLayout)  root_view.findViewById(R.id.ly_details);
        name.setText(titleString);
    }

    /**
     *添加详情item
     * @param name
     * @param value
     */
    public void addDetailTabRow(String name,String value){
        DetailTableRow row = new DetailTableRow(mContext);
        row.updateData( name, value);
        lyDetails.addView(row);
    }

    public void addDetailRowNotLine(String name,String value){
        DetailTableRow row = new DetailTableRow(mContext);
        row.updateData( name, value);
        row.setRowLineVisable(false);
        lyDetails.addView(row);
    }

    /**
     * 设置分组标题
     * @param title
     */
    public void setGroupTitle(String title){
        name.setText(title);
    }
}
