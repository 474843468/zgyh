package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.model.FundFloatingProfileLossModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui.adapter.FundFloatingProfileLossTotalAdapter;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.widget.tabLayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taoyongzhen on 2016/12/4.
 */

public class FundFloatingProfileLossTotalDetailFragment extends MvpBussFragment {

    private View rootView;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private BussFragment queryTransOntranFragment;
    private BussFragment statusDdApplyQueryFragment;
    private List<Fragment> fragmentList = new ArrayList<>();
    private FundFloatingProfileLossTotalAdapter adapter;

    private List<FundFloatingProfileLossModel.ResultListBean> totalBeans;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

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
            adapter = new FundFloatingProfileLossTotalAdapter(getChildFragmentManager());
        }

    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(FundFloatingProfileLossConstant.FUND_PROFILE_LOSS_TOTAL_BEAN_KEY)){
            totalBeans = bundle.getParcelableArrayList(FundFloatingProfileLossConstant.FUND_PROFILE_LOSS_TOTAL_BEAN_KEY);
            initDisplayDate();
        }
    }

    @Override
    public void setListener() {
        super.setListener();
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initDisplayDate(){
        if (totalBeans == null || totalBeans.size() <=0){
            return;
        }
        viewPager.setOffscreenPageLimit(totalBeans.size());
        for (FundFloatingProfileLossModel.ResultListBean bean : totalBeans) {
            FundFloatingProfileLossSingleDetailFragment fragment = new FundFloatingProfileLossSingleDetailFragment();
            fragment.setDetailType(FundFloatingProfileLossConstant.FUND_PROFILE_LOSS_DETAIL_TYPE_TOTAL);
            Bundle bundle = new Bundle();
            bundle.putParcelable(FundFloatingProfileLossConstant.FUND_PROFILE_LOSS_BEAN_KEY,bean);
            fragment.setArguments(bundle);
            adapter.addPage(fragment, PublicCodeUtils.getCurrency(mContext,bean.getCurceny()));
        }
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

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }
}
