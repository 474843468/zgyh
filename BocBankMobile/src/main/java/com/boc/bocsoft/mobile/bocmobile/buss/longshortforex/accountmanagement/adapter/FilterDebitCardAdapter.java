package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.AccountUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGFilterDebitCardViewModel;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.List;

/**
 * Adapter：双向宝-账户管理-新增保证金账户-过滤出符合条件的借记卡账户
 * Created by zhx on 2016/12/12
 */
public class FilterDebitCardAdapter extends BaseAdapter {
    private Context context;
    private String currentTradeAccountNumber;
    private List<VFGFilterDebitCardViewModel.DebitCardEntity> list;

    public FilterDebitCardAdapter(Context context, List<VFGFilterDebitCardViewModel.DebitCardEntity> list) {
        this.context = context;
        this.list = list;
    }

    public void setCurrentTradeAccountNumber(String currentTradeAccountNumber) {
        this.currentTradeAccountNumber = currentTradeAccountNumber;
    }

    public void setList(List<VFGFilterDebitCardViewModel.DebitCardEntity> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public VFGFilterDebitCardViewModel.DebitCardEntity getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.list_item_debit_card, null);
        }
        ViewHolder viewHolder = ViewHolder.getHolder(convertView);

        VFGFilterDebitCardViewModel.DebitCardEntity item = getItem(position);
        viewHolder.tv_account_num.setText(NumberUtils.formatCardNumberStrong(item.getAccountNumber()));
        AccountUtils.CardType cardType = AccountUtils.getCardType(item.getAccountType());
        viewHolder.iv_account_image.setImageResource(AccountUtils.getCardPic(cardType));
        viewHolder.tv_account_type.setText(PublicCodeUtils.changeaccounttypetoName(item.getAccountType()));

        // 是否是交易账户
        if (currentTradeAccountNumber.equals(item.getAccountNumber())) {
            viewHolder.tv_is_current_trade_account.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tv_is_current_trade_account.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView iv_account_image;
        TextView tv_account_num;
        TextView tv_account_type;
        TextView tv_is_current_trade_account;

        public ViewHolder(View convertView) {
            iv_account_image = (ImageView) convertView.findViewById(R.id.iv_account_image);
            tv_account_num = (TextView) convertView.findViewById(R.id.tv_account_num);
            tv_account_type = (TextView) convertView.findViewById(R.id.tv_account_type);
            tv_is_current_trade_account = (TextView) convertView.findViewById(R.id.tv_is_current_trade_account);
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
