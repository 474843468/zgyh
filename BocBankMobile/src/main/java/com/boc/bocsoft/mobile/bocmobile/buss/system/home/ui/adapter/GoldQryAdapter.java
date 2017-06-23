package com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.FundBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.GoldBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseListAdapter;

/**
 * 贵金属adapter Created by gwluo on 2016/8/31.
 */
public class GoldQryAdapter extends BaseListAdapter<GoldBean> {
    private Context mContext;

    public GoldQryAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GoldBean item = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            TextView fundView = new TextView(mContext);
            fundView.setTextAppearance(mContext, R.style.tv_order_item);
            fundView.setTextColor(mContext.getResources().getColor(R.color.boc_text_color_dark_gray));
            int padSize = PublicUtils.dip2px(mContext, 10);
            fundView.setPadding(padSize, padSize, padSize, padSize);
            convertView = fundView;
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.currency.setText(PublicCodeUtils.getGoldCurrencyCode(mContext, item.getSourceCurrencyCode())
                + "/" + PublicCodeUtils.getGoldCurrencyCode(mContext, item.getTargetCurrencyCode()));
        return convertView;
    }

    private class ViewHolder {

        TextView currency;

        ViewHolder(View itemView) {
            currency = (TextView) itemView;
        }
    }
}
