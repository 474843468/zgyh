package com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.ui.overview;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.BaseDetailView;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.model.VirtualBillTransModel;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

/**
 * @author wangyang
 *         16/9/6 20:01
 *         交易明细界面
 */
@SuppressLint("ValidFragment")
public class VirtualTransDetailFragment extends BaseAccountFragment {

    private BaseDetailView detailView;

    private VirtualBillTransModel model;

    private String accountNumber;

    public VirtualTransDetailFragment(String accountNumber, VirtualBillTransModel model) {
        this.model = model;
        this.accountNumber = accountNumber;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_overview_detail_regular_info_title);
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_virtual_trans_detail, null);
    }

    @Override
    public void initView() {
        detailView = (BaseDetailView) mContentView.findViewById(R.id.bdv_detail);
    }

    @Override
    public void initData() {
        String title = getString(R.string.boc_virtual_account_bill_detail_out_title);
        switch (model.getDebitCreditFlag()) {
            case VirtualBillTransModel.FLAG_CRED:
                title = getString(R.string.boc_virtual_account_bill_detail_in_title);
                break;
            case VirtualBillTransModel.FLAG_NMON:
                title = getString(R.string.boc_virtual_account_bill_detail_fee_title);
                break;
        }

        detailView.updateHeadData(title + " (" + PublicCodeUtils.getCurrency(getContext(), model.getTranCurrency()) + ")", MoneyUtils.transMoneyFormat(model.getTranAmount(), model.getTranCurrency()));
        detailView.updateHeadDetail(getString(R.string.boc_virtual_account_bill_detail_date), model.getBookDate().format(DateFormatters.dateFormatter1));

        detailView.addDetailRow(getString(R.string.boc_virtual_account_bill_detail_number), NumberUtils.formatCardNumber(accountNumber));
        detailView.addDetailRow(getString(R.string.boc_virtual_account_bill_detail_type), model.getType());
        detailView.addDetailRow(getString(R.string.boc_virtual_account_bill_detail_description), model.getRemark());
        detailView.addDetailRow(getString(R.string.boc_virtual_account_bill_detail_amount), PublicCodeUtils.getCurrency(getContext(), model.getTranCurrency()) + " " + MoneyUtils.transMoneyFormat(model.getTranAmount(), model.getTranCurrency()));
        detailView.addDetailRow(getString(R.string.boc_transaction_date), model.getTransDate().format(DateFormatters.dateFormatter1));
    }
}
