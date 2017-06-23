package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * 基金-账户管理-主界面适配器
 * Created by lyf7084 on 2016/11/22.
 */
public class PagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragmentsList;
    private ArrayList<String> titleList;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentsList = new ArrayList<Fragment>();
        titleList = new ArrayList<String>();
    }

    public PagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        fragmentsList = list;
    }

    public PagerAdapter(FragmentManager fm, ArrayList<Fragment> list, ArrayList<String> titleList) {
        super(fm);
        fragmentsList = list;
        this.titleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        if (fragmentsList != null && fragmentsList.size() > position) {
            return fragmentsList.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        return (fragmentsList == null ? 0 : fragmentsList.size());
    }

    public void addPage(Fragment pageView, String pageTitle) {
        fragmentsList.add(pageView);
        titleList.add(pageTitle);
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

}
