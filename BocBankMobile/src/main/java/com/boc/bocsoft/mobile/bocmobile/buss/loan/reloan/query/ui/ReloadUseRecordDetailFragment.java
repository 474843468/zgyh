package com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.BaseDetailView;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.FacilityInquiryConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.ReloanQryConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.model.ReloanUseRecordsViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

/**
 * Created by taoyongzhen on 2016/10/20.
 */

public class ReloadUseRecordDetailFragment extends BussFragment {

    private View rootView;
    private BaseDetailView detailView;
    private ReloanUseRecordsViewModel.PsnLOANUseRecordsQueryBean psnLOANUseRecordsQueryBean;
    private String currencyCode = "";
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_reload_use_record_detail,null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        detailView = (BaseDetailView) rootView.findViewById(R.id.details_view);
    }

    @Override
    public void initData() {
        super.initData();
        if (getArguments() != null) {
            psnLOANUseRecordsQueryBean = getArguments().getParcelable(ReloanQryConst.KEY_RELOAD_USER_RECORD_BEAN);
            currencyCode = getArguments().getString(ReloanQryConst.KEY_CURRENCY);
            //头部信息
            String headTitle = String.format(getString(R.string.boc_loan_reloan_drawamount),
                    DataUtils.getCurrencyDescByLetter(mContext, currencyCode));
            String amount =
                    MoneyUtils.transMoneyFormat(psnLOANUseRecordsQueryBean.getLoanApplyAmount(),
                            currencyCode);
            detailView.updateHeadData(headTitle, amount);
            detailView.updateHeadDetail(getString(R.string.boc_loan_reloan_loanapplydate),
                    psnLOANUseRecordsQueryBean.getLoanApplyDate());

            //详细信息
            String applyInfo = psnLOANUseRecordsQueryBean.getMerchant();
            if(StringUtils.isEmptyOrNull(applyInfo)){
                applyInfo = "-";
            }
            String channal = psnLOANUseRecordsQueryBean.getChannel();
            if(StringUtils.isEmptyOrNull(channal)){
                channal = "-";
            }
            String applyId = psnLOANUseRecordsQueryBean.getLoanApplyId();

            detailView.addDetailRow(getString(R.string.boc_loan_reloan_applyinfo), applyInfo);
            detailView.addDetailRow(getString(R.string.boc_loan_reloan_channel), channal);
            detailView.addDetailRow(getString(R.string.boc_loan_reloan_applyid), applyId);
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
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_transfer_details_title);
    }
}
