package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundguessyoulike.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundguessyoulike.bean.FundListBean;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseRecycleViewAdapter;

import java.util.List;

/**
 * Created by zcy7065 on 2016/11/30.
 */
public class FundGuessYouLikeAdapter extends BaseRecycleViewAdapter<FundListBean,FundGuessYouLikeAdapter.ViewHolder>{

    public FundGuessYouLikeAdapter(Context context, List<FundListBean> datas) {
        super(context);
        mList = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.boc_fund_item_gussyoulike, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        FundListBean fundListBean = getItem(i);
        viewHolder.tvProduct.setText(fundListBean.getFundName());
        viewHolder.tvProfit.setText(fundListBean.getAddUpNetVal());
        viewHolder.tvPeriod.setText(fundListBean.getAlwRdptDat());
    }

    protected  class ViewHolder extends BaseRecycleViewAdapter.BaseViewHolder{
        TextView tvProduct,tvProfit,tvPeriod;

        public ViewHolder(View itemView) {
            super(itemView);
            tvProduct = (TextView) itemView.findViewById(R.id.tv_product);
            tvProfit = (TextView) itemView.findViewById(R.id.tv_profit);
            tvPeriod = (TextView) itemView.findViewById(R.id.tv_period);
        }
    }
}
