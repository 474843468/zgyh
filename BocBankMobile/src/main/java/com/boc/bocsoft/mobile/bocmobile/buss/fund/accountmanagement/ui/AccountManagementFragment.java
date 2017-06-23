package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.container.ContainerPageAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.container.IContainer;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.TaAccountModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui.TaAccount.TaAccountView;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui.TransAccount.TransAccountView;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.model.InvstBindingInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.riskassessment.ui.FundRiskEvaluationResultFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;
import com.boc.bocsoft.mobile.framework.widget.tabLayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 基金-账户管理首页2 SlidingTab 包含两个View
 * Created by lyf 7084    2016-12-16
 */
//Fragment没有接口调用，故而继承BussFragment,而不用MvpBussFragment
public class AccountManagementFragment
        extends BussFragment
        implements AccountManagementContract.View {

    private View rootView = null;

    private TransAccountView mTransView;    // 交易账户View
    private TaAccountView mTaView;    // Ta账户View
    protected SlidingTabLayout lytTab;    // 可滑动切换Tab组件
    protected ViewPager viewPager;
    private ContainerPageAdapter mPageAdapter;    // viewPager的适配器

    private TextView tvAccountLevel;    // 账户风险等级，须传参
    private RelativeLayout rlyRiskAssessment;    // 风险评估布局

    private int currentTabindex = 0;    // 当前Tab位置0、1

    private InvstBindingInfoViewModel mBindingInfoModel = null;    // 基金账户信息：由首页传递DataModel

    private List<TaAccountModel.TaAccountBean> TAList = null;    // Ta账户列表viewModel

    public List<TaAccountModel.TaAccountBean> getTAList() {
        return this.TAList;
    }

    public void setTAList(List<TaAccountModel.TaAccountBean> TAList) {
        this.TAList = TAList;
    }

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_account_management, null);
        return rootView;
    }

    @Override
    public void initView() {
        tvAccountLevel = (TextView) rootView.findViewById(R.id.tvAccountLevel);
        rlyRiskAssessment = (RelativeLayout) rootView.findViewById(R.id.rlyRiskAssessment);
        rlyRiskAssessment.setVisibility(View.VISIBLE);
        // TODO：进入该页面才请求风险评估登记时候合适？
        viewPager = (ViewPager) rootView.findViewById(R.id.vpContent);
        lytTab = (SlidingTabLayout) rootView.findViewById(R.id.lyt_tab);
        mTitleBarView.setRightButton(getString(R.string.boc_fund_notice));    // 标题栏右侧按钮设置：权益须知
        mTitleBarView.getRightTextButton().setVisibility(View.VISIBLE);    // 默认显示基金交易账户
    }

    @Override
    public void initData() {
        currentTabindex = getArguments().getInt(DataUtils.KEY_ACCOUNT_CURRENT_TAB_INDEX);
        mBindingInfoModel = (InvstBindingInfoViewModel) getArguments().getSerializable(DataUtils.KEY_BINDING_INFO);

        mTransView = new TransAccountView(mContext);
        mTransView.setBindingInfo(mBindingInfoModel);

        mTransView.attach(this);
        mTaView = new TaAccountView(mContext);
        mTaView.setBindingInfo(mBindingInfoModel);
        mTaView.attach(this);
        List<IContainer> containerList = new ArrayList<>();
        containerList.add(mTransView);
        containerList.add(mTaView);
        List<String> titleList = new ArrayList<>();
        titleList.add(getString(R.string.boc_fund_trans_account));
        titleList.add(getString(R.string.boc_fund_ta_account));
        mPageAdapter = new ContainerPageAdapter(titleList, containerList);
        viewPager.setAdapter(mPageAdapter);
        lytTab.setViewPager(viewPager);
        lytTab.setCurrentTab(currentTabindex);
    }

    @Override
    public void setListener() {

        tvAccountLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(new FundRiskEvaluationResultFragment(AccountManagementFragment.class));
            }
        });

        lytTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    LogUtil.d("change to TRANS view");
                    mTitleBarView.getRightTextButton().setVisibility(View.VISIBLE);
                } else {
                    LogUtil.d("change to TA view");
                    mTitleBarView.getRightTextButton().setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onDestroy() {
        mTransView.onDestroy();
        mTaView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_fund_account_management);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return true;
    }

    @Override
    protected void titleRightIconClick() {
        Toast.makeText(getContext(), "权益须知", Toast.LENGTH_LONG).show();
//        start(new SelectFundCoFragment());
    }

    public SlidingTabLayout getLytTab() {
        return lytTab;
    }

}