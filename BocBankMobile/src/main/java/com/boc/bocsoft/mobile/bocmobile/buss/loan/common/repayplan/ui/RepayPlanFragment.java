package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.tab.TableLabelView;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanDrawDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui.EloanRepayDetailFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui.EloanSettleDetailFramgent;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.ui.repayhistory.RepayHistoryFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.ui.repayoverdue.RepayOverdueFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.ui.repayremain.RepayRemainFragment;

import java.util.ArrayList;


/**
 * 还款计划查询
 * Created by liuzc on 2016/8/10.
 */
public class RepayPlanFragment extends BussFragment implements TableLabelView.ITabClickListener, ViewPager.OnPageChangeListener{
    private View mRootView = null;
    private ImageView imgLeftIcon = null; //标题回退按钮
    private TableLabelView tlvTabSel = null; //标题Tab页签
    private ViewPager vpContent = null; //内容区viewpager

    /**详情对象*/
    private EloanDrawDetailModel mDrawDetail;
    /**当前时间*/
    private String mEndDate;
    private RepayRemainFragment repayRemain;
    private RepayHistoryFragment repayHistory;
    private RepayOverdueFragment repayOverdue;
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = View.inflate(mContext, R.layout.boc_fragment_loan_repay_plan, null);
        return mRootView;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }


    @Override
    public void initView() {
        View titleView= mRootView.findViewById(R.id.viewTitle);
        imgLeftIcon = (ImageView)titleView.findViewById(R.id.leftIconIv);
        tlvTabSel = (TableLabelView)titleView.findViewById(R.id.tabLabel);
        tlvTabSel.setTabClickListener(this);

        vpContent = (ViewPager)mRootView.findViewById(R.id.vpContent);

        ArrayList<Fragment> frgList = new ArrayList<Fragment>();

        if (repayRemain == null) {
            repayRemain = new RepayRemainFragment();
        }

        if (repayHistory == null) {
            repayHistory = new RepayHistoryFragment();
        }

        if (repayOverdue == null) {
            repayOverdue = new RepayOverdueFragment();
        }

        frgList.add(repayRemain);  //剩余还款
        frgList.add(repayHistory); //历史还款
        frgList.add(repayOverdue); //逾期还款


        RepayPlanPageAdapter adpter = new RepayPlanPageAdapter(
                getChildFragmentManager(), frgList);
        vpContent.setAdapter(adpter);
        vpContent.addOnPageChangeListener(this);
        vpContent.setOffscreenPageLimit(3); //不销毁隐藏的页面
    }

    @Override
    public void initData() {
        mDrawDetail = (EloanDrawDetailModel) getArguments().getSerializable(EloanConst.LOAN_DRAWDETA);
        mEndDate = getArguments().getString(EloanConst.DATA_TIME);
        String repayType = getArguments().getString(EloanConst.LOAN_REPAYTYPE);
        repayRemain.setRepayType(repayType);

        int pageIndex = (int)getArguments().getInt(EloanConst.DEFAULT_PAGE_INDEX);
        vpContent.setCurrentItem(pageIndex);
        if (mDrawDetail != null) {
            repayRemain.setRemainData(mDrawDetail);
            repayHistory.setHistoryData(mDrawDetail.getLoanAccount(), mEndDate, mDrawDetail.getCurrency());
            repayOverdue.setOverdueData(mDrawDetail.getLoanAccount(), mDrawDetail.getCurrency());
        }

    }

    @Override
    protected void titleLeftIconClick() {
    	// TODO Auto-generated method stub
    	super.titleLeftIconClick();

    }

    @Override
    public void setListener() {
        //title左侧返回按钮
        imgLeftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleLeftIconClick();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if(!getActivity().isFinishing()){
            FragmentTransaction mTransaction = getFragmentManager()
                    .beginTransaction();
            mTransaction.remove(repayRemain);
            mTransaction.remove(repayHistory);
            mTransaction.remove(repayOverdue);
            mTransaction.commit();
        }
        super.onDestroy();
    }

    @Override
    public void onClickTab(int tabIndex) {
        vpContent.setCurrentItem(tabIndex);
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
}
