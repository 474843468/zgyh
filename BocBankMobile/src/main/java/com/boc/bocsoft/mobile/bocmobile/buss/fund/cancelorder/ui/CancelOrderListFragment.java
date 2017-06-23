package com.boc.bocsoft.mobile.bocmobile.buss.fund.cancelorder.ui;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.cancelorder.CancelOrderAdapter;
import com.boc.bocsoft.mobile.framework.widget.tabLayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taoyongzhen on 2016/11/21.
 */

public class CancelOrderListFragment extends BussFragment  {

    private View rootView;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private BussFragment queryTransOntranFragment;
    private BussFragment statusDdApplyQueryFragment;
    private List<Fragment> fragmentList = new ArrayList<>();
    private CancelOrderAdapter adapter;
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_fund_cancel_order,null);
        return rootView;
    }

    @Override
    public void initView() {
        slidingTabLayout = (SlidingTabLayout) rootView.findViewById(R.id.lyt_tab);
        viewPager = (ViewPager) rootView.findViewById(R.id.vpContent);
        if (adapter == null) {
            adapter = new CancelOrderAdapter(getChildFragmentManager());
        }
        if (queryTransOntranFragment == null) {
            queryTransOntranFragment = new QueryTransOntranFragment();
            adapter.addPage(queryTransOntranFragment,getString(R.string.boc_fund_cancelorder_head_ontran));
        }
        if (statusDdApplyQueryFragment == null) {
            statusDdApplyQueryFragment = new StatusDdApplyQueryFragment();
            adapter.addPage(statusDdApplyQueryFragment,getString(R.string.boc_fund_cancelorder_head_ddapply));
        }
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        slidingTabLayout.setViewPager(viewPager);

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
        return getString(R.string.boc_fund_cancelorder_title);
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

}
