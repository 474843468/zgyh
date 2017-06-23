package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailListQueryViewModel;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseListAdapter;

import java.util.List;

/**
 * Created by hty7062 on 2016/12/2.
 */
public class SelectMarginAccountAdapter extends BaseListAdapter<VFGBailListQueryViewModel>{


    private List<VFGBailListQueryViewModel.BailItemEntity> listVFGBailListQueryViewModel;
    public SelectMarginAccountAdapter(Context context, List<VFGBailListQueryViewModel.BailItemEntity> list) {
        super(context);
        listVFGBailListQueryViewModel = list;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_select_margin_account, null);
        }

        ViewHolder holder = ViewHolder.getHolder(convertView);
        holder.marginAccountCurrency.setText("[" + PublicCodeUtils.getCurrency(mContext,listVFGBailListQueryViewModel.get(position).getSettleCurrency()) + "]");
        holder.marginAccount.setText(NumberUtils.formatCardNumberStrong(listVFGBailListQueryViewModel.get(position).getMarginAccountNo()));

        return convertView;
    }

    static class ViewHolder {
        TextView marginAccountCurrency;
        TextView marginAccount;

        public ViewHolder(View convertView) {
            marginAccountCurrency = (TextView) convertView.findViewById(R.id.tv_currency);
            marginAccount = (TextView) convertView.findViewById(R.id.tv_account);
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
