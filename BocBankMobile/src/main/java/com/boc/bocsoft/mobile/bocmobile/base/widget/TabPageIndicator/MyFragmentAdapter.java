package com.boc.bocsoft.mobile.bocmobile.base.widget.TabPageIndicator;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.boc.bocsoft.mobile.bocmobile.base.activity.BussActivity;

/**
 * @ClassName: MyFragmentAdapter
 * @author WangTong
 */
public class MyFragmentAdapter extends FragmentStatePagerAdapter {

	protected BussActivity mActivity;
	public String[] mData;
	protected String[] mTitle;

	public MyFragmentAdapter(BussActivity activity, String[] data, String[] title) {
		super(activity.getSupportFragmentManager());
		mActivity = activity;
		mData = data;
		mTitle = title;
	}

	@Override
	public Object instantiateItem(ViewGroup arg0, int arg1) {
		return super.instantiateItem(arg0, arg1);
	}

	@Override
	public Fragment getItem(int position) {
		return Fragment.instantiate(mActivity, mData[position], null);
	}

	@Override
	public int getCount() {
		return mData.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mTitle[position];
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
	}

}
