package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui;

import java.math.BigDecimal;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.SpUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.BaseDetailView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowButton;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.ui.PrepayFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.ui.RepayPlanFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.ui.ApplyFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.ui.ChangeAccountResultFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.ui.ChangeRepaymentAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanAccountListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanDrawDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanDrawRecordModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanOverdueModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanRepaymentModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.presenter.EloanqueryPresenter;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.singleprepay.ui.SinglePrepaySubmitFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.utils.ResUtils;


/**
 * Created by huixiaobo on 2016/7/7.
 * 还款详情页面
 */
public class EloanRepayDetailFragment extends BussFragment implements View.OnClickListener,EloanQueryContract.RepayNumView {
    /**view*/
    protected View rootView;
    /**显示详情公共组件*/
    protected DetailTableHead repayDetailHead;
    /**显示抬头底部内容组件*/
    protected DetailContentView repayDetailRow;
    /**公组件*/
    private DetailContentView repayDetail;
    /**网络请求接口类*/
    private EloanqueryPresenter mPresenter;
    /**提前还款组件布局*/
    private LinearLayout eloanRepayLl;
    /**贷款账户*/
    private String loanAccount;
    /**说明组件*/
    private TextView hintTv;
    /**用款页面model*/
    private EloanAccountListModel.PsnLOANListEQueryBean mPrepayDetailModel;
    /**当前时间*/
    private String mEndDate;
    /**还款账户*/
    private String mRayAccount;
    protected BaseDetailView titleValueTv;
    /**详情对象*/
    private EloanDrawDetailModel mDrawDetail;
    /**额度编号*/
    private String mQuoteNo;
    /***/
    private boolean isDrawDetail;
    /**应还未还总额布局*/
    private LinearLayout repayAmount;
    /**应还未还总额名称*/
    private TextView repayOverdueAum;
    /**应还未还总额金额*/
    private TextView repayOverdueAumNo;
    /**到期日*/
    private String mLantoDate;
    
    private RepayPlanFragment repayPlan;

    //是否永久隐藏提前还款按钮（中银E贷提前还款页进入本页面，要隐藏）
    private boolean bAlwaysHidePrepayBtn = false;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_repaydetail_fragment, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        repayDetailHead = (DetailTableHead) rootView.findViewById(R.id.repayDetailHead);
        repayDetailRow = (DetailContentView) rootView.findViewById(R.id.repayDetailRow);
        repayDetail = (DetailContentView) rootView.findViewById(R.id.repayDetail);
        hintTv = (TextView) rootView.findViewById(R.id.hintTv);
        repayAmount = (LinearLayout) rootView.findViewById(R.id.repayAmount);
        repayOverdueAum = (TextView) rootView.findViewById(R.id.repayOverdueAum);
        repayOverdueAumNo = (TextView) rootView.findViewById(R.id.repayOverdueAumNo);
        eloanRepayLl = (LinearLayout) rootView.findViewById(R.id.eloanRepayLl);
        eloanRepayLl.setOnClickListener(this);

    }

    @Override
    public void initData() {
        super.initData();
        mPresenter = new EloanqueryPresenter(this);
        //单条未还款数据
        mPrepayDetailModel = (EloanAccountListModel.PsnLOANListEQueryBean) getArguments()
                .getSerializable(EloanConst.ELON_PREPAY_DETAIL);
        //还款账户
        mRayAccount = getArguments().getString(EloanConst.PEPAY_ACCOUNT);
        //当前时间
        mEndDate = getArguments().getString(EloanConst.DATA_TIME);
        //额度编号
        mQuoteNo = getArguments().getString(EloanConst.LOAN_QUOTENO);
        //返回跳转用款首页判断字段
        isDrawDetail = getArguments().getBoolean(EloanConst.LOAN_PREPAY_DETAIL);
        //007 详情卡接口请求
        if (mPrepayDetailModel!= null && mPrepayDetailModel.getAccountNumber() != null) {
            showLoadingDialog();
            mPresenter.queryDrawingDetail(mPrepayDetailModel.getAccountNumber());
        }
    }

    @Override
    protected void titleLeftIconClick() {
        super.titleLeftIconClick();
        if (isDrawDetail) {
            popToAndReInit(EloanDrawFragment.class);
        }
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_eloan_details);
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }


    @Override
    public void eDrawingDetailFail(BiiResultErrorException biiResultErrorException) {
    	closeProgressDialog();
    }

    @Override
    public void eDrawingDetailSuccess(EloanDrawDetailModel eloanDrawDetailResult) {
        if (eloanDrawDetailResult == null) {
            return;
        }
        if (mPrepayDetailModel!= null && mPrepayDetailModel.getAccountNumber() != null) {
            mPresenter.queryDraw(mPrepayDetailModel.getAccountNumber(), mPrepayDetailModel.getLoanDate());
        }
        closeProgressDialog();
        mDrawDetail = new EloanDrawDetailModel();
        mDrawDetail = eloanDrawDetailResult;
        loanAccount = eloanDrawDetailResult.getLoanAccount();
        mLantoDate = eloanDrawDetailResult.getLoanToDate();

        BigDecimal big = new BigDecimal(mPrepayDetailModel.getNopayamtAmount());
        if (TextUtils.isEmpty(mPrepayDetailModel.getNopayamtAmount())
                || big.compareTo(BigDecimal.ZERO) == 0) {
            repayAmount.setVisibility(View.GONE);
        } else {
            repayAmount.setVisibility(View.VISIBLE);
            repayOverdueAum.setText(getString(R.string.boc_details_overduerepayamount));
            repayOverdueAumNo.setText(MoneyUtils.transMoneyFormat(mPrepayDetailModel.getNopayamtAmount(),"001"));
        }
        repayDetailHead.updateData(getString(R.string.boc_details_title), MoneyUtils.transMoneyFormat(eloanDrawDetailResult.getRemainCapital(),
                "001"));
        repayDetailHead.setDetailVisable(false);
        repayDetail.addDetail(getString(R.string.boc_details_drawamount),
                MoneyUtils.transMoneyFormat(eloanDrawDetailResult.getLoanAmount(), "001"));
        repayDetail.addDetail(getString(R.string.boc_details_Period),
                eloanDrawDetailResult.getLoanPeriod()+"个月" + "/"
                        +eloanDrawDetailResult.getLoanRate() + "%");
        
        if (eloanDrawDetailResult.getInterestType().equals("B")
                && eloanDrawDetailResult.getLoanRepayPeriod().equals("01")
                ||eloanDrawDetailResult.getLoanRepayPeriod().equals("1")) {

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0, 0,0);
            repayDetailRow.getDetailTableRowButton().addTextBtn(getResources().getString(R.string.boc_details_repaydate),
                    eloanDrawDetailResult.getThisIssueRepayDate(),
                    getResources().getString(R.string.boc_eloan_repayrecord),getResources().getColor(R.color.boc_main_button_color), params);

            repayDetailRow.addDetail(getString(R.string.boc_details_repayrate),
                        MoneyUtils.transRoundMoneyFormat(eloanDrawDetailResult.getThisIssueRepayInterest(), "001") );
            //修改逾期时应还金额显示NopayamtAmount
            repayDetailRow.addDetail(getString(R.string.boc_details_repayamount),
                        MoneyUtils.transMoneyFormat(eloanDrawDetailResult.getThisIssueRepayAmount(), "001"));

//            if ("01".equals(mPrepayDetailModel.getOverDueStat())
//                    || "1".equals(mPrepayDetailModel.getOverDueStat())) {
//                repayDetailRow.addDetail(getString(R.string.boc_details_repayoverdueamount),
//                        MoneyUtils.transMoneyFormat(mPrepayDetailModel.getNopayamtAmount(), "001"));
//            }
            repayDetail.addDetail(getString(R.string.boc_details_repaytype),
                    getString(R.string.boc_details_repaytypeOne));
          //判断是否逾期
            if (mPrepayDetailModel.getOverDueStat().equals("01")
                    || mPrepayDetailModel.getOverDueStat().equals("1")) {
            	hintTv.setVisibility(View.VISIBLE);
            	hintTv.setText(getString(R.string.boc_details_OverdueIssue));
            }

            //判断是否可提前还款
            if (mPrepayDetailModel.getOverDueStat().equals("01")
                    || mPrepayDetailModel.getOverDueStat().equals("1")
                    || (!StringUtils.isEmpty(mEndDate)
                    && eloanDrawDetailResult.getLoanToDate().equals(mEndDate))
                    || mPrepayDetailModel.getTransFlag().equals("none")) {

                eloanRepayLl.setVisibility(View.GONE);

            } else {
                eloanRepayLl.setVisibility(View.VISIBLE);

            }
            setPager();
        } else if (eloanDrawDetailResult.getInterestType().equals("B")
                && eloanDrawDetailResult.getLoanRepayPeriod().equals("98")){

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0, 0,0);
            repayDetailRow.getDetailTableRowButton().addTextBtn(getResources().getString(R.string.boc_details_date),
                    eloanDrawDetailResult.getThisIssueRepayDate(),
                    getResources().getString(R.string.boc_eloan_repayrecord),getResources().getColor(R.color.boc_main_button_color),params);
            //修改逾期时应还金额显示NopayamtAmount
            repayDetailRow.addDetail(getString(R.string.boc_details_ramount),
                        MoneyUtils.transMoneyFormat(eloanDrawDetailResult.getThisIssueRepayAmount(), "001"));

//            if ("01".equals(mPrepayDetailModel.getOverDueStat()) ||
//                    "1".equals(mPrepayDetailModel.getOverDueStat())) {
//                repayDetailRow.addDetail(getString(R.string.boc_details_repayoverdueamount),
//                        MoneyUtils.transMoneyFormat(mPrepayDetailModel.getNopayamtAmount(), "001"));
//            }

            repayDetail.addDetail(getString(R.string.boc_details_repaytype),
                    getString(R.string.boc_details_repaytypeTwo));
            //判断是否逾期
            if (mPrepayDetailModel.getOverDueStat().equals("01")
                    || mPrepayDetailModel.getOverDueStat().equals("1")) {
            	hintTv.setVisibility(View.VISIBLE);
                hintTv.setText(getString(R.string.boc_details_OverdueIssue));
            }

            //判断是否可提前还款
            if (mPrepayDetailModel.getOverDueStat().equals("01")
                    || mPrepayDetailModel.getOverDueStat().equals("1")
                    || (!StringUtils.isEmpty(mEndDate)
                    && eloanDrawDetailResult.getLoanToDate().equals(mEndDate))
                    || mPrepayDetailModel.getTransFlag().equals("none")) {

                eloanRepayLl.setVisibility(View.GONE);

            } else {
                eloanRepayLl.setVisibility(View.VISIBLE);

            }
            setPager();
        }

        repayDetail.addDetail(getString(R.string.boc_eloan_toptiem), eloanDrawDetailResult.getLoanDate());
        repayDetail.addDetail(getString(R.string.boc_details_loanDate), eloanDrawDetailResult.getLoanToDate());
        
        // 还款账户
        if (!TextUtils.isEmpty(ChangeRepaymentAccountFragment.mQuoteno)
                && ChangeRepaymentAccountFragment.mQuoteno.equals(mQuoteNo)
                &&!StringUtils.isEmptyOrNull(ChangeAccountResultFragment.newAccount)) {
        	repayDetail.addDetail(getString(R.string.boc_eloan_repaymentAccount),
                    NumberUtils.formatCardNumber(ChangeAccountResultFragment.newAccount));
        } else {
        	repayDetail.addDetail(getString(R.string.boc_eloan_repaymentAccount),
                    NumberUtils.formatCardNumber(mRayAccount));
        }

        if(bAlwaysHidePrepayBtn){
            eloanRepayLl.setVisibility(View.GONE);
        }
    }

    @Override
    public void eOverdueQueryFail(BiiResultErrorException biiResultErrorException) {

    }

    @Override
    public void eOverdueQuerySuccess(EloanOverdueModel overdueModel) {

    }

    @Override
    public void setPresenter(EloanQueryContract.Presenter presenter) {

    }

    /**
     * 还款记录点击事件
     */
    private void setPager() {
        repayDetailRow.getDetailTableRowButton().setOnclick(new DetailTableRowButton.BtnCallback() {
            @Override
            public void onClickListener() {
                //还款记录
                RepayPlanFragment repayPlan = new RepayPlanFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(EloanConst.LOAN_DRAWDETA, mDrawDetail);
                bundle.putString(EloanConst.DATA_TIME, mEndDate);
                //还款方式
                bundle.putString(EloanConst.LOAN_REPAYTYPE, DataUtils.getRepayTypeDesc(getContext(),
                        mPrepayDetailModel.getInterestType(),mDrawDetail.getLoanRepayPeriod()));
                if (mPrepayDetailModel.getOverDueStat().equals("01")
                        || mPrepayDetailModel.getOverDueStat().equals("1")) {

                    bundle.putInt(EloanConst.DEFAULT_PAGE_INDEX, 2);
                } else {
                    bundle.putInt(EloanConst.DEFAULT_PAGE_INDEX, 0);
                }
                repayPlan.setArguments(bundle);
                start(repayPlan);

            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.eloanRepayLl) {
            // 提前还款
        	 if (ApplicationContext.getInstance().getUser().getMobile() == null) {
                 showErrorDialog(getString(R.string.boc_eloan_mobile));
                 return;
             }

            if (mPrepayDetailModel != null) {
                SinglePrepaySubmitFragment submitFragment = new SinglePrepaySubmitFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(EloanConst.ELON_PREPAY, mPrepayDetailModel);
                //额度编号
                bundle.putString(EloanConst.LOAN_QUOTENO, mQuoteNo);
                //还款账号
                bundle.putString(EloanConst.PEPAY_ACCOUNT, mRayAccount);
                //当前时间
                bundle.putString(EloanConst.DATA_TIME, mEndDate);
                submitFragment.setArguments(bundle);
                start(submitFragment);
            }
        }
    }

    /**
     * 018查询交易用途详情失败
     */
    @Override
    public void eDrawRecordFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /**
     *018查询交易用途详情成功
     */
    @Override
    public void eDrawRecordSuccess(List<EloanDrawRecordModel> drawRecord) {
         if (drawRecord == null) {
             return;
         }

        if (!TextUtils.isEmpty(drawRecord.get(0).getMerchant())
                && !TextUtils.isEmpty(drawRecord.get(0).getChannel())) {
            repayDetail.addDetail(getString(R.string.boc_eloan_channel), drawRecord.get(0).getChannel());
            repayDetail.addDetail(getString(R.string.boc_eloan_merchant), drawRecord.get(0).getMerchant());
        }
    }

    public void hidePrepayBtn(){
        bAlwaysHidePrepayBtn = true;
    }

}
