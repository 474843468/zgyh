package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.recommend.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOcrmProductQuery.PsnOcrmProductQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOcrmProductQuery.PsnOcrmProductQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.recommend.adapter.FundRecommendListAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.recommend.presenter.FundRecommendPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.model.RepayRemainViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.ui.repayplanlistview.RepayPlanBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanDrawDetailModel;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * 基金推荐页面
 * Created by liuzc on 2016/8/10.
 */
public class FundRecommendFragment extends MvpBussFragment<FundRecommendContract.Presenter> implements FundRecommendContract.View{
    private View mRootView = null;
    private PullToRefreshLayout pullToRefreshLayout = null; //上拉刷新页面
    private ListView lsvRecommend = null;  //listview
    private TextView tvNodata = null; //无数据提示

    private FundRecommendListAdapter adapter = null;

    private PsnOcrmProductQueryResult mViewModel = null; //页面显示数据
    //当前加载页码
    private int pageCurrentIndex = 0;
    //每页大小
    private final static int pageSize = ApplicationConst.PAGE_SIZE;
    //是否通过上拉刷新进行的查询请求
	private boolean isQueryByPullToRefresh = false;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = View.inflate(mContext, R.layout.boc_fragment_fund_recommend, null);
        return mRootView;
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
        return getString(R.string.boc_fund_rec);
    }

    @Override
    public void initView() {
        pullToRefreshLayout = (PullToRefreshLayout) mRootView.findViewById(R.id.layoutPullToRefresh);
        lsvRecommend = (ListView) mRootView.findViewById(R.id.lsvRecommend);
        adapter = new FundRecommendListAdapter(getContext());
        lsvRecommend.setAdapter(adapter);

        tvNodata = (TextView)mRootView.findViewById(R.id.tvNoData);
    }

    @Override
    public void initData() {
        mViewModel = new PsnOcrmProductQueryResult();
        mViewModel.setRecordNumber("-1"); //设置默认的记录数目为-1
        pageCurrentIndex = 0;

        queryRecommendList();
    }
    


    //执行查询操作
    public void queryRecommendList(){
    	if(!isQueryByPullToRefresh){
    		showLoadingDialog();
    	}

        PsnOcrmProductQueryParams params = new PsnOcrmProductQueryParams();
        params.setProtpye("01");
        params.setTradeType("");
        params.setCurrentIndex(String.valueOf(pageCurrentIndex));
        params.setPageSize(String.valueOf(ApplicationConst.FUND_PAGE_SIZE));

        getPresenter().queryFundsRecommend(params);
    }

    //刷新页面数据数据
    private void refreshViewData(PsnOcrmProductQueryResult resultModel){
        mViewModel.setRecordNumber(resultModel.getRecordNumber());
        if( mViewModel.getResultList() == null){
            mViewModel.setResultList(new LinkedList<PsnOcrmProductQueryResult.OcrmDetail>());
        }
        List<PsnOcrmProductQueryResult.OcrmDetail> list =  resultModel.getResultList();
        for(int i = 0; i < list.size(); i ++){
            mViewModel.getResultList().add(list.get(i));
        }
    }

    //刷新listView
	private void refreshListView(){
        if(mViewModel == null || mViewModel.getResultList() == null){
            return;
        }

        adapter.setData(mViewModel);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setListener() {
        //上拉加载
        pullToRefreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                //当已经加载过数据，并且已加载数据数目大于等于总数时，不在请求数据

                try{
                    int recordNumber = 0;
                    if(!StringUtils.isEmptyOrNull(mViewModel.getRecordNumber()) &&
                            !mViewModel.getRecordNumber().equals("null")){
                        recordNumber = Integer.valueOf(mViewModel.getRecordNumber());
                    }
                    boolean bCanLoadMore = !(mViewModel.getResultList() != null
                            && mViewModel.getResultList().size() >= recordNumber);
                    if(bCanLoadMore){
                        isQueryByPullToRefresh = true;
                        queryRecommendList();
                    }
                    else{
                        pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    
    /**
     * 展示无数据情况
     */
    private void showNoData(){
        tvNodata.setVisibility(View.VISIBLE);
        tvNodata.setText(getResources().getString(R.string.boc_loan_repay_no_remain));
        pullToRefreshLayout.setVisibility(View.GONE);
    }

    @Override
    public void setPresenter(FundRecommendContract.Presenter presenter) {

    }

    @Override
    protected FundRecommendContract.Presenter initPresenter() {
        return new FundRecommendPresenter(this);
    }

    @Override
    public void queryFundsRecommendSuccess(PsnOcrmProductQueryResult result) {
        closeProgressDialog();

        if(pageCurrentIndex < 0){
            pageCurrentIndex = 0;
        }

        //是否无数据
        boolean bNoData = (result == null || result.getResultList() == null ||
                result.getResultList().size() <= 0);

        //无返回记录
        if(bNoData){
            if(isQueryByPullToRefresh){
                pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
            }
            else{
                //非下拉刷新加载，即第一次请求数据为空
                showNoData();
            }
        }
        else{//有记录返回
            refreshViewData(result);
            pageCurrentIndex ++;

            if(isQueryByPullToRefresh){
                pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
            }

            refreshListView();
        }
    }

    @Override
    public void queryFundsRecommendFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        if(biiResultErrorException == null || StringUtils.isEmptyOrNull(biiResultErrorException.getErrorCode())){
            showNoData();
            return;
        }
        //已经返回过数据，本次是通过上拉刷新方式加载的数据
        if(isQueryByPullToRefresh){
            pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
        }
        else{
            showNoData();
        }
    }
}
