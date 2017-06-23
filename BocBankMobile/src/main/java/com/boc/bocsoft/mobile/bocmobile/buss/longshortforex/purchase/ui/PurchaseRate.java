package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.moneyinputview.MoneyInputTextView;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;

import static com.boc.bocsoft.mobile.bocmobile.R.drawable.boc_longshortforex_purchase_green;


/**
 * @author wangyang
 *         2016/12/26 15:20
 *         汇率输入框
 */
public class PurchaseRate extends LinearLayout implements View.OnClickListener, TextWatcher {

    private ViewGroup llSecond, llFirstTab;

    private MoneyInputTextView etRateFirst, etRateSecond;

    private ImageView ivPlusFirst, ivMinFirst, ivPlusSecond, ivMinSecond, ivLineFirst;

    private BigDecimal step;

    private OnRateChangeListener changeListener;

    private TextView tvProfit, tvLosses, tvTitleFirst;

    public PurchaseRate(Context context) {
        super(context);
        init();
    }

    public PurchaseRate(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PurchaseRate(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PurchaseRate(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.boc_view_purchase_rate, this);
        llSecond = (ViewGroup) view.findViewById(R.id.ll_second);
        llFirstTab = (ViewGroup) view.findViewById(R.id.ll_first_tab);

        tvProfit = (TextView) view.findViewById(R.id.tv_profit);
        tvLosses = (TextView) view.findViewById(R.id.tv_losses);
        tvTitleFirst = (TextView) view.findViewById(R.id.tv_title_first);

        etRateFirst = (MoneyInputTextView) view.findViewById(R.id.et_rate_first);
        ivPlusFirst = (ImageView) view.findViewById(R.id.iv_plus_first);
        ivMinFirst = (ImageView) view.findViewById(R.id.iv_min_first);
        ivLineFirst = (ImageView) view.findViewById(R.id.iv_line_first);

        etRateSecond = (MoneyInputTextView) view.findViewById(R.id.et_rate_second);
        ivPlusSecond = (ImageView) view.findViewById(R.id.iv_plus_second);
        ivMinSecond = (ImageView) view.findViewById(R.id.iv_min_second);

        ivPlusFirst.setOnClickListener(this);
        ivMinFirst.setOnClickListener(this);
        ivPlusSecond.setOnClickListener(this);
        ivMinSecond.setOnClickListener(this);

        etRateFirst.addTextChangedListener(this);
        etRateSecond.addTextChangedListener(this);

        tvProfit.setOnClickListener(this);
        tvLosses.setOnClickListener(this);

        etRateFirst.setHint(getResources().getString(R.string.boc_common_input_hint));
        etRateSecond.setHint(getResources().getString(R.string.boc_common_input_hint));
    }

    public void showProfitTab(String rate) {
        showProfitTab(rate, step.floatValue());
    }

    public void showProfitTab(String rate, float step) {
        tvProfit.setSelected(true);
        changeFirstTab(getResources().getColor(R.color.boc_text_color_red), R.drawable.boc_longshortforex_purchase_red);
        llSecond.setVisibility(GONE);
        llFirstTab.setVisibility(VISIBLE);
        tvTitleFirst.setVisibility(GONE);
        if (!StringUtils.isEmptyOrNull(rate))
            etRateFirst.setInputMoney(rate);

    }

    public void showLossesTab(String rate) {
        showLossesTab(rate, step.floatValue());
    }

    public void showLossesTab(String rate, float step) {
        tvLosses.setSelected(true);
        changeFirstTab(getResources().getColor(R.color.boc_text_color_green), boc_longshortforex_purchase_green);
        llSecond.setVisibility(GONE);
        llFirstTab.setVisibility(VISIBLE);
        tvTitleFirst.setVisibility(GONE);
        if (!StringUtils.isEmptyOrNull(rate))
            etRateFirst.setInputMoney(rate);
    }

    private void changeFirstTab(int textColor, int resId) {
        etRateFirst.setTextColor(textColor);
        ivLineFirst.setImageResource(resId);
    }

    public void setFirstRate(String title, String rate, float step) {
        if (!StringUtils.isEmptyOrNull(title))
            tvTitleFirst.setText(title);
        if (!StringUtils.isEmptyOrNull(rate))
            etRateFirst.setInputMoney(rate);
        llFirstTab.setVisibility(GONE);
        tvTitleFirst.setVisibility(VISIBLE);
        this.step = new BigDecimal(step);
    }

    public void setSecondRate(String rate, float step) {
        llSecond.setVisibility(VISIBLE);
        llFirstTab.setVisibility(GONE);
        tvTitleFirst.setVisibility(VISIBLE);
        if (!StringUtils.isEmptyOrNull(rate))
            etRateSecond.setInputMoney(rate);
        this.step = new BigDecimal(step);
    }

    @Override
    public void onClick(View v) {
        if (v == ivPlusFirst)
            changeTextByStp(etRateFirst, true);
        if (v == ivMinFirst)
            changeTextByStp(etRateFirst, false);
        if (v == ivPlusSecond)
            changeTextByStp(etRateSecond, true);
        if (v == ivMinSecond)
            changeTextByStp(etRateSecond, false);
        if (v == tvProfit) {
            tvProfit.setSelected(true);
            tvLosses.setSelected(false);
            changeFirstTab(getResources().getColor(R.color.boc_text_color_red), R.drawable.boc_longshortforex_purchase_red);
        }
        if (v == tvLosses) {
            tvProfit.setSelected(false);
            tvLosses.setSelected(true);
            changeFirstTab(getResources().getColor(R.color.boc_text_color_green), R.drawable.boc_longshortforex_purchase_green);
        }
    }

    private void changeTextByStp(MoneyInputTextView editText, boolean isPlus) {
        BigDecimal bigDecimal = new BigDecimal(editText.getInputMoney());
        if (isPlus)
            bigDecimal = bigDecimal.add(step);
        else
            bigDecimal = bigDecimal.min(step);

        if (bigDecimal.floatValue() <= 0)
            return;
        editText.setInputMoney(bigDecimal.toPlainString());
    }

    public void setScrollView(View scrollView) {
        etRateSecond.setScrollView(scrollView);
        etRateFirst.setScrollView(scrollView);
    }

    public void setOnChangeListener(OnRateChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (changeListener != null)
            changeListener.onRateChange(new BigDecimal(etRateFirst.getInputMoney()).floatValue(), new BigDecimal(etRateSecond.getInputMoney()).floatValue());
    }

    public interface OnRateChangeListener {
        void onRateChange(float FirstRate, float SecondRate);
    }
}
