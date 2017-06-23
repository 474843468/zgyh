package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.ResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.model.InstallmentRecordModel;

import java.util.LinkedHashMap;

/**
 * Created by yangle on 2016/11/21.
 * 提前结清结果界面
 */
public class PayAdvanceResultFragment extends BussFragment implements BaseResultView.HomeBackListener {

    private static final String MODEL = "model";
    private View mView;
    private BaseResultView mResultView;
    private InstallmentRecordModel mModel;

    public static PayAdvanceResultFragment newInstance(InstallmentRecordModel recordModel) {
        PayAdvanceResultFragment resultFragment = new PayAdvanceResultFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MODEL,recordModel);
        resultFragment.setArguments(bundle);
        return resultFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = getArguments().getParcelable(MODEL);
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mView = mInflater.inflate(R.layout.boc_fragment_creditcard_payadvance_result, null);
        return mView;
    }

    @Override
    public void initView() {
        super.initView();
        mResultView = (BaseResultView) mView.findViewById(R.id.borv_result_view);
    }

    @Override
    public void initData() {
        super.initData();
        // 设置头部
        mResultView.addStatus(ResultHead.Status.SUCCESS, getString(R.string.boc_crcd_myinstallment_pay_advance_success));
        mResultView.addTitle(getString(R.string.boc_crcd_myinstallment_result_title,mModel.getCurrencyName(mContext),mModel.getRestAmountMoneyFormat()));
        // 设置祥情
        mResultView.updateDetail(getString(R.string.boc_crcd_myinstallment_see_details));
        mResultView.addDetail(getDetailMap(mModel));
    }

    private LinkedHashMap<String,String> getDetailMap(InstallmentRecordModel model) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(getString(R.string.boc_crcd_myinstallment_amount), mModel.getCurrencyName(mContext) + mModel.getAmountMoneyFormat());
        map.put(getString(R.string.boc_crcd_myinstallment_instmt_count), getString(R.string.boc_crcd_myinstallment_d_count_instmt, mModel.getInstmtCount()));
        map.put(getString(R.string.boc_crcd_myinstallment_creditcard_num), mModel.getCreditCardNumStr());
        map.put(getString(R.string.boc_crcd_myinstallment_instmt_date), mModel.getInstmtDate());
        map.put(getString(R.string.boc_crcd_myinstallment_instmt_desc), mModel.getInstmtDescription());
        map.put(getString(R.string.boc_crcd_myinstallment_change_mode), mModel.getChargeModeName());
        return map;
    }

    @Override
    public void setListener() {
        super.setListener();
        mResultView.setOnHomeBackClick(this);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_crcd_myinstallment_result);
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
    public void onHomeBack() {
        mActivity.finish();
    }

    @Override
    protected void titleLeftIconClick() {
        popToAndReInit(InstallmentHistoryFragment.class);
    }

    @Override
    public boolean onBackPress() {
        popToAndReInit(InstallmentHistoryFragment.class);
        return true;
    }

}
