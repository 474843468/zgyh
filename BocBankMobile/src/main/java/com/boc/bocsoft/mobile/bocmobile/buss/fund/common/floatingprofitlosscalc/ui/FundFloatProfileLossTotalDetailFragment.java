package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.container.ContainerPageAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.container.IContainer;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.model.FundFloatingProfileLossModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;
import com.boc.bocsoft.mobile.framework.widget.tabLayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taoyongzhen on 2016/12/24.
 */

public class FundFloatProfileLossTotalDetailFragment extends BussFragment {

    private LinearLayout rootView;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    List<IContainer> containers = new ArrayList<>();// 需要重新排序使得人民币居中显示
    List<String> titleList = new ArrayList<>();
    private ContainerPageAdapter adapter;
    private List<FundFloatingProfileLossModel.ResultListBean> totalBeans;
    private String timeHint;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = (LinearLayout) mInflater.inflate(R.layout.boc_fragment_fund_cancel_order, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        slidingTabLayout = (SlidingTabLayout) rootView.findViewById(R.id.lyt_tab);
        viewPager = (ViewPager) rootView.findViewById(R.id.vpContent);
    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getArguments();
        if (bundle == null){
            return;
        }
        timeHint = bundle.getString(DataUtils.FUND_PROFILE_LOSS_TIME_HINT,getString(R.string.boc_fund_profile_one_year));
        if (bundle.containsKey(DataUtils.FUND_PROFILE_LOSS_TOTAL_BEAN_KEY)) {
            totalBeans = bundle.getParcelableArrayList(DataUtils.FUND_PROFILE_LOSS_TOTAL_BEAN_KEY);
            initDisplayDate();
        }
    }

    @Override
    public void setListener() {
        super.setListener();
    }

    private void initDisplayDate() {
        if (totalBeans == null || totalBeans.size() <= 0) {
            return;
        }

        if (totalBeans.size() == 1) {
            rootView.removeAllViews();
            FundFloatProfileLossTotalRecordView contentView = new FundFloatProfileLossTotalRecordView(mContext);
            contentView.setResultBean(totalBeans.get(0));
            contentView.setTimeHint(timeHint);
            contentView.attach(this);
            contentView.showView();
            rootView.addView(contentView);
            return;
        }

        viewPager.setOffscreenPageLimit(totalBeans.size());
        for (FundFloatingProfileLossModel.ResultListBean bean : totalBeans) {
            FundFloatProfileLossTotalRecordView contentView = new FundFloatProfileLossTotalRecordView(mContext);
            contentView.setResultBean(bean);
            contentView.setTimeHint(timeHint);
            contentView.attach(this);
            titleList.add(DataUtils.getCurrencyAndCashFlagDesSpecalRMB(mContext, bean.getCurceny(), bean.getCashFlag()));
            containers.add(contentView);

        }
        adapter = new ContainerPageAdapter(titleList, containers);
        viewPager.setAdapter(adapter);
        slidingTabLayout.setViewPager(viewPager);

    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_fund_profileloss_total_detail_title);
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

}
