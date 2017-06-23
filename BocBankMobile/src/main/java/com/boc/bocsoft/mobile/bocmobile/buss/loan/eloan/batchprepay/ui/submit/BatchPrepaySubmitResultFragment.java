package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.batchprepay.ui.submit;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANBatchRepaySubmit.PsnELOANBatchRepaySubmitResultBean;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.LoanManagerFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.batchprepay.model.BatchPrepayQryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.batchprepay.model.BatchPrepaySubmitConfirmModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.batchprepay.ui.query.BatchPrepayQryFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.utils.DataUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui.HomeFragment;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import java.math.BigDecimal;

/**
 * Created by liuzc on 2016/9/8.
 */
public class BatchPrepaySubmitResultFragment extends BussFragment implements BaseOperationResultView.BtnCallback, OperationResultBottom.HomeBtnCallback {

    private View mRoot;
    private OperationResultHead.Status status;
    protected LinearLayout ll_content;

    private BatchPrepaySubmitConfirmModel prepaySubmitRes;
    private BatchPrepaySubmitTotalBean mTotalInfoBean;
    private BatchPrepayQryModel mQryModel = null; //详情信息

    //结果页
    private BaseOperationResultView borvResult;

    private int successCount = 0;//交易成功数量
    private int failedCount = 0; //交易失败数量

    private String currencyCode = ApplicationConst.CURRENCY_CNY; //币种

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRoot = mInflater.inflate(R.layout.fragment_eloan_prepay_results, null);
        return mRoot;
    }

    @Override
    public void initView() {
        borvResult = (BaseOperationResultView) mContentView.findViewById(R.id.rv_result);
        borvResult.findViewById(R.id.txt_title).setVisibility(View.GONE);
        borvResult.updateButtonStyle(); //按钮为白底红字
    }

    public void setStatus(OperationResultHead.Status status) {
        this.status = status;
    }

    @Override
    protected void titleRightIconClick() {
        super.titleRightIconClick();
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_eloan_draw_operatingresults_pagename);
    }

    @Override
    public void beforeInitView() {
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
    public void initData() {
        prepaySubmitRes= (BatchPrepaySubmitConfirmModel) getArguments().getSerializable(EloanConst.LOAN_PREPY_RESULT);
        mTotalInfoBean = (BatchPrepaySubmitTotalBean)getArguments().getSerializable(EloanConst.ELOAN_REPAY_TOTAL);
        mQryModel = (BatchPrepayQryModel)getArguments().getSerializable(EloanConst.ELON_PREPAY_COMMIT);

        if(mQryModel != null && mQryModel.getLoanList() != null && mQryModel.getLoanList().size() > 0){
            currencyCode = mQryModel.getLoanList().get(0).getCurrencyCode();
        }

        if(mTotalInfoBean == null  || prepaySubmitRes == null || prepaySubmitRes.getBatchRepayList() == null){
            borvResult.updateHead(status, getResources().getString(R.string.boc_result_status_transfer_fail));
            borvResult.setDetailsTitleIsShow(false);
            return;
        }

        initSuccessFailCount();
        borvResult.updateHead(status,
                String.format("%s", getString(R.string.boc_eloan_prepay_submit)),
                String.format("%s,%s,%s",
                        getString(R.string.boc_eloan_prepay_result_desc1, (successCount + failedCount)),
                        String.format("%s(%s%s)", getString(R.string.boc_eloan_success_count, successCount),
                                DataUtils.getCurrencyDescByLetter(mContext, currencyCode),
                                MoneyUtils.transMoneyFormat(calcSuccessSumAmount(), currencyCode)),
                        getString(R.string.boc_eloan_fail_count, failedCount)));

//        borvResult.setDetailsName(getResources().getString(R.string.boc_see_detail));

        borvResult.addDetailLayout(genDetailView());
    }

    /**
     * 初始化成功、失败记录数目
     */
    private void initSuccessFailCount(){
        for(int i = 0; i < prepaySubmitRes.getBatchRepayList().size(); i ++){
            PsnELOANBatchRepaySubmitResultBean bean = prepaySubmitRes.getBatchRepayList().get(i);
            if(bean.getStatus() != null && bean.getStatus().equals("A")){
                successCount ++;
            }
            else{
                failedCount ++;
            }
        }
    }

    /**
     * 计算成功交易金额汇总（包含本金、利息、手续费）
     * @return
     */
    private BigDecimal calcSuccessSumAmount(){
        BigDecimal reuslt = new BigDecimal(0);
        try{
            for(int i = 0; i < prepaySubmitRes.getBatchRepayList().size(); i ++){
                PsnELOANBatchRepaySubmitResultBean bean = prepaySubmitRes.getBatchRepayList().get(i);
                if(bean.getStatus() != null && bean.getStatus().equals("A")){
                    BigDecimal bdCapital = new BigDecimal(bean.getAdvanceRepayCapital());
                    BigDecimal bdInterest = new BigDecimal(bean.getAdvanceRepayInterest());
                    BigDecimal bdCharge = new BigDecimal(MoneyUtils.getNormalMoneyFormat(getTransactionCharge(bean)));
                    reuslt = reuslt.add(bdCapital).add(bdInterest).add(bdCharge);
                }

            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return reuslt;
    }

    /**
     * 生成详情页
     * @return
     */
    private View genDetailView(){
        View resultDetailView = View.inflate(mContext, R.layout.boc_view_eloan_batch_prepay_result, null);

        //失败部分layout
        LinearLayout llyFailed = (LinearLayout)resultDetailView.findViewById(R.id.llyFailed);
        TextView tvFailedTitle = (TextView)resultDetailView.findViewById(R.id.tvFailedTitle);
        tvFailedTitle.setText(getString(R.string.boc_eloan_fail_count, failedCount));
        if(failedCount < 1){
            llyFailed.setVisibility(View.GONE);
        }

        //成功部分layout
        LinearLayout llySuccess = (LinearLayout)resultDetailView.findViewById(R.id.llySuccess);
        TextView tvSuccessTitle = (TextView)resultDetailView.findViewById(R.id.tvSuccessTitle);
        tvSuccessTitle.setText(getString(R.string.boc_eloan_success_count, successCount));
        if(successCount < 1){
            llySuccess.setVisibility(View.GONE);
        }

        //成功区域添加汇总信息
        llySuccess.addView(genSumSuccessView());

        //添加成功、失败单条记录信息
        for(int i = 0; i < prepaySubmitRes.getBatchRepayList().size(); i ++){
            PsnELOANBatchRepaySubmitResultBean bean = prepaySubmitRes.getBatchRepayList().get(i);

            String capital = bean.getAdvanceRepayCapital(); //本金
            String interest = DataUtils.getFormatMoney(bean.getAdvanceRepayInterest()); //利息
            //合计
            String totalAmount = MoneyUtils.transMoneyFormat(
                    new BigDecimal(capital).add(new BigDecimal(interest)), currencyCode);

            capital = MoneyUtils.transMoneyFormat(capital, currencyCode);
            interest = MoneyUtils.transMoneyFormat(interest, currencyCode);

            if(bean.getStatus() != null && bean.getStatus().equals("A")){
                //成功记录
                String charges = getTransactionCharge(bean);
                if(StringUtils.isEmptyOrNull(charges)){
                    charges = "0";
                }
                charges = MoneyUtils.transMoneyFormat(charges, currencyCode);

                totalAmount = MoneyUtils.transMoneyFormat(
                        new BigDecimal(MoneyUtils.getNormalMoneyFormat(capital)).
                                add(new BigDecimal(MoneyUtils.getNormalMoneyFormat(interest))).
                                add(new BigDecimal(charges)), currencyCode);
                llySuccess.addView(genSuccessView(getUseMoneyDate(bean), capital, interest, charges, totalAmount));
            }
            else{
                totalAmount = MoneyUtils.transMoneyFormat(totalAmount, currencyCode);
                //失败记录
                llyFailed.addView(genFailedView(getUseMoneyDate(bean), capital, interest, totalAmount, bean.getErrorMsg()));
            }
        }
        return resultDetailView;
    }

    //获取用款日
    private String getUseMoneyDate(PsnELOANBatchRepaySubmitResultBean bean){
        if(bean == null || mQryModel == null || mQryModel.getLoanList() == null){
            return "";
        }

        String loanAccount = bean.getLoanAccount();

        for(int i = 0; i < mQryModel.getLoanList().size(); i ++){
            String account = mQryModel.getLoanList().get(i).getAccountNumber();
            if(account != null && account.equals(loanAccount)){
                return  mQryModel.getLoanList().get(i).getLoanDate();
            }
        }
        return "";
    }

    //获取交易手续费
    private String getTransactionCharge(PsnELOANBatchRepaySubmitResultBean bean){
        if(bean == null || mQryModel == null || mQryModel.getLoanList() == null){
            return "0";
        }

        String loanAccount = bean.getLoanAccount();

        for(int i = 0; i < mQryModel.getLoanList().size(); i ++){
            String account = mQryModel.getLoanList().get(i).getAccountNumber();
            if(account != null && account.equals(loanAccount)){
                return  mQryModel.getChargeList().get(i);
            }
        }
        return "0";
    }

    /**
     * 生成单笔失败记录layout
     * @param date
     * @param capital
     * @param interest
     * @param amount
     * @param reason
     * @return
     */
    private View genFailedView(String date, String capital, String interest, String amount, String reason){
        LinearLayout resultView = (LinearLayout)View.inflate(mContext, R.layout.boc_item_eloan_batch_prepay_result, null);
        LinearLayout llyContent = (LinearLayout)resultView.findViewById(R.id.llyContent);
        TextView tvTitle = (TextView)resultView.findViewById(R.id.tvTitle);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setTextColor(getResources().getColor(R.color.boc_text_color_red));
        if(!StringUtils.isEmptyOrNull(reason)){
            reason = reason.trim();
            tvTitle.setText(reason);
        }

        addRowView(getResources().getString(R.string.boc_eloan_use_money_date), date, llyContent);
        addRowView(getResources().getString(R.string.boc_eloan_prepay_capital), capital, llyContent);
        addRowView(getResources().getString(R.string.boc_eloan_prepay_interest_desc2), interest, llyContent);
        addRowView(getResources().getString(R.string.boc_eloan_prepay_capital_interest), amount, llyContent);


        return resultView;
    }

    /**
     * 生成单笔成功记录layout
     * @param date
     * @param capital
     * @param interest
     * @param charges
     * @param amount
     * @return
     */
    private View genSuccessView(String date, String capital, String interest, String charges, String amount){
        LinearLayout resultView = (LinearLayout)View.inflate(mContext, R.layout.boc_item_eloan_batch_prepay_result, null);
        LinearLayout llyContent = (LinearLayout)resultView.findViewById(R.id.llyContent);
        TextView tvTitle = (TextView)resultView.findViewById(R.id.tvTitle);
        tvTitle.setVisibility(View.GONE);

        addRowView(getResources().getString(R.string.boc_eloan_use_money_date), date, llyContent);
        addRowView(getResources().getString(R.string.boc_eloan_prepay_capital), capital, llyContent);
        addRowView(getResources().getString(R.string.boc_eloan_prepay_interest_desc2), interest, llyContent);
        addRowView(getResources().getString(R.string.boc_eloan_fee), DataUtils.getFormatCharges(charges), llyContent);

        addRowView(getResources().getString(R.string.boc_eloan_prepay_total_desc2), amount, llyContent);

        return resultView;
    }

    /**
     * 生成成功记录汇总信息layout
     * @return
     */
    private View genSumSuccessView(){
        BigDecimal bdCapital = new BigDecimal(0); //本金
        BigDecimal bdInterest = new BigDecimal(0); //利息
        BigDecimal bdCharges = new BigDecimal(0); //手续费
        BigDecimal bdTotalAmount = new BigDecimal(0); //总额

        for(int i = 0; i < prepaySubmitRes.getBatchRepayList().size(); i ++){
            PsnELOANBatchRepaySubmitResultBean bean = prepaySubmitRes.getBatchRepayList().get(i);
            if(bean.getStatus() != null && bean.getStatus().equals("A")){
                bdCapital = bdCapital.add(new BigDecimal(MoneyUtils.getNormalMoneyFormat(bean.getAdvanceRepayCapital())));
                bdInterest = bdInterest.add(new BigDecimal(MoneyUtils.getNormalMoneyFormat(bean.getAdvanceRepayInterest())));
                bdCharges = bdCharges.add(new BigDecimal(MoneyUtils.getNormalMoneyFormat(getTransactionCharge(bean))));
            }
        }

        bdTotalAmount = bdTotalAmount.add(bdCapital).add(bdInterest).add(bdCharges);

        LinearLayout resultView = (LinearLayout)View.inflate(mContext, R.layout.boc_item_eloan_batch_prepay_result, null);
        LinearLayout llyContent = (LinearLayout)resultView.findViewById(R.id.llyContent);
        TextView tvTitle = (TextView)resultView.findViewById(R.id.tvTitle);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
        tvTitle.setText(getResources().getString(R.string.boc_eloan_summary_info));

        addRowView(getResources().getString(R.string.boc_eloan_prepay_capital),
                MoneyUtils.transMoneyFormat(bdCapital, currencyCode), llyContent);
        addRowView(getResources().getString(R.string.boc_eloan_prepay_interest_desc2),
                MoneyUtils.transMoneyFormat(bdInterest, currencyCode), llyContent);
        addRowView(getResources().getString(R.string.boc_eloan_fee),
                DataUtils.getFormatCharges(MoneyUtils.transMoneyFormat(bdCharges, currencyCode)), llyContent);

        addRowView(getResources().getString(R.string.boc_eloan_prepay_total_desc2),
                MoneyUtils.transMoneyFormat(bdTotalAmount, currencyCode),
                getResources().getColor(R.color.boc_text_color_dark_gray), llyContent);

        return resultView;
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

    /**
     * 向lyt中添加一行, 若名称或者值为空，则不添加
     * @param title 名称
     * @param value 值
     * @param valueColor value的颜色
     * @param lyt
     */
    private void addRowView(String title, String value, int valueColor, LinearLayout lyt) {
        if(StringUtils.isEmpty(title) || StringUtils.isEmpty(value)){
            return;
        }
        View tempView = View.inflate(mContext, R.layout.boc_view_detail_row, null);

        TextView tvTitle = (TextView)tempView.findViewById(R.id.tv_name);
        tvTitle.setText(title);

        TextView tvValue = (TextView)tempView.findViewById(R.id.tv_value);
        tvValue.setText(value);
        tvValue.setTextColor(valueColor);

        lyt.addView(tempView);
    }

    @Override
    public void setListener() {
        borvResult.setOnclick(this);
        borvResult.setgoHomeOnclick(this);
    }

    @Override
    protected void titleLeftIconClick() {
        onClickBack();
    }

    @Override
    public boolean onBack() {
        onClickBack();
        return false;
    }

    /**
     * 返回事件处理
     */
    private void onClickBack(){
        if(getFragmentManager().findFragmentByTag(LoanManagerFragment.class.getName()) != null){
            popToAndReInit(LoanManagerFragment.class);
        }
        else{
            popToAndReInit(BatchPrepayQryFragment.class);
        }
    }

    @Override
    public void onHomeBack() {
        ModuleActivityDispatcher.popToHomePage();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClickListener(View v) {

    }

}
