package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hceaddcard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.ResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.HceTransactionViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hcecardlist.HceCardListFragment;

import java.util.LinkedHashMap;

/**
 * Created by yangle on 2016/12/14.
 * 描述:结果界面
 */
public class HceResultFragment extends BussFragment implements  BaseResultView.HomeBackListener {

    private static final String KEY_MODEL = "key_model";

    public BaseResultView mResultView;
    private HceTransactionViewModel mViewModel;
    public static HceResultFragment newInstance(HceTransactionViewModel model) {
        HceResultFragment resultFragment = new HceResultFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_MODEL,model);
        resultFragment.setArguments(bundle);
        return resultFragment;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mResultView = new BaseResultView(mContext);
    }

    @Override
    public void initData() {
       super.initData();
        mViewModel = getArguments().getParcelable(KEY_MODEL);
        // 设置头部
        mResultView.addStatus(ResultHead.Status.SUCCESS, "开通成功");
        mResultView.addTitle(getResultTitle());
        // 设置祥情
        mResultView.addTopDetail(getDetailMap(mViewModel));
    }

    @Nullable
    private String getResultTitle() {
        String resultTitle = null;
        if (mViewModel.getFrom() == HceTransactionViewModel.From.APPLY) {
            resultTitle = getString(R.string.boc_hce_result_fragment_title_2,mViewModel.getSlaveCardNo());
        } else if (mViewModel.getFrom() == HceTransactionViewModel.From.CARD_LIST){
            resultTitle =  getString(R.string.boc_hce_result_fragment_title,mViewModel.getSlaveCardNo());
        }

        switch (mViewModel.getCardBrandModel().getAppType()) {
                case MasterCard:
                    resultTitle += getString(R.string.boc_hce_result_master_use);
                    break;
                case VISA:
                    resultTitle += getString(R.string.boc_hce_result_visa_use);
                    break;
                case PBOC_Credit:
                case PBOC_Debit:
                    resultTitle += getString(R.string.boc_hce_result_pboc_use);
                    break;
                default:
        }
        return resultTitle;
    }

    private LinkedHashMap<String, String> getDetailMap(HceTransactionViewModel model) {
        // TODO: 2016/12/16
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("e闪付卡号", model.getSlaveCardNo());
        map.put("单笔交易限额", model.getSingleQuota());
        map.put("交易日限额", model.getPerDayQuota());
        return map;
    }

    @Override
    public void setListener() {
        super.setListener();
        mResultView.setOnHomeBackClick(this);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_account_loss_success_title);
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
        Toast.makeText(mActivity, "返回首页", Toast.LENGTH_SHORT).show();
        ModuleActivityDispatcher.popToHomePage();
    }

    @Override
    protected void titleLeftIconClick() {
        popToAndReInit(HceCardListFragment.class);
    }

    @Override
    public boolean onBackPress() {
        popToAndReInit(HceCardListFragment.class);
        return true;
    }

}
