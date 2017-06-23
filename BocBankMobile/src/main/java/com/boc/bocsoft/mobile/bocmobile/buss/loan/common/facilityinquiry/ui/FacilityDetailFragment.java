package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.BaseDetailView;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.FacilityInquiryConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.model.FacilityInquiryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

/**
 * Created by XieDu on 2016/7/12.
 */
public class FacilityDetailFragment extends BussFragment{
    /**
     * 根布局
     */
    private View rootView;
    private BaseDetailView detailView;
    private Button btnUsedRecord = null; //额度使用记录按钮

    private FacilityInquiryViewModel.FacilityInquiryBean mFacilityInquiryBean;

    public static FacilityDetailFragment newInstance(
            FacilityInquiryViewModel.FacilityInquiryBean bean) {
        FacilityDetailFragment fragment = new FacilityDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(FacilityInquiryConst.KEY_FACILITY_INQUIRY_BEAN, bean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.fragment_facility_detail, null);
        return rootView;
    }

    @Override
    public void initView() {
        detailView = (BaseDetailView) rootView.findViewById(R.id.details_view);
        btnUsedRecord = (Button)rootView.findViewById(R.id.btn_used_record);
    }

    @Override
    public void initData() {
        if (getArguments() != null) {
            mFacilityInquiryBean = getArguments().getParcelable(FacilityInquiryConst.KEY_FACILITY_INQUIRY_BEAN);

            //头部信息
            String headTitle = String.format(getString(R.string.boc_facility_detail_amount_title),
                    DataUtils.getCurrencyDescByLetter(mContext, mFacilityInquiryBean.getCurrencyCode()));
            String amount =
                    MoneyUtils.transMoneyFormat(mFacilityInquiryBean.getQuota().toPlainString(),
                            mFacilityInquiryBean.getCurrencyCode());
            detailView.updateHeadData(headTitle, amount);
            detailView.updateHeadDetail(getString(R.string.boc_facility_to_date),
                    mFacilityInquiryBean.getLoanToDate().format(DateFormatters.dateFormatter1));

            //详细信息
            String loanType =
                    PublicCodeUtils.getLoanTypeName(mContext, mFacilityInquiryBean.getLoanType());
            String loanToDate = mFacilityInquiryBean.getLoanToDate() == null ? "-"
                    : mFacilityInquiryBean.getLoanToDate().format(DateFormatters.dateFormatter1);
            String number = mFacilityInquiryBean.getQuotaNumber();
            String availableQuota =
                    (mFacilityInquiryBean.getAvailableQuota() == null || StringUtils.isEmpty(
                            mFacilityInquiryBean.getCurrencyCode())) ? "-"
                            : MoneyUtils.transMoneyFormat(
                                    mFacilityInquiryBean.getAvailableQuota().toPlainString(),
                                    mFacilityInquiryBean.getCurrencyCode());
            String usedQuota = (mFacilityInquiryBean.getQuotaUsed() == null || StringUtils.isEmpty(
                    mFacilityInquiryBean.getCurrencyCode())) ? "-" : MoneyUtils.transMoneyFormat(
                    mFacilityInquiryBean.getQuotaUsed().toPlainString(),
                    mFacilityInquiryBean.getCurrencyCode());
            String quotaStatus = PublicCodeUtils.getFacilityStatus(mContext,
                    mFacilityInquiryBean.getQuotaStatus());
            detailView.addDetailRow(getString(R.string.boc_facility_name), loanType);

            if(!StringUtils.isEmptyOrNull(number)){
                if(number.length() >= 8){
                    number = NumberUtils.formatCardNumberStrong(number);
                }
                detailView.addDetailRow(getString(R.string.boc_quote_No), number);
            }

            detailView.addDetailRow(getString(R.string.boc_facility_quota_used), usedQuota);
            detailView.addDetailRow(getString(R.string.boc_facility_available_quota),
                    availableQuota);
            detailView.addDetailRow(getString(R.string.boc_facility_status), quotaStatus);
            detailView.isShowBottomView(true);
        }
    }

    @Override
    public void setListener() {
        btnUsedRecord.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(FacilityInquiryConst.KEY_FACILITY_INQUIRY_BEAN,
                        mFacilityInquiryBean);
                FacilityUseRecFragment fragment = new FacilityUseRecFragment();
                fragment.setArguments(bundle);
                start(fragment);
            }
        });
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
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_transfer_details_title);
    }
}
