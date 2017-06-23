package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailListQueryViewModel;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.List;

/**
 * Adapter：双向宝-账户管理-变更交易账户-原保证金列表
 * Created by zhx on 2016/12/19
 */
public class SelectBailAdapter extends BaseAdapter {
    private Context context;
    private List<VFGBailListQueryViewModel.BailItemEntity> oldList; // “原交易账户”保证金列表
    private List<String> filterCurrenyList;

    public SelectBailAdapter(Context context, List<VFGBailListQueryViewModel.BailItemEntity> oldList) {
        this.context = context;
        this.oldList = oldList;
    }

    public void setFilterCurrenyList(List<String> filterCurrenyList) {
        this.filterCurrenyList = filterCurrenyList;
    }

    @Override
    public int getCount() {
        if (oldList != null) {
            return oldList.size();
        }
        return 0;
    }

    @Override
    public VFGBailListQueryViewModel.BailItemEntity getItem(int position) {
        return oldList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.list_item_bail_product1, null);
        }
        final ViewHolder viewHolder = ViewHolder.getHolder(convertView);

        final VFGBailListQueryViewModel.BailItemEntity item = getItem(position);
        // 币种
        String currency = PublicCodeUtils.getCurrency(context, item.getSettleCurrency());
        viewHolder.tv_currency.setText("[" + currency + "]");
        // 保证金账号
        String marginAccountNo = item.getMarginAccountNo();
        int index = marginAccountNo.indexOf("00000");
        if (index == 0) {
            String substring = marginAccountNo.substring(5, marginAccountNo.length());
            viewHolder.tv_margin_account_no.setText(NumberUtils.formatCardNumber(substring));
        } else {
            viewHolder.tv_margin_account_no.setText(NumberUtils.formatCardNumber(marginAccountNo));
        }

        // 是否显示选中按钮
        boolean isContain = filterCurrenyList.contains(item.getSettleCurrency());
        viewHolder.iv_check.setVisibility(isContain ? View.GONE : View.VISIBLE);

        // 是否选中
        viewHolder.iv_check.setImageResource(item.isChecked() ? R.drawable.checkbox_checked : R.drawable.checkbox_normal);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //<editor-fold desc="如果新交易账户包含指定币种的保证金，那么不能选">
                if (filterCurrenyList.contains(item.getSettleCurrency())) {
                    return;
                }
                //</editor-fold>

                //<editor-fold desc="切换条目的选中状态">
                item.setChecked(!item.isChecked());
                viewHolder.iv_check.setImageResource(item.isChecked() ? R.drawable.checkbox_checked : R.drawable.checkbox_normal);
                //</editor-fold>
            }
        });

        return convertView;
    }

    static class ViewHolder {
        ImageView iv_check;
        TextView tv_currency;
        TextView tv_margin_account_no;

        public ViewHolder(View convertView) {
            iv_check = (ImageView) convertView.findViewById(R.id.iv_check);
            tv_currency = (TextView) convertView.findViewById(R.id.tv_currency);
            tv_margin_account_no = (TextView) convertView.findViewById(R.id.tv_margin_account_no);
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
