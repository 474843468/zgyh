package com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.ui;

import android.app.Activity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.adapter.InvestfragmentAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.presenter.InvestMgPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;
import com.boc.bocsoft.mobile.framework.widget.tabLayout.SlidingTabLayout;

/**
 * Created by huixiaobo on 2016/11/24.
 * 基金定投管理  有效申请-失效申请
 */
public class InvestmanageFragment extends MvpBussFragment<InvestMgPresenter> {

    protected View rootView;
    protected SlidingTabLayout lytTab;
    protected ViewPager vpContent;
    protected ImageView investIv;
    protected TextView investHit;
    /**失效定投*/
    private InvalidinvestFragment invalidFragment;
    /**有效定投*/
    private ValidinvestFragment validFragment;
    /**滑动view适配器*/
    private InvestfragmentAdapter investfragmentAdapter;
    private String startFlag;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fund_investmanager, null);
        return rootView;
    }


    @Override
    public void initView() {
        super.initView();
        lytTab = (SlidingTabLayout) rootView.findViewById(R.id.lyt_tab);
        vpContent = (ViewPager) rootView.findViewById(R.id.vpContent);
        investIv = (ImageView) rootView.findViewById(R.id.investIv);
        investHit = (TextView) rootView.findViewById(R.id.investHit);
    }

    @Override
    public void initData() {
        super.initData();
        startFlag = getArguments().getString(DataUtils.FUND_CODE_KEY);
        validFragment = new ValidinvestFragment();
        invalidFragment = new InvalidinvestFragment();
        investfragmentAdapter = new InvestfragmentAdapter(getActivity().getSupportFragmentManager());

        investfragmentAdapter.pageTitle(validFragment, getString(R.string.boc_fund_validtitle));
        investfragmentAdapter.pageTitle(invalidFragment, getString(R.string.boc_fund_invalidtitle));
        vpContent.setAdapter(investfragmentAdapter);
        vpContent.setOffscreenPageLimit(2);
        lytTab.setViewPager(vpContent);
         if ("Y".equals(startFlag)) {
             lytTab.setCurrentTab(0);
         } else if ("N".equals(startFlag)){
             lytTab.setCurrentTab(1);
         }
    }


    @Override
    protected boolean isHaveTitleBarView() {
        return true;
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
    protected String getTitleValue() {
        return getString(R.string.boc_fund_invstmanager);
    }

    @Override
    protected InvestMgPresenter initPresenter() {
        return null;
    }

    @Override
    public void onDestroy() {
        if (!getActivity().isFinishing()) {
            mTitleBarView.setVisibility(View.GONE);
            removeFragments();
        }
        super.onDestroy();
    }

    /**
     * 释放现有的fragment
     */
    private void removeFragments(){
        FragmentTransaction mTransaction = getFragmentManager()
                .beginTransaction();
        mTransaction.remove(validFragment);
        mTransaction.remove(invalidFragment);
        mTransaction.commit();
    }

}
