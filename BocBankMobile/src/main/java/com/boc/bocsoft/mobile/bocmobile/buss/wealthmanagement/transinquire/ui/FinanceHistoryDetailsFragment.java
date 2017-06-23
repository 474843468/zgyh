package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadTransInfoDetailQuery.PsnXpadTransInfoDetailQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRow;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.TransInfoDetailViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadAccountQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadHisTradStatusViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.presenter.FinanceHistoryDetailsPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.DetailsRequestBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthDetailsFragment;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

/**
 * * Fragment：中银理财-历史交易-交易详情
 * Created by zc on 2016/9/12
 */
public class FinanceHistoryDetailsFragment extends BussFragment implements FinanceHistoryDetailsContract.View {

    private View rootView;
    private FinanceHistoryDetailsContract.Presenter mFinanceHistoryDetailsPresenter;
    private XpadAccountQueryViewModel.XPadAccountEntity currentQueryAccount;

    private TextView tv_pay_currency;
    private TextView tv_pay_status;
    private TextView tv_pay_amount;

    private DetailTableRow fail_reasons;//失败原因
    private TextView product_names;//产品名称
    private TextView product_codes;
//    private DetailFinancialTabRowTextButton product_names= null;
    private DetailTableRow detail_types;//交易类型
    private DetailTableRow detail_values;//成交净值
    private DetailTableRow detail_sums;//成交份额
    private DetailTableRow detail_charges;//手续费用
    private DetailTableRow detail_rewards;//业绩报酬
    private DetailTableRow detail_entrust_dates;//委托日期
    private DetailTableRow detail_operate_dates;//交易日期.
    private DetailTableRow detail_success_dates;//成交日期
    private LinearLayout ll_head_layout;


    private String currencyCode;//币种
    private String cashRemit;

    private XpadHisTradStatusViewModel.XpadHisTradStatusResultEntity mXpadHisTradStatusResultEntity;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            FinanceHistoryDetailsFragment.this.pop();
        }
    };
//    private int mScreenWidth;
//    private int mMarginLeft;
//    private int mViewWith;
//    private int mMarginRight;
//    private int mMargin;
//    private int mReferViewWith;

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_finance_history_details_fail, null);
        super.getTitleValue();
        return rootView;
    }

    @Override
    public void initView() {

        tv_pay_currency = (TextView) rootView.findViewById(R.id.pay_currency);
        tv_pay_status = (TextView) rootView.findViewById(R.id.pay_state);
        tv_pay_amount = (TextView) rootView.findViewById(R.id.pay_amount);

        fail_reasons = (DetailTableRow) rootView.findViewById(R.id.fail_reason);
        product_names = (TextView) rootView.findViewById(R.id.product_name);
        product_codes = (TextView) rootView.findViewById(R.id.product_code);
        detail_types = (DetailTableRow) rootView.findViewById(R.id.detail_type);
        detail_values = (DetailTableRow) rootView.findViewById(R.id.detail_value);
        detail_sums = (DetailTableRow) rootView.findViewById(R.id.detail_sum);
        detail_charges = (DetailTableRow) rootView.findViewById(R.id.detail_charge);
        detail_rewards = (DetailTableRow) rootView.findViewById(R.id.detail_rewards);
        detail_entrust_dates = (DetailTableRow) rootView.findViewById(R.id.detail_entrust_date);
        detail_operate_dates = (DetailTableRow) rootView.findViewById(R.id.detail_operate_date);
        detail_success_dates = (DetailTableRow) rootView.findViewById(R.id.detail_success_date);
        ll_head_layout = (LinearLayout) rootView.findViewById(R.id.head_layout);


        tv_pay_currency.setVisibility(View.GONE);
        fail_reasons.setVisibility(View.GONE);
        product_names.setVisibility(View.GONE);
        detail_types.setVisibility(View.GONE);
        detail_values.setVisibility(View.GONE);
        detail_sums.setVisibility(View.GONE);
        detail_charges.setVisibility(View.GONE);
        detail_rewards.setVisibility(View.GONE);
        detail_entrust_dates.setVisibility(View.GONE);
        detail_operate_dates.setVisibility(View.GONE);
        detail_success_dates.setVisibility(View.GONE);
        ll_head_layout.setVisibility(View.GONE);

    }

    @Override
    public void initData() {
        mFinanceHistoryDetailsPresenter = new FinanceHistoryDetailsPresenter(this);
        mXpadHisTradStatusResultEntity = getArguments().getParcelable("xpadHisTradStatusResultEntity");
        currentQueryAccount = getArguments().getParcelable("currentQueryAccount");

        // 请求所有账户
        TransInfoDetailViewModel viewModel = new TransInfoDetailViewModel();
        viewModel.setAccountKey(getArguments().getString("amountKey"));
        viewModel.setTranSeq(mXpadHisTradStatusResultEntity.getTranSeq());
        mFinanceHistoryDetailsPresenter.psnPsnXpadTransInfoDetailQuery(viewModel);

//        // kkkkkkkkkkkkkkkkkkkkkkkkkk
//        mScreenWidth = mActivity.getWindowManager().getDefaultDisplay().getWidth();
//        mMarginLeft = (int) mActivity.getResources().getDimension(R.dimen.boc_space_between_30px);
//        mViewWith = (int) mActivity.getResources().getDimension(R.dimen.boc_space_between_160px);
//        mMarginRight = (int) mActivity.getResources().getDimension(R.dimen.boc_space_between_50px);
//        mMargin = (int) mActivity.getResources().getDimension(R.dimen.boc_space_between_42px);
//        mReferViewWith = mScreenWidth - mMarginLeft - mViewWith - mMarginRight - mMargin;

    }

    @Override
    public void setListener() {
        product_codes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WealthDetailsFragment detailsFragment = new WealthDetailsFragment();
                Bundle bundle = new Bundle();
                DetailsRequestBean detailsRequestBean = new DetailsRequestBean();
                detailsRequestBean.setProdCode(mXpadHisTradStatusResultEntity.getProdCode());
                detailsRequestBean.setProdKind(mXpadHisTradStatusResultEntity.getProductKind());
                detailsRequestBean.setIbknum(currentQueryAccount.getIbkNumber());
                bundle.putBoolean(WealthDetailsFragment.OTHER, true);
                bundle.putParcelable(WealthDetailsFragment.DETAILS_REQUEST, detailsRequestBean);
                detailsFragment.setArguments(bundle);
                start(detailsFragment);

            }
        });

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


    @Override
    public void onDestroy() {
        mFinanceHistoryDetailsPresenter.unsubscribe();
        super.onDestroy();
    }

    //成功回调，历史常规交易详情查询成功
    @Override
    public void psnPsnXpadTransInfoDetailQuerySuccess(PsnXpadTransInfoDetailQueryResult psnXpadTransInfoDetailQueryResult) {
        PsnXpadTransInfoDetailQueryResult model = psnXpadTransInfoDetailQueryResult;

        // 交易币种（001：人民币元 014：美元 012：英镑 013：港币 028: 加拿大元 029：澳元 038：欧元 027：日元）

        currencyCode = PublicCodeUtils.getCurrency(getActivity(), mXpadHisTradStatusResultEntity.getCurrencyCode());
        //// 钞汇标识（01：钞 02：汇 00：人民币钞汇）
        if ("01".equals(mXpadHisTradStatusResultEntity.getCashRemit())) {
            cashRemit = "/钞";
        } else if ("02".equals(mXpadHisTradStatusResultEntity.getCashRemit())) {
            cashRemit = "/汇";
        } else if ("00".equals(mXpadHisTradStatusResultEntity.getCashRemit())) {
            cashRemit = "";
        }

        if ("人民币元".equals(currencyCode)) {
            currencyCode = "元";
        }

        tv_pay_status.setText(getStatuss(mXpadHisTradStatusResultEntity.getStatus()));

        tv_pay_amount.setText(MoneyUtils.transMoneyFormat(mXpadHisTradStatusResultEntity.getAmount(), mXpadHisTradStatusResultEntity.getCurrencyCode()));

        product_names.setText(mXpadHisTradStatusResultEntity.getProdName());
        product_codes.setText("(" + mXpadHisTradStatusResultEntity.getProdCode() + ")");


        // 交易类型（00：认购 01：申购 02：赎回 03：红利再投 04：红利发放 05：（经过）利息返还 06：本金返还 07：起息前赎回 08：利息折份额 09:赎回亏损 10:赎回盈利 11:产品转让 12:份额转换）
        detail_types.updateValue(PublicCodeUtils.getTransferType(getActivity(), mXpadHisTradStatusResultEntity.getTrfType()));
        detail_sums.updateValue(MoneyUtils.transMoneyFormat(mXpadHisTradStatusResultEntity.getTrfAmount(), mXpadHisTradStatusResultEntity.getCurrencyCode()));

        //失败时候
        if ("2".equals(mXpadHisTradStatusResultEntity.getStatus())) {
            tv_pay_status.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.boc_transaction_status_bg_red));
            tv_pay_currency.setText("交易金额 (" + currencyCode + cashRemit + ")");
            fail_reasons.setVisibility(View.VISIBLE);
            product_names.setVisibility(View.VISIBLE);
            detail_types.setVisibility(View.VISIBLE);
            detail_sums.setVisibility(View.VISIBLE);
            detail_operate_dates.setVisibility(View.VISIBLE);
            fail_reasons.updateValue(model.getFailReason());
            detail_operate_dates.updateValue(mXpadHisTradStatusResultEntity.getPaymentDate().format(DateFormatters.dateFormatter1));
            if (mXpadHisTradStatusResultEntity.getFutureDate() != null){
                detail_entrust_dates.updateValue(mXpadHisTradStatusResultEntity.getFutureDate().format(DateFormatters.dateFormatter1));
                detail_entrust_dates.setVisibility(View.VISIBLE);
            }


        } else if ("1".equals(mXpadHisTradStatusResultEntity.getStatus())) {//成功
            //判断业绩基准型或者净值型产品
            tv_pay_currency.setText("成交金额 (" + currencyCode + cashRemit + ")");
            tv_pay_status.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.boc_transaction_status_bg_green));
            product_names.setVisibility(View.VISIBLE);
            detail_types.setVisibility(View.VISIBLE);
            detail_operate_dates.updateValue(mXpadHisTradStatusResultEntity.getPaymentDate().format(DateFormatters.dateFormatter1));
            detail_success_dates.updateValue(model.getConfirmDate().format(DateFormatters.dateFormatter1));
            if ("1".equals(mXpadHisTradStatusResultEntity.getProductKind())) {
                detail_values.setVisibility(View.VISIBLE);
                detail_sums.setVisibility(View.VISIBLE);
                detail_charges.setVisibility(View.VISIBLE);
                detail_rewards.setVisibility(View.VISIBLE);
                detail_operate_dates.setVisibility(View.VISIBLE);
                detail_success_dates.setVisibility(View.VISIBLE);

                detail_values.updateValue(MoneyUtils.transMoneyFormat(model.getTranNetVal(), mXpadHisTradStatusResultEntity.getCurrencyCode()));
                detail_charges.updateValue(MoneyUtils.transMoneyFormat(model.getHandlingCost(), mXpadHisTradStatusResultEntity.getCurrencyCode()));
                detail_rewards.updateValue(MoneyUtils.transMoneyFormat(model.getContrFee(), mXpadHisTradStatusResultEntity.getCurrencyCode()));
            } else if ("0".equals(mXpadHisTradStatusResultEntity.getProductKind())) {

                detail_values.setVisibility(View.VISIBLE);
                detail_sums.setVisibility(View.VISIBLE);
                detail_operate_dates.setVisibility(View.VISIBLE);
                detail_success_dates.setVisibility(View.VISIBLE);

                detail_values.updateData(getResources().getString(R.string.boc_wealth_real_reward_yield), model.getYearlyRR()+" %");
            }


        } else {
            tv_pay_currency.setText("交易金额 (" + currencyCode + cashRemit + ")");
            tv_pay_status.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.boc_transaction_status_bg_orange));
            product_names.setVisibility(View.VISIBLE);
            detail_types.setVisibility(View.VISIBLE);
            detail_operate_dates.updateValue(mXpadHisTradStatusResultEntity.getPaymentDate().format(DateFormatters.dateFormatter1));

            if (model.getConfirmDate() != null){
                detail_success_dates.updateValue(model.getConfirmDate().format(DateFormatters.dateFormatter1));
                detail_success_dates.setVisibility(View.VISIBLE);
            }

            if ("1".equals(mXpadHisTradStatusResultEntity.getProductKind())) {
                detail_values.setVisibility(View.VISIBLE);
                detail_sums.setVisibility(View.VISIBLE);
                detail_charges.setVisibility(View.VISIBLE);
                detail_rewards.setVisibility(View.VISIBLE);
                detail_operate_dates.setVisibility(View.VISIBLE);

                detail_values.updateValue(MoneyUtils.transMoneyFormat(model.getTranNetVal(), mXpadHisTradStatusResultEntity.getCurrencyCode()));
                detail_charges.updateValue(MoneyUtils.transMoneyFormat(model.getHandlingCost(), mXpadHisTradStatusResultEntity.getCurrencyCode()));
                detail_rewards.updateValue(MoneyUtils.transMoneyFormat(model.getContrFee(), mXpadHisTradStatusResultEntity.getCurrencyCode()));

            } else if ("0".equals(mXpadHisTradStatusResultEntity.getProductKind())) {
                detail_values.setVisibility(View.VISIBLE);
                detail_sums.setVisibility(View.VISIBLE);
                detail_operate_dates.setVisibility(View.VISIBLE);
                detail_values.updateData(getResources().getString(R.string.boc_wealth_real_reward_yield), model.getYearlyRR()+" %");
            }
        }

        tv_pay_currency.setVisibility(View.VISIBLE);
        ll_head_layout.setVisibility(View.VISIBLE);

    }

    //失败回调：历史常规交易详情查询失败
    @Override
    public void psnPsnXpadTransInfoDetailQueryFail(BiiResultErrorException biiResultErrorException) {

    }

    // 状态（0：委托待处理 1：成功 2：失败 3：已撤销 4：已冲正 5：已赎回）
    public String getStatuss(String string) {
        String statuss = "";
        if (string.equals("0")) {
            statuss = "委托待处理";
        } else if (string.equals("1")) {
            statuss = "成功";
        } else if (string.equals("2")) {
            statuss = "失败";
        } else if (string.equals("3")) {
            statuss = "已撤销";
        } else if (string.equals("4")) {
            statuss = "已冲正";
        } else if (string.equals("5")) {
            statuss = "已赎回";
        }
        return statuss;
    }

}
