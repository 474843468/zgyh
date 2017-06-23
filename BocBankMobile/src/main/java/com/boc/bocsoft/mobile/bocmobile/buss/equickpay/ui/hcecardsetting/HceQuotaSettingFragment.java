package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hcecardsetting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.HceTransactionViewModel;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;


/**
 * Created by yangle on 2016/12/14.
 * 描述:单笔交易限额
 */
public class HceQuotaSettingFragment extends BussFragment implements View.OnClickListener, EditMoneyInputWidget.RightTextClickListener {

    public static String MODEL_KEY = "model_key";

    private View mRootView;
    private EditMoneyInputWidget mSingleQuotaWidget;
    private Button mBtnNext;
    private TextView mTextContent;

    private HceTransactionViewModel mModel;

    public static HceQuotaSettingFragment newInstance(HceTransactionViewModel viewModel) {
        HceQuotaSettingFragment fragment = new HceQuotaSettingFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MODEL_KEY,viewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // 获取并判断 因title需要model显示
        mModel = (HceTransactionViewModel) PublicUtils.checkNotNull(getArguments().getParcelable(MODEL_KEY),"HceTransactionViewModel == null");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mRootView =  mInflater.inflate(R.layout.boc_fragment_hce_single_quota, null);
    }

    @Override
    public void initView() {
        super.initView();
        mTextContent = (TextView) mRootView.findViewById(R.id.text_content);
        mSingleQuotaWidget = (EditMoneyInputWidget) mRootView.findViewById(R.id.quota_single);
        mSingleQuotaWidget.setContentHint("不超过1000元");
        mBtnNext = (Button) mRootView.findViewById(R.id.btn_next);
    }

    @Override
    public void initData() {
        super.initData();
        mTextContent.setText(mModel.getSlaveCardNo());
        mSingleQuotaWidget.setmContentMoneyEditText(mModel.getSingleQuota());
    }

    @Override
    public void setListener() {
        super.setListener();
        mSingleQuotaWidget.setRightTextViewOnClick(this);
        mBtnNext.setOnClickListener(this);
    }

    @Override
    protected String getTitleValue() {
        // 云闪付卡号
        return mModel.getSlaveCardNo();
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
        // 下一步
        HceTransactionViewModel updateModel = getUpdateModel();
        if (!checkSingleQuota(updateModel.getSingleQuota())) {
            return;
        }
        start(HceQuotaSettingConfirmFragment.newInstance(updateModel));
    }

    private HceTransactionViewModel getUpdateModel() {
        mModel.setSingleQuota(mSingleQuotaWidget.getContentMoney());
        return mModel;
    }

    private boolean checkSingleQuota(String quota) {
        if (quota == null || quota.isEmpty()) {
            ToastUtils.show("单笔交易限额不能为空！");
            return false;
        }

        // 边界判断 大于0,小于等于1000
        // TODO: 2017/1/3
        double singleQuota = Double.parseDouble(quota);
        if (singleQuota <= 0 || singleQuota > 1000) {
            ToastUtils.show("单笔不能为0或大于1000");
            return false;
        }
        return true;
    }

    private HceTransactionViewModel getFakeModel() {
        HceTransactionViewModel viewModel = new HceTransactionViewModel();
        viewModel.setMasterCardNo("主卡56131341213546");
        viewModel.setSlaveCardNo("e卡46132151213546");
        viewModel.setSingleQuota(mSingleQuotaWidget.getContentMoney().isEmpty()? "1000.00":mSingleQuotaWidget.getContentMoney());
        viewModel.setFrom(HceTransactionViewModel.From.CARD_LIST);
        return viewModel;
    }

    @Override
    public void onRightClick(View v) {
        // 点击取消
        pop();
    }
}
