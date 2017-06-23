package com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.container.ContainerPageAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.container.IContainer;
import com.boc.bocsoft.mobile.bocmobile.base.widget.tab.TableLabelView;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedpurchase.ui.InvestFragmentView;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedredeem.ui.RedeemFragmentView;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taoyongzhen on 2016/12/19.
 */

public class FixedInvestFragment extends MvpBussFragment implements TableLabelView.ITabClickListener, OnPageChangeListener,View.OnClickListener{

    private View rootView;
    private ImageView imgLeftIcon = null; //标题回退按钮
    private TableLabelView tlvTabSel = null; //标题Tab页签
    private ViewPager vpContent = null; //内容区viewpager
    private TextView tvManager = null;

    private List<IContainer> containers = new ArrayList<>();
    //定投申请
    private InvestFragmentView investView;
    //定赎申请
    private RedeemFragmentView redeemFragmentView;

    private ContainerPageAdapter adapter;

    private String fundCode;
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_fund_fixedinvest,null);
        return rootView;
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    public void initView() {
        super.initView();
        View titleView= rootView.findViewById(R.id.view_title);
        imgLeftIcon = (ImageView)titleView.findViewById(R.id.left_icon);
        tlvTabSel = (TableLabelView)titleView.findViewById(R.id.tab_label);
        tvManager = (TextView)titleView.findViewById(R.id.tv_manager);
        vpContent = (ViewPager)rootView.findViewById(R.id.vp_content);



    }


    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getArguments();
        if (bundle == null || bundle.containsKey(DataUtils.FUND_CODE_KEY) == false){
            return;
        }

        fundCode = bundle.getString(DataUtils.FUND_CODE_KEY);
        investView = new InvestFragmentView(mContext);
        investView.setFundCode(fundCode);
        investView.attach(this);
        redeemFragmentView = new RedeemFragmentView(mContext);
        redeemFragmentView.setFundCode(fundCode);
        redeemFragmentView.attach(this);
        containers.add(investView);
        containers.add(redeemFragmentView);
        adapter = new ContainerPageAdapter(containers);
        vpContent.setAdapter(adapter);
    }



    @Override
    public void setListener() {
        tlvTabSel.setTabClickListener(this);
        tvManager.setOnClickListener(this);
        imgLeftIcon.setOnClickListener(this);

    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tlvTabSel.setCurSelectedIndex(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClickTab(int tabIndex) {
        vpContent.setCurrentItem(tabIndex);
    }

    @Override
    public void onClick(View v) {
        //点击管理
        if (v.getId() == R.id.tv_manager){
            return;
        }
        if (v.getId() == R.id.left_icon){
            titleLeftIconClick();
            return;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (investView != null){
            investView.onDestroy();
        }
        if (redeemFragmentView !=null){
            redeemFragmentView.onDestroy();
        }
        if (containers != null){
            containers.clear();
            containers = null;

        }

    }
}
