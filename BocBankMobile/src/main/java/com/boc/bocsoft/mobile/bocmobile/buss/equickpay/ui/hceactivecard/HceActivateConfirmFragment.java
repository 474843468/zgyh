package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hceactivecard;

import android.os.Bundle;

import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassActivationSubmit.PsnHCEQuickPassActivationSubmitResult;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.HceTransactionViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.presenter.HceActivateConfirmPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hceaddcard.HceBaseConfirmContract;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hceaddcard.HceBaseConfirmFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hceaddcard.HceResultFragment;

import java.util.LinkedHashMap;

/**
 * Created by yangle on 2016/12/19.
 * 描述:申请确认界面与激活界面
 */
public class HceActivateConfirmFragment extends HceBaseConfirmFragment<PsnHCEQuickPassActivationSubmitResult> {


    public static HceActivateConfirmFragment newInstance(HceTransactionViewModel model) {
        HceActivateConfirmFragment fragment = new HceActivateConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_MODEL, model);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void setConfirmData() {
        mConfirmInfoView.addData(getDataMap());

        // 贷记卡可通过电话激活
        if (!mViewModel.getCardBrandModel().isCredit()) {
            mConfirmInfoView.setBottomHint("你也可通过8886326161进行激活");
        }
    }

    @Override
    protected HceBaseConfirmContract.Present initPresenter() {
        return new HceActivateConfirmPresenter(this);
    }

    @Override
    public void onSubmitSuccess(PsnHCEQuickPassActivationSubmitResult submitResult) {
        // 跳转至结果界面
        start(HceResultFragment.newInstance(mViewModel));
    }

    private LinkedHashMap<String, ? extends CharSequence> getDataMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("云闪付卡", mViewModel.getSlaveCardNo());
        return map;
    }

    @Override
    protected String getTitleValue() {
        if (mViewModel.getFrom() == HceTransactionViewModel.From.CARD_LIST) {
            return "激活卡片";
        }
        return super.getTitleValue();
    }
}
