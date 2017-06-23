package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.model.XpadQueryGuarantyProductViewModel;

import java.util.List;

/**
 * 组合购买已押押品查询
 * Created by zhx on 2016/9/22
 */
public class GuarantyProductAdapter extends BaseAdapter {
    private Context context;
    List<XpadQueryGuarantyProductViewModel.GuarantyProductEntity> guarantyProductList;

    public GuarantyProductAdapter(Context context, List<XpadQueryGuarantyProductViewModel.GuarantyProductEntity> guarantyProductList) {
        this.context = context;
        this.guarantyProductList = guarantyProductList;
    }

    @Override
    public int getCount() {
        if (guarantyProductList != null && guarantyProductList.size() > 0) {
            return guarantyProductList.size();
        }
        return 0;
    }

    @Override
    public XpadQueryGuarantyProductViewModel.GuarantyProductEntity getItem(int position) {
        return guarantyProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.list_item_wealth_guaranty_product, null);
        }
        ViewHolder viewHolder = ViewHolder.getHolder(convertView);

        String prodName = getItem(position).getProdName();
        String freezeUnit = getItem(position).getFreezeUnit();
        String impawnPermit = "";
        String permit = getItem(position).getImpawnPermit();
        if("0".equals(permit)) {
            impawnPermit = "未成交";
        } else if("1".equals(permit)) {
            impawnPermit = "成交成功";
        } else if("2".equals(permit)) {
            impawnPermit = "成交失败";
        }
        viewHolder.tv_item.setText(prodName + " " + MoneyUtils.transMoneyFormat(freezeUnit, getItem(position).getCurCode()) + " (" + impawnPermit + ")");
        return convertView;
    }

    static class ViewHolder {
        TextView tv_item;

        public ViewHolder(View convertView) {
            tv_item = (TextView) convertView.findViewById(R.id.tv_item);
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