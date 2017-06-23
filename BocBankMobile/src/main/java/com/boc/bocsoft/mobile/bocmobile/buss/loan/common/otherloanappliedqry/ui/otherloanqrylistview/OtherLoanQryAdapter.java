package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanappliedqry.ui.otherloanqrylistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 其他类型贷款进度查询listview公共组件适配器
 * <p/>
 * Created by liuzc on 2016/8/15.
 */
public class OtherLoanQryAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;

    private ClickListener mListener = null;

    //页面显示信息列表
    private List<OtherLoanQryBean> list;

    public OtherLoanQryAdapter(Context context) {
        this.context = context;
        list = new ArrayList<OtherLoanQryBean>();
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<OtherLoanQryBean> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.boc_item_loan_other_qry, null);

            viewHolder.tvDateAndAmount = (TextView)convertView.findViewById(R.id.tvDateAndAmount);;
            viewHolder.tvResult = (TextView)convertView.findViewById(R.id.tvResult);
            viewHolder.tvMoneyType = (TextView)convertView.findViewById(R.id.tvMoneyType);
            viewHolder.tvMoneySum = (TextView)convertView.findViewById(R.id.tvMoneySum);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        convertView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onItemClick(position);
                }
            }
        });

        OtherLoanQryBean bean = list.get(position);

        //第一行信息
        viewHolder.tvDateAndAmount.setText(bean.getFirstLineInfo());

        //第二行左侧信息
        String tempInfo = bean.getSecondLineLeftInfo();
//        if(tempInfo != null){
//            if(tempInfo.equals(context.getResources().getString(R.string.boc_loan_applied_status_success))){
//                viewHolder.tvResult.setBackgroundResource(R.drawable.boc_loan_status_bg_green);
//            }
//            else if(tempInfo.equals(context.getResources().getString(R.string.boc_loan_applied_status_fail))){
//                viewHolder.tvResult.setBackgroundResource(R.drawable.boc_loan_status_bg_red);
//            }
//            else{
//                viewHolder.tvResult.setBackgroundResource(R.drawable.boc_loan_status_bg_gray);
//            }
//        }
//        else{
//            viewHolder.tvResult.setBackgroundResource(R.drawable.boc_loan_status_bg_gray);
//        }

        if(tempInfo != null){
            if(tempInfo.equals(context.getResources().getString(R.string.boc_loan_applied_status_success))){
                viewHolder.tvResult.setTextColor(context.getResources().getColor(R.color.boc_text_color_green));
            }
            else if(tempInfo.equals(context.getResources().getString(R.string.boc_loan_applied_status_fail))){
                viewHolder.tvResult.setTextColor(context.getResources().getColor(R.color.boc_text_color_red));
            }
            else{
                viewHolder.tvResult.setTextColor(context.getResources().getColor(R.color.boc_text_color_gray));
            }
        }
        else{
            viewHolder.tvResult.setTextColor(context.getResources().getColor(R.color.boc_text_color_gray));
        }
        viewHolder.tvResult.setText(bean.getSecondLineLeftInfo());

        //第二行右侧第一条信息
        viewHolder.tvMoneyType.setText(bean.getSecondLinerightFirstInfo());

        //第二行右侧第二条信息
        viewHolder.tvMoneySum.setText(bean.getSecondLinerightSecInfo());
        return convertView;
    }

    public void setOnClickListener(ClickListener listener){
        mListener = listener;
    }

    public interface ClickListener {
        void onItemClick(int position);
    }

    class ViewHolder{
        //第一行信息
        TextView tvDateAndAmount;
        //第二行左侧信息
        TextView tvResult;
        //第二行右侧第一条信息
        TextView tvMoneyType;
        //第二行右侧第二条信息
        TextView tvMoneySum;
    }
}
