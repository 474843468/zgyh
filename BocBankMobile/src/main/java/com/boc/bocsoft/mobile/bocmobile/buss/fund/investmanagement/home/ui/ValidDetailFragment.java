package com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.cancelorder.ui.InvestInfoFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.cancelorder.ui.RedeemInfoFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.InvestBuyDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.InvestParams;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.InvestSellDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.InvestpsRsModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.RedeemParams;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.RedeempsRsModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.ValidinvestModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.presenter.InvestMgPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.ui.InvestUpdateFragment;

/**
 * Created by huixiaobo on 2016/11/29.
 * 有效定投申请详情
 */
public class ValidDetailFragment extends MvpBussFragment<InvestMgPresenter> implements
        InvestMgContract.InvestDetailView, InvestMgContract.InvestPauseResumeView,View.OnClickListener,TitleAndBtnDialog.DialogBtnClickCallBack {

    protected View rootView;
    protected DetailTableHead validDetailHead;
    protected DetailContentView validDetailRow;
    protected TextView fundetailq;
    protected TextView funddetailend;
    protected TextView fundStop;
    protected TextView fundCancelorder;
    protected TextView fundUpdate;
    private TextView fundCancel;
    private TextView fundStart;
    /**有效传值对象*/
    private ValidinvestModel.ListBean validData;
    /**定申详情对象*/
    private InvestBuyDetailModel mBuyDetail;
    /**定赎详情对象*/
    private InvestSellDetailModel mSellDetail;
    /**详情显示组件*/
    private DetailContentView detailContent;
    /**有效详底部情按钮*/
    private LinearLayout funddetailbut;
    /**暂停底部按钮*/
    private LinearLayout fundbntPause;
    /**暂停/恢复弹出框*/
    private TitleAndBtnDialog pauseDialog;
    /**暂停标识符*/
    private boolean isPause;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fund_validdetail, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        validDetailHead = (DetailTableHead) rootView.findViewById(R.id.validDetailHead);
        validDetailRow = (DetailContentView) rootView.findViewById(R.id.validDetailRow);
        fundetailq = (TextView) rootView.findViewById(R.id.fundetailq);
        funddetailend = (TextView) rootView.findViewById(R.id.funddetailend);
        fundStop = (TextView) rootView.findViewById(R.id.fundStop);
        fundCancelorder = (TextView) rootView.findViewById(R.id.fundCancelorder);
        fundUpdate = (TextView) rootView.findViewById(R.id.fundUpdate);
        funddetailbut = (LinearLayout) rootView.findViewById(R.id.funddetailbut);
        fundbntPause = (LinearLayout) rootView.findViewById(R.id.fundbntPause);
        fundCancel = (TextView) rootView.findViewById(R.id.fundCancel);
        fundStart = (TextView) rootView.findViewById(R.id.fundStart);
        fundStop.setOnClickListener(this);
        fundCancelorder.setOnClickListener(this);
        fundUpdate.setOnClickListener(this);
        fundCancel.setOnClickListener(this);
        fundStart.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        //上页面传值对象
        validData = (ValidinvestModel.ListBean) getArguments().getSerializable(InvestConst.VALID_DETAIL);
        if (validData == null) {
            return;
        }
        showLoadingDialog();
        if ("0".equals(validData.getTransType())) {
            getPresenter().queryInvestBuyDetail(validData.getApplyDate(),validData.getFundSeq());
        } else if ("1".equals(validData.getTransType())){
            getPresenter().queryInvestSellDetail(validData.getApplyDate(),validData.getFundSeq());
        }

        if ("0".equals(validData.getRecordStatus())) {
            isPause = true;
            funddetailbut.setVisibility(View.VISIBLE);
            fundbntPause.setVisibility(View.GONE);
        } else if ("1".equals(validData.getRecordStatus())) {
            isPause = false;
            funddetailbut.setVisibility(View.GONE);
            fundbntPause.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setListener() {
        super.setListener();
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return true;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_fundstate_detail);
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
    protected InvestMgPresenter initPresenter() {
        return new InvestMgPresenter(this,this);
    }

    /**054接口返回失败*/
    @Override
    public void fundInvestBuyDetailFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /**054接口返回数据*/
    @Override
    public void fundInvestBuyDetailSuccess(InvestBuyDetailModel buyDetail) {
        closeProgressDialog();
        if (buyDetail != null) {
            mBuyDetail = buyDetail;
        }
        //页面组件赋值
        setViewData();
    }

    /**055接口返回数据*/
    @Override
    public void fundInvestSellDetailFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }
    /**055接口返回数据*/
    @Override
    public void fundInvestSellDetailSuccess(InvestSellDetailModel sellDetail) {
        closeProgressDialog();
        if (sellDetail != null) {
            mSellDetail = sellDetail;
        }
        //页面组件赋值
        setViewData();
    }

    private void setViewData() {
        if (validData == null) {
            return;
        }
        validDetailHead.updateData(getString(R.string.boc_fund_validAmc),
                MoneyUtils.transMoneyFormat(validData.getApplyAmount(), validData.getCurrency()));
        validDetailHead.setTableRow(getString(R.string.boc_fund_validApplydate), validData.getApplyDate());
        validDetailRow.addDetail(getString(R.string.boc_fundDetail_name), validData.getFundName() + validData.getFundCode());
        validDetailRow.addDetail(getString(R.string.boc_fundDetail_transtutes), validData.getRecordStatus());
        validDetailRow.addDetail(getString(R.string.boc_fundDetail_transtype), validData.getTransType());
        validDetailRow.addDetail(getString(R.string.boc_fundDetail_transz), validData.getDtdsFlag());
        validDetailRow.addDetail(getString(R.string.boc_fundDetail_transdate), validData.getSubDate());
        fundetailq.setText(getString(R.string.boc_fundInvalid_detail));
        detailContent = new DetailContentView(getContext());
        if ("0".equals(validData.getTransType())) {
            detailContent.addDetail(getString(R.string.boc_fundDetail_transnum), mBuyDetail.getEndSum());
            detailContent.addDetail(getString(R.string.boc_fundDetail_transAmc), mBuyDetail.getEndAmt());
        } else if ("1".equals(validData.getTransType())) {
            detailContent.addDetail(getString(R.string.boc_fundDetail_transnum), mSellDetail.getEndSum());
            detailContent.addDetail(getString(R.string.boc_fundDetail_transAmc), mSellDetail.getEndCount());
        }
        funddetailend.setText(getString(R.string.boc_fundInvalid_detailend));
        getFundDetailEnd();
    }

    /**
     * 页面显示
     */
    private void getFundDetailEnd() {
        if (validData == null) {
            return;
        }
        if ("1".equals(validData.getEndFlag())) {
            detailContent.addDetail(getString(R.string.boc_fundDetail_endDate),validData.getApplyDate());
        } else if ("2".equals(validData.getEndFlag())) {
            detailContent.addDetail(getString(R.string.boc_fundDetail_endnum),validData.getEndSum());
        }else if ("3".equals(validData.getEndFlag())){
            detailContent.addDetail(getString(R.string.boc_fundDetail_endAmt),validData.getEndAmt()+"");
        }
    }


    @Override
    public void setPresenter(InvestMgContract.Presenter presenter) {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.fundStop) {
            isPause = true;
            getPauseDialog();
        } else if (i == R.id.fundCancelorder) {
            fundCancelStart();
        } else if (i == R.id.fundUpdate) {
            fundUpdateStrat();
        } else if (i == R.id.fundCancel) {
            fundCancelStart();
        } else if (i == R.id.fundStart) {
            isPause = false;
            getPauseDialog();
        }
    }

    /**
     * 定投/定赎撤销跳转
     */
    private void fundCancelStart() {
        Bundle bundle = new Bundle();
        if ("0".equals(validData.getTransType())) {
            InvestInfoFragment infoFragment = new InvestInfoFragment();
            //详情对象
            bundle.putSerializable(InvestConst.INVEST_BUY, mBuyDetail);
           //基金申请日期
            bundle.putString(InvestConst.INVEST_FUNDDATE, validData.getApplyDate());
            //基金序号
            bundle.putString(InvestConst.INVEST_FUNDSEQ, validData.getFundSeq());
            infoFragment.setArguments(bundle);
            start(infoFragment);
        } else if ("1".equals(validData.getTransType())) {
            RedeemInfoFragment infoFragment = new RedeemInfoFragment();
            bundle.putSerializable(InvestConst.INVEST_SELL, mSellDetail);
            bundle.putString(InvestConst.INVEST_FUNDDATE, validData.getApplyDate());
            bundle.putString(InvestConst.INVEST_FUNDSEQ, validData.getFundSeq());
            infoFragment.setArguments(bundle);
            start(infoFragment);
        }
    }

    /**定申/定赎修改跳转页面*/
    private void fundUpdateStrat() {
        InvestUpdateFragment investUpdate = new InvestUpdateFragment();
        Bundle bundle = new Bundle();
        bundle.putString(InvestConst.TRANS_TYPE, validData.getTransType());
        validData.setInvestUpdate(true);
        bundle.putSerializable(InvestConst.VALID_DETAIL, validData);
        investUpdate.setArguments(bundle);
        start(investUpdate);
    }

    /**
     * 定投暂停/恢复网络请求
     */
    private void investPauseResume() {
        InvestParams investParams = new InvestParams();
        investParams.setScheduleBuyDate(validData.getApplyDate());
        investParams.setFundCode(validData.getFundCode());
        investParams.setScheduleBuyNum(validData.getFundSeq());
        if (isPause) {
            investParams.setFundTransFlag("1");
        } else {
            investParams.setFundTransFlag("0");
        }
        showLoadingDialog();
        //定投暂停恢复网络请求
        getPresenter().queryInvestPauseResume(investParams);

    }

    /**
     * 定赎暂停/恢复网络请求
     */
    private void redeemPauseResume() {
        RedeemParams redeemParams =new RedeemParams();
        redeemParams.setFundCode(validData.getFundCode());
        redeemParams.setScheduleSellDate(validData.getApplyDate());
        redeemParams.setScheduleSellNum(validData.getFundSeq());
        if (isPause) {
            redeemParams.setFundTransFlag("1");
        } else {
            redeemParams.setFundTransFlag("0");
        }
        showLoadingDialog();
        //定赎暂停/恢复网络请求
        getPresenter().queryRedeemPauseResume(redeemParams);

    }
    /**
     * 暂停/恢复弹出框格式
     */
    private void getPauseDialog() {
        if (pauseDialog == null) {
            pauseDialog = new TitleAndBtnDialog(getActivity());
            pauseDialog.setBtnName(new String[]{getString(R.string.boc_fundInvest_leftbut),getString(R.string.boc_fundInest_right)});
            pauseDialog.isShowTitle(false);
            if ("0".equals(validData.getTransType())) {
                if (isPause) {
                    pauseDialog.setNoticeContent(getString(R.string.boc_fundInvest_pausedialog) +"“"+validData.getFundName()+"”" +"?");
                } else {
                    pauseDialog.setNoticeContent(getString(R.string.boc_fundInvest_restdialog) + "“"+ validData.getFundName()+"”" +"?");
                }
            } else if ("1".equals(validData.getTransType())) {
                if (isPause) {
                    pauseDialog.setNoticeContent(getString(R.string.boc_fundredeem_pausedialog) + "“"+validData.getFundName()+"”" +"?");
                } else {
                    pauseDialog.setNoticeContent(getString(R.string.boc_fundredeem_resdialog) + "“"+validData.getFundName()+"”" +"?");
                }
            }
            pauseDialog.setLeftBtnTextBgColor(getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_common_cell_color));
            pauseDialog.setRightBtnTextBgColor(getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_text_color_red));
            pauseDialog.setDialogBtnClickListener(this);
        }
        pauseDialog.show();
    }

    /**056接口定投暂停/恢复失败*/
    @Override
    public void fundInvestPauseResumeFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /**056接口定投暂停/恢复成功*/
    @Override
    public void fundInvestPauseResumeSuccess(InvestpsRsModel insRsModel) {
        closeProgressDialog();
        if (insRsModel == null) {
            return;
        }
        if (isPause) {
            fundbntPause.setVisibility(View.VISIBLE);
            funddetailbut.setVisibility(View.GONE);
        } else {
            fundbntPause.setVisibility(View.GONE);
            funddetailbut.setVisibility(View.VISIBLE);
        }

    }
    /**057接口定赎暂停/恢复失败*/
    @Override
    public void fundRedeemPauseResumeFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }
    /**057接口定赎暂停/恢复成功*/
    @Override
    public void fundRedeemPauseResumeSuccess(RedeempsRsModel rsModel) {
        closeProgressDialog();
        if(rsModel== null) {
            return;
        }
        if (isPause) {
            fundbntPause.setVisibility(View.VISIBLE);
            funddetailbut.setVisibility(View.GONE);
        } else {
            fundbntPause.setVisibility(View.GONE);
            funddetailbut.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLeftBtnClick(View view) {
        pauseDialog.dismiss();
    }

    @Override
    public void onRightBtnClick(View view) {

        if ("0".equals(validData.getTransType())) {
            investPauseResume();
        } else if ("1".equals(validData.getTransType())) {
            redeemPauseResume();
        }
        pauseDialog.dismiss();
    }
}
