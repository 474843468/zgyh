package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.common;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.SimpleListViewDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.moneyinputview.MoneyInputTextView;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.home.BuyExchangeHomeContract;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.home.BuyExchangeHomePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.home.model.PriceModel;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.boc.bocsoft.mobile.bocmobile.R.id.cus_remit;

/**
 * 计算器页面
 * Created by gwluo on 2016/12/29.
 */

public class BuySellCalculatorFragment extends MvpBussFragment<BuyExchangeHomePresenter> implements View.OnClickListener, BuyExchangeHomeContract.View {

    private View root;
    private TextView tv_cash_remit_type;
    private TextView tv_currency_foreign;
    private ImageView iv_cash_remit_change;
    private ImageView iv_national_flag_foreign;
    private String currency = "USD";
    private boolean isBuy = true;//是否购汇

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        root = mInflater.inflate(R.layout.fragment_buy_sell_calculator, null);
        return root;
    }

    private MoneyInputTextView cus_remit;//汇
    private MoneyInputTextView cus_cash;//钞
    private MoneyInputTextView cus_china_money;//人民币

    @Override
    public void initView() {
        tv_cash_remit_type = mViewFinder.find(R.id.tv_cash_remit_type);
        iv_cash_remit_change = mViewFinder.find(R.id.iv_cash_remit_change);
        iv_national_flag_foreign = mViewFinder.find(R.id.iv_national_flag_foreign);
        tv_currency_foreign = mViewFinder.find(R.id.tv_currency_foreign);
        cus_remit = mViewFinder.find(R.id.cus_remit);
        cus_cash = mViewFinder.find(R.id.cus_cash);
        cus_china_money = mViewFinder.find(R.id.cus_china_money);
    }

    @Override
    public void initData() {
        if (ApplicationContext.isLogin()) {
            getPresenter().psnFessQueryQuotePrice();
        } else {
            getPresenter().psnGetExchangeOutlayParams();
        }

    }

    @Override
    public void setListener() {
        iv_cash_remit_change.setOnClickListener(this);
        tv_currency_foreign.setOnClickListener(this);
        cus_remit.addTextChangedListener(cusRemitWatcher);
        cus_cash.addTextChangedListener(cusCashtWatcher);
        cus_china_money.addTextChangedListener(cusRmbWatcher);
    }

    /**
     * 汇
     */
    private TextWatcher cusRemitWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (StringUtils.isEmpty(s.toString())) return;
            BigDecimal amount = new BigDecimal(MoneyUtils.getNormalMoneyFormat(s.toString()));
            BigDecimal rmb = amount.multiply(getPrice(currency, false, isBuy)).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
            cus_china_money.setText("");
            cus_china_money.setHint(rmb.toPlainString());
            BigDecimal price = getPrice(currency, true, isBuy);
            if (price.compareTo(new BigDecimal("0")) != 0) {
                BigDecimal cash = rmb.multiply(new BigDecimal("100")).divide(price, 2, BigDecimal.ROUND_HALF_UP);
                cus_cash.setText("");
                cus_cash.setHint(cash.toPlainString());
            }
        }
    };

    /**
     * 获取牌价
     *
     * @param currency
     * @param isCash   是否为钞
     * @param isBuy    是否购汇
     * @return
     */
    private BigDecimal getPrice(String currency, boolean isCash, boolean isBuy) {
        String price = "";
        for (PriceModel model : priceList) {
            if (model.getCurrency().equals(currency)) {
                if (isCash) {
                    if (isBuy) {
                        price = model.getBuyNoteRate();
                    } else {
                        price = model.getSellNoteRate();
                    }
                } else {
                    if (isBuy) {
                        price = model.getBuyRate();
                    } else {
                        price = model.getSellRate();
                    }
                }
                if (StringUtils.isEmpty(price)) {
                    return new BigDecimal("0");
                }
                return new BigDecimal(price);
            }
        }
        return new BigDecimal("0");
    }

    /**
     * 钞
     */
    private TextWatcher cusCashtWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (StringUtils.isEmpty(s.toString())) return;
            BigDecimal amount = new BigDecimal(MoneyUtils.getNormalMoneyFormat(s.toString()));
            BigDecimal rmb = amount.multiply(getPrice(currency, true, isBuy)).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
            cus_china_money.setText("");
            cus_china_money.setHint(rmb.toPlainString());
            BigDecimal price = getPrice(currency, false, isBuy);
            //计算汇
            if (price.compareTo(new BigDecimal("0")) != 0) {
                BigDecimal remit = rmb.multiply(new BigDecimal("100")).divide(price, 2, BigDecimal.ROUND_HALF_UP);
                cus_remit.setText("");
                cus_remit.setHint(remit.toPlainString());
            }
        }
    };
    /**
     * 人民币
     */
    private TextWatcher cusRmbWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (StringUtils.isEmpty(s.toString())) return;
            BigDecimal rmb = new BigDecimal(MoneyUtils.getNormalMoneyFormat(s.toString()));
            BigDecimal remitPrice = getPrice(currency, false, isBuy);
            if (remitPrice.compareTo(new BigDecimal("0")) != 0) {
                BigDecimal remit = rmb.multiply(new BigDecimal("100")).divide(remitPrice, 2, BigDecimal.ROUND_HALF_UP);
                cus_remit.setText("");
                cus_remit.setHint(remit.toPlainString());
            }
            BigDecimal cashPrice = getPrice(currency, true, isBuy);
            if (cashPrice.compareTo(new BigDecimal("0")) != 0) {
                BigDecimal cash = rmb.multiply(new BigDecimal("100")).divide(cashPrice, 2, BigDecimal.ROUND_HALF_UP);
                cus_cash.setText("");
                cus_cash.setHint(cash.toPlainString());
            }
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_cash_remit_change) {//
            onCashRemitChange();
        } else if (id == R.id.tv_currency_foreign) {
            onCurrencyChange();
        }
    }

    /**
     * 外币选择
     */
    private void onCurrencyChange() {
        List<String> currencyList = BuyExchangeCurrencyUtil.
                getInstance(mContext).getCurrencyList();
        showChoiceDialog("选择币种", currencyList);
    }

    protected void showChoiceDialog(String title, List<String> performList) {
        final SimpleListViewDialog dialog = new SimpleListViewDialog(mContext);
        dialog.isShowBottomBtn(false).isShowTitle(true).setTitle(title);
        dialog.setData(performList);
        dialog.setOnSelectListener(new SimpleListViewDialog.OnSelectListener<String>() {
            @Override
            public void onItemClick(int position, String str) {
                tv_currency_foreign.setText(str);
                currency = PublicCodeUtils.getCurrencyWithLetter(mContext, str);
                iv_national_flag_foreign.setBackgroundResource(
                        BuyExchangeCurrencyUtil.getNationalFlagFromCurrency(
                                PublicCodeUtils.getCurrency(mContext, str)));
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 结构汇切换
     */
    private void onCashRemitChange() {
        isBuy = tv_cash_remit_type.getText().toString().equals("结汇");
        if (isBuy) {
            tv_cash_remit_type.setText("购汇");
//            iv_cash_remit_change.setBackgroundResource(R.drawable.abc_ab_share_pack_holo_dark);
        } else {
            tv_cash_remit_type.setText("结汇");
//            iv_cash_remit_change.setBackgroundResource(R.drawable.abc_ab_share_pack_holo_dark);
        }
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_calculator);
    }

    @Override
    protected BuyExchangeHomePresenter initPresenter() {
        return new BuyExchangeHomePresenter(this);
    }

    private List<PriceModel> priceList = new ArrayList<>();//牌价列表

    @Override
    public void getDataSucc(List<PriceModel> list) {
        if (list == null) {
            priceList.clear();
            return;
        }
        priceList = list;
    }

    @Override
    public void getDataFail() {

    }

    @Override
    public void setPresenter(BuyExchangeHomeContract.Presenter presenter) {

    }
}
