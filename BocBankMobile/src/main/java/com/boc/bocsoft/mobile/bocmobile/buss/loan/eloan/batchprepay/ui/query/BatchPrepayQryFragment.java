package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.batchprepay.ui.query;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullableListView;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.batchprepay.model.BatchPrepayQryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.batchprepay.presenter.BatchPrepayQryPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.batchprepay.ui.submit.BatchPrepaySubmitFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanAccountListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui.EloanRepayDetailFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.singleprepay.ui.SinglePrepaySubmitFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;

import java.util.LinkedList;
import java.util.List;

/**
 * 剩余还款查询
 * Created by liuzc on 2016/8/10.
 */
public class BatchPrepayQryFragment extends BussFragment implements
        BatchPrepayQryContract.View, AdapterView.OnItemClickListener, BatchPrepayQryAdapter.OnSelectListener{
    private View rootView = null; //根视图
    protected PullToRefreshLayout layoutPullToRefresh; //上拉刷新布局
    protected PullableListView lstvRecords;  //可上拉的listview
    protected TextView tvTotalCapital;  //本金合计
    protected Button btnNext;  //下一步按钮
    protected LinearLayout llyContent;  //内容区布局
    protected LinearLayout llyTotalDesc;  //本息合计布局
    protected TextView tvNoData; //无数据提示


    private BatchPrepayQryPresenter mPresenter = null;

    private BatchPrepayQryModel mViewModel = null; //页面显示数据
    //当前加载页码
    private int pageCurrentIndex = 0;
    //每页大小
    private final static int pageSize = ApplicationConst.PAGE_SIZE;
    //是否通过上拉刷新进行的查询请求
    private boolean isQueryByPullToRefresh = false;

    private BatchPrepayQryAdapter mQryAdapter = null;

    //上层页面传过来的数据
    private EloanAccountListModel accountListModel = null;
    private String quoteNo = null; //额度编号
    private String currentDate = null; //当前日期
    private String repayaccount = null; //还款账号
    private String eLoanState = null; //E贷标识
    private String conversationID = null; //会话ID

    //已经获取的记录的个数
    private int returnedRecNumber = 0;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = View.inflate(mContext, R.layout.boc_fragment_loan_eloan_batch_prepay_list, null);
        return rootView;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    protected boolean isHaveTitleBarView() {
        return true;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_eloan_prepay_pagename);
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    public void initView() {
        lstvRecords = (PullableListView) rootView.findViewById(R.id.lstvRecords);
        lstvRecords.setDividerHeight(0);
        layoutPullToRefresh = (PullToRefreshLayout) rootView.findViewById(R.id.layoutPullToRefresh);
        tvTotalCapital = (TextView)rootView.findViewById(R.id.tvTotalCapital);
        btnNext = (Button) rootView.findViewById(R.id.btnNext);
        llyContent = (LinearLayout) rootView.findViewById(R.id.llyContent);
        llyTotalDesc = (LinearLayout) rootView.findViewById(R.id.llyTotalDesc);
        tvNoData = (TextView) rootView.findViewById(R.id.tvNoData);
    }

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        accountListModel = (EloanAccountListModel)bundle.getSerializable(EloanConst.ELON_QUOTE);

        //额度编号
        quoteNo = bundle.getString(EloanConst.LOAN_QUOTENO);
        //当前时间
        currentDate = bundle.getString(EloanConst.DATA_TIME);
        //还款账号
        repayaccount = bundle.getString(EloanConst.PEPAY_ACCOUNT);
        eLoanState = bundle.getString(EloanConst.ELOAN_QUOTETYPE);

        mPresenter = new BatchPrepayQryPresenter(this);
        mViewModel = new BatchPrepayQryModel();
        mViewModel.setMoreFlag("1");
        pageCurrentIndex = 0;

        updateTotalCapital();

        //上层页面传过来数据了，直接使用（作为第一页，后续页面再请求
        BatchPrepayQryModel model = new BatchPrepayQryModel();
        if(accountListModel != null){
            model.setMoreFlag(accountListModel.getMoreFlag());
            mViewModel.seteLoanState(accountListModel.geteLoanState());
            conversationID = accountListModel.getConversationId();

            mViewModel.setRecordNumber(accountListModel.getRecordNumber());

            //不直接使用accountListModel.getLoanList()，而是建立复本，防止后续过滤操作影响原始对象；
            if(accountListModel.getLoanList() != null){
                List<PsnLOANListEQueryResult.PsnLOANListEQueryBean> loanList = new LinkedList<PsnLOANListEQueryResult.PsnLOANListEQueryBean>();
                for(int i = 0; i < accountListModel.getLoanList().size(); i ++){
                    loanList.add(accountListModel.getLoanList().get(i));
                }
                model.setLoanList(loanList);
            }

        }
        queryBatchPrepayListSuccess(model);
    }

    //执行查询操作
    public void beginQuery() {
        if (!isQueryByPullToRefresh) {
            showLoadingDialog();
        }

        BatchPrepayQryModel model = new BatchPrepayQryModel();
        model.seteFlag("N");
        model.seteLoanState(eLoanState);
        model.setPageSize(pageSize);
        //CurrentIndex 必须用pageCurrentIndex * pageSize，出现bug，探索后发现，后台接口文档有误
        model.setCurrentIndex(pageCurrentIndex * pageSize);
        model.set_refresh("false");

        mPresenter.setConversationID(conversationID);
        mPresenter.queryBatchPrepayList(model);
    }


    //刷新页面数据数据
    private void refreshViewData(BatchPrepayQryModel viewModel) {
        if (mViewModel.getLoanList() == null) {
            mViewModel.setLoanList(new LinkedList<BatchPrepayQryModel.PsnLOANListEQueryBean>());
        }
        List<BatchPrepayQryModel.PsnLOANListEQueryBean> list = viewModel.getLoanList();
        for (int i = 0; i < list.size(); i++) {
            mViewModel.getLoanList().add(list.get(i));
        }
    }

    //刷新listView
    private void refreshListView() {
        if (mViewModel == null || mViewModel.getLoanList() == null) {
            return;
        }
        List<BatchPrepayQryBean> listBean = new
                LinkedList<BatchPrepayQryBean>();

        List<BatchPrepayQryModel.PsnLOANListEQueryBean> viewModelList = mViewModel.getLoanList();

        for (int i = 0; i < viewModelList.size(); i++) {
            BatchPrepayQryModel.PsnLOANListEQueryBean modelBean = viewModelList.get(i);
            BatchPrepayQryBean bean = new BatchPrepayQryBean();
            bean.setSelected(false);
            bean.setBean(modelBean);
            listBean.add(bean);
        }

        if(mQryAdapter == null){
            mQryAdapter = new BatchPrepayQryAdapter(mContext);
            mQryAdapter.setOnSelectListener(this);

            lstvRecords.setAdapter(mQryAdapter);
        }

        mQryAdapter.setDatas(listBean);
        mQryAdapter.notifyDataSetChanged();

        if(listBean.size() < 1){
            showNoData();
        }
    }

    @Override
    public void setListener() {
        //listview点击监听
        lstvRecords.setOnItemClickListener(this);
        //上拉加载
        layoutPullToRefresh.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                //当已经加载过数据，并且已加载数据数目大于等于总数时，不在请求数据
//                boolean bCanLoadMore = (mViewModel!= null
//                        && mViewModel.getMoreFlag() != null && mViewModel.getMoreFlag().equals("1"));
                //huixiobo说，服务端返回的moreflag没用，这里，只能通过getRecordNumber()来获取是否有更多记录
                boolean bCanLoadMore = (mViewModel != null && returnedRecNumber < mViewModel.getRecordNumber());
                if (bCanLoadMore) {
                    isQueryByPullToRefresh = true;
                    beginQuery();
                } else {
                    pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                }
            }
        });

        //点击 下一步 按钮
        btnNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onClickNext();
            }
        });
    }

    /**
     * 点击下一步操作，进入单笔或者批量提前还款页
     */
    private void onClickNext(){
        if(mQryAdapter == null){
            return;
        }

        int selectCount = mQryAdapter.getSelectedCount();
        if(selectCount < 1){
            showErrorDialog(getResources().getString(R.string.boc_eloan_select_prepay_record));
        }
        else if(selectCount == 1){
            gotoSinglePrepayPage();
        }
        else{
            gotoBatchPrepayPage();
        }
    }

    /**
     * 进入单笔提前还款页
     */


    private void gotoSinglePrepayPage(){
        PsnLOANListEQueryResult.PsnLOANListEQueryBean selItem =
                mQryAdapter.getSelectList().get(0).getBean();
        SinglePrepaySubmitFragment fragment = new SinglePrepaySubmitFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EloanConst.ELON_PREPAY, selItem);
        bundle.putString(EloanConst.LOAN_QUOTENO, quoteNo);
        bundle.putString(EloanConst.PEPAY_ACCOUNT, repayaccount);
        fragment.setArguments(bundle);
        start(fragment);
    }

    /**
     * 进入批量提前还款页
     */
    private void gotoBatchPrepayPage(){
        BatchPrepaySubmitFragment fragment = new BatchPrepaySubmitFragment();
        //构造bundle，把选中的对象信息传递给下一页面
        Bundle bundle = new Bundle();
        BatchPrepayQryModel model = new BatchPrepayQryModel();

        List<PsnLOANListEQueryResult.PsnLOANListEQueryBean> list =
                new LinkedList<PsnLOANListEQueryResult.PsnLOANListEQueryBean>();
        for(int i = 0; i < mQryAdapter.getSelectedCount(); i ++){
            list.add(mQryAdapter.getSelectList().get(i).getBean());
        }
        model.setLoanList(list);
        bundle.putSerializable(EloanConst.ELON_PREPAY, model);
        //额度编号
        bundle.putString(EloanConst.LOAN_QUOTENO, quoteNo);
        bundle.putString(EloanConst.PEPAY_ACCOUNT, repayaccount);
        fragment.setArguments(bundle);
        start(fragment);
    }

    @Override
    public void onDestroyView() {
        accountListModel = null;
        mPresenter.unsubscribe();
        super.onDestroyView();
    }

    /**
     * 展示无数据情况
     */
    private void showNoData() {
        tvNoData.setVisibility(View.VISIBLE);
        tvNoData.setText(getResources().getString(R.string.boc_eloan_no_prepay_records));
        llyContent.setVisibility(View.GONE);
    }


    @Override
    public void queryBatchPrepayListSuccess(BatchPrepayQryModel model) {
        closeProgressDialog();

        llyContent.setVisibility(View.VISIBLE);

        if (pageCurrentIndex < 0) {
            pageCurrentIndex = 0;
        }

        if(model != null){
            mViewModel.setMoreFlag(model.getMoreFlag());
            if(model.getLoanList() != null){
                returnedRecNumber += model.getLoanList().size();
            }
        }

        //是否无数据
        boolean bNoData = (model == null || model.getLoanList() == null ||
                model.getLoanList().size() <= 0);

        //无返回记录
        if (bNoData) {
            if (isQueryByPullToRefresh) {
                layoutPullToRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
            } else {
                //非下拉刷新加载，即第一次请求数据为空
                showNoData();
            }
        } else {//有记录返回
            filterNonPrepayRecords(model); //过滤掉不可提前还款记录；

            refreshViewData(model);
            pageCurrentIndex++;

            if (isQueryByPullToRefresh) {
                bNoData = (model == null || model.getLoanList() == null ||
                        model.getLoanList().size() <= 0);
                if(bNoData){
                    layoutPullToRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                }
                else{
                    layoutPullToRefresh.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
                }
            }
            else{
                //第一次请求，添加listview头
                View headerView = View.inflate(mContext, R.layout.boc_view_eloan_batch_prepay_qry_title, null);
                TextView tvMaxSel = (TextView)headerView.findViewById(R.id.tvMaxSel);
                tvMaxSel.setText(getResources().getString(R.string.boc_eloan_prepay_max_select, BatchPrepayQryAdapter.SELECT_COUNT_MAX));
                lstvRecords.addHeaderView(headerView);
            }
            refreshListView();
        }
    }

    /**
     * 从model中把不可提前还款的记录过滤掉
     * @param model
     */
    private void filterNonPrepayRecords(BatchPrepayQryModel model){
        if(model == null || model.getLoanList() == null || model.getLoanList().size() < 1){
            return ;
        }

        for(int i = model.getLoanList().size() - 1; i >= 0; i --){
            if(!canPrepay(model.getLoanList().get(i))){
                model.getLoanList().remove(i);
            }
        }
    }

    /**
     * item对应记录是否可提前还款
     * @param item
     * @return
     */
    private boolean canPrepay(PsnLOANListEQueryResult.PsnLOANListEQueryBean item){
        if(item == null){
            return false;
        }

        boolean bValidDate = true;
        try{
            //当前日起
            LocalDate curDate = LocalDate.parse(currentDate, DateFormatters.dateFormatter1);
            //到期日
            LocalDate endDate = LocalDate.parse(item.getLoanCycleMatDate(), DateFormatters.dateFormatter1);
            //到期日早于当期日期
            if(endDate.isBefore(curDate)){
                bValidDate = false;
            }
            else{
                bValidDate = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        if(item.getTransFlag() != null && item.getTransFlag().equals("advance")
                && item.getOverDueStat() != null && item.getOverDueStat().equals("00") && bValidDate){
            return true;
        }
        return false;
    }


    @Override
    public void queryBatchPrepayListFail(BiiResultErrorException biiResultErrorException) {
        //已经返回过数据，本次是通过上拉刷新方式加载的数据
        if (biiResultErrorException.getErrorCode().equals("validation.no.relating.acc")) {
            if (isQueryByPullToRefresh) {
                layoutPullToRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
            } else {
                showNoData();
            }
        } else {
            showErrorDialog(biiResultErrorException.getMessage());

        }
        closeProgressDialog();
    }

    @Override
    public void setPresenter(BatchPrepayQryContract.Presenter presenter) {

    }

    @Override
    public void onFirstSelect() {

    }

    @Override
    public void onUnselectAll() {

    }

    @Override
    public void onSelect(int position, boolean selectedState) {
        updateTotalCapital(); //更新汇总数据
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //posiiton为1，即点击listview的header时，不做处理
        if(position < 1){
            return;
        }

        PsnLOANListEQueryResult.PsnLOANListEQueryBean bean = mViewModel.getLoanList().get(position - 1);
        EloanRepayDetailFragment fragment = new EloanRepayDetailFragment();
        fragment.hidePrepayBtn();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EloanConst.ELON_PREPAY_DETAIL, bean);
        bundle.putString(EloanConst.PEPAY_ACCOUNT, repayaccount);
        bundle.putString(EloanConst.DATA_TIME, currentDate);

        fragment.setArguments(bundle);
        start(fragment);

//        mQryAdapter.onCheck(position - 1); //加了header，position比adapter中的索引值大1
    }

    /**
     * 更新本息合计文字
     */
    private void updateTotalCapital(){
        //无数据时隐藏
        if(mViewModel == null || mViewModel.getLoanList() == null ||
                mViewModel.getLoanList().size() < 1){
            llyTotalDesc.setVisibility(View.GONE);
            return;
        }

        llyTotalDesc.setVisibility(View.VISIBLE);
        String currencyCode = mViewModel.getLoanList().get(0).getCurrencyCode();
        String totalCapial = MoneyUtils.transMoneyFormat(mQryAdapter.getTotalSelectedCapital(), currencyCode);
        tvTotalCapital.setText(String.format("%s %s",
                DataUtils.getCurrencyDescByLetter(mContext, currencyCode),
                totalCapial));
    }
}
