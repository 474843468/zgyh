package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DateTimePicker;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.assignment.SelectAgreementView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialog.MessageDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.DoubleDataSelectedDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectedDialogWithSelfInput;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.webView.ContractFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.DateUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.riskassess.ui.RiskAssessFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.ProtocolModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.XpadApplyAgreementResultViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.XpadQueryRiskMatchViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.presenter.ProtocolBalanceInvestPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.widget.SelectedDialogNew;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selectdialog.SelectDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * 定时定额投资
 * Created by zhx on 2016/11/9
 */
public class FixedtimeFixedAmountInvestFragment extends MvpBussFragment<ProtocolBalanceInvestContact.Presenter> implements ProtocolBalanceInvestContact.View {
    private View rootView;
    private TextView tv_sub_amount;
    private TextView tv_add_amount;
    private LinearLayout ll_sub_amount;
    private LinearLayout ll_add_amount;
    private TextView tv_low_limit_amount;
    private LinearLayout ll_low_limit_amount;
    private TextView tv_currency_and_proname;
    protected LinearLayout purchaseBox;
    protected LinearLayout redeemBox;
    private EditChoiceWidget ecw_xpad_cashremit; // 钞汇
    private View line_1;
    private EditMoneyInputWidget emiw_redeem_amount; // 定投金额
    private EditMoneyInputWidget money_1;
    private EditMoneyInputWidget money_2;
    private EditChoiceWidget ecw_total_period;
    protected EditChoiceWidget investOften;
    private EditChoiceWidget ecw_invest_time;
    private SelectAgreementView sav_read;
    private ProtocolModel mViewModel;
    private TextView tv_next;
    private TextView txt_account;
    private ProtocolBalanceInvestPresenter protocolBalanceInvestPresenter;
    private String xpadCashRemit = "0"; // 钞汇标识（0人民币、1钞、2汇）
    private String totalPeriod = "-1"; // 总期数（-1 不限期或者为大于0的整数）
    private String investOftenStr = null; // 定投频率
    private String timeInvestType = "0"; // 定投类型(0购买 1赎回)
    private boolean isDialogItemClick = false; // 是否是对话框的条目被点击
    private XpadQueryRiskMatchViewModel riskMatchViewModel;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_fixedtime_fixedamount_invest, null);
        return rootView;
    }

    @Override
    public void initView() {
        tv_sub_amount = (TextView) rootView.findViewById(R.id.tv_sub_amount); // 申购起点金额
        tv_add_amount = (TextView) rootView.findViewById(R.id.tv_add_amount); // 追加申购起点金额
        tv_low_limit_amount = (TextView) rootView.findViewById(R.id.tv_low_limit_amount); // 追加申购起点金额
        ll_sub_amount = (LinearLayout) rootView.findViewById(R.id.ll_sub_amount);
        ll_add_amount = (LinearLayout) rootView.findViewById(R.id.ll_add_amount);
        ll_low_limit_amount = (LinearLayout) rootView.findViewById(R.id.ll_low_limit_amount); // 赎回起点份额
        tv_currency_and_proname = (TextView) rootView.findViewById(R.id.tv_currency_and_proname); // 币种和产品名称
        purchaseBox = (LinearLayout) rootView.findViewById(R.id.purchase_box);
        redeemBox = (LinearLayout) rootView.findViewById(R.id.redeem_box);
        ecw_xpad_cashremit = (EditChoiceWidget) rootView.findViewById(R.id.ecw_xpad_cashremit); // 钞汇标识
        line_1 = (View) rootView.findViewById(R.id.line_1);
        emiw_redeem_amount = (EditMoneyInputWidget) rootView.findViewById(R.id.emiw_redeem_amount); // 定投金额
        ecw_total_period = (EditChoiceWidget) rootView.findViewById(R.id.ecw_total_period); // 总期数
        money_1 = (EditMoneyInputWidget) rootView.findViewById(R.id.money_1); // 赎回触发金额
        money_2 = (EditMoneyInputWidget) rootView.findViewById(R.id.money_2); // 购买触发金额
        investOften = (EditChoiceWidget) rootView.findViewById(R.id.invest_often);
        ecw_invest_time = (EditChoiceWidget) rootView.findViewById(R.id.ecw_invest_time); // 自动开始投资日
        sav_read = (SelectAgreementView) rootView.findViewById(R.id.sav_read); // 协议
        tv_next = (TextView) rootView.findViewById(R.id.tv_next); // “下一步”按钮
        txt_account = (TextView) rootView.findViewById(R.id.txt_account); // 理财交易账户

        // --------“定投金额”字段---------
        emiw_redeem_amount.setScrollView(mContentView);
        emiw_redeem_amount.setContentHint("请输入");
        emiw_redeem_amount.setEditWidgetTitle("定投金额");
        emiw_redeem_amount.setMaxLeftNumber(12);
        emiw_redeem_amount.setMaxRightNumber(2);
        emiw_redeem_amount.setCurrency("001");
        emiw_redeem_amount.getContentMoneyEditText().setTextColor(getResources().getColor(R.color.boc_text_color_red));
    }

    @Override
    public void initData() {
        mViewModel = getArguments().getParcelable(ProtocolSelectFragment.PROTOCOL_PURCHASE);

        // zhx ------------ temp2016-11-11 -------- start
        if (mViewModel == null) {
            mViewModel = new ProtocolModel();
        }
        //        if (resultViewModel == null) {
        //            resultViewModel = new XpadApplyAgreementResultViewModel();
        //        }
        // zhx ------------ temp2016-11-11 -------- end
        emiw_redeem_amount.setCurrency(mViewModel.getCurCode());

        // 设置币种和产品名称
        String currency = PublicCodeUtils.getCurrency(mActivity, mViewModel.getCurCode());
        String prodName = mViewModel.getProdName();
        tv_currency_and_proname.setText("[" + currency + "]" + prodName);
        // 设置申购起点金额
        String subAmountFormat = MoneyUtils.transMoneyFormat(mViewModel.getSubAmount(), mViewModel.getCurCode());
        tv_sub_amount.setText(subAmountFormat);
        // 设置追加申购起点金额
        String addAmountFormat = MoneyUtils.transMoneyFormat(mViewModel.getAddAmount(), mViewModel.getCurCode());
        tv_add_amount.setText(addAmountFormat);
        // 赎回起点份额
        tv_low_limit_amount.setText(MoneyUtils.transMoneyFormat(mViewModel.getLowLimitAmount(), mViewModel.getCurCode()));
        // 设置钞汇标识
        boolean isRMB = "001".equals(mViewModel.getCurCode());
        ecw_xpad_cashremit.setVisibility(isRMB ? View.GONE : View.VISIBLE);
        line_1.setVisibility(isRMB ? View.GONE : View.VISIBLE);
        if (!isRMB) {
            xpadCashRemit = "2";
        } else {
            xpadCashRemit = "0";
        }
        // 设置期数
        // 设置定投频率
        investOften.setChoiceTextContent("请选择");

        // 设置默认的“自动开始投资日”
        LocalDate defaultDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().plusDays(1);
        ecw_invest_time.setChoiceTextContent(defaultDate.format(DateFormatters.dateFormatter1));
        // 阅读协议
        sav_read.setAgreement(mContext.getString(R.string.boc_protocol_read));
        // 理财交易账户
        String accountNoStr = "";
        if (mViewModel.getAccountList() != null && mViewModel.getAccountList().size() > 0) {
            String accountNo = mViewModel.getAccountList().get(0).getAccountNo();
            String start = accountNo.substring(0, 4);
            String end = accountNo.substring(accountNo.length() - 4, accountNo.length());
            accountNoStr = start + " ****** " + end;
        }
        txt_account.setText("理财交易账户 " + accountNoStr);


    }

    @Override
    public void setListener() {
        // 钞汇标识
        ecw_xpad_cashremit.setOnClickListener(new View.OnClickListener() {
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
                        switch (index) {
                            case 0:
                                ecw_xpad_cashremit.setChoiceTextContent("现钞");
                                xpadCashRemit = "1";
                                break;
                            default:
                                ecw_xpad_cashremit.setChoiceTextContent("现汇");
                                xpadCashRemit = "2";
                                break;
                        }
                    }
                });
            }
        });

        // “购买”按钮
        purchaseBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView icon = (ImageView) purchaseBox.findViewById(R.id.purchase_image);
                TextView text = (TextView) purchaseBox.findViewById(R.id.purchase_text);
                icon.setVisibility(View.VISIBLE);
                text.setTextColor(getResources().getColor(R.color.boc_text_color_red));
                purchaseBox.setBackgroundResource(R.drawable.boc_textview_bg_light);

                ImageView icon2 = (ImageView) redeemBox.findViewById(R.id.redeem_image);
                TextView text2 = (TextView) redeemBox.findViewById(R.id.redeem_text);
                icon2.setVisibility(View.GONE);
                text2.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
                redeemBox.setBackgroundResource(R.drawable.boc_textview_bg_default);

                timeInvestType = "0";
                ll_sub_amount.setVisibility(View.VISIBLE);
                ll_add_amount.setVisibility(View.VISIBLE);
                ll_low_limit_amount.setVisibility(View.GONE);

                emiw_redeem_amount.setEditWidgetTitle("定投金额");
                emiw_redeem_amount.getContentMoneyEditText().setHint("请输入");
            }
        });

        // "赎回"按钮
        redeemBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView icon = (ImageView) redeemBox.findViewById(R.id.redeem_image);
                TextView text = (TextView) redeemBox.findViewById(R.id.redeem_text);
                icon.setVisibility(View.VISIBLE);
                text.setTextColor(getResources().getColor(R.color.boc_text_color_red));
                redeemBox.setBackgroundResource(R.drawable.boc_textview_bg_light);

                ImageView icon2 = (ImageView) purchaseBox.findViewById(R.id.purchase_image);
                TextView text2 = (TextView) purchaseBox.findViewById(R.id.purchase_text);
                icon2.setVisibility(View.GONE);
                text2.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
                purchaseBox.setBackgroundResource(R.drawable.boc_textview_bg_default);

                timeInvestType = "1";
                ll_sub_amount.setVisibility(View.GONE);
                ll_add_amount.setVisibility(View.GONE);
                ll_low_limit_amount.setVisibility(View.VISIBLE);

                emiw_redeem_amount.setEditWidgetTitle("定赎份额");
                emiw_redeem_amount.getContentMoneyEditText().setHint("输入0为全额赎回");
            }
        });

        // 总期数
        ecw_total_period.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SelectedDialogWithSelfInput dialog = new SelectedDialogWithSelfInput(mActivity);
                final List<String> list = new ArrayList<>();
                list.add("不限期");
                list.add("6期");
                list.add("12期");
                list.add("24期");
                dialog.showDialog(list);
                dialog.setTitle("选择总期数");
                dialog.setOnBottomViewClick(new SelectedDialogWithSelfInput.OnBottomViewClick() {
                    @Override
                    public void onBottomViewClick(String str) {
                    }
                });
                dialog.setListener(new SelectedDialogWithSelfInput.OnItemSelectDialogClicked() {
                    @Override
                    public void onListItemClicked(int index) {
                        String strContent = list.get(index);
                        ecw_total_period.setChoiceTextContent(strContent);
                        if ("不限期".equals(strContent)) {
                            totalPeriod = "-1";
                        } else if (strContent.endsWith("期")) {
                            totalPeriod = strContent.substring(0, strContent.length() - 1);
                        }
                    }
                });
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface d) {
                        if (!isDialogItemClick) {
                            String str = dialog.getEditContent().getText().toString();
                            if (!TextUtils.isEmpty(str)) {
                                totalPeriod = str;
                                ecw_total_period.setChoiceTextContent(str + "期");
                            }
                        }
                        isDialogItemClick = false;
                    }
                });
            }
        });

        emiw_redeem_amount.setMoneyChangeListener(new EditMoneyInputWidget.MoneyChangeListenerInterface() {
            @Override
            public void setOnMoneyChangeListener(String str) {
                String familyName = null;
                if (TextUtils.isEmpty(str)) {
                    emiw_redeem_amount.getContentMoneyEditText().setTypeface(Typeface.create(familyName, Typeface.NORMAL));
                } else {
                    emiw_redeem_amount.getContentMoneyEditText().setTypeface(Typeface.create(familyName, Typeface.BOLD));
                }
            }
        });

        // 定投频率
        investOften.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> list = new ArrayList<>();
                list.add("每周一次");
                list.add("每月一次");
                list.add("每三月一次");
                list.add("每年一次");
                list.add("自定义");
                SelectDialog dialog = new SelectDialog(getContext());
                dialog.setListener(new SelectDialog.OnItemSelectDialogClicked() {
                    @Override
                    public void onListItemClicked(int index) {
                        if (index != 4) {
                            investOften.setChoiceTextContent(list.get(index));
                        }

                        if (index == 0) {
                            investOftenStr = "1w";
                        } else if (index == 1) {
                            investOftenStr = "1m";
                        } else if (index == 2) {
                            investOftenStr = "3m";
                        } else if (index == 3) {
                            investOftenStr = "1y";
                        } else if (index == 4) {
                            // 显示自定义选择频率的对话框
                            showCustomOftenDialog();
                        }

                    }
                });
                dialog.showDialog(list);
                dialog.setTitle("选择定投频率");
            }
        });

        sav_read.setOnClickContractListener(new SelectAgreementView.OnClickContractListener() {
            @Override
            public void onClickContract(int index) {
                if (index == 0) {
                    start(ContractFragment.newInstance("file:///android_asset/webviewcontent/wealthmanagement/portfoliopurchase/notice/notice.html"));
                } else {
                    start(ContractFragment.newInstance("http://srh.bankofchina.com/search/finprod/getProdPage.jsp?keyword=" + mViewModel.getProductId()));
                }
            }
        });

        // 自动开始投资日
        ecw_invest_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LocalDateTime currentSystemDate = ApplicationContext.getInstance().getCurrentSystemDate();
                LocalDate defaultDate = currentSystemDate.toLocalDate().plusDays(1);
                long minDate = DateUtil.parse(defaultDate.format(DateFormatters.dateFormatter1));
                DateTimePicker.showRangeDatePick(mContext, defaultDate, minDate, 0, DateFormatters.dateFormatter1, new DateTimePicker.DatePickCallBack() {
                    @Override
                    public void onChoiceDateSet(String strChoiceTime, LocalDate choiceDate) {
                        if (!currentSystemDate.toLocalDate().isBefore(choiceDate)) {
                            showErrorDialog("请选择明天之后的日期");
                            return;
                        }

                        ecw_invest_time.setChoiceTextContent(choiceDate.format(DateFormatters.dateFormatter1)); // 回显选中的日期
                    }
                });
            }
        });

        // “下一步”按钮
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkData()) {
                    return;
                }

                XpadQueryRiskMatchViewModel xpadQueryRiskMatchViewModel = new XpadQueryRiskMatchViewModel();
                xpadQueryRiskMatchViewModel.setAccountKey(mViewModel.getAccountList().get(0).getAccountKey()); // 账号缓存标识
                xpadQueryRiskMatchViewModel.setProductCode(mViewModel.getProId()); // 产品代码(选输周期性产品系列编号与产品数字代码均未输入，则此项必输)
                getPresenter().queryRiskMatch(xpadQueryRiskMatchViewModel);
            }
        });

    }

    // 显示自定义"定投频率"选择对话框
    private void showCustomOftenDialog() {
        final List<String> leftList = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            leftList.add(i + "个月一次");
        }

        final List<String> rightList = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            rightList.add(i + "天一次");
        }

        DoubleDataSelectedDialog dialog = new DoubleDataSelectedDialog(mActivity);
        dialog.setListener(new DoubleDataSelectedDialog.OnItemSelectDialogClicked() {
            @Override
            public void onListItemClicked(int leftOrRight, int index) {
                switch (leftOrRight) {
                    case 0: // 左边条目被点击
                        investOftenStr = index + 1 + "m";
                        investOften.setChoiceTextContent(leftList.get(index));
                        break;
                    case 1: // 右边条目被点击
                        investOftenStr = index + 1 + "d";
                        investOften.setChoiceTextContent(rightList.get(index));
                        break;
                }
            }
        });
        dialog.showDialog(leftList, rightList);
        dialog.setTitle("月", "日");
    }

    // 检查数据
    private boolean checkData() {
        // 定投金额（或定赎份额）
        String contentMoney = emiw_redeem_amount.getContentMoney();

        if (TextUtils.isEmpty(contentMoney)) {
            if ("0".equals(timeInvestType)) {
                showErrorDialog("请输入大于0的定投金额");
            } else {
                showErrorDialog("请输入定赎份额");
            }

            return false;
        }

        double moneyValue = Double.parseDouble(contentMoney);
        if ("0".equals(timeInvestType) && moneyValue <= 0) {
            showErrorDialog("请输入大于0的定投金额");
            return false;
        }

        // 定投频率
        if (TextUtils.isEmpty(investOftenStr)) {
            showErrorDialog("请选择定投频率");
            return false;
        }

        // 协议
        if (!sav_read.isSelected()) {
            showErrorDialog("请阅知勾选产品相关协议后进行下一步操作");
            return false;
        }

        return true;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return "定时定额投资";
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String listItem = data.getStringExtra("listItem");
        ecw_total_period.setChoiceTextContent(listItem);
    }

    @Override
    public void queryRiskMatchSuccess(XpadQueryRiskMatchViewModel riskMatchViewModel) {
        FixedtimeFixedAmountInvestFragment.this.riskMatchViewModel = riskMatchViewModel;

        // 判断是否可以跳转到下一个页面
        if ("0".equals(riskMatchViewModel.getRiskMatch())) {
            goToConfrimFragment();
        } else if ("2".equals(riskMatchViewModel.getRiskMatch())) {
            String leftTitle = getString(R.string.boc_purchase_product_risk_retry);
            String rightTitle = "";
            if (findFragment(WealthProductFragment.class) != null) {
                rightTitle = "去看看其他产品";
            } else {
                rightTitle = "关闭";
            }
            showMessageDialog(leftTitle, new MessageOnClickListener(MessageOnClickListener.TYPE_RISK_RETRY), rightTitle, new MessageOnClickListener(MessageOnClickListener.TYPE_RISK_OTHER), getString(R.string.boc_purchase_product_risk_fail_hint));
        } else if ("1".equals(riskMatchViewModel.getRiskMatch())) {
            String left = getString(R.string.boc_purchase_product_risk_cancel);
            String right = getString(R.string.boc_purchase_product_risk_ok);
            showMessageDialog(left, right, new MessageOnClickListener(MessageOnClickListener.TYPE_PRE_TRANSACTION), getString(R.string.boc_purchase_product_risk_warn_hint));
        }
    }

    // 跳转到确认页面
    private void goToConfrimFragment() {
        FixedtimeFixedAmountInvestConfirmFragment toFragment = new FixedtimeFixedAmountInvestConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ProtocolSelectFragment.PROTOCOL_PURCHASE, this.mViewModel);
        XpadApplyAgreementResultViewModel resultViewModel = generateXpadApplyAgreementResultViewModel();
        bundle.putParcelable("resultViewModel", resultViewModel);
        bundle.putParcelable("riskMatchViewModel", this.riskMatchViewModel);
        toFragment.setArguments(bundle);
        start(toFragment);
    }

    private MessageDialog messageDialog;

    private void showMessageDialog(String leftTitle, String rightTitle, View.OnClickListener rightListener, String content) {
        showMessageDialog(leftTitle, null, rightTitle, rightListener, content);

    }

    private void showMessageDialog(String leftTitle, View.OnClickListener leftListener, String rightTitle, View.OnClickListener rightListener, String content) {
        if (messageDialog == null)
            messageDialog = new MessageDialog(mContext);

        if (!StringUtils.isEmptyOrNull(leftTitle))
            messageDialog.setLeftButtonText(leftTitle);

        messageDialog.setLeftButtonClickListener(leftListener);

        if (!StringUtils.isEmptyOrNull(rightTitle))
            messageDialog.setRightButtonText(rightTitle);

        messageDialog.setRightButtonClickListener(rightListener);

        messageDialog.showDialog(content);
    }

    // 生成“投资协议申请”的ViewModel
    private XpadApplyAgreementResultViewModel generateXpadApplyAgreementResultViewModel() {
        XpadApplyAgreementResultViewModel viewModel = new XpadApplyAgreementResultViewModel();

        viewModel.setXpadCashRemit(xpadCashRemit); // 钞汇标识
        viewModel.setTotalPeriod(totalPeriod); // 总期数
        viewModel.setInvestType("0"); // 投资方式(0表示定时定额,1表示余额理财)
        viewModel.setRedeemAmount(emiw_redeem_amount.getContentMoney()); // 定投金额
        //        viewModel.setMinAmount(money_1.getContentMoney()); // 赎回触发金额
        //        viewModel.setMaxAmount(money_2.getContentMoney()); // 购买触发金额
        String oftenRate = investOftenStr.substring(0, investOftenStr.length() - 1);
        String oftenRateFlag = investOftenStr.substring(investOftenStr.length() - 1);
        viewModel.setTimeInvestType(timeInvestType); // 定投类型
        viewModel.setTimeInvestRate(oftenRate); // 定投频率
        viewModel.setTimeInvestRateFlag(oftenRateFlag); // 定投频率类型(定投频率类型须输入格式为字母,如'w'表示周, d:天, w:周, m:月, y:年；)
        viewModel.setInvestTime(ecw_invest_time.getChoiceTextContent()); // 首次投资日
        return viewModel;
    }

    // 失败回调：查询客户风险等级与产品风险等级是否匹配
    @Override
    public void queryRiskMatchFailed(BiiResultErrorException biiResultErrorException) {
    }

    @Override
    public void setPresenter(ProtocolBalanceInvestContact.Presenter presenter) {
    }

    @Override
    protected ProtocolBalanceInvestContact.Presenter initPresenter() {
        protocolBalanceInvestPresenter = new ProtocolBalanceInvestPresenter(FixedtimeFixedAmountInvestFragment.this);
        return protocolBalanceInvestPresenter;
    }

    private class MessageOnClickListener implements View.OnClickListener {

        /**
         * 预交易
         */
        private static final int TYPE_PRE_TRANSACTION = 1;
        /**
         * 挂单弹框
         */
        private static final int TYPE_ORDER_TIME = 2;
        /**
         * 投资弹框
         */
        private static final int TYPE_INVEST = 3;
        /**
         * 先投资弹框,再挂单弹框
         */
        private static final int TYPE_INVEST_AND_ORDER_TIME = 4;
        /**
         * 重新风险评估
         */
        private static final int TYPE_RISK_RETRY = 5;
        /**
         * 查看其它理财产品
         */
        private static final int TYPE_RISK_OTHER = 6;

        private int type;

        public MessageOnClickListener(int type) {
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            switch (type) {
                case TYPE_PRE_TRANSACTION: // 跳转到确认页面
                    goToConfrimFragment();
                    messageDialog.dismiss();
                    break;
                case TYPE_ORDER_TIME:
                case TYPE_INVEST:
                    messageDialog.dismiss();
                    //                    start(new PurchaseConfirmFragment(model));
                    break;
                case TYPE_INVEST_AND_ORDER_TIME:
                    //                    showMessageDialog("取消", "确认", new MessageOnClickListener(MessageOnClickListener.TYPE_ORDER_TIME), "该产品在非交易期间，可进行挂单交易，挂单交易将在下一个工作日由系统自动成交。点击“确认”继续交易，点击“取消”取消交易。");
                    break;
                case TYPE_RISK_RETRY: // 重新进行风险评估
                    messageDialog.dismiss();
                    start(new RiskAssessFragment(FixedtimeFixedAmountInvestFragment.class));
                    break;
                case TYPE_RISK_OTHER: // 看看其它的产品
                    messageDialog.dismiss();
                    if (findFragment(WealthProductFragment.class) != null)
                        popToAndReInit(WealthProductFragment.class);
                    break;
            }
        }
    }
}