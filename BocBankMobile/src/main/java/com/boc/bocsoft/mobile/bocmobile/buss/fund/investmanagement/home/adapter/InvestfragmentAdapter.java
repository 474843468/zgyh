package com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by louis.hui on 2016/10/25.
 * 基金定投管理滑动适配器
 */
public class InvestfragmentAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragmentsList;
    private ArrayList<String> titleList;

    public InvestfragmentAdapter(FragmentManager fm) {
        super(fm);
        fragmentsList = new ArrayList<Fragment>();
        titleList = new ArrayList<String>();
    }

    public InvestfragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragmentsList) {
        super(fm);
        this.fragmentsList = fragmentsList;
    }

    public InvestfragmentAdapter(FragmentManager fm, ArrayList<String> titleList, ArrayList<Fragment> fragmentsList) {
        super(fm);
        this.fragmentsList = fragmentsList;
        this.titleList = titleList;

    }



    @Override
    public Fragment getItem(int position) {
        if(fragmentsList != null && fragmentsList.size() > position){
            return fragmentsList.get(position);
        }
        return null;

    }

    @Override
    public int getCount() {
        return (fragmentsList == null ? 0: fragmentsList.size());
    }

    public void pageTitle(Fragment pageView, String pageTitle) {
        fragmentsList.add(pageView);
        titleList.add(pageTitle);
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
