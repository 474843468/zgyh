package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.recommend.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOcrmProductQuery.PsnOcrmProductQueryResult;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.model.FundCompanyListViewModel;

/**
 * 基金推荐列表适配器
 * Created by liuzc on 2016/12/26.
 */
public class FundRecommendListAdapter extends BaseAdapter {
    private Context mContext;
    private PsnOcrmProductQueryResult viewModel; //view层的数据，内含有列表

    public FundRecommendListAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(PsnOcrmProductQueryResult value) {
        viewModel = value;
    }

    @Override
    public int getCount() {
        if(viewModel == null || viewModel.getResultList() == null){
            return 0;
        }
        return viewModel.getResultList().size();
    }

    @Override
    public PsnOcrmProductQueryResult.OcrmDetail getItem(int position) {
        if(viewModel == null || viewModel.getResultList() == null){
            return null;
        }
        return viewModel.getResultList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.boc_item_fund_recommend, null);

            holder.tvFundName = (TextView)convertView.findViewById(R.id.tvFundName);
            holder.tvFundCode = (TextView)convertView.findViewById(R.id.tvFundCode);
            holder.tvTradeCount = (TextView)convertView.findViewById(R.id.tvTradeCount);
            holder.tvTradeAmount = (TextView)convertView.findViewById(R.id.tvTradeAmount);
            holder.tvDwjz = (TextView)convertView.findViewById(R.id.tvDwjz);
            holder.tvAction = (TextView)convertView.findViewById(R.id.tvAction);
            holder.tvWealthManager = (TextView)convertView.findViewById(R.id.tvWealthManager);
            holder.tvTradeCode = (TextView)convertView.findViewById(R.id.tvTradeCode);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //获取当前条目的数据
        PsnOcrmProductQueryResult.OcrmDetail curBean = viewModel.getResultList().get(position);

//        String name = curBean.get();
//        holder.tvFundName.setText(name);

        return convertView;
    }

    private class ViewHolder {
        private TextView tvFundName; //基金名称
        private TextView tvFundCode; //基金代码
        private TextView tvTradeCount; //交易份额
        private TextView tvTradeAmount; //基金金额
        private TextView tvDwjz; //单位净值
        private TextView tvAction; //基金操作：购买、赎回等
        private TextView tvWealthManager; //理财经理
        private TextView tvTradeCode; //交易编码
    }

}
