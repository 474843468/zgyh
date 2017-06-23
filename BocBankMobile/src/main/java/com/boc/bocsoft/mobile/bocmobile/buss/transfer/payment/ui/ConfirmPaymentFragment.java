package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQuotaQuery.PsnTransQuotaQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConfig;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.model.ConfirmPaymentModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.model.PaymentDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.presenter.ConfirmPaymantPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 确认付款页面
 * Created by wangtong on 2016/6/29.
 */
public class ConfirmPaymentFragment extends MvpBussFragment<ConfirmPaymentContact.Presenter>
        implements ConfirmPaymentContact.View {

    protected TextView head;
    protected EditChoiceWidget payerAccount;
    protected TextView accountAmount;
    protected EditMoneyInputWidget payAmount;
    protected TextView btnConfirm;
    protected ImageView btnBack;
    protected TextView currency;
    private View rootView;
    //数据模型
    private ConfirmPaymentModel model;
    private PaymentDetailModel detail;
    private LinearLayout availableGroup;
    private LinearLayout zone;
    //    private ConfirmPaymantPresenter presenter;
    private TextView tvRemain;
    private String mark = "";

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_confirm_payment, null);
        return rootView;
    }

    @Override
    public void initView() {
        head = (TextView) rootView.findViewById(R.id.fragment_title);
        payerAccount = (EditChoiceWidget) rootView.findViewById(R.id.payer_account);
        accountAmount = (TextView) rootView.findViewById(R.id.account_amount);
        payAmount = (EditMoneyInputWidget) rootView.findViewById(R.id.pay_amount);
        btnConfirm = (TextView) rootView.findViewById(R.id.btn_confirm);
        availableGroup = (LinearLayout) rootView.findViewById(R.id.availableGroup);
        btnBack = (ImageView) rootView.findViewById(R.id.btn_back);
        currency = (TextView) rootView.findViewById(R.id.currency);
        zone = (LinearLayout) rootView.findViewById(R.id.zone);
        tvRemain = (TextView) rootView.findViewById(R.id.tvRemain);
        currency.setTextColor(getResources().getColor(R.color.boc_text_color_money_count));
    }

    @Override
    public void initData() {
        head.setText("确认付款");
        model = new ConfirmPaymentModel();
        payerAccount.setChoiceTextContentHint("请选择");
        detail = getArguments().getParcelable("model");
        model.setDetail(detail);
        payAmount.getContentMoneyEditText().setTextColor(mContext.getResources().getColor(R.color.boc_text_color_red));
        payAmount.setMaxLeftNumber(11);
        payAmount.setMaxRightNumber(2);
        payAmount.setmContentMoneyEditText(MoneyUtils.transMoneyFormat(model.getDetail().getRequestAmount(), model.getDetail().getTrfCur()));

        String payerAccountNumber = detail.getPayerAccountNumber();
        if (!TextUtils.isEmpty(payerAccountNumber)) {
            model.setAccountId(getAccountId(payerAccountNumber));
            payerAccount.setChoiceTextContent(NumberUtils.formatCardNumber(payerAccountNumber));

            if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(detail.getPayerAccountType())) {
                mark = "1";
                getPresenter().queryCreditAccountDetail(model.getAccountId());
            } else if (ApplicationConst.ACC_TYPE_GRE.equals(detail.getPayerAccountType())) {
                mark = "1";
                getPresenter().queryCreditAccountDetail(model.getAccountId());
            } else {
                mark = "2";
                getPresenter().psnAccountQueryAccountDetail();
            }
        } else {
            payerAccount.setChoiceTextContent(NumberUtils.formatCardNumber(detail.getPayerAccountNumber()));
            accountAmount.setText("0.00");
        }
        currency.setText(PublicCodeUtils.getCurrency(getActivity(), model.getDetail().getTrfCur()));
    }

    private SelectAccoutFragment selectAccFragment;
    private List<String> accountType;//账户过滤条件
    // 选中item的bundle
    private Bundle selectAccDate;

    @Override
    public void setListener() {
        super.setListener();
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConditions()) {
                    if (ApplicationConfig.isBigAmountNotice) {
                        showLoadingDialog();
                        getPresenter().psnTransQuotaquery();
                    } else {
                        jumpPaymentPre();
                    }
                }
            }


        });

        payerAccount.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accountType = new ArrayList<>();
                        accountType.add(ApplicationConst.ACC_TYPE_ZHONGYIN);
                        accountType.add(ApplicationConst.ACC_TYPE_GRE);
                        accountType.add(ApplicationConst.ACC_TYPE_BRO);

                        selectAccFragment = new SelectAccoutFragment();
                        selectAccFragment.isRequestNet(true);
                        selectAccFragment.setOnItemListener(new SelectAccoutFragment.ItemListener() {
                            @Override
                            public void onItemClick(Bundle bundle) {
                                selectAccDate = bundle;
                                AccountBean accountBean = bundle.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
                                updateAccount(accountBean);
                                if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(accountBean.getAccountType())) {
                                    getPresenter().queryCreditAccountDetail(accountBean.getAccountId());
                                } else if (ApplicationConst.ACC_TYPE_GRE.equals(accountBean.getAccountType())) {
                                    getPresenter().queryCreditAccountDetail(accountBean.getAccountId());
                                } else {
                                    getPresenter().psnAccountQueryAccountDetail();
                                }
                            }
                        });
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList(SelectAccoutFragment.ACCOUNT_TYPE_LIST,
                                (ArrayList<String>) accountType);
                        selectAccFragment.setArguments(bundle);
                        startForResult(selectAccFragment, SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT);
                    }
                }

        );

        btnBack.setOnClickListener(new View.OnClickListener()

                                   {
                                       @Override
                                       public void onClick(View v) {
                                           pop();
                                       }
                                   }

        );
    }

    /**
     * 跳转到预交易界面
     */
    private void jumpPaymentPre() {
        PaymentPreFragment fragment = new PaymentPreFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("model", model);
        fragment.setArguments(bundle);
        start(fragment);
    }

    private boolean checkConditions() {
        model.getDetail().setTrfAmount(payAmount.getContentMoney());
        if (TextUtils.isEmpty(model.getDetail().getPayerAccountNumber())) {
            showErrorDialog("付款账户不能为空");
            return false;
        }
        if (TextUtils.isEmpty(model.getDetail().getTrfAmount())) {
            showErrorDialog("实付金额不能为空");
            return false;
        }
        String flag = model.getLoanBalanceLimitFlag();
        if (!StringUtils.isEmpty(flag)) {
            if (!"1".equals(flag)) {
                showErrorDialog("转账金额大于实时余额，请修改");
                return false;
            }
        }
        //余额
        String trfAmount = model.getDetail().getTrfAmount();
        String remain = model.getRemainAmount().toString();
        Double contentMoney = Double.parseDouble(trfAmount);
        Double remainAmount = Double.parseDouble(remain);
        if (contentMoney > remainAmount) {
            if ("1".equals(mark)) {
                showErrorDialog("转账金额大于实时余额，请修改");
            } else {
                showErrorDialog("实付金额不能大于可用余额！");
            }
            return false;
        }
        model.getDetail().setTrfAmount(payAmount.getContentMoney());

        if (TextUtils.isEmpty(model.getRemainAmount().toString())) {
            showErrorDialog("付款账户余额查询失败，无法完成交易");
            return false;
        }
        return true;
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT == requestCode && data != null) {
            AccountBean accountBean = data.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
            updateAccount(accountBean);
        }
    }

    private void updateAccount(AccountBean accountBean) {
        payerAccount.setChoiceTextContent(NumberUtils.formatCardNumber(accountBean.getAccountNumber()));
        model.getDetail().setPayerAccountNumber(accountBean.getAccountNumber());
        model.setAccountId(accountBean.getAccountId());
        model.getDetail().setPayeeAccountType(accountBean.getAccountType());
    }

    private String getAccountId(String accountNum) {
        String accountId = "";
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
    public ConfirmPaymentModel getModel() {
        return model;
    }

    @Override
    public void psnAccountQueryAccountDetailReturned() {
        if (model.isQueryStates()) {
            zone.setVisibility(View.GONE);
            availableGroup.setVisibility(View.VISIBLE);
            tvRemain.setText("可用余额:");
            accountAmount.setText(MoneyUtils.transMoneyFormat(model.getRemainAmount(), model.getRemainCurrency()));
            currency.setText(PublicCodeUtils.getCurrency(getActivity(), model.getRemainCurrency()));
            if (selectAccFragment != null && selectAccFragment.isVisible()) {
                selectAccFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, selectAccDate);
                selectAccFragment.pop();
            }
        } else {
            if ("1".equals(model.getWarnType())) {
                showErrorDialog(model.getWarning());
            }
        }
    }

    @Override
    public void queryCreditAccountDetailReturned() {
        if (model.isQueryStates()) {
            zone.setVisibility(View.GONE);
            availableGroup.setVisibility(View.VISIBLE);
            tvRemain.setText("实时余额:");
            currency.setText(PublicCodeUtils.getCurrency(getActivity(), model.getRemainCurrency()));
            accountAmount.setText(MoneyUtils.transMoneyFormat(model.getRemainAmount(), model.getRemainCurrency()));
            String flag = model.getLoanBalanceLimitFlag();
            if ("1".equals(flag)) {
                currency.setText("存款 " + PublicCodeUtils.getCurrency(getActivity(), model.getRemainCurrency()));
                accountAmount.setText(MoneyUtils.transMoneyFormat(model.getRemainAmount(), model.getRemainCurrency()));
            } else if ("2".equals(flag)) {
                currency.setText("存款 " + PublicCodeUtils.getCurrency(getActivity(), model.getRemainCurrency()));
                accountAmount.setText("0");
            } else {
                currency.setText("欠款 " + PublicCodeUtils.getCurrency(getActivity(), model.getRemainCurrency()));
                accountAmount.setText(MoneyUtils.transMoneyFormat(model.getRemainAmount(), model.getRemainCurrency()));
            }
            if (selectAccFragment != null && selectAccFragment.isVisible()) {
                selectAccFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, selectAccDate);
                selectAccFragment.pop();
            }
        } else {
            if ("1".equals(model.getWarnType())) {
                showErrorDialog(model.getWarning());
            }
        }
    }

    @Override
    public void queryQuotaForTransFailed(BiiResultErrorException exception) {
        closeProgressDialog();
        jumpPaymentPre();
    }

    @Override
    public void queryQuotaForTransSuccess(PsnTransQuotaQueryResult result) {
        closeProgressDialog();
        BigDecimal quotaAmount = new BigDecimal(result.getQuotaAmount());
        BigDecimal transAmount = new BigDecimal(payAmount.getContentMoney());
        BigDecimal maxAmount = new BigDecimal(300000);
        // 判断金额+返回的限额是否超过30w
        if (quotaAmount.add(transAmount).compareTo(maxAmount) == 1) {
            final TitleAndBtnDialog dialog = new TitleAndBtnDialog(getContext());
            String[] btnNames = new String[]{"取消", "确认"};
            dialog.setRightBtnTextBgColor(getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_text_color_red));
            dialog.setBtnName(btnNames);
            dialog.setNoticeContent("您网银和手机银行累计转账金额已超过30万元，请确认是否继续转账");
            dialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
                @Override
                public void onLeftBtnClick(View view) {
                    dialog.dismiss();
                }

                @Override
                public void onRightBtnClick(View view) {
                    dialog.dismiss();
                    jumpPaymentPre();
                }
            });
            dialog.show();
        } else {
            jumpPaymentPre();
        }
    }

    @Override
    public void setPresenter(ConfirmPaymentContact.Presenter presenter) {

    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    protected ConfirmPaymentContact.Presenter initPresenter() {
        return new ConfirmPaymantPresenter(this);
    }
}
