package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hceunlosscard;

import android.os.Bundle;

import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassLiftLoseSubmit.PsnHCEQuickPassLiftLoseSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassQuotaSettingSubmit.PsnHCEQuickPassQuotaSettingSubmitResult;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.HceTransactionViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.hcecardlist.HceCardListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.presenter.HceCardUnlossPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.presenter.HceQuotaSettingConfirmPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hceaddcard.HceAddNewCarFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hceaddcard.HceBaseConfirmContract;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hceaddcard.HceBaseConfirmFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hceaddcard.HceResultFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hcecardlist.HceCardListFragment;

import java.util.LinkedHashMap;

/**
 * Created by gengjunying on 2016/12/21.
 * 描述: 解挂确认界面
 */
public class HceCardUnlossConfirmFragment extends HceBaseConfirmFragment<PsnHCEQuickPassLiftLoseSubmitResult> {

    public static HceCardUnlossConfirmFragment newInstance(HceTransactionViewModel model) {
        HceCardUnlossConfirmFragment fragment = new HceCardUnlossConfirmFragment();
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
        return new HceCardUnlossPresenter(this);
    }


    private LinkedHashMap<String, ? extends CharSequence> getDataMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        // 修改确认
        map.put("e闪付卡号",mViewModel.getSlaveCardNo());
        map.put("实体卡号",mViewModel.getMasterCardNo());
        return map;
    }


    @Override
    protected String getTitleValue() {
        return "解挂确认";
    }

    @Override
    public void onSubmitSuccess(PsnHCEQuickPassLiftLoseSubmitResult submitResult) {
        start(new HceCardListFragment());
    }
}
