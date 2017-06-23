package com.chinamworld.bocmbci.biz.foreign.adapter;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.biz.foreign.Foreign;
import com.chinamworld.bocmbci.biz.foreign.StringTools;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.llbt.userwidget.NumberStyleTextView;

import java.util.List;
import java.util.Map;

/**
 * 外汇行情适配器
 * 功能外置 @see ForexRateInfoOutlayActivity
 * @author luqp 2016年9月22日
 */
public class ForeignPriceExternalAdapter extends BaseAdapter {
    private static final String TAG = "ForeignPriceExternalAdapter";
    private Context context = null;
    private List<Map<String, Object>> list = null;
    private List<Integer> selectedList = null;

    public List<Map<String, Object>> getList() {
        return list;
    }
    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }

    /** 刷新涨跌幅 涨跌值*/
    private boolean isOnClick = false;
    /** 解决以显示就开启动画问题*/
    private boolean isStartAnimator = false;
    /** 涨跌幅按钮事件 */
    private AdapterView.OnItemClickListener riseImgOnItemClickListener = null;
    /** listView item 点击事件 */
    private AdapterView.OnItemClickListener risebuySellClickListener = null;

    public void setSelectedPosition(Boolean isOnClick) {
        this.isOnClick = isOnClick;
        notifyDataSetChanged();
    }

    public AdapterView.OnItemClickListener getRisebuySellClickListener() {
        return risebuySellClickListener;
    }

    public void setRisebuySellClickListener(AdapterView.OnItemClickListener risebuySellClickListener) {
        this.risebuySellClickListener = risebuySellClickListener;
    }

    public AdapterView.OnItemClickListener getRiseImgOnItemClickListener() {
        return riseImgOnItemClickListener;
    }

    public void setRiseImgOnItemClickListener(AdapterView.OnItemClickListener riseImgOnItemClickListener) {
        this.riseImgOnItemClickListener = riseImgOnItemClickListener;
    }

    public ForeignPriceExternalAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    /**
     * 刷新数据
     * @param list 刷新数据的集合
     * @param isOpenAnimator 是否开启动画
     */
    public void refreshDeta(List<Map<String, Object>> list,Boolean isOpenAnimator){
        this.list = list;
        isStartAnimator = isOpenAnimator; //刷新时开启动画
        notifyDataSetChanged();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isStartAnimator = false;
            }
        },200);
    }

    /**
     * 刷新数据
     * @param list 刷新数据的集合
     * @param listItem 记录数据变化的item
     * @param isOpenAnimator 是否开启动画
     */
    public void refreshDetaChaged(List<Map<String, Object>> list,List<Integer> listItem ,Boolean isOpenAnimator){
        this.list = list;
        this.selectedList = listItem;
        isStartAnimator = isOpenAnimator; //刷新时开启动画
        notifyDataSetChanged();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isStartAnimator = false;
            }
        },200);
    }

    /**
     * 更新银行卖价、银行买价
     * @param list
     */
    public void dataChaged(List<Map<String, Object>> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    Handler handler = new Handler();

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LogGloble.d(TAG, "getView");
            convertView = LayoutInflater.from(context).inflate(R.layout.foreign_price_external_item, null);
            holder = new ViewHolder();
            holder.sourceCode = (TextView) convertView.findViewById(R.id.forex_sourceCurCde);
            holder.buyRate = (NumberStyleTextView) convertView.findViewById(R.id.forex_buy);
            holder.sellRate = (NumberStyleTextView) convertView.findViewById(R.id.forex_sell);
            holder.riseFall = (LinearLayout) convertView.findViewById(R.id.forex_rise_fall_amplitude);
            holder.riseAndFall = (TextView) convertView.findViewById(R.id.rise_and_fall);
            holder.buySellCilckLl =(LinearLayout) convertView.findViewById(R.id.ll_buy_sell_cilck);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //重复view,要清楚上次的动画
        Animation animation = convertView.getAnimation();
        if(animation != null){
            animation.cancel();
        }

        Map<String, Object> map = (Map<String, Object>) list.get(position);
        holder.sourceCode.setTag(position);
        holder.buyRate.setTag(position);
        holder.riseFall.setTag(position);
        holder.sellRate.setTag(position);
        // 得到源货币的代码
        String sourceCurrencyCode = (String) map.get(Forex.FOREX_RATE_SOURCECODE_RES);
        String sourceDealCode = null;
        if (LocalData.ForeignCurrency.containsKey(sourceCurrencyCode)) {
            sourceDealCode = LocalData.ForeignCurrency.get(sourceCurrencyCode);
        }
        /** 得到目标货币代码*/
        String targetCurrencyCode = (String) map.get(Forex.FOREX_RATE_TARGETCODE_RES);
        String targetDealCode = null;
        if (LocalData.ForeignCurrency.containsKey(targetCurrencyCode)) {
            targetDealCode = LocalData.ForeignCurrency.get(targetCurrencyCode);
        }
        StringBuilder sb = new StringBuilder(sourceDealCode);
        sb.append("/");
        sb.append(targetDealCode);
        holder.sourceCode.setText(sb.toString().trim());

        // 动画 start========================================================================
        /** X-FUND涨跌标志*/
        String flag = (String) map.get(Forex.FOREX_RATE_FLAG_RES);
        if(isStartAnimator) {
            if (selectedList != null){
                for(int j =0;j < selectedList.size();j++){
                    int selectedPosition = selectedList.get(j);
                    if (position == selectedPosition) {
                        if (flag.equals(ConstantGloble.FOREX_FLAG0)) {  //持平
                            startAnimation(convertView,R.color.share_gray_color); //加载灰色动画
                        } else if (flag.equals(ConstantGloble.FOREX_FLAG1)) { // 涨
                            startAnimation(convertView, R.color.share_red_color); //加载红色动画
                        } else if (flag.equals(ConstantGloble.FOREX_FLAG2)) { //跌
                            startAnimation(convertView, R.color.share_green_color); //加载绿色动画
                        }
                    }
                }
            }
        }
        // 动画 end===========================================================================

        if (isOnClick){ //涨跌幅被点击 数据换成涨跌值
            String currDiff = (String) map.get(Foreign.CURRDIFF); //今日涨跌值
            currDiff = StringTools.valueOf1(currDiff);
            holder.riseAndFall.setText(currDiff); //涨跌幅 保留3位小数
//            // 去掉小数点尾部0 如果大于3位四舍五入 如果小于三位保留当前位数
//            String currDiffStr = StringTools.subZeroAndDot(currDiff);
//            int number = StringTools.splitStringwith2pointnew(currDiffStr); // 判断小数位后面几个0
//            if (number>=3){
//                currDiffStr = StringTools.parseStringPattern(currDiffStr,3);
//            }
            /** 保留3位小数*/  // by luqp 2016年11月24日 注
//            currDiff = StringTools.parseStringPattern(currDiff,3);
            // 根据返回的数据第一位 判断 如果: +涨 -跌 没有+或-持平 返回空显示 --
            if (!StringUtil.isNull(currDiff) && !currDiff.equals("--")) {
                StringBuilder sbs2 = new StringBuilder(currDiff.subSequence(0, 1));
                String str = sbs2.toString().trim();
                if (str.equals("+")) { //涨
                    holder.riseFall.setBackgroundResource(R.drawable.llbt_btn_corner_red);
                } else if (str.equals("-")) { // 跌
                    holder.riseFall.setBackgroundResource(R.drawable.llbt_btn_corner_green);
                } else if (!StringUtil.isNull(str) || !str.equals("+") || !str.equals("-")) { //持平
                    holder.riseFall.setBackgroundResource(R.drawable.llbt_btn_corner_gray);
                } else { //持平
                    holder.riseFall.setBackgroundResource(R.drawable.llbt_btn_corner_gray);
                }
            } else {
                holder.riseFall.setBackgroundResource(R.drawable.llbt_btn_corner_gray);
            }
        } else { // 涨跌幅
            String currPercentDiff = (String) map.get(Foreign.CURRPERCENTDIFF); // 今日涨跌幅
            currPercentDiff = StringTools.valueOf1(currPercentDiff);
            holder.riseAndFall.setText(currPercentDiff); //涨跌幅 保留3位小数
            // 格式化小数点尾部0 如果大于五位四舍五入 如果小于五位保留当前位数
//            String currPercentDiffStr = StringTools.parseStringPattern5(currPercentDiff);
//            currPercentDiff = StringTools.parseOfTheString(currPercentDiff);
            // 根据返回的数据第一位 判断 如果: +涨 -跌 没有+或-持平 返回空显示 --
            if (!StringUtil.isNull(currPercentDiff) && !currPercentDiff.equals("--")) {
                StringBuilder sbs2 = new StringBuilder(currPercentDiff.subSequence(0, 1));
                String str = sbs2.toString().trim();
                if (str.equals("+")) { //涨
                    holder.riseFall.setBackgroundResource(R.drawable.llbt_btn_corner_red);
                } else if (str.equals("-")) { // 跌
                    holder.riseFall.setBackgroundResource(R.drawable.llbt_btn_corner_green);
                } else if (!StringUtil.isNull(str) || !str.equals("+") || !str.equals("-")) { //持平
                    holder.riseFall.setBackgroundResource(R.drawable.llbt_btn_corner_gray);
                } else { //持平
                    holder.riseFall.setBackgroundResource(R.drawable.llbt_btn_corner_gray);
                }
            } else {
                holder.riseFall.setBackgroundResource(R.drawable.llbt_btn_corner_gray);
            }
        }
        int number = 4;
        if(map.containsKey("formatNumber")){
            number = (Integer)map.get("formatNumber");
        }

        String buyRate = (String) map.get(Forex.FOREX_RATE_BUYRATE_RES);
        String buyRateStr = StringUtil.parseStringPattern(buyRate,number);
        holder.buyRate.setNumberText(buyRateStr,2,"--");

        String sellRate = (String) map.get(Forex.FOREX_RATE_SELLRATE_RES);
        String sellRateStr = StringUtil.parseStringPattern(sellRate,number);
        holder.sellRate.setNumberText(sellRateStr,2,"--");

        /** item 点击事件*/
        holder.buySellCilckLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (risebuySellClickListener != null){
                    risebuySellClickListener.onItemClick(null,view,position,position);
                }
            }
        });

        /** 点击右边的图片按钮，跳转到外汇详情页面*/
        holder.riseFall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (riseImgOnItemClickListener != null) {
                    riseImgOnItemClickListener.onItemClick(null, v, position, position);
                }
            }
        });
        // listView的监听事件
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
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
        colorAnimator.setDuration(400);
        colorAnimator.start();
    }

    /** 内部类--控件 */
    public final class ViewHolder {
        /** 货币对*/
        public TextView sourceCode = null;
        /** buyRate：买价*/
        public NumberStyleTextView buyRate;
        /** sellRate：卖价*/
        public NumberStyleTextView sellRate;
        /** button:进入详情页面*/
        public LinearLayout riseFall;
        /** riseAndFall：涨跌幅*/
        public TextView riseAndFall;
        /** 点击事件 Ll*/
        public LinearLayout buySellCilckLl;
    }
}
