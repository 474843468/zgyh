package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hcesecuritysetting;

import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.SpUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.util.HceConstants;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.widget.HceScenceSelectGroupView;

/**
 * Created by yangle on 2017/1/3.
 * 描述: 支付场景选择界面
 */
public class HceSceneSelectFragment extends BussFragment implements  HceScenceSelectGroupView.OnCheckedChangeListener {

    private HceScenceSelectGroupView mRootView;
    private boolean mRequireSupportUnlock;

    public static HceSceneSelectFragment newInstance() {
        return new HceSceneSelectFragment();
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = new HceScenceSelectGroupView(mContext);
        return mRootView;
    }

    @Override
    public void initView() {
        super.initView();

    }

    @Override
    public void setListener() {
        super.setListener();
        mRootView.setOnCheckedChangeListener(this);
    }

    @Override
    protected String getTitleValue() {
        return "场景选择";
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
    protected void titleLeftIconClick() {
        super.titleLeftIconClick();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 界面关闭前保存
        SpUtils.saveBoolean(mContext, HceConstants.REQUIRE_SUPPORT_UNLOCK, mRequireSupportUnlock);
    }

    @Override
    public void onCheckedChange(HceScenceSelectGroupView view, int index, int checkedId) {
        mRequireSupportUnlock = index == 1;
        ToastUtils.show("mRequireSupportUnlock=" + mRequireSupportUnlock);
    }
}
