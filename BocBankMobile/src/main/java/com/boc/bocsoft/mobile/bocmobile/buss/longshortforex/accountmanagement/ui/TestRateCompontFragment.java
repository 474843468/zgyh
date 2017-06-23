package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.progress.LiquidationAndWarnRatio;

import java.math.BigDecimal;

/**
 * Fragment：测试斩仓比例和报警比例组件（保证金详情页面用到）
 * Created by zhx on 2016/12/21
 */
public class TestRateCompontFragment extends BussFragment {
    private View mRootView;
    private LiquidationAndWarnRatio lwr_ratio;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.fragment_test_rate_component, null);

        return mRootView;
    }

    @Override
    public void initView() {
        lwr_ratio = (LiquidationAndWarnRatio) mRootView.findViewById(R.id.lwr_ratio);
        lwr_ratio.post(new Runnable() {
            @Override
            public void run() {
                lwr_ratio.setRate(BigDecimal.valueOf(0.2), BigDecimal.valueOf(0.5));
            }
        });

    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void setListener() {
        super.setListener();
    }

    @Override
    protected String getTitleValue() {
        return super.getTitleValue();
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return super.isDisplayRightIcon();
    }

    @Override
    protected boolean getTitleBarRed() {
        return super.getTitleBarRed();
    }
}
