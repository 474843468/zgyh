package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplancalc.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanRepaymentPlan.PsnLoanRepaymentPlanResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.LoanCosnt;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplancalc.model.RepayPlanCalcReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplancalc.model.RepayPlanCalcRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplancalc.presenter.RepayPlanCalcPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplancalc.ui.adapter.RepayPlanCalcAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplancalc.ui.adapter.RepayPlanCalcBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanQuoteViewModel;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * 还款计划试算
 * Created by liuzc on 2016/8/30.
 */
public class RepayPlanCalcFragment extends BussFragment implements RepayPlanCalcContract.View {
    private View mRootView = null;
    protected ListView lstContent; //listview

    private RepayPlanCalcPresenter mPresenter = null;

    private RepayPlanCalcAdapter listAdapter = null; //listview的adapter

    //上层页面传递数据
    private EloanQuoteViewModel mQryResultBean = null;

    private String useAmount = ""; //用款金额
    private String loanPeriod = ""; //用款期限
    private String loanAccount = ""; //收款账户
    private String payType = ""; //还款方式类型
    private String currencyCode = "001"; //币种，默认为001

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = View.inflate(mContext, R.layout.boc_fragment_loan_repay_plan_calc, null);
        return mRootView;
    }

    @Override
    public void beforeInitView() {

    }

    /**
     * 设置 用款金额
     * @param value
     */
    public void setUseAmount(String value){
        useAmount = value;
    }

    public void setCurrencyCode(String value){
        currencyCode = value;
    }

    /**
     * 用款期限
     * @param value
     */
    public void setLoanPeriod(String value){
        loanPeriod = value;
    }

    /**
     * 用款期限和还款方式类型
      * @param value
     * @param payType
     */
    public void setLoanPeriod(String value,String payType){
        loanPeriod = value;
       this.payType = payType;
    }
    /**
     * 贷款账号
     * @param value
     */
    public void setLoanAccount(String value){
        loanAccount = value;
    }

    /**
     * 设置试算还款计划的数据
     * @param mQryResultBean
     */
    public void setData(EloanQuoteViewModel mQryResultBean){
        this.mQryResultBean = mQryResultBean;
    }
    @Override
    protected boolean isHaveTitleBarView() {
        return true;
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
        return getResources().getString(R.string.boc_eloan_draw_repaymentPlan_calc);
    }

    @Override
    public void initView() {
        lstContent = (ListView) mRootView.findViewById(R.id.lstContent);
    }

    @Override
    public void initData() {
//        mQryResultBean = (PsnLOANListEQueryResult.PsnLOANListEQueryBean)getArguments().getSerializable(LoanCosnt.LOAN_DATA);
        mPresenter = new RepayPlanCalcPresenter(this);
        queryRepayPlanCalc();
    }

    //执行查询操作
    private void queryRepayPlanCalc() {
        showLoadingDialog();
        RepayPlanCalcReq model = new RepayPlanCalcReq();

        model.setActType("1160");
        model.setCat("1001");
        model.setAmount(new BigDecimal(useAmount));

//        LocalDate repayDate = LocalDate.parse(mQryResultBean.getIssueRepayDate(), DateFormatters.dateFormatter1);
//        model.setIssueRepayDate(repayDate.getDayOfMonth() + "");
        model.setIssueRepayDate(mQryResultBean.getIssueRepayDate());
        model.setLoanPeriod(loanPeriod);
        model.setLoanRate(mQryResultBean.getRate());
        model.setLoanRepayAccount(mQryResultBean.getRepayAcct());
        model.setPayType(payType);
        model.setNextRepayDate(mQryResultBean.getNextRepayDate());

//        model.setActType("1160");
//        model.setCat("1001");
//        model.setAmount(new BigDecimal(useAmount));
//        model.setIssueRepayDate("1");
//        model.setLoanPeriod("12");
//        model.setLoanRate("6.40");
//        model.setLoanRepayAccount("100798608679");
//        model.setPayType("2");
//        model.setNextRepayDate("0");

        mPresenter.queryRepayPlanCalc(model);
    }

    @Override
    public void onDestroyView() {
        mPresenter.unsubscribe();
        super.onDestroyView();
    }

    @Override
    public void setPresenter(RepayPlanCalcContract.Presenter presenter) {

    }

    @Override
    public void queryRepayPlanCalcSuccess(RepayPlanCalcRes result) {
        closeProgressDialog();
        onQuerySuccess(result);
    }

    @Override
    public void queryRepayPlanCalcFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        //// TODO: 2016/8/30 如何提示
    }

    /**
     * 返回成功的操作
     * @param result
     */
    private void onQuerySuccess(RepayPlanCalcRes result){
        List<PsnLoanRepaymentPlanResult.ListBean> resList = result.getList();
        
        int resListCount = 0;

        //初始化listview的头部
        View headerView = View.inflate(mContext, R.layout.boc_view_loan_repay_plan_calc_title, null);
        TextView tvTitle = (TextView)headerView.findViewById(R.id.tvTitle);
        
        if(resList != null){
            resListCount = resList.size();
        }

        tvTitle.setText(String.format("%s%s%s",
                getResources().getString(R.string.boc_total),
                resListCount,
                getResources().getString(R.string.boc_period_unit)));
        lstContent.addHeaderView(headerView);
        
        if(resListCount <= 0){
            return;
        }

        //listview条目
        List<RepayPlanCalcBean> adapterList = new LinkedList<>();
        for(int i = 0; i < resList.size(); i ++){
            RepayPlanCalcBean adapterBean = new RepayPlanCalcBean();
            PsnLoanRepaymentPlanResult.ListBean listBean = resList.get(i);

            adapterBean.setRepayDate(listBean.getRepayDate());
            adapterBean.setRepayAmount(String.format("%s%s%s",
                    getResources().getString(R.string.boc_loan_repay_shouldpay),
                    getString(R.string.boc_finance_account_transfer_recharge_max_money),
                    MoneyUtils.transMoneyFormat(listBean.getRemainAmount(), currencyCode)));
            adapterBean.setRepayCapital(String.format("%s%s",
                    getResources().getString(R.string.boc_repayment_Principal),
                    MoneyUtils.transMoneyFormat(listBean.getRemainCapital(), currencyCode)));
            adapterBean.setRepayInt(String.format("%s%s",
                    getResources().getString(R.string.boc_repayment_Interest),
                    MoneyUtils.transMoneyFormat(listBean.getRemainInterest(), currencyCode)));
            adapterList.add(adapterBean);
        }

        if(listAdapter == null){
            listAdapter = new RepayPlanCalcAdapter(mContext);
            lstContent.setAdapter(listAdapter);
        }
        listAdapter.setData(adapterList);
        listAdapter.notifyDataSetChanged();
    }
}
