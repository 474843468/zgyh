package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.AccountUtils;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.List;

/**
 * Adapter：收款账户列表
 * Created by zhx on 2016/7/11
 */
public class PayeeAccountAdapter extends BaseAdapter {
    List<AccountBean> mAccountBeanList;
    Context mContext;

    public PayeeAccountAdapter(List<AccountBean> accountBeanList, Context context) {
        this.mAccountBeanList = accountBeanList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        if (mAccountBeanList != null) {
            return mAccountBeanList.size();
        }
        return 0;
    }

    @Override
    public AccountBean getItem(int position) {
        return mAccountBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_item_payee_account, null);
        }
        ViewHolder viewHolder = ViewHolder.getHolder(convertView);

        AccountBean accountBean = getItem(position);
        viewHolder.iv_account_image.setImageResource(AccountUtils.getCardPic(accountBean));
        viewHolder.tv_account_num.setText(NumberUtils.formatCardNumber(accountBean.getAccountNumber()));
        viewHolder.tv_account_type.setText(accountBean.getNickName());
        return convertView;
    }

    static class ViewHolder {
        ImageView iv_account_image;
        TextView tv_account_num;
        TextView tv_account_type;

        public ViewHolder(View convertView) {
            iv_account_image = (ImageView) convertView.findViewById(R.id.iv_account_image);
            tv_account_num = (TextView) convertView.findViewById(R.id.tv_account_num);
            tv_account_type = (TextView) convertView.findViewById(R.id.tv_account_type);
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
