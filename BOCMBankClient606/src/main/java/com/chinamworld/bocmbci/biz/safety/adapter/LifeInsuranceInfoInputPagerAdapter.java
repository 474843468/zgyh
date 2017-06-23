package com.chinamworld.bocmbci.biz.safety.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class LifeInsuranceInfoInputPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> listFrag;
	
	public LifeInsuranceInfoInputPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	public LifeInsuranceInfoInputPagerAdapter(FragmentManager fm, List<Fragment> listFragment) {
		super(fm);
		this.listFrag = listFragment;
	}

	@Override
	public int getCount() {
		return listFrag.size();
	}

	@Override
	public Fragment getItem(int arg0) {
		return listFrag.get(arg0);
	}
}
