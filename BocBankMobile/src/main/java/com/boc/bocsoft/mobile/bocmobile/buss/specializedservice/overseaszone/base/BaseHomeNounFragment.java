package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.base;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussActivity;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.TabPageIndicator.MyFragmentAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.tablayout.SimpleTabLayout;

/**
 * 作者：xwg on 16/10/31 17:37
 *  出国攻略- 主界面基类
 */
public abstract class BaseHomeNounFragment extends BussFragment {

    protected ViewPager viewPager;
    View rootView;
    protected SimpleTabLayout tabIndicator;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.fragment_overseas_noun, null);
        return rootView;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_fragment_overseas_noun_title);
    }

    @Override
    public void initView() {
        super.initView();
        viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        viewPager.setAdapter(new MyFragmentAdapter((BussActivity) getActivity(), getData(), getTitle()));
        tabIndicator = (SimpleTabLayout) rootView.findViewById(R.id.tab_indicator);
        tabIndicator.setupWithViewPager(viewPager);
//        tabIndicator.setSelectedTabIndicatorColor(getResources().getColor(R.color.boc_text_color_red));
//        tabIndicator.setSelectedTabIndicatorHeight((int) getResources().getDimension(R.dimen.boc_space_between_5px));
//        tabIndicator.setTabTextColors(getResources().getColor(R.color.boc_black),
//                getResources().getColor(R.color.boc_text_color_red));
//        tabIndicator.setTabTextSize(getResources().getDimension(R.dimen.boc_space_between_26px));


//        tabIndicator.addViewPager(viewPager);
//
//        try {
//            tabIndicator.setIndicatorColor(R.color.boc_text_color_red);
//            tabIndicator.setIndicatorUnSelectedColor(R.color.boc_common_bg_color);
//            tabIndicator.setIndicatorHeight((int) getResources().getDimension(R.dimen.boc_space_between_5px));
//            tabIndicator.setSelectedTextColor(R.color.boc_text_color_red,R.color.boc_text_color_common_gray);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 获取几个内容页界面
    */
    public abstract String[] getData();
    /**
    *   获取内容页的title
    */
    public abstract String[] getTitle();
}
