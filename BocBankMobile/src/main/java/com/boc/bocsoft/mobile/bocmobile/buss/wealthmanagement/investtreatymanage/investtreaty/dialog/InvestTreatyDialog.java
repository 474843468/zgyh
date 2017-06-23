package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputDialogView;

/**
 * 协议明细界面的修改弹框
 * Created by guokai on 2016/9/16.
 */
public class InvestTreatyDialog extends TitleAndBtnDialog {

    private Context mConrext;
    private LinearLayout rootView;
    private TextView tvNotice, tvDesc, tvMinMoney, tvMaxMoney;
    private Button btn_left;
    private Button btn_right;
    private EditMoneyInputDialogView etPeriod, etMinAmount, etMaxAmount;
    //签约期数
    public String inputPeriod;
    //输入框结果
    public String inputMinAmount, inputMaxAmount;
    public CheckBox cbCheck;
    private LinearLayout llEtPeriod, llEtMinAmount, llEtMaxAmount;

    public InvestTreatyDialog(Context context) {
        super(context);
        this.mConrext = context;
    }

    @Override
    protected View onAddContentView() {
        rootView = (LinearLayout) View.inflate(mContext,
                R.layout.boc_dialog_invest_reaty_info, null);
        return this.rootView;
    }

    @Override
    protected void initView() {
        tvDesc = (TextView) rootView.findViewById(R.id.tv_desc);
        tvNotice = (TextView) rootView.findViewById(R.id.tv_notice);
        tvMinMoney = (TextView) rootView.findViewById(R.id.tv_min_money);
        tvMaxMoney = (TextView) rootView.findViewById(R.id.tv_max_money);
        cbCheck = (CheckBox) rootView.findViewById(R.id.cb_check);
        etPeriod = (EditMoneyInputDialogView) rootView.findViewById(R.id.et_period);
        etMinAmount = (EditMoneyInputDialogView) rootView.findViewById(R.id.et_min_amount);
        etMaxAmount = (EditMoneyInputDialogView) rootView.findViewById(R.id.et_max_amount);
        llEtPeriod = (LinearLayout) rootView.findViewById(R.id.ll_et_period);
        llEtMinAmount = (LinearLayout) rootView.findViewById(R.id.ll_et_min_amount);
        llEtMaxAmount = (LinearLayout) rootView.findViewById(R.id.ll_et_max_amount);
        btn_left = (Button) rootView.findViewById(R.id.btn_left);
        btn_right = (Button) rootView.findViewById(R.id.btn_right);
        etPeriod.setCurrency("027");
        etMinAmount.setMaxLeftNumber(12);
        etMinAmount.setMaxRightNumber(2);
        etMaxAmount.setMaxLeftNumber(12);
        etMaxAmount.setMaxRightNumber(2);
        etPeriod.setScrollView(rootView, this);
        etMinAmount.setScrollView(rootView, this);
        etMaxAmount.setScrollView(rootView, this);
    }

    /**
     * 局部组件显示（签约期数）
     */
    public void llEtPeriodVisibility(String period, boolean isPeriod) {
        llEtPeriod.setVisibility(View.VISIBLE);
        if (isPeriod) {
            etPeriod.setMaxLeftNumber(2);
        } else {
            etPeriod.setMaxLeftNumber(6);
        }
        etPeriod.setmContentMoneyEditText(period);
    }

    /**
     * 局部组件显示（签约期数不限期）
     */
    public void llEtBuyPeriodVisibility(String period, boolean isPeriod) {
        llEtPeriod.setVisibility(View.VISIBLE);
        if (isPeriod) {
            etPeriod.setMaxLeftNumber(2);
        } else {
            etPeriod.setMaxLeftNumber(6);
        }
        etPeriod.setVisibility(View.INVISIBLE);
        cbCheck.setVisibility(View.VISIBLE);
        cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etPeriod.setVisibility(View.INVISIBLE);
                } else {
                    etPeriod.setVisibility(View.VISIBLE);
                }
            }
        });
        etPeriod.setmContentMoneyEditText(period);
    }

    /**
     * 局部组件显示（最低预留金额、最大扣款金额）
     */
    public void llEtAmountVisibility(String minAmount, String maxAmount, String currency) {
        llEtMinAmount.setVisibility(View.VISIBLE);
        llEtMaxAmount.setVisibility(View.VISIBLE);
        tvMinMoney.setText(mContext.getString(R.string.boc_invest_treaty_trade_min_amount));
        tvMaxMoney.setText(mContext.getString(R.string.boc_invest_treaty_trade_max_amount));
        etMinAmount.setCurrency(currency);
        etMaxAmount.setCurrency(currency);
        etMinAmount.setmContentMoneyEditText(minAmount);
        etMaxAmount.setmContentMoneyEditText(maxAmount);
    }

    /**
     * 局部组件显示（赎回触发金额、购买触发金额）
     */
    public void llEtRedemmAmountVisibility(String minAmount, String maxAmount, String currency) {
        tvDesc.setVisibility(View.VISIBLE);
        tvDesc.setText(mContext.getString(R.string.boc_invest_treaty_money_zero_gou));
        tvMinMoney.setText(mContext.getString(R.string.boc_invest_treaty_trade_code_min_amount));
        tvMaxMoney.setText(mContext.getString(R.string.boc_invest_treaty_trade_code_max_amount));
        llEtMinAmount.setVisibility(View.VISIBLE);
        llEtMaxAmount.setVisibility(View.VISIBLE);
        etMinAmount.setCurrency(currency);
        etMaxAmount.setCurrency(currency);
        etMinAmount.setmContentMoneyEditText(minAmount);
        etMaxAmount.setmContentMoneyEditText(maxAmount);
    }

    /**
     * 局部组件显示（协议投资金额）
     */
    public void llEtInvestBaseAmountVisibility(String amount, String currency) {
        llEtMinAmount.setVisibility(View.VISIBLE);
        tvMinMoney.setText(mContext.getString(R.string.boc_invest_treaty_amount));
        etMinAmount.setCurrency(currency);
        etMinAmount.setmContentMoneyEditText(amount);
    }

    /**
     * 局部组件显示（协议金额/协议份额）
     */
    public void llEtBaseAmountVisibility(String amount, String currency, String tradeCode) {
        if ("0".equals(tradeCode)) {
            tvDesc.setVisibility(View.VISIBLE);
            tvDesc.setText(mContext.getString(R.string.boc_invest_treaty_money_zero));
            tvMinMoney.setText(mContext.getString(R.string.boc_invest_treaty_amount_fen_precent));
        } else {
            tvDesc.setVisibility(View.GONE);
            tvMinMoney.setText(mContext.getString(R.string.boc_invest_treaty_amount_jin_precent));
        }
        llEtMinAmount.setVisibility(View.VISIBLE);
        etMinAmount.setCurrency(currency);
        if ("-1.00".equals(amount)) {
            etMinAmount.setmContentMoneyEditText("0.00");
        } else {
            etMinAmount.setmContentMoneyEditText(amount);
        }
    }

    @Override
    protected void setListener() {
        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (R.id.btn_left == v.getId()) {
            if (btnClickCallBack != null) {
                btnClickCallBack.onLeftBtnClick(v);
            }
        } else if (R.id.btn_right == v.getId()) {
            inputPeriod = etPeriod.getContentMoney();
            inputMinAmount = etMinAmount.getContentMoney();
            inputMaxAmount = etMaxAmount.getContentMoney();
            if (btnClickCallBack != null) {
                btnClickCallBack.onRightBtnClick(v);
            }
        }
    }

    private DialogBtnClickCallBack btnClickCallBack;

    /**
     * 设置按钮点击监听
     *
     * @param callBack
     */
    public void setDialogBtnClickListener(DialogBtnClickCallBack callBack) {
        btnClickCallBack = callBack;
    }

    public interface DialogBtnClickCallBack {
        public void onLeftBtnClick(View view);

        public void onRightBtnClick(View view);
    }

    /**
     * 设置提示内容
     *
     * @param content
     * @return
     */
    public TitleAndBtnDialog setNoticeContent(String content) {
        tvNotice.setText(content);
        return this;
    }

}
