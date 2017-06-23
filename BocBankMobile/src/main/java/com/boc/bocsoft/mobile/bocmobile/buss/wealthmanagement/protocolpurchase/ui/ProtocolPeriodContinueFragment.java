package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui;

import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.pdf.PDFFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.assignment.SelectAgreementView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditClearWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.webView.ContractFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.model.WealthAccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.riskassess.ui.RiskAssessFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadproductdetailquery.PsnXpadProductDetailQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.productrecommend.model.RecommendModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.productrecommend.ui.RecommendFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.ProtocolModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.PsnXpadSignInitBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.presenter.ProtocolPresenter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialog.MessageDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui.data.BuildViewData;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.widget.SelectedDialogNew;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.WealthConst;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 周期滚续投资
 * 1、选择投资协议页面跳转；2、当只有一种协议申请直接进入；3、理财推荐进入
 * 理财推荐跳转才存在更改账户功能
 * Created by wangtong on 2016/10/25.
 */
public class ProtocolPeriodContinueFragment extends MvpBussFragment<ProtocolPresenter> implements ProtocolContact.ProtocolPeriodContinueView {

    public static final String SIGNINIT = "signInit";
    public static final String CONTINUERESULT = "ContinueResult";
    public static final String CONTINUE_FROM_KEY = "CONTINUE_FROM_KEY";
    public static final int CONTINUE_FROM_VALUE = 201;

    private View rootView;
    private TextView txtHead;
    private LinearLayout llHeadList;
    private EditChoiceWidget choice_currency;//钞汇
    private EditClearWidget invest_period;//认购期数
    private EditChoiceWidget base_amount_type;//基础金额模式
    private EditMoneyInputWidget base_amount;//基础金额
    private EditMoneyInputWidget min_remain_amount;//最低预留金额
    private EditMoneyInputWidget max_invest_amount;//最大扣款金额
    private LinearLayout ll_amount_regular;//定期layout
    private LinearLayout ll_amount_no_regular;//不定期layout
    private Button btnNext;
    private TextView porfolio_account;//账户
    private TextView porfolio_account_change;//更改账户
    private SelectAgreementView sav_read;

    private ProtocolModel mViewModel;
    private PsnXpadSignInitBean initBean;// 页面传值
    private PsnXpadProductDetailQueryResModel detailQueryResModel;// 理财推荐专用
    private RecommendModel.OcrmDetail ocrmDetail;// 理财推荐专用
    private AccountBean selectAccount;// 理财推荐专用
    private WealthAccountBean selectWealthAccount;
    private ArrayList<WealthAccountBean> accountWealthlist;
    private ArrayList<AccountBean> accountBeanlist;
    private String currentDealCode = "";
    private int currentFromValue = 0;// 入口判断

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_protocol_continue, null);
        return rootView;
    }

    @Override
    public void initView() {
        txtHead = (TextView) rootView.findViewById(R.id.txt_head);
        llHeadList = (LinearLayout) rootView.findViewById(R.id.ll_head_list);

        choice_currency = (EditChoiceWidget) rootView.findViewById(R.id.choice_currency);
        invest_period = (EditClearWidget) rootView.findViewById(R.id.invest_period);
        invest_period.getContentEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
        invest_period.getContentEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        base_amount_type = (EditChoiceWidget) rootView.findViewById(R.id.base_amount_type);

        base_amount = (EditMoneyInputWidget) rootView.findViewById(R.id.base_amount);
        min_remain_amount = (EditMoneyInputWidget) rootView.findViewById(R.id.min_remain_amount);
        max_invest_amount = (EditMoneyInputWidget) rootView.findViewById(R.id.max_invest_amount);
        base_amount.getContentMoneyEditText().setTextColor(getResources().getColor(R.color.boc_text_color_red));
        max_invest_amount.getContentMoneyEditText().setTextColor(getResources().getColor(R.color.boc_text_color_red));
        base_amount.setScrollView(rootView);
        base_amount.setMaxLeftNumber(12);
        base_amount.setMaxRightNumber(2);
        min_remain_amount.setScrollView(rootView);
        min_remain_amount.setMaxLeftNumber(12);
        min_remain_amount.setMaxRightNumber(2);
        max_invest_amount.setScrollView(rootView);
        max_invest_amount.setMaxLeftNumber(12);
        max_invest_amount.setMaxRightNumber(2);

        ll_amount_regular = (LinearLayout) rootView.findViewById(R.id.ll_amount_regular);
        ll_amount_no_regular = (LinearLayout) rootView.findViewById(R.id.ll_amount_no_regular);

        sav_read = (SelectAgreementView) rootView.findViewById(R.id.sav_read);

        btnNext = (Button) rootView.findViewById(R.id.btn_next);
        porfolio_account = (TextView) rootView.findViewById(R.id.porfolio_account);
        porfolio_account_change = (TextView) rootView.findViewById(R.id.porfolio_account_change);
    }

    @Override
    public void initData() {
        currentFromValue = getArguments().getInt(CONTINUE_FROM_KEY, -1);
        if (currentFromValue == CONTINUE_FROM_VALUE) {// 理财推荐
            detailQueryResModel = (PsnXpadProductDetailQueryResModel) getArguments().getSerializable(RecommendFragment.DETAILRESMODEL);
            ocrmDetail = (RecommendModel.OcrmDetail) getArguments().getSerializable(RecommendFragment.OCRMDETAIL);
            initBean = getArguments().getParcelable(RecommendFragment.SIGNINITBEAN);
            accountWealthlist = getArguments().getParcelableArrayList(RecommendFragment.ACCOUNTLIST);
            currentDealCode = getArguments().getString(RecommendFragment.DEALCODE, "");

            accountBeanlist = changeWealthBean2AccountBean(accountWealthlist);
            if (accountBeanlist != null && accountBeanlist.size() > 0) {
                selectAccount = accountBeanlist.get(0);
                getSelectWealthAccountBean();
            }
            mViewModel = new ProtocolModel();
            mViewModel.setAccountList(accountWealthlist);
            mViewModel.setSignInitBean(initBean);
        } else {
            mViewModel = getArguments().getParcelable(ProtocolSelectFragment.PROTOCOL_PURCHASE);
            initBean = getArguments().getParcelable(SIGNINIT);
            mViewModel.setSignInitBean(initBean);
        }
        initShowViewData();
    }

    @Override
    public void setListener() {
        choice_currency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedDialogNew dialog = new SelectedDialogNew(mContext);
                final List<String> list = new ArrayList<>();
                list.add("现钞");
                list.add("现汇");
                dialog.showDialog(list);
                dialog.setTitle("选择钞汇");
                dialog.setListener(new SelectedDialogNew.OnItemSelectDialogClicked() {
                    @Override
                    public void onListItemClicked(int index) {
                        choice_currency.setChoiceTextContent(list.get(index));
                    }
                });
            }
        });

        base_amount_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedDialogNew dialog = new SelectedDialogNew(mContext);
                final List<String> list = new ArrayList<>();
                list.add("定额");
                list.add("不定额");
                dialog.showDialog(list);
                dialog.setTitle("选择基础金额模式");
                dialog.setListener(new SelectedDialogNew.OnItemSelectDialogClicked() {
                    @Override
                    public void onListItemClicked(int index) {// 周期连续与不连续
                        base_amount_type.setChoiceTextContent(list.get(index));
                        if (index == 0) {
                            ll_amount_regular.setVisibility(View.VISIBLE);
                            ll_amount_no_regular.setVisibility(View.GONE);
                            base_amount.setEditWidgetTitle("基础金额");
                            base_amount.setContentHint("请输入");
                            base_amount.setmContentMoneyEditText("");
                        } else {
                            ll_amount_regular.setVisibility(View.GONE);
                            ll_amount_no_regular.setVisibility(View.VISIBLE);
                            min_remain_amount.setEditWidgetTitle("最低预留金额");
                            max_invest_amount.setEditWidgetTitle("最大扣款金额");
                            min_remain_amount.setContentHint("请输入");
                            max_invest_amount.setContentHint("输入0为全额购买");
                            min_remain_amount.setmContentMoneyEditText("");
                            max_invest_amount.setmContentMoneyEditText("");
                        }
                    }
                });
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConditions()) {
                    if (currentFromValue == CONTINUE_FROM_VALUE) {// 理财推荐
                        getPresenter().queryRiskMatchByContinue(initBean.getSerialCode(), selectWealthAccount.getAccountKey());
                    } else {
                        getPresenter().queryRiskMatchByContinue(initBean.getSerialCode(), mViewModel.getAccountList().get(0).getAccountKey());
                    }
                }
            }
        });
        porfolio_account_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startForResult(SelectAccoutFragment.newInstanceWithData(accountBeanlist), SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT);
            }
        });
        sav_read.setOnClickContractListener(new SelectAgreementView.OnClickContractListener() {
            @Override
            public void onClickContract(int index) {
                if (index == 0) {
                    start(ContractFragment.newInstance("file:///android_asset/webviewcontent/wealthmanagement/portfoliopurchase/notice/notice.html"));
                } else {
                    String url = WealthConst.URL_INSTRUCTION + initBean.getProductCode();
                    start(PDFFragment.newInstance(Uri.parse(url)));
                }
            }
        });
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return "周期滚续协议";
    }

    @Override
    protected ProtocolPresenter initPresenter() {
        return new ProtocolPresenter(this);
    }

    public ProtocolModel getViewModel() {
        return mViewModel;
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (resultCode == SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT) {
            selectAccount = data.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
            getSelectWealthAccountBean();
            porfolio_account.setText(NumberUtils.formatCardNumberStrong(selectAccount.getAccountNumber()));
        }
    }

    /**
     * 获取accountWealthlist中选择的账户
     */
    private void getSelectWealthAccountBean() {
        for (int i = 0; i < accountWealthlist.size(); i++) {
            if (selectAccount.getAccountId().equals(accountWealthlist.get(i).getAccountId())) {
                selectWealthAccount = accountWealthlist.get(i);
            }
        }
    }

    /**
     * 页面数据初始化
     */
    private void initShowViewData() {
        txtHead.setText("[" + PublicCodeUtils.getCurrency(mContext, initBean.getCurCode()) + "]" + initBean.getProductName() + "（"
                + initBean.getProductCode() + "）");// 设置名称

        if (currentFromValue == CONTINUE_FROM_VALUE) {// 理财推荐
            String[] names = BuildViewData.buildContinueName();
            String[] values = BuildViewData.buildContinueValue(initBean.getSerialName(), MoneyUtils.transMoneyFormat(detailQueryResModel.getSubAmount(), detailQueryResModel.getCurCode()),
                    MoneyUtils.transMoneyFormat(detailQueryResModel.getAddAmount(), detailQueryResModel.getCurCode()));
            showHeadListView(names, values);

            if (ApplicationConst.CURRENCY_CNY.equals(detailQueryResModel.getCurCode())) {// 币种
                choice_currency.setVisibility(View.GONE);
            } else {
                choice_currency.setVisibility(View.VISIBLE);
            }
            if ("1".equals(detailQueryResModel.getPeriodical())) {// 若是周期性产品，则只能为定额模式
                base_amount_type.setChoiceTextContent("定额");
                base_amount_type.setClickable(false);
                base_amount_type.setEnabled(false);
                base_amount_type.setArrowImageGone(false);
            }
            if ("0".equals(ocrmDetail.getAmountType())) {//定额
                base_amount_type.setChoiceTextContent("定额");
                base_amount.setEditWidgetTitle("基础金额");
                base_amount.setContentHint("请输入");
                base_amount.setmContentMoneyEditText(MoneyUtils.transMoneyFormat(ocrmDetail.getAmount(), ocrmDetail.getCurrencyCode()));
            } else if ("1".equals(ocrmDetail.getAmountType())) {//不定额
                base_amount_type.setChoiceTextContent("不定额");
                min_remain_amount.setEditWidgetTitle("最低预留金额");
                max_invest_amount.setEditWidgetTitle("最大扣款金额");
                min_remain_amount.setContentHint("请输入");
                max_invest_amount.setContentHint("输入0为全额购买");
                min_remain_amount.setmContentMoneyEditText(MoneyUtils.transMoneyFormat(ocrmDetail.getMinAmount(), ocrmDetail.getCurrencyCode()));
                max_invest_amount.setmContentMoneyEditText(MoneyUtils.transMoneyFormat(ocrmDetail.getMaxAmount(), ocrmDetail.getCurrencyCode()));
            }

            base_amount.setCurrency(detailQueryResModel.getCurCode());
            min_remain_amount.setCurrency(detailQueryResModel.getCurCode());
            max_invest_amount.setCurrency(detailQueryResModel.getCurCode());

            invest_period.setEditWidgetHint("最大" + initBean.getRemainCycleCount() + "期");
            invest_period.setEditWidgetContent(ocrmDetail.getBoughtCount());//默认的买入期数

            porfolio_account.setText(NumberUtils.formatCardNumberStrong(selectAccount.getAccountNumber()));
            porfolio_account_change.setVisibility(View.VISIBLE);

        } else {
            String[] names = BuildViewData.buildContinueName();
            String[] values = BuildViewData.buildContinueValue(initBean.getSerialName(), MoneyUtils.transMoneyFormat(mViewModel.getSubAmount(), mViewModel.getCurCode()),
                    MoneyUtils.transMoneyFormat(mViewModel.getAddAmount(), mViewModel.getCurCode()));
            showHeadListView(names, values);

            if (ApplicationConst.CURRENCY_CNY.equals(mViewModel.getCurCode())) {// 币种
                choice_currency.setVisibility(View.GONE);
            } else {
                choice_currency.setVisibility(View.VISIBLE);
            }
            if ("1".equals(mViewModel.getPeriodical())) {// 若是周期性产品，则只能为定额模式
                base_amount_type.setChoiceTextContent("定额");
                base_amount_type.setClickable(false);
                base_amount_type.setEnabled(false);
                base_amount_type.setArrowImageGone(false);
            }
            base_amount.setCurrency(mViewModel.getCurCode());
            min_remain_amount.setCurrency(mViewModel.getCurCode());
            max_invest_amount.setCurrency(mViewModel.getCurCode());

            invest_period.setEditWidgetHint("最大" + initBean.getRemainCycleCount() + "期");
            base_amount_type.setChoiceTextContent("定额");
            base_amount.setEditWidgetTitle("基础金额");

            porfolio_account.setText(NumberUtils.formatCardNumberStrong(mViewModel.getAccountList().get(0).getAccountNo()));
            porfolio_account_change.setVisibility(View.GONE);
        }
        sav_read.setAgreement(mContext.getString(R.string.boc_protocol_read));//阅读协议
    }


    private void showHeadListView(String[] names, String[] values) {
        llHeadList.removeAllViews();
        for (int i = 0; i < names.length; i++) {
            View view = View.inflate(mContext, R.layout.boc_fragment_protocol_item, null);
            TextView txtName = (TextView) view.findViewById(R.id.txt_name);
            TextView txtValue = (TextView) view.findViewById(R.id.txt_content);
            txtName.setText(names[i]);
            txtValue.setText(values[i]);
            llHeadList.addView(view);
        }
    }

    /**
     * 判断输入内容的正确性
     *
     * @return
     */
    private boolean checkConditions() {
        if (TextUtils.isEmpty(invest_period.getEditWidgetContent())) {
            showErrorDialog("请输入认购期数");
            return false;
        }

        if (Integer.parseInt(invest_period.getEditWidgetContent()) > Integer.parseInt(initBean.getRemainCycleCount())) {
            showErrorDialog("认购期数输入有误，请输入大于0且小于等于最大投资期数的整数");
            return false;
        }

        if ("定额".equals(base_amount_type.getChoiceTextContent())) {
            if (TextUtils.isEmpty(base_amount.getContentMoney())) {
                showErrorDialog("请输入大于0的基础金额");
                return false;
            }
        } else if ("不定额".equals(base_amount_type.getChoiceTextContent())) {
            if (TextUtils.isEmpty(min_remain_amount.getContentMoney())) {
                showErrorDialog("请输入最低预留金额");
                return false;
            }

            if (TextUtils.isEmpty(max_invest_amount.getContentMoney())) {
                showErrorDialog("请输入最大扣款金额");
                return false;
            }
        }

        if (!sav_read.isSelected()) {
            showErrorDialog("请阅知勾选产品相关协议后进行下一步操作");
            return false;
        }
        return true;
    }

    @Override
    public void psnXpadQueryRiskMatchReturned() {
        if ("0".equals(mViewModel.getRiskMatch())) {
            buildViewData();
            Bundle bundle = new Bundle();
            bundle.putParcelable(CONTINUERESULT, mViewModel);
            ConfirmContinueFragment fragment = new ConfirmContinueFragment();
            fragment.setArguments(bundle);
            start(fragment);
        } else if ("1".equals(mViewModel.getRiskMatch())) {
            final MessageDialog dialog = new MessageDialog(getContext());
            dialog.setLeftButtonText("放弃");
            dialog.setRightButtonText("确认购买");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setRightButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buildViewData();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(CONTINUERESULT, mViewModel);
                    ConfirmContinueFragment fragment = new ConfirmContinueFragment();
                    fragment.setArguments(bundle);
                    start(fragment);
                    dialog.dismiss();
                }
            });
            dialog.showDialog(getString(R.string.boc_protocol_risk_hint_1));
        } else if ("2".equals(mViewModel.getRiskMatch())) {
            final MessageDialog dialog = new MessageDialog(getContext());
            dialog.setLeftButtonText("重新风险评估");
            if (findFragment(WealthProductFragment.class) != null) {
                dialog.setRightButtonText("去看看其他产品");
            } else {
                dialog.setRightButtonText("关闭");
            }
            dialog.setCanceledOnTouchOutside(false);
            dialog.showDialog(getString(R.string.boc_protocol_risk_hint_2));
            dialog.setLeftButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    start(new RiskAssessFragment(ProtocolPeriodContinueFragment.class));
                }
            });
            dialog.setRightButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (findFragment(WealthProductFragment.class) != null)
                        popToAndReInit(WealthProductFragment.class);
                }
            });
        }
    }

    /**
     * 周期滚续确认（请求）
     *
     * @return
     */
    private void buildViewData() {
        mViewModel.setProId(initBean.getProductCode());
        if (currentFromValue == CONTINUE_FROM_VALUE) {
            mViewModel.setAccountId(selectAccount.getAccountId());
            mViewModel.setAccountNum(selectAccount.getAccountNumber());
        } else {
            mViewModel.setAccountId(mViewModel.getAccountList().get(0).getAccountId());
            mViewModel.setAccountNum(mViewModel.getAccountList().get(0).getAccountNo());
        }
        mViewModel.setSerialName(initBean.getSerialName());
        mViewModel.setSerialCode(initBean.getSerialCode());
        mViewModel.setCurCode(initBean.getCurCode());
        mViewModel.setTotalPeriod(invest_period.getEditWidgetContent());
        mViewModel.setDealCode(currentDealCode);
        if (ApplicationConst.CURRENCY_CNY.equals(initBean.getCurCode())) {
            mViewModel.setXpadCashRemit("0");
        } else {
            if ("现钞".equals(choice_currency.getChoiceTextContent())) {
                mViewModel.setXpadCashRemit("1");
            } else if ("现汇".equals(choice_currency.getChoiceTextContent())) {
                mViewModel.setXpadCashRemit("2");
            }
        }
        if ("定额".equals(base_amount_type.getChoiceTextContent())) {
            mViewModel.setAmountTypeCode("0");
            mViewModel.setBaseAmount(base_amount.getContentMoney());
            mViewModel.setMinAmount("0");// 账户保留余额
            mViewModel.setMaxAmount("0");// 最大购买金额
        } else if ("不定额".equals(base_amount_type.getChoiceTextContent())) {
            mViewModel.setAmountTypeCode("1");
            mViewModel.setMinAmount(min_remain_amount.getContentMoney());// 账户保留余额
            mViewModel.setMaxAmount(max_invest_amount.getContentMoney());// 最大购买金额
            mViewModel.setBaseAmount("0");
        }
    }

    /**
     * 将理财账户数据转换为通用账户数据
     *
     * @param sourceData
     * @return
     */
    public static ArrayList<AccountBean> changeWealthBean2AccountBean(ArrayList<WealthAccountBean> sourceData) {
        ArrayList<AccountBean> mAccountList = new ArrayList<AccountBean>();
        if (null != sourceData) {
            for (WealthAccountBean item : sourceData) {
                AccountBean convertItem = new AccountBean();
                convertItem.setAccountType(item.getAccountType());
                convertItem.setAccountIbkNum(item.getAccountIbkNum());
                convertItem.setAccountId(String.valueOf(item.getAccountId()));
                convertItem.setAccountName(item.getAccountName());
//                convertItem.setAccountNumber(item.getAccountNumber());
                convertItem.setAccountStatus(item.getAccountStatus());
                convertItem.setBranchId(String.valueOf(item.getBranchId()));
                convertItem.setCardDescription(item.getCardDescription());
                convertItem.setCurrencyCode(item.getCurrencyCode());
                convertItem.setCurrencyCode2(item.getCurrencyCode2());
                convertItem.setCardDescriptionCode(item.getCardDescriptionCode());
                convertItem.setCustomerId(String.valueOf(item.getCustomerId()));
                convertItem.setBranchName(item.getBranchName());
                convertItem.setEcard(item.getEcard());
                convertItem.setHasOldAccountFlag(item.getHasOldAccountFlag());
                convertItem.setIsECashAccount(item.getIsECashAccount());
                convertItem.setIsMedicalAccount(item.getIsMedicalAccount());
                convertItem.setNickName(item.getNickName());
                convertItem.setVerifyFactor(item.getVerifyFactor());

                convertItem.setAccountNumber(item.getAccountNo());//资金账号
                mAccountList.add(convertItem);
            }
        }
        return mAccountList;
    }
}
