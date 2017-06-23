package com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.ui.overview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailRow;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SmsVerifyView;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.model.VirtualBillModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.model.VirtualCardModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.presenter.VirCardPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.presenter.VirtualCardContract;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.ui.VirtualBillItemView;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.ui.limit.VirtualUpdateLimitFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import java.util.List;

/**
 * @author wangyang
 *         16/9/3 00:07
 *         虚拟银行卡详情
 */
@SuppressLint("ValidFragment")
public class VirtualCardDetailFragment extends BaseAccountFragment<VirCardPresenter> implements VirtualCardContract.VirBillView, View.OnClickListener, SmsVerifyView.SmsActionListener {

    private DetailRow dtrValidDate, dtrSingleLimit, dtrTotalLimit, dtrATotalLimit;

    private Button btnLimit;

    private SmsVerifyView cbPassword;

    private VirtualBillItemView llBill;

    private TextView tvBill;

    private LinearLayout llHadBill;

    private VirtualCardModel model;

    public VirtualCardDetailFragment(VirtualCardModel model) {
        this.model = model;
    }

    @Override
    protected VirCardPresenter initPresenter() {
        return new VirCardPresenter(this);
    }

    @Override
    protected String getTitleValue() {
        return NumberUtils.formatCardNumber(model.getAccountIbkNum());
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return true;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_virtual_detail, null);
    }

    @Override
    public void initView() {
        dtrValidDate = (DetailRow) mContentView.findViewById(R.id.dtr_valid);
        dtrSingleLimit = (DetailRow) mContentView.findViewById(R.id.dtr_single_limit);
        dtrTotalLimit = (DetailRow) mContentView.findViewById(R.id.dtr_total_limit);
        dtrATotalLimit = (DetailRow) mContentView.findViewById(R.id.dtr_atotal_limit);

        btnLimit = (Button) mContentView.findViewById(R.id.btn_limit);
        cbPassword = (SmsVerifyView) mContentView.findViewById(R.id.cb_password);

        llBill = (VirtualBillItemView) mContentView.findViewById(R.id.ll_bill);
        tvBill = (TextView) mContentView.findViewById(R.id.tv_bill);
        llHadBill = (LinearLayout) mContentView.findViewById(R.id.ll_had_bill);
    }

    @Override
    public void setListener() {
        btnLimit.setOnClickListener(this);
        cbPassword.setOnSmsActionListener(this);
    }

    @Override
    public void initData() {
        dtrValidDate.updateValue(model.getEndDate().format(DateFormatters.dateFormatter1));
        dtrSingleLimit.updateValue(PublicCodeUtils.getCurrency(mContext, model.getCurrencyCode()) + " " + MoneyUtils.transMoneyFormat(model.getSignleLimit(), model.getCurrencyCode()));
        dtrTotalLimit.updateValue(PublicCodeUtils.getCurrency(mContext, model.getCurrencyCode()) + " " + MoneyUtils.transMoneyFormat(model.getTotalLimit(), model.getCurrencyCode()));
        dtrATotalLimit.updateValue(PublicCodeUtils.getCurrency(mContext, model.getCurrencyCode()) + " " + MoneyUtils.transMoneyFormat(model.getAtotalLimit(), model.getCurrencyCode()));

        showLoadingDialog();
        getPresenter().psnCrcdVirtualCardUnsettledbillSum(model);
    }

    @Override
    public void onClick(View v) {
        if (v == btnLimit)
            goUpdateLimit();
        else if (v == llBill)
            start(new VirtualUnsettledBillFragment(model, llBill.getData()));

    }

    @Override
    public void sendSms() {
        getPresenter().psnCrcdVirtualCardSendMessage(model);
    }

    @Override
    public void onSmsReceived(String code) {

    }

    @Override
    protected void titleRightIconClick() {
        start(new MoreAccountFragment(model));
    }

    /**
     * 跳转修改限额界面
     *
     * @author wangyang
     * @time 16/9/5 18:22
     */
    private void goUpdateLimit() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_BEAN, model);
        VirtualUpdateLimitFragment fragment = new VirtualUpdateLimitFragment();
        fragment.setArguments(bundle);
        start(fragment);
    }

    @Override
    public void queryUnsettledbillSum(List<VirtualBillModel> models) {
        if (models == null)
            return;

        //设置未出账单
        llBill.setVisibility(View.VISIBLE);
        llBill.setOnClickListener(this);
        llBill.setData(models);
    }

    @Override
    public void querySettledbill(List<List<VirtualBillModel>> models) {
        closeProgressDialog();
        if(!isCurrentFragment())
            return;

        tvBill.setVisibility(View.VISIBLE);
        if (models == null || models.isEmpty()) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.boc_space_between_200px));
            tvBill.setLayoutParams(params);
            tvBill.setGravity(Gravity.CENTER);
            tvBill.setText(getString(R.string.boc_virtual_account_detail_had_bill_no_data));
        } else{
            tvBill.setText(getString(R.string.boc_virtual_account_detail_had_bill));
            initHadBill(models);
        }
    }

    private void initHadBill(List<List<VirtualBillModel>> models) {
        llHadBill.setVisibility(View.VISIBLE);
        for (int i=0;i<models.size();i++){
            VirtualBillItemView itemView = new VirtualBillItemView(mContext);

            final List<VirtualBillModel> billModel = models.get(i);

            if (i == 0)
                itemView.setData(billModel,true);
            else
                itemView.setData(billModel,false);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    start(new VirtualSettledBillFragment(model.getAccountIbkNum(), billModel));
                }
            });

            llHadBill.addView(itemView);
            View.inflate(mContext,R.layout.boc_divide_line,llHadBill);
        }
    }
}
