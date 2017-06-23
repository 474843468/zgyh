package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryQuotePrice.PsnFessQueryQuotePriceResult;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页牌价适配器
 * Created by gwluo on 2016/12/29.
 */

public class CashRemitAdapter extends BaseAdapter {
    private Context mContext;

    public CashRemitAdapter(Context context) {
        mContext = context;
    }

    private List<PsnFessQueryQuotePriceResult.QuotePrice> list = new ArrayList<>();

    public void updata(List<PsnFessQueryQuotePriceResult.QuotePrice> data) {
        list = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.boc_item_buysell_exchange, null);
            holder.iv_national_flag = (ImageView) convertView.findViewById(R.id.iv_national_flag);
            holder.tv_currency_china = (TextView) convertView.findViewById(R.id.tv_currency_china);
            holder.tv_currency_letter = (TextView) convertView.findViewById(R.id.tv_currency_letter);
            holder.tv_sell_cash = (TextView) convertView.findViewById(R.id.tv_sell_cash);
            holder.tv_sell_remit = (TextView) convertView.findViewById(R.id.tv_sell_remit);
            holder.tv_buy_cash = (TextView) convertView.findViewById(R.id.tv_buy_cash);
            holder.tv_buy_remit = (TextView) convertView.findViewById(R.id.tv_buy_remit);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PsnFessQueryQuotePriceResult.QuotePrice price = list.get(position);
        //TODO 设置图片
        holder.iv_national_flag.setBackgroundResource(R.drawable.ic_launcher);
        holder.tv_currency_china.setText(PublicCodeUtils.getCurrency(mContext, price.getCurrency()));
        //TODO 设置数据
        holder.tv_currency_letter.setText("");
        //TODO 钞汇为空，gone.格式化牌价
        holder.tv_sell_cash.setText(price.getSellNoteRate());
        holder.tv_sell_remit.setText(price.getSellRate());
        holder.tv_buy_cash.setText(price.getBuyNoteRate());
        holder.tv_buy_remit.setText(price.getBuyRate());
        return convertView;
    }

    public class ViewHolder {
        TextView tv_currency_china, tv_currency_letter, tv_sell_cash, tv_sell_remit, tv_buy_cash, tv_buy_remit;
        ImageView iv_national_flag;
    }
}
