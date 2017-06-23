package com.boc.bocsoft.mobile.bocmobile.base.widget.operation;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRow;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowButton;

/**
 * (请使用最新结果页组件 BaseResultView,使用参考 TestResultFragment)操作结果详情信息
 * Created by niuguobin on 2016/6/2.
 */
@Deprecated
public class OperationResultDetail extends LinearLayout {
    private Context mContext;
    private View rootView;
    // 详情按钮名称
    private TextView txtDetails;
    LinearLayout layout_detail, layout_btn;
    //2016年10月13日 19:50:39 yx add   添加固定显示项目
    private  LinearLayout layout_top_detail;
    //是否显示 top 详情 一直显示
    private boolean isShowTopDetailRow = false;

    @Deprecated
    public OperationResultDetail(Context context) {
        this(context, null);
    }

    @Deprecated
    public OperationResultDetail(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    @Deprecated
    private void init() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.boc_view_operation_result_content, this);
        layout_top_detail = (LinearLayout) findViewById(R.id.layout_top_detail);
        layout_detail = (LinearLayout) findViewById(R.id.layout_detail);
        txtDetails = (TextView) rootView.findViewById(R.id.txt_details_title);
        layout_btn = (LinearLayout) findViewById(R.id.layout_btn);
        layout_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShowTopDetailRow){
                    layout_top_detail.setVisibility(View.VISIBLE);
                }else {
                    layout_top_detail.setVisibility(View.GONE);
                }
                layout_btn.setVisibility(View.GONE);
                layout_detail.setVisibility(View.VISIBLE);
            }
        });
    }


    /**
     * 获取详情Layout
     *
     * @return
     */
    @Deprecated
    public LinearLayout getLayoutDetailParent() {
        return layout_detail;
    }


    /**
     * 添加详情item
     *
     * @param name
     * @param value
     */
    @Deprecated
    public void addDetailRow(String name, String value) {
        DetailTableRow tableRow = new DetailTableRow(mContext);
        tableRow.setValueEllipsize(TextUtils.TruncateAt.MIDDLE);
        tableRow.updateData(name, value);
        layout_detail.addView(tableRow);
    }
    /**
     * 添加top 固定显示的详情item
     *
     * @param name
     * @param value
     * @date 2016年10月13日 19:53:09
     * @author yx
     */
    @Deprecated
    public void addTopDetailRow(String name, String value) {
        DetailTableRow tableRow = new DetailTableRow(mContext);
        tableRow.updateData(name, value);
        layout_top_detail.addView(tableRow);
    }
    /**
     * 设置是否显示 top 详情row
     *
     * @param isShowTopDetailRow 是否显示top 详情row
     * @date 2016年10月13日 19:53:09
     * @author yx
     */
    @Deprecated
    public void isAddTopDetailRow(boolean isShowTopDetailRow) {
       this.isShowTopDetailRow = isShowTopDetailRow;
        if(isShowTopDetailRow){
            layout_top_detail.setVisibility(View.VISIBLE);
        }else {
            layout_top_detail.setVisibility(View.GONE);
        }
    }

    @Deprecated
    public void addDetailRow(String name, String value, boolean isVisible, boolean isShow) {
        DetailTableRow tableRow = new DetailTableRow(mContext);
        tableRow.updateData(name, value);
        tableRow.setRowLineVisable(isVisible);// 是否显示分隔线
        tableRow.isShowDividerLine(isShow);
        layout_detail.addView(tableRow);
    }

    /**
     * 添加详情Btn
     *
     * @param name
     * @param value
     * @param text
     * @param textColor
     * @param btnCallback
     */
    @Deprecated
    public void addDetailRowBtn(String name, String value, String text, int textColor, DetailTableRowButton.BtnCallback btnCallback) {
        DetailTableRowButton tableBtn = new DetailTableRowButton(mContext);
        tableBtn.setOnclick(btnCallback);
        tableBtn.addTextBtn(name, value, text, textColor);
        layout_detail.addView(tableBtn);
    }

    /**
     * 添加详情item,不显示分割线。（不带分组和按钮）
     *
     * @param name
     * @param value
     */
    @Deprecated
    public void addDetailRowNotLine(String name, String value) {
        DetailTableRow tableRow = new DetailTableRow(mContext);
        tableRow.updateData(name, value);
        tableRow.setRowLineVisable(false);
        layout_detail.addView(tableRow);
    }

    @Deprecated
    public void setTextTitle(String name) {
        txtDetails.setText(name);
    }

//    public void setTextTitleMeasure(int gravity,int left, int top, int right, int bottom){
//        txtDetails.setGravity(gravity);
//        txtDetails.setPadding(left,top,right,bottom);
//    }

    /**
     * 添加详情布局
     *
     * @param detailLayout 布局页面
     * @param params       布局参数
     */
    @Deprecated
    public void addDetailLayout(View detailLayout, LinearLayout.LayoutParams params) {
        if (detailLayout == null) {
            return;
        }
        layout_detail.addView(detailLayout, params);
    }

    /**
     * 设置是否显示 交易明细 title
     *
     * @param isShowDetailsTitle true 有title 点击展开明细，false 没有title 直接显示明细
     * @date 2016年10月9日 19:58:30
     * @author yx
     */
    @Deprecated
    public void setDetailsTitleIsShow(boolean isShowDetailsTitle) {
        if (isShowDetailsTitle) {
            layout_btn.setVisibility(View.VISIBLE);
            layout_detail.setVisibility(View.GONE);
        } else {
            layout_btn.setVisibility(View.GONE);
            layout_detail.setVisibility(View.VISIBLE);
        }

    }

}
