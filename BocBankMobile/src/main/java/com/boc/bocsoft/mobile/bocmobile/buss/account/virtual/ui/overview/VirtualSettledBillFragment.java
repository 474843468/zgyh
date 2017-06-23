package com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.ui.overview;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRow;
import com.boc.bocsoft.mobile.bocmobile.base.widget.transactionlist.TransactionView;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.model.VirtualBillModel;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import java.util.List;

/**
 * @author wangyang
 *         16/9/6 19:11
 *         已出账单明细
 */
@SuppressLint("ValidFragment")
public class VirtualSettledBillFragment extends BaseAccountFragment implements View.OnClickListener, TransactionView.ClickListener {

    private ViewGroup rlLeft, rlRight;

    private View viewLeft, viewRight;

    private TextView tvLeft, tvRight;

    private DetailTableRow dtrDate, dtrPaymentDate, dtrPaymentAmount;

    private TransactionView transDetail;

    private List<VirtualBillModel> models;

    private String accountNumber;

    public VirtualSettledBillFragment(String accountNumber, List<VirtualBillModel> models) {
        super();
        this.models = models;
        this.accountNumber = accountNumber;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_virtual_account_detail_had_bill);
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_virtual_settled, null);
    }

    @Override
    public void initView() {
        viewLeft = mContentView.findViewById(R.id.view_left);
        viewRight = mContentView.findViewById(R.id.view_right);
        rlLeft = (ViewGroup) mContentView.findViewById(R.id.rl_left);
        rlRight = (ViewGroup) mContentView.findViewById(R.id.rl_right);
        tvLeft = (TextView) mContentView.findViewById(R.id.tv_left);
        tvRight = (TextView) mContentView.findViewById(R.id.tv_right);

        dtrDate = (DetailTableRow) mContentView.findViewById(R.id.dtr_date);
        dtrPaymentDate = (DetailTableRow) mContentView.findViewById(R.id.dtr_payment_date);
        dtrPaymentAmount = (DetailTableRow) mContentView.findViewById(R.id.dtr_amount);

        transDetail = (TransactionView) mContentView.findViewById(R.id.trans_detail);
    }

    @Override
    public void setListener() {
        tvRight.setOnClickListener(this);
        tvLeft.setOnClickListener(this);
        transDetail.setListener(this);
    }

    @Override
    public void initData() {
        initTitle();
        requestFocus(tvLeft);

        transDetail.setAdapter();
        initTransData();
    }

    private void initTransData() {
        VirtualBillModel billModel = models.get(0);
        if (tvRight.hasFocus()) {
            billModel = models.get(1);

            viewRight.setVisibility(View.VISIBLE);
            viewLeft.setVisibility(View.GONE);
            tvLeft.setTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
            tvRight.setTextColor(getResources().getColor(R.color.boc_text_color_red));
        } else {
            viewRight.setVisibility(View.GONE);
            viewLeft.setVisibility(View.VISIBLE);
            tvLeft.setTextColor(getResources().getColor(R.color.boc_text_color_red));
            tvRight.setTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
        }

        if (billModel.getBillDate() != null)
            dtrDate.updateValue(billModel.getBillDate().format(DateFormatters.dateFormatter1));
        else
            dtrDate.updateValue(getString(R.string.boc_zan_wu));

        if (billModel.getDueDate() != null)
            dtrPaymentDate.updateValue(billModel.getDueDate().format(DateFormatters.dateFormatter1));
        else
            dtrPaymentDate.updateValue(getString(R.string.boc_zan_wu));

        dtrPaymentAmount.updateValueAndColor(MoneyUtils.transMoneyFormat(billModel.getTotalOut(), billModel.getCurrency()), getResources().getColor(R.color.boc_text_color_red));

        transDetail.setData(ModelUtil.generateTransactionBean(getContext(), billModel.getTransModels(), false));
    }

    private void initTitle() {
        tvLeft.setText(PublicCodeUtils.getCurrency(getContext(), models.get(0).getCurrency()));
        if (models.size() > 1) {
            rlRight.setVisibility(View.VISIBLE);
            tvRight.setText(PublicCodeUtils.getCurrency(getContext(), models.get(1).getCurrency()));
        }
    }

    @Override
    public void onClick(View v) {
        initTransData();
    }

    @Override
    public void onItemClickListener(int position) {
        if (tvRight.hasFocus())
            start(new VirtualTransDetailFragment(accountNumber, models.get(1).getTransModels().get(position)));
        else
            start(new VirtualTransDetailFragment(accountNumber, models.get(0).getTransModels().get(position)));
    }
}
