package com.boc.bocsoft.mobile.bocmobile.base.widget.operation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRow;

/**
 * 操作结果详情信息
 * Created by guokai on 2016/6/2.
 */
public class InvestTreatyDetail extends LinearLayout {
    private Context mContext;
    private View rootView;
    // 详情按钮名称
    LinearLayout layout_detail;
    private TextView tvTitle;

    private ClickListener clickListener;

    public InvestTreatyDetail(Context context) {
        this(context, null);
    }

    public InvestTreatyDetail(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.boc_view_invest_treaty_detail, this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        layout_detail = (LinearLayout) findViewById(R.id.layout_detail);
    }


    /**
     * 获取详情Layout
     * @return
     */
    public LinearLayout getLayoutDetailParent(){
        return layout_detail;
    }

    /**
     * 设置标题
     */
    public void setTvTitle(String title){
        tvTitle.setText(title);
    }

    /**
     * 设置详情item
     *
     * @param name
     * @param value
     */
    public void addDetailRow(String name, String value) {
        DetailTableRow tableRow = new DetailTableRow(mContext);
        tableRow.updateData(name, value);
        layout_detail.addView(tableRow);
    }

    /**
     * 设置无条件的查询结果
     *
     */
    public void addNullBetweenRow() {
        View nullView = LayoutInflater.from(mContext).inflate(R.layout.boc_view_detail_confirm_null_row, null);
        layout_detail.addView(nullView);
    }


    /**
     * 设置详情item 内容分开在两头显示il
     *
     */
    public void addDetailBetweenRow(String tdsType, String memo,String state, String date,String tdsAmt,String tdsUnit) {

        View treatyView = LayoutInflater.from(mContext).inflate(R.layout.boc_view_detail_invest_treaty, null);
        TextView tvTdsType = (TextView) treatyView.findViewById(R.id.tv_tds_type);
        TextView tvTdsMemo = (TextView) treatyView.findViewById(R.id.tv_memo);
        TextView tvTdsState = (TextView) treatyView.findViewById(R.id.tv_tds_state);
        TextView tvTdsDate = (TextView) treatyView.findViewById(R.id.tv_tds_date);
        TextView tvTdsUnit = (TextView) treatyView.findViewById(R.id.tv_tds_unit);

        if ("1".equals(state)){
            tvTdsState.setTextColor(getResources().getColor(R.color.boc_text_color_red));
            tvTdsMemo.setVisibility(VISIBLE);
            tvTdsMemo.setText(memo);
            tvTdsState.setText(mContext.getString(R.string.boc_invest_treaty_failed));
        }else if ("0".equals(state)){
            tvTdsState.setText(mContext.getString(R.string.boc_invest_treaty_success));
            tvTdsState.setTextColor(getResources().getColor(R.color.boc_text_color_green));
            tvTdsMemo.setVisibility(GONE);
        }

        if ("1".equals(tdsType)){
            tvTdsType.setText(mContext.getString(R.string.boc_invest_treaty_isneed_pur));
            tvTdsUnit.setText(tdsAmt);
        } else if ("0".equals(tdsType)){
            tvTdsType.setText(mContext.getString(R.string.boc_invest_treaty_isneedred));
            tvTdsUnit.setText(tdsUnit);
        }
        tvTdsDate.setText(date);

        layout_detail.addView(treatyView);
    }

    /**
     * 设置修改详情
     * @param name
     * @param value
     */
    public void addDetailAlterRow(String name, String value) {
        View changeView = LayoutInflater.from(mContext).inflate(R.layout.boc_view_detail_confirm_change_row, null);
        TextView tvNameAgain = (TextView) changeView.findViewById(R.id.tv_name_again);
        TextView tvValueAgain = (TextView) changeView.findViewById(R.id.tv_value_again);
        TextView tvAlter = (TextView) changeView.findViewById(R.id.tv_alter);

        tvAlter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClickListener();
            }
        });
        tvNameAgain.setText(name);
        tvValueAgain.setText(value);
        layout_detail.addView(changeView);
    }

    public void addDetailRow(String name, String value, boolean isVisible, boolean isShow) {
        DetailTableRow tableRow = new DetailTableRow(mContext);
        tableRow.updateData(name, value);
        tableRow.setRowLineVisable(isVisible);// 是否显示分隔线
        tableRow.isShowDividerLine(isShow);
        layout_detail.addView(tableRow);
    }

    public interface ClickListener{
        void onClickListener();
    }

    /**
     * 修改按钮的点击事件
     * @param clickListener
     */
    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }

}
