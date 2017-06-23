package com.chinamworld.bocmbci.widget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.constant.DictionaryData;
import com.chinamworld.bocmbci.constant.LocalData;

import java.util.List;
import java.util.Map;

/**
 * 底部弹框  显示货币对条目的适配器
 * @see DialogItemAdapter
 * @author luqp 2016年10月31日
 */
public class DialogItemAdapter extends BaseAdapter {
    private final String TAG = "ForeignDialogAdapter";
    private Context context;
    /** 全部货币对*/
    private List<Map<String, Object>> allCodeList = null;
    /** 标记list*/
    private List<Boolean> customerCoseList = null;
    /** 条目按钮事件 */
    private AdapterView.OnItemClickListener itemClickListener = null;

    public AdapterView.OnItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public DialogItemAdapter(Context context) {
        this.context = context;
    }

    /**
     * 该构造方法
     * @param context：上下文
     * @param allCodeList：所有的货币对
     */
    public DialogItemAdapter(Context context, List<Map<String, Object>> allCodeList) {
        this.context = context;
        this.allCodeList = allCodeList;
    }

    /** 刷新适配器*/
    public void refreshDeta(List<Boolean> customerCoseList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.foreign_details_dialog_item, null);
            holder = new ViewHolder();
            holder.itemCilck = (RelativeLayout) convertView.findViewById(R.id.ll_item_cilck);
            holder.sourceCode = (TextView) convertView.findViewById(R.id.rate_sourceCurCde);
            holder.targetCode = (TextView) convertView.findViewById(R.id.rate_targetCurCde);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.sourceCode.setTag(position);
        holder.targetCode.setTag(position);
        Map<String, Object> allMap = allCodeList.get(position);
        String targetCurrencyCode = (String) allMap.get(Forex.FOREX_RATE_TARGETCODE_RES);
        String targetDealCode = null;
        String sourceDealCode = null;
        String sourceCurrencyCode = (String) allMap.get(Forex.FOREX_RATE_SOURCECODE_RES);
        sourceDealCode = DictionaryData.transCurrency(sourceCurrencyCode);
        targetDealCode = DictionaryData.transCurrency(targetCurrencyCode);
        // by luqp 2016年12月5日  修改生产货币对
//        if (LocalData.ForeignCurrency.containsKey(targetCurrencyCode)) {
//            targetDealCode = LocalData.ForeignCurrency.get(targetCurrencyCode); //得到目标货币代码
//        }
//        // 如果贵金属币种为空 处理普通币种
//        if (LocalData.ForeignCurrency.containsKey(sourceCurrencyCode)) {
//            sourceDealCode = LocalData.ForeignCurrency.get(sourceCurrencyCode);// 得到源货币的代码
//        }
//        if(LocalData.goldLists.contains(sourceDealCode)||LocalData.goldLists.contains(targetDealCode)){
//            sourceDealCode = (String)LocalData.code_Map_Left.get(sourceCurrencyCode);
//            targetDealCode = (String)LocalData.code_Map_Right.get(targetCurrencyCode);
//        }
        holder.sourceCode.setText(sourceDealCode);
        holder.targetCode.setText(targetDealCode);

        /** 条目点击事件*/
        holder.itemCilck.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(null, v, position, position);
                }
            }
        });
        return convertView;
    }
    /** 内部类--控件*/
    public final class ViewHolder {
        /** 货币对点击事件*/
        private RelativeLayout itemCilck;
        /** sourceCode:源货币对*/
        private TextView sourceCode = null;
        /** targetCode:目标货币对*/
        private TextView targetCode = null;
    }
}
