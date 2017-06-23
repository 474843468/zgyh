package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui.bean.FundFloatingProfileLossBean;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by taoyongzhen on 2016/11/30.
 */

public class FundFloatingProfileLossListAdapter extends BaseAdapter {

    private Context mContect;

    private List<FundFloatingProfileLossBean> beans;

    public FundFloatingProfileLossListAdapter(Context mContect) {
        this.mContect = mContect;
    }
    public List<FundFloatingProfileLossBean> getBeans() {
        return beans;
    }

    public void setBeans(List<FundFloatingProfileLossBean> beans) {
        this.beans = beans;
        notifyDataSetChanged();
    }



    @Override
    public int getCount() {
        return beans == null ? 0:beans.size();
    }

    @Override
    public Object getItem(int position) {
        return beans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
//        convertView的初始化
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)mContect.getSystemService(LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.boc_view_fund_profilefloatloss_item,parent,false);
            holder = new ViewHolder();
            holder.tvFundInfo = (TextView) convertView.findViewById(R.id.tv_left_value);
            holder.tvAmount = (TextView) convertView.findViewById(R.id.tv_right_first);
            holder.tvCurrency = (TextView) convertView.findViewById(R.id.tv_right_second);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        FundFloatingProfileLossBean bean = beans.get(position);
        String fundRegName = bean.getLeftContent();
        holder.tvFundInfo.setText(fundRegName);
        String taAccountNo = bean.getRightFirstContent();
        if (taAccountNo.startsWith(DataUtils.NUM_POSITIVE_FLAG)){
            holder.tvAmount.setTextColor(mContect.getResources().getColor(R.color.boc_text_color_green));
        }else{
            holder.tvAmount.setTextColor(mContect.getResources().getColor(R.color.boc_text_money_color_red));
        }
        holder.tvAmount.setText(taAccountNo);
        String accountStatus = bean.getRightSecondContent();
        holder.tvCurrency.setText(accountStatus);
        return convertView;
    }

    private static class ViewHolder {
        public TextView tvFundInfo;
        public TextView tvAmount;
        public TextView tvCurrency;
    }
}
