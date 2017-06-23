package com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.FundBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.LoadMoreListHelper;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullableListView;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.fundqueryoutlay.FundQueryOutlayParams;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.presenter.FundPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui.adapter.FundQryAdapter;
import com.boc.bocsoft.mobile.framework.utils.PublicConst;
import com.boc.bocsoft.mobile.framework.utils.regex.RegexResult;
import com.boc.bocsoft.mobile.framework.utils.regex.RegexUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 基金列表
 * Created by lxw on 2016/8/17 0017
 */
public class FundListViewFragment extends MvpBussFragment<FundContract.Presenter> implements FundContract.View,
        LoadMoreListHelper.LoadListListener<FundQueryOutlayParams>,
        AdapterView.OnItemClickListener, View.OnClickListener {

    private View mRoot;
    //基金的ListView
    PullableListView fundListView;
    //详情的上拉刷新
    PullToRefreshLayout transRefresh;
    EditText et_search_fund;
    Button btn_qry_fund;
    TextView empty_view;
    View refresh_view;
    FundContract.Presenter presenter;
    //无数据的TextView
//    private TextView tvNoData;
    private LoadMoreListHelper<FundQueryOutlayParams> mLoadMoreListHelper;

    protected View onCreateView(LayoutInflater inflater) {
        mRoot = inflater.inflate(R.layout.boc_fragment_main_home_fund_list, null);
        return mRoot;
    }

    private ArrayList<EditInvestModuleListViewFragment.InvestItem> funds;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        funds = (ArrayList) bundle.get(ApplicationConst.SELECTED_INVEST_FUND);
        }

    @Override
    public void initView() {
        fundListView = mViewFinder.find(R.id.content_view);
        transRefresh = mViewFinder.find(R.id.refresh_view);
        et_search_fund = mViewFinder.find(R.id.et_search_fund);
        btn_qry_fund = mViewFinder.find(R.id.btn_qry_fund);
        empty_view = mViewFinder.find(R.id.empty_view);
        refresh_view = mViewFinder.find(R.id.refresh_view);
    }

    @Override
    protected FundContract.Presenter initPresenter() {
        presenter = new FundPresenter(this);
        return presenter;
    }

    @Override
    public void setListener() {
        fundListView.setOnItemClickListener(this);
        btn_qry_fund.setOnClickListener(this);
        super.setListener();
    }

    @Override
    public void setPresenter(FundContract.Presenter presenter) {

    }

    FundQueryOutlayParams params;

    @Override
    public void initData() {
        super.initData();
        qryFund();
    }

    private void qryFund() {
        showLoadingDialog();
        RegexResult result = RegexUtils.check(mContext, "fundInput", et_search_fund.getText().toString(), false);
        if (!result.isAvailable) {
            showErrorDialog(result.errorTips);
            return;
        }
        params = getParams(params, 0, et_search_fund.getText().toString());
        mLoadMoreListHelper = new LoadMoreListHelper<>(params, transRefresh, this);
        getPresenter().getFundList(params);
    }

    private FundQueryOutlayParams getParams(FundQueryOutlayParams params, int index, String fundQry) {
        if (params == null) {
            params = new FundQueryOutlayParams();
        }
        params.setFundInfo(fundQry);
        params.setCompany("");
        params.setCurrencyCode("");
        params.setFundKind("00");
        params.setFundType("00");
        params.setRiskGrade("");
        params.setFundState("");
        params.setSortFlag("");
        params.setSortField("");
        params.setCurrentIndex(index);
        params.setPageSize(ApplicationConst.PAGE_SIZE);
        return params;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_title_fund);
    }

    private FundQryAdapter mAdapter;
//    private FundQueryOutlayResult mViewModel;

    @Override
    public void updateFundView(FundQueryOutlayParams fundQueryOutlay) {
        closeProgressDialog();
        if (mAdapter == null) {
            mAdapter = new FundQryAdapter(mContext);
            fundListView.setAdapter(mAdapter);
        }
        mLoadMoreListHelper.onLoadSuccess();
        List<FundBean> fundBeenList = fundQueryOutlay.getList();
        //过滤已选数据
        List<FundBean> removeList = new ArrayList<>();
        for (FundBean bean : fundBeenList) {
            for (EditInvestModuleListViewFragment.InvestItem selectBean : funds) {
                if (bean.getFundCode().equals(selectBean.getSaveId())) {
                    removeList.add(bean);
                }
            }
        }
        //删除已经添加的基金
        for (FundBean removeBean : removeList) {
            fundBeenList.remove(removeBean);
        }
        mAdapter.setDatas(fundBeenList);
        if (fundBeenList.size() == 0) {
            empty_view.setVisibility(View.VISIBLE);
            refresh_view.setVisibility(View.GONE);
        } else {
            empty_view.setVisibility(View.GONE);
            refresh_view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FundBean fundBean = mAdapter.getItem(position);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PublicConst.RESULT_DATA, fundBean);
        setFramgentResult(Activity.RESULT_OK, bundle);
        pop();
    }

    @Override
    public void loadData(FundQueryOutlayParams viewModel) {
        // 传过来的currentIndex是起始页数，进行处理
        FundQueryOutlayParams params;
        try {
            params = (FundQueryOutlayParams) viewModel.clone();
        } catch (Exception e) {
            return;
        }
        params.setCurrentIndex(0);
        params.setPageSize((viewModel.getCurrentIndex() + 1) * ApplicationConst.PAGE_SIZE);
        startPresenter();
        getPresenter().getFundList(params);
    }

    @Override
    public void updateFail() {
        mLoadMoreListHelper.onLoadFailed();
        closeProgressDialog();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_qry_fund) {
            qryFund();
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

    @Nullable
    @Override
    public View getView() {
        return mRoot;
    }

    @Override
    public void onStop() {
        super.onStop();
        hideSoftInput();
    }
}
