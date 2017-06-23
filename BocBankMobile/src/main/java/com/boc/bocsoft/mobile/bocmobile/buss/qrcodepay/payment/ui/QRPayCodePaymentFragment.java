package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.payment.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.moneyinputview.MoneyInputTextView;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.scan.model.QRCodeContentBean;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.scan.model.QRPayScanPaymentViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.scan.presenter.QRPayScanPaymentPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.scan.ui.QRPayScanFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.scan.ui.QRPayScanPaymentContract;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui.QRPayMainFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.widget.QRPayPwdDialog;

/**
 * 二维码支付 - 付款 - 付款二维码 - 反扫后商户未设置支付金额
 * Created by wangf on 2016/8/23.
 */
public class QRPayCodePaymentFragment extends BussFragment {

    private View mRootView;

    //商户名称的Layout（无图标）
    private LinearLayout llPaymentNameNO;
    //商户名称（无图标）
    private TextView tvPaymentNameNO;
    //金额输入
    private MoneyInputTextView mtvInputMoney;
    //确认按钮
    private Button btnPaymentConfirm;
    //支付账户
    private TextView tvPaymentAccount;
    //修改支付账户
    private TextView tvPaymentAccountChange;




    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = View.inflate(mContext, R.layout.boc_fragment_qrpay_scan_payment, null);
        return mRootView;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_qrpay_payment);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
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
        llPaymentNameNO = (LinearLayout) mRootView.findViewById(R.id.ll_qrpay_scan_payment_name1);
        tvPaymentNameNO = (TextView) mRootView.findViewById(R.id.tv_qrpay_scan_payment_name);
        mtvInputMoney = (MoneyInputTextView) mRootView.findViewById(R.id.mtv_qrpay_scan_payment_money);
        btnPaymentConfirm = (Button) mRootView.findViewById(R.id.btn_qrpay_scan_payment_confirm);
        tvPaymentAccount = (TextView) mRootView.findViewById(R.id.tv_qrpay_scan_payment_account);
        tvPaymentAccountChange = (TextView) mRootView.findViewById(R.id.tv_qrpay_scan_payment_account_change);
    }

    @Override
    public void initData() {

//        tvPaymentNameNO.setText("向" + qrCodeContentBean.getN() + "付款");
    }

    @Override
    public void setListener() {
        btnPaymentConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog();
//                mPayScanPaymentPresenter.loadGetRandom();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }



}