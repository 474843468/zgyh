package com.boc.bocsoft.mobile.bocmobile.base.widget.BalanceHint;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;

import java.math.BigDecimal;

/**
 * @author wangyang
 *         16/6/25 11:22
 *         充值,转账提示内容控件
 */
public class BalanceHintView extends LinearLayout {

    /**
     * 提示内容,提示金额
     */
    private TextView tvContent, tvAmount;

    private double value;

    public BalanceHintView(Context context) {
        super(context);
        initView();
    }

    public BalanceHintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BalanceHintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BalanceHintView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.boc_view_finance_amount_hint, this);
        tvContent = (TextView) view.findViewById(R.id.tv_content);
        tvAmount = (TextView) view.findViewById(R.id.tv_amount);
    }

    public double getValue() {
        return value;
    }

    /**
     * 设置提示内容,及提示金额
     *
     * @param content
     * @param amount
     */
    public void setData(String content, BigDecimal amount) {
        setData(content, amount, ApplicationConst.CURRENCY_CNY);
    }

    public void setData(String content, BigDecimal amount, String currency) {
        setVisibility(VISIBLE);
        tvContent.setText(content);

        if (amount != null) {
            tvAmount.setText(PublicCodeUtils.getCurrency(getContext(), currency) + " " + MoneyUtils.transMoneyFormat(amount, currency));
            value = amount.doubleValue();
        }
    }

    public void setData(String content,String currency, BigDecimal amount, int... textColors) {
        if (textColors != null && textColors.length > 0) {
            if (textColors.length > 0 && textColors[0] > 0)
                tvContent.setTextColor(getResources().getColor(textColors[0]));

            if (textColors.length > 1 && textColors[1] > 0)
                tvAmount.setTextColor(getResources().getColor(textColors[1]));
        }

        tvContent.setText(content);
        tvAmount.setText(MoneyUtils.transMoneyFormat(amount, currency));
    }

    public void setTextContent(String content, String money) {
        String str = content + money;
        SpannableString spannableStringFir = new SpannableString(str);
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        spannableStringFir.setSpan(styleSpan, content.length(), str.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvContent.setText(spannableStringFir);
    }


    /**
     * 设置文字颜色
     */
    public void setDataColor(int colorId1, int colorId2) {
        tvContent.setTextColor(getResources().getColor(colorId1));
        tvAmount.setTextColor(getResources().getColor(colorId2));
    }
}
