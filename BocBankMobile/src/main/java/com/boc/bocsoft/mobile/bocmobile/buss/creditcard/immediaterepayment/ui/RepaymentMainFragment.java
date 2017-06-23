package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.ui;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.tablayout.SimpleTabLayout;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.ui.CrcdHomeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.adapter.RepaymentPageAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.model.CrcdBillInfoBean;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.presenter.RepayPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.presenter.RepaymentContract;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.base.NoScrollViewPager;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xwg on 16/11/22 11:07
 * 信用卡--立即还款--容器界面
 *  viewPager包含本币还款和外币还款两个fragment
 */
public class RepaymentMainFragment extends MvpBussFragment<RepayPresenter> implements RepaymentContract.RepaymentMainView, ViewPager.OnPageChangeListener {

    private View rootView;

    String[] title ;
    private NoScrollViewPager viewPager;
    private SimpleTabLayout tabIndicator;
    private String crcdAccNo,crcdAccId;


    public static final String CRCD_ACC_ID_KEY="CRCD_ACCOUNT_ID";
    public static final String CRCD_ACC_NO_KEY="CRCD_ACCOUNT_NO";

    private CrcdBillInfoBean localCurrencyInfo;//本币账单信息
    private CrcdBillInfoBean foreignCurrencyInfo;//外币账单信息

    private int currentIndex=0;
    private boolean reInitData=false;

    //人民币全额还款
    public static  final int PAY_WAY_LOCAL_FULL=0;
    //人民币最低还款
    public static  final int PAY_WAY_LOCAL_MINI_LIMIT=1;
    //人民币自定义还款
    public static  final int PAY_WAY_LOCAL_CUST=2;
    //人民币购汇全额还款
    public static  final int PAY_WAY_FOREIGN_RMB_FULL=3;
    //外币全额还款
    public static  final int PAY_WAY_FOREIGN_FULL=4;
    //外币最低还款
    public static  final int PAY_WAY_FOREIGN_MINI_LIMIT=5;
    //外币自定义 购汇还款
    public static  final int PAY_WAY_FOREIGN_CUST_CASHREMIT=6;
    //外币自定义 外币还款
    public static  final int PAY_WAY_FOREIGN_CUST=7;
    private RepaymentPageAdapter pageAdpter;
    private ArrayList<BussFragment> fragments = new ArrayList<BussFragment>();
    private ForeignCurrencyPaymentFragment foreignFragment;
    private LocalCurrencyPaymentFragment localFragment;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.fragment_crcd_repayment, null);
        return rootView;
    }

    @Override
    public void beforeInitView() {
        super.beforeInitView();
        AccountBean accountBean= getArguments().getParcelable(CrcdHomeFragment.CUR_ACCOUNT);
        if (accountBean!=null) {
            crcdAccId = accountBean.getAccountId();
            crcdAccNo = accountBean.getAccountNumber();
        }else {
            crcdAccId=getArguments().getString(CRCD_ACC_ID_KEY);
            crcdAccNo=getArguments().getString(CRCD_ACC_NO_KEY);
        }
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
        return getResources().getString(R.string.boc_creditcard_repayment);
    }

    @Override
    public void initView() {
        super.initView();
        viewPager = (NoScrollViewPager) rootView.findViewById(R.id.view_pager);
        viewPager.setNoScroll(true);
        viewPager.setOffscreenPageLimit(1);
        tabIndicator = (SimpleTabLayout) rootView.findViewById(R.id.tab_indicator);

        viewPager.addOnPageChangeListener(this);

    }

    @Override
    public void initData() {
        super.initData();

        showLoadingDialog();

        //查询该卡实时账单
        getPresenter().queryCrcdRTBill(crcdAccId);

    }

    @Override
    protected RepayPresenter initPresenter() {
        return new RepayPresenter(this);
    }


    @Override
    public void setPresenter(BasePresenter presenter) {

    }

    @Override
    public void queryCrcdRTBillSuccess(List<CrcdBillInfoBean> beanList) {
        if (beanList.size()==0){
            showErrorDialog("您暂无已出账单");
            return;
        }

        for (CrcdBillInfoBean bean:beanList){
            bean.setCrcdAccNo(crcdAccNo);
            bean.setCrcdAccId(crcdAccId);
            if (ApplicationConst.CURRENCY_CNY.equals(bean.getCurrency())){
                localCurrencyInfo=bean;
            } else {
                foreignCurrencyInfo=bean;
            }
        }

        if (reInitData){//刷新界面
            if (localFragment!=null)
                localFragment.reInitData();
            if (foreignFragment!=null){
                foreignFragment.startInitData();
            }
            return;
        }


        if (beanList.size()==1){//单币种
            initSinglePageView();
            startInit();
        }else{// 如果是双币种卡
//            getPresenter().crcdChargeOnRMBAccountQuery(crcdAccId);
            initDoublePageView();
            startInit();
        }

    }


    @Override
    public void crcdChargeOnRMBAccountQuery(boolean openFlag, String displayNum) {

//        if (openFlag){
//            initSinglePageView();
//        }else{
//            initDoublePageView();
//        }
//
//        startInit();
    }

    /**
     *  开始显示数据
     */
    private void startInit(){
        if (pageAdpter==null)
            pageAdpter=new RepaymentPageAdapter(getFragmentManager(),fragments,title);
        viewPager.setAdapter(pageAdpter);

        tabIndicator.setupWithViewPager(viewPager);
        tabIndicator.setHasIndicatorBackground(false);

        //查询该卡还款方式 自动还款或是主动还款
        getPresenter().queryCrcdPayway(crcdAccId);
    }

    /**
     *  初始化 单币种账单还款界面
     */
    private void initSinglePageView() {
        tabIndicator.setVisibility(View.GONE);
        if (localCurrencyInfo!=null&&ApplicationConst.CURRENCY_CNY.equals(localCurrencyInfo.getCurrency())){//如果是单人民币账单
            title=new String[]{getResources().getString(R.string.boc_creditcard_pay_rmb)};
            if (localFragment==null)
                localFragment=new LocalCurrencyPaymentFragment();
            fragments.add(localFragment);
        }else{//如果是外币账单
            title=new String[]{""};
            if (foreignFragment==null)
                foreignFragment=new ForeignCurrencyPaymentFragment();
            fragments.add(foreignFragment);
        }
    }

    /**
     *  初始化双币种账单还款界面
     */
    private void initDoublePageView() {
        if (localFragment==null)
            localFragment=new LocalCurrencyPaymentFragment();
        fragments.add(localFragment);
        if (foreignFragment==null)
            foreignFragment=new ForeignCurrencyPaymentFragment();
        fragments.add(foreignFragment);

        title=new String[2];
        title[0]=getResources().getString(R.string.boc_creditcard_pay_rmb);
        String foreignCurrency= PublicCodeUtils.getCurrency(mContext,foreignCurrencyInfo.getCurrency());
        title[1]=foreignCurrency.length()==2?"\u3000"+foreignCurrency+"\u3000":foreignCurrency;
        tabIndicator.setVisibility(View.VISIBLE);
    }

    public int getFragmentListSize(){
        return fragments.size();
    }

    @Override
    public void queryCrcdPayway(String localPayway, String foreignPayway) {

        if ("1".equals(localPayway) || "1".equals(foreignPayway)) {//自动还款
            if (foreignFragment != null)
                    foreignFragment.setTvPayway();
            if (localFragment != null)
                localFragment.setTvPayway();
        }
    }


    public CrcdBillInfoBean getLocalCurrencyInfo() {
        return localCurrencyInfo;
    }

    public CrcdBillInfoBean getForeignCurrencyInfo() {
        return foreignCurrencyInfo;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentIndex=position;
        if (position==1&&foreignFragment!=null)
            foreignFragment.startInitData();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void reInit() {
        super.reInit();

        reInitData=true;

        showLoadingDialog();

        //重新查询该卡实时账单
        getPresenter().queryCrcdRTBill(crcdAccId);
        if (foreignFragment!=null)
            foreignFragment.setInited(false);
        viewPager.setCurrentItem(currentIndex);


    }

    public int getCurrentIndex(){
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    /**
     *  根据还款方式返回对应字符
     */
    public String getPaywayString(int payway,String currency,String cashRemit){
        StringBuffer currencyBuffer = new StringBuffer();
        if (!StringUtil.isNullOrEmpty(currency)&&!ApplicationConst.CURRENCY_CNY.equals(currency)) {
            currencyBuffer.append(PublicCodeUtils.getCurrency(mContext,currency));
        }

        switch (payway){
            case RepaymentMainFragment.PAY_WAY_LOCAL_FULL:
                return getResources().getString(R.string.boc_creditcard_payway_full);
            case RepaymentMainFragment.PAY_WAY_LOCAL_MINI_LIMIT:
                return getResources().getString(R.string.boc_creditcard_payway_limit);
            case RepaymentMainFragment.PAY_WAY_LOCAL_CUST:
                return getResources().getString(R.string.boc_creditcard_repayment_custom);
            case RepaymentMainFragment.PAY_WAY_FOREIGN_RMB_FULL:
                return getResources().getString(R.string.boc_creditcard_payway_rmbtoforeign_full);
            case RepaymentMainFragment.PAY_WAY_FOREIGN_FULL:
                return currencyBuffer.append(cashRemit).append(getResources().getString(R.string.boc_creditcard_payway_full)).toString();
            case RepaymentMainFragment.PAY_WAY_FOREIGN_MINI_LIMIT:
                return currencyBuffer.append(cashRemit).append(getResources().getString(R.string.boc_creditcard_payway_limit)).toString();
            case RepaymentMainFragment.PAY_WAY_FOREIGN_CUST:
                return currencyBuffer.append(cashRemit).append(getResources().getString(R.string.boc_creditcard_repayment_custom)).toString();
            case RepaymentMainFragment.PAY_WAY_FOREIGN_CUST_CASHREMIT:
                return getResources().getString(R.string.boc_creditcard_payway_rmbtoforeign)+getString(R.string.boc_creditcard_repayment_custom);
            default:
                return "";

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!getActivity().isFinishing()) {
            FragmentTransaction mTransaction = getFragmentManager().beginTransaction();
            if (localFragment!=null)
                mTransaction.remove(localFragment);
            if (foreignFragment!=null)
                mTransaction.remove(foreignFragment);
            mTransaction.commit();
        }
    }
}
