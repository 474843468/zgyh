package com.boc.bocsoft.mobile.bocmobile.buss.loan.nonreloan.query.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepayAccountDetailQuery.PsnDrawingDetailResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.BaseDetailView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRow;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowButton;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.changeaccount.ui.ChangeAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.LoanCosnt;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.ui.RepayPlanFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.ui.PrepayFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanDrawDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.nonreloan.query.presenter.NonReloanQryPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.ReloanQryConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;

/**
 * 非循环贷款信息查询
 * Created by liuzc on 2016/8/20.
 */
public class NonReloanQryFragment extends BussFragment implements NonReloanQryContract.View{
    /**
     * 根布局
     */
    private View rootView;
    private LinearLayout llyBackground; ////页面背景

    protected BaseDetailView viewContent; //内容区
    protected Button btnPreRepay; //提前还款按钮
    private TextView tvNodata; //无数据提示


    private PsnLOANListEQueryResult.PsnLOANListEQueryBean mQryResultBean = null;
    private String currentDate = null;

    private String cardNumber = null;

    private String repayType = null; //还款方式

    private NonReloanQryContract.Presenter mPresenter = null;
    private String repayPeriodCode = null; //还款周期code值
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_loan_nonreloan_qry, null);
        return rootView;
    }

    @Override
    public void initView() {
        viewContent = (BaseDetailView) rootView.findViewById(R.id.viewContent);
        btnPreRepay = (Button) rootView.findViewById(R.id.btnPreRepay);
        llyBackground = (LinearLayout) rootView.findViewById(R.id.llyBackground);
        tvNodata = (TextView)rootView.findViewById(R.id.tvNoData);
    }

    /**
     * 字符串是否为空、null或者0
     * @param value
     * @return
     */
    private boolean isEmptyOrNullOrZero(String  value){
        return (StringUtils.isEmptyOrNull(value) || value.equals("0"));
    }

    @Override
    public void initData() {
        if(mQryResultBean == null){
//            mQryResultBean = genQryResult();
            mQryResultBean = (PsnLOANListEQueryResult.PsnLOANListEQueryBean)getArguments().getSerializable(LoanCosnt.LOAN_DATA);
            currentDate = getArguments().getString(LoanCosnt.LOAN_ENDDATE);
            cardNumber = getArguments().getString(LoanCosnt.LOAN_REPYNUM);
        }

        updateTitle();

        mPresenter = new NonReloanQryPresenter(this);
        showLoadingDialog();

        mPresenter.queryDrawingDetail(mQryResultBean.getAccountNumber());
    }

    //更新标题
    private void updateTitle(){
        String title = PublicCodeUtils.getLoanTypeName(mContext, mQryResultBean.getLoanType());
        if(!StringUtils.isEmptyOrNull(title) || !title.equals("-")){
            updateTitleValue(title);
        }
    }

    private void updateViews(){
        llyBackground.setVisibility(View.VISIBLE);

        updateTitle();

        //分隔线距离屏幕左端距离（与文字垂直对齐）
        int lineMarginLeft = (int)getResources().getDimension(R.dimen.boc_space_between_32px);

        String currencyCode =  mQryResultBean.getCurrencyCode();
        /**
         * 头部区域
         */
        viewContent.updateHeadData(getString(R.string.boc_facility_loan_amount_title,
                DataUtils.getCurrencyDescByLetter(mContext, currencyCode)),
                MoneyUtils.transMoneyFormat(mQryResultBean.getLoanCycleAdvVal(),currencyCode));

        //逾期标识
        boolean bIsOverdue = (mQryResultBean.getOverDueStat() != null &&
                mQryResultBean.getOverDueStat().equals("01"));
        if(bIsOverdue){
            viewContent.updateHeadStatus(getResources().getString(R.string.boc_loan_overdue),
                    getResources().getColor(R.color.boc_text_color_red));
        }

        if(StringUtils.isEmptyOrNull(mQryResultBean.getLoanCycleLifeTerm()) || mQryResultBean.getLoanCycleLifeTerm().equals("0")){
            if(!StringUtils.isEmptyOrNull(mQryResultBean.getLoanCycleRate())){
                String sIntAndRate = String.format("%s%s", mQryResultBean.getLoanCycleRate(), "%");

                viewContent.updateHeadDetail(getResources().getString(R.string.boc_details_interest), sIntAndRate);
            }
        }
        else{
            String sIntAndRate = String.format("%s/%s%s", getResources().getString(R.string.boc_loan_n_month, mQryResultBean.getLoanCycleLifeTerm()),
                     mQryResultBean.getLoanCycleRate(), "%");
            viewContent.updateHeadDetail(getResources().getString(R.string.boc_pledge_info_peroid_rate), sIntAndRate);
        }

        /**
         * 内容区域
         */
        String thisIssueRepayAmount = mQryResultBean.getThisIssueRepayAmount(); //本期应还
        //只有中银E贷的逾期才用getNopayamtAmount
//        if(isRecordOverdue()){
//            thisIssueRepayAmount = mQryResultBean.getNopayamtAmount();
//        }
        if(!StringUtils.isEmptyOrNull(thisIssueRepayAmount)){
            DetailTableRow viewRepay = genTabRow(getResources().getString(R.string.boc_facility_this_issue_repay),
                    MoneyUtils.transMoneyFormat(
                            thisIssueRepayAmount, currencyCode),
                    true, lineMarginLeft);
            viewRepay.setValueColor(R.color.boc_text_color_red);
            viewContent.addDetailRowView(viewRepay);
        }


        //一次还本付息，不显示还款计划
        if(!isEmptyOrNullOrZero(mQryResultBean.getThisIssueRepayDate())){
            if(mQryResultBean.getLoanCycleLifeTerm() == "1"){
                viewContent.addDetailRowView(genTabRow(getResources().getString(R.string.boc_details_date),
                        mQryResultBean.getThisIssueRepayDate(), true, lineMarginLeft));
            }
            else{
                DetailTableRowButton viewPayDate = new DetailTableRowButton(mContext);
                viewPayDate.addTextBtn(getResources().getString(R.string.boc_details_date),
                        mQryResultBean.getThisIssueRepayDate(), getResources().getString(R.string.boc_eloan_draw_repayment),
                        getResources().getColor(R.color.boc_main_button_color));
                viewPayDate.setOnclick(new DetailTableRowButton.BtnCallback(){
                    @Override
                    public void onClickListener() {
                        gotoRepayPlan();
                    }
                });
                viewPayDate.setDividerMargin(lineMarginLeft, 0);
                viewContent.addDetailRowView(viewPayDate);
            }
        }

        if(!isEmptyOrNullOrZero(cardNumber)){
            //是否可变更还款账户标识
            boolean bCanChangeRepayAccount = (cardNumber != null
                    && mQryResultBean.getPayAccountFlag() != null
                    && mQryResultBean.getPayAccountFlag().equals("0"));
            if(bCanChangeRepayAccount){
                DetailTableRowButton viewPayAccount = new DetailTableRowButton(mContext);
                viewPayAccount.addTextBtn(getResources().getString(R.string.boc_eloan_prepay_repaymentAccount), NumberUtils.formatCardNumber(
                        cardNumber), getResources().getString(R.string.security_change_verify),
                        getResources().getColor(R.color.boc_main_button_color));
                viewPayAccount.setOnclick(new DetailTableRowButton.BtnCallback(){
                    @Override
                    public void onClickListener() {
                        gotoChangeAccountFragment();
                    }
                });
                viewPayAccount.setDividerMargin(lineMarginLeft, 0);
                viewContent.addDetailRowView(viewPayAccount);
            }
            else{
                viewContent.addDetailRowView(genTabRow(getResources().getString(R.string.boc_eloan_prepay_repaymentAccount),  NumberUtils.formatCardNumber(
                        cardNumber), true, lineMarginLeft));
            }
        }

        repayType = DataUtils.getRepayTypeDesc(mContext, mQryResultBean.getInterestType(), repayPeriodCode);
        if(!StringUtils.isEmptyOrNull(repayType)){
            viewContent.addDetailRowView(genTabRow(getResources().getString(R.string.boc_details_repaytype), repayType, true, lineMarginLeft));
        }

        if(!StringUtils.isEmptyOrNull(mQryResultBean.getLoanDate()) && !StringUtils.isEmptyOrNull(mQryResultBean.getLoanCycleMatDate())){
            viewContent.addDetailRowView(genTabRow(getResources().getString(R.string.boc_loan_date_range),
                    String.format("%s~%s", mQryResultBean.getLoanDate(), mQryResultBean.getLoanCycleMatDate()),
                    true, lineMarginLeft));
        }

//        if(!StringUtils.isEmptyOrNull(mQryResultBean.getRemainIssue())){
//            viewContent.addDetailRowView(genTabRow(getResources().getString(R.string.boc_facility_remain_issue),
//                    mQryResultBean.getRemainIssue(), true, lineMarginLeft));
//        }

        if(!StringUtils.isEmptyOrNull(mQryResultBean.getRemainCapital())){
            viewContent.addDetailRowView(genTabRow(getResources().getString(R.string.boc_facility_repay_capital_remain),
                    MoneyUtils.transMoneyFormat(mQryResultBean.getRemainCapital(), mQryResultBean.getCurrencyCode()),
                    true, lineMarginLeft));
        }

        if(!isEmptyOrNullOrZero(mQryResultBean.getThisIssueRepayInterest())){
            viewContent.addDetailRowView(genTabRow(getResources().getString(R.string.boc_facility_issue_repay_interest),
                    MoneyUtils.transMoneyFormat(mQryResultBean.getThisIssueRepayInterest(), currencyCode),
                    true, lineMarginLeft));
        }

        if(!isEmptyOrNullOrZero(mQryResultBean.getAccountNumber())){
            viewContent.addDetailRowView(genTabRow(getResources().getString(R.string.boc_details_loanAccount),
                    NumberUtils.formatCardNumber(mQryResultBean.getAccountNumber()),
                    false, 0));
        }
    }

    /**
     * 是否可以进行提前还款
     * @return
     */
    private boolean canPrepay(){
        //逾期标识
        boolean bIsOverdue = (mQryResultBean.getOverDueStat() != null &&
                mQryResultBean.getOverDueStat().equals("01"));

        return (!bIsOverdue && (mQryResultBean.getTransFlag() != null &&
                mQryResultBean.getTransFlag().equals("advance") && isCurDateBeforeMatDate()));
    }

    private void showNodata(){
        tvNodata.setVisibility(View.VISIBLE);
        tvNodata.setText(getResources().getString(R.string.boc_loan_query_detail_fail));
        llyBackground.setVisibility(View.GONE);
    }

    /**
     * 判断当前日期是否早于到期日，是否可提前还款会用到
     * 到期日：052接口返回，loanCycleMatDate，如何理解需业务确认，开发人员尚不清楚
     * @return
     */
    private boolean isCurDateBeforeMatDate(){
        try{
            //当前日起
            LocalDate curDate = LocalDate.parse(currentDate, DateFormatters.dateFormatter1);
            //到期日
            LocalDate endDate = LocalDate.parse(mQryResultBean.getLoanCycleMatDate(), DateFormatters.dateFormatter1);
            //到期日早于当期日期
            if(endDate.isBefore(curDate)){
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return true;
        }
        return true;
    }

    private void gotoChangeAccountFragment(){
        ChangeAccountFragment fragement = new ChangeAccountFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(EloanConst.ELON_ACCOUNT, mQryResultBean);
        bundle.putString(LoanCosnt.LOAN_REPYNUM, cardNumber);
        fragement.setArguments(bundle);

        start(fragement);
    }

    /**
     * 进入还款计划
     */
    private void gotoRepayPlan(){
        RepayPlanFragment fragment = new RepayPlanFragment();
        Bundle bundle = new Bundle();
        EloanDrawDetailModel mDrawDetail = new EloanDrawDetailModel();
        mDrawDetail.setLoanAccount(mQryResultBean.getAccountNumber());
        mDrawDetail.setRemainIssue(Integer.parseInt(mQryResultBean.getRemainIssue()));
        mDrawDetail.setInterestType(mQryResultBean.getInterestType());
        mDrawDetail.setCurrency(mQryResultBean.getCurrencyCode());
        mDrawDetail.setLoanRepayPeriod("");

        //贷款状态： 40：结清
        String  loanAccountStatus = mQryResultBean.getLoanAccountStats();

        String pageIndex = "0"; //默认显示还款计划的剩余还款页
        if(mQryResultBean.getOverDueStat() != null && mQryResultBean.getOverDueStat().equals("01")){
            //已经逾期，进入逾期页
            pageIndex = "2";
        }
        else{
            if(loanAccountStatus.equals("40")){
                //已经结清，进入历史还款页
                pageIndex = "1";
            }
        }
        bundle.putSerializable(EloanConst.LOAN_DRAWDETA, mDrawDetail);
        bundle.putSerializable(EloanConst.DATA_TIME, currentDate);
        bundle.putSerializable(EloanConst.DEFAULT_PAGE_INDEX, pageIndex);
        bundle.putString(EloanConst.LOAN_REPAYTYPE, repayType);

        fragment.setArguments(bundle);
        start(fragment);
    }

    /**
     * 生成tabrow
     * @param title 标题
     * @param value 内容
     * @param bShowDivLine 是否显示分隔线
     * @param marginLeft 分隔线marginleft
     */
    private DetailTableRow genTabRow(String title, String value, boolean bShowDivLine,
                                     int marginLeft){
        DetailTableRow tabRow = new DetailTableRow(mContext);
        tabRow.updateData(title, value);
        if(bShowDivLine){
            tabRow.showDividerlineWithMargin(marginLeft, 0);
        }
        else{
            tabRow.isShowDividerLine(false);
            tabRow.setRowLineVisable(false);
        }

        return tabRow;
    }

    @Override
    public void setListener() {
        //提前还款
        btnPreRepay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!canPrepay()){
                    showErrorDialog(getString(R.string.boc_loan_not_support_prepay));
                    return;
                }
                else{
                    PrepayFragment prepayFragment = new PrepayFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(EloanConst.ELON_PREPAY, mQryResultBean);
                    bundle.putString(LoanCosnt.LOAN_REPYNUM, cardNumber);
                    prepayFragment.setArguments(bundle);
                    start(prepayFragment);
                }
            }
        });
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return true;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return true;
    }

    @Override
    protected void titleRightIconClick() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ReloanQryConst.KEY_STATUS_QRY_RESULT_BEAN, mQryResultBean);
        bundle.putString(LoanCosnt.LOAN_ENDDATE, currentDate);
        NonReloanMoreOptionFragment fragement = new NonReloanMoreOptionFragment();
        fragement.setArguments(bundle);
        start(fragement);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return "";
    }

    @Override
    public void eDrawingDetailFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showNodata();
    }

    @Override
    public void eDrawingDetailSuccess(PsnDrawingDetailResult eloanDrawDetailResult) {
        closeProgressDialog();

        if(eloanDrawDetailResult != null){
            repayPeriodCode = eloanDrawDetailResult.getLoanRepayPeriod();
        }

        updateViews();
    }

    @Override
    public void setPresenter(NonReloanQryContract.Presenter presenter) {

    }

    @Override
    public void onDestroyView() {
        if(mPresenter != null){
            mPresenter.unsubscribe();
        }
        super.onDestroyView();
    }

    /**
     * 记录是否逾期
     * @return
     */
    private boolean isRecordOverdue(){
        if (mQryResultBean.getOverDueStat() != null &&
                mQryResultBean.getOverDueStat().equals(ReloanQryConst.OVERDUE_STATUS_OVER)){
            return true;
        }

        return false;
    }
}
