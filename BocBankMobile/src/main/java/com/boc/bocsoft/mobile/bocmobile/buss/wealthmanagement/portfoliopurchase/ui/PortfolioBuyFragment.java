package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.ui;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.pdf.PDFFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.assignment.SelectAgreementView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialog.MessageDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.moneyruler.MoneyRulerWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.webView.ContractFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.riskassess.ui.RiskAssessFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.model.PortfolioPurchaseModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.WealthConst;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

/**
 * 组合购买——购买页面
 * Created by wangtong on 2016/9/16.
 * 修改：谢端阳
 */
public class PortfolioBuyFragment extends MvpBussFragment<PortfolioPurchaseContact.Presenter>
        implements PortfolioPurchaseContact.BuyView, SelectAgreementView.OnClickContractListener {

    protected TextView btnNext;
    protected TextView portfolioAccount;
    protected MoneyRulerWidget moneyRuler;
    protected SelectAgreementView viewAgreement;
    private View rootView;

    private PortfolioPurchaseModel model;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_portfolio_buy, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        btnNext = (TextView) rootView.findViewById(R.id.btn_next);
        portfolioAccount = (TextView) rootView.findViewById(R.id.porfolio_account);
        moneyRuler = (MoneyRulerWidget) rootView.findViewById(R.id.money_ruler);
        viewAgreement = (SelectAgreementView) rootView.findViewById(R.id.view_agreement);
    }

    @Override
    public void initData() {
        super.initData();
        initUserLimit();
        model = findFragment(PortfolioPurchaseFragment.class).model;
        getPresenter().setBuyView(this);
        moneyRuler.initMoneyRuler((int) model.getMinStartAmount(),
                (int) Double.parseDouble(model.getBaseAmount()), model.getCurCode());
        moneyRuler.setMinTip("购买金额不得小于起点金额");
        //moneyRuler.setMaxTip("购买金额不得大于质押金额");
        moneyRuler.setMoneyLabel("组合购买金额");
        moneyRuler.setInitMoney(model.getSubAmount() + "");
        portfolioAccount.setText(NumberUtils.formatCardNumberStrong(model.getAccountNum()));
    }

    @Override
    public void setListener() {
        super.setListener();
        viewAgreement.setOnClickContractListener(this);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConditions()) {
                    getPresenter().psnXpadQueryRiskMatch();
                }
            }
        });
    }

    private void initUserLimit() {
        String limitString = getString(R.string.boc_portfolio_buy_user_limits);
        viewAgreement.setAgreement(limitString);
        //viewAgreement.setAgreement(limitString);
    }

    private boolean checkConditions() {
        boolean ret = false;
        model.setPayAmount(moneyRuler.getInputMoney());
        if (!viewAgreement.isSelected()) {
            showErrorDialog(getString(R.string.boc_portfolio_assignment_not_checked));
        } else {
            ret = true;
        }
        return ret;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected void titleLeftIconClick() {
        pop();
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return "购买信息";
    }

    @Override
    public void psnXpadQueryRiskMatchReturned() {
        if ("0".equals(model.getRiskMatch())) {
            model.setPayAmount(moneyRuler.getInputMoney());
            start(new PortfolioConfirmFragment());
        } else if ("1".equals(model.getRiskMatch())) {
            final MessageDialog dialog = new MessageDialog(getContext());
            dialog.setLeftButtonText("放弃");
            dialog.setRightButtonText("确认购买");
            dialog.setRightButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.setPayAmount(moneyRuler.getInputMoney());
                    start(new PortfolioConfirmFragment());
                    dialog.dismiss();
                }
            });
            dialog.showDialog(getString(R.string.boc_purchase_product_risk_warn_hint));
        } else {
            final MessageDialog dialog = new MessageDialog(getContext());
            dialog.setLeftButtonText("重新风险评估");
            String rightTitle;
            if (findFragment(WealthProductFragment.class) != null) {
                rightTitle = getString(R.string.boc_purchase_product_risk_other);
            } else {
                rightTitle = getString(R.string.boc_purchase_product_risk_other_close);
            }
            dialog.setRightButtonText(rightTitle);
            dialog.setLeftButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    start(new RiskAssessFragment(PortfolioBuyFragment.class));
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
            dialog.showDialog(getString(R.string.boc_purchase_product_risk_fail_hint));
        }
    }

    @Override
    protected PortfolioPurchaseContact.Presenter initPresenter() {
        return findFragment(PortfolioPurchaseFragment.class).getPresenter();
    }

    @Override
    public void onClickContract(int index) {
        switch (index) {
            case 0:
                start(ContractFragment.newInstance(
                        "file:///android_asset/webviewcontent/wealthmanagement/portfoliopurchase/notice/notice.html"));
                break;
            case 1:
                String url = WealthConst.URL_INSTRUCTION + model.getProdCode();
                start(PDFFragment.newInstance(Uri.parse(url)));
                break;
        }
    }
}
