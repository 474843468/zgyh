package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.collection.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditDialogWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;

/**
 * Created by fanbin on 16/10/9.
 */
public class QRCollectionSetMoneyFragment extends BussFragment {
    private View mRootView;
    private LayoutInflater mInflater;

    private EditMoneyInputWidget et_boc_setmoney_fragment_money;
    private Button bt_boc_fragment_setmoney_queding;
    private Button bt_boc_fragment_setmoney_queding_touming;
    private TextView tv_boc_fragment_setmoney_addbeizhu;
    private ImageView iv_xiaotubiao;
    private EditDialogWidget editDialogWidget;
    public static final int SET_MONEY_FRAGMENT=12121;
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        this.mInflater = mInflater;
        mRootView = View.inflate(mContext, R.layout.boc_dialog_setting_money, null);
        return mRootView;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    public void initView() {
        et_boc_setmoney_fragment_money= (EditMoneyInputWidget) mRootView.findViewById(R.id.et_boc_setmoney_fragment_money);
        bt_boc_fragment_setmoney_queding= (Button) mRootView.findViewById(R.id.bt_boc_fragment_setmoney_queding);
        bt_boc_fragment_setmoney_queding_touming= (Button) mRootView.findViewById(R.id.bt_boc_fragment_setmoney_queding_touming);
        tv_boc_fragment_setmoney_addbeizhu= (TextView) mRootView.findViewById(R.id.tv_boc_fragment_setmoney_addbeizhu);
        iv_xiaotubiao= (ImageView) mRootView.findViewById(R.id.iv_xiaotubiao);

    }

    @Override
    public void initData() {
        et_boc_setmoney_fragment_money.setMaxLeftNumber(11);
        et_boc_setmoney_fragment_money.setMaxRightNumber(2);
        et_boc_setmoney_fragment_money.setClearIconVisible(false);
        et_boc_setmoney_fragment_money.setMoneyChangeListener(new EditMoneyInputWidget.MoneyChangeListenerInterface() {
            @Override
            public void setOnMoneyChangeListener(String str) {
                if (!TextUtils.isEmpty(str)) {
                    bt_boc_fragment_setmoney_queding_touming.setVisibility(View.GONE);
                } else {
                    bt_boc_fragment_setmoney_queding_touming.setVisibility(View.VISIBLE);
                }
            }
        });
        tv_boc_fragment_setmoney_addbeizhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialogWidget=new EditDialogWidget(mContext,10);
                editDialogWidget.setEditWidgetHint("收款备注");
                editDialogWidget.setRightStyle(getResources().getColor(R.color.boc_text_money_color_red), getResources().getColor(R.color.boc_common_cell_color));
                editDialogWidget.setEditDialogListener(new EditDialogWidget.EditDialogCallBack() {
                    @Override
                    public void onClick(String strEditTextContent) {
                        if (!TextUtils.isEmpty(strEditTextContent)) {
                            tv_boc_fragment_setmoney_addbeizhu.setText(strEditTextContent);
                            tv_boc_fragment_setmoney_addbeizhu.setTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
                            iv_xiaotubiao.setVisibility(View.VISIBLE);
                        }
                        editDialogWidget.dismiss();
                    }
                });
                editDialogWidget.show();

            }
        });
        bt_boc_fragment_setmoney_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strMoney=et_boc_setmoney_fragment_money.getContentMoney();
                String strBeizhu=tv_boc_fragment_setmoney_addbeizhu.getText().toString();
                if (TextUtils.isEmpty(strMoney)){
                    showErrorDialog("金额不能为空");
                    return;
                }else{
                    Double money=Double.parseDouble(strMoney);
                    if (money==0){
                        showErrorDialog("输入金额错误");
                        return;
                    }
                }
                Bundle bundle=new Bundle();
                bundle.putCharSequence("str_money",strMoney);
                bundle.putCharSequence("str_beizhu",strBeizhu);
                QRCollectionSetMoneyFragment.this.setFramgentResult(SET_MONEY_FRAGMENT, bundle);
                QRCollectionSetMoneyFragment.this.pop();
            }
        });
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }
    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_qrpay_setting_money);
    }
}
