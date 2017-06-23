package com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.AccountUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.model.AccountListItemViewModel;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

/**
 * 只用于账户列表卡片控件中余额、额度等的显示，包括币种、类型、数量
 * Created by xdy on 2016/5/24.
 */
public class AccountListItemAmountView extends LinearLayout {

    protected TextView tvCurrencyCashRemit;
    protected TextView tvAmount;
    protected TextView tvDepositDebt;
    private Context mContext;
    private View rootView;

    public AccountListItemAmountView(Context context) {
        super(context);
        initView(context);
    }

    public AccountListItemAmountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AccountListItemAmountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        rootView = inflate(context, R.layout.boc_view_account_list_item_amount, this);
        tvCurrencyCashRemit = (TextView) rootView.findViewById(R.id.tv_currency_cash_remit);
        tvAmount = (TextView) rootView.findViewById(R.id.tv_amount);
        tvDepositDebt = (TextView) rootView.findViewById(R.id.tv_deposit_debt);
    }

    /**
     * 设置数据
     *
     * @param amountInfo 额度显示信息
     */
    public void setData(AccountListItemViewModel.CardAmountViewModel amountInfo) {
        if (amountInfo == null) {
            return;
        }
        StringBuilder currencyCashRemit = new StringBuilder(
                PublicCodeUtils.getCurrency(mContext, amountInfo.getCurrencyCode()));
        String cashRemit = AccountUtils.getCashRemit(amountInfo.getCashRemit());
        if (!StringUtils.isEmpty(cashRemit)) {
            currencyCashRemit.append("/").append(cashRemit);
        }
        if (StringUtils.isEmpty(amountInfo.getLoanBalanceLimitFlag())) {
            tvDepositDebt.setVisibility(GONE);
        } else {
            tvDepositDebt.setVisibility(VISIBLE);
            // 实时余额标志位 “0”-欠款;“1”-存款 ;2”-余额0
            tvDepositDebt.setText(getContext().getString(
                    "0".equals(amountInfo.getLoanBalanceLimitFlag()) ? R.string.boc_account_debt
                            : R.string.boc_account_deposit));
        }
        tvCurrencyCashRemit.setText(currencyCashRemit.toString());

        tvAmount.setText(MoneyUtils.transMoneyFormat(amountInfo.getBookBalance(), amountInfo.getCurrencyCode()));
    }
}
