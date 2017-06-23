package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 作者：lq7090
 * 创建时间：2016/10/26.
 * 用途：跨境专区星球进入的 Fragment 的Adapter
 */
public class OverseasFragmentAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragmentsList;

    public OverseasFragmentAdapter(FragmentManager fm, ArrayList<Fragment> list){
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

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}

