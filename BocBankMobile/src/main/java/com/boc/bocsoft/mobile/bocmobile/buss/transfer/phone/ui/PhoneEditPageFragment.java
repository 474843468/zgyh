package com.boc.bocsoft.mobile.bocmobile.buss.transfer.phone.ui;

import android.app.AlertDialog;
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
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.phone.model.PhoneEditModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.phone.presenter.EditPagePresenter;
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
 * 手机号转账
 * Created by wangtong on 2016/7/26.
 */
public class PhoneEditPageFragment extends MvpBussFragment<EditPagePresenter> implements PhoneEditPageContact.View {

    private static final int REQUEST_CODE_SELECT_MOBILE_CONTACT = 3434;
    protected EditChoiceWidget feeAccount;
    protected TextView currency;
    protected TextView remainAmount;
    protected EditMoneyInputWidget payAmount;
    protected EditClearWidget payeeMobile;
    protected EditClearWidget payeeName;
    protected EditClearWidget tips;
    protected TextView btnConfirm;
    //    protected ImageView btnBack;
//    protected TextView fragmentTitle;
    protected LinearLayout availableGroup;
    private ImageView mobileImage;
    private View rootView;
    private PhoneEditModel model;
    //    private EditPagePresenter presenter;
    private static final int PICK_CONTACT = 1;
    private LinearLayout zone;
    private String contactNum;
    private TextView tvRemain;//金额标题
    private String mark = "";

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_phone_trans_edit, null);
        return rootView;
    }

    @Override
    public void initView() {
        feeAccount = (EditChoiceWidget) rootView.findViewById(R.id.fee_account);
        currency = (TextView) rootView.findViewById(R.id.currency);
        remainAmount = (TextView) rootView.findViewById(R.id.remain_amount);
        payAmount = (EditMoneyInputWidget) rootView.findViewById(R.id.pay_amount);
        payeeMobile = (EditClearWidget) rootView.findViewById(R.id.payee_mobile);
        payeeName = (EditClearWidget) rootView.findViewById(R.id.payee_name);
        tips = (EditClearWidget) rootView.findViewById(R.id.tips);
        btnConfirm = (TextView) rootView.findViewById(R.id.btn_confirm);
//        btnBack = (ImageView) rootView.findViewById(R.id.btn_back);
//        fragmentTitle = (TextView) rootView.findViewById(R.id.fragment_title);
        mobileImage = (ImageView) rootView.findViewById(R.id.call_book);
        availableGroup = (LinearLayout) rootView.findViewById(R.id.available_group);
        tvRemain = (TextView) rootView.findViewById(R.id.tvRemain);
        zone = (LinearLayout) rootView.findViewById(R.id.zone);

        mTitleBarView.setRightImgBtnVisible(false);
        availableGroup.setVisibility(View.GONE);
    }

    private void setDefaultFocus() {
        feeAccount.getChoiceNameTextView().setFocusable(true);
        feeAccount.getChoiceNameTextView().setFocusableInTouchMode(true);
        feeAccount.getChoiceNameTextView().requestFocus();
    }

    @Override
    public void reInit() {
        initView();
        payAmount.setContentHint("免手续费");
        payAmount.setmContentMoneyEditText("");
        payeeMobile.setEditWidgetContent("");
        payeeName.setEditWidgetContent("");
        tips.setEditWidgetContent("");
        tips.setEditWidgetHint("选填，最多10个汉字");
        initData();
        setListener();

    }

    private List<String> accountType;

    @Override
    public void initData() {
        model = new PhoneEditModel();
        // 防止转账成功重新进入这个类model被重新new，getPresenter()不为空，presenter存放的是第一次initPresenter中的model
        getPresenter().setUiModel(model);
        setDefaultFocus();
        payAmount.setContentHint("免手续费");
        payAmount.getContentMoneyEditText().setTextColor(getResources().getColor(R.color.boc_text_money_color_red));
        tips.setEditWidgetHint("选填，最多10个汉字");
        payAmount.setMaxLeftNumber(11);
        payAmount.setMaxRightNumber(2);
        mobileImage.setImageResource(R.drawable.boc_callbook);
        payAmount.setClearIconVisible(false);
        payeeMobile.getContentEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        payeeMobile.getContentEditText().setInputType(InputType.TYPE_CLASS_NUMBER);

        accountType = new ArrayList<>();
        accountType.add(ApplicationConst.ACC_TYPE_ZHONGYIN);
        accountType.add(ApplicationConst.ACC_TYPE_GRE);
        accountType.add(ApplicationConst.ACC_TYPE_BRO);

        List<AccountBean> list = ApplicationContext.getInstance().getChinaBankAccountList(accountType);
        //此处从存储的位置获取上次转款存储的账户id，如果该id为空，转账账户显示集合第一个，否则显示账户id对应的账户
        String accountId = BocCloudCenter.getInstance().getAccountId(AccountType.ACC_TYPE_PHONETRANS);
        if (list != null && list.size() > 0) {
            if (!StringUtils.isEmpty(accountId)) {
                for (AccountBean bean : list) {
                    String sha256String = BocCloudCenter.getSha256String(bean.getAccountId());
                    if (accountId.equals(sha256String)) {
                        setSelectAccData(bean);
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

    // 选中item的bundle
    private Bundle selectAccDate;
    private SelectAccoutFragment selectAccFragment;

    @Override
    public void setListener() {
        payeeMobile.getContentEditText().addTextChangedListener(new textAgent());

        feeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAccFragment = new SelectAccoutFragment();
                selectAccFragment.isRequestNet(true);
                selectAccFragment.setOnItemListener(new SelectAccoutFragment.ItemListener() {
                    @Override
                    public void onItemClick(Bundle bundle) {
                        selectAccDate = bundle;
                        AccountBean accountBean = bundle.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
                        setSelectAccData(accountBean);
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

                selectAccFragment.setArguments(bundle);

                startForResult(selectAccFragment, SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT);

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

        mobileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setData(ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                titleLeftIconClick();
//            }
//        });
    }

    private boolean checkConditions() {
//        String flag = model.getLoanBalanceLimitFlag();
        if (currency.getText().toString().contains("欠款")) {
            showErrorDialog("转账金额大于实时余额，请修改");
            return false;
        }

        //输入金额
        String input = payAmount.getContentMoney();
        if (StringUtils.isEmpty(input)) {
            showErrorDialog("转账金额不能为空");
            return false;
        }

        //余额
        Double contentMoney = Double.parseDouble(input);
        String reamin = model.getRemainAmount().toString();
        Double remainAmount = Double.parseDouble(reamin);
        if (contentMoney > remainAmount) {
            if ("1".equals(mark)) {
                showErrorDialog("实付金额不能大于实时余额！");
            } else {
                showErrorDialog("实付金额不能大于可用余额！");
            }

            return false;
        }
        if (TextUtils.isEmpty(model.getPayerAccount())) {
            showErrorDialog("付款账号不能为空！");
            return false;
        }
        if (TextUtils.isEmpty(reamin)) {
            showErrorDialog("付款账户余额查询失败，无法完成交易！");
            return false;
        }
        if (TextUtils.isEmpty(payAmount.getContentMoney())) {
            showErrorDialog("转账金额不能为空");
            return false;
        }
        model.setTrfAmount(input);
        String PayeeMobile = payeeMobile.getEditWidgetContent().toString().trim().replaceAll(" ", "");
        RegexResult payeeMobileResult = RegexUtils.check(mContext, "phone_trans_no_mobile", PayeeMobile, true);
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
        }
        model.setTips(tips.getEditWidgetContent());
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
        model.setPayerName(accountBean.getAccountName());
        model.setAccountId(accountBean.getAccountId());
        model.setRemainCurrency(accountBean.getCurrencyCode());
        model.setAccountType(accountBean.getAccountType());
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT == resultCode) {
            AccountBean accountBean = data.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
            updateView(accountBean);
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

                    // 判断是否需要带回name
                    payeeName.setEditWidgetContent(PhoneNumberFormat.getPhoneName(contactNum));
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
                            payeeName.setEditWidgetContent(PhoneNumberFormat.getPhoneName(contactNum));
                        }
                    }).create().show();
                }
            }
        }
    }

    @Override
    public PhoneEditModel getModel() {
        return model;
    }

    @Override
    public void psnGetSecurityFactorReturned() {
        PhoneConfirmFragment fragment = new PhoneConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("model", model);
        fragment.setArguments(bundle);
        start(fragment);
    }


    @Override
    protected EditPagePresenter initPresenter() {
        return new EditPagePresenter(this);

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

    //普通账户查询回调
    @Override
    public void psnAccountQueryAccountDetailReturned() {
        mark = "2";
        if (model.isQueryStates()) {
            onBalanceUpateView("可用余额");
            remainAmount.setText(MoneyUtils.transMoneyFormat(model.getRemainAmount(), model.getRemainCurrency()));
            currency.setText(PublicCodeUtils.getCurrency(getActivity(), model.getRemainCurrency()));
            if (selectAccFragment != null && selectAccFragment.isVisible()) {
                selectAccFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, selectAccDate);
                selectAccFragment.pop();
            }
        } else {
            if ("1".equals(model.getWarnType())) {
                showErrorDialog(model.getWarning());
            }
//            onBalanceUpateView(false, "");
        }
    }

    //信用卡查询回调
    @Override
    public void queryCreditAccountDetailReturned() {
        mark = "1";
        if (model.isQueryStates()) {
            onBalanceUpateView("实时余额");
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
            if (selectAccFragment != null && selectAccFragment.isVisible()) {
                selectAccFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, selectAccDate);
                selectAccFragment.pop();
            }
        } else {
            if ("1".equals(model.getWarnType())) {
                showErrorDialog(model.getWarning());
            }
//            onBalanceUpateView(false, "");
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


    /**
     * 查询余额后更新界面显示
     *
     * @param balancePerTitle 余额显示标题
     */
    private void onBalanceUpateView(String balancePerTitle) {
//        if (isSucc) {
        feeAccount.setChoiceTextContent(NumberUtils.formatCardNumber(model.getPayerAccount()));
        zone.setVisibility(View.GONE);
        availableGroup.setVisibility(View.VISIBLE);
        tvRemain.setText(balancePerTitle + ":");
//        } else {
//            zone.setVisibility(View.VISIBLE);
//            availableGroup.setVisibility(View.GONE);
//            feeAccount.setChoiceTextContent("");
//            feeAccount.setChoiceTextContentHint("请选择");
//        }
    }

    @Override
    public void setPresenter(PhoneEditPageContact.Presenter presenter) {

    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return "手机号转账";
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
