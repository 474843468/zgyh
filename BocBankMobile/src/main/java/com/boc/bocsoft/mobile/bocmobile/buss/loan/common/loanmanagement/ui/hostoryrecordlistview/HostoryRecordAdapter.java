package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.ui.hostoryrecordlistview;

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
 */
public class HostoryRecordAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;

    //页面显示信息列表
    private List<HostoryRecordBean> list;

    public HostoryRecordAdapter(Context context) {
        this.context = context;
        list = new ArrayList<HostoryRecordBean>();
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<HostoryRecordBean> list) {
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
            convertView = inflater.inflate(R.layout.boc_item_loan_hostory_record, null);

            viewHolder.loanName = (TextView)convertView.findViewById(R.id.tv_todate);;
            viewHolder.tvDate = (TextView)convertView.findViewById(R.id.tv_period);
            viewHolder.currencyCode = (TextView)convertView.findViewById(R.id.tv_currency);
            viewHolder.count = (TextView)convertView.findViewById(R.id.tv_money);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        HostoryRecordBean bean = list.get(position);

        //序号
        viewHolder.loanName.setText(bean.getFirstLineTopInfo());

        //第一行信息
        viewHolder.tvDate.setText(bean.getFirstLineBottomInfo());

        //第二行信息
        viewHolder.currencyCode.setText(bean.getSecondLineLeftInfo());
        viewHolder.count.setText(bean.getSecondLinerightInfo());

        return convertView;
    }

    class ViewHolder{
        TextView loanName;//贷款名称
        TextView tvDate; //贷款日期
        TextView currencyCode; //人民币元
        TextView count; //账户金额
    }

}
