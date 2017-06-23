package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.MenuFragment;

/**
 * Created by issuser on 2016/12/7.
 */
public class TempFragment extends BussFragment {
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return new TextView(mActivity);
    }

    @Override
    protected String getTitleValue() {
        return "双向宝首页";
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return true;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected void titleRightIconClick() {
        hideSoftInput(); // 隐藏软键盘

        Bundle bundle = new Bundle();
        bundle.putInt("Menu", MenuFragment.LONG_SHORT_FOREX);
        MenuFragment fragment = new MenuFragment();
        fragment.setArguments(bundle);
        start(fragment);
    }
}
