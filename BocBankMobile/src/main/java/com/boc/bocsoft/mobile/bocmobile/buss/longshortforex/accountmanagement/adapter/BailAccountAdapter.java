package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGGetBindAccountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui.AccountManagementContact;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Adapter：双向宝-账户管理-保证金列表适配器
 * Created by zhx on 2016/11/23
 */
public class BailAccountAdapter extends BaseAdapter {
    private Context context;
    private List<VFGBailListQueryViewModel.BailItemEntity> list;
    private String firstAcccountNumber;
    private boolean isAccountNumberSizeBiggerThanZero = false;
    private VFGGetBindAccountViewModel vfgGetBindAccountViewModel; // 双向宝交易账户ViewModel
    private AccountManagementContact.View mView;

    public BailAccountAdapter(Context context, List<VFGBailListQueryViewModel.BailItemEntity> list, AccountManagementContact.View view) {
        this.context = context;
        this.list = list;
        mView = view;
    }

    public void setList(List<VFGBailListQueryViewModel.BailItemEntity> list) {
        this.list = list;
    }

    public List<VFGBailListQueryViewModel.BailItemEntity> getList() {
        return list;
    }

    public void setAccountNumberSizeBiggerThanZero(boolean accountNumberSizeBiggerThanZero) {
        isAccountNumberSizeBiggerThanZero = accountNumberSizeBiggerThanZero;
    }

    public boolean isAccountNumberSizeBiggerThanZero() {
        return isAccountNumberSizeBiggerThanZero;
    }

    public VFGGetBindAccountViewModel getVfgGetBindAccountViewModel() {
        return vfgGetBindAccountViewModel;
    }

    public void setVfgGetBindAccountViewModel(VFGGetBindAccountViewModel vfgGetBindAccountViewModel) {
        this.vfgGetBindAccountViewModel = vfgGetBindAccountViewModel;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public VFGBailListQueryViewModel.BailItemEntity getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.list_item_long_short_account2, null);
        }
        ViewHolder viewHolder = ViewHolder.getHolder(convertView);

        VFGBailListQueryViewModel.BailItemEntity bailItemEntity = getItem(position);
        viewHolder.tv_account_num.setText(NumberUtils.formatCardNumber(bailItemEntity.getAccountNumber())); // 借记卡卡号

        String currency = PublicCodeUtils.getCurrency(context, bailItemEntity.getSettleCurrency()); // 结算货币
        String marginAccountNo = bailItemEntity.getMarginAccountNo();
        int index = marginAccountNo.indexOf("00000");
        if (index == 0) {
            String substring = marginAccountNo.substring(5, marginAccountNo.length());
            viewHolder.tv_currency.setText("[" + currency + "]保证金 " + NumberUtils.formatCardNumber(substring));
        } else {
            viewHolder.tv_currency.setText("[" + currency + "]保证金 " + NumberUtils.formatCardNumber(marginAccountNo));
        }



        // 设置“(当前交易账户)”，及“更改”按钮的隐藏和显示
        boolean isCurrentAccount = bailItemEntity.isCurrentAccount();
        if (isCurrentAccount) {
            viewHolder.tv_is_current_trade_account.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tv_is_current_trade_account.setVisibility(View.GONE);
        }

        if (position == 0) {
            viewHolder.fl_container0.setVisibility(View.VISIBLE);
        } else {
            VFGBailListQueryViewModel.BailItemEntity preItem = getItem(position - 1);
            if (preItem.getAccountNumber().equals(bailItemEntity.getAccountNumber())) {
                viewHolder.fl_container0.setVisibility(View.GONE);
            } else {
                viewHolder.fl_container0.setVisibility(View.VISIBLE);
            }
        }

        // “更改”按钮的隐藏和显示


        // 保证金净值
        BigDecimal marginNetBalance = bailItemEntity.getMarginNetBalance();
        if (marginNetBalance != null) {
            String marginNetBalanceStr = MoneyUtils.transMoneyFormat(marginNetBalance, bailItemEntity.getSettleCurrency());
            viewHolder.tv_margin_net_balance.setText(marginNetBalanceStr);
        }

        // “保证金不足”是否显示
        String alarmFlag = bailItemEntity.getAlarmFlag();
        if (alarmFlag != null) {
            viewHolder.tv_margin_rate.setVisibility("N".equals(alarmFlag) ? View.GONE : View.VISIBLE);
        }

        // “暂计盈亏”的字体颜色
        String profitLossFlag = bailItemEntity.getProfitLossFlag();
        BigDecimal currentProfitLoss = bailItemEntity.getCurrentProfitLoss();
        if (profitLossFlag != null) {
            int greenColor = context.getResources().getColor(R.color.boc_text_color_green);
            int redColor = context.getResources().getColor(R.color.boc_text_color_red);
            int greyColor = context.getResources().getColor(R.color.boc_text_color_cinerous);

            if ("P".equals(profitLossFlag)) {
                if (currentProfitLoss.doubleValue() == 0.0) {
                    viewHolder.tv_current_profit_loss.setTextColor(greyColor);
                } else {
                    viewHolder.tv_current_profit_loss.setTextColor(redColor);
                }
            } else {
                viewHolder.tv_current_profit_loss.setTextColor(greenColor);
            }
        }

        // 暂计盈亏
        if (currentProfitLoss != null) {
            String profitLossStr = MoneyUtils.transMoneyFormat(currentProfitLoss, bailItemEntity.getSettleCurrency());
            if ("P".equals(profitLossFlag)) {
                viewHolder.tv_current_profit_loss.setText(profitLossStr);
            } else {
                viewHolder.tv_current_profit_loss.setText("-" + profitLossStr);
            }
        }

        // 卡类型
        viewHolder.tv_account_type.setText(bailItemEntity.getNickName());

        return convertView;
    }

    static class ViewHolder {
        ImageView iv_account_image;
        TextView tv_account_num;
        TextView tv_account_type;
        TextView tv_currency;
        TextView tv_is_current_trade_account;
        TextView tv_current_profit_loss;
        TextView tv_margin_net_balance;
        TextView tv_margin_rate;
        FrameLayout fl_container0;
        LinearLayout ll_container1;

        public ViewHolder(View convertView) {
            iv_account_image = (ImageView) convertView.findViewById(R.id.iv_account_image);
            tv_account_num = (TextView) convertView.findViewById(R.id.tv_account_num);
            tv_account_type = (TextView) convertView.findViewById(R.id.tv_account_type);
            tv_currency = (TextView) convertView.findViewById(R.id.tv_currency);
            tv_is_current_trade_account = (TextView) convertView.findViewById(R.id.tv_is_current_trade_account);
            tv_current_profit_loss = (TextView) convertView.findViewById(R.id.tv_current_profit_loss);
            tv_margin_net_balance = (TextView) convertView.findViewById(R.id.tv_margin_net_balance);
            tv_margin_rate = (TextView) convertView.findViewById(R.id.tv_margin_rate);
            fl_container0 = (FrameLayout) convertView.findViewById(R.id.fl_container0);
            ll_container1 = (LinearLayout) convertView.findViewById(R.id.ll_container1);
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
