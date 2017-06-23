package com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableHead;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.InvalidinvestModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.InvestBuyDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.InvestSellDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.presenter.InvestMgPresenter;

/**
 * Created by huixiaobo on 2016/11/29.
 * 失效定投申请详情
 */
public class InvalidDetailFragment extends MvpBussFragment<InvestMgPresenter> implements InvestMgContract.InvestDetailView {

    protected View rootView;
    protected DetailTableHead invalidDetailHead;
    protected DetailContentView invalidDetailRow;
    protected TextView fundetailq;
    protected TextView funddetailend;
    /**失效申请对象*/
    private InvalidinvestModel.ListBean mInvalidBean;
    /**定赎详情对象*/
    private InvestSellDetailModel mSellDetail;
    /**定投详情对象*/
    private InvestBuyDetailModel mBuyDetail;
    /**详情显示组件*/
    private DetailContentView detailContent;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fund_invaliddetail, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        invalidDetailHead = (DetailTableHead) rootView.findViewById(R.id.invalidDetailHead);
        invalidDetailRow = (DetailContentView) rootView.findViewById(R.id.invalidDetailRow);
        fundetailq = (TextView) rootView.findViewById(R.id.fundetailq);
        funddetailend = (TextView) rootView.findViewById(R.id.funddetailend);
    }

    @Override
    public void initData() {
        super.initData();
        //上页面传值
        mInvalidBean = (InvalidinvestModel.ListBean) getArguments().getSerializable(InvestConst.INVALID_DETAIL);
        //定投详情
        if ("1".equals(mInvalidBean.getApplyType())) {
            getPresenter().queryInvestBuyDetail(mInvalidBean.getApplyDate(),mInvalidBean.getOrnScheduleNum());
        } //定赎详情
        else if ("2".equals(mInvalidBean.getApplyType())){
            getPresenter().queryInvestSellDetail(mInvalidBean.getApplyDate(), mInvalidBean.getOrnScheduleNum());
        }
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
    protected String getTitleValue() {
        return getString(R.string.boc_fundstate_detail);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected InvestMgPresenter initPresenter() {
        return null;
    }

    /**054接口*/
    @Override
    public void fundInvestBuyDetailFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }
    /**054接口*/
    @Override
    public void fundInvestBuyDetailSuccess(InvestBuyDetailModel buyDetail) {
        closeProgressDialog();
        if (buyDetail != null) {
            mBuyDetail = buyDetail;
        }
        setViewData();
    }
    /**055接口*/
    @Override
    public void fundInvestSellDetailFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }
    /**055接口*/
    @Override
    public void fundInvestSellDetailSuccess(InvestSellDetailModel sellDetail) {
        closeProgressDialog();
        if (sellDetail != null) {
            mSellDetail = sellDetail;
        }
        setViewData();
    }

    /**
     * 页面组件赋值
     *
     */
    private void setViewData() {
        if (mInvalidBean != null) {
            return;
        }

        invalidDetailHead.updateData(getString(R.string.boc_fund_validAmc),
                MoneyUtils.transMoneyFormat(mInvalidBean.getTransCount(), mInvalidBean.getCurrencyCode()));
        invalidDetailHead.setTableRow(getString(R.string.boc_fundInvalid_date), mInvalidBean.getApplyDate());

        invalidDetailRow.addDetail(getString(R.string.boc_fundDetail_name),
                mInvalidBean.getFundName() + mInvalidBean.getFundCode());
        invalidDetailRow.addDetail(getString(R.string.boc_fundInvalid_stutes), mInvalidBean.getStatus());
        invalidDetailRow.addDetail(getString(R.string.boc_fundDetail_transtype), mInvalidBean.getApplyType());
        invalidDetailRow.addDetail(getString(R.string.boc_fundDetail_transz), mInvalidBean.getTransCycle());
        invalidDetailRow.addDetail(getString(R.string.boc_fundDetail_transdate),
                mInvalidBean.getTransDate());
        fundetailq.setText(getString(R.string.boc_fundInvalid_detail));
        detailContent = new DetailContentView(getContext());
        if ("1".equals(mInvalidBean.getApplyType())) {
            detailContent.addDetail(getString(R.string.boc_fundDetail_transnum), mBuyDetail.getEndSum());
            detailContent.addDetail(getString(R.string.boc_fundDetail_transAmc), mBuyDetail.getEndAmt());
        } else if ("2".equals(mInvalidBean.getApplyType())) {
            detailContent.addDetail(getString(R.string.boc_fundDetail_transnum), mSellDetail.getEndSum());
            detailContent.addDetail(getString(R.string.boc_fundDetail_transAmc), mSellDetail.getEndCount());
        }
        funddetailend.setText(getString(R.string.boc_fundInvalid_detailend));
        getFundDetailEnd();
    }
    /**结束条件赋值*/
    private void getFundDetailEnd() {
        if (mInvalidBean == null) {
            return;
        }
        if ("1".equals(mInvalidBean.getEndFlag())) {
            detailContent.addDetail(getString(R.string.boc_fundDetail_endDate), mInvalidBean.getEndDate());
        } else if ("2".equals(mInvalidBean.getEndFlag())) {
            detailContent.addDetail(getString(R.string.boc_fundDetail_endnum), mInvalidBean.getEndSum());
        }else if ("3".equals(mInvalidBean.getEndFlag())){
            detailContent.addDetail(getString(R.string.boc_fundDetail_endAmt), mInvalidBean.getEndCount());
        }
    }


    @Override
    public void setPresenter(InvestMgContract.Presenter presenter) {

    }
}
