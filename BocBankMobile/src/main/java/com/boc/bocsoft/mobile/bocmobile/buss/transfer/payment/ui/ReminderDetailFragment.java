package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.ui;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRow;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ConfirmDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.model.ReminderDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.presenter.ReminderDetailPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 收款详情页
 * Created by wangtong on 2016/6/28.
 */
public class ReminderDetailFragment extends MvpBussFragment<ReminderDetailPresenter> implements ReminderDetailContact.View {

    protected TextView payCurrency;
    protected TextView payState;
    protected TextView payAmount;
    protected DetailTableRow payAlready;
    protected DetailTableRow payeeName;
    protected DetailTableRow payeeAccount;
    protected DetailTableRow payeePhone;
    protected DetailTableRow payName;
    protected DetailTableRow payCustomerCode;
    protected DetailTableRow tips;
    protected DetailTableRow orderDate;
    protected DetailTableRow startChannel;
    protected DetailTableRow orderNumber;
    protected ImageView btnBack;
    protected TextView fragmentTitle;
    protected TextView revertReminder;
    protected TextView smsReminder;
    protected DetailTableRow payerAccount;
    protected DetailTableRow payerMobile;
    protected DetailTableRow payerDate;
    protected DetailTableRow payerChannel;
    protected DetailTableRow transNumber;
    protected LinearLayout ll_content;
    private View rootView;
    //数据模型
    private ReminderDetailModel model;
    private String payAccount;
    private String payDate;
    private String trandID;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_reminder_detail, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        payCurrency = (TextView) rootView.findViewById(R.id.pay_currency);
        payState = (TextView) rootView.findViewById(R.id.pay_state);
        payAmount = (TextView) rootView.findViewById(R.id.pay_amount);
        payAlready = (DetailTableRow) rootView.findViewById(R.id.pay_already);
        payeeName = (DetailTableRow) rootView.findViewById(R.id.payee_name);
        payeeAccount = (DetailTableRow) rootView.findViewById(R.id.payee_account);
        payeePhone = (DetailTableRow) rootView.findViewById(R.id.payee_phone);
        payName = (DetailTableRow) rootView.findViewById(R.id.pay_name);
        payCustomerCode = (DetailTableRow) rootView.findViewById(R.id.pay_customer_code);
        tips = (DetailTableRow) rootView.findViewById(R.id.tips);
        orderDate = (DetailTableRow) rootView.findViewById(R.id.order_date);
        startChannel = (DetailTableRow) rootView.findViewById(R.id.start_channel);
        orderNumber = (DetailTableRow) rootView.findViewById(R.id.order_number);
        revertReminder = (TextView) rootView.findViewById(R.id.revert_reminder);
        btnBack = (ImageView) rootView.findViewById(R.id.btn_back);
        fragmentTitle = (TextView) rootView.findViewById(R.id.fragment_title);
        smsReminder = (TextView) rootView.findViewById(R.id.sms_reminder);
        payerAccount = (DetailTableRow) rootView.findViewById(R.id.payer_account);
        payerMobile = (DetailTableRow) rootView.findViewById(R.id.payer_mobile);
        payerDate = (DetailTableRow) rootView.findViewById(R.id.payer_date);
        payerChannel = (DetailTableRow) rootView.findViewById(R.id.payer_channel);
        transNumber = (DetailTableRow) rootView.findViewById(R.id.trans_number);
        ll_content = (LinearLayout) rootView.findViewById(R.id.ll_content);
        ll_content.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        super.initData();
        fragmentTitle.setText("明细");
        String notifyId = getArguments().getString("notifyId");
        model = new ReminderDetailModel();
        model.setNotifyId(notifyId);
        model.setPayeeAccountId(getAccountId(model.getPayeeAccountNumber()));
        getPresenter().psnTransActReminderOrderDetail();
    }

    @Override
    public void setListener() {
        super.setListener();
        revertReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ConfirmDialog dialog = new ConfirmDialog(getActivity());
                dialog.setMessage("请您再次确认是否撤销该笔交易？");
                dialog.setLeftButton("取消");
                dialog.setRightButton("确定");
                dialog.setButtonClickInterface(new ConfirmDialog.OnButtonClickInterface() {
                    @Override
                    public void onLeftClick(ConfirmDialog warnDialog) {
                        dialog.cancel();
                    }

                    @Override
                    public void onRightClick(ConfirmDialog warnDialog) {
                        getPresenter().psnTransActUndoReminderOrder();
                    }
                });
                dialog.show();
            }
        });

        smsReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().psnTransActReminderSms();
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

        trandID = "-";

        payCurrency.setText(getString(R.string.boc_transfer_reminder_currency,
                PublicCodeUtils.getCurrency(getActivity(), model.getTrfCur())));

        if (model.getStatus().equals("1")) {
            payState.setText("未付");
            payDate = "-";
            revertReminder.setVisibility(View.VISIBLE);
            revertReminder.setText("撤销");
            payState.setBackgroundResource(R.drawable.boc_transaction_status_bg_yellow);
        } else if (model.getStatus().equals("2")) {
            // 已付状态显示，已付金额，付款日期，付款渠道，付款账号，其他状态不显示
            payAlready.setVisibility(View.VISIBLE);
            payAlready.updateValue(MoneyUtils.transMoneyFormat(model.getTrfAmount() + "", model.getTrfCur()));
            payerAccount.setVisibility(View.VISIBLE);
            payerDate.setVisibility(View.VISIBLE);
            transNumber.setVisibility(View.VISIBLE);
            payerChannel.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(model.getPayerChannel())) {
                payerChannel.updateValue("-");
            } else if (model.getPayerChannel().equals("1")) {
                payerChannel.updateValue("网上银行");
            } else {
                payerChannel.updateValue("手机银行");
            }
            payState.setText("已付");
            trandID = model.getNotifyId();
            transNumber.updateValue(trandID);
            payDate = model.getPayerDate();
            payerDate.updateValue(payDate);
            payAccount = NumberUtils.formatCardNumber(model.getPayerAccount()) + "";
            payState.setBackgroundResource(R.drawable.boc_transaction_status_bg_green);
            revertReminder.setVisibility(View.GONE);
            smsReminder.setVisibility(View.GONE);
        } else if (model.getStatus().equals("3")) {
            payState.setText("已撤销");
            payDate = "-";
            payState.setBackgroundResource(R.drawable.boc_transaction_status_bg_red);
            revertReminder.setVisibility(View.GONE);
            smsReminder.setVisibility(View.GONE);
        } else if (model.getStatus().equals("4")) {
            payState.setText("状态未明");
            payDate = "-";
            payState.setBackgroundResource(R.drawable.boc_transaction_status_bg_red);
            revertReminder.setVisibility(View.GONE);
            smsReminder.setVisibility(View.GONE);
        } else if (model.getStatus().equals("5")) {
            payState.setText("过期未付");
            payDate = "-";
            payState.setBackgroundResource(R.drawable.boc_transaction_status_bg_red);
            revertReminder.setVisibility(View.GONE);
            smsReminder.setVisibility(View.GONE);
        }
        payAmount.setText(MoneyUtils.transMoneyFormat(model.getRequestAmount() + "", model.getTrfCur()));

        payeeName.updateValue(model.getPayeeName());
        payeeAccount.updateValue(NumberUtils.formatCardNumber(model.getPayeeAccountNumber()));
        payeePhone.updateValue(NumberUtils.formatMobileNumber(model.getPayeeMobile()));
        payName.updateValue(model.getPayerName());
        payCustomerCode.updateValue(model.getPayerCustomerId());
        // 附言不为空才显示
        if (!StringUtils.isEmpty(model.getFurInfo())) {
            tips.setVisibility(View.VISIBLE);
            tips.updateValue(model.getFurInfo());
        }
        orderDate.updateValue(model.getCreateDate());
        payerAccount.updateValue(payAccount);
        payerMobile.updateValue(NumberUtils.formatMobileNumber(model.getPayerMobile()));


        if (model.getCreateChannel().equals("1")) {
            startChannel.updateValue("网上银行");
        } else {
            startChannel.updateValue("手机银行");
        }
        orderNumber.updateValue(model.getNotifyId() + "");
    }

    private String getAccountId(String accountNum) {
        String accountId = "";
        List<String> accountType = new ArrayList<>();
        accountType.add("103");
        accountType.add("104");
        accountType.add("119");

        List<AccountBean> list = ApplicationContext.getInstance().getChinaBankAccountList(accountType);
        for (AccountBean bean : list) {
            if (bean.getAccountNumber().equals(accountNum)) {
                accountId = bean.getAccountId();
                break;
            }
        }
        return accountId;
    }

    @Override
    public ReminderDetailModel getModel() {
        return model;
    }

    @Override
    public void reminderOrderDetailReturned() {
        ll_content.setVisibility(View.VISIBLE);
        updateViews();
    }

    @Override
    public void psnTransActReminderSmsReturned() {
        if ("B".equals(model.getSmsStatus())) {
            Toast.makeText(getActivity(), "发送失败", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "发送成功", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void psnTransActUndoReminderOrderReturned() {
        findFragment(OrderListFragment.class).refreshResultData("已撤销");
        Toast.makeText(getActivity(), "撤销成功！", Toast.LENGTH_SHORT).show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pop();
                findFragment(OrderListFragment.class).cleanReQryReminder();
            }
        }, 1000);

    }

    @Override
    public void setPresenter(ReminderDetailContact.Presenter presenter) {

    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    protected ReminderDetailPresenter initPresenter() {
        return new ReminderDetailPresenter(this);
    }
}
