package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.unsettledbills.ui;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.container.ContainerPageAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.container.IContainer;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.tablayout.SimpleTabLayout;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.ui.CrcdHomeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 未出账单——列表
 * Created by liuweidong on 2016/12/14.
 */

public class CrcdBillQueryNFragment extends BussFragment {

    private View rootView;
    private ViewPager viewPager;

    // 已入账
    private CrcdBillRecordView billRecordView;
    // 未入账
    private CrcdBillUnrecordView billUnrecordView;
    // 容器的pageAdapter
    private ContainerPageAdapter mPageAdapter;
    private AccountBean accountBean;
    private SimpleTabLayout tabIndicator;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = View.inflate(mContext, R.layout.boc_fragment_crcd_bill_n, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        tabIndicator = (SimpleTabLayout) rootView.findViewById(R.id.tab_indicator);
        viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);

        accountBean= getArguments().getParcelable(CrcdHomeFragment.CUR_ACCOUNT);
    }

    @Override
    public void initData() {
        billRecordView = new CrcdBillRecordView(mContext);
        billRecordView.attach(this);
        billRecordView.setUserVisibleHint(true);

        billUnrecordView = new CrcdBillUnrecordView(mContext);
        billUnrecordView.attach(this);
        billUnrecordView.setUserVisibleHint(false);

        List<IContainer> containerList = new ArrayList<>();
        containerList.add(billRecordView);
        containerList.add(billUnrecordView);
        List<String> titleList = new ArrayList<>();
        titleList.add(getResources().getString(R.string.boc_crcd_bill_n_tab_1));
        titleList.add(getResources().getString(R.string.boc_crcd_bill_n_tab_2));
        mPageAdapter = new ContainerPageAdapter(titleList, containerList);
        viewPager.setAdapter(mPageAdapter);
        tabIndicator.setupWithViewPager(viewPager);
        tabIndicator.setHasIndicatorBackground(false);

        billRecordView.startLoadData(accountBean);
        billUnrecordView.setData(accountBean);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        billRecordView.onDestroy();
        billUnrecordView.onDestroy();
    }

    @Override
    public void setListener() {
        super.setListener();
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_crcd_bill_n_title);
    }

}
