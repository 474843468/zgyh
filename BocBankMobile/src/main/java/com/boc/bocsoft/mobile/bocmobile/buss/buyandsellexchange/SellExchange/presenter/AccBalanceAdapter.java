package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.SellExchange.presenter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.SellExchange.model.SellAccountBalanceModel;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.SellExchange.ui.CurrencyAccFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.ArrayList;
import java.util.List;

/**结汇账户和余额适配器
 * Created by gwluo on 2016/12/21.
 */

public class AccBalanceAdapter extends BaseAdapter {
    private Context mContext;
    private CurrencyAccFragment mFragment;

    public AccBalanceAdapter(Context context, CurrencyAccFragment fragment) {
        mContext = context;
        mFragment = fragment;
    }

    private ArrayList<SellAccountBalanceModel> data = new ArrayList<>();

    public void update(ArrayList<SellAccountBalanceModel> modelList) {
        data = modelList;
        notifyDataSetChanged();
    }

    public ArrayList<SellAccountBalanceModel> getData() {
        return data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

//    private String lastAccId = "";//记录getView上一个的账户id

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.boc_item_sell_exchange_balance, null);
            holder.tv_account = (TextView) convertView.findViewById(R.id.tv_account);
            holder.tv_currency_cash_spot = (TextView) convertView.findViewById(R.id.tv_currency_cash_spot);
            holder.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final SellAccountBalanceModel model = data.get(position);
        if (model.isBalanceFail()) {
            holder.tv_account.setVisibility(View.VISIBLE);
            holder.tv_account.setText(NumberUtils.formatCardNumberStrong(
                    model.getAccountNum()) + " " +
                    PublicCodeUtils.getAccountType(mContext, model.getAccountType()));
//            lastAccId = model.getAccountId();//记录账户id
            holder.tv_amount.setText("重新获取");
            holder.tv_amount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFragment.getSingleAccBalance(model.getAccountId());
                }
            });
        } else {
            //如果相同的accountID只显示第一个
            if (model.isShowTitle()) {
                holder.tv_account.setVisibility(View.VISIBLE);
                holder.tv_account.setText(NumberUtils.formatCardNumberStrong(
                        model.getAccountNum()) + " " +
                        PublicCodeUtils.getAccountType(mContext, model.getAccountType()));
            } else {
                holder.tv_account.setVisibility(View.GONE);
            }
//            lastAccId = model.getAccountId();//记录账户id
            holder.tv_currency_cash_spot.setText(
                    PublicCodeUtils.getCurrency(mContext, model.getCurrency()) + " " +
                            PublicCodeUtils.getCashSpot(mContext, model.getCashRemit()));
            holder.tv_amount.setText(model.getAvailableBalance());
        }
        return convertView;
    }

    public class ViewHolder {
        TextView tv_account, tv_currency_cash_spot, tv_amount;
    }
}
