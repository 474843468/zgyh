package com.chinamworld.bocmbci.biz.foreign.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.List;
import java.util.Map;

/**
 * 外汇 定制货币对适配器
 * 功能外置 @see ForeignCurrencyPairsAdapter
 * @author luqp 2016年10月11日
 */
public class ForeignCurrencyPairsAdapter extends BaseAdapter {
    private final String TAG = "ForeignCurrencyPairsAdapter";
    private Context context;
    /** 全部货币对*/
    private List<Map<String, Object>> allCodeList = null;
    /** 标记list*/
    private List<Boolean> customerCoseList = null;

    public List<Boolean> getCustomerCoseList() {
        return customerCoseList;
    }

    public void setCustomerCoseList(List<Boolean> customerCoseList) {
        this.customerCoseList = customerCoseList;
    }

    public ForeignCurrencyPairsAdapter(Context context) {
        this.context = context;
    }

    /**
     * 用户没有定制货币对时，使用该构造方法
     * @param context：上下文
     * @param allCodeList：所有的货币对
     */
    public ForeignCurrencyPairsAdapter(Context context, List<Map<String, Object>> allCodeList) {
        this.context = context;
        this.allCodeList = allCodeList;
    }

    /**
     * 用户定制货币对时，使用该构造方法
     * @param context：上下文
     * @param allCodeList：所有的货币对
     * @param customerCoseList:标记list
     */
    public ForeignCurrencyPairsAdapter(Context context, List<Map<String, Object>> allCodeList, List<Boolean> customerCoseList) {
        this.context = context;
        this.allCodeList = allCodeList;
        this.customerCoseList = customerCoseList;
    }

    public void dateChanged(List<Boolean> customerCoseList) {
        this.customerCoseList = customerCoseList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return allCodeList.size();
    }

    @Override
    public Object getItem(int position) {
        return allCodeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.foreign_rate_make_code_list, null);
            holder = new ViewHolder();
            holder.sourceCode = (TextView) convertView.findViewById(R.id.rate_sourceCurCde);

            holder.targetCode = (TextView) convertView.findViewById(R.id.rate_targetCurCde);
            holder.okImg = (ImageView) convertView.findViewById(R.id.imageViewright);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.sourceCode.setTag(position);
        holder.targetCode.setTag(position);
        holder.okImg.setTag(position);
        Map<String, Object> allMap = allCodeList.get(position);
        String sourceCode = (String) allMap.get(Forex.FOREX_MAKE_SOURCECURCDE_RES);
        String sourceDealCode = null;
        if (LocalData.CurrencyShort.containsKey(sourceCode)) {
            sourceDealCode = LocalData.CurrencyShort.get(sourceCode);
        }
        String targetCode = (String) allMap.get(Forex.FOREX_MAKE_TARGETCURCDE_RES);
        String targetDealCode = null;
        if (LocalData.CurrencyShort.containsKey(targetCode)) {
            targetDealCode = LocalData.CurrencyShort.get(targetCode);
        }
        if(StringUtil.isNullOrEmpty(customerCoseList)){
            //用户没有定制货币对，显示全部的货币对
        }else{
            if (customerCoseList.get(position)) {
                holder.okImg.setBackgroundResource(R.drawable.boc_checkbox_true);
            } else {
                holder.okImg.setBackgroundResource(R.drawable.boc_checkbox);
            }
        }
        holder.sourceCode.setText(sourceDealCode);
        holder.targetCode.setText(targetDealCode);
        return convertView;
    }

    public int getNumber() {
        int len = customerCoseList.size();
        int number = 0;
        for (int i = 0; i < len; i++) {
            if (customerCoseList.get(i)) {
                number++;
            }
        }
        return number;
    }

    /** 内部类--控件*/
    public final class ViewHolder {
        /** sourceCode:源货币对*/
        private TextView sourceCode = null;
        /** targetCode:目标货币对*/
        private TextView targetCode;
        /** 选中货币对后显示*/
        private ImageView okImg;
    }

}
