package com.chinamworld.bocmbci.widget.adapter;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.llbt.userwidget.NumberStyleTextView;


import java.util.List;

/**
 * Created by Administrator on 2016/11/10.
 */
public class InvestAdaper extends CommonAdapter<InvestListItem> implements ICommonAdapter<InvestListItem> {

    private boolean isShow = false;
    boolean isStartAnimator = false;
    public InvestAdaper(Context context, List<InvestListItem> source) {
        super(context, source, null);
        commonAdapterCallBack = this;
    }

    public InvestAdaper(Context context, ListView listView, List<InvestListItem> source) {
        super(context, listView, source, null);
        commonAdapterCallBack = this;
    }

    public void setSouceList(List<InvestListItem> list,int totalCount){
        super.setSourceList(list,totalCount);
    }

    @Override
    public View getView(int arg0, InvestListItem currentItem, LayoutInflater inflater, View convertView, ViewGroup viewGroup) {
        convertView = inflater.inflate(R.layout.invest_forex_gold_list_item,null);
        LinearLayout detail = (LinearLayout)convertView.findViewById(R.id.detail);
        TextView tv_currency = (TextView) convertView.findViewById(R.id.tv_currency);
        NumberStyleTextView tv_sale = (NumberStyleTextView) convertView.findViewById(R.id.tv_sale);
        NumberStyleTextView tv_buy = (NumberStyleTextView) convertView.findViewById(R.id.tv_buy);

        TextView tv_riseorfall = (TextView) convertView.findViewById(R.id.tv_riseorfall);
        tv_currency.setText(currentItem.getCodeName());
        tv_buy.setNumberText(currentItem.getBuy(),2,"--");
        tv_sale.setNumberText(currentItem.getSell(),2,"--");
        if(isShow) {
            tv_riseorfall.setText(currentItem.getriseOrFall());
        }
        else {
            tv_riseorfall.setText(currentItem.getOffPercentage());
        }
        tv_riseorfall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShow = !isShow;
                InvestAdaper.this.notifyDataSetChanged();
            }
        });
        if (currentItem.getriseOrFall() != null && currentItem.getriseOrFall().contains("-")) {//绿色背景
            tv_riseorfall.setBackgroundResource(R.drawable.shape_llbt_round_green);
        } else {//红色背景
            tv_riseorfall.setBackgroundResource(R.drawable.shape_llbt_round_red);
        }

        if (currentItem.getUpAndDownFlag() == 1) {  //持平
            if(isStartAnimator) {
                startAnimation(convertView,R.color.boc_text_color_gray); //加载灰色动画
            }
        } else if (currentItem.getUpAndDownFlag() == 2) { // 涨
            if(isStartAnimator) {
                startAnimation(convertView, R.color.boc_text_color_red); //加载红色动画
            }
        } else if (currentItem.getUpAndDownFlag() == 3) { //跌
            if(isStartAnimator) {
                startAnimation(convertView,R.color.boc_text_color_green); //加载绿色动画
            }
        }

        return convertView;

    }

    /** 涨跌幅 跌时动画显示*/
    public void startAnimation(final View view, final int colorID) {
        //创建动画,这里的关键就是使用ArgbEvaluator, 后面2个参数就是 开始的颜色,和结束的颜色.
        final ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), context.getResources().getColor(colorID), Color.WHITE);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int color = (int) animation.getAnimatedValue();//之后就可以得到动画的颜色了
                view.setBackgroundColor(color);//设置一下, 就可以看到效果.
            }
        });
        colorAnimator.setDuration(500);
        colorAnimator.start();
    }




}
