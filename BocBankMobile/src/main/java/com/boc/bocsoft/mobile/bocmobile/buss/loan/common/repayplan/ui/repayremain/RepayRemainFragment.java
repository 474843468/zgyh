package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.ui.repayremain;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanDrawDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.ui.repayplanlistview.RepayPlanBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.ui.repayplanlistview.RepayPlanListView;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.model.RepayRemainViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.presenter.RepayRemainPresenter;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * 剩余还款查询
 * Created by liuzc on 2016/8/10.
 */
public class RepayRemainFragment extends BussFragment implements RepayRemainContract.View{
    private View mRootView = null;
    private PullToRefreshLayout pullToRefreshLayout = null; //上拉刷新页面
    private RepayPlanListView mRepayPlanListView = null;  //listview
    private TextView tvNodata = null; //无数据提示
    private RepayRemainPresenter mPresenter = null;

    private RepayRemainViewModel mViewModel = null; //页面显示数据
    //当前加载页码
    private int pageCurrentIndex = 0;
    //每页大小
    private final static int pageSize = ApplicationConst.PAGE_SIZE;
    //是否通过上拉刷新进行的查询请求
	private boolean isQueryByPullToRefresh = false;
    //对象
    private EloanDrawDetailModel mDerwdetail;
    
    private boolean bHasRequest = false; //记录是否请求过记录，如果已经请求过数据，则当页面有隐藏变为可见时，不主动请求
    private boolean bHasInitView = false; //view是否已经初始化，当页面展示时，如果未请求过网络数据，并且页面已经初始化，则发起网络请求
    private boolean bVisibleBeforeInit = false; //是否初始化前可见，是则主动请求数据
    private boolean bIsRequesting = false; //是否正在请求数据，正在请求的时候不可同时再次请求
    private String repayType = null; //还款方式
    
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = View.inflate(mContext, R.layout.boc_fragment_loan_repay_plan_content, null);
        bHasInitView = true; 
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
        pullToRefreshLayout = (PullToRefreshLayout) mRootView.findViewById(R.id.remain_query_view);
        mRepayPlanListView = (RepayPlanListView)mRootView.findViewById(R.id.lv_remain_query);
        tvNodata = (TextView)mRootView.findViewById(R.id.tvNoData);
    }

    @Override
    public void initData() {
        mPresenter = new RepayRemainPresenter(this);
        mViewModel = new RepayRemainViewModel();
        mViewModel.setRecordNumber(-1); //设置默认的记录数目为-1
        pageCurrentIndex = 0;
        
        if(bVisibleBeforeInit){
        	Log.d("request" +"initData", "网络请求");
            queryRepayRemain();
        }

    }
    
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
      //没有初始化前就可见
        if(isVisibleToUser && !bHasInitView){
            bVisibleBeforeInit = true;
        }
        //第一次可见时请求数据
        if(bHasInitView && isVisibleToUser && !bHasRequest){
            queryRepayRemain();
        }
        
    }
    

    //执行查询操作
    public void queryRepayRemain(){
//    	//如果正在请求数据，则返回
//    	if(bIsRequesting){
//    		return;
//    	}
    	
    	bIsRequesting = true;
    	bHasRequest = true;
    	if(!isQueryByPullToRefresh){
    		showLoadingDialog();
    	}
        
        RepayRemainViewModel model = new RepayRemainViewModel();
        model.setPageSize(String.valueOf(pageSize));

        model.setCurrentIndex(String.valueOf(pageCurrentIndex * pageSize));
        //// TODO: 2016/8/11
        model.setActNum(mDerwdetail.getLoanAccount());
        model.set_refresh("true");
        mPresenter.queryRepayRemainList(model);
    }
    
    public void setRemainData(EloanDrawDetailModel derwdetail) {
    	mDerwdetail = derwdetail;
    }

    public void setRepayType(String value){
        repayType = value;
        if(StringUtils.isEmptyOrNull(repayType)){
            repayType = "";
        }
    }

    //刷新页面数据数据
    private void refreshViewData(RepayRemainViewModel repayRemainViewModel){
        mViewModel.setRecordNumber(repayRemainViewModel.getRecordNumber());
        if( mViewModel.getList() == null){
            mViewModel.setList(new LinkedList<RepayRemainViewModel.ListBean>());
        }
        List<RepayRemainViewModel.ListBean> list =  repayRemainViewModel.getList();
        for(int i = 0; i < list.size(); i ++){
            mViewModel.getList().add(list.get(i));
        }
    }

    //刷新listView
	private void refreshListView(){
        if(mViewModel == null || mViewModel.getList() == null){
            return;
        }
        List<RepayPlanBean> listBean = new LinkedList<RepayPlanBean>();

        List<RepayRemainViewModel.ListBean> viewModelList = mViewModel.getList();
        for(int i = 0; i < viewModelList.size(); i ++){
            RepayPlanBean bean = new RepayPlanBean();
            RepayRemainViewModel.ListBean modelBean = viewModelList.get(i);

            String firstInfo = String.format("%s%s%s", modelBean.getRepayDate(),
                    getResources().getString(R.string.boc_loan_repay_shouldpay),
                    MoneyUtils.transMoneyFormat(modelBean.getRemainAmount(), mDerwdetail.getCurrency()));
            bean.setFirstLineInfo(firstInfo);

            String secLeftInfo = String.format("%s%s", getResources().getString(R.string.boc_repayment_Principal),
                    MoneyUtils.transMoneyFormat(modelBean.getRemainCapital(), mDerwdetail.getCurrency()));
            bean.setSecondLineLeftInfo(secLeftInfo);

            String secRightInfo = String.format("%s%s", getResources().getString(R.string.boc_repayment_Interest),
                    MoneyUtils.transMoneyFormat(modelBean.getRemainInterest(), mDerwdetail.getCurrency()));
            bean.setSecondLinerightInfo(secRightInfo);

            listBean.add(bean);
        }
        mRepayPlanListView.setData(listBean);
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
                    queryRepayRemain();
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
    public void queryRepayRemainListSuccess(RepayRemainViewModel repayRemainViewModel) {
    	bIsRequesting = false;
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
            	showNoData();
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
                View headerView = View.inflate(mContext, R.layout.boc_view_loan_repay_remain_title, null);
                TextView tvRemain = (TextView)headerView.findViewById(R.id.tvRemain);
                tvRemain.setText(String.format(getResources().getString(R.string.boc_loan_repay_remain_month), mDerwdetail.getRemainIssue()));
                
                TextView tvType = (TextView)headerView.findViewById(R.id.tvtype);
                //还款方式
                tvType.setText(repayType);

                mRepayPlanListView.addHeaderView(headerView);
            }
            refreshListView();
        }
    }

    @Override
    public void queryRepayRemainListFail(BiiResultErrorException biiResultErrorException) {
    	bIsRequesting = false;
        closeProgressDialog();
        if(biiResultErrorException == null || StringUtils.isEmptyOrNull(biiResultErrorException.getErrorCode())){
            showNoData();
            return;
        }
        //已经返回过数据，本次是通过上拉刷新方式加载的数据
        if (biiResultErrorException.getErrorCode().equals("validation.no.relating.acc")) {
        	 if(isQueryByPullToRefresh){
                 pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
             }
             else{
                 showNoData();
             }
        } else {
            showErrorDialog(biiResultErrorException.getMessage());
        }
    }
    
    @Override
	public void queryOtherReturn() {
    	bIsRequesting = false;
    	if(isQueryByPullToRefresh){
            pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
        }
        else{
            showNoData();
        }
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
    public void setPresenter(RepayRemainContract.Presenter presenter) {

    }
}
