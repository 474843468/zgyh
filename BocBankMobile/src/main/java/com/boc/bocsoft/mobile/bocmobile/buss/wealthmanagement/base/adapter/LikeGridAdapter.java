package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.model.PurchaseModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.utils.ResultConvertUtils;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseListAdapter;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wangyang
 *         2016/11/12 22:03
 *         猜你喜欢Adaoter
 */
public class LikeGridAdapter extends BaseListAdapter<WealthListBean> {

    public LikeGridAdapter(Context context, List<WealthListBean> beans) {
        super(context);
        mList = beans;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.boc_item_recommend, null);
            viewHolder = new ViewHolder();
            viewHolder.tvProduct = (TextView) convertView.findViewById(R.id.tv_product);
            viewHolder.tvProfit = (TextView) convertView.findViewById(R.id.tv_profit);
            viewHolder.tvPeriod = (TextView) convertView.findViewById(R.id.tv_period);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        WealthListBean wealthListBean = getItem(position);
        viewHolder.tvProduct.setText(wealthListBean.getProdName());

        if (PurchaseModel.PRODUCT_KIND_ZERO.equals(wealthListBean.getProductKind()))
            viewHolder.tvProfit.setText(ResultConvertUtils.convertRate(wealthListBean.getYearlyRR(), wealthListBean.getRateDetail()));
        else
            viewHolder.tvProfit.setText(MoneyUtils.getRoundNumber(wealthListBean.getPrice(), 4, BigDecimal.ROUND_HALF_UP));

        viewHolder.tvPeriod.setText(ResultConvertUtils.convertDate(wealthListBean.getProductKind(), wealthListBean.getPeriedTime(), wealthListBean.getIsLockPeriod(), wealthListBean.getTermType()));

        return convertView;
    }

    private static class ViewHolder {
        TextView tvProduct, tvProfit, tvPeriod;
    }
}