package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.ui.repayplanlistview;

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
 * 还款计划公共组件适配器
 * <p/>
 * Created by liuzc on 2016/8/10.
 */
public class RepayPlanAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;

    //页面显示信息列表
    private List<RepayPlanBean> list;

    public RepayPlanAdapter(Context context) {
        this.context = context;
        list = new ArrayList<RepayPlanBean>();
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<RepayPlanBean> list) {
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
            convertView = inflater.inflate(R.layout.list_item_repay_plan_query, null);

            viewHolder.tvNumer = (TextView)convertView.findViewById(R.id.tvNumber);;
            viewHolder.tvDateAndAmount = (TextView)convertView.findViewById(R.id.tvDateAndAmount);
            viewHolder.tvCapital = (TextView)convertView.findViewById(R.id.tvCapital);
            viewHolder.tvInterest = (TextView)convertView.findViewById(R.id.tvInterest);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        RepayPlanBean bean = list.get(position);

        //序号
        viewHolder.tvNumer.setText(String.format("%d", position + 1));

        //第一行信息
        viewHolder.tvDateAndAmount.setText(bean.getFirstLineInfo());

        //第二行信息
        viewHolder.tvCapital.setText(bean.getSecondLineLeftInfo());
        viewHolder.tvInterest.setText(bean.getSecondLinerightInfo());

        return convertView;
    }

    class ViewHolder{
        TextView tvNumer;//序号
        TextView tvDateAndAmount; //日期和金额
        TextView tvCapital; //本金
        TextView tvInterest; //利息
    }

}
