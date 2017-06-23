package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import java.util.ArrayList;

/**
 * 额度使用记录配器
 * Created by lzc on 16/9/23.
 */
public class FacilityUseRecAdapter extends FragmentPagerAdapter{
    private ArrayList<Fragment> fragmentsList;
    private ArrayList<String> titleList;

    public FacilityUseRecAdapter(FragmentManager fm){
        super(fm);
        fragmentsList = new ArrayList<Fragment>();
        titleList = new ArrayList<String>();
    }

    public FacilityUseRecAdapter(FragmentManager fm, ArrayList<Fragment> list){
        super(fm);
        fragmentsList = list;
    }

    public FacilityUseRecAdapter(FragmentManager fm, ArrayList<Fragment> list, ArrayList<String> titleList){
        super(fm);
        fragmentsList = list;
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
