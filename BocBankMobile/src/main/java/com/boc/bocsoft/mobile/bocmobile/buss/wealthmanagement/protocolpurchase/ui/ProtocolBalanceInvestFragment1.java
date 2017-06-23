package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
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
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * 余额理财投资
 * Created by zhx on 2016/11/9
 */
public class ProtocolBalanceInvestFragment1 extends MvpBussFragment<ProtocolBalanceInvestContact.Presenter> implements ProtocolBalanceInvestContact.View {
    private View rootView;
    private TextView tv_sub_amount;
    private TextView tv_add_amount;
    private TextView tv_currency_and_proname;
    private EditChoiceWidget ecw_xpad_cashremit;
    private View line_1;
    private EditMoneyInputWidget money_1;
    private EditMoneyInputWidget money_2;
    private EditChoiceWidget ecw_total_period;
    private EditChoiceWidget ecw_invest_time;
    private SelectAgreementView sav_read;
    private TextView tv_next;
    private TextView txt_account;
    private ProtocolModel mViewModel;
    private ProtocolBalanceInvestPresenter protocolBalanceInvestPresenter;
    private String xpadCashRemit = "0"; // 钞汇标识（0人民币、1钞、2汇）
    private String totalPeriod = "-1"; // 总期数（-1 不限期或者为大于0的整数）
    private boolean isDialogItemClick = false; // 是否是对话框的条目被点击
    private String strContent; //
    private XpadQueryRiskMatchViewModel riskMatchViewModel;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_protocol_balance, null);
        return rootView;
    }

    @Override
    public void initView() {
        tv_sub_amount = (TextView) rootView.findViewById(R.id.tv_sub_amount); // 申购起点金额
        tv_add_amount = (TextView) rootView.findViewById(R.id.tv_add_amount); // 追加申购起点金额
        tv_currency_and_proname = (TextView) rootView.findViewById(R.id.tv_currency_and_proname); // 币种和产品名称
        ecw_xpad_cashremit = (EditChoiceWidget) rootView.findViewById(R.id.ecw_xpad_cashremit); // 钞汇标识
        line_1 = (View) rootView.findViewById(R.id.line_1);
        ecw_total_period = (EditChoiceWidget) rootView.findViewById(R.id.ecw_total_period); // 总期数
        money_1 = (EditMoneyInputWidget) rootView.findViewById(R.id.money_1); // 赎回触发金额
        money_2 = (EditMoneyInputWidget) rootView.findViewById(R.id.money_2); // 购买触发金额
        ecw_invest_time = (EditChoiceWidget) rootView.findViewById(R.id.ecw_invest_time); // 自动开始投资日
        sav_read = (SelectAgreementView) rootView.findViewById(R.id.sav_read); // 协议
        tv_next = (TextView) rootView.findViewById(R.id.tv_next); // “下一步”按钮
        txt_account = (TextView) rootView.findViewById(R.id.txt_account); // 理财交易账户

        // --------“赎回触发金额”字段---------
        money_1.setScrollView(mContentView);
        money_1.setContentHint("请输入");
        money_1.setEditWidgetTitle("赎回触发金额");
        money_1.setMaxLeftNumber(12);
        money_1.setMaxRightNumber(2);

        // --------“购买触发金额”字段---------
        money_2.setScrollView(mContentView);
        money_2.setContentHint("请输入");
        money_2.setEditWidgetTitle("购买触发金额");
        money_2.setMaxLeftNumber(12);
        money_2.setMaxRightNumber(2);
        money_2.getContentMoneyEditText().setHint("输入0为全额购买");
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
        money_1.setCurrency(mViewModel.getCurCode());
        money_2.setCurrency(mViewModel.getCurCode());


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
                        strContent = list.get(index);
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
                        strContent = null;
                    }
                });
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

    // 检查数据
    private boolean checkData() {
        String money1 = money_1.getContentMoney();
        if (TextUtils.isEmpty(money1)) {
            showErrorDialog("请输入赎回触发金额");
            return false;
        }

        String money2 = money_2.getContentMoney();
        if (TextUtils.isEmpty(money2)) {
            showErrorDialog("请输入购买触发金额");
            return false;
        }

        double m1 = Double.parseDouble(money1);
        double m2 = Double.parseDouble(money2);
        if (m1 > m2) {
            showErrorDialog("购买触发金额需大于等于赎回触发金额");
            return false;
        }

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
        return "余额理财投资";
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return true;
    }

    @Override
    public void queryRiskMatchSuccess(XpadQueryRiskMatchViewModel riskMatchViewModel) {
        this.riskMatchViewModel = riskMatchViewModel;

        // 判断是否可以跳转到下一个页面
        if ("0".equals(riskMatchViewModel.getRiskMatch())) {
            goToConfrimFragment();
        } else if ("2".equals(riskMatchViewModel.getRiskMatch())) {
            String leftTitle = getString(R.string.boc_purchase_product_risk_retry);
            String rightTitle = "";
            if (findFragment(WealthProductFragment.class) != null) {
                rightTitle="去看看其他产品";
            } else {
                rightTitle="关闭";
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
        ProtocolBalanceInvestConfirmFragment toFragment = new ProtocolBalanceInvestConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ProtocolSelectFragment.PROTOCOL_PURCHASE, mViewModel);
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

    @Override
    protected ProtocolBalanceInvestContact.Presenter initPresenter() {
        protocolBalanceInvestPresenter = new ProtocolBalanceInvestPresenter(ProtocolBalanceInvestFragment1.this);
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
                    start(new RiskAssessFragment(ProtocolBalanceInvestFragment1.class));
                    break;
                case TYPE_RISK_OTHER: // 看看其它的产品
                    messageDialog.dismiss();
                    if (findFragment(WealthProductFragment.class) != null)
                        popToAndReInit(WealthProductFragment.class);
                    break;
            }
        }
    }

    // 生成“投资协议申请”的ViewModel
    private XpadApplyAgreementResultViewModel generateXpadApplyAgreementResultViewModel() {
        XpadApplyAgreementResultViewModel viewModel = new XpadApplyAgreementResultViewModel();

        viewModel.setXpadCashRemit(xpadCashRemit); // 钞汇标识
        viewModel.setTotalPeriod(totalPeriod); // 总期数
        viewModel.setInvestType("1"); // 投资方式
        viewModel.setMinAmount(money_1.getContentMoney()); // 赎回触发金额
        viewModel.setMaxAmount(money_2.getContentMoney()); // 购买触发金额
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

}