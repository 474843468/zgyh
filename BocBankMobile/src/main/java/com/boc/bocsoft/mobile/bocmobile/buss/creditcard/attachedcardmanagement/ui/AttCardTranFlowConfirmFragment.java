package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.ui;

import android.os.Bundle;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainTranSetResult.PsnCrcdAppertainTranSetResultResult;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmContract;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.model.AttCardTradeFlowModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.presenter.AttCardTradeFlowSecurityPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.LinkedHashMap;

/**
 * Name: liukai
 * Time：2016/12/5 9:51.
 * Created by lk7066 on 2016/12/5.
 * It's used to 附属卡交易流量信息确认页面
 */

public class AttCardTranFlowConfirmFragment extends BaseConfirmFragment<AttCardTradeFlowModel, PsnCrcdAppertainTranSetResultResult> {

    public static AttCardTranFlowConfirmFragment newInstance(AttCardTradeFlowModel model, VerifyBean verifyBean) {
        Bundle args = getBundleForNew(model, verifyBean);
        AttCardTranFlowConfirmFragment ConfirmFragment = new AttCardTranFlowConfirmFragment();
        ConfirmFragment.setArguments(args);
        return ConfirmFragment;
    }

    @Override
    protected void setConfirmViewData() {
        //哈希表格写入显示的数据
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        confirmInfoView.setHeadValue(getResources().getString(R.string.boc_crcd_attcard_tranflow_edittitle) + "（" + PublicCodeUtils.getCurrency(mContext, mFillInfoBean.getCurrency()) + "）", MoneyUtils.transMoneyFormat(mFillInfoBean.getAmount(), mFillInfoBean.getCurrency()));
        map.put(getResources().getString(R.string.boc_crcd_attcard_crcd_no), NumberUtils.formatCardNumber(mFillInfoBean.getAccountNo()));
        map.put(getResources().getString(R.string.boc_crcd_attcard_attcard_name), mFillInfoBean.getSubCardName());
        map.put(getResources().getString(R.string.boc_crcd_attcard_attcard_no), NumberUtils.formatCardNumber(mFillInfoBean.getSubCrcdNo()));
        confirmInfoView.addData(map, true);
    }

    @Override
    public void onSubmitSuccess(PsnCrcdAppertainTranSetResultResult submitResult) {
        //流量设置的页面出栈，并且调用ChooseFragment的Reinit方法刷新页面
        popToAndReInit(AttCardTranFlowChooseFragment.class);
        //Toast显示设置成功
        Toast.makeText(getContext(), getResources().getString(R.string.boc_crcd_attcard_tranflow_setup_success), Toast.LENGTH_LONG).show();
    }

    @Override
    protected BaseConfirmContract.Presenter<AttCardTradeFlowModel> initPresenter() {
        return new AttCardTradeFlowSecurityPresenter(this);
    }
}
