package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.finance;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.finance.PledgeProductBean;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseListAdapter;

public class PledgeFinanceProductAdapter extends BaseListAdapter<PledgeProductBean> {
    public PledgeFinanceProductAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PledgeProductBean item = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.boc_item_pledge_finance_product, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //给viewHolder中的控件设置数据
        viewHolder.tvName.setText(item.getProdName());
        String toDate = item.getProdEnd() == null ? ""
                : item.getProdEnd().format(DateFormatters.dateFormatter1);
        viewHolder.tvTodate.setText(
                String.format(mContext.getString(R.string.boc_pledge_finance_product_todate),
                        toDate));
        viewHolder.tvCurrency.setText(PublicCodeUtils.getCurrency(mContext, item.getCurCode()));
        viewHolder.tvMoney.setText(
                MoneyUtils.transMoneyFormatNoLossAccuracy(item.getAvailableQuantity(), item.getCurCode()));
        return convertView;
    }

    private class ViewHolder {

        protected TextView tvName;
        protected TextView tvTodate;
        protected TextView tvMoney;
        protected TextView tvCurrency;

        ViewHolder(View itemView) {
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvTodate = (TextView) itemView.findViewById(R.id.tv_todate);
            tvMoney = (TextView) itemView.findViewById(R.id.tv_money);
            tvCurrency = (TextView) itemView.findViewById(R.id.tv_currency);
        }
    }
}
