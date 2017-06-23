package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAccountListAndDetailQuery.PsnLOANAccountListAndDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAccountListAndDetailQuery.PsnLOANAccountListAndDetailQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullableListView;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.FacilityInquiryConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.model.FacilityInquiryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.presenter.FacilityUseRecordPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.ui.adapter.FacilityUseRecQryAdapter;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.LinkedList;
import java.util.List;

/**额度使用记录查询页面
 * Created by liuzc on 2016/8/18.
 */
public class FacilityUseRecQryFragment extends BussFragment
        implements FacilityUseRecordQryContact.View, FacilityUseRecQryAdapter.ClickListener{
    private View mRootView = null;
    private PullToRefreshLayout pullToRefreshLayout = null; //上拉刷新页面
    private PullableListView mListView = null;  //listview
    private TextView tvNodata = null; //无数据提示
    private FacilityUseRecordPresenter mPresenter = null;

    private FacilityUseRecQryAdapter mQryAdapter = null;

    private PsnLOANAccountListAndDetailQueryResult mViewModel = null; //页面显示数据
    //当前加载页码
    private int pageCurrentIndex = 0;
    //每页大小
    private final static int pageSize = ApplicationConst.PAGE_SIZE;
    //是否通过上拉刷新进行的查询请求
    private boolean isQueryByPullToRefresh = false;

    private boolean bHasRequest = false; //记录是否请求过记录，如果已经请求过数据，则当页面有隐藏变为可见时，不主动请求
    private boolean bHasInitView = false; //view是否已经初始化，当页面展示时，如果未请求过网络数据，并且页面已经初始化，则发起网络请求
    private boolean bVisibleBeforeInit = false; //是否初始化前可见，是则主动请求数据

    //对象
    private FacilityInquiryViewModel.FacilityInquiryBean mFacilityBean;
    private String sRepayOverFlag = "N"; //结清标识，Y：结清； N：非结清；空：全部

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = View.inflate(mContext, R.layout.fragment_facility_use_record_qry, null);
        bHasInitView = true;
        return mRootView;
    }

    /**
     * 设置额度数据
     * @param bean
     */
    public void setFacilityBean(FacilityInquiryViewModel.FacilityInquiryBean bean){
        mFacilityBean = bean;
    }

    /**
     * 设置结清标识
     * @param flag
     */
    public void setRepayOverFlag(String flag){
        sRepayOverFlag = flag;
    }

    /**
     * 是否显示标题栏
     */
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    public void initView() {
        pullToRefreshLayout = (PullToRefreshLayout) mRootView.findViewById(R.id.layoutPullToRefresh);
        mListView = (PullableListView)mRootView.findViewById(R.id.lvContent);
        tvNodata = (TextView)mRootView.findViewById(R.id.tvNoData);
    }

    @Override
    public void initData() {
        mPresenter = new FacilityUseRecordPresenter(this);
        mViewModel = new PsnLOANAccountListAndDetailQueryResult();
        pageCurrentIndex = 0;

        if(bVisibleBeforeInit){
            queryRecords();
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
            queryRecords();
        }

    }


    //执行查询操作
    public void queryRecords(){
        bHasRequest = true;

        if(!isQueryByPullToRefresh){
            showLoadingDialog();
        }

        PsnLOANAccountListAndDetailQueryParams model = new PsnLOANAccountListAndDetailQueryParams();
        model.seteFlag(sRepayOverFlag);
        model.setQueryType("1");
        model.setQuotaNumber(mFacilityBean.getQuotaNumber());
        model.setCurrentIndex(pageCurrentIndex);
        model.setPageSize(pageSize);
        if(pageCurrentIndex == 0){
            model.set_refresh("true");
        }
        else{
            model.set_refresh("false");
        }
        mPresenter.getUseFacilityRecord(model);
    }


    //刷新页面数据数据
    private void refreshViewData(PsnLOANAccountListAndDetailQueryResult resultModel){
        mViewModel.setRecordNumber(resultModel.getRecordNumber());
        mViewModel.setMoreFlag(resultModel.getMoreFlag());
        if( mViewModel.getAccountList() == null){
            mViewModel.setAccountList(new LinkedList<PsnLOANAccountListAndDetailQueryResult.ListBean>());
        }
        List<PsnLOANAccountListAndDetailQueryResult.ListBean> list =  resultModel.getAccountList();
        for(int i = 0; i < list.size(); i ++){
            PsnLOANAccountListAndDetailQueryResult.ListBean bean = list.get(i);
            if(!StringUtils.isEmptyOrNull(bean.getLoanRate())){
                bean.setLoanRate(MoneyUtils.transRateFormat(bean.getLoanRate()));
            }
            mViewModel.getAccountList().add(bean);
        }
    }

    //刷新listView
    private void refreshListView(){
        if(mViewModel == null || mViewModel.getAccountList() == null){
            return;
        }

        if(mQryAdapter == null){
            mQryAdapter = new FacilityUseRecQryAdapter(mContext);
            mQryAdapter.setOnClickListener(this);

            if(sRepayOverFlag.equals("N")){
                mQryAdapter.setIsRepayOver(false);
            }
            else{
                mQryAdapter.setIsRepayOver(true);
            }

            mListView.setAdapter(mQryAdapter);
        }

        mQryAdapter.setData(mViewModel.getAccountList());
        mQryAdapter.notifyDataSetChanged();
    }

    @Override
    public void setListener() {
        //上拉加载
        pullToRefreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                //当已经加载过数据，并且已加载数据数目大于等于总数时，不在请求数据
                boolean bCanLoadMore = (mViewModel != null
                        && mViewModel.getMoreFlag() != null && mViewModel.getMoreFlag().equals("1"));
                if(bCanLoadMore){
                    isQueryByPullToRefresh = true;
                    queryRecords();
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

    /**
     * 展示无数据情况
     */
    private void showNoData(){
        tvNodata.setVisibility(View.VISIBLE);
        tvNodata.setText(getResources().getString(R.string.boc_no_facility_used_records));
        pullToRefreshLayout.setVisibility(View.GONE);
    }

    @Override
    public void onLoadSuccess(PsnLOANAccountListAndDetailQueryResult model) {
        closeProgressDialog();

        if(pageCurrentIndex < 0){
            pageCurrentIndex = 0;
        }

        //是否无数据
        boolean bNoData = (model == null || model.getAccountList() == null ||
                model.getAccountList().size() <= 0);


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
            refreshViewData(model);
            pageCurrentIndex ++;

            if(isQueryByPullToRefresh){
                pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
            }

            refreshListView();
        }
    }

    @Override
    public void onLoadFail(BiiResultErrorException biiResultErrorException) {
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
    public void onItemClick(int position) {

        Bundle bundle = new Bundle();
        bundle.putSerializable(FacilityInquiryConst.KEY_FACILITY_USE_REC_BEAN,
                mViewModel.getAccountList().get(position));
        bundle.putString(FacilityInquiryConst.KEY_REPAYOVER_FLAG, sRepayOverFlag);
        FacilityUseRecDetailFragment detailFragment = new FacilityUseRecDetailFragment();
        detailFragment.setArguments(bundle);
        start(detailFragment);
    }

    @Override
    public void setPresenter(FacilityUseRecordQryContact.Presenter presenter) {

    }
}
