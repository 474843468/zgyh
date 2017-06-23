package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAccountListAndDetailQuery.PsnLOANAccountListAndDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepayAccountDetailQuery.PsnDrawingDetailResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.BaseDetailView;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.FacilityInquiryConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.model.FacilityInquiryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.presenter.FacilityUseRecordDetailPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import org.threeten.bp.format.DateTimeFormatter;

/**贷款使用记录详情
 * Created by liuzc on 2016/8/18.
 */
public class FacilityUseRecDetailFragment extends BussFragment implements FacilityUseRecDetailContact.View{
    /**
     * 根布局
     */
    private View rootView;
    private LinearLayout llyBackground = null;
    private BaseDetailView detailView;
    private Button btnUsedRecord = null; //额度使用记录按钮

    private PsnLOANAccountListAndDetailQueryResult.ListBean mDetailBean;
    private String sRepayOverFlag = "N"; //结清标识，Y：结清； N：非结清；空：全部

    private FacilityUseRecordDetailPresenter mPresenter = null;

    private String repayAccountNumber = null; //还款账户卡号
    private String repayPeriodCode = null; //还款周期code值

    public static FacilityUseRecDetailFragment newInstance(
            FacilityInquiryViewModel.FacilityInquiryBean bean) {
        FacilityUseRecDetailFragment fragment = new FacilityUseRecDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(FacilityInquiryConst.KEY_FACILITY_INQUIRY_BEAN, bean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.fragment_facility_detail, null);
        return rootView;
    }

    @Override
    public void initView() {
        llyBackground = (LinearLayout) rootView.findViewById(R.id.llyBackground);
        detailView = (BaseDetailView) rootView.findViewById(R.id.details_view);
        btnUsedRecord = (Button)rootView.findViewById(R.id.btn_used_record);
        btnUsedRecord.setVisibility(View.GONE);
        llyBackground.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        mPresenter = new FacilityUseRecordDetailPresenter(this);

        mDetailBean = (PsnLOANAccountListAndDetailQueryResult.ListBean)getArguments().getSerializable(FacilityInquiryConst.KEY_FACILITY_USE_REC_BEAN);
        sRepayOverFlag = getArguments().getString(FacilityInquiryConst.KEY_REPAYOVER_FLAG);

        String repayAccount = mDetailBean.getPayAccountNumber(); //还款账号

        //如果还款账号为空、null、0，则显示详情页，否则请求对应卡号后显示
        if(StringUtils.isEmptyOrNull(repayAccount) || repayAccount.equals("0")){
            updateViews();
        }
        else{
            showLoadingDialog();
            mPresenter.queryCardNum(repayAccount);
        }
    }

    private void updateViews(){
        llyBackground.setVisibility(View.VISIBLE);

        String currencyCode = mDetailBean.getCurrencyCode();
        //头部信息
        String headTitle = String.format(getString(R.string.boc_facility_loan_amount_title),
                new Object[] {
                        DataUtils.getCurrencyDescByLetter(mContext, currencyCode)
                });
        String amount =
                MoneyUtils.transMoneyFormat(mDetailBean.getLoanAmount().toPlainString(),
                        mDetailBean.getCurrencyCode());
        detailView.updateHeadData(headTitle, amount);

        //是否为未结清
        boolean bRepaying = (sRepayOverFlag != null && sRepayOverFlag.equals("N"));

        String periodAndRate = String.format("%s%s / %s%s",
                mDetailBean.getLoanPeriod(),
                getResources().getString(R.string.boc_period_unit),
                String.valueOf(mDetailBean.getLoanRate()),
                "%");

        if(mDetailBean.getLoanPeriod() > 0){
            periodAndRate = String.format("%s%s / %s%s",
                    mDetailBean.getLoanPeriod(),
                    getResources().getString(R.string.boc_period_unit),
                    String.valueOf(mDetailBean.getLoanRate()),
                    "%");
            detailView.updateHeadDetail(getString(R.string.boc_repaydetails_interest),
                    periodAndRate);
        }
        else{
            detailView.updateHeadDetail(getString(R.string.boc_details_interest),
                    periodAndRate);
        }

        //详细信息
        String loanType =
                PublicCodeUtils.getLoanTypeName(mContext, mDetailBean.getLoanType());
        if(!StringUtils.isEmptyOrNull(loanType)){
            detailView.addDetailRow(getString(R.string.boc_facility_loan_type), loanType);
        }

        if(bRepaying){
            detailView.addDetailRow(getResources().getString(R.string.boc_facility_this_issue_repay),
                    MoneyUtils.transMoneyFormat(mDetailBean.getThisIssueRepayAmount(), currencyCode
                    ));
            detailView.addDetailRow(getResources().getString(R.string.boc_details_repaydate),
                    mDetailBean.getThisIssueRepayDate());
        }

        if(!StringUtils.isEmptyOrNull(repayAccountNumber) && !repayAccountNumber.equals("0")){
            detailView.addDetailRow(getResources().getString(R.string.boc_eloan_repaymentAccount),
                    NumberUtils.formatCardNumber(repayAccountNumber));
        }

        if(bRepaying){
            detailView.addDetailRow(getResources().getString(R.string.boc_facility_repay_capital_remain),
                    MoneyUtils.transMoneyFormat(mDetailBean.getRemainCapital(), currencyCode));
            detailView.addDetailRow(getResources().getString(R.string.boc_facility_this_issue_repay_interest),
                    MoneyUtils.transMoneyFormat(mDetailBean.getThisIssueRepayInterest(), currencyCode
                    ));
        }

        if(!StringUtils.isEmptyOrNull(mDetailBean.getInterestType())){
            detailView.addDetailRow(getResources().getString(R.string.boc_pledge_info_repay_type),
                    DataUtils.getRepayTypeDesc(mContext, mDetailBean.getInterestType(), repayPeriodCode));
        }

        detailView.addDetailRow(getResources().getString(R.string.boc_facility_loan_period_unit),
                DataUtils.getLoanUnitPeriodUnitDesc(mContext,
                        mDetailBean.getLoanPeriodUnit()));

//        if(bRepaying){
//            detailView.addDetailRow(getResources().getString(R.string.boc_facility_overdue_issue),
//                    String.valueOf(mDetailBean.getOverdueIssue()));
//
//            detailView.addDetailRow(getResources().getString(R.string.boc_facility_loan_remain_issue),
//                    String.valueOf(mDetailBean.getRemainIssue()));
//        }
        detailView.addDetailRow(getResources().getString(R.string.boc_facility_loan_date),
                String.valueOf( mDetailBean.getLoanDate()));
        detailView.addDetailRow(getResources().getString(R.string.boc_details_loanDate),
                String.valueOf(mDetailBean.getLoanToDate()));
        detailView.addDetailRow(getResources().getString(R.string.boc_loan_number),
                NumberUtils.formatCardNumber(mDetailBean.getAccountNumber()));
    }

    @Override
    public void onDestroyView() {
        if(mPresenter != null){
            mPresenter.unsubscribe();
        }
        super.onDestroyView();
    }

    @Override
    public void setListener() {

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
        return getResources().getString(R.string.boc_transfer_details_title);
    }

    @Override
    public void queryCardNumSuccess(String cardNum) {
        repayAccountNumber = cardNum;

        mPresenter.queryDrawingDetail(mDetailBean.getAccountNumber());
    }

    @Override
    public void queryCardNumFailed() {
        closeProgressDialog();
        updateViews();
    }

    @Override
    public void eDrawingDetailFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        updateViews();
    }

    @Override
    public void eDrawingDetailSuccess(PsnDrawingDetailResult eloanDrawDetailResult) {
        if(eloanDrawDetailResult != null){
            repayPeriodCode = eloanDrawDetailResult.getLoanRepayPeriod();
        }

        closeProgressDialog();
        updateViews();
    }
}
