package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.finance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.finance.FinancePledgeParamsData;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.finance.PledgeProductBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.finance.ProductsData;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.presenter.finance.PledgeLoanFinanceProductSelectPresenter;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import java.math.BigDecimal;

/**
 * 使用{@link PledgeLoanFinanceProductSelectFragment#newInstance}静态方法来创建该fragment的一个实例
 */
public class PledgeLoanFinanceProductSelectFragment
        extends MvpBussFragment<PledgeLoanFinanceProductSelectContract.Presenter>
        implements PledgeLoanFinanceProductSelectContract.View, AdapterView.OnItemClickListener {

    private static final String PARAM = "param";
    protected ListView lvProduct;
    private View rootView;
    private ProductsData mParam;

    private PledgeFinanceProductAdapter mAdapter;
    private BigDecimal mAvailableAmount;
    private PledgeProductBean mPledgeProductBean;

    /**
     * 使用该静态方法快速创建该fragment的一个实例，它接收了指定的参数
     *
     * @param param 参数
     * @return PledgeLoanFinanceProductSelectFragment的一个实例
     */
    public static PledgeLoanFinanceProductSelectFragment newInstance(ProductsData param) {
        PledgeLoanFinanceProductSelectFragment fragment =
                new PledgeLoanFinanceProductSelectFragment();
        Bundle args = new Bundle();
        args.putParcelable(PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView =
                mInflater.inflate(R.layout.boc_fragment_pledge_loan_finance_product_select, null);
        return rootView;
    }

    @Override
    public void initView() {
        mTitleBarView.setDividerBottomVisible(true);
        lvProduct = (ListView) rootView.findViewById(R.id.lv_product);
    }

    @Override
    public void initData() {
        if (getArguments() != null) {
            mParam = getArguments().getParcelable(PARAM);
            mAdapter = new PledgeFinanceProductAdapter(mContext);
            mAdapter.setDatas(mParam.getPledgeProductBeanList());
            lvProduct.setAdapter(mAdapter);
        }
    }

    @Override
    public void setListener() {
        lvProduct.setOnItemClickListener(this);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_loan_pledge_finance);
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
    protected PledgeLoanFinanceProductSelectContract.Presenter initPresenter() {
        return new PledgeLoanFinanceProductSelectPresenter(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //调用综合查询接口
        showLoadingDialog();
        mPledgeProductBean = mAdapter.getItem(position);
        getPresenter().setConversationId(mParam.getConversationId());
        getPresenter().qryFinancePledgeParameter(mPledgeProductBean);
    }

    @Override
    public void onQryFinancePledgeParamsDataSuccess(
            FinancePledgeParamsData financePledgeParamsData) {
        closeProgressDialog();
        // 如果1000元比单笔金额下线大，用1000元作比较，如果可贷金额小于1000，直接提示可贷金额不能小于1000元，反之则跳转
        // 如果1000比单笔金额下线小，用单笔金额下作比较，如果可贷金额小于单笔金额下线，直接提示可贷金额不能小于单笔金额下线
        BigDecimal singleQuotaMin;
        BigDecimal thousand = new BigDecimal(1000);
        mAvailableAmount = getAvailableAmount(financePledgeParamsData);
        if (StringUtils.isEmpty(financePledgeParamsData.getSingleQuotaMin()) || thousand.compareTo(
                singleQuotaMin = new BigDecimal(financePledgeParamsData.getSingleQuotaMin())) > 0) {
            financePledgeParamsData.setSingleQuotaMin("1000");
            if (mAvailableAmount.compareTo(thousand) < 0) {
                showErrorDialog(
                        getString(R.string.boc_pledge_finance_available_amount_too_little_1000));
                return;
            }
        } else if (mAvailableAmount.compareTo(singleQuotaMin) < 0) {
            showErrorDialog(String.format(
                    getString(R.string.boc_pledge_finance_available_amount_too_little),
                    MoneyUtils.transMoneyFormat(singleQuotaMin, ApplicationConst.CURRENCY_CNY)));
            return;
        }
        //进入填写信息页面
        start(PledgeLoanFinanceInfoFillFragment.newInstance(mAvailableAmount.toPlainString(),
                mPledgeProductBean, financePledgeParamsData));
    }

    /**
     * 贷款可用额度 、单笔限额上限、当日剩余可用金额中取最小值；
     * 选择产品币种为人民币贷款可用额度为：可用份额*质押率 ；
     * 如果选择的币种是外币，则贷款可用额度为：页面可用份额*质押率*汇率；//待定
     */
    protected BigDecimal getAvailableAmount(FinancePledgeParamsData financePledgeParamsData) {
        BigDecimal availableLoanAmount =
                new BigDecimal(mPledgeProductBean.getAvailableQuantity()).multiply(
                        MoneyUtils.transRate(mPledgeProductBean.getPledgeRate()));
        BigDecimal min = StringUtils.isEmpty(financePledgeParamsData.getSingleQuotaMax())
                ? availableLoanAmount : availableLoanAmount.min(
                new BigDecimal(financePledgeParamsData.getSingleQuotaMax()));
        min = StringUtils.isEmpty(financePledgeParamsData.getAvailableAmtToday()) ? min
                : min.min(new BigDecimal(financePledgeParamsData.getAvailableAmtToday()));
        return min;
    }
}