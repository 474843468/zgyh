package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRow;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.model.PaymentDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.presenter.PaymentDetailPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

/**
 * 付款明细页面
 * Created by wangtong on 2016/6/28.
 */
public class PaymentDetailFragment extends MvpBussFragment<PaymentDetailPresenter> implements PaymentDetailContact.View {

    private View rootView;
    //数据模型
    private PaymentDetailModel model;
    protected TextView payCurrency;
    protected TextView payState;
    protected TextView payAmount;
    protected DetailTableRow payAlready;
    protected DetailTableRow payeeName;
    protected DetailTableRow payeeAccount;
    protected DetailTableRow payeePhone;
    protected DetailTableRow payName;
    protected DetailTableRow payAccount;
    protected DetailTableRow payPhone;
    protected DetailTableRow payCustomerCode;
    protected DetailTableRow tips;
    protected DetailTableRow orderDate;
    protected DetailTableRow startChannel;
    protected DetailTableRow payChannel;
    protected DetailTableRow orderNumber;
    protected TextView payConfirm;
    protected TextView fragmentTitle;
    protected ImageView btnBack;
    protected DetailTableRow payDate;
    protected LinearLayout ll_content;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_payment_detail, null);
        return rootView;
    }

    @Override
    public void initView() {
        payCurrency = (TextView) rootView.findViewById(R.id.pay_currency);
        payState = (TextView) rootView.findViewById(R.id.pay_state);
        payAmount = (TextView) rootView.findViewById(R.id.pay_amount);
        payAlready = (DetailTableRow) rootView.findViewById(R.id.pay_already);
        payeeName = (DetailTableRow) rootView.findViewById(R.id.payee_name);
        payeeAccount = (DetailTableRow) rootView.findViewById(R.id.payee_account);
        payeePhone = (DetailTableRow) rootView.findViewById(R.id.payee_phone);
        payName = (DetailTableRow) rootView.findViewById(R.id.pay_name);
        payAccount = (DetailTableRow) rootView.findViewById(R.id.pay_account);
        payPhone = (DetailTableRow) rootView.findViewById(R.id.pay_phone);
        payCustomerCode = (DetailTableRow) rootView.findViewById(R.id.pay_customer_code);
        tips = (DetailTableRow) rootView.findViewById(R.id.tips);
        orderDate = (DetailTableRow) rootView.findViewById(R.id.order_date);
        startChannel = (DetailTableRow) rootView.findViewById(R.id.start_channel);
        payChannel = (DetailTableRow) rootView.findViewById(R.id.pay_channel);
        orderNumber = (DetailTableRow) rootView.findViewById(R.id.order_number);
        payConfirm = (TextView) rootView.findViewById(R.id.pay_confirm);
        fragmentTitle = (TextView) rootView.findViewById(R.id.fragment_title);
        btnBack = (ImageView) rootView.findViewById(R.id.btn_back);
        payDate = (DetailTableRow) rootView.findViewById(R.id.pay_date);
        ll_content = (LinearLayout) rootView.findViewById(R.id.ll_content);
        ll_content.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        fragmentTitle.setText("明细");
        String notifyId = getArguments().getString("notifyId");
        model = new PaymentDetailModel();
        model.setNotifyId(notifyId);
        getPresenter().psnTransActPaymentOrderDetail();
    }

    @Override
    public void setListener() {
        payConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmPaymentFragment fragment = new ConfirmPaymentFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("model", model);
                fragment.setArguments(bundle);
                start(fragment);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
    }

    private void updateViews() {
        payCurrency.setText(getString(R.string.boc_transfer_payment_currency,
                PublicCodeUtils.getCurrency(getActivity(), model.getTrfCur())));

        if (model.getStatus().equals("2")) {
            // 已付状态显示，已付金额，付款日期，付款渠道，付款账号，其他状态不显示
            payAlready.setVisibility(View.VISIBLE);
            payAlready.updateValue(MoneyUtils.transMoneyFormat(model.getTrfAmount(), model.getTrfCur()));
            payAccount.setVisibility(View.VISIBLE);
            payAccount.updateValue(NumberUtils.formatCardNumberStrong(model.getPayerAccountNumber()));
            payDate.setVisibility(View.VISIBLE);
            payDate.updateValue(model.getPaymentDate());
            payChannel.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(model.getTrfChannel())) {
                payChannel.updateValue("-");
            } else if ("1".equals(model.getTrfChannel())) {
                payChannel.updateValue("网上银行");
            } else if ("2".equals(model.getTrfChannel())) {
                payChannel.updateValue("手机银行");
            }
            payState.setText("已付");
            payState.setBackgroundResource(R.drawable.boc_transaction_status_bg_green);
            payConfirm.setVisibility(View.GONE);
        } else if (model.getStatus().equals("3")) {
            payState.setText("已撤销");
            payState.setBackgroundResource(R.drawable.boc_transaction_status_bg_red);
            payConfirm.setVisibility(View.GONE);
        } else if (model.getStatus().equals("4")) {
            payState.setText("状态未明");
            payState.setBackgroundResource(R.drawable.boc_transaction_status_bg_red);
            payConfirm.setVisibility(View.GONE);
        } else if (model.getStatus().equals("5")) {
            payState.setText("过期未付");
            payState.setBackgroundResource(R.drawable.boc_transaction_status_bg_red);
            payConfirm.setVisibility(View.GONE);
        }
        payAmount.setText(MoneyUtils.transMoneyFormat(model.getRequestAmount(), model.getTrfCur()));
        payeeName.updateValue(model.getPayeeName());
        payeeAccount.updateValue(model.getPayeeAccountNumber());
        payeePhone.updateValue(NumberUtils.formatMobileNumber(model.getPayeeMobile()));
        payName.updateValue(model.getPayerName());
        payPhone.updateValue(NumberUtils.formatMobileNumber(model.getPayerMobile()));
        payCustomerCode.updateValue(model.getPayerCustomerId());
        // 附言不为空才显示
        if (!StringUtils.isEmpty(model.getFurInfo())) {
            tips.setVisibility(View.VISIBLE);
            tips.updateValue(model.getFurInfo());
        }

        orderDate.updateValue(model.getCreateDate());
        if ("1".equals(model.getCreateChannel())) {
            startChannel.updateValue("网上银行");
        } else {
            startChannel.updateValue("手机银行");
        }
        orderNumber.updateValue(model.getNotifyId() + "");
    }

    @Override
    public PaymentDetailModel getModel() {
        return model;
    }

    @Override
    public void paymentOrderDetailReturned() {
        ll_content.setVisibility(View.VISIBLE);
        updateViews();
    }

    @Override
    public void setPresenter(PaymentDetailContact.Presenter presenter) {

    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    protected PaymentDetailPresenter initPresenter() {
        return new PaymentDetailPresenter(this);
    }
}
