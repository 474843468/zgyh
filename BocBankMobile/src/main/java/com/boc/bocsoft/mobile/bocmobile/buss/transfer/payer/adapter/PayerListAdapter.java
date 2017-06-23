package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payer.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payer.model.PayerBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 付款人管理Adapter
 * Created by liuyang on 2016/7/21.
 */
public class PayerListAdapter extends BaseAdapter {
    private Context context;
    private String filterWord;
    private List<PayerBean> mBean;
    private List<PayerBean> mBeanFilterList = new ArrayList<PayerBean>();

    public PayerListAdapter(List<PayerBean> mBean, Context context) {
        this.mBean = mBean;
        this.context = context;
    }

    public void setFilterWord(String filterWord) {
        this.filterWord = filterWord;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {

        if (mBean != null && mBean.size() > 0) {
            if (!TextUtils.isEmpty(filterWord)) {
                mBeanFilterList.clear();

                for (PayerBean bean : mBean) {
                    try {
                        // 如果名称或者名称的拼音包含过滤文字，那么，添加到mBeanList中
                        if (bean.getPayerName().contains(filterWord) || bean.getPinyin().contains(filterWord.toUpperCase())) {
                            mBeanFilterList.add(bean);
                        }
                    } catch (Exception e) {
                        // bean.getPinyin()可能为null，所以加try...catch...语句块
                    }
                }

                return mBeanFilterList.size();
            }
            return mBean.size();
        }

        return 0;
    }


    @Override
    public PayerBean getItem(int position) {
        PayerBean bean;
        if (!TextUtils.isEmpty(filterWord)) {
            bean = mBeanFilterList.get(position);
        } else {
            bean = mBean.get(position);
        }
        return bean;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.boc_list_item_payer, null);
        }
        ViewHolder viewHolder = ViewHolder.getHolder(convertView);
        PayerBean bean = getItem(position);
        viewHolder.tv_payer_name.setText(bean.getPayerName()); // 付款人姓名
        viewHolder.tv_payer_phone_num.setText(bean.getPayerMobile()); // 付款人手机号
        String type = bean.getIdentifyType();
        if (type != null && type.equals("1")) {
            viewHolder.tv_cyber_bank.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tv_cyber_bank.setVisibility(View.GONE);
        }
        return convertView;
    }

    public static class ViewHolder {
        TextView tv_payer_name;
        TextView tv_payer_phone_num;
        TextView tv_cyber_bank;

        public ViewHolder(View convertView) {
            tv_payer_name = (TextView) convertView.findViewById(R.id.tv_payer_name);
            tv_cyber_bank = (TextView) convertView.findViewById(R.id.tv_cyber_bank);
            tv_payer_phone_num = (TextView) convertView.findViewById(R.id.tv_payer_phone_num);
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
