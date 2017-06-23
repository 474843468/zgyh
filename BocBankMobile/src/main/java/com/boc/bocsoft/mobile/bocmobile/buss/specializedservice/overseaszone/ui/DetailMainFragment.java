package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.adapter.OverseasFragmentAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.base.NoScrollViewPager;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.base.OSTableLabelView;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.vpcontent.AbroadStudyingFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.vpcontent.AfterVisaFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.vpcontent.ApplyAbroadStudyFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.vpcontent.BeforeVisaFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.vpcontent.ExpatriateAfterFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.vpcontent.ExpatriateBeforeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.vpcontent.ExpatriateInFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.vpcontent.InvestAfterFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.vpcontent.InvestBeforeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.vpcontent.TravelAfterFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.vpcontent.TravelBeforeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.vpcontent.TravelInFragment;

import java.util.ArrayList;

/**
 *
 * //国际商旅
 */
public class DetailMainFragment extends BussFragment implements OSTableLabelView.ITabClickListener, ViewPager.OnPageChangeListener, View.OnTouchListener {
    private View mRootView = null;
    private ImageView imgLeftIcon = null; //标题回退按钮
    private OSTableLabelView tlvTabSel = null; //标题Tab页签
    private NoScrollViewPager vpContent = null; //内容区viewpager

    private ImageView ivStrategy;
    /**
     *   出国留学界面
     */
    public static final int ABROAD_STUDY=1;
    /**
     * 外派工作
     */
    public static final int ABROAD_WORK=2;
    /**
     *  投资移民
     */
    public static final int ABROAD_INVEST=3;
    /**
     * 国际商旅
     */
    public static final int ABROAD_TRAVEL=4;

    private int fragmentType;
    private AbroadStudyingFragment abroadStudyingFragment;
    private BeforeVisaFragment beforeVisaFragment;
    private AfterVisaFragment afterVisaFragment;
    private ApplyAbroadStudyFragment applyAbroadStudyFragment;
    private ExpatriateBeforeFragment expatriateBeforeFragment;
    private ExpatriateInFragment expatriateInFragment;
    private ExpatriateAfterFragment expatriateAfterFragment;
    private InvestBeforeFragment investBeforeFragment;
    private InvestAfterFragment investAfterFragment;
    private TravelBeforeFragment travelbeforefragment;
    private TravelInFragment travelinfragment;
    private TravelAfterFragment travelafterfragment;
    private RelativeLayout rl_container;

    private   int screenWidth,screenHeight;
    public DetailMainFragment(int type) {
        this.fragmentType=type;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.fragment_abroadstudy, null);
        return mRootView;
    }

    @Override
    public void beforeInitView() {
        DisplayMetrics dm=getResources().getDisplayMetrics();
         screenWidth=dm.widthPixels;
        screenHeight=dm.heightPixels;
    }

    /**
     * 是否显示标题栏，默认显示
     * 子类可以重写此方法，控制是否显示标题栏
     */
    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }


    @Override
    public void initView() {
        View titleView = mRootView.findViewById(R.id.viewTitle1);
        imgLeftIcon = (ImageView) titleView.findViewById(R.id.leftIconIv1);
        rl_container=(RelativeLayout)mRootView.findViewById(R.id.rl_container);

        tlvTabSel = (OSTableLabelView) titleView.findViewById(R.id.tabLabel1);
        tlvTabSel.setTabClickListener(this);

        tlvTabSel.setTitle(getTitleString(fragmentType));

        vpContent = (NoScrollViewPager) mRootView.findViewById(R.id.vpContent1);

        ivStrategy=(ImageView)mRootView.findViewById(R.id.strategy);

    }

    @Override
    public void initData() {

        OverseasFragmentAdapter adpter = new OverseasFragmentAdapter(
                getActivity().getSupportFragmentManager(), getFragmentList(fragmentType));
        vpContent.setAdapter(adpter);
        vpContent.addOnPageChangeListener(this);

//        vpContent.setOffscreenPageLimit(getFragmentList(fragmentType).size()); //不销毁隐藏的页面
        vpContent.setOffscreenPageLimit(1); //不销毁隐藏的页面
        vpContent.setNoScroll(true);//设置不能滑动


        int pageIndex = getArguments().getInt(EloanConst.DEFAULT_PAGE_INDEX);
        vpContent.setCurrentItem(pageIndex, false);

//        TranslateAnimation animation=new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,
//                0,Animation.RELATIVE_TO_SELF,-0.03f,Animation.RELATIVE_TO_SELF,0.03f);
//        animation.setDuration(800);
//        animation.setRepeatCount(Animation.INFINITE);
//        animation.setRepeatMode(Animation.REVERSE);
//        if (ivStrategy!=null)
//            ivStrategy.startAnimation(animation);

    }

    /**
     * 根据传入类型 初始化不同界面
     * @param type  1：出国留学  2：外派工作 3：投资移民 4：国际商旅
     */
    private ArrayList<Fragment> getFragmentList(int type) {
        ArrayList<Fragment> frgList = new ArrayList<Fragment>();
        switch (type){
            case ABROAD_STUDY:  //出国留学
                if (abroadStudyingFragment==null)
                    abroadStudyingFragment = new AbroadStudyingFragment();
                if (beforeVisaFragment==null)
                    beforeVisaFragment = new BeforeVisaFragment();
                if (afterVisaFragment==null)
                    afterVisaFragment = new AfterVisaFragment();
                if (applyAbroadStudyFragment==null)
                    applyAbroadStudyFragment = new ApplyAbroadStudyFragment();

                frgList.add(applyAbroadStudyFragment);//申请出国留学
                frgList.add(beforeVisaFragment);  //签证前
                frgList.add(afterVisaFragment); //签证后
                frgList.add(abroadStudyingFragment); //国外学习时

                tlvTabSel.setLayoutWidth(getResources().getDimensionPixelSize(R.dimen.boc_space_between_600px));
                break;

            case ABROAD_WORK://外派工作
                if (expatriateInFragment==null)
                    expatriateInFragment = new ExpatriateInFragment();
                if (expatriateAfterFragment==null)
                    expatriateAfterFragment = new ExpatriateAfterFragment();
                if (expatriateBeforeFragment==null)
                    expatriateBeforeFragment = new ExpatriateBeforeFragment();

                frgList.add(expatriateBeforeFragment);
                frgList.add(expatriateInFragment);
                frgList.add(expatriateAfterFragment);
                tlvTabSel.setLayoutWidth(getResources().getDimensionPixelSize(R.dimen.boc_space_between_400px));
                break;
            case ABROAD_INVEST://3：投资移民
                if (investBeforeFragment==null)
                    investBeforeFragment = new InvestBeforeFragment();
                if (investAfterFragment==null)
                    investAfterFragment = new InvestAfterFragment();

                frgList.add(investBeforeFragment);
                frgList.add(investAfterFragment);
                tlvTabSel.setLayoutWidth(getResources().getDimensionPixelSize(R.dimen.boc_space_between_260px));
                break;
            case ABROAD_TRAVEL://4：国际商旅
                if (travelbeforefragment==null)
                    travelbeforefragment = new TravelBeforeFragment();
                if (travelinfragment==null)
                    travelinfragment = new TravelInFragment();
                if (travelafterfragment==null)
                    travelafterfragment = new TravelAfterFragment();

                frgList.add(travelbeforefragment);  //出国前
                frgList.add(travelinfragment); //出国中
                frgList.add(travelafterfragment); //出国后

                tlvTabSel.setLayoutWidth(getResources().getDimensionPixelSize(R.dimen.boc_space_between_400px));
                break;
        }
        return frgList;
    }

    private int lastX,lastY,posX,posY;
    @Override
    public void setListener() {
        //title左侧返回按钮
        imgLeftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleLeftIconClick();
            }
        });
        ivStrategy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statStrategy(fragmentType);
            }
        });


        ivStrategy.setOnTouchListener(this);
    }

    /**
     * 跳转出国攻略
     */
    private void statStrategy(int fragmentType) {
        switch (fragmentType){
            case ABROAD_STUDY:  //出国留学
                start(new OverSeasStudyNounFragment());
                break;

            case ABROAD_WORK://外派工作
                start(new OverseasWorkNounFragment());
                break;

            case ABROAD_INVEST://3：投资移民
                start(new OverseasInvserNounFragment());
                break;

            case ABROAD_TRAVEL://4：国际商旅
                start(new OverseasTravleNounFragment());
                break;
        }
    }
    private String getTitleString(int fragmentType) {
        String title="";
        switch (fragmentType){
            case ABROAD_STUDY:  //出国留学
                title= getResources().getString(R.string.boc_fragment_abroadstudytab);
                break;

            case ABROAD_WORK://外派工作
                title= getResources().getString(R.string.boc_fragment_ew_tab);
                break;

            case ABROAD_INVEST://3：投资移民
                title= getResources().getString(R.string.boc_fragment_ii_tab);
                break;

            case ABROAD_TRAVEL://4：国际商旅
                title= getResources().getString(R.string.boc_fragment_abroadtraveltab);
                break;
        }
        return title;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        OverseasHomeFragment fragment=findFragment(OverseasHomeFragment.class);
        if (fragment!=null)
            fragment.recoverView();
    }

    @Override
    public void onClickTab(int tabIndex) {
        vpContent.setCurrentItem(tabIndex, false);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tlvTabSel.setCurSelectedIndex(position);

        rl_container.removeView(ivStrategy);
        rl_container.addView(ivStrategy);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!getActivity().isFinishing()) {
            FragmentTransaction mTransaction = getFragmentManager().beginTransaction();
            if (abroadStudyingFragment != null)
                mTransaction.remove(abroadStudyingFragment);
            if (beforeVisaFragment != null)
                mTransaction.remove(beforeVisaFragment);
            if (afterVisaFragment != null)
                mTransaction.remove(afterVisaFragment);
            if (applyAbroadStudyFragment != null)
                mTransaction.remove(applyAbroadStudyFragment);
            if (expatriateBeforeFragment != null)
                mTransaction.remove(expatriateBeforeFragment);
            if (expatriateInFragment != null)
                mTransaction.remove(expatriateInFragment);
            if (expatriateAfterFragment != null)
                mTransaction.remove(expatriateAfterFragment);
            if (investBeforeFragment != null)
                mTransaction.remove(investBeforeFragment);
            if (investAfterFragment != null)
                mTransaction.remove(investAfterFragment);
            if (travelbeforefragment != null)
                mTransaction.remove(travelbeforefragment);
            if (travelinfragment != null)
                mTransaction.remove(travelinfragment);
            if (travelafterfragment != null)
                mTransaction.remove(travelafterfragment);
            mTransaction.commit();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                posX=lastX=(int)event.getRawX();
                posY=lastY=(int)event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:

                int dx=(int)event.getRawX()-lastX;
                int dy=(int)event.getRawY()-lastY;

                int left=v.getLeft()+dx;
                int top=v.getTop()+dy;
                int right=v.getRight()+dx;
                int bottom=v.getBottom()+dy;

                if (left<0){
                    left=0;
                    right=left+v.getWidth();
                }

                if (right>screenWidth){
                    right=screenWidth;
                    left=right-v.getWidth();
                }
                if (top<0){
                    top=0;
                    bottom=top+v.getHeight();
                }
                if (bottom>screenHeight){
                    bottom=screenHeight;
                    top=bottom-v.getHeight();
                }

                if (Math.abs(event.getRawX()-posX)>10||Math.abs(event.getRawY()-posY)>10)
                    v.layout(left,top,right,bottom);

                lastX=(int)event.getRawX();
                lastY=(int)event.getRawY();

                break;
            case MotionEvent.ACTION_UP:
                //屏蔽点击事件
                if (Math.abs(event.getRawX()-posX)>10||Math.abs(event.getRawY()-posY)>10){
                    return true;
                }
                break;
        }
        return false;
    }
}

