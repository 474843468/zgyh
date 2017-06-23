package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.ui;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.FacilityInquiryConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.model.FacilityInquiryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.ui.adapter.FacilityUseRecAdapter;
import com.boc.bocsoft.mobile.framework.widget.tabLayout.SlidingTabLayout;


/**
 * 额度使用记录背景页，包含结清、未结清两个子页面
 * Created by liuzc on 2016/9/23.
 */
public class FacilityUseRecFragment extends BussFragment {
    private View mRootView = null;
    private ViewPager vpContent = null; //内容区viewpager

    protected SlidingTabLayout lytTab;
    private FacilityUseRecAdapter mPageAdapter;

    private FacilityUseRecQryFragment frgRepaying = null;
    private FacilityUseRecQryFragment frgRepayOver = null;

    //对象
    private FacilityInquiryViewModel.FacilityInquiryBean mFacilityBean;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = View.inflate(mContext, R.layout.fragment_facility_use_record_bg, null);
        return mRootView;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    protected boolean isHaveTitleBarView() {
        return true;
    }


    @Override
    public void initView() {

        lytTab = (SlidingTabLayout)mRootView.findViewById(R.id.lyt_tab);
        vpContent = (ViewPager)mRootView.findViewById(R.id.vpContent);
        mFacilityBean = getArguments().getParcelable(FacilityInquiryConst.KEY_FACILITY_INQUIRY_BEAN);

        mPageAdapter = new FacilityUseRecAdapter(getActivity().getSupportFragmentManager());

        frgRepaying = new FacilityUseRecQryFragment(); //未结清
        frgRepaying.setRepayOverFlag("N");
        frgRepaying.setFacilityBean(mFacilityBean);

        frgRepayOver = new FacilityUseRecQryFragment(); //已结清
        frgRepayOver.setFacilityBean(mFacilityBean);
        frgRepayOver.setRepayOverFlag("Y");



        mPageAdapter.addPage(frgRepaying, getString(R.string.boc_loan_repay_notover));
        mPageAdapter.addPage(frgRepayOver, getString(R.string.boc_loan_repay_over));
        vpContent.setAdapter(mPageAdapter);
//        vpContent.addOnPageChangeListener(this);
        vpContent.setOffscreenPageLimit(2); //不销毁隐藏的页面
        lytTab.setViewPager(vpContent);

    }


    @Override
    public void initData() {

    }

    private void removeFragments(){
        if(!getActivity().isFinishing()){
            FragmentTransaction mTransaction = getFragmentManager()
                    .beginTransaction();
            mTransaction.remove(frgRepaying);
            mTransaction.remove(frgRepayOver);
            mTransaction.commit();
        }
    }


    @Override
    public void onDestroy() {
        removeFragments();
        super.onDestroy();
    }

    protected String getTitleValue() {
        return getString(R.string.boc_facility_used_record);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }


    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

}
