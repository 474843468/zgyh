package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;

import java.util.ArrayList;

/**
 * 作者：xwg on 16/12/5 20:14
 */
public class RepaymentPageAdapter extends FragmentPagerAdapter {

    private ArrayList<BussFragment> fragments;
    private FragmentManager fm;
    private String [] titles;

    public RepaymentPageAdapter(FragmentManager fragmentManager,
                                    ArrayList<BussFragment> fragments,String []titles) {
        super(fragmentManager);
        this.titles=titles;
        this.fm = fragmentManager;
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void setFragments(ArrayList<BussFragment> fragments) {
        if (this.fragments != null) {
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment f : this.fragments) {
                ft.remove(f);
            }
            ft.commit();
            ft = null;
            fm.executePendingTransactions();
        }
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
