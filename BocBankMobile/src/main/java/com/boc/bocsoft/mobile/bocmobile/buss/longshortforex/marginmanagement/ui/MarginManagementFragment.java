package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.ui;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussActivity;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selectdialog.SelectDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.MenuFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGQueryMaxAmountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.balanceenquiry.ui.BalanceEnquiryFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.model.XpadVFGGetBindAccountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.presenter.MarginManagementPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.tencent.mm.sdk.platformtools.Log;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hty7062
 *         保证金转入转出--首页面
 */
public class MarginManagementFragment extends MvpBussFragment<MarginManagementContract.Presenter>
    implements MarginManagementContract.View {

  private View rootView;
  private MarginManagementContract.Presenter mMarginManagementPresenter;
  /**
   * 整个页面LinearLayout
   */
  private LinearLayout marginManageLinearLayout;
  /**
   * 左侧账户标题
   */
  private TextView leftAccountTitleText;
  /**
   * 左侧账户
   */
  private TextView leftAccountText;
  /**
   * 左侧币种
   */
  private TextView leftAccountCurrency;
  /**
   * 右侧账户标题
   */
  private TextView rightAccountTitleText;
  /***
   * 右侧账户
   */
  private TextView rightAccountText;
  /***
   * 右侧币种
   */
  private TextView rightAccountCurrency;
  /***
   * 其中包含账户的TextView和币种的TextView
   */
  private LinearLayout leftAccountLayout;
  private LinearLayout rightAccountLayout;
  /***
   * 资金账户和保证金账户转换按钮
   */
  private RelativeLayout capitalTransferMarginLayout;
  /***
   * 资金余额，保证金转出可用最大余额
   */
  private TextView balanceTitle;
  /***
   * 资金账户余额汇和钞
   */
  private TextView spotExchangeTextView;
  private TextView cashTextView;
  /***
   * 钞/汇选择
   */
  private EditChoiceWidget editChoiceWidget;
  /***
   * 输入金额组件
   */
  private EditMoneyInputWidget editMoneyInputWidget;
  /***
   * 存入转出金额
   */
  private BigDecimal mAmount;
  /***
   * 操作方式-资金账户转入保证金账户，还是保证金账户转出到资金账户
   */
  private Boolean modeTransfer = true;
  /***
   * 颜色变化
   */
  private ColorStateList color;
  /***
   * 初始化币种
   */
  private String initCurreny;
  /***
   * 保证金账号
   */
  private String marginAccount;
  /***
   * 资金账户
   */
  String capitalAccount;
  /***
   * 结算币种
   */
  private String settlementCurrency;
  /***
   * 保证金存入或转出按钮
   */
  private Button marginButton;
  /***
   * 资金账户各币种余额查询
   */
  List<PsnAccountQueryAccountDetailResult.AccountDetaiListBean> listAccountDetail;
  /***
   * 保证金账户列表
   */
  private List<VFGBailListQueryViewModel.BailItemEntity> listVFGBailListQueryViewModel =
      new ArrayList<>();
  /***
   * 保证金账户列表
   */
  private List<VFGBailListQueryViewModel.BailItemEntity> tempListVFGBailListQuery =
      new ArrayList<>();
  /***
   * 是否有钞
   */
  private boolean isHaveCash = false;
  /***
   * 是否有汇
   */
  private boolean isHaveSpot = false;

  // 资金账户有这个币种字段
  private boolean isHaveAccount;
  /***
   * 标记币种
   */
  private static String currencyCode = "";
  /***
   * 保证金钞的余额
   */
  private BigDecimal marginBalanceCash;
  /***
   * 保证金汇的余额
   */
  private BigDecimal marginBalanceSpotExchange;
  /***
   * 资金账户的accountId
   */
  private String accountId;
  /***
   * 资金账户的钞的余额
   */
  private BigDecimal balanceCash;
  /***
   * 资金账户的汇的余额
   */
  private BigDecimal balanceSpot;
  /**
   * 结算币种 钞、汇、人民币
   */
  private String cashRemit;
  /***
   * 资金账户
   */
  private String settleMarginAccount;
  /***
   * pageId 从那个页面进入
   */
  private Class<? extends BussFragment> pageClass;

  private Boolean isEnoughFund;
  //调试使用
  private String tempTransferAccount;
  private String tempTransferCurrency;

  /**
   * 其他页面跳入保证金存入转出入口
   */
  public static MarginManagementFragment newInstance(BussActivity bussActivity,
      Class<? extends BussFragment> fromWhere, String currency) {
    MarginManagementFragment fragment = bussActivity.findFragment(MarginManagementFragment.class);
    if (fragment == null) {
      fragment = new MarginManagementFragment();
      Bundle bundle = new Bundle();
      bundle.putString("currency", currency);
      bundle.putSerializable("pageClass", fromWhere);
      fragment.setArguments(bundle);
      bussActivity.start(fragment);
    } else {
      fragment = bussActivity.findFragment(MarginManagementFragment.class);
      Bundle bundle = new Bundle();
      bundle.putString("currency", currency);
      fragment.setArguments(bundle);
      bussActivity.popTo(MarginManagementFragment.class, false);
    }
    return fragment;
  }

  public static MarginManagementFragment newInstance(BussActivity bussActivity,
      Class<? extends BussFragment> fromWhere) {
    MarginManagementFragment fragment = bussActivity.findFragment(MarginManagementFragment.class);
    if (fragment == null) {
      fragment = new MarginManagementFragment();
      Bundle bundle = new Bundle();
      bundle.putSerializable("pageClass", fromWhere);
      fragment.setArguments(bundle);
      bussActivity.start(fragment);
    } else {
      fragment = bussActivity.findFragment(MarginManagementFragment.class);
      Bundle bundle = new Bundle();
      fragment.setArguments(bundle);
      bussActivity.popTo(MarginManagementFragment.class, false);
    }
    return fragment;
  }

  @Override protected View onCreateView(LayoutInflater mInflater) {
    rootView = mInflater.inflate(R.layout.boc_fragment_margin_management, null);
    return rootView;
  }

  @Override public void initView() {
    marginManageLinearLayout = (LinearLayout) rootView.findViewById(R.id.ll_margin_management);
    leftAccountTitleText = (TextView) rootView.findViewById(R.id.left_account_title);
    leftAccountText = (TextView) rootView.findViewById(R.id.left_account);
    rightAccountTitleText = (TextView) rootView.findViewById(R.id.right_account_title);
    rightAccountText = (TextView) rootView.findViewById(R.id.right_account);
    leftAccountCurrency = (TextView) rootView.findViewById(R.id.left_account_currency);
    rightAccountCurrency = (TextView) rootView.findViewById(R.id.right_account_currency);
    leftAccountLayout = (LinearLayout) rootView.findViewById(R.id.left_account_linearlayout);
    rightAccountLayout = (LinearLayout) rootView.findViewById(R.id.right_account_linearlayout);
    balanceTitle = (TextView) rootView.findViewById(R.id.tv_balance_account);
    capitalTransferMarginLayout =
        (RelativeLayout) rootView.findViewById(R.id.capital_tranfer_margin);
    cashTextView = (TextView) rootView.findViewById(R.id.tv_capital_cash);
    spotExchangeTextView = (TextView) rootView.findViewById(R.id.tv_capital_spot_exchange);
    editChoiceWidget = (EditChoiceWidget) rootView.findViewById(R.id.cash_or_exchange);
    editChoiceWidget.setChoiceTitleBold(true);
    editChoiceWidget.setBottomLineVisibility(true);
    editMoneyInputWidget = (EditMoneyInputWidget) rootView.findViewById(R.id.edit_money_input);
    editMoneyInputWidget.setTitleTextBold(true);
    marginButton = (Button) rootView.findViewById(R.id.margin_button);
  }

  @Override public void initData() {
    pageClass = (Class<? extends BussFragment>) getArguments().getSerializable("pageClass");
    if (BalanceEnquiryFragment.class.equals(pageClass) || MenuFragment.class.equals(pageClass)) {
      currencyCode = "";
    } else {
      currencyCode = getArguments().getString("currency");
    }
    //NumberUtils.formatCardNumberStrong(mAutTradEntitiy.getAccountNumber())   账户格式化
    marginManageLinearLayout.setVisibility(View.INVISIBLE);
    editMoneyInputWidget.setMaxLeftNumber(11);
    editMoneyInputWidget.setMaxRightNumber(2);
    editMoneyInputWidget.setScrollView(rootView);
    showLoadingDialog();
    /**
     * 获取交易账户
     * */
    XpadVFGGetBindAccountViewModel viewModel = new XpadVFGGetBindAccountViewModel();
    getPresenter().psnVFGGetBindAccount(viewModel);
  }

  @Override protected MarginManagementContract.Presenter initPresenter() {
    return new MarginManagementPresenter(this);
  }

  @Override public void setListener() {
    /**
     * 点击中间圆圈，保证金存入、转出切换
     */
    capitalTransferMarginLayout.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        color = leftAccountText.getTextColors();
        tempTransferAccount = leftAccountTitleText.getText().toString();
        leftAccountTitleText.setText(rightAccountTitleText.getText());
        rightAccountTitleText.setText(tempTransferAccount);
        leftAccountText.setTextColor(rightAccountText.getTextColors());

        tempTransferAccount = leftAccountText.getText().toString();
        leftAccountText.setText(rightAccountText.getText());
        rightAccountText.setText(tempTransferAccount);
        rightAccountText.setTextColor(color);

        tempTransferCurrency = leftAccountCurrency.getText().toString();
        leftAccountCurrency.setText(rightAccountCurrency.getText());
        rightAccountCurrency.setText(tempTransferCurrency);
        rightAccountCurrency.setTextColor(color);
        modeTransfer = !modeTransfer;
        if (modeTransfer) {
          marginButton.setText("保证金存入");
          balanceTitle.setText("资金账户余额：");
          if ("001".equals(currencyCode)) {
            isHaveCash = true;
            isHaveSpot = true;
            editChoiceWidget.setVisibility(View.GONE);
            spotExchangeTextView.setVisibility(View.GONE);
            for (int i = 0; i < listAccountDetail.size(); i++) {
              if (currencyCode.equals(listAccountDetail.get(i).getCurrencyCode())) {
                balanceCash = listAccountDetail.get(i).getBookBalance();
                cashTextView.setText(
                    "人民币元 " + MoneyUtils.transMoneyFormat(listAccountDetail.get(i).getBookBalance(),
                        currencyCode));
              }
            }
          } else {
            editChoiceWidget.setVisibility(View.VISIBLE);
            spotExchangeTextView.setVisibility(View.VISIBLE);
            editChoiceWidget.setChoiceTextContent("现汇");
            isHaveSpot = false;
            isHaveCash = false;
            for (int i = 0; i < listAccountDetail.size(); i++) {
              if (listAccountDetail.get(i).getCurrencyCode().equals(currencyCode)) {
                /**现汇余额*/
                if ("02".equals(listAccountDetail.get(i).getCashRemit())) {
                  isHaveSpot = true;
                  balanceSpot = listAccountDetail.get(i).getBookBalance();
                  spotExchangeTextView.setText(initCurreny + "/汇 " + MoneyUtils.transMoneyFormat(
                      listAccountDetail.get(i).getBookBalance(), currencyCode));
                  continue;
                }
                /**现钞余额*/
                if ("01".equals(listAccountDetail.get(i).getCashRemit())) {
                  isHaveCash = true;
                  balanceCash = listAccountDetail.get(i).getBookBalance();
                  cashTextView.setText(initCurreny + "/钞 " + MoneyUtils.transMoneyFormat(
                      listAccountDetail.get(i).getBookBalance(), currencyCode));
                  continue;
                }
              }
            }
            //                        if (!isHaveCash) {
            //                            System.out.print("没有现钞");
            //                            cashTextView.setText(initCurreny + "/钞 " + MoneyUtils.transMoneyFormat("0", currencyCode));
            //                        }
            //                        if (!isHaveSpot) {
            //                            cashTextView.setText(initCurreny + "/汇 " + MoneyUtils.transMoneyFormat("0", currencyCode));
            //                        }
          }
        } else {
          marginButton.setText("保证金转出");
          balanceTitle.setText("可转出最大金额：");
          VFGQueryMaxAmountViewModel vfgQueryMaxAmountViewModel = new VFGQueryMaxAmountViewModel();
          vfgQueryMaxAmountViewModel.setCurrencyCode(currencyCode);
          if ("001".equals(currencyCode)) {
            vfgQueryMaxAmountViewModel.setCashRemit("0");
          } else {
            vfgQueryMaxAmountViewModel.setCashRemit("1");
          }
          showLoadingDialog("加载中...");
          getPresenter().psnVFGQueryMaxAmount(vfgQueryMaxAmountViewModel);
        }
      }
    });

    //点击选择保证金账户
    rightAccountLayout.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (rightAccountTitleText.getText().toString().equals("保证金账户")) {
          SelectMarginAccountFragment selectMarginAccountFragment =
              new SelectMarginAccountFragment();
          selectMarginAccountFragment.setListVFGBailListQueryViewModel(
              listVFGBailListQueryViewModel);
          startForResult(selectMarginAccountFragment,
              SelectMarginAccountFragment.REQUEST_CODE_SELECT_ACCOUNT);
        } else {
          return;
        }
      }
    });

    leftAccountLayout.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (leftAccountTitleText.getText().toString().equals("保证金账户")) {
          SelectMarginAccountFragment selectMarginAccountFragment =
              new SelectMarginAccountFragment();
          selectMarginAccountFragment.setListVFGBailListQueryViewModel(
              listVFGBailListQueryViewModel);
          startForResult(selectMarginAccountFragment,
              SelectMarginAccountFragment.REQUEST_CODE_SELECT_ACCOUNT);
        } else {
          return;
        }
      }
    });

    /**
     * 选择现汇/现钞
     */
    editChoiceWidget.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        showCashRemitDialog();
      }
    });

    /**
     * 点击保证金存入或转出按钮
     */
    marginButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mAmount = editMoneyInputWidget.getMoney();
        String mAmountTemp = editMoneyInputWidget.getContentMoney();
        Bundle bundle = new Bundle();
        isEnoughFund = true;
        if (StringUtils.isEmptyOrNull(mAmountTemp)) {
          showErrorDialog("请输入金额");
          isEnoughFund = false;
        } else {
          int r = mAmount.compareTo(BigDecimal.ZERO);
          if (0 == r) {
            showErrorDialog("金额不能为 0");
            isEnoughFund = false;
          }
          settlementCurrency = editChoiceWidget.getChoiceTextContent();
          if ("001".equals(currencyCode)) {
            settlementCurrency = "人民币元";
            cashRemit = "0";
          }
          if (settlementCurrency.equals("现汇")) {
            cashRemit = "2";
          } else {
            cashRemit = "1";
          }

          bundle.putString("SETTLEMENT_CURRENCY", settlementCurrency);
          bundle.putString("MAMOUNT", mAmount.toString());
          bundle.putBoolean("MODE_TRANSFER", modeTransfer);
          bundle.putString("CURRENCY_CODE", currencyCode);
          bundle.putString("CASH_REMIT", cashRemit);
          bundle.putSerializable("pageClass", pageClass);
          bundle.putString("SETTLE_MARGIN_ACCOUNT", settleMarginAccount);

          if ("保证金账户".equals(rightAccountTitleText.getText().toString())) {
            if ("001".equals(currencyCode)) {
              if (1 == mAmount.compareTo(balanceCash)) {
                showErrorDialog("资金账户人民币余额不足");
                isEnoughFund = false;
              }
            } else {
              if ("1".equals(cashRemit)) {
                if (1 == mAmount.compareTo(balanceCash)) {
                  showErrorDialog("资金账户钞余额不足");
                  isEnoughFund = false;
                }
              }
              if ("2".equals(cashRemit)) {
                if (1 == mAmount.compareTo(balanceSpot)) {
                  showErrorDialog("资金账户汇余额不足");
                  isEnoughFund = false;
                }
              }
            }
          }
          if ("保证金账户".equals(leftAccountTitleText.getText().toString())) {
            if ("001".equals(currencyCode)) {
              if (1 == mAmount.compareTo(marginBalanceCash)) {
                showErrorDialog("保证金账户人民币余额不足");
                isEnoughFund = false;
              }
            } else {
              if ("1".equals(cashRemit)) {
                if (1 == mAmount.compareTo(marginBalanceCash)) {
                  showErrorDialog("保证金账户钞余额不足");
                  isEnoughFund = false;
                }
              }
              if ("2".equals(cashRemit)) {
                if (1 == mAmount.compareTo(marginBalanceSpotExchange)) {
                  showErrorDialog("保证金账户汇余额不足");
                  isEnoughFund = false;
                }
              }
            }
          }
        }
        if (isEnoughFund) {
          ConfirmInformationFragment confirmInformationFragment = new ConfirmInformationFragment();
          confirmInformationFragment.setArguments(bundle);
          start(confirmInformationFragment);
        }
      }
    });
  }

  @Override protected String getTitleValue() {
    return "保证金存入/转出";
  }

  @Override protected boolean getTitleBarRed() {
    return false;
  }

  @Override protected boolean isDisplayRightIcon() {
    return false;
  }

  @Override public void setPresenter(MarginManagementContract.Presenter presenter) {

  }

  /**
   * 交易账户查询接口返回成功
   */
  @Override public void psnVFGGetBindAccountSuccess(
      XpadVFGGetBindAccountViewModel xpadVFGGetBindAccountViewModel) {
    settleMarginAccount = xpadVFGGetBindAccountViewModel.getAccountNumber();
    accountId = xpadVFGGetBindAccountViewModel.getAccountId();
    leftAccountTitleText.setText("资金账户");
    leftAccountText.setText(NumberUtils.formatCardNumberStrong(
        xpadVFGGetBindAccountViewModel.getAccountNumber().toString()));
    capitalAccount = xpadVFGGetBindAccountViewModel.getAccountNumber().toString();
    Log.d("AccountId", "------>>>>>" + xpadVFGGetBindAccountViewModel.getAccountId());

    /**调用保证金账户*/
    VFGBailListQueryViewModel marginViewModel = new VFGBailListQueryViewModel();
    getPresenter().psnXpadBailListQuery(marginViewModel);
  }

  /**
   * 交易账户查询接口返回失败
   */
  @Override public void psnVFGGetBindAccountFail(BiiResultErrorException billResultErrorException) {
  }

  /**
   * 交易账户余额查询成功
   */
  @Override public void psnAccountQueryAccountSuccess(
      PsnAccountQueryAccountDetailResult psnAccountQueryAccountDetailResult) {
    closeProgressDialog();
    listAccountDetail = psnAccountQueryAccountDetailResult.getAccountDetaiList();
    marginManageLinearLayout.setVisibility(View.VISIBLE);

    if ("001".equals(currencyCode + "")) {
      isHaveCash = false;
      initCurreny = PublicCodeUtils.getCurrency(mActivity, currencyCode);
      for (int i = 0; i < listAccountDetail.size(); i++) {
        if (currencyCode.equals(listAccountDetail.get(i).getCurrencyCode() + "")) {
          balanceCash = listAccountDetail.get(i).getAvailableBalance();
          isHaveCash = true;
          break;
        }
      }
      cashTextView.setText(
          PublicCodeUtils.getCurrency(mActivity, currencyCode) + MoneyUtils.transMoneyFormat(
              balanceCash, currencyCode));
    } else {
      isHaveSpot = false;
      isHaveCash = false;
      initCurreny = PublicCodeUtils.getCurrency(mActivity, currencyCode);
      for (int i = 0; i < listAccountDetail.size(); i++) {
        if (currencyCode.equals(listAccountDetail.get(i).getCurrencyCode() + "")) {
          if ("01".equals(listAccountDetail.get(i).getCashRemit() + "")) {
            balanceCash = listAccountDetail.get(i).getAvailableBalance();
            isHaveCash = true;
            continue;
          }
          if ("02".equals(listAccountDetail.get(i).getCashRemit() + "")) {
            balanceSpot = listAccountDetail.get(i).getAvailableBalance();
            isHaveSpot = true;
            continue;
          }
        }
      }
      cashTextView.setText(PublicCodeUtils.getCurrency(mActivity, currencyCode) + "/钞 " + MoneyUtils
          .transMoneyFormat(balanceCash, currencyCode));
      spotExchangeTextView.setText(PublicCodeUtils.getCurrency(mActivity, currencyCode) + "/汇 "
          + MoneyUtils.transMoneyFormat(balanceSpot, currencyCode));
    }
    if (!isHaveCash) {
      balanceCash = new BigDecimal(0);
      cashTextView.setText(PublicCodeUtils.getCurrency(mActivity, currencyCode) + "/钞 " + MoneyUtils
          .transMoneyFormat("0", currencyCode));
    }
    if (!isHaveSpot) {
      balanceSpot = new BigDecimal(0);
      spotExchangeTextView.setText(PublicCodeUtils.getCurrency(mActivity, currencyCode) + "/汇 "
          + MoneyUtils.transMoneyFormat("0", currencyCode));
    }

    balanceTitle.setText("资金账户余额：");
  }

  /**
   * 保证金列表查询成功
   */
  @Override public void psnXpadBailListQuerySuccess(VFGBailListQueryViewModel viewModel) {

    /**筛选出本资金账户下的保证金账户*/
    tempListVFGBailListQuery = viewModel.getEntityList();
    System.out.println("capitalAccount----->>>>>" + capitalAccount);
    for (int i = 0; i < tempListVFGBailListQuery.size(); ) {
      System.out.println(
          "getAccountNumber()----->>>>>" + tempListVFGBailListQuery.get(i).getAccountNumber());
      if (!((capitalAccount + "").equals(
          tempListVFGBailListQuery.get(i).getAccountNumber() + ""))) {
        tempListVFGBailListQuery.remove(i);
        continue;
      }
      i++;
    }
    listVFGBailListQueryViewModel = tempListVFGBailListQuery;

    /**
     * 判断入口是否传入币种
     */
    if (StringUtils.isEmptyOrNull(currencyCode)) {
      for (int i = 0; i < listVFGBailListQueryViewModel.size(); i++) {
        if ("001".equals(listVFGBailListQueryViewModel.get(i).getSettleCurrency() + "")) {
          currencyCode = listVFGBailListQueryViewModel.get(i).getSettleCurrency();
          initCurreny = listVFGBailListQueryViewModel.get(i).getSettleCurrency();
          rightAccountText.setText(NumberUtils.formatCardNumberStrong(
              listVFGBailListQueryViewModel.get(i).getMarginAccountNo()));
          break;
        } else {
          currencyCode = listVFGBailListQueryViewModel.get(0).getSettleCurrency();
          initCurreny = listVFGBailListQueryViewModel.get(0).getSettleCurrency();
          rightAccountText.setText(NumberUtils.formatCardNumberStrong(
              listVFGBailListQueryViewModel.get(0).getMarginAccountNo()));
        }
      }
    } else {
      for (int i = 0; i < listVFGBailListQueryViewModel.size(); i++) {
        if (currencyCode.equals(listVFGBailListQueryViewModel.get(i).getSettleCurrency() + "")) {
          initCurreny = listVFGBailListQueryViewModel.get(i).getSettleCurrency();
          rightAccountText.setText(NumberUtils.formatCardNumberStrong(
              listVFGBailListQueryViewModel.get(i).getMarginAccountNo()));
          break;
        }
      }
    }
    if ("001".equals(currencyCode)) {
      editChoiceWidget.setVisibility(View.GONE);
      spotExchangeTextView.setVisibility(View.GONE);
    } else {
      editChoiceWidget.setChoiceTextContent("现汇");
      //editChoiceWidget.setChoiceTextContent(PublicCodeUtils.getCurrency(mActivity, "现钞"));
      editChoiceWidget.setVisibility(View.VISIBLE);
      spotExchangeTextView.setVisibility(View.VISIBLE);
    }
    rightAccountTitleText.setText("保证金账户");
    rightAccountCurrency.setText(PublicCodeUtils.getCurrency(mActivity, currencyCode));
    getPresenter().psnAccountQueryAccountDetail(accountId);
    System.out.println("tempListVFGBailListQuery的长度" + tempListVFGBailListQuery.size());
  }

  /**
   * 保证金列表查询失败
   */
  @Override public void psnXpadBailListQueryFail(BiiResultErrorException biiResultErrorException) {
  }

  /**
   * 保证金钞最大余额查询成功
   */
  @Override public void psnVFGQueryMaxCashAmountSuccess(VFGQueryMaxAmountViewModel viewModel) {
    if ("001".equals(viewModel.getCurrencyCode() + "")) {
      closeProgressDialog();
      marginBalanceCash = viewModel.getMaxBalance();
      cashTextView.setText(
          PublicCodeUtils.getCurrency(mContext, currencyCode) + MoneyUtils.transMoneyFormat(
              marginBalanceCash, currencyCode));
    } else {
      marginBalanceCash = viewModel.getMaxBalance();
      VFGQueryMaxAmountViewModel vfgQueryMaxAmountViewModel = new VFGQueryMaxAmountViewModel();
      vfgQueryMaxAmountViewModel.setCurrencyCode(currencyCode);
      vfgQueryMaxAmountViewModel.setCashRemit("2");
      getPresenter().psnVFGQueryMaxAmount(vfgQueryMaxAmountViewModel);
    }
  }

  /**
   * 保证金钞最大余额查询失败
   */
  @Override public void psnVFGQueryMaxCashAmountFail(
      BiiResultErrorException biiResultErrorException) {
    closeProgressDialog();
  }

  /**
   * 保证金汇最大余额查询成功
   */
  @Override public void psnVFGQueryMaxSpotAmountSuccess(VFGQueryMaxAmountViewModel viewModel) {
    closeProgressDialog();
    marginBalanceSpotExchange = viewModel.getMaxBalance();
    cashTextView.setText(
        PublicCodeUtils.getCurrency(mContext, currencyCode) + "/钞 " + MoneyUtils.transMoneyFormat(
            marginBalanceCash, currencyCode));
    spotExchangeTextView.setText(
        PublicCodeUtils.getCurrency(mContext, currencyCode) + "/汇 " + MoneyUtils.transMoneyFormat(
            marginBalanceSpotExchange, currencyCode));
  }

  /**
   * 保证金汇最大余额查询失败
   */
  @Override public void psnVFGQueryMaxSpotAmountFail(
      BiiResultErrorException biiResultErrorException) {
    closeProgressDialog();
  }

  /**
   * 弹窗选择【现/汇】弹窗
   */
  private void showCashRemitDialog() {
    final List<String> list = new ArrayList<>();
    list.add("现汇");
    list.add("现钞");
    SelectDialog selectListDialog = new SelectDialog(getContext());
    selectListDialog.setListener(new SelectDialog.OnItemSelectDialogClicked() {
      @Override public void onListItemClicked(int index) {
        editChoiceWidget.setChoiceTextContent(list.get(index).toString());
      }
    });
    selectListDialog.showDialog(list);
  }

  @Override protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
    super.onFragmentResult(requestCode, resultCode, data);
    if (SelectMarginAccountFragment.REQUEST_CODE_SELECT_ACCOUNT == requestCode) {
      if (resultCode == SelectMarginAccountFragment.RESULT_CODE_SELECT_ACCOUNT) {
        initCurreny = data.getString(SelectMarginAccountFragment.CURRENCY);
        marginAccount = data.getString(SelectMarginAccountFragment.CURRENCYACCOUNT);
        //去掉返回内容左右两边[ ]
        initCurreny = initCurreny.substring(1, (initCurreny.length() - 1));
        editMoneyInputWidget.getContentMoneyEditText().clearText();
        /**
         * 右侧为保证金账户
         */
        if ("保证金账户".equalsIgnoreCase(rightAccountTitleText.getText().toString())) {
          if ("人民币元".equals(initCurreny)) {
            currencyCode = "001";
            editChoiceWidget.setVisibility(View.GONE);
            spotExchangeTextView.setVisibility(View.GONE);
            for (int i = 0; i < listAccountDetail.size(); i++) {
              if (currencyCode.equals(listAccountDetail.get(i).getCurrencyCode() + "")) {
                balanceCash = listAccountDetail.get(i).getBookBalance();
                cashTextView.setText(
                    "人民币元 " + MoneyUtils.transMoneyFormat(listAccountDetail.get(i).getBookBalance(),
                        currencyCode));
              }
            }
          } else {
            editChoiceWidget.setVisibility(View.VISIBLE);
            spotExchangeTextView.setVisibility(View.VISIBLE);
            editChoiceWidget.setChoiceTextContent("现汇");
            isHaveSpot = false;
            isHaveCash = false;
            isHaveAccount = false;
            for (int i = 0; i < listAccountDetail.size(); i++) {
              if (initCurreny.equals(PublicCodeUtils.getCurrency(mContext,
                  listAccountDetail.get(i).getCurrencyCode() + ""))) {
                isHaveAccount = true;
                currencyCode = listAccountDetail.get(i).getCurrencyCode();
                /**现汇余额*/
                if ("02".equals(listAccountDetail.get(i).getCashRemit() + "")) {
                  isHaveSpot = true;
                  balanceSpot = listAccountDetail.get(i).getBookBalance();
                  spotExchangeTextView.setText(initCurreny + "/汇 " + MoneyUtils.transMoneyFormat(
                      listAccountDetail.get(i).getBookBalance(), currencyCode));
                  continue;
                }
                /**现钞余额*/
                if ("01".equals(listAccountDetail.get(i).getCashRemit() + "")) {
                  isHaveCash = true;
                  balanceCash = listAccountDetail.get(i).getBookBalance();
                  cashTextView.setText(initCurreny + "/钞 " + MoneyUtils.transMoneyFormat(
                      listAccountDetail.get(i).getBookBalance(),
                      listAccountDetail.get(i).getCurrencyCode()));
                  continue;
                }
              }
            }
            if (!isHaveAccount) {
              for (int i = 0; i < listVFGBailListQueryViewModel.size(); i++) {
                if (initCurreny.equals(PublicCodeUtils.getCurrency(mContext,
                    listVFGBailListQueryViewModel.get(i).getSettleCurrency() + ""))) {
                  isHaveAccount = true;
                  currencyCode = listVFGBailListQueryViewModel.get(i).getSettleCurrency();
                }
              }
            }
            if (!isHaveCash) {
              balanceCash = new BigDecimal(0);
              cashTextView.setText(
                  initCurreny + "/钞 " + MoneyUtils.transMoneyFormat("0", currencyCode));
            }
            if (!isHaveSpot) {
              balanceSpot = new BigDecimal(0);
              spotExchangeTextView.setText(
                  initCurreny + "/汇 " + MoneyUtils.transMoneyFormat("0", currencyCode));
            }
          }
          rightAccountCurrency.setText(initCurreny);
          rightAccountText.setText(NumberUtils.formatCardNumberStrong(marginAccount));
        }

        /**
         * 左侧为保证金账户
         */
        if ("保证金账户".equalsIgnoreCase(leftAccountTitleText.getText().toString() + "")) {
          if ("人民币元".equals(initCurreny)) {
            currencyCode = "001";
            editChoiceWidget.setVisibility(View.GONE);
            spotExchangeTextView.setVisibility(View.GONE);

            VFGQueryMaxAmountViewModel vfgQueryMaxAmountViewModel =
                new VFGQueryMaxAmountViewModel();
            vfgQueryMaxAmountViewModel.setCurrencyCode(currencyCode);
            vfgQueryMaxAmountViewModel.setCashRemit("0");
            showLoadingDialog("加载中...");
            getPresenter().psnVFGQueryMaxAmount(vfgQueryMaxAmountViewModel);
            cashTextView.setText(
                "人民币元 " + MoneyUtils.transMoneyFormat(marginBalanceCash, currencyCode));
          } else {
            editChoiceWidget.setVisibility(View.VISIBLE);
            spotExchangeTextView.setVisibility(View.VISIBLE);
            editChoiceWidget.setChoiceTextContent("现汇");
            isHaveSpot = false;
            isHaveCash = false;
            for (int i = 0; i < listVFGBailListQueryViewModel.size(); i++) {
              if ((PublicCodeUtils.getCurrency(mContext,
                  listVFGBailListQueryViewModel.get(i).getSettleCurrency())).equals(initCurreny)) {
                currencyCode = listVFGBailListQueryViewModel.get(i).getSettleCurrency();
                initCurreny = PublicCodeUtils.getCurrency(mContext,
                    listVFGBailListQueryViewModel.get(i).getSettleCurrency());
                break;
              }
            }
            /**
             * 现钞余额
             */
            VFGQueryMaxAmountViewModel vfgQueryMaxAmountViewModel =
                new VFGQueryMaxAmountViewModel();
            vfgQueryMaxAmountViewModel.setCurrencyCode(currencyCode);
            vfgQueryMaxAmountViewModel.setCashRemit("1");
            showLoadingDialog("加载中...");
            getPresenter().psnVFGQueryMaxAmount(vfgQueryMaxAmountViewModel);

            cashTextView.setText(PublicCodeUtils.getCurrency(mContext, currencyCode) + "/钞 "
                + MoneyUtils.transMoneyFormat(marginBalanceCash, currencyCode));
            spotExchangeTextView.setText(PublicCodeUtils.getCurrency(mContext, currencyCode) + "/汇 "
                + MoneyUtils.transMoneyFormat(marginBalanceSpotExchange, currencyCode));
            if (!isHaveCash) {
              marginBalanceCash = new BigDecimal(0);
              cashTextView.setText(
                  initCurreny + "/钞 " + MoneyUtils.transMoneyFormat(marginBalanceCash,
                      currencyCode));
            }
            if (!isHaveSpot) {
              marginBalanceSpotExchange = new BigDecimal(0);
              spotExchangeTextView.setText(
                  initCurreny + "/汇 " + MoneyUtils.transMoneyFormat(marginBalanceSpotExchange,
                      currencyCode));
            }
          }
          leftAccountCurrency.setText(initCurreny);
          leftAccountText.setText(NumberUtils.formatCardNumberStrong(marginAccount));
        }
      }
    }
    editMoneyInputWidget.setCurrency(currencyCode);
  }

  /**
   * 继续保证金交易
   */
  public void reInit() {
    editMoneyInputWidget.getContentMoneyEditText().clearText();
    if ("保证金账户".equals(rightAccountTitleText.getText().toString())) {
      showLoadingDialog();
      getPresenter().psnAccountQueryAccountDetail(accountId);
    } else {
      if ("001".equals(currencyCode)) {
        VFGQueryMaxAmountViewModel vfgQueryMaxAmountViewModel = new VFGQueryMaxAmountViewModel();
        vfgQueryMaxAmountViewModel.setCurrencyCode(currencyCode);
        vfgQueryMaxAmountViewModel.setCashRemit("0");
        showLoadingDialog("加载中...");
        getPresenter().psnVFGQueryMaxAmount(vfgQueryMaxAmountViewModel);
      } else {
        VFGQueryMaxAmountViewModel vfgQueryMaxAmountViewModel = new VFGQueryMaxAmountViewModel();
        vfgQueryMaxAmountViewModel.setCurrencyCode(currencyCode);
        vfgQueryMaxAmountViewModel.setCashRemit("1");
        showLoadingDialog("加载中...");
        getPresenter().psnVFGQueryMaxAmount(vfgQueryMaxAmountViewModel);
      }
    }
  }

  //    @Override
  //    protected void titleLeftIconClick() {
  //        super.titleLeftIconClick();
  //        if ("BalanceEnquiryFragment".equals(pageId)) {
  //            popToAndReInit(BalanceEnquiryFragment.class);
  //        }
  //    }
}