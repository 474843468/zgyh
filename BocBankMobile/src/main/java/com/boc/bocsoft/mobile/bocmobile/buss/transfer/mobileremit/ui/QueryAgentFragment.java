package com.boc.bocsoft.mobile.bocmobile.buss.transfer.mobileremit.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.ListModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullableListView;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.mobileremit.adapter.ListVerticalAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.mobileremit.model.AgentViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.mobileremit.presenter.MobileRemitPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * 代理点查询页面
 * Created by liuweidong on 2016/7/26.
 */
public class QueryAgentFragment extends MvpBussFragment<MobileRemitContract.Presenter> implements MobileRemitContract.AgentView {
    private View rootView;
    private PullToRefreshLayout pullToRefreshLayout;
    private PullableListView listView;
    private TextView txtNoData;
    private ListVerticalAdapter listVerticalAdapter;

    private AgentViewModel mViewModel;// 接口数据
    private int curIndex = 0;// 当前页起始索引
    private String curProvinceNum = "";// 当前省联行号
    private boolean isPullToRefresh;// 是否上拉加载

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = View.inflate(mContext, R.layout.boc_fragment_transfer_agent, null);
        return rootView;
    }

    @Override
    public void initView() {
        pullToRefreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.pull_refresh);
        listView = (PullableListView) rootView.findViewById(R.id.listview);
        txtNoData = (TextView) rootView.findViewById(R.id.txt_no_data);
        listVerticalAdapter = new ListVerticalAdapter(mContext);
        listView.setAdapter(listVerticalAdapter);
    }

    @Override
    public void initData() {
        mViewModel = new AgentViewModel();
        // TODO: 2016/8/16 定位
        mTitleBarView.setRightButton("北京");
        curProvinceNum = "40142";
        queryAgent(curProvinceNum);
    }

    @Override
    public void setListener() {
        pullToRefreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (mViewModel.getList() != null) {
                    if (mViewModel.getList().size() < mViewModel.getRecordNumber()) {//数据小于记录数再次请求
                        isPullToRefresh = true;
                        getPresenter().queryAgent(curProvinceNum, String.valueOf(curIndex), String.valueOf(ApplicationConst.PAGE_SIZE));
                    } else {
                        pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                    }
                } else {
                    isPullToRefresh = true;
                    curIndex = 0;//重置当前索引
                    getPresenter().queryAgent(curProvinceNum, String.valueOf(curIndex), String.valueOf(ApplicationConst.PAGE_SIZE));
                }
            }
        });
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_transfer_mobile_remit_agent_title);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected void titleRightIconClick() {
        startForResult(new SelectZoneFragment(), 101);
    }

    @Override
    protected MobileRemitContract.Presenter initPresenter() {
        return new MobileRemitPresenter(this);
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        ListModel listModel = (ListModel) data.getSerializable("ListModel");
        mTitleBarView.setRightButton(listModel.getName());//地区名称设置
        curProvinceNum = listModel.getNameID();
        curIndex = 0;//重置当前索引
        mViewModel.getList().clear();//清空数据
        setData(mViewModel.getList());
        queryAgent(curProvinceNum);
    }

    /**
     * 更新页面数据
     *
     * @param list
     */
    public void setData(List<ListModel> list) {
        listVerticalAdapter.setData(list);
        listVerticalAdapter.notifyDataSetChanged();
    }

    /**
     * 代理点查询
     *
     * @param prvIbkNum
     */
    private void queryAgent(String prvIbkNum) {
        showLoadingDialog();
        getPresenter().queryAgent(prvIbkNum, String.valueOf(curIndex), String.valueOf(ApplicationConst.PAGE_SIZE));
    }

    /**
     * 网络请求成功更新数据
     *
     * @param agentViewModel
     */
    private void updateData(AgentViewModel agentViewModel) {
        List<ListModel> newList = agentViewModel.getList();
        List<ListModel> oldList = mViewModel.getList();
        if (oldList == null) {
            oldList = new ArrayList<>();
        }
        oldList.addAll(newList);
        mViewModel.setList(oldList);
        mViewModel.setRecordNumber(agentViewModel.getRecordNumber());
        setData(oldList);
    }

    /**
     * 查询代理点失败
     *
     * @param biiResultErrorException
     */
    @Override
    public void queryAgentFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        isPullToRefresh = false;
        pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.FAIL);
    }

    /**
     * 查询代理点成功
     *
     * @param agentViewModel
     */
    @Override
    public void queryAgentSuccess(AgentViewModel agentViewModel) {
        closeProgressDialog();
        if (agentViewModel.getRecordNumber() == 0) {// 没有数据
            pullToRefreshLayout.setVisibility(View.GONE);
            txtNoData.setVisibility(View.VISIBLE);
        } else {
            pullToRefreshLayout.setVisibility(View.VISIBLE);
            txtNoData.setVisibility(View.GONE);
            curIndex += ApplicationConst.PAGE_SIZE;
            updateData(agentViewModel);
            if (isPullToRefresh) {
                pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
                isPullToRefresh = false;
            }
        }
    }

    @Override
    public void setPresenter(MobileRemitContract.Presenter presenter) {

    }
}
