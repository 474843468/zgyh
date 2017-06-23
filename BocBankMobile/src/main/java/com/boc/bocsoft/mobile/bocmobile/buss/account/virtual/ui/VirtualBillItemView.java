package com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.model.VirtualBillModel;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author wangyang
 *         16/9/3 23:16
 *         虚拟银行卡账单View
 */
public class VirtualBillItemView extends LinearLayout {

    private TextView tvDate, tvTitle, tvCurrency, tvAmount, tvCurrency1, tvAmount1;

    private List<VirtualBillModel> models;

    public VirtualBillItemView(Context context) {
        super(context);
        init();
    }

    public VirtualBillItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VirtualBillItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VirtualBillItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.boc_fragment_virtual_bill_view, this);
        tvDate = (TextView) view.findViewById(R.id.tv_date);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvCurrency = (TextView) view.findViewById(R.id.tv_currency);
        tvAmount = (TextView) view.findViewById(R.id.tv_amount);
        tvCurrency1 = (TextView) view.findViewById(R.id.tv_currency1);
        tvAmount1 = (TextView) view.findViewById(R.id.tv_amount1);
    }

    /**
     * 设置已出账单
     *
     * @param models
     * @param isCurrent
     */
    public void setData(List<VirtualBillModel> models, boolean isCurrent) {
        setData(models, false, isCurrent);
    }

    /**
     * 未出账单设置收入,支出
     *
     * @param models
     */
    public void setUnsettled(List<VirtualBillModel> models, boolean isTotalIn) {
        this.models = models;
        //隐藏日期
        tvDate.setVisibility(GONE);

        //设置标题
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        if (isTotalIn)
            tvTitle.setText(getResources().getString(R.string.boc_virtual_account_bill_total_in));
        else
            tvTitle.setText(getResources().getString(R.string.boc_virtual_account_bill_total_out));
        initCurrencyAmount(isTotalIn);
    }

    /**
     * 设置未出账单
     *
     * @param models
     */
    public void setData(List<VirtualBillModel> models) {
        setData(models, true, false);
    }

    public List<VirtualBillModel> getData() {
        return models;
    }

    private void setData(List<VirtualBillModel> models, boolean isUnsettled, boolean isCurrent) {
        this.models = models;
        if (isUnsettled)
            initUnsettled();
        else
            initSettled(isCurrent);
    }

    private void initSettled(boolean isCurrent) {
        if (models != null && !models.isEmpty())
            tvDate.setText(models.get(0).getBillDate().format(DateFormatters.monthFormatter2));

        //本期账单,添加标题
        if (isCurrent) {
            tvTitle.setVisibility(VISIBLE);
            tvTitle.setTextColor(getResources().getColor(R.color.boc_text_color_green));
            tvTitle.setText(getResources().getString(R.string.boc_virtual_account_detail_current_bill));
        }else
            tvTitle.setVisibility(GONE);

        initCurrencyAmount(true);
    }

    private void initUnsettled() {
        //隐藏日期
        tvDate.setVisibility(GONE);

        //设置标题
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tvTitle.setText(getResources().getString(R.string.boc_virtual_account_detail_bill));

        //设置第一个金额币种
        initCurrencyAmount(true);
    }

    private void initCurrencyAmount(boolean isTotalOut) {
        if (models == null || models.isEmpty())
            return;

        //设置第一个金额币种
        VirtualBillModel firstModel = models.get(0);
        tvCurrency.setText(PublicCodeUtils.getCurrency(getContext(), firstModel.getCurrency()));

        BigDecimal firstAmount = firstModel.getTotalIn();
        if (isTotalOut)
            firstAmount = firstModel.getTotalOut();

        if (firstAmount.compareTo(new BigDecimal(0)) == 1)
            tvAmount.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tvAmount.setText(MoneyUtils.transMoneyFormat(firstAmount, firstModel.getCurrency()));

        if (models.size() == 1)
            return;

        findViewById(R.id.ll_currency_amount1).setVisibility(VISIBLE);
        //设置第二个金额币种
        VirtualBillModel secondModel = models.get(1);
        tvCurrency1.setText(PublicCodeUtils.getCurrency(getContext(), secondModel.getCurrency()));

        BigDecimal secondAmount = firstModel.getTotalIn();
        if (isTotalOut)
            secondAmount = firstModel.getTotalOut();

        if (secondAmount.compareTo(new BigDecimal(0)) == 1)
            tvAmount1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tvAmount1.setText(MoneyUtils.transMoneyFormat(secondAmount, secondModel.getCurrency()));
    }
}
