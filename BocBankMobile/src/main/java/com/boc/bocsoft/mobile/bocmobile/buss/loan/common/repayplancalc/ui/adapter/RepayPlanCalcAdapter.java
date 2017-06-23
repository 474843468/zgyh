package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplancalc.ui.adapter;

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
 * 还款计划试算公共组件适配器
 * <p/>
 * Created by liuzc on 2016/8/30.
 */
public class RepayPlanCalcAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;

    //页面显示信息列表
    private List<RepayPlanCalcBean> list;

    public RepayPlanCalcAdapter(Context context) {
        this.context = context;
        list = new ArrayList<RepayPlanCalcBean>();
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<RepayPlanCalcBean> list) {
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
            convertView = inflater.inflate(R.layout.list_item_repay_plan_calc, null);

            viewHolder.tvDate = (TextView)convertView.findViewById(R.id.tvDate);;
            viewHolder.tvAmount = (TextView)convertView.findViewById(R.id.tvAmount);
            viewHolder.tvCapital = (TextView)convertView.findViewById(R.id.tvCapital);
            viewHolder.tvInterest = (TextView)convertView.findViewById(R.id.tvInterest);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        RepayPlanCalcBean bean = list.get(position);

        //序号
        viewHolder.tvDate.setText(bean.getRepayDate());

        //第一行信息
        viewHolder.tvAmount.setText(bean.getRepayAmount());

        //第二行信息
        viewHolder.tvCapital.setText(bean.getRepayCapital());
        viewHolder.tvInterest.setText(bean.getRepayInt());

        return convertView;
    }

    class ViewHolder{
        TextView tvDate;//序号
        TextView tvAmount; //日期和金额
        TextView tvCapital; //本金
        TextView tvInterest; //利息
    }

}
