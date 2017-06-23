package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.model.FundCompanyListViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.ui.SyncHorizontalScrollView;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.querymultiplefund.WFSSQueryMultipleFundResult;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 基金公司列表适配器
 * Created by liuzc on 2016/11/29.
 */
public class FundCompanyListAdapter extends BaseAdapter {
    private Context mContext;
    private FundCompanyListViewModel viewModel; //view层的数据，内含有列表

    public FundCompanyListAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(FundCompanyListViewModel value) {
        viewModel = value;
    }


    @Override
    public int getCount() {
        return viewModel.getList().size();
    }

    @Override
    public FundCompanyListViewModel.ListBean getItem(int position) {
        return viewModel.getList().get(position);
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
            convertView = View.inflate(mContext, R.layout.list_item_fund_product_alltype, null);

            holder.tvFundName = (TextView)convertView.findViewById(R.id.tvFundName);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //获取当前条目的数据
        FundCompanyListViewModel.ListBean curBean = viewModel.getList().get(position);

        String name = curBean.getFundCompanyName();
        holder.tvFundName.setText(name);

        return convertView;
    }

    private class ViewHolder {
        private TextView tvFundName; //基金名称

    }

}
