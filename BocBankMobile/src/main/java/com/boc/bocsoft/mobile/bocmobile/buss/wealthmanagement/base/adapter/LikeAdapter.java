package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseRecycleViewAdapter;

import java.util.List;

/**
 * @author wangyang
 *         2016/11/12 22:03
 *         猜你喜欢Adaoter
 */
public class LikeAdapter extends BaseRecycleViewAdapter<WealthListBean,LikeAdapter.ViewHolder> {

    public LikeAdapter(Context context, List<WealthListBean> datas) {
        super(context);
        mList = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.boc_item_recommend, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        WealthListBean wealthListBean = getItem(i);
        viewHolder.tvProduct.setText(wealthListBean.getProdName());
        viewHolder.tvProfit.setText(wealthListBean.getYearlyRR());
        viewHolder.tvPeriod.setText(wealthListBean.getPeriedTime());
    }

    protected class ViewHolder extends BaseRecycleViewAdapter.BaseViewHolder{
        TextView tvProduct,tvProfit,tvPeriod;

        public ViewHolder(View itemView) {
            super(itemView);
            tvProduct = (TextView) itemView.findViewById(R.id.tv_product);
            tvProfit = (TextView) itemView.findViewById(R.id.tv_profit);
            tvPeriod = (TextView) itemView.findViewById(R.id.tv_period);
        }
    }
}
