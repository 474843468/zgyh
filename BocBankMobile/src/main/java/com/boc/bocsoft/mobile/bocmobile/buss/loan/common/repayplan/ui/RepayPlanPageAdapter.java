package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.ui;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * 还款计划适配器
 * Created by lzc on 16/8/10.
 */
public class RepayPlanPageAdapter extends FragmentPagerAdapter{
    private ArrayList<Fragment> fragmentsList;

    public RepayPlanPageAdapter(FragmentManager fm, ArrayList<Fragment> list){
        super(fm);
        fragmentsList = list;
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
}
