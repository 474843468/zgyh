package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hcecardsetting;

import android.os.Bundle;

import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassQuotaSettingSubmit.PsnHCEQuickPassQuotaSettingSubmitResult;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.HceTransactionViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.presenter.HceQuotaSettingConfirmPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hceaddcard.HceBaseConfirmContract;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hceaddcard.HceBaseConfirmFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hcecardlist.HceCardListFragment;

import java.util.LinkedHashMap;

/**
 * Created by yangle on 2016/12/20.
 * 描述: 修改设置限额确认界面
 */
public class HceQuotaSettingConfirmFragment extends HceBaseConfirmFragment<PsnHCEQuickPassQuotaSettingSubmitResult> {


    public static HceQuotaSettingConfirmFragment newInstance(HceTransactionViewModel model) {
        HceQuotaSettingConfirmFragment fragment = new HceQuotaSettingConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_MODEL, model);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void setConfirmData() {
        mConfirmInfoView.addData(getDataMap());
    }

    @Override
    protected HceBaseConfirmContract.Present initPresenter() {
        return new HceQuotaSettingConfirmPresenter(this);
    }

    @Override
    public void onSubmitSuccess(PsnHCEQuickPassQuotaSettingSubmitResult submitResult) {
        // TODO: 2016/12/20  返回首页
        ToastUtils.show("限额设置成功");
        popTo(HceCardListFragment.class,true);
    }

    private LinkedHashMap<String, ? extends CharSequence> getDataMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        // 修改确认
        map.put("云闪付卡号",mViewModel.getSlaveCardNo());
        map.put("实体卡号",mViewModel.getMasterCardNo());
        map.put("单笔交易限额", mViewModel.getSingleQuota());
        boolean isCredit = mViewModel.getCardBrandModel().isCredit();
        if (!isCredit) {
            map.put("日交易限额", mViewModel.getPerDayQuota());
        }
        return map;
    }

}
