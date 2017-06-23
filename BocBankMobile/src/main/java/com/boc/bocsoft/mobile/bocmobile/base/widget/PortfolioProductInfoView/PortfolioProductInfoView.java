package com.boc.bocsoft.mobile.bocmobile.base.widget.PortfolioProductInfoView;

import android.content.Context;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * 作者：XieDu
 * 创建时间：2016/11/7 21:01
 * 描述：
 */
public class PortfolioProductInfoView extends RelativeLayout {
    protected TextView tvProductCurrency;
    protected TextView tvProductName;
    protected TextView tvProductCode;

    public PortfolioProductInfoView(Context context) {
        this(context, null);
    }

    public PortfolioProductInfoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PortfolioProductInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View rootView = inflate(getContext(), R.layout.boc_view_portfolio_product_info, this);
        tvProductCurrency = (TextView) rootView.findViewById(R.id.tv_product_currency);
        tvProductName = (TextView) rootView.findViewById(R.id.tv_product_name);
        tvProductCode = (TextView) rootView.findViewById(R.id.tv_product_code);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        refreshLayout(widthSize);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void refreshLayout(int measuredWidthSize) {
        LayoutParams lp = (LayoutParams) tvProductCode.getLayoutParams();
        if (getTextTotalWidth() > measuredWidthSize) {//总长度超过了1行，产品代码需要另起一行
            lp.addRule(RelativeLayout.RIGHT_OF, 0);
            lp.addRule(RelativeLayout.BELOW, R.id.tv_product_currency);
        } else {
            lp.addRule(RelativeLayout.BELOW, 0);
            lp.addRule(RelativeLayout.RIGHT_OF, R.id.tv_product_name);
        }
        tvProductCode.setLayoutParams(lp);
    }

    public void setText(String currency, String productName, String productCode) {
        tvProductCurrency.setText(currency);
        tvProductName.setText(productName);
        tvProductCode.setText(productCode);
    }

    private float getTextTotalWidth() {
        TextPaint paintCurrency = tvProductCurrency.getPaint();
        TextPaint paintName = tvProductName.getPaint();
        TextPaint paintCode = tvProductCode.getPaint();
        return paintCurrency.measureText(tvProductCurrency.getText().toString())
                + paintName.measureText(tvProductName.getText().toString()) + paintCode.measureText(
                tvProductCode.getText().toString());
    }
}
