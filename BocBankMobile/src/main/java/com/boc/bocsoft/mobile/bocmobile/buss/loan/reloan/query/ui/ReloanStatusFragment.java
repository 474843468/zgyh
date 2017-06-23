package com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepayAccountDetailQuery.PsnDrawingDetailResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanMinAmountQuery.PsnLOANCycleLoanMinAmountQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRow;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.waterwaveballview.WaterWaveBallView;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.changeaccount.ui.ChangeAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.draw.ui.LoanDrawApplyFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.LoanCosnt;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.ui.PrepayFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.ReloanQryConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.presenter.ReloanStatusPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import org.threeten.bp.LocalDate;

import java.math.BigDecimal;


/**
 * 循环类贷款信息页面
 * Created by liuzc on 2016/8/20.
 */
public class ReloanStatusFragment extends BussFragment implements ReloanStatusContract.View{
    protected View rootView;

    protected LinearLayout llyBackground; //页面背景
    protected WaterWaveBallView waveBllView;  //水球控件
    protected TextView tvAvailQuota; //可用额度
    protected TextView tvEndDate;  //到期日
    protected TextView tvAction;  //用款 或者 逾期
    protected LinearLayout llyMainInfo; //主要信息layout
    protected LinearLayout llyOtherInfo; //其他信息layout
    private TextView tvNodata; //无数据提示
    protected Button btnPreRepay; //提前还款按钮

    private ReloanStatusContract.Presenter mPresenter = null;

    private String repayPeriodCode = null; //还款周期code值

    //上层页面传递数据
    private PsnLOANListEQueryResult.PsnLOANListEQueryBean mQryResultBean = null;
    private String currentDate = null;
    private String cardNumber = null;
    //客户是否逾期，从074接口获取； 052接口的逾期为该笔记录是否逾期
    private String customerOverdueState = null;

    private String minUseMoneyAmount = null; //最小放款金额

    private String repayType = null; //还款方式
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = View.inflate(mContext, R.layout.boc_fragment_loan_reloan_status, null);
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
        return true;
    }

    @Override
    protected void titleRightIconClick() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ReloanQryConst.KEY_STATUS_QRY_RESULT_BEAN, mQryResultBean);
        bundle.putString(LoanCosnt.LOAN_ENDDATE, currentDate);
        bundle.putString(LoanCosnt.LOAN_REPYNUM, cardNumber);
        bundle.putString(LoanCosnt.LOAN_OVERDUE, customerOverdueState);
        bundle.putString(EloanConst.LOAN_REPAYTYPE, repayType);
        ReloanMoreOptionFragment fragement = new ReloanMoreOptionFragment();
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
    public void initView() {
        llyBackground = (LinearLayout) rootView.findViewById(R.id.llyBackground);
        waveBllView = (WaterWaveBallView) rootView.findViewById(R.id.waveBllView);
        tvAvailQuota = (TextView) rootView.findViewById(R.id.tvAvailQuota);
        btnPreRepay = (Button) rootView.findViewById(R.id.btnPreRepay);
        tvEndDate = (TextView) rootView.findViewById(R.id.tvEndDate);
        tvAction = (TextView) rootView.findViewById(R.id.tvAction);
        llyMainInfo = (LinearLayout) rootView.findViewById(R.id.lly_main_info);
        llyOtherInfo = (LinearLayout) rootView.findViewById(R.id.lly_other_info);
        tvNodata = (TextView)rootView.findViewById(R.id.tvNoData);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(waveBllView != null){
            waveBllView.startWave();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(waveBllView != null){
            waveBllView.stopWave();
        }
    }

    /**
     * 获取可用额度占总额度的比例
     * @param usedAmount
     * @param totalAmount
     * @return
     */
    private float getUsedAmountPercent(String usedAmount, String totalAmount){
        float result = 0;
        try{
            BigDecimal bdusedAmount = new BigDecimal(MoneyUtils.getNormalMoneyFormat(usedAmount));
            BigDecimal bdTotalAmount = new BigDecimal(MoneyUtils.getNormalMoneyFormat(totalAmount));
            if(bdTotalAmount.compareTo(new BigDecimal(0)) > 0){
                //bigdecimal除法运算要保留指定小数位数，否则计算结果出现无限循环会抛出异常
                return bdusedAmount.divide(bdTotalAmount, 2, BigDecimal.ROUND_HALF_UP).floatValue();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void initData() {
        if(mQryResultBean == null){
            mQryResultBean = (PsnLOANListEQueryResult.PsnLOANListEQueryBean)getArguments().getSerializable(LoanCosnt.LOAN_DATA);
            currentDate = getArguments().getString(LoanCosnt.LOAN_ENDDATE);
            cardNumber = getArguments().getString(LoanCosnt.LOAN_REPYNUM);
            customerOverdueState = getArguments().getString(LoanCosnt.LOAN_OVERDUE);
        }

        //更新标题
        String title = PublicCodeUtils.getLoanTypeName(mContext, mQryResultBean.getLoanType());
        if(!StringUtils.isEmptyOrNull(title) || !title.equals("-")){
            updateTitleValue(title);
        }

        mPresenter = new ReloanStatusPresenter(this);

        showLoadingDialog();
        mPresenter.queryDrawingDetail(mQryResultBean.getAccountNumber());
    }

    /**
     * 字符串是否为空、null或者0
     * @param value
     * @return
     */
    private boolean isEmptyOrNullOrZero(String  value){
        return (StringUtils.isEmptyOrNull(value) || value.equals("0"));
    }

    private void updateViews(){
        llyBackground.setVisibility(View.VISIBLE);
        //分隔线距离屏幕左端距离（与文字垂直对齐）
        int lineMarginLeft = (int)getResources().getDimension(R.dimen.boc_space_between_32px);

        String currency = mQryResultBean.getCurrencyCode();
        /**
         * 水球控件部分信息修改
         */
        tvAvailQuota.setText(MoneyUtils.getLoanAmountShownRMB(mQryResultBean.getLoanCycleAvaAmount()));

        if(!isEmptyOrNullOrZero(mQryResultBean.getLoanCycleMatDate())){
            tvEndDate.setText(String.format("%s%s", mQryResultBean.getLoanCycleMatDate(),
                    getResources().getString(R.string.boc_end_date)));
        }
        else{
            tvEndDate.setVisibility(View.GONE);
        }

        if(!isValidUseMoneyDate()){
            tvAction.setText(getResources().getString(R.string.boc_quote_loss));
            tvAction.setClickable(false);
        }
        else  if(isRecordOverdue() || isCustomerOverdue()){//逾期,记录逾期与用户逾期都显示“已逾期”
            tvAction.setText(getResources().getString(R.string.boc_overduehit));
            tvAction.setClickable(false);
        }
        else if(canUseMoney()){ //我要用款
            tvAction.setText(getResources().getString(R.string.boc_eloan_submitEloanTv));
            tvAction.setBackgroundResource(R.drawable.boc_eloan_but);
            // tvAction.setAlpha(0.7f);
            tvAction.setClickable(true);
        }
        else{//不显示“我要用款”
            tvAction.setVisibility(View.GONE);
        }

        //计算水球百分比
        float fUsedPercent = getUsedAmountPercent(mQryResultBean.getLoanCycleAvaAmount(),
                mQryResultBean.getLoanCycleAppAmount());
        waveBllView.setFinallyWaterHeight(fUsedPercent);
        waveBllView.startWave();

        Boolean bHasUsedMoney = hasUsedMoney();

        /**
         * 内容区信息设定
         */
        String thisIssueRepayAmount = mQryResultBean.getThisIssueRepayAmount(); //本期应还
        //只有中银E贷的逾期才用getNopayamtAmount
//        if(isRecordOverdue()){
//            thisIssueRepayAmount = mQryResultBean.getNopayamtAmount();
//        }

        if(!StringUtils.isEmptyOrNull((thisIssueRepayAmount)) && bHasUsedMoney){
            DetailTableRow viewRow = genTabRow(getResources().getString(R.string.boc_facility_this_issue_repay),
                    MoneyUtils.transMoneyFormat(thisIssueRepayAmount, currency),
                    true, lineMarginLeft);
            viewRow.setValueColor(R.color.boc_text_color_red);
            llyMainInfo.addView(viewRow);
        }

        if(!isEmptyOrNullOrZero(mQryResultBean.getThisIssueRepayDate())){
            llyMainInfo.addView(genTabRow(getResources().getString(R.string.boc_details_date), mQryResultBean.getThisIssueRepayDate(), false, 0));
        }


        if(!StringUtils.isEmptyOrNull(mQryResultBean.getLoanCycleAppAmount())){
            llyOtherInfo.addView(genTabRow(getResources().getString(R.string.boc_loan_quota), MoneyUtils.transMoneyFormat(
                    mQryResultBean.getLoanCycleAppAmount(), currency) , true, lineMarginLeft));
        }

        if(bHasUsedMoney){
            if(StringUtils.isEmptyOrNull(mQryResultBean.getLoanCycleLifeTerm()) || mQryResultBean.getLoanCycleLifeTerm().equals("0")){
                if(!StringUtils.isEmptyOrNull(mQryResultBean.getLoanCycleRate())){
                    String sIntAndRate = String.format("%s%s", mQryResultBean.getLoanCycleRate(), "%");
                    llyOtherInfo.addView(genTabRow(getResources().getString(R.string.boc_details_interest),
                            sIntAndRate, true, lineMarginLeft));
                }
            }
            else{
                String sIntAndRate = String.format("%s/%s%s", getResources().getString(R.string.boc_loan_n_month, mQryResultBean.getLoanCycleLifeTerm()),
                         mQryResultBean.getLoanCycleRate(), "%");
                llyOtherInfo.addView(genTabRow(getResources().getString(R.string.boc_pledge_info_peroid_rate),
                        sIntAndRate, true, lineMarginLeft));
            }
        }

        if(!isEmptyOrNullOrZero(cardNumber)){
            //是否可变更还款账户标识
            boolean bCanChangeRepayAccount = (cardNumber != null
                    && mQryResultBean.getPayAccountFlag() != null
                    && mQryResultBean.getPayAccountFlag().equals("0"));
            if(bCanChangeRepayAccount){
                DetailTableRowButton viewPayAccount = new DetailTableRowButton(mContext);
                viewPayAccount.addTextBtn(getResources().getString(R.string.boc_details_repayaccount), NumberUtils.formatCardNumber(
                        cardNumber), getResources().getString(R.string.boc_details_update),
                        getResources().getColor(R.color.boc_main_button_color));
                viewPayAccount.setOnclick(new DetailTableRowButton.BtnCallback(){
                    @Override
                    public void onClickListener() {
                        gotoChangeAccountFragment();
                    }
                });
                viewPayAccount.setDividerMargin(lineMarginLeft, 0);
                llyOtherInfo.addView(viewPayAccount);
            }
            else{
                llyOtherInfo.addView(genTabRow(getResources().getString(R.string.boc_details_repayaccount), NumberUtils.formatCardNumber(
                        cardNumber), true, lineMarginLeft));
            }
        }


        repayType =  DataUtils.getRepayTypeDesc(mContext,
                mQryResultBean.getInterestType(), repayPeriodCode);
        if(!StringUtils.isEmptyOrNull(repayType)){
            llyOtherInfo.addView(genTabRow(getResources().getString(R.string.boc_details_repaytype), repayType, true, lineMarginLeft));
        }

        if(!StringUtils.isEmptyOrNull(repayPeriodCode)){
            llyOtherInfo.addView(genTabRow(getResources().getString(R.string.boc_facility_loan_period_unit)
                    , DataUtils.getLoanRepayPeriodDesc(mContext, repayPeriodCode), true, lineMarginLeft));
        }

        if(!isEmptyOrNullOrZero(mQryResultBean.getLoanDate()) && !isEmptyOrNullOrZero( mQryResultBean.getLoanCycleMatDate())){
            llyOtherInfo.addView(genTabRow(getResources().getString(R.string.boc_loan_date_range),
                    String.format("%s~%s", mQryResultBean.getLoanDate(), mQryResultBean.getLoanCycleMatDate()),
                    true, lineMarginLeft));
        }

//        if(!StringUtils.isEmptyOrNull(mQryResultBean.getRemainIssue()) && bHasUsedMoney){
//            llyOtherInfo.addView(genTabRow(getResources().getString(R.string.boc_facility_remain_issue)
//                    , mQryResultBean.getRemainIssue(), true, lineMarginLeft));
//        }


        if(!StringUtils.isEmptyOrNull(mQryResultBean.getThisIssueRepayInterest()) && bHasUsedMoney){
            llyOtherInfo.addView(genTabRow(getResources().getString(R.string.boc_facility_issue_repay_interest),
                    MoneyUtils.transMoneyFormat(mQryResultBean.getThisIssueRepayInterest(), currency),
                    true, lineMarginLeft));
        }

        if(!isEmptyOrNullOrZero(mQryResultBean.getAccountNumber())){
            llyOtherInfo.addView(genTabRow(getResources().getString(R.string.boc_loan_number),
                    NumberUtils.formatCardNumber(mQryResultBean.getAccountNumber()),
                    false, 0));
        }
    }

    /**
     * 是否用过款，因后台没有是否用款的字段标识，业务让尝试用利率是否为0来判断，
     * @return
     */
    private boolean hasUsedMoney(){
        String rate = mQryResultBean.getLoanCycleRate();
        try{
            if(StringUtils.isEmptyOrNull(rate)){
                return false;
            }

            if(Double.parseDouble(rate) <= 0){
                return false;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
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
    public void onDestroy() {
        waveBllView.stopWave();
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        mPresenter.unsubscribe();
        super.onDestroyView();
    }

    @Override
    public void setListener() {
        tvAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!canUseMoney()){
                    return;
                }

                if(!StringUtils.isEmptyOrNull(minUseMoneyAmount)){
                    //已经成功获取过最小放款金额，则直接判断可用额度是否合法，是则直接跳转
                    doAvalQuotAmountCheck();
                }
                else{
                    //先获取最小放款金额，之后再判断可用额度是否合法
                    showLoadingDialog();

                    PsnLOANCycleLoanMinAmountQueryParams params = new PsnLOANCycleLoanMinAmountQueryParams();
                    mPresenter.queryLoanCycleMinAmount(params);
                }
            }
        });

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

    /**
     * 进入用款页
     */
    private void gotoDrawApplyPage(){
        Bundle bundle = new Bundle();
        bundle.putSerializable(EloanConst.ELON_DRAW, mQryResultBean);
        bundle.putString(EloanConst.LOAN_REPAY_PERIOD, repayPeriodCode);
        bundle.putString(LoanCosnt.LOAN_REPYNUM, cardNumber);
        bundle.putString(ReloanQryConst.KEY_MIN_USEMONEY_AMOUNT, minUseMoneyAmount);
        LoanDrawApplyFragment fragement = new LoanDrawApplyFragment();
        fragement.setArguments(bundle);
        start(fragement);
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

    /**
     * 是否有客户逾期
     * @return
     */
    private boolean isCustomerOverdue(){
        return (!StringUtils.isEmptyOrNull(customerOverdueState) && customerOverdueState.equals("Y"));
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

    /**
     * 是否可以用款
     * @return
     */
    private boolean canUseMoney(){
        //cycleflag != 1（不循环）不能用款
        if(mQryResultBean.getCycleFlag() == null || !mQryResultBean.getCycleFlag().equals(
                ReloanQryConst.CYCLE_FLAT_NORMAL)){
            return false;
        }

        //逾期不能用款
        if (isCustomerOverdue()){
            return false;
        }

        return isValidUseMoneyDate();
    }

    //用款日期是否合理
    private boolean isValidUseMoneyDate(){
        if(StringUtils.isEmptyOrNull(currentDate) || StringUtils.isEmptyOrNull(mQryResultBean.getLoanCycleDrawdownDate())){
            return true;
        }
        else{
            //当前日起
            LocalDate curDate = LocalDate.parse(currentDate, DateFormatters.dateFormatter1);
            //放款截止日
            LocalDate endDate = LocalDate.parse(mQryResultBean.getLoanCycleDrawdownDate(), DateFormatters.dateFormatter1);
            //截止日期早于当期日期，不能用款
            if(endDate.isBefore(curDate)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 点击提交
     */
    private void onSubmit() {

    }

    private void showNodata(){
        tvNodata.setVisibility(View.VISIBLE);
        tvNodata.setText(getResources().getString(R.string.boc_loan_query_detail_fail));
        llyBackground.setVisibility(View.GONE);
    }

    private PsnLOANListEQueryResult.PsnLOANListEQueryBean genQryResult(){
        PsnLOANListEQueryResult listQryRes = new PsnLOANListEQueryResult();
        PsnLOANListEQueryResult.PsnLOANListEQueryBean result =listQryRes.new PsnLOANListEQueryBean();

        /**贷款账户状态  01：新账户 05：已核准 07：部分放款 08：全额放款 10：取消 12：已展期
         40：结清
         50：到期
         */
        result.setLoanAccountStats("08");
        /**贷款品种*/
        result.setLoanType("1046");
        /**贷款账号*/
        result.setAccountNumber("7973922342447807");
        /**币种*/
        result.setCurrencyCode("001");
        /**核准金额*/
        result.setLoanCycleAppAmount("100000");
        /**累计放款金额（贷款金额）*/
        result.setLoanCycleAdvVal("43042");
        /**贷款余额*/
        result.setLoanCycleBalance("21434");
        /**可用金额*/
        result.setLoanCycleAvaAmount("12345");
        /**放款截止日*/
        result.setLoanCycleDrawdownDate("2016/07/01");
        /**到期日*/
        result.setLoanCycleMatDate("2016/10/11");
        /**贷款期限*/
        result.setLoanCycleLifeTerm("12");
        /**贷款利率*/
        result.setLoanCycleRate("6.4");
        /**还款账户*/
        result.setLoanCycleRepayAccount("2345556");
        /**线上标识 0：非线上 1：线上*/
        result.setOnlineFlag("1");
        /**循环类型 R：循环动用 F：不循环，一次动用 M：不循环，分次动用*/
        result.setCycleType("R");
        /**贷款放款日期*/
        result.setLoanDate("2016/01/01");
        /**逾期状态 00：正常 01：逾期*/
        result.setOverDueStat("00");
        /**还款账户标识 0：存款账号 1：长城卡*/
        result.setPayAccountFlag("0");
        /**剩余期数*/
        result.setRemainIssue("1");
        /**还款方式 F：等额本息G：等额本金B：只还利息N：协议还款*/
        result.setInterestType("F");
        /**贷款期限单位 贷款期限的单位，当前的取值只有： M-月*/
        result.setLoanPeriodUnit("M");
        /**剩余应还本金*/
        result.setRemainCapital("2000");
        /**本期应还总金额*/
        result.setThisIssueRepayAmount("200");
        /**本期还款日*/
        result.setThisIssueRepayDate("2016/02/02");
        /** 逾期期数*/
        result.setOverdueIssue("2");
        /** 本期截至当前应还利*/
        result.setThisIssueRepayInterest("100");
        /**标识该账户可执行的交易 1“advance”可以执行提前还款 2、none”，不可执行提前还款*/
        result.setTransFlag("advance");
        /**可循环贷款标示*/
        result.setCycleFlag("1");

        return result;
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

    /**
     * 检查可用额度是否合法：不能小于最低放款金额
     */
    private void doAvalQuotAmountCheck(){
        try{
            BigDecimal avlQuoteAmount = new BigDecimal(MoneyUtils.getNormalMoneyFormat(mQryResultBean.getLoanCycleAvaAmount()));
            BigDecimal minUsedAmount = new BigDecimal(MoneyUtils.getNormalMoneyFormat(minUseMoneyAmount));
            //可用额度大于等于最小放款金额
            if(avlQuoteAmount.compareTo(minUsedAmount) >= 0){
                gotoDrawApplyPage();
            }
            else{
                showErrorDialog(getString(R.string.boc_loan_draw_amount_min));
            }
        }catch(Exception e){
            e.printStackTrace();
            gotoDrawApplyPage();
        }
    }

    @Override
    public void queryLoanCycleMinAmountSuccess(String result) {
        closeProgressDialog();
        if(!StringUtils.isEmptyOrNull(result)){
            minUseMoneyAmount = result;
            doAvalQuotAmountCheck();
        }
        else{
            gotoDrawApplyPage();
        }
    }

    @Override
    public void queryLoanCycleMinAmountFail(ErrorException e) {
        closeProgressDialog();
        gotoDrawApplyPage();
    }

    @Override
    public void setPresenter(ReloanStatusContract.Presenter presenter) {

    }
}
