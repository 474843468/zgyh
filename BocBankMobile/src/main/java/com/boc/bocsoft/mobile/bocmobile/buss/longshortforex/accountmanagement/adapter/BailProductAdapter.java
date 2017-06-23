package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailProductQueryViewModel;

import java.math.BigDecimal;
import java.util.List;

/**
 * Adapter：双向宝-账户管理-新增保证金账户-可签约保证金产品列表
 * Created by zhx on 2016/12/8
 */
public class BailProductAdapter extends BaseAdapter {
    private Context context;
    private List<VFGBailProductQueryViewModel.VFGBailProduct> list;
    private int currentSelectIndex = -1;
    private List<String> filterCurrenyList;

    public BailProductAdapter(Context context, List<VFGBailProductQueryViewModel.VFGBailProduct> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(List<VFGBailProductQueryViewModel.VFGBailProduct> list) {
        this.list = list;
    }

    public List<String> getFilterCurrenyList() {
        return filterCurrenyList;
    }

    public void setFilterCurrenyList(List<String> filterCurrenyList) {
        this.filterCurrenyList = filterCurrenyList;
    }

    public void setCurrentSelectIndex(int currentSelectIndex) {
        this.currentSelectIndex = currentSelectIndex;
    }

    public int getCurrentSelectIndex() {
        return currentSelectIndex;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public VFGBailProductQueryViewModel.VFGBailProduct getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.list_item_bail_product, null);
        }
        ViewHolder viewHolder = ViewHolder.getHolder(convertView);

        VFGBailProductQueryViewModel.VFGBailProduct item = getItem(position);

        String currency = PublicCodeUtils.getCurrency(context, item.getSettleCurrency());
        viewHolder.tv_currency.setText("[" + currency + "]"); // 结算货币
        viewHolder.tv_bail_name.setText(item.getBailCName()); // 保证金产品名称
        String bailNo = item.getBailNo();
        viewHolder.tv_bail_no.setText("(" + bailNo + ")"); // 保证金产品序号
        viewHolder.tv_liquidation_ratio.setText(getRate(item.getLiquidationRatio())); // 斩仓比例
        viewHolder.tv_need_margin_ratio.setText(getRate(item.getNeedMarginRatio())); // 交易所需保证金比例
        viewHolder.tv_warn_ratio.setText(getRate(item.getWarnRatio())); // 报警比例
        viewHolder.tv_open_rate.setText(getRate(item.getOpenRate())); // 开仓充足率

        // 是否是当前选中的条目
        viewHolder.iv_select_item.setImageResource(position == currentSelectIndex ? R.drawable.ic_single_selected : R.drawable.ic_single_unselected);


        if (!filterCurrenyList.contains(item.getSettleCurrency())) { // 如果filterCurrenyList中不包含该币种
            convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
            viewHolder.iv_select_item.setVisibility(View.VISIBLE);
            viewHolder.iv_is_sign.setVisibility(View.GONE);
        } else {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.boc_main_btn_bg_color));
            viewHolder.iv_select_item.setVisibility(View.GONE);
            viewHolder.iv_is_sign.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    // 获取百分比
    private String getRate(BigDecimal rate) {
        if (rate == null) {
            return "";
        }

        int rateInt = (int) (rate.doubleValue() * 100);
        String rateStr = rateInt + "%";
        return rateStr;
    }

    static class ViewHolder {
        ImageView iv_is_sign;
        ImageView iv_select_item;
        TextView tv_currency;
        TextView tv_bail_name;
        TextView tv_bail_no;
        TextView tv_liquidation_ratio;
        TextView tv_need_margin_ratio;
        TextView tv_warn_ratio;
        TextView tv_open_rate;

        public ViewHolder(View convertView) {
            iv_is_sign = (ImageView) convertView.findViewById(R.id.iv_is_sign);
            iv_select_item = (ImageView) convertView.findViewById(R.id.iv_select_item);
            tv_currency = (TextView) convertView.findViewById(R.id.tv_currency);
            tv_bail_name = (TextView) convertView.findViewById(R.id.tv_bail_name);
            tv_bail_no = (TextView) convertView.findViewById(R.id.tv_bail_no);
            tv_liquidation_ratio = (TextView) convertView.findViewById(R.id.tv_liquidation_ratio);
            tv_need_margin_ratio = (TextView) convertView.findViewById(R.id.tv_need_margin_ratio);
            tv_warn_ratio = (TextView) convertView.findViewById(R.id.tv_warn_ratio);
            tv_open_rate = (TextView) convertView.findViewById(R.id.tv_open_rate);
        }

        public static ViewHolder getHolder(View convertView) {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            return viewHolder;
        }
    }
}
