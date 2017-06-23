package com.chinamworld.bocmbci.biz.epay.transquery.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.chinamworld.bocmbci.base.activity.BaseActivity;

public class OtherPayInfoAdapter extends PagerAdapter{

	private ArrayList<View> viewList;
	private BaseActivity context;
	private ArrayList<String> titles;
	
	public OtherPayInfoAdapter(ArrayList<View> viewList, BaseActivity context) {
		this.viewList = viewList;
		this.context = context;
	}

	public OtherPayInfoAdapter(ArrayList<String> titles, ArrayList<View> viewList, BaseActivity context) {
		this.viewList = viewList;
		this.titles = titles;
		this.context = context;
	}

	@Override  
    public boolean isViewFromObject(View view, Object arg) {  
        return view == arg;  
    }  

    @Override  
    public int getCount() {  
        return viewList.size();  
    }  

    @Override  
    public void destroyItem(ViewGroup container, int position,  
            Object object) {  
        container.removeView(viewList.get(position));  
    }  

    @Override  
    public int getItemPosition(Object object) {  
        return super.getItemPosition(object);  
    }  
    
    @Override  
    public CharSequence getPageTitle(int position) {  
        return titles.get(position);  
    }  

    @Override  
    public Object instantiateItem(ViewGroup container, int position) {  
        container.addView(viewList.get(position));  
        return viewList.get(position);  
    }  

}
