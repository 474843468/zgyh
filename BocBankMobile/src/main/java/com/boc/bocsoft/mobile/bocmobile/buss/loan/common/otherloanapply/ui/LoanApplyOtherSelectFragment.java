package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.OnLineLoanCityBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.OnLineLoanProductBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.presenter.LoanApplyOtherSelectPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieDu on 2016/7/21.
 */
public class LoanApplyOtherSelectFragment extends BussFragment
        implements LoanApplyOtherSelectContract.View, AdapterView.OnItemClickListener {

    protected ListView lvLoantype;
    protected View tvTips;
    protected TextView tvSelect;
    private View rootView;
    private LoanApplyOtherSelectPresenter mLoanApplyOtherSelectPresenter;
    //无数据对应的布局,默认不显示
    private LinearLayout llyNodata;
    private TextView tvNoData;
    /**
     * 页面跳转数据传递
     */
    private final static int REQUEST_LOCATION = 1;
    private LoanTypeListAdapter mLoanTypeListAdapter;

    private OnLineLoanCityBean mCityBean;

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.fragment_other_loan_apply, null);
        return rootView;
    }

    @Override
    public void initView() {
        llyNodata = (LinearLayout) rootView.findViewById(R.id.llyNodata);
        tvNoData = (TextView) rootView.findViewById(R.id.tvNoData);
        lvLoantype = (ListView) rootView.findViewById(R.id.lv_loantype);
        tvTips = View.inflate(getContext(), R.layout.boc_footview_loan_apply_other, null);
        mLoanTypeListAdapter = new LoanTypeListAdapter(mContext);
        lvLoantype.setOnItemClickListener(this);
        lvLoantype.setAdapter(mLoanTypeListAdapter);
        tvSelect = (TextView) rootView.findViewById(R.id.tv_select);
        tvSelect.setVisibility(View.INVISIBLE);
        lvLoantype.setVisibility(View.INVISIBLE);
        mTitleBarView.setRightButton(getString(R.string.boc_loan_city_beijing));
        mTitleBarView.setRightButton(getResources().getDrawable(R.drawable.icon_location));
    }

    @Override
    public void initData() {
        mLoanApplyOtherSelectPresenter = new LoanApplyOtherSelectPresenter(this);

        OnLineLoanCityBean cityBean = new OnLineLoanCityBean();
        cityBean.setCityCode("217");
        cityBean.setCityName(getString(R.string.boc_loan_city_beijing));
        requestOnlineLoanProducts(cityBean);
    }

    /**
     * 是否显示标题栏
     */
    protected boolean isHaveTitleBarView() {
        return true;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_loan_apply_other);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    public void onDestroy() {
        mLoanApplyOtherSelectPresenter.unsubscribe();
        super.onDestroy();
    }

    @Override
    protected void titleRightIconClick() {
        startForResult(new LoanDistrictSelectFragment(), REQUEST_LOCATION);
    }

    @Override
    public void onLoadProductsSuccess(List<OnLineLoanProductBean> products) {
        refleshLayout(products);
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (requestCode == REQUEST_LOCATION && resultCode == RESULT_OK && data != null) {
            OnLineLoanCityBean cityBean =
                    data.getParcelable(LoanDistrictSelectFragment.SELECT_CITY);
            requestOnlineLoanProducts(cityBean);
        }
    }

    /**
     * 查找选择城市对应的贷款产品
     * @param cityBean
     */
    private void requestOnlineLoanProducts(OnLineLoanCityBean cityBean){
        if (cityBean != null) {
            mCityBean = cityBean;
            mTitleBarView.setRightButton(mContext, getResources().getDrawable(R.drawable.icon_location),
                    cityBean.getCityName());
            showLoadingDialog();
            mLoanApplyOtherSelectPresenter.getOnlineLoanProducts(cityBean.getCityCode());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OnLineLoanProductBean bean = mLoanTypeListAdapter.getItem(position);
        start(LoanApplyOtherInfoFillFragment.newInstance(mCityBean.getCityCode(), bean));
    }


    private void refleshLayout(List<OnLineLoanProductBean> products){
        closeProgressDialog();
        if(products == null ){
            products = new ArrayList<>();
        }
        mLoanTypeListAdapter.setDatas(products);
        boolean isShowNoData = products.isEmpty() ? true:false;
        refleshNoDataLayout(isShowNoData);
        if(isShowNoData == false) {
            lvLoantype.removeFooterView(tvTips);
            lvLoantype.addFooterView(tvTips);
            tvSelect.setVisibility(View.VISIBLE);
            lvLoantype.setVisibility(View.VISIBLE);

        } else {
            lvLoantype.removeFooterView(tvTips);
            tvSelect.setVisibility(View.INVISIBLE);
            lvLoantype.setVisibility(View.INVISIBLE);
        }
    }

    private void refleshNoDataLayout(boolean isShow){
        if(isShow) {
            llyNodata.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.VISIBLE);
            tvNoData.setText(getResources().getString(R.string.boc_loan_other_no_use_record));

        }else{
            if(llyNodata.getVisibility() == View.VISIBLE){
                llyNodata.setVisibility(View.GONE);
            }
        }
    }
}
