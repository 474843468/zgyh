package com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.ui;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PhoneNumberFormat;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ErrorDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditClearWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.model.QrcodeTransModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.model.ScanResultAccountModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.presenter.QrcodeTransPresenter;
import com.boc.bocsoft.mobile.bocmobile.yun.BocCloudCenter;
import com.boc.bocsoft.mobile.bocmobile.yun.other.AccountType;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.utils.regex.RegexResult;
import com.boc.bocsoft.mobile.framework.utils.regex.RegexUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 二维码转账界面
 * Created by wangtong on 2016/7/28.
 */
public class QrcodeTransFragment extends MvpBussFragment<QrcodeTransPresenter> implements QrcodeTransContact.View {

    private static final int PICK_CONTACT = 1;
    public static final int QRCODE_SCAN_REQUEST_CODE = 2;
    protected EditChoiceWidget feeAccount;
    protected TextView currency;
    protected TextView remainAmount;
    protected EditMoneyInputWidget payAmount;
    protected TextView payeeAccount;
    protected EditClearWidget payeeName;
    protected EditClearWidget payeeMobile;
    protected EditClearWidget tips;
    protected TextView btnConfirm;
    protected ImageView btnBack;
    protected TextView fragmentTitle;
    private View rootView;
    private QrcodeTransModel model;
    private ImageView qrcodeImage;
    private ImageView phoneImage;
    private LinearLayout zone;
    private LinearLayout accountRemain;
    private TextView tvRemain;
    private String mark = "";
    private String contactNum;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_qrcode_trans, null);
        return rootView;
    }

    @Override
    public void initView() {
        remainAmount = (TextView) rootView.findViewById(R.id.remain_amount);
        feeAccount = (EditChoiceWidget) rootView.findViewById(R.id.fee_account);
        currency = (TextView) rootView.findViewById(R.id.currency);
        payAmount = (EditMoneyInputWidget) rootView.findViewById(R.id.pay_amount);
        payeeAccount = (TextView) rootView.findViewById(R.id.payee_account);
        payeeName = (EditClearWidget) rootView.findViewById(R.id.payee_name);
        payeeMobile = (EditClearWidget) rootView.findViewById(R.id.payee_mobile);
        tips = (EditClearWidget) rootView.findViewById(R.id.tips);
        btnConfirm = (TextView) rootView.findViewById(R.id.btn_confirm);
        qrcodeImage = (ImageView) rootView.findViewById(R.id.qrcode_icon);
        phoneImage = payeeMobile.getClearEditRightImageView();
        btnBack = (ImageView) rootView.findViewById(R.id.btn_back);
        fragmentTitle = (TextView) rootView.findViewById(R.id.fragment_title);
        zone = (LinearLayout) rootView.findViewById(R.id.zone);
        tvRemain = (TextView) rootView.findViewById(R.id.tvRemain);
        accountRemain = (LinearLayout) rootView.findViewById(R.id.accountRemain);

        accountRemain.setVisibility(View.GONE);
    }

    private void setDefaultFocus() {
        feeAccount.getChoiceNameTextView().setFocusable(true);
        feeAccount.getChoiceNameTextView().setFocusableInTouchMode(true);
        feeAccount.getChoiceNameTextView().requestFocus();
    }

    @Override
    public void reInit() {
        initView();
        initData();
        setListener();
        payAmount.setContentHint("免手续费");
        payAmount.setmContentMoneyEditText("");
        payeeAccount.setText("");
        payeeName.setEditWidgetContent("");
        payeeMobile.setEditWidgetContent("");
        tips.setEditWidgetHint("选填");
        tips.setEditWidgetContent("");
    }

    private List<String> accountType;

    @Override
    public void initData() {
        fragmentTitle.setText("二维码转账");
        model = new QrcodeTransModel();
        // 防止转账成功重新进入这个类model被重新new，getPresenter()不为空，presenter存放的是第一次initPresenter中的model
        getPresenter().setUiModel(model);
        setDefaultFocus();

        ScanResultAccountModel scanModel = getArguments().getParcelable(QrcodeScanFragment.RESULT_KEY);
        payeeAccount.setText(NumberUtils.formatCardNumber(scanModel.getCustActNum()));
        payeeName.setEditWidgetContent(scanModel.getCustName());
        model.setPayeeAccount(scanModel.getCustActNum());

        payAmount.setContentHint("免手续费");
        payeeName.setEditWidgetHint("");
        payAmount.getContentMoneyEditText().setTextColor(getResources().getColor(R.color.boc_text_money_color_red));
        payAmount.setMaxLeftNumber(11);
        payAmount.setMaxRightNumber(2);
        payAmount.setClearIconVisible(false);
        phoneImage.setImageResource(R.drawable.boc_callbook);
        payeeMobile.setEditWidgetHint("短信通知收款人，选填");
        payeeMobile.getContentEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        payeeMobile.getContentEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        payeeMobile.showEditWidgetRightImage(true);
        tips.setEditWidgetHint("选填");

        accountType = new ArrayList<>();
        accountType.add(ApplicationConst.ACC_TYPE_ORD);
        accountType.add(ApplicationConst.ACC_TYPE_ZHONGYIN);
        accountType.add(ApplicationConst.ACC_TYPE_GRE);
        accountType.add(ApplicationConst.ACC_TYPE_BRO);
        accountType.add(ApplicationConst.ACC_TYPE_RAN);

        List<AccountBean> list = ApplicationContext.getInstance().getChinaBankAccountList(accountType);

        //此处从存储的位置获取上次转款存储的账户id，如果为空，转账账户显示集合第一个，否则显示账户id对应的账户
        String accountId = BocCloudCenter.getInstance().getAccountId(AccountType.ACC_TYPE_QRCODETRANS);

        if (list != null && list.size() > 0) {
            if (!StringUtils.isEmpty(accountId)) {
                for (AccountBean bean : list) {
                    String sha256String = BocCloudCenter.getSha256String(bean.getAccountId());
                    if (accountId.equals(sha256String)) {
                        setSelectAccData(bean);
                        showLoadingDialog();
                        if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(bean.getAccountType())) {
                            getPresenter().queryCreditAccountDetail(bean.getAccountId());
                            mark = "1";
                        } else if (ApplicationConst.ACC_TYPE_GRE.equals(bean.getAccountType())) {
                            getPresenter().queryCreditAccountDetail(bean.getAccountId());
                            mark = "1";
                        } else {
                            getPresenter().psnAccountQueryAccountDetail(bean.getAccountId());
                            mark = "2";
                        }
                    }
                }
            } else {
                setSelectAccData(list.get(0));
                showLoadingDialog();
                if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(list.get(0).getAccountType())) {
                    getPresenter().queryCreditAccountDetail(list.get(0).getAccountId());
                    mark = "1";
                } else if (ApplicationConst.ACC_TYPE_GRE.equals(list.get(0).getAccountType())) {
                    getPresenter().queryCreditAccountDetail(list.get(0).getAccountId());
                    mark = "1";
                } else {
                    getPresenter().psnAccountQueryAccountDetail(list.get(0).getAccountId());
                    mark = "2";
                }
            }

        } else {
            ErrorDialog errorDialog = new ErrorDialog(mContext);
            errorDialog.setBtnText("确认");
            errorDialog.setErrorData("无可操作账户");
            errorDialog.setOnBottomViewClickListener(new ErrorDialog.OnBottomViewClickListener() {
                @Override
                public void onBottomViewClick() {
                    titleLeftIconClick();
                }
            });
            errorDialog.show();
        }
    }

    // 选择账户fragment
    private SelectAccoutFragment selectAccoutFragment;
    // 选中item的bundle
    private Bundle selectAccDate;

    @Override
    public void setListener() {
        payeeMobile.getContentEditText().addTextChangedListener(new textAgent());

        feeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectAccoutFragment = new SelectAccoutFragment();
                selectAccoutFragment.isRequestNet(true);
                selectAccoutFragment.setOnItemListener(new SelectAccoutFragment.ItemListener() {
                    @Override
                    public void onItemClick(Bundle bundle) {
                        selectAccDate = bundle;
                        AccountBean accountBean = bundle.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
                        setSelectAccData(accountBean);
                        showLoadingDialog();
                        if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(accountBean.getAccountType())) {
                            getPresenter().queryCreditAccountDetail(accountBean.getAccountId());
                        } else if (ApplicationConst.ACC_TYPE_GRE.equals(accountBean.getAccountType())) {
                            getPresenter().queryCreditAccountDetail(accountBean.getAccountId());
                        } else {
                            getPresenter().psnAccountQueryAccountDetail(accountBean.getAccountId());
                        }
                    }
                });
                Bundle bundle = new Bundle();

                bundle.putStringArrayList(SelectAccoutFragment.ACCOUNT_TYPE_LIST, (ArrayList<String>) accountType);

                selectAccoutFragment.setArguments(bundle);

                startForResult(selectAccoutFragment, SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConditions()) {
                    showLoadingDialog();
                    if (ApplicationConfig.isBigAmountNotice) {
                        getPresenter().psnTransQuotaquery();
                    } else {
                        getPresenter().psnGetSecurityFactor();
                    }
                }
            }
        });

        qrcodeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QrcodeScanFragment fragment = new QrcodeScanFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("isStartForResult", true);
                fragment.setArguments(bundle);
                startForResult(fragment, QRCODE_SCAN_REQUEST_CODE);
            }
        });

        phoneImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setData(ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleLeftIconClick();
            }
        });
    }

    private boolean checkConditions() {
//        String flag = model.getLoanBalanceLimitFlag();
        if (currency.getText().toString().contains("欠款")) {
            showErrorDialog("转账金额大于实时余额，请修改");
            return false;
        }
        if (TextUtils.isEmpty(payAmount.getContentMoney())) {
            showErrorDialog("转账金额不能为空！");
            return false;
        }

        double inputNum = Double.parseDouble(payAmount.getContentMoney().toString().trim().replaceAll(" ", ""));
        double remainNum = Double.parseDouble(model.getRemainAmount().toString().trim().replaceAll(" ", ""));
        model.setTrfAmount(inputNum + "");

        if (TextUtils.isEmpty(model.getRemainAmount().toString())) {
            showErrorDialog("付款账户余额查询失败，无法完成交易！");
            return false;
        }
        if (inputNum > remainNum) {
            if ("1".equals(mark)) {
                showErrorDialog("转账金额大于实时余额，请修改");
            } else {
                showErrorDialog("转账金额大于可用余额，请修改");
            }
            return false;
        }
        if (TextUtils.isEmpty(model.getPayerAccount())) {
            showErrorDialog("付款账号不能为空！");
            return false;
        }
        if (TextUtils.isEmpty(payeeAccount.getText().toString())) {
            showErrorDialog("收款账号不能为空！");
            return false;
        }

        String payeeNum = payeeMobile.getEditWidgetContent().trim().replaceAll(" ", "");
        model.setPayeeMobile(payeeNum.replaceAll("-", ""));

        String PayeeMobile = payeeMobile.getEditWidgetContent().toString().trim().replaceAll(" ", "");
        RegexResult payeeMobileResult = RegexUtils.check(mContext, "phone_trans_no_mobile", PayeeMobile, false);
        if (payeeMobileResult.isAvailable) {
            model.setPayeeMobile(PayeeMobile.replaceAll("-", ""));
        } else {
            showErrorDialog(payeeMobileResult.errorTips);
            return false;
        }

        RegexResult PayeeNameResult = RegexUtils.check(mContext, "phone_trans_payee_name", payeeName.getEditWidgetContent(), true);
        if (!PayeeNameResult.isAvailable) {
            showErrorDialog(PayeeNameResult.errorTips);
            return false;
        } else {
            model.setPayeeName(payeeName.getEditWidgetContent());
        }

        RegexResult TipsResult = RegexUtils.check(mContext, "mbdi_tr_trans_remark", tips.getEditWidgetContent(), false);
        if (!TipsResult.isAvailable) {
            showErrorDialog(TipsResult.errorTips);
            return false;
        } else {
            model.setTips(tips.getEditWidgetContent());
        }
        return true;
    }

    private void updateView(AccountBean accountBean) {
        setSelectAccData(accountBean);
        model.setLoanBalanceLimitFlag(null);
    }

    /**
     * 付款人选中时设置数据
     *
     * @param accountBean
     */
    private void setSelectAccData(AccountBean accountBean) {
        model.setPayerAccount(accountBean.getAccountNumber());
//        model.setPayerName(accountBean.getAccountName());
        model.setAccountId(accountBean.getAccountId());
        model.setRemainCurrency(accountBean.getCurrencyCode());
        model.setTrfCurrency(accountBean.getCurrencyCode());
        model.setPayerAccountType(accountBean.getAccountType());
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT == resultCode) {
            AccountBean accountBean = data.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
            updateView(accountBean);
        } else if (QRCODE_SCAN_REQUEST_CODE == resultCode) {
            ScanResultAccountModel scanModel = data.getParcelable(QrcodeScanFragment.RESULT_KEY);
            payeeAccount.setText(NumberUtils.formatCardNumber(scanModel.getCustActNum()));
            payeeName.setEditWidgetContent(scanModel.getCustName());
            model.setPayeeAccount(scanModel.getCustActNum());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            final List<String> phoneList = PhoneNumberFormat.insertPhonenumber1(mActivity, data);

            final ArrayList<String> newList = new ArrayList();

            for (String contact : phoneList) {
                String num = contact.replaceAll("-", "").replaceAll(" ", "");
                newList.add(PhoneNumberFormat.getPhoneNum(num));
            }

            contactNum = "";

            if (phoneList != null) {
                if (phoneList.size() == 1) {
                    contactNum = phoneList.get(0);
                    if (contactNum.contains("#")) {
                        int indexOf = contactNum.indexOf("#");
                        String s = contactNum.substring(indexOf + 1);
                        String num = s.replaceAll("-", "").replaceAll(" ", "");
                        payeeMobile.setEditWidgetContent(NumberUtils.formatMobileNumber(num));
                    } else {
                        String num = contactNum.replaceAll("-", "").replaceAll(" ", "");
                        payeeMobile.setEditWidgetContent(NumberUtils.formatMobileNumber(num));
                    }

                } else if (phoneList.size() > 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    int size = phoneList.size();
                    builder.setTitle("请选择一个号码").setItems(newList.toArray(new String[size]), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int position = which;
                            contactNum = phoneList.get(position);
                            if (contactNum.contains("#")) {
                                int indexOf = contactNum.indexOf("#");
                                String s = contactNum.substring(indexOf + 1);
                                String num = s.replaceAll("-", "").replaceAll(" ", "");
                                payeeMobile.setEditWidgetContent(NumberUtils.formatMobileNumber(num));
                            } else {
                                String num = contactNum.replaceAll("-", "").replaceAll(" ", "");
                                payeeMobile.setEditWidgetContent(NumberUtils.formatMobileNumber(num));
                            }
                        }
                    }).create().show();
                }
            }
        }
    }

    @Override
    public QrcodeTransModel getModel() {
        return model;
    }

    @Override
    protected QrcodeTransPresenter initPresenter() {
        return new QrcodeTransPresenter(this);
    }

    class textAgent implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            PhoneNumberFormat.onEditTextChanged(s, start, before, payeeMobile.getContentEditText());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    }

    @Override
    public void queryCreditAccountDetailReturned() {
        if (model.isCheckStates()) {
            onBalanceUpateView();
            tvRemain.setText("实时余额:");
            String flag = model.getLoanBalanceLimitFlag();
            if ("1".equals(flag)) {
                remainAmount.setText(MoneyUtils.transMoneyFormat(model.getRemainAmount(), model.getRemainCurrency()));
                currency.setText("存款 " + PublicCodeUtils.getCurrency(getActivity(), model.getRemainCurrency()));
            } else if ("2".equals(flag)) {
                remainAmount.setText("0");
                currency.setText("存款 " + PublicCodeUtils.getCurrency(getActivity(), model.getRemainCurrency()));
            } else {
                remainAmount.setText(MoneyUtils.transMoneyFormat(model.getRemainAmount(), model.getRemainCurrency()));
                currency.setText("欠款 " + PublicCodeUtils.getCurrency(getActivity(), model.getRemainCurrency()));
            }
            if (selectAccoutFragment != null && selectAccoutFragment.isVisible()) {
                selectAccoutFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, selectAccDate);
                selectAccoutFragment.pop();
            }
        } else {
            if ("1".equals(model.getWarnType())) {
                showErrorDialog(model.getWarning());
            }
        }
    }

    @Override
    public void queryQuotaForTransFailed(BiiResultErrorException exception) {
        getPresenter().psnGetSecurityFactor();
    }

    @Override
    public void queryQuotaForTransSuccess(PsnTransQuotaQueryResult result) {
        BigDecimal quotaAmount = new BigDecimal(result.getQuotaAmount());
        BigDecimal transAmount = new BigDecimal(payAmount.getContentMoney());
        BigDecimal maxAmount = new BigDecimal(300000);
        // 判断金额+返回的限额是否超过30w
        if (quotaAmount.add(transAmount).compareTo(maxAmount) == 1) {
            closeProgressDialog();
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
                    showLoadingDialog();
                    getPresenter().psnGetSecurityFactor();
                }
            });
            dialog.show();
        } else {
            getPresenter().psnGetSecurityFactor();
        }
    }

    @Override
    public void psnAccountQueryAccountDetailReturned() {
        if (model.isCheckStates()) {
            onBalanceUpateView();
            tvRemain.setText("可用余额:");
            remainAmount.setText(MoneyUtils.transMoneyFormat(model.getRemainAmount().toString(), model.getRemainCurrency()));
            currency.setText(PublicCodeUtils.getCurrency(getActivity(), model.getRemainCurrency()));
            if (selectAccoutFragment != null && selectAccoutFragment.isVisible()) {
                selectAccoutFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, selectAccDate);
                selectAccoutFragment.pop();
            }
        } else {
            if ("1".equals(model.getWarnType())) {
                showErrorDialog(model.getWarning());
            }
//            onBalanceUpateView(false);
        }
    }

    /**
     * 查询余额后更新界面显示
     */
    private void onBalanceUpateView() {
//        if (isSucc) {
        feeAccount.setChoiceTextContent(NumberUtils.formatCardNumberStrong(model.getPayerAccount()));
        zone.setVisibility(View.GONE);
        accountRemain.setVisibility(View.VISIBLE);
//        } else {
//            zone.setVisibility(View.VISIBLE);
//            accountRemain.setVisibility(View.GONE);
//            feeAccount.setChoiceTextContent("");
//            feeAccount.setChoiceTextContentHint("请选择");
//        }
    }

    @Override
    public void securityFactorReturned() {
        QrcodeConfirmFragment fragment = new QrcodeConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("model", model);
        fragment.setArguments(bundle);
        start(fragment);
    }

    @Override
    public void psnTransBocTransferVerifyReturned() {

    }


    @Override
    public void setPresenter(QrcodeTransContact.Presenter presenter) {
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Nullable
    @Override
    public View getView() {
        return rootView;
    }


    @Override
    public void onStop() {
        super.onStop();
        hideSoftInput();
    }
}
