package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.productrecommend.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.productrecommend.ItemOnClickListener;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.productrecommend.model.RecommendModel;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseListAdapter;
import com.boc.bocsoft.mobile.framework.widget.listview.ListAdapterHelper;

/**
 * 理财——商品推荐数据适配器
 * Created by Wan mengxin on 2016/10/16.
 */
public class RecommendAdapter extends BaseListAdapter<RecommendModel.OcrmDetail> {

    public RecommendAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.boc_fragment_list_commend_item, null);
            holder = new ViewHolder();
            holder.productName = (TextView) convertView.findViewById(R.id.productName);
            holder.productCode = (TextView) convertView.findViewById(R.id.productCode);
            holder.tradeCount = (TextView) convertView.findViewById(R.id.tradeCount);
            holder.tradeSum = (TextView) convertView.findViewById(R.id.tradeSum);
            holder.clickBtn = (TextView) convertView.findViewById(R.id.clickBtn);
            holder.empName = (TextView) convertView.findViewById(R.id.empName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RecommendModel.OcrmDetail item = getItem(position);
        holder.productName.setText(item.getProductName());
        holder.productCode.setText("(" + item.getProductCode() + ")");
        holder.empName.setText("理财经理  " + item.getEmpcName());

        String code = item.getCurrencyCode();
        String charcode;

        if ("1".equals(item.getCharCode())) {
            charcode = "现钞";
        } else {
            charcode = "现汇";
        }
        if ("05".equals(item.getTransType()) || "07".equals(item.getTransType())) {
            if ("0".equals(item.getIsPre())) {
                holder.clickBtn.setText(mContext.getResources().getString(R.string.recommend_main_btn_value3));
                holder.clickBtn.setTextColor(mContext.getResources().getColor(R.color.boc_text_color_red));
                if ("1".equals(item.getAmountType())) {
                    holder.tradeSum.setText(mContext.getResources().getString(R.string.recommend_main_tradesum_value1)); //交易金额	String
                    holder.tradeCount.setText(item.getTransCount());
                } else {
                    holder.tradeCount.setText(mContext.getResources().getString(R.string.recommend_main_trade));
                    if ("001".equals(item.getCurrencyCode())) {
                        holder.tradeSum.setText(item.getTransSum() + "  " + "元");
                    } else {
                        holder.tradeSum.setText(MoneyUtils.transMoneyFormat(item.getTransSum(), item.getCurrencyCode()) + "  " + PublicCodeUtils.getCurrency(mContext, code) + " " + charcode);
                    }
                }
            } else {
                holder.tradeCount.setText(mContext.getResources().getString(R.string.recommend_main_trade));
                holder.clickBtn.setText(mContext.getResources().getString(R.string.recommend_main_btn_value1));
                holder.clickBtn.setTextColor(mContext.getResources().getColor(R.color.boc_text_color_red));
                if ("001".equals(item.getCurrencyCode())) {
                    holder.tradeSum.setText(item.getTransSum() + "  " + "元");
                } else {
                    holder.tradeSum.setText(MoneyUtils.transMoneyFormat(item.getTransSum(), item.getCurrencyCode()) + "  " + PublicCodeUtils.getCurrency(mContext, code) + " " + charcode);
                }
            }
        } else if ("06".equals(item.getTransType()) || "08".equals(item.getTransType())) {
            holder.tradeSum.setText(mContext.getResources().getString(R.string.recommend_main_trade)); //交易金额	String
            holder.tradeCount.setText(item.getTransCount());  //交易份额	String
            holder.clickBtn.setText(mContext.getResources().getString(R.string.recommend_main_btn_value2));
            holder.clickBtn.setTextColor(mContext.getResources().getColor(R.color.boc_main_button_color));
        }
        setOnClickChildViewInItemItf(position, item, holder.productCode);
        setOnClickChildViewInItemItf(position, item, holder.clickBtn);
        return convertView;
    }

    public void setOnViewClickListener(final ItemOnClickListener itemOnClickListener) {
        setOnClickChildViewInItemItf(
                new ListAdapterHelper.OnClickChildViewInItemItf<RecommendModel.OcrmDetail>() {
                    @Override
                    public void onClickChildViewInItem(int position, RecommendModel.OcrmDetail item, View childView) {
                        if (childView.getId() == R.id.productCode) {
                            itemOnClickListener.onClickerCode(position, item, childView);
                        } else if (childView.getId() == R.id.clickBtn) {
                            itemOnClickListener.onClickerBtn(position, item, childView);
                        }
                    }
                });
    }

    static class ViewHolder {
        public TextView productName;
        public TextView productCode;
        public TextView tradeCount;
        public TextView tradeSum;
        public TextView clickBtn;
        public TextView empName;
    }
}
