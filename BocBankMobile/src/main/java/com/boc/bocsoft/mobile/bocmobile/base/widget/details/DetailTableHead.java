package com.boc.bocsoft.mobile.bocmobile.base.widget.details;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;


/**
 * 交易详情头部组件
 */
public class DetailTableHead extends LinearLayout {


    protected TextView tvSumTitle;
    protected TextView tvSum;
    protected TextView tvStatus;
    protected View vSum;
    protected DetailTableRow detail;
    protected LinearLayout layoutDetailHead;

    //    2016-11-10 20:15:41  理财添加支持
    protected View vSum_line;
    protected DetailTableRow detailTow;
    protected LinearLayout ll_datail_tow;

    public View layout_root;
    private Context mContext;

    public DetailTableHead(Context context) {
        this(context, null, 0);
    }

    public DetailTableHead(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DetailTableHead(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        layout_root = LayoutInflater.from(mContext).inflate(R.layout.boc_view_details_table_head, this);
        tvSumTitle = (TextView) layout_root.findViewById(R.id.tv_sum_title);
        tvSum = (TextView) layout_root.findViewById(R.id.tv_sum);
        tvStatus = (TextView) layout_root.findViewById(R.id.tv_status);
        vSum = (View) layout_root.findViewById(R.id.v_sum);
        detail = (DetailTableRow) layout_root.findViewById(R.id.detail);
        layoutDetailHead = (LinearLayout) layout_root.findViewById(R.id.layout_detail_head);
        vSum_line = (View) layout_root.findViewById(R.id.v_sum_line);
        detailTow = (DetailTableRow) layout_root.findViewById(R.id.detail_tow);
        ll_datail_tow = (LinearLayout) layout_root.findViewById(R.id.ll_datail_tow);

        detail.setRowLineVisable(false);
        detailTow.setRowLineVisable(false);
    }

    /**
     * 改变显示内容
     *
     * @param title:左侧标题内容
     * @param sum:右侧详情数据
     */
    public void updateData(String title, String sum) {
        tvSumTitle.setText(title);
        tvSum.setText(sum);
    }

    /**
     * 设置连接行参数
     *
     * @param content
     * @param value
     */
    public void setTableRow(String content, String value) {
        detail.updateData(content, value);
    }

    /**
     * 设置第二个 row
     *
     * @param name
     * @param value
     */
    public void setTableRowTwo(String name, String value) {
        detail.isShowDividerLine(false);
        ll_datail_tow.setVisibility(View.VISIBLE);
        detailTow.updateData(name, value);
    }


    public void setTvSumMargin(int left,int top,int right,int bottom){
        LayoutParams lp = new LayoutParams(tvSum.getLayoutParams());
        lp.setMargins(left, top, right, bottom);
        tvSum.setLayoutParams(lp);
    }

    public void setViewSumMargin(int left,int top,int right,int bottom){
        LayoutParams lp = new LayoutParams(vSum.getLayoutParams());
        lp.setMargins(left, top, right, bottom);
        vSum.setLayoutParams(lp);
    }

    /**
     * 单条的详细row是否显示
     * 默认为显示
     *
     * @param isTrue：false是不显示
     */
    public void setDetailVisable(boolean isTrue) {
        if (!isTrue) {
            detail.setVisibility(View.GONE);
        }
    }

    /**
     * 设置数字下面的分割线的左右margging值
     * 设置边距时调用
     *
     * @param left
     * @param right
     */
    public void setLineMargging(int left, int right) {
        LayoutParams lp = new LayoutParams(vSum.getLayoutParams());
        lp.setMargins(12, 0, 12, 0);
    }


    /**
     * 设置右侧状态信息
     *
     * @param text
     * @param resId
     */
    public void setHeadStatus(String text, int resId) {
        tvStatus.setText(text + "");
        tvStatus.setBackgroundResource(resId);
        tvStatus.setVisibility(View.VISIBLE);
    }

    /**
     * 显示头部状态信息（指定字体颜色）
     *
     * @param text
     * @param color
     */
    public void showHeadStatus(String text, int color) {
        tvStatus.setText(text + "");
        tvStatus.setTextColor(color);
        tvStatus.setVisibility(View.VISIBLE);
    }

    public void addDetail(String name, String value) {
        detail.updateData(name, value);
    }

    /**
     * 设置 此view 底部没有 间距
     */
    public void setLayoutDetailHeadNoMargin() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        layoutDetailHead.setLayoutParams(lp);
    }

}
