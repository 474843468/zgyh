package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.batchprepay.ui.submit;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANRepayCount.PsnELOANRepayCountParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANRepayCount.PsnELOANRepayCountResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayerAcountCheck.PsnLOANPayerAcountCheckParams;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model.PrepayAccountDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.batchprepay.model.BatchPrepayQryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.QueryAllChinaBankAccountRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.RepaymentAccountCheckRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.ui.AccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.singleprepay.presenter.SinglePrepaySubmitPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.singleprepay.ui.SinglePrepaySubmitContract;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * 贷款管理-中银E贷-批量提前还款Fragment
 * 页面初始化后请求数据逻辑： 请求会话ID -> 查找默认账户 -> 账户检查 -> 获取账户详情
 * Created by liuzc on 2016/9/7.
 */
public class BatchPrepaySubmitFragment extends BussFragment implements SinglePrepaySubmitContract.View{
    //获取会话ID成功后的下一步操作
    enum NEXT_STEP{
        getDefRepayAccount,  //获取默认还款账户
        gotoSelectAccount   //进入账户选择页面
    }
    private static final String TAG = "BPrepaySubmitFrag";

    private View mRootView;
    private ScrollView sclvBackground; //背景scrollView

    private LinearLayout llyRepayAccount = null; //还款账户背景
    private LinearLayout llyContent = null; //还款账户以下的页面背景

    private EditChoiceWidget edtCRepaymentAccount;//还款账户
    private LinearLayout llyAvailableAmount; //账户可用余额layout
    private TextView tvAvailableAmount; //账户可用余额
    private LinearLayout llyTotalInfo; //汇总信息layout
    private LinearLayout llyDetailInfo; //详情信息layout

    private Button btnSubmit;//提交按钮

    private SinglePrepaySubmitPresenter mPresenger;

    //还款账户
    private String accountId = null;
    private String account = null;

    //卡内可用余额
    private String availableBalance = null;

    //上层页面传递过来的数据
    private BatchPrepayQryModel mQryModel = null;
    private String quoteNo = null; //额度编号
    private String repayaccount = null; //还款账号

    private BatchPrepaySubmitTotalBean mTotalInfoBean = null; //金额、利息的汇总信息
    private BigDecimal totalRepayAmount = null; //全部还款总额, 包含手续费

    private String currencyCode = ApplicationConst.CURRENCY_CNY; //币种，默认为人民币

    private List<String> chargeList; //手续费列表
    private int calcChargesIndex = 0; //计算费用的记录的索引值

    private String conversationID = null; //会话ID

    private NEXT_STEP nextStep = NEXT_STEP.getDefRepayAccount;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.fragment_eloan_batch_prepay, null);
        return mRootView;
    }

    @Override
    protected void titleLeftIconClick() {
        super.titleLeftIconClick();
    }


    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_eloan_prepay_info);
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
    public void initView() {
        //初始化组件
        sclvBackground = (ScrollView)mRootView.findViewById(R.id.sclvBackground);
        llyRepayAccount = (LinearLayout)mRootView.findViewById(R.id.llyRepayAccount);
        llyContent = (LinearLayout)mRootView.findViewById(R.id.llyContent);

        edtCRepaymentAccount = (EditChoiceWidget) mRootView.findViewById(R.id.repaymentAccount);
        llyAvailableAmount = (LinearLayout)mRootView.findViewById(R.id.llyAvailableAmount);
        tvAvailableAmount = (TextView) mRootView.findViewById(R.id.tvAvailableAmount);

        llyTotalInfo = (LinearLayout)mRootView.findViewById(R.id.llyTotalInfo);
        llyDetailInfo = (LinearLayout)mRootView.findViewById(R.id.llyDetailInfo);

        btnSubmit = (Button) mRootView.findViewById(R.id.btnSubmit);

        edtCRepaymentAccount.setChoiceTextName(getString(R.string.boc_eloan_repaymentAccount));
        edtCRepaymentAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));
    }

    @Override
    public void initData() {
        mQryModel = (BatchPrepayQryModel)getArguments().getSerializable(EloanConst.ELON_PREPAY);
        quoteNo = getArguments().getString(EloanConst.LOAN_QUOTENO);

        //利息四舍五入显示
        for(int i = 0; i < mQryModel.getLoanList().size(); i ++){
            PsnLOANListEQueryResult.PsnLOANListEQueryBean bean = mQryModel.getLoanList().get(i);
            bean.setThisIssueRepayInterest(DataUtils.getFormatMoney(bean.getThisIssueRepayInterest()));
            bean.setRemainCapital(DataUtils.getFormatMoney(bean.getRemainCapital()));
        }

        //还款账号
        repayaccount = getArguments().getString(EloanConst.PEPAY_ACCOUNT);

        mPresenger = new SinglePrepaySubmitPresenter(this);

        mTotalInfoBean = new BatchPrepaySubmitTotalBean();

        showLoadingDialog();
        mPresenger.creatConversation();
    }

    /**
     * 查询中行账户列表
     */
    private void queryAccount(){
        List<String> list = new ArrayList<String>();
        list.add("119");
        mPresenger.queryAllChinaBankAccount(list);
    }

    /**
     * 更新页面
     */
    private void updateViews(){
        llyDetailInfo.removeAllViews();
        llyTotalInfo.removeAllViews();
        llyContent.setVisibility(View.VISIBLE);
        //初始化金额信息
        BigDecimal prepayCapital = new BigDecimal(0); //提前还款本金
        BigDecimal prepayInterest = new BigDecimal(0);//提前还款利息
        BigDecimal totalCapAndInt = new BigDecimal(0);//本息合计汇总
        totalRepayAmount = new BigDecimal(0); //总额 本息合计 加上 手续费

        //绘制详情信息，累加到汇总信息中
        List<PsnLOANListEQueryResult.PsnLOANListEQueryBean> prepayList = mQryModel.getLoanList();

        for(int i = 0; i < prepayList.size(); i ++){
            PsnLOANListEQueryResult.PsnLOANListEQueryBean bean = prepayList.get(i);
            currencyCode = bean.getCurrencyCode();
            String sCapital = bean.getRemainCapital();
            String sInterest = bean.getThisIssueRepayInterest();
            String sCharges = mQryModel.getChargeList().get(i);
            if(StringUtils.isEmptyOrNull(sCharges)){
                sCharges = "0";
            }

            BigDecimal bigdCapital = new BigDecimal(sCapital);
            BigDecimal bigdInterest = new BigDecimal(sInterest);
            BigDecimal bigdAmount = bigdCapital.add(bigdInterest);

            prepayCapital = prepayCapital.add(bigdCapital);
            prepayInterest = prepayInterest.add(bigdInterest);
            totalCapAndInt = totalCapAndInt.add(bigdAmount);


            llyDetailInfo.addView(genDetailInfoView(bean.getLoanDate(), MoneyUtils.transMoneyFormat(sCapital, currencyCode),
                    MoneyUtils.transMoneyFormat(sInterest, currencyCode),
                    MoneyUtils.transMoneyFormat(sCharges, currencyCode),
                    MoneyUtils.transMoneyFormat(bigdAmount.add(new BigDecimal(sCharges)), currencyCode)));
        }

        //绘制汇总信息
        totalRepayAmount = totalCapAndInt.add(getTotalCharges());

        String sTotalCapital = MoneyUtils.transMoneyFormat(prepayCapital, currencyCode);
        String sTotalInterest = MoneyUtils.transMoneyFormat(prepayInterest, currencyCode);
        String sTotalCapAndInt = MoneyUtils.transMoneyFormat(totalCapAndInt, currencyCode);
        String sTotalAmount = MoneyUtils.transMoneyFormat(totalRepayAmount, currencyCode);
        String sTotalCharges = MoneyUtils.transMoneyFormat(getTotalCharges(), currencyCode);
        llyTotalInfo.addView(genSumInfoView(sTotalCapital, sTotalInterest, sTotalCharges, sTotalAmount));

        mTotalInfoBean.setTotalCapital(sTotalCapital);
        mTotalInfoBean.setTotalInterest(sTotalInterest);
        mTotalInfoBean.setTotalAmount(sTotalCapAndInt);
        mTotalInfoBean.setTotalAmountWithCharges(sTotalAmount);
        mTotalInfoBean.setTotalCharges(sTotalCharges);
    }

    /**
     * 开始计算提前还款费用
     */
    private void beginCalcPrepayCharges(){
        calcChargesIndex = 0;
        PsnLOANListEQueryResult.PsnLOANListEQueryBean bean = mQryModel.getLoanList().get(0);
        showLoadingDialog();
        calcPrepayCharges(bean);
    }

    /**
     * 计算单笔提前还款手续费
     * @param bean
     */
    private void calcPrepayCharges(PsnLOANListEQueryResult.PsnLOANListEQueryBean bean){
        try{
            PsnELOANRepayCountParams params = new PsnELOANRepayCountParams();

            params.setLoanType(bean.getLoanType());
            params.setLoanAccount(bean.getAccountNumber());
            params.setCurrency(ApplicationConst.CURRENCY_CNY);
            params.setAdvanceRepayCapital(bean.getRemainCapital());
            params.setAdvanceRepayInterest(getStrWith2Decimal(bean.getThisIssueRepayInterest()));

            String remainAmount = (new BigDecimal(bean.getRemainCapital()).add(new BigDecimal(bean.getThisIssueRepayInterest()))).toString();
            params.setRepayAmount(getStrWith2Decimal(remainAmount));
            params.setAccountId(accountId);
            params.setPayAccount(bean.getLoanCycleRepayAccount());

            mPresenger.calcCharges(params);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 数值四舍五入得到两位小数
     * @param value
     * @return
     */
    private String getStrWith2Decimal(String value){
        if(StringUtils.isEmptyOrNull(value)){
            return "";
        }

        BigDecimal bdValue = new BigDecimal(value);
        return bdValue.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 添加汇总信息
     * @param capital 本金
     * @param interest 利息
     * @param charges 手续费
     * @param amount 还款总额
     * @return
     */
    private View genSumInfoView(String capital,String interest, String charges, String amount){
        View sumInfoView = View.inflate(mContext, R.layout.boc_item_eloan_batch_prepay_submit, null);
        LinearLayout llyContent = (LinearLayout)sumInfoView.findViewById(R.id.llyContent);

        addRowView(getResources().getString(R.string.boc_eloan_prepay_capital), capital, llyContent);
        addRowView(getResources().getString(R.string.boc_eloan_prepay_interest_desc2), interest, llyContent);
        addRowView(getResources().getString(R.string.boc_eloan_fee), DataUtils.getFormatCharges(charges), llyContent);
        addRowView(getResources().getString(R.string.boc_eloan_prepay_total_desc2), amount,
                getResources().getColor(R.color.boc_text_color_red), llyContent);

        return sumInfoView;
    }

    /**
     * 添加详情信息
     * *@param date 用款日期
     * @param capital 本金
     * @param interest 利息
     * @param charges 手续费
     * @param amount 还款总额
     * @return
     */
    private View genDetailInfoView(String date, String capital,String interest, String charges, String amount){
        View detailInfoView = View.inflate(mContext, R.layout.boc_item_eloan_batch_prepay_submit, null);
        LinearLayout llyContent = (LinearLayout)detailInfoView.findViewById(R.id.llyContent);

        addRowView(getResources().getString(R.string.boc_eloan_use_money_date), date, llyContent);
        addRowView(getResources().getString(R.string.boc_eloan_prepay_capital), capital, llyContent);
        addRowView(getResources().getString(R.string.boc_eloan_prepay_interest_desc2), interest, llyContent);
        addRowView(getResources().getString(R.string.boc_eloan_fee), DataUtils.getFormatCharges(charges), llyContent);
        addRowView(getResources().getString(R.string.boc_eloan_prepay_total_desc2), amount, llyContent);

        return detailInfoView;
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
        edtCRepaymentAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringUtils.isEmptyOrNull(conversationID)){
                    showLoadingDialog(false);
                    nextStep = NEXT_STEP.gotoSelectAccount;
                    mPresenger.creatConversation();
                }
                else{
                    gotoSelectAccountPage();
                }
            }
        });
        
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judge();
            }
        });
    }

    private void gotoSelectAccountPage(){
        AccoutFragment accoutFragment = new AccoutFragment();
        accoutFragment.setAccountType(AccoutFragment.AccountType.SELECT_REPAYMENTACCOUNT);
        accoutFragment.setConversationId(conversationID);
        Bundle bundle = new Bundle();
        bundle.putString("type", "prepay");
        accoutFragment.setArguments(bundle);
        startForResult(accoutFragment, AccoutFragment.RequestCode);
    }

    private void judge() {
        if (!getResources().getString(R.string.boc_common_select).equals(edtCRepaymentAccount.getChoiceTextContent())){
            if (!TextUtils.isEmpty(availableBalance)){

                Boolean bAccountMoneyEnough = (totalRepayAmount.compareTo(new BigDecimal(
                        MoneyUtils.getNormalMoneyFormat(availableBalance))) <= 0);
                if (bAccountMoneyEnough){
                    gotoConfirmPage();
                }else{
                    showErrorDialog(getResources().getString(R.string.boc_eloan_no_enough_money));
                }
            }else{
                showErrorDialog(getResources().getString(R.string.boc_eloan_input_repay_amount));
            }
        }else{
            showErrorDialog(getResources().getString(R.string.boc_loan_select_repay_account));
        }
    }

    //进入确认页
    private void gotoConfirmPage(){
        BatchPrepaySubmitConfirmFragment fragment = new BatchPrepaySubmitConfirmFragment();

        fragment.setConversationID(conversationID);
        //传递还款详情、汇总信息
        Bundle bundle = new Bundle();
        bundle.putSerializable(EloanConst.ELON_PREPAY_COMMIT, mQryModel);
        bundle.putSerializable(EloanConst.ELOAN_REPAY_TOTAL, mTotalInfoBean);
        bundle.putString(EloanConst.LOAN_QUOTENO, quoteNo);

        fragment.setArguments(bundle);
        start(fragment);
    }

    @Override
    public void onDestroyView() {
        mPresenger.unsubscribe();
        super.onDestroyView();
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == AccoutFragment.ResultCode) {
            if (data != null) {
                Log.i(TAG, "-------->account" + data.get("account").toString());
                account = data.get("account").toString();
                accountId = data.get("accountId").toString();
                mTotalInfoBean.setPayAccount(account);
                mTotalInfoBean.setPayAccountID(accountId);
                edtCRepaymentAccount.setChoiceTextContent(NumberUtils.formatCardNumber(account));

                llyContent.setVisibility(View.GONE);
                showLoadingDialog();
                mPresenger.prepayCheckAccountDetail(accountId);
            }
        }
    }

    @Override
    public void obtainConversationSuccess(String conversationId) {
        this.conversationID = conversationId;

        if(nextStep == NEXT_STEP.getDefRepayAccount){
            queryAccount();
        }
        else{
            gotoSelectAccountPage();
        }
    }

    @Override
    public void obtainConversationFail(ErrorException e) {
        closeProgressDialog();
    }

    @Override
    public void prepayCheckAccountDetailSuccess(PrepayAccountDetailModel.AccountDetaiListBean result) {
        Log.i(TAG, "提前还款查询账户详情成功！");
        availableBalance=String.valueOf(result.getAvailableBalance());
        llyAvailableAmount.setVisibility(View.VISIBLE);
        
        if(StringUtils.isEmptyOrNull(currencyCode)){
            currencyCode = ApplicationConst.CURRENCY_CNY;
        }
        tvAvailableAmount.setText((DataUtils.getCurrencyDescByLetter(mContext, currencyCode) +
                " " + MoneyUtils.transMoneyFormat(availableBalance, currencyCode)));
        beginCalcPrepayCharges();
    }

    @Override
    public void prepayCheckAccountDetailFail(ErrorException e) {
        Log.i(TAG, "提前还款查询账户详情失败！");
        llyAvailableAmount.setVisibility(View.GONE);
        availableBalance = "";
        closeProgressDialog();
        showErrorDialog(getString(R.string.boc_eloan_searchdetail_fail));
    }

    @Override
    public void calcChargesSuccess(PsnELOANRepayCountResult result) {
        if(chargeList == null){
            chargeList = new LinkedList<String>();
        }

        String charges = result.getCharges(); //当前手续费
        if(StringUtils.isEmptyOrNull(charges)){
            charges = "0";
        }
        else{
            //四舍五入保留两位小数
            charges = DataUtils.getFormatMoney(charges);
        }

        chargeList.add(charges);
        calcChargesIndex ++;

        if(calcChargesIndex == mQryModel.getLoanList().size()){
            //所有记录的手续费都已经算完，进入到确认页
            closeProgressDialog();
            mQryModel.setChargeList(chargeList);
            updateViews();
        }
        else{
            //继续计算下一笔手续费
            calcPrepayCharges(mQryModel.getLoanList().get(calcChargesIndex));
        }
    }

    @Override
    public void calcChargesFail(ErrorException e) {
        closeProgressDialog();
    }

    @Override
    public void obtainAllChinaBankAccountSuccess(List<QueryAllChinaBankAccountRes> res) {
        boolean  istrue = false;
        if (!PublicUtils.isEmpty(res) && repayaccount != null) {
            //账户检查参数
            PsnLOANPayerAcountCheckParams params = new PsnLOANPayerAcountCheckParams();
            params.setConversationId(conversationID);

            for (QueryAllChinaBankAccountRes queryAllChinaBankAccountRes : res) {
                if (repayaccount != null && repayaccount.equals(
                        queryAllChinaBankAccountRes.getAccountNumber())) {
                    account = queryAllChinaBankAccountRes.getAccountNumber();
                    accountId = queryAllChinaBankAccountRes.getAccountId() + "";
                    mTotalInfoBean.setPayAccount(account);
                    mTotalInfoBean.setPayAccountID(accountId);

                    params.setAccountId(accountId);
                    params.setCurrencyCode(queryAllChinaBankAccountRes.getCurrencyCode());

                    edtCRepaymentAccount.setChoiceTextContent(NumberUtils.formatCardNumber(account));
                    LogUtils.i(TAG, "------------>获取账户列表成功!----->有匹配的");
                    istrue =true;
                    break;
                } else {
                    LogUtils.i(TAG, "------------>获取账户列表成功!---->没有匹配的");
                    edtCRepaymentAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));
                    istrue =false;
                }
            }
            if(istrue && !StringUtils.isEmptyOrNull(accountId)){
                mPresenger.checkRepaymentAccount(params);
            }else{
                availableBalance = null;
                edtCRepaymentAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));
                closeProgressDialog();
            }
        } else {
            LogUtils.i(TAG, "------------>获取账户列表成功!------->为空");
            availableBalance = null;
            edtCRepaymentAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));
            closeProgressDialog();
        }
    }

    @Override
    public void obtainAllChinaBankAccountFail(ErrorException e) {
        closeProgressDialog();

        showErrorDialog(getString(R.string.boc_eloan_search_accountlist_fail));
        availableBalance = null;
        edtCRepaymentAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));
    }

    @Override
    public void doRepaymentAccountCheckSuccess(RepaymentAccountCheckRes res) {
        if(res != null && res.getCheckResult() != null && res.getCheckResult().size() > 0){
            String status = (String)res.getCheckResult().get(0);
            if(!StringUtils.isEmptyOrNull(status) && status.equals("01") || status.equals("00")){
                //检查成功，继续请求账户余额信息
                mPresenger.prepayCheckAccountDetail(accountId);
                return;
            }
        }

        closeProgressDialog();
        availableBalance = null;
        edtCRepaymentAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));
    }

    @Override
    public void doRepaymentAccountCheckFail(ErrorException e) {
        closeProgressDialog();
        availableBalance = null;
        edtCRepaymentAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));
    }

    @Override
    public void setPresenter(SinglePrepaySubmitContract.Presenter presenter) {

    }

    /**
     * 计算总的手续费
     * @return
     */
    private BigDecimal getTotalCharges(){
        BigDecimal result = new BigDecimal(0);
        if(chargeList == null || chargeList.size() < 1){
            return result;
        }
        for(int i = 0; i < chargeList.size(); i ++){
            result = result.add(new BigDecimal(chargeList.get(i)));
        }

        return result;
    }
}
