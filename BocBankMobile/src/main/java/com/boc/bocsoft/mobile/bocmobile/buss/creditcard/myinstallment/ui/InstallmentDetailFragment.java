package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.SecurityViewModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.model.InstallmentRecordModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.presenter.InstallmentDetailPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.widget.DetailHeaderView;

/**
 * Created by yangle on 2016/11/21.
 * 分期明细界面
 */
public class InstallmentDetailFragment extends MvpBussFragment<InstallmentDetailPresenter> implements InstallmentDetailContract.View,View.OnClickListener {

    public static final String MODEL = "model";
    private View mRootView;
    private DetailHeaderView mDetailHead;
    private DetailContentView mDetailContent1, mDetailContent2;//分上下两部分
    private View mPayAdvance;
    private InstallmentRecordModel mModel;

    public static InstallmentDetailFragment newInstance(InstallmentRecordModel recordModel) {
        InstallmentDetailFragment detailFragment = new InstallmentDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MODEL,recordModel);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = getArguments().getParcelable(MODEL);
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_creditcard_installment_details, null);
        return mRootView;
    }

    @Override
    public void initView() {
        super.initView();
        mDetailHead = (DetailHeaderView) mRootView.findViewById(R.id.installment_detail_header);
        mDetailContent1 = (DetailContentView) mRootView.findViewById(R.id.installment_detail_content1);
        mDetailContent2 = (DetailContentView) mRootView.findViewById(R.id.installment_detail_content2);
        mDetailContent2.setVisibility(mModel.isAccomplished() ? View.GONE : View.VISIBLE);

        mPayAdvance = mRootView.findViewById(R.id.installment_pay_advance);
        mPayAdvance.setVisibility(mModel.isAccomplished() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
        mDetailHead.setTitleAndValue(getString(R.string.boc_crcd_myinstallment_cash, mModel.getCurrencyName(mContext)), mModel.getAmountMoneyFormat());
        mDetailHead.addRowDetail(getString(R.string.boc_crcd_myinstallment_instmt_count),getString(R.string.boc_crcd_myinstallment_d_count_instmt, mModel.getInstmtCount()));

        mDetailContent1.addDetail(getString(R.string.boc_crcd_myinstallment_category), mModel.getInstmtCategory());
        mDetailContent1.addDetail(getString(R.string.boc_crcd_myinstallment_creditcard_num), mModel.getCreditCardNumStr());
        mDetailContent1.addDetail(getString(R.string.boc_crcd_myinstallment_instmt_date), mModel.getInstmtDate());
        mDetailContent1.addDetail(getString(R.string.boc_crcd_myinstallment_instmt_desc), mModel.getInstmtDescription());
        mDetailContent1.addDetail(getString(R.string.boc_crcd_myinstallment_change_mode), mModel.getChargeModeName());
        mDetailContent1.addDetail(getString(R.string.boc_crcd_myinstallment_accomplished_date), getAccomplishDateString());

        mDetailContent2.addDetail(getString(R.string.boc_crcd_myinstallment_first_in_amount),mModel.getFirstInAmountMoneyFormat());
        mDetailContent2.addDetail(getString(R.string.boc_crcd_myinstallment_next_time_amount), mModel.getNextTimeAmountMoneyFormat());
        mDetailContent2.addDetail(getString(R.string.boc_crcd_myinstallment_next_income_date),  mModel.isAccomplished() ? "-" :mModel.getNextIncomeDate());
        mDetailContent2.addDetail(getString(R.string.boc_crcd_myinstallment_income_count), getString(R.string.boc_crcd_myinstallment_d_count_instmt,mModel.getIncomeTimeCount()));
        mDetailContent2.addDetail(getString(R.string.boc_crcd_myinstallment_income_amount), mModel.getIncomeAmountMoneyFormat());
        mDetailContent2.addDetail(getString(R.string.boc_crcd_myinstallment_rest_count), getString(R.string.boc_crcd_myinstallment_d_count_instmt, mModel.getRestTimeCount()));
        mDetailContent2.addDetail(getString(R.string.boc_crcd_myinstallment_rest_amount), mModel.getRestAmountMoneyFormat());
    }

    private String getAccomplishDateString() {
        String accomplishDate = "";
        if (mModel.isAccomplished()) {
            accomplishDate = mModel.getAccomplishDate();
            accomplishDate += mModel.isAdvanceAccomplished() ? "(已提前结清)" : "(已完成)";
        } else {
            accomplishDate =  getString(R.string.boc_crcd_myinstallment_unaccomplished);
        }
        return accomplishDate;
    }

    @Override
    public void setListener() {
        super.setListener();
        mPayAdvance.setOnClickListener(this);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_crcd_myinstallment_detail);
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
    public void onClick(View v) {
        getPresenter().getSecurityFactor();
    }

    @Override
    protected InstallmentDetailPresenter initPresenter() {
        return new InstallmentDetailPresenter(this);
    }

    @Override
    public void getSecurityFactorSuccess(SecurityViewModel securityModel) {
        //  跳转至提前结清入账界面
        start(PayAdvanceFragment.newInstance(mModel,securityModel));
    }

    @Override
    public void setPresenter(InstallmentDetailContract.Presenter presenter) {
    }

    @Override
    public void showLoading() {
        super.showLoadingDialog();
    }


    @Override
    public void closeLoading() {
        super.closeProgressDialog();
    }

    @Override
    public InstallmentRecordModel getViewModel() {
        return mModel;
    }
}
