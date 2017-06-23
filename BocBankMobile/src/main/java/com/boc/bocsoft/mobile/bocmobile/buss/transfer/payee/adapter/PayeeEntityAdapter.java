package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PayeeEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 收款人列表适配器
 * Created by zhx on 2016/7/19
 */
public class PayeeEntityAdapter extends BaseAdapter {
    private Context context;
    private String filterWord;
    private List<PayeeEntity> payeeEntityList;
    private List<PayeeEntity> payeeEntityFilterList = new ArrayList<PayeeEntity>();

    public PayeeEntityAdapter(Context context, List<PayeeEntity> payeeEntityList) {
        this.context = context;
        this.payeeEntityList = payeeEntityList;
    }

    public void setFilterWord(String filterWord) {
        this.filterWord = filterWord;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (payeeEntityList != null && payeeEntityList.size() > 0) {
            if (!TextUtils.isEmpty(filterWord)) {
                payeeEntityFilterList.clear();

                for (PayeeEntity payeeEntity : payeeEntityList) {
                    try {
                        // 如果名称或者名称的拼音包含过滤文字，那么，添加到payeeEntityFilterList中
                        if (payeeEntity.getName().contains(filterWord.toUpperCase()) || payeeEntity.getPinyin().contains(filterWord.toUpperCase())) {
                            payeeEntityFilterList.add(payeeEntity);
                        }
                    } catch (Exception e) {
                        // payeeEntity.getPinyin()可能为null，所以加try...catch...语句块
                    }
                }
                return payeeEntityFilterList.size();
            }
            return payeeEntityList.size();
        }
        return 0;
    }

    @Override
    public PayeeEntity getItem(int position) {
        PayeeEntity payeeEntity;
        if (!TextUtils.isEmpty(filterWord)) {
            payeeEntity = payeeEntityFilterList.get(position);
        } else {
            payeeEntity = payeeEntityList.get(position);
        }
        return payeeEntity;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context,
                    R.layout.list_item_payee, null);
        }
        ViewHolder holder = ViewHolder.getHolder(convertView);
        PayeeEntity payeeEntity = getItem(position); // 界面刚才显示有问题，是因为这里取PayeeEntity出错，改为getItem(position)正确
        holder.name.setText(payeeEntity.getName());

        String firstWord = payeeEntity.getPinyin().charAt(0) + ""; // 当前的首字母
        if (position > 0) {
            // 获取前一个名字的首字母
            String preFirstWord = getItem(position - 1).getPinyin().charAt(0) + "";
            if (firstWord.equals(preFirstWord)) {
                holder.fl_letter.setVisibility(View.GONE);
            } else {
                holder.fl_letter.setVisibility(View.VISIBLE);
                holder.letter.setText(firstWord.equals("z") ? "#" : firstWord);
            }
        } else {
            holder.fl_letter.setVisibility(View.VISIBLE);
            holder.letter.setText(firstWord);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView name, letter;
        FrameLayout fl_letter;

        public ViewHolder(View convertView) {
            name = (TextView) convertView.findViewById(R.id.name);
            letter = (TextView) convertView.findViewById(R.id.letter);
            fl_letter = (FrameLayout) convertView.findViewById(R.id.fl_letter);
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