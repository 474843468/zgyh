package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepayAccountDetailQuery.PsnDrawingDetailResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.BaseDetailView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DeatilsBottomTableButtom;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.LoanCosnt;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.presenter.LoanAccountDetailQueryPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.ui.repayhistory.RepayHistoryFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.ReloanQryConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.ui.ReloanUseRecordsFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.ButtonClickLock;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

/**
 * Created by taoyongzhen on 2016/10/28.
 */

public class HostoryLoanRecordDetail extends BussFragment  implements LoanMangerContract.AccountDetailQuaryView{

    private static final String TAG = "HostoryLoanRecordDetail";
    private View rootView;
    private DeatilsBottomTableButtom btPly;
    private DeatilsBottomTableButtom btDraw;
    private BaseDetailView detailView;
    private String accountNumber;
    private String currencyCode;
    private static final String btPlyKey = "btn_ply";
    private static final String btDrawKey = "btn_draw";
    private LoanAccountDetailQueryPresenter mPresenter;

    private String endDate = "";

    //上层页面传递数据
    private PsnLOANListEQueryResult.PsnLOANListEQueryBean mQryResultBean = null;
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_load_hostory_detail,null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        detailView = (BaseDetailView) rootView.findViewById(R.id.details_view);
        btPly = (DeatilsBottomTableButtom) rootView.findViewById(R.id.btn_ply);
        btDraw = (DeatilsBottomTableButtom) rootView.findViewById(R.id.btn_draw);
        btPly.updateColor(mContext.getResources().getColor(R.color.boc_main_button_color));
        btDraw.updateColor(mContext.getResources().getColor(R.color.boc_main_button_color));
    }

    @Override
    public void setListener() {
        btPly.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (!ButtonClickLock.isCanClick(btPlyKey)) {
                    return;
                }
                gotoRepayHistoryFragment();
            }
        });
        btDraw.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (!ButtonClickLock.isCanClick(btDrawKey)) {
                    return;
                }
                gotoReloanUseRecordsFragment();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        mPresenter = new LoanAccountDetailQueryPresenter(this);
        ButtonClickLock.getLock(btPlyKey).lockDuration = EloanConst.CLICK_MORE_TIME;
        ButtonClickLock.getLock(btDrawKey).lockDuration = EloanConst.CLICK_MORE_TIME;
        if (getArguments() != null) {
            mQryResultBean = (PsnLOANListEQueryResult.PsnLOANListEQueryBean)getArguments().getSerializable(LoanCosnt.LOAN_DATA);
            endDate = getArguments().getString(LoanCosnt.LOAN_END_DATE);
            queryData();
        }
    }




    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_load_settle_titile);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }


    // 用款记录
    private void gotoReloanUseRecordsFragment(){
        if(mQryResultBean == null){
            return;
        }
        if(StringUtils.isEmptyOrNull(mQryResultBean.getAccountNumber())){
            Log.d(TAG,"accountNumber is no vailed");
            return;
        }
        if(StringUtils.isEmptyOrNull(mQryResultBean.getCurrencyCode())) {
            Log.d(TAG,"currencyCode is no vailed");
            return;

        }
        Bundle bundle = new Bundle();
        bundle.putString(ReloanQryConst.KEY_ACCOUNT_NUMBER, mQryResultBean.getAccountNumber());
        bundle.putString(ReloanQryConst.KEY_CURRENCY, mQryResultBean.getCurrencyCode());
        bundle.putBoolean(ReloanQryConst.SHOW_DETAIL,false);
        ReloanUseRecordsFragment fragment = new ReloanUseRecordsFragment();
        fragment.setArguments(bundle);
        start(fragment);

    }

    //还款记录
    private void gotoRepayHistoryFragment() {
        Bundle bundle = new Bundle();
        RepayHistoryFragment repayHistoryFragment = new  RepayHistoryFragment();
        repayHistoryFragment.setShowTitleBarView(true);
        repayHistoryFragment.setHistoryData(mQryResultBean.getAccountNumber(),endDate,mQryResultBean.getCurrencyCode());
        repayHistoryFragment.setArguments(bundle);
        start(repayHistoryFragment);

    }

    private void showData(String loanRepayPeriod){
        if (mQryResultBean == null) {
            return;
        }

        //头部信息
        currencyCode = DataUtils.getCurrencyDescByLetter(mContext, mQryResultBean.getCurrencyCode());
        //贷款金额
        String headTitle = String.format(getString(R.string.boc_loan_hostory_drawamount), currencyCode);
        //金额：循环 核准金额；非循环 累计放款金额（贷款金额）
        String  amount = "";
        if ("R".equals(mQryResultBean.getCycleType())){
            amount = MoneyUtils.transMoneyFormat(mQryResultBean.getLoanCycleAppAmount(),mQryResultBean.getCurrencyCode());
        } else{
            amount = MoneyUtils.transMoneyFormat(mQryResultBean.getLoanCycleAdvVal(),mQryResultBean.getCurrencyCode());
        }
        detailView.updateHeadData(headTitle, amount);
        //期限/利率 12期/5.32%
        String headDetailKey = getString(R.string.boc_load_hostory_detail_rate_term);
        String headdetailValue = String.format(getString(R.string.boc_load_hostory_detail_rate_term_vlaue),mQryResultBean.getLoanCycleLifeTerm(),MoneyUtils.transRatePercentTypeFormat(mQryResultBean.getLoanCycleRate()));

        detailView.updateHeadDetail(headDetailKey, headdetailValue);

        //还款方式
        String interestType = "-";
        if(StringUtil.isNullOrEmpty(loanRepayPeriod) == false) {
            interestType = DataUtils.getRepayTypeDesc(mContext,mQryResultBean.getInterestType(),loanRepayPeriod);
        }
        //还款周期
        String periodUnit = mQryResultBean.getLoanPeriodUnit();
        periodUnit = DataUtils.getLoanUnitPeriodUnitDesc(mContext,periodUnit);
        if(StringUtils.isEmptyOrNull(periodUnit)){
            periodUnit = "-";
        }
        //放款日
        String loanDate = mQryResultBean.getLoanDate();
        if(StringUtils.isEmptyOrNull(loanDate)){
            loanDate = "-";
        }

        //贷款到期日
        String loanCycleMatDate = mQryResultBean.getLoanCycleMatDate();
        if(StringUtils.isEmptyOrNull(loanCycleMatDate)){
            loanCycleMatDate = "-";
        }
        //还款账号
        String loanCycleRepayAccount = NumberUtils.formatCardNumber(mQryResultBean.getLoanCycleRepayAccount());
        if(StringUtils.isEmptyOrNull(loanCycleRepayAccount)){
            loanCycleRepayAccount = "-";
        }
        //贷款账号
        accountNumber = NumberUtils.formatCardNumber(mQryResultBean.getAccountNumber());
        if(StringUtils.isEmptyOrNull(periodUnit)){
            accountNumber = "-";
        }
        
        detailView.addDetailRow(getString(R.string.boc_load_hostory_detail_interest_type), interestType);
        detailView.addDetailRow(getString(R.string.boc_load_hostory_detail_period_unit), periodUnit);
        detailView.addDetailRow(getString(R.string.boc_load_hostory_detail_loan_date), loanDate);
        detailView.addDetailRow(getString(R.string.boc_load_hostory_detail_mat_date), loanCycleMatDate);
        detailView.addDetailRow(getString(R.string.boc_load_hostory_detail_replay_account), loanCycleRepayAccount);
        detailView.addDetailRow(getString(R.string.boc_load_hostory_detail_account_number), accountNumber);

    }

    @Override
    public void quaryAccountDetailFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showData("");

    }

    @Override
    public void quaryAccounDetailSuccess(PsnDrawingDetailResult eloanDrawDetailResult) {
        closeProgressDialog();
        if (eloanDrawDetailResult == null || StringUtil.isNullOrEmpty(eloanDrawDetailResult.getLoanRepayPeriod())){
            showData("");
        }else {
            showData(eloanDrawDetailResult.getLoanRepayPeriod());
        }
    }

    @Override
    public void setPresenter(LoanMangerContract.Presenter presenter) {

    }


    private void queryData(){
        if (mQryResultBean == null) {
            return;
        }
        String account = mQryResultBean.getAccountNumber();
        if(StringUtil.isNullOrEmpty(account)) {
            showData("");
            return;
        }
        showLoadingDialog();
        mPresenter.queryAccountDetail(account);
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.unsubscribe();
        }
        super.onDestroyView();
    }
}
