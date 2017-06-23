package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;

/**
 * Dialog：双向宝-账户管理-保证金转出确认对话框
 * Created by zhx on 2016/12/6
 */
public class BailTransferConfirmDialog extends BaseDialog {
    private LinearLayout rootView;
    private TextView tv_margin_account_no;
    private TextView tv_cash_banlance;
    private TextView tv_remit_banlance;
    private TextView tv_cancel;
    private TextView tv_ok;

    public BailTransferConfirmDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected View onAddContentView() {
        rootView = (LinearLayout) View.inflate(mContext,
                R.layout.boc_dialog_bail_transfer_confirm, null);
        return rootView;
    }

    @Override
    protected void initView() {
        tv_margin_account_no = (TextView) rootView.findViewById(R.id.tv_margin_account_no);
        tv_cash_banlance = (TextView) rootView.findViewById(R.id.tv_cash_banlance);
        tv_remit_banlance = (TextView) rootView.findViewById(R.id.tv_remit_banlance);
        tv_cancel = (TextView) rootView.findViewById(R.id.tv_cancel);
        tv_ok = (TextView) rootView.findViewById(R.id.tv_ok);
    }

    /**
     * 设置账户
     *
     * @param account
     */
    public void setAccount(String account) {
        tv_margin_account_no.setText(account);
    }

    /**
     * @param currency      币种
     * @param cashBanlance  钞（如果是人民币，只有这一项）
     * @param remitBanlance 汇
     */
    public void setAmount(String currency, String cashBanlance, String remitBanlance) {
        if (TextUtils.isEmpty(currency)) {
            return;
        }

        String currencyStr = PublicCodeUtils.getCurrency(mContext, currency);

        if ("001".equals(currency)) {
            tv_cash_banlance.setVisibility(View.INVISIBLE);

            String renminbi = MoneyUtils.transMoneyFormat(cashBanlance, currency);
            tv_remit_banlance.setText(renminbi + " 元");
        } else {
            String cashBanlanceStr = MoneyUtils.transMoneyFormat(cashBanlance, currency);
            tv_cash_banlance.setText(cashBanlanceStr + " " + currencyStr + "/钞");

            String remitBanlanceStr = MoneyUtils.transMoneyFormat(remitBanlance, currency);
            tv_remit_banlance.setText(remitBanlanceStr + " " + currencyStr + "/汇");
        }
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void setListener() {
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BailTransferConfirmDialog.this.dismiss();
            }
        });

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BailTransferConfirmDialog.this.dismiss();
                if (onConfirmListener != null) {
                    onConfirmListener.onConfirm();
                }
            }
        });
    }

    public interface OnConfirmListener {
        void onConfirm();
    }

    private OnConfirmListener onConfirmListener;

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }
}
