package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 收款人列表适配器
 * Created by zhx on 2016/7/19
 */
public class PayeeListAdapter extends BaseAdapter {
    private Context context;
    private String filterWord;
    private List<AccountBean> payeeEntityList;
    private List<AccountBean> payeeEntityFilterList = new ArrayList<AccountBean>();

    public PayeeListAdapter(Context context, List<AccountBean> payeeEntityList) {
        this.context = context;
        this.payeeEntityList = payeeEntityList;
    }

    public void setFilterWord(String filterWord) {
        this.filterWord = filterWord;
        notifyDataSetChanged();
    }

    public void setPayeeEntityList(List<AccountBean> payeeEntityList) {
        this.payeeEntityList = payeeEntityList;
    }

    public List<AccountBean> getPayeeEntityFilterList() {
        return payeeEntityFilterList;
    }

    @Override
    public int getCount() {
        if (payeeEntityList != null && payeeEntityList.size() > 0) {
            if (!TextUtils.isEmpty(filterWord)) {
                payeeEntityFilterList.clear();

                for (AccountBean payeeEntity : payeeEntityList) {
                    try {
                        // 如果名称或者名称的拼音包含过滤文字，那么，添加到payeeEntityFilterList中
                        if (payeeEntity.getAccountName().contains(filterWord.toUpperCase()) || payeeEntity.getAccountNamePinyin().contains(filterWord.toUpperCase())) {
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
    public AccountBean getItem(int position) {
        AccountBean payeeEntity;
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
                    R.layout.list_item_payee_for_wy, null);
        }
        ViewHolder holder = ViewHolder.getHolder(convertView);
        AccountBean payeeEntity = getItem(position); // 界面刚才显示有问题，是因为这里取PayeeEntity出错，改为getItem(position)正确
        holder.name.setText(payeeEntity.getAccountName()); // 收款人帐户名称
        holder.tv_account_number.setText(NumberUtils.formatCardNumberStrong(payeeEntity.getAccountNumber())); // 收款人银行帐号

        String firstWord = payeeEntity.getAccountNamePinyin().charAt(0) + ""; // 当前的首字母
        if (position > 0) {
            // 获取前一个名字的首字母
            String preFirstWord = getItem(position - 1).getAccountNamePinyin().charAt(0) + "";
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

        holder.tv_bank_name.setText("中国银行"); // 收款人银行名称
        holder.view_separate_line.setVisibility(View.VISIBLE);

        return convertView;
    }

    static class ViewHolder {
        TextView name, letter, tv_account_number, tv_bank_name, tv_ding_xiang, tv_shi_shi;
        ImageView iv_bank_logo;
        FrameLayout fl_letter;
        View view_separate_line;

        public ViewHolder(View convertView) {
            name = (TextView) convertView.findViewById(R.id.name);
            letter = (TextView) convertView.findViewById(R.id.letter);
            tv_account_number = (TextView) convertView.findViewById(R.id.tv_account_number);
            tv_bank_name = (TextView) convertView.findViewById(R.id.tv_bank_name);
            tv_ding_xiang = (TextView) convertView.findViewById(R.id.tv_ding_xiang);
            tv_shi_shi = (TextView) convertView.findViewById(R.id.tv_shi_shi);
            iv_bank_logo = (ImageView) convertView.findViewById(R.id.iv_bank_logo);
            fl_letter = (FrameLayout) convertView.findViewById(R.id.fl_letter); // 包裹“字母”组件的布局
            view_separate_line = convertView.findViewById(R.id.view_separate_line); // 银行号码和银行名称中间的分割线
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