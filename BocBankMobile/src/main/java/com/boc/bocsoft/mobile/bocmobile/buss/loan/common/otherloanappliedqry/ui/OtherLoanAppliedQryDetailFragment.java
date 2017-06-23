package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanappliedqry.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanappliedqry.OtherLoanAppliedQryConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanappliedqry.model.OtherLoanAppliedQryDetailViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanappliedqry.presenter.OtherLoanAppliedQryDetailPresenter;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;


/**
 * 其他类型贷款申请进度查询--用款详情页面
 * Created by liuzc on 2016/8/16.
 */
public class OtherLoanAppliedQryDetailFragment extends BussFragment implements OtherLoanAppliedQryDetailContract.View{
    protected View rootView;
    private ScrollView scrlviewContent;
    protected LinearLayout lytInfoCustomer;
    protected LinearLayout lytInfoLoan;
    protected LinearLayout lytInfoEnterprise;
    private TextView tvNodata; //无数据提示

    private OtherLoanAppliedQryDetailPresenter mPresenter = null;

    private String paramLoanNumber = null;  //贷款编号

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = View.inflate(mContext, R.layout.boc_fragment_loan_other_applied_qry_detail, null);
        return rootView;
    }

    @Override
    public void beforeInitView() {

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
    public void initView() {
        lytInfoCustomer = (LinearLayout) rootView.findViewById(R.id.lyt_info_customer);
        scrlviewContent = (ScrollView) rootView.findViewById(R.id.view_detail);
        lytInfoLoan = (LinearLayout) rootView.findViewById(R.id.lyt_info_loan);
        lytInfoEnterprise = (LinearLayout) rootView.findViewById(R.id.lyt_info_enterprise);
        tvNodata = (TextView)rootView.findViewById(R.id.tvNoData);
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_finance_account_transfer_detail_title);
    }

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        paramLoanNumber = bundle.getString(OtherLoanAppliedQryConst.KEY_LOAN_NUMBER);
        String conversationID = bundle.getString(OtherLoanAppliedQryConst.KEY_CONVERSATIONID);
        mPresenter = new OtherLoanAppliedQryDetailPresenter(this);
        mPresenter.setConverSationID(conversationID);

        showLoadingDialog();

        OtherLoanAppliedQryDetailViewModel model = new OtherLoanAppliedQryDetailViewModel();
        model.setLoanNumber(paramLoanNumber);
        mPresenter.queryOtherLoanOnlineDetail(model);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onDestroyView() {
        mPresenter.unsubscribe();
        super.onDestroyView();
    }

    @Override
    public void queryOtherLoanOnlineDetailSuccess(OtherLoanAppliedQryDetailViewModel viewModel) {
        closeProgressDialog();
        scrlviewContent.setVisibility(View.VISIBLE);

        //第一部分区域，贷款者信息
        addRowView(getResources().getString(R.string.boc_loan_type),
                viewModel.getProductName(), lytInfoCustomer);





        String nameAndSex = viewModel.getAppName();
        if(!StringUtils.isEmpty(viewModel.getAppSex())){
            nameAndSex = String.format("%s(%s)",
                    nameAndSex, OtherLoanAppliedQryConst.getSexDesc(getContext(),
                            viewModel.getAppSex()));
        }
        if(!StringUtils.isEmpty(viewModel.getAppName())){
            addRowView(getResources().getString(R.string.boc_loan_customer_name), nameAndSex, lytInfoCustomer);
        }

        if(isValidNumber(viewModel.getAppAge())){
            addRowView(getResources().getString(R.string.boc_loan_customer_age),
                    viewModel.getAppAge(), lytInfoCustomer);
        }

        addRowView(getResources().getString(R.string.boc_loan_phone),
                viewModel.getAppPhone(), lytInfoCustomer);
        addRowView(getResources().getString(R.string.boc_loan_enterprise_name),
                viewModel.getEntName(), lytInfoCustomer);
        addRowView(getResources().getString(R.string.boc_loan_enterprise_office_address),
                viewModel.getOfficeAddress(), lytInfoCustomer);
        addRowView(getResources().getString(R.string.boc_loan_enterprise_main_business),
                viewModel.getMainBusiness(), lytInfoCustomer);
        addRowView(getResources().getString(R.string.boc_loan_enterprise_principal_name),
                viewModel.getPrincipalName(), lytInfoCustomer);

        //第二部分区域，贷款信息
        String currency = viewModel.getCurrency();
        if(!StringUtils.isEmpty(currency)){
            addRowView(getResources().getString(R.string.boc_loan_currency),
                    PublicCodeUtils.getCurrency(mContext,
                            OtherLoanAppliedQryConst.getCurrencyCode(viewModel.getCurrency())),
                    lytInfoLoan);

            if(isValidNumber(viewModel.getHouseTradePrice())){
                addRowView(getResources().getString(R.string.boc_loan_house_trade_price),
                        MoneyUtils.transMoneyFormat(viewModel.getHouseTradePrice(),
                                OtherLoanAppliedQryConst.getCurrencyCode(currency)), lytInfoLoan);
            }
        }

        if(isValidNumber(viewModel.getHouseAge())){
            addRowView(getResources().getString(R.string.boc_loan_purchase_house_age),
                    viewModel.getHouseAge() + getString(R.string.boc_invest_treaty_period_year), lytInfoLoan);
        }

        if(!StringUtils.isEmpty(currency)){
            if(isValidNumber(viewModel.getTuitionTradePrice())){
                addRowView(getResources().getString(R.string.boc_loan_tuition_trade_price),
                        MoneyUtils.transMoneyFormat(viewModel.getTuitionTradePrice(),
                                OtherLoanAppliedQryConst.getCurrencyCode(currency)), lytInfoLoan);
            }

            if(isValidNumber(viewModel.getCarTradePrice())){
                addRowView(getResources().getString(R.string.boc_loan_car_price),
                        MoneyUtils.transMoneyFormat(viewModel.getCarTradePrice(),
                                OtherLoanAppliedQryConst.getCurrencyCode(currency)), lytInfoLoan);
            }

            if(isValidNumber(viewModel.getLoanAmount())){
                addRowView(getResources().getString(R.string.boc_loan_amount),
                        MoneyUtils.transMoneyFormat(viewModel.getLoanAmount(),
                                OtherLoanAppliedQryConst.getCurrencyCode(currency)), lytInfoLoan);
            }
        }

        addRowView(getResources().getString(R.string.boc_loan_term), getString(R.string.boc_pledge_info_period_month, viewModel.getLoanTerm()), lytInfoLoan);

        if(!StringUtils.isEmpty(viewModel.getGuaTypeFlag()) && viewModel.getGuaTypeFlag().equals("1")){
            addRowView(getResources().getString(R.string.boc_loan_gua_type_flag),
                    OtherLoanAppliedQryConst.getGuaTypeFlag(getContext(),
                    viewModel.getGuaTypeFlag()), lytInfoLoan);

            if(!StringUtils.isEmpty(viewModel.getGuaType())) {
                addRowView(getResources().getString(R.string.boc_loan_gua_type),
                        OtherLoanAppliedQryConst.getGuaTypeDesc(getContext(),
                                viewModel.getGuaType()), lytInfoLoan);
            }
        }

        if(!StringUtils.isEmptyOrNull(viewModel.getGuaWay()) && !viewModel.getGuaWay().equals("0")) {
            addRowView(getResources().getString(R.string.boc_loan_gua_way),
                    OtherLoanAppliedQryConst.getGuaWay(getContext(),
                            viewModel.getGuaWay()), lytInfoLoan);
        }


        //第三部分区域，其他信息，包括贷款状态、拒绝原因等
        addRowView(getResources().getString(R.string.boc_loan_branch_name),
                viewModel.getDeptName(), lytInfoEnterprise);
        addRowView(getResources().getString(R.string.boc_loan_branch_address),
                viewModel.getDeptAddr(), lytInfoEnterprise);
        if(!StringUtils.isEmpty(viewModel.getLoanStatus())) {
            addRowView(getResources().getString(R.string.boc_loan_status),
                    OtherLoanAppliedQryConst.getStatusDesc(getContext(),
                    viewModel.getLoanStatus()), lytInfoLoan);

            //如果贷款未成功处理，显示拒绝原因
            if(viewModel.getLoanStatus().trim().equals(OtherLoanAppliedQryConst.LOAN_STATUS_FAIL)){
                if(!StringUtils.isEmpty(viewModel.getRefuseReason())){
                    addRowView(getResources().getString(R.string.boc_loan_refuse_reason),
                            OtherLoanAppliedQryConst.getRefuseReasonDesc(getContext(),
                            viewModel.getRefuseReason()), lytInfoLoan);
                }
            }
        }
    }

    /**
     * 数字是否合法： 不为空、null、0
     * @param value
     * @return
     */
    private boolean isValidNumber(String value){
        try{
            if(StringUtils.isEmptyOrNull(value)){
                return false;
            }

            if(value.equals("0") || (new BigDecimal(value)).compareTo(new BigDecimal(0)) <= 0){
                return false;
            }

            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向lyt中添加一行, 若名称或者值为空，则不添加
     * @param title 名称
     * @param value 值
     * @param lyt
     */
    private void addRowView(String title, String value, LinearLayout lyt) {
        if(StringUtils.isEmpty(title) || StringUtils.isEmpty(value)){
            return;
        }
        View tempView = View.inflate(mContext, R.layout.boc_view_detail_row, null);

        TextView tvTitle = (TextView)tempView.findViewById(R.id.tv_name);
        tvTitle.setText(title);

        TextView tvValue = (TextView)tempView.findViewById(R.id.tv_value);
        tvValue.setText(value);

        lyt.addView(tempView);
    }

    @Override
    public void queryOtherLoanOnlineDetailFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showNodata();
    }

    private void showNodata(){
        tvNodata.setVisibility(View.VISIBLE);
        tvNodata.setText(getResources().getString(R.string.boc_loan_query_detail_fail));
        scrlviewContent.setVisibility(View.GONE);
    }

    @Override
    public void setPresenter(OtherLoanAppliedQryDetailContract.Presenter presenter) {

    }
}
