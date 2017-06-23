package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.mypurchase.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.mypurchase.model.XpadPsnVFGPositionInfoModel;

/**
 * Created by Administrator on 2017/1/6 0006.
 */

public class MypurchaseAdapter extends BaseAdapter {
    private XpadPsnVFGPositionInfoModel xpadPsnVFGPositionInfoModel;
    private   Context context;
    public MypurchaseAdapter(Context conntext,XpadPsnVFGPositionInfoModel xpadPsnVFGPositionInfoMode){
            this.xpadPsnVFGPositionInfoModel=xpadPsnVFGPositionInfoMode;
             this.context=context;
    }
    @Override
    public int getCount() {
        return xpadPsnVFGPositionInfoModel.getDetails().size();
    }

    @Override
    public XpadPsnVFGPositionInfoModel.DetailsEntity getItem(int position) {
        return xpadPsnVFGPositionInfoModel.getDetails().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.boc_item_mypurchase,null);
            holder = new ViewHolder();
            holder.currencytype= (TextView) convertView.findViewById(R.id.currencytype);
            holder.balancetype= (TextView) convertView.findViewById(R.id.balancetype);

            holder.buyprice= (TextView) convertView.findViewById(R.id.tv_buyprice);
            holder.banlance= (TextView) convertView.findViewById(R.id.tv_banlance);
            holder.profitorloss= (TextView) convertView.findViewById(R.id.tv_profitorloss);
            holder.saleprice= (TextView) convertView.findViewById(R.id.tv_saleprice);
            holder.salehavebanlance= (TextView) convertView.findViewById(R.id.tv_salehavebanlance);
            holder.saleprofitorloss= (TextView) convertView.findViewById(R.id.tv_saleprofitorloss);

            holder.ll_buy = (LinearLayout) convertView.findViewById(R.id.ll_buy);
            holder.sale_layout = (LinearLayout) convertView.findViewById(R.id.sale_layout);


            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        XpadPsnVFGPositionInfoModel.DetailsEntity  detailsEntity = getItem(position);

        XpadPsnVFGPositionInfoModel.DetailsEntity.Currency1Entity currency1 = detailsEntity.getCurrency1();
        XpadPsnVFGPositionInfoModel.DetailsEntity.Currency2Entity currency2 = detailsEntity.getCurrency2();
        String code1 = currency1.getCode();
        String code2 = currency2.getCode();
        String currency_1 = PublicCodeUtils.getCurrency(context, code1);
        String currency_2 = PublicCodeUtils.getCurrency(context, code2);
        holder.currencytype.setText(currency_1+"/"+currency_2);
        holder.balancetype.setText(String.valueOf(xpadPsnVFGPositionInfoModel.getSettleCurrency()));


        String direction = detailsEntity.getDirection();
        if(direction.equals("B")){
            holder.buyprice.setText(String.valueOf(detailsEntity.getMeanPrice()));
            holder.banlance.setText(detailsEntity.getBalance());

            holder.profitorloss.setText(String.valueOf(detailsEntity.getCurrentProfitLoss()));
            holder.ll_buy.setVisibility(View.VISIBLE);

        } else if(direction.equals("S")){

            holder.saleprice.setText(String.valueOf(detailsEntity.getMeanPrice()));
            holder.salehavebanlance.setText(detailsEntity.getBalance());
            holder.saleprofitorloss.setText(String.valueOf(detailsEntity.getCurrentProfitLoss()));
            holder.sale_layout.setVisibility(View.VISIBLE);
        }


        String profitLossFlag = detailsEntity.getProfitLossFlag();
        if(profitLossFlag.equals("P")){
            holder.profitorloss.setTextColor(Color.RED);
        }else if(profitLossFlag.equals("L")){
            holder.profitorloss.setTextColor(Color.GREEN);
        }


        return convertView;
    }

    static class ViewHolder{
        TextView currencytype;
        TextView balancetype;
        TextView buyprice;   //买入平均价格
        TextView banlance;  //持有余额
        TextView profitorloss;  //暂亏盈计
        TextView saleprice;  //卖出平均价格
        TextView salehavebanlance;  //持有余额
        TextView saleprofitorloss;   //暂亏盈计
        LinearLayout  ll_buy;
        LinearLayout  sale_layout;

    }
}
