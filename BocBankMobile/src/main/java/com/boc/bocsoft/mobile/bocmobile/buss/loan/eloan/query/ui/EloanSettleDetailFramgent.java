package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowButton;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.ui.RepayPlanFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.ui.ChangeAccountResultFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanAccountListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanDrawDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanDrawRecordModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanOverdueModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanRepaymentModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.presenter.EloanqueryPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.framework.utils.ResUtils;

/**
 * Created by louis.hui on 2016/7/13.
 * 已结清详情
 */
public class EloanSettleDetailFramgent extends BussFragment implements EloanQueryContract.RepayNumView {
    /**view*/
    protected View rootView;
    /**头部区域*/
    protected DetailTableHead repayDetailHead;
    /**详情单条*/
    protected DetailContentView repayDetailRow;
    /**还款记录单条*/
    protected DetailContentView repaymentRow;

    /**网络请求接口类*/
    private EloanqueryPresenter mPresenter;
    /**结清详情对象*/
    private EloanDrawDetailModel mDrawDetail;
    /**结清对象*/
    private EloanAccountListModel.PsnLOANListEQueryBean mSettleRepayModel;
    /**贷款账户*/
    private String mLoannum;
    /**还款账号*/
    private String mRayAccount;
    /**系统当前时间*/
    private String mEndDate;
    /**还款金额*/
    private String mRepayAmount;
    /**还款日期*/
    private String mRepayDate;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_settledetail_fragment, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        repayDetailHead = (DetailTableHead) rootView.findViewById(R.id.repayDetailHead);
        repayDetailRow = (DetailContentView) rootView.findViewById(R.id.repayDetailRow);

    }

    @Override
    public void initData() {
        super.initData();
        mSettleRepayModel = (EloanAccountListModel.PsnLOANListEQueryBean) getArguments().getSerializable(EloanConst.LOAN_ACCOUNT_NUM);
        //还款账户
        mRayAccount = getArguments().getString(EloanConst.PEPAY_ACCOUNT);
        //系统当前时间
        mEndDate = getArguments().getString(EloanConst.DATA_TIME);
        mPresenter = new EloanqueryPresenter(this);
        showLoadingDialog();
        //007详情
        mPresenter.queryDrawingDetail(mSettleRepayModel.getAccountNumber());


    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_settle_details);
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
    public void eDrawingDetailFail(BiiResultErrorException biiResultErrorException) {
    	closeProgressDialog();

    }

    @Override
    public void eDrawingDetailSuccess(EloanDrawDetailModel eloanDrawDetailResult) {
        if (eloanDrawDetailResult == null) {
            return;
        }
        mLoannum = eloanDrawDetailResult.getLoanAccount();
        mDrawDetail = new EloanDrawDetailModel();
        mDrawDetail = eloanDrawDetailResult;
        //请求018接口
        mPresenter.queryDraw(mSettleRepayModel.getAccountNumber(), mSettleRepayModel.getLoanDate());
        getSettleData();
        closeProgressDialog();
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
     * 018查询交易用途详情失败
     */
    @Override
    public void eDrawRecordFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /**
     * 018查询交易用途详情成功
     */
    @Override
    public void eDrawRecordSuccess(List<EloanDrawRecordModel> drawRecord) {
        if (drawRecord == null) {
            return;
        }
        if (!TextUtils.isEmpty(drawRecord.get(0).getMerchant())
                && !TextUtils.isEmpty(drawRecord.get(0).getChannel())) {
            repayDetailRow.addDetail(getString(R.string.boc_eloan_channel), drawRecord.get(0).getChannel());
            repayDetailRow.addDetail(getString(R.string.boc_eloan_merchant), drawRecord.get(0).getMerchant());
        }
    }

    /**
	 * 显示页面内容
	 */
	private void getSettleData() {

		if (mDrawDetail.getInterestType().equals("B")
                && mDrawDetail.getLoanRepayPeriod().equals("01")
                || mDrawDetail.getLoanRepayPeriod().equals("1")) {

            repayDetailRow.addDetailRow(getString(R.string.boc_details_repaytype),
                    getString(R.string.boc_details_repaytypeOne));

        } else  if(mDrawDetail.getInterestType().equals("B")
                && mDrawDetail.getLoanRepayPeriod().equals("98")) {

            repayDetailRow.addDetail(getString(R.string.boc_details_repaytype),
                    getString(R.string.boc_details_repaytypeTwo));
        }
        repayDetailHead.updateData(getString(R.string.boc_details_settletitle),
                MoneyUtils.transMoneyFormat(mDrawDetail.getLoanAmount(), "001"));
        repayDetailHead.setTableRow(getString(R.string.boc_details_Period),
        		mDrawDetail.getLoanPeriod() + "个月" + "/"
                        +mDrawDetail.getLoanRate()+ "%");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0, 0,0);
        repayDetailRow.getDetailTableRowButton().addTextBtn(getResources().getString(R.string.boc_eloan_toptiem),
                mDrawDetail.getLoanDate(), getResources().getString(R.string.boc_eloan_repayrecord), getResources().getColor(R.color.boc_main_button_color),params);

        repayDetailRow.getDetailTableRowButton().setOnclick(new DetailTableRowButton.BtnCallback() {
            @Override
            public void onClickListener() {
                if (ApplicationContext.getInstance().getUser().getMobile() == null) {
                    showErrorDialog(getString(R.string.boc_eloan_mobile));
                    return;
                }

                if (mLoannum != null) {

                    RepayPlanFragment repayPlan = new RepayPlanFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(EloanConst.LOAN_DRAWDETA, mDrawDetail);
                    bundle.putBoolean(EloanConst.LOAN_REPAYMNETHIT, true);
                    //还款方式
                    bundle.putString(EloanConst.LOAN_REPAYTYPE, DataUtils.getRepayTypeDesc(getContext(),
                            mSettleRepayModel.getInterestType(), mDrawDetail.getLoanRepayPeriod()));
                    bundle.putString(EloanConst.DATA_TIME, mEndDate);
                    bundle.putInt(EloanConst.DEFAULT_PAGE_INDEX, 1);
                    repayPlan.setArguments(bundle);
                    start(repayPlan);
                }
            }
        });
        repayDetailRow.addDetail(getString(R.string.boc_details_loanDate),
        		mDrawDetail.getLoanToDate());
        //007还款账户
        repayDetailRow.addDetail(getString(R.string.boc_eloan_repaymentAccount),
                NumberUtils.formatCardNumber(mRayAccount));

	}
}
