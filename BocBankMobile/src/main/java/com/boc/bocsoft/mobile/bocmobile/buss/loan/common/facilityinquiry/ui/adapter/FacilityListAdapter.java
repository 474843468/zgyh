package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.model.FacilityInquiryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseRecycleViewAdapter;
import java.math.BigDecimal;

/**
 * Created by XieDu on 2016/7/12.
 */
public class FacilityListAdapter extends
        BaseRecycleViewAdapter<FacilityInquiryViewModel.FacilityInquiryBean, FacilityListAdapter.FacilityViewHolder> {

    public FacilityListAdapter(Context context) {
        super(context);
    }

    @Override
    public FacilityViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.boc_item_facility_inquiry, viewGroup, false);
        return new FacilityViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FacilityViewHolder facilityViewHolder, int i) {
        FacilityInquiryViewModel.FacilityInquiryBean facilityInquiryBean = getItem(i);
        if (facilityInquiryBean==null)
            return;

        String loanType =
                PublicCodeUtils.getLoanTypeName(mContext, facilityInquiryBean.getLoanType());

        String loanToDate = facilityInquiryBean.getLoanToDate() == null ? "-"
                : facilityInquiryBean.getLoanToDate().format(DateFormatters.dateFormatter1);
        BigDecimal availableQuota = facilityInquiryBean.getQuota();
        String currencyCode = facilityInquiryBean.getCurrencyCode();
        String amount = String.format("%s %s",
                DataUtils.getCurrencyDescByLetter(mContext, currencyCode),
                MoneyUtils.transMoneyFormat(availableQuota.toPlainString(), currencyCode));
        facilityViewHolder.tvType.setText(loanType);
        facilityViewHolder.tvDate.setText(loanToDate);
        facilityViewHolder.tvAmount.setText(amount);
    }

    class FacilityViewHolder extends BaseRecycleViewAdapter.BaseViewHolder {

        protected TextView tvType;
        protected TextView tvDate;
        protected TextView tvAmount;

        public FacilityViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            tvType = (TextView) itemView.findViewById(R.id.tv_type);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvAmount = (TextView) itemView.findViewById(R.id.tv_amount);
        }
    }
}
