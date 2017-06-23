package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hcesecuritysetting;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.SpUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.util.HceConstants;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.widget.ToggleRowView;

/**
 * Created by yangle on 2016/12/13.
 * 描述:安全设置界面
 */
@Deprecated
public class HceSecuritySettingFragment extends BussFragment implements ToggleRowView.OnToggleListener {


    private View mRootView;
    private ToggleRowView mToggleView;//带开关的view


    public static HceSecuritySettingFragment newInstance() {
        return new HceSecuritySettingFragment();
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mRootView =  mInflater.inflate(R.layout.boc_fragment_hce_security_setting, null);
    }

    @Override
    public void initView() {
        super.initView();
        mToggleView = (ToggleRowView) mRootView.findViewById(R.id.toggle_view);
        //  初始化回显
        mToggleView.setChecked(SpUtils.getSpBoolean(mContext, HceConstants.REQUIRE_SUPPORT_UNLOCK,false));
    }

    @Override
    public void setListener() {
        super.setListener();
        mToggleView.setOnToggleListener(this);
    }

    @Override
    protected String getTitleValue() {
        return "安全设置";
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
    public void onToggleChangedListener(CompoundButton buttonView, boolean isChecked) {
        ToastUtils.show(isChecked ? "已开启锁屏支付":"已关闭锁屏支付");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 界面关闭前保存
        SpUtils.saveBoolean(mContext, HceConstants.REQUIRE_SUPPORT_UNLOCK, mToggleView.isChecked());
    }

}
