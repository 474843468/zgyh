package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadQueryGuarantyProductResultModel;

import java.util.List;

/**
 * Created by zc7067 on 2016/9/26.
 * 历史组合购买详情——被组合产品列表
 * @des ${TODO}
 */

public class HistoryCombinationProductAdapter extends BaseAdapter {
    private Context context;
    List<XpadQueryGuarantyProductResultModel.QueryGuarantyProductResultEntity> comProductLis;
    private String impawnPermit;

    public HistoryCombinationProductAdapter(Context context, List<XpadQueryGuarantyProductResultModel.QueryGuarantyProductResultEntity> comProductList) {
        this.context = context;
        this.comProductLis = comProductList;
    }

    @Override
    public int getCount() {
        if (comProductLis != null && comProductLis.size() > 0) {
            return comProductLis.size();
        }
        return 0;
    }


    @Override
    public XpadQueryGuarantyProductResultModel.QueryGuarantyProductResultEntity getItem(int position) {
        return comProductLis.get(position);
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
        String freezeUnit = MoneyUtils.transMoneyFormat(getItem(position).getFreezeUnit(),getItem(position).getCurCode());

//         组合购买状态（0：未成交 1：成交成功 2：成交失败）
        if("0".equals(getItem(position).getImpawnPermit())){
            impawnPermit="未成交";
            viewHolder.tv_item.setText(prodName + "  " + freezeUnit+" ("+impawnPermit+")");
        }else if("1".equals(getItem(position).getImpawnPermit())){
            viewHolder.tv_item.setText(prodName + "  " + freezeUnit);
        }
        else if("2".equals(getItem(position).getImpawnPermit())){
            viewHolder.tv_item.setText(prodName + "  " + freezeUnit);
        }

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
