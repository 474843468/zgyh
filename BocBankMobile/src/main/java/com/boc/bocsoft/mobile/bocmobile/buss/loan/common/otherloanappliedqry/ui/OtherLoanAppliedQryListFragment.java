package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanappliedqry.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullableListView;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanappliedqry.ui.otherloanqrylistview.OtherLoanQryAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanappliedqry.ui.otherloanqrylistview.OtherLoanQryBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanappliedqry.OtherLoanAppliedQryConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanappliedqry.model.OtherLoanAppliedQryListViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanappliedqry.presenter.OtherLoanAppliedQryListPresenter;
import java.util.LinkedList;
import java.util.List;

/**
 * 其他类型贷款申请进度查询--列表显示页面
 * Created by liuzc on 2016/8/15.
 */
public class OtherLoanAppliedQryListFragment extends BussFragment implements
        OtherLoanQryAdapter.ClickListener, OtherLoanAppliedQryListContract.View{
    private View mRootView = null;
    private PullToRefreshLayout pullToRefreshLayout = null; //上拉刷新页面
    private PullableListView mLoanOnlineQryListView = null;  //listview
    private TextView tvNodata; //无数据提示
    private OtherLoanAppliedQryListPresenter mPresenter = null;

    private OtherLoanAppliedQryListViewModel mViewModel = null; //页面显示数据

    private OtherLoanQryAdapter mListViewAdapter = null;
    //当前加载页码
    private int pageCurrentIndex = 0;
    //每页大小
    private final static int pageSize = ApplicationConst.PAGE_SIZE;
    //是否通过上拉刷新进行的查询请求
    private boolean isQueryByPullToRefresh = false;

    private String paramName = null; //请求参数：姓名\企业名称
    private String paramPhoneOrEmail = null; //请求参数：联系电话\Email地址
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = View.inflate(mContext, R.layout.boc_fragment_loan_other_applied_qry_list, null);
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
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_loan_applied_query_result);
    }


    @Override
    public void initView() {
        pullToRefreshLayout = (PullToRefreshLayout) mRootView.findViewById(R.id.other_loan_online_query_view);
        mLoanOnlineQryListView = (PullableListView)mRootView.findViewById(R.id.lv_other_loan_online_query);
        tvNodata = (TextView)mRootView.findViewById(R.id.tvNoData);
    }

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        paramName = bundle.getString(OtherLoanAppliedQryConst.KEY_QRY_NAME);
        paramPhoneOrEmail = bundle.getString(OtherLoanAppliedQryConst.KEY_QRY_PHONE);

        mPresenter = new OtherLoanAppliedQryListPresenter(this);
        mViewModel = new OtherLoanAppliedQryListViewModel();
        mViewModel.setRecordNumber(-1); //设置默认的记录数目为-1
        pageCurrentIndex = 0;

        queryOtherLoanOnline();
    }

    //执行查询操作
    private void queryOtherLoanOnline(){
        showLoadingDialog();
        OtherLoanAppliedQryListViewModel model = new OtherLoanAppliedQryListViewModel();

        model.setName(paramName);
        if(OtherLoanAppliedQryConst.isEmailAddress(paramPhoneOrEmail)){
            model.setAppEmail(paramPhoneOrEmail);
        }
        else{
            model.setAppPhone(paramPhoneOrEmail);
        }

        model.setPageSize(pageSize);
        model.setCurrentIndex(pageCurrentIndex);
        model.set_refresh(true);

        mPresenter.queryOtherLoanOnlineList(model);
    }

    //刷新页面数据数据
    private void refreshViewData(OtherLoanAppliedQryListViewModel model){
        mViewModel.setRecordNumber(model.getRecordNumber());
        if( mViewModel.getList() == null){
            mViewModel.setList(new LinkedList<OtherLoanAppliedQryListViewModel.ListBean>());
        }
        List<OtherLoanAppliedQryListViewModel.ListBean> list =  model.getList();
        for(int i = 0; i < list.size(); i ++){
            mViewModel.getList().add(list.get(i));
        }
    }

    //刷新listView
    private void refreshListView(){
        if(mViewModel == null || mViewModel.getList() == null){
            return;
        }
        List<OtherLoanQryBean> listBean = new LinkedList<>();

        List<OtherLoanAppliedQryListViewModel.ListBean> viewModelList = mViewModel.getList();
        for(int i = 0; i < viewModelList.size(); i ++){
            OtherLoanQryBean bean = new OtherLoanQryBean();
            OtherLoanAppliedQryListViewModel.ListBean modelBean = viewModelList.get(i);

            String firstLineInfo = String.format("%s%s%s", modelBean.getApplyDate(),
                    getResources().getString(R.string.boc_apply),
                    modelBean.getProductName());
            bean.setFirstLineInfo(firstLineInfo);
            bean.setSecondLineLeftInfo(OtherLoanAppliedQryConst.getStatusDesc(mContext, modelBean.getLoanStatus()));
            bean.setSecondLinerightFirstInfo(OtherLoanAppliedQryConst.getCurrentcyDesc(mContext, modelBean.getCurrency()) );
            bean.setSecondLinerightSecInfo(MoneyUtils.transMoneyFormat(modelBean.getLoanAmount(),
                    OtherLoanAppliedQryConst.getCurrencyCode(modelBean.getCurrency())));

            listBean.add(bean);
        }

        if(mListViewAdapter == null){
            mListViewAdapter = new OtherLoanQryAdapter(mContext);
            mListViewAdapter.setOnClickListener(this);
            mLoanOnlineQryListView.setAdapter(mListViewAdapter);
        }

        mListViewAdapter.setData(listBean);
        mListViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void setListener() {
        //上拉加载
        pullToRefreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                //当已经加载过数据，并且已加载数据数目大于等于总数时，不在请求数据
                boolean bCanLoadMore = !(mViewModel.getList() != null
                        && mViewModel.getList().size() >= mViewModel.getRecordNumber());
                if(bCanLoadMore){
                    isQueryByPullToRefresh = true;
                    queryOtherLoanOnline();
                }
                else{
                    pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        mPresenter.unsubscribe();
        super.onDestroyView();
    }

    @Override
    public void setPresenter(OtherLoanAppliedQryListContract.Presenter presenter) {

    }

    @Override
    public void queryOtherLoanOnlineListSuccess(OtherLoanAppliedQryListViewModel repayRemainViewModel) {
        closeProgressDialog();

        if(pageCurrentIndex < 0){
            pageCurrentIndex = 0;
        }

        //是否无数据
        boolean bNoData = (repayRemainViewModel == null || repayRemainViewModel.getList() == null ||
                repayRemainViewModel.getList().size() <= 0);

        //无返回记录
        if(bNoData){
            if(isQueryByPullToRefresh){
                pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
            }
            else{
                //非下拉刷新加载，即第一次请求数据为空
                showNoDataView();
            }
        }
        else{//有记录返回
            refreshViewData(repayRemainViewModel);
            pageCurrentIndex ++;

            if(isQueryByPullToRefresh){
                pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
            }
            else{
                //第一次请求，添加listview头
                View headerView = View.inflate(mContext, R.layout.boc_view_loan_repay_history_title, null);
                mLoanOnlineQryListView.addHeaderView(headerView);
            }
            refreshListView();
        }
    }

    @Override
    public void queryOtherLoanOnlineListFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        //已经返回过数据，本次是通过上拉刷新方式加载的数据
        if(isQueryByPullToRefresh){
            pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
        }
        else{
            showNoDataView();
        }
    }

    //显示为空的view
    private void showNoDataView(){
        tvNodata.setVisibility(View.VISIBLE);
        tvNodata.setText(getResources().getString(R.string.boc_loan_no_applied_records));
        pullToRefreshLayout.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        String loanNumber = mViewModel.getList().get(position).getLoanNumber();
        bundle.putString(OtherLoanAppliedQryConst.KEY_LOAN_NUMBER, loanNumber);
        bundle.putString(OtherLoanAppliedQryConst.KEY_CONVERSATIONID, mPresenter.getConversationID());

        OtherLoanAppliedQryDetailFragment detailFragment = new OtherLoanAppliedQryDetailFragment();
        detailFragment.setArguments(bundle);
        start(detailFragment);
    }
}
