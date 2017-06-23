package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taoyongzhen on 2016/12/4.
 */

public class FundFloatingProfileLossTotalAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList = new ArrayList<>();
    private ArrayList<String> titleList = new ArrayList<>();

    public FundFloatingProfileLossTotalAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    public List<Fragment> getFragmentList() {
        return fragmentList;
    }

    public void setFragmentList(List<Fragment> fragmentList) {
        this.fragmentList = fragmentList;
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    public void addPage(Fragment pageView, String pageTitle) {
        fragmentList.add(pageView);
        titleList.add(pageTitle);
        notifyDataSetChanged();
    }


}
