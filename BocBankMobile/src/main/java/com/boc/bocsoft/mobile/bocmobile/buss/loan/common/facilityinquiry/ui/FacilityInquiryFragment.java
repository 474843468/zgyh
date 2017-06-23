package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.FacilityInquiryConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.model.FacilityInquiryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.presenter.FacilityInquiryPrensenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.ui.adapter.FacilityListAdapter;
import com.boc.bocsoft.mobile.framework.widget.listview.OnItemClickListener;
import com.hp.hpl.sparta.Text;

/**
 * Created by XieDu on 2016/7/12.
 */
public class FacilityInquiryFragment extends BussFragment
        implements FacilityInquiryContact.View, OnItemClickListener {
    protected View rootView;
    protected RecyclerView listFacility;
    private FacilityListAdapter mFacilityListAdapter;
    private TextView tvDescription; //顶部提示
    private TextView tvNodata; //无数据提示

    private FacilityInquiryPrensenter mFacilityInquiryPrensenter;

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = View.inflate(mContext, R.layout.fragment_facility_inquiry, null);
        return rootView;
    }

    /**
     * 初始化数据
     */
    @Override
    public void initView() {
        listFacility = (RecyclerView) rootView.findViewById(R.id.list_facility);
        listFacility.setLayoutManager(new LinearLayoutManager(mContext));
        mFacilityListAdapter = new FacilityListAdapter(mContext);
        mFacilityListAdapter.setOnItemClickListener(this);
        listFacility.setAdapter(mFacilityListAdapter);

        tvDescription = (TextView)rootView.findViewById(R.id.tvDesc);
        tvNodata = (TextView)rootView.findViewById(R.id.tvNoData);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    protected String getTitleValue() {
        return getString(R.string.boc_facility_inquiry);
    }

    @Override
    public void initData() {
        mFacilityInquiryPrensenter = new FacilityInquiryPrensenter(this);
        showLoadingDialog();
        mFacilityInquiryPrensenter.requestPsnLOANQuotaQuery();
    }

    /**
     * 是否显示标题栏
     */
    protected boolean isHaveTitleBarView() {
        return true;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }


    @Override
    public void onDestroy() {
        mFacilityInquiryPrensenter.unsubscribe();
        super.onDestroy();
    }

    @Override
    public void onLoadSuccess(FacilityInquiryViewModel viewModel) {
        closeProgressDialog();
        mFacilityListAdapter.setDatas(viewModel.getFacilityInquiryList());
    }

    @Override
    public void onLoadFailed() {
        closeProgressDialog();

        tvNodata.setVisibility(View.VISIBLE);
        tvNodata.setText(getResources().getString(R.string.boc_no_facility_records));
        listFacility.setVisibility(View.GONE);
        tvDescription.setVisibility(View.GONE);

    }

    @Override
    public void onItemClick(View itemView, int position) {
        if(mFacilityListAdapter.getDatas() == null || mFacilityListAdapter.getDatas().size() <= position){
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(FacilityInquiryConst.KEY_FACILITY_INQUIRY_BEAN,
                mFacilityListAdapter.getDatas().get(position));
        FacilityDetailFragment detailFragment = new FacilityDetailFragment();
        detailFragment.setArguments(bundle);
        start(detailFragment);
    }
}
