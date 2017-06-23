package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.AccountUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadAccountQueryViewModel;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.List;

/**
 * 已到期产品列表列表适配器（中银理财）
 * Created by zhx on 2016/9/17
 */
public class WealthAccountAdapter extends BaseAdapter {
    private Context context;
    List<XpadAccountQueryViewModel.XPadAccountEntity> accountList;

    public WealthAccountAdapter(Context context, List<XpadAccountQueryViewModel.XPadAccountEntity> accountList) {
        this.context = context;
        this.accountList = accountList;
    }

    @Override
    public int getCount() {
        if (accountList != null && accountList.size() > 0) {
            return accountList.size();
        }
        return 0;
    }

    @Override
    public XpadAccountQueryViewModel.XPadAccountEntity getItem(int position) {
        return accountList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.list_item_wealth_account, null);
        }
        ViewHolder viewHolder = ViewHolder.getHolder(convertView);

        viewHolder.tv_account_num.setText(NumberUtils.formatCardNumberStrong(getItem(position).getAccountNo()));
        AccountUtils.CardType cardType = AccountUtils.getCardType(getItem(position).getAccountType());
        viewHolder.iv_account_image.setImageResource(AccountUtils.getCardPic(cardType));
        viewHolder.tv_account_name.setText(PublicCodeUtils.changeaccounttypetoName(getItem(position).getAccountType()));
        return convertView;
    }

    static class ViewHolder {
        TextView tv_account_num;
        ImageView iv_account_image;
        TextView tv_account_name;

        public ViewHolder(View convertView) {
            tv_account_num = (TextView) convertView.findViewById(R.id.tv_account_num);
            iv_account_image = (ImageView) convertView.findViewById(R.id.iv_account_image);
            tv_account_name = (TextView) convertView.findViewById(R.id.tv_account_type);
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
