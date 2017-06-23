package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadAccountQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadDueProductProfitQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.DetailsRequestBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthDetailsFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

/**
 * Fragment：中银理财-交易查询-到期产品详情页面
 * Created by zhx on 2016/9/23
 */
public class ExpireProductDetailFragment extends BussFragment {
    private View rootView;
    private XpadDueProductProfitQueryViewModel.DueProductEntity dueProductEntity;
    private TextView tv_currency;
    private TextView tv_amount;
    private TextView tv_prod_name;
    private TextView tv_prod_id;
    private TextView tv_tran_payrate;
    private TextView tv_pay_profit;
    private TextView tv_proterm;
    private TextView tv_eDate;
    private TextView tv_accno;
    private XpadAccountQueryViewModel.XPadAccountEntity currentQueryAccount;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.fragment_wealth_expire_product_detail, null);
        return rootView;
    }

    @Override
    public void initView() {
        tv_currency = (TextView) rootView.findViewById(R.id.tv_currency);
        tv_amount = (TextView) rootView.findViewById(R.id.tv_amount);
        tv_prod_name = (TextView) rootView.findViewById(R.id.product_name);
        tv_prod_id = (TextView) rootView.findViewById(R.id.product_id);
        tv_pay_profit = (TextView) rootView.findViewById(R.id.tv_pay_profit);
        tv_tran_payrate = (TextView) rootView.findViewById(R.id.tv_tran_payrate);
        tv_proterm = (TextView) rootView.findViewById(R.id.tv_proterm);
        tv_eDate = (TextView) rootView.findViewById(R.id.tv_eDate);
        tv_accno = (TextView) rootView.findViewById(R.id.tv_accno);
    }

    @Override
    public void initData() {
        dueProductEntity = getArguments().getParcelable("dueProductEntity");
        currentQueryAccount = getArguments().getParcelable("currentQueryAccount");
        setDisplayData();
    }

    @Override
    public void setListener() {
        tv_prod_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开理财产品明细页面
                WealthDetailsFragment detailsFragment = new WealthDetailsFragment();
                Bundle bundle = new Bundle();
                DetailsRequestBean detailsRequestBean = new DetailsRequestBean();
                detailsRequestBean.setProdCode(dueProductEntity.getProId());
                detailsRequestBean.setProdKind(dueProductEntity.getKind());
                detailsRequestBean.setIbknum(currentQueryAccount.getIbkNumber());
                bundle.putBoolean(WealthDetailsFragment.OTHER, true);
                bundle.putParcelable(WealthDetailsFragment.DETAILS_REQUEST, detailsRequestBean);
                detailsFragment.setArguments(bundle);
                start(detailsFragment);
            }
        });
    }

    // 设置界面上展示的数据
    private void setDisplayData() {
        String currencyCode = PublicCodeUtils.getCurrency(getActivity(), dueProductEntity.getProcur());
        String cashRemit = "";
        //// 钞汇标识（01：钞 02：汇 00：人民币钞汇）
        if ("01".equals(dueProductEntity.getBuyMode())) {
            cashRemit = "/钞";
        } else if ("02".equals(dueProductEntity.getBuyMode())) {
            cashRemit = "/汇";
        } else if ("00".equals(dueProductEntity.getBuyMode())) {
            cashRemit = "";
        }

        if (currencyCode.equals("人民币元")) {
            currencyCode = "元";
        }
        tv_currency.setText("投资本金 (" + currencyCode + cashRemit + ")");
        tv_amount.setText(MoneyUtils.transMoneyFormat(dueProductEntity.getAmount(), dueProductEntity.getProcur())); // 投资本金
//        tv_prod_name.addTextAndButtonContent(getResources().getString(R.string.boc_wealth_product_name),dueProductEntity.getProname()," ("+dueProductEntity.getProId()+")"); // 产品名称
//        tv_prod_name.setRightTvListener(new DetailContentView.DetailContentRightTvOnClickListener() {
//            @Override
//            public void onClickRightTextView() {
//
//            }
//        });
        tv_prod_name.setText(dueProductEntity.getProname());
        tv_prod_id.setText("(" + dueProductEntity.getProId() + ")"); // 产品代码

        String payFlag = dueProductEntity.getPayFlag();
        if("0".equals(payFlag)) { // 0未付(未付，前端显示“结算中”)
            tv_pay_profit.setText("结算中"); // 实际收益
            tv_tran_payrate.setText("结算中"); // 实际年收益率
        } else if("1".equals(payFlag)) { // 1已付
            tv_pay_profit.setText(MoneyUtils.transMoneyFormat(dueProductEntity.getPayProfit(), dueProductEntity.getProcur())); // 实际收益
            tv_tran_payrate.setText(dueProductEntity.getPayRate() + " %"); // 实际年收益率
        } else if("2".equals(payFlag)) { // 2：非理财系统入账（前端实际收益和实际收益率显示为“-”）
            tv_pay_profit.setText("-"); // 实际收益
            tv_tran_payrate.setText("-"); // 实际年收益率
        }

        tv_proterm.setText(dueProductEntity.getProterm() + "天");
        tv_eDate.setText(dueProductEntity.getEDate().format(DateFormatters.dateFormatter1));
        tv_accno.setText(NumberUtils.formatCardNumberStrong(dueProductEntity.getAccno()));
    }

    @Override
    protected String getTitleValue() {
        return "明细";
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }
}
