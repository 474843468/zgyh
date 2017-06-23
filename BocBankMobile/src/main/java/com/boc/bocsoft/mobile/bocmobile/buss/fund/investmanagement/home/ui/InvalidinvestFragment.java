package com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.PinnedSectionListView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.ShowListConst;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.adapter.ShowListAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.bean.ShowListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.InvalidinvestModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.InvestParamsModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.presenter.InvestMgPresenter;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louis.hui on 2016/11/28.
 * 失效定投管理
 */
public class InvalidinvestFragment extends MvpBussFragment<InvestMgPresenter> implements InvestMgContract.InvestView {

    protected View rootView;
    protected PinnedSectionListView PinnedSectionlv;
    /**交易流水适配器*/
    private ShowListAdapter showListAdapter;
    /**list日期bean*/
    private List<ShowListBean> showViewBeanList;
    protected PullToRefreshLayout invalidRefresh;
    protected TextView invalidTv;
    /**失效列表数据*/
    private List<InvalidinvestModel.ListBean> mInvalidList;
    /**当前加载页码*/
    private int pageCurrentIndex = 1;
    /**每页大小*/
    private final static int pageSize = ApplicationConst.PAGE_SIZE;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fund_invalidinvest, null);
        return rootView;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        PinnedSectionlv = (PinnedSectionListView) rootView.findViewById(R.id.PinnedSectionlv);
        invalidRefresh = (PullToRefreshLayout) rootView.findViewById(R.id.invalidRefresh);
        invalidTv = (TextView) rootView.findViewById(R.id.invalidTv);

        showListAdapter = new ShowListAdapter(getActivity(), -1);
        PinnedSectionlv.saveAdapter(showListAdapter);
        PinnedSectionlv.setShadowVisible(false);
        PinnedSectionlv.setAdapter(showListAdapter);

    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    public void initData() {
        //失效网络请求
        if (getUserVisibleHint()) {
            invaildinvestData();
        }
    }

    /**
     * 058网络请求数据
     */
    private void invaildinvestData() {
        InvestParamsModel paramsModel = new InvestParamsModel();
        paramsModel.setDtFlag("");
        paramsModel.setPageSize(pageSize + "");
        paramsModel.setCurrentIndex(String.valueOf(pageCurrentIndex * pageSize));
        paramsModel.set_refresh("true");
        showLoadingDialog();
        getPresenter().queryInvalid(paramsModel);
    }

    @Override
    public void setListener() {
        invalidRefresh.setOnLoadListener(new  PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

            }
        });

        PinnedSectionlv.setListener(new PinnedSectionListView.ClickListener() {
            @Override
            public void onItemClickListener(int position) {
                InvalidDetailFragment invalidDetailFragment = new InvalidDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(InvestConst.INVALID_DETAIL, mInvalidList.get(position));
                invalidDetailFragment.setArguments(bundle);
                start(invalidDetailFragment);
            }
        });
    }

    @Override
    public void fundInvalidFail(BiiResultErrorException biiResultErrorException) {

    }

    @Override
    public void fundInvalidSuccess(InvalidinvestModel invalidModel) {
        if (invalidModel != null) {
            mInvalidList = invalidModel.getList();
        }
        setInvalidInvestData();
        showListAdapter.setData(showViewBeanList);
    }

    /**
     *有效定投页面列表添加数据
     */
    private void setInvalidInvestData(){
        if (mInvalidList != null && mInvalidList.size() > 0) {
            showViewBeanList = new ArrayList<>();
            for (int i = 0; i< mInvalidList.size(); i++ ) {
                LocalDate localDate = LocalDate.parse(mInvalidList.get(i).getApplyDate(),
                        DateFormatters.dateFormatter1);
                String formatTime = "";
                String tempTime = "";
                if (localDate != null) {
                    formatTime = localDate.format(DateFormatters.monthFormatter1);
                }
                if (i > 0) {
                    tempTime = LocalDate.parse(mInvalidList.get(i - 1).getApplyDate(),
                            DateFormatters.dateFormatter1).format(DateFormatters.monthFormatter1);
                }

                if (tempTime.equals(formatTime)) {
                    ShowListBean item = new ShowListBean();
                    item.type = ShowListBean.CHILD;
                    item.setTitleID(ShowListConst.TITLE_WEALTH);
                    item.setChangeColor(true);
                    item.setTime(localDate);

                    //基金名称+ 基金编码
                    item.setContentLeftAbove(mInvalidList.get(i).getFundName() + " " +
                            mInvalidList.get(i).getFundCode());
                    //交易类型
                    item.setContentLeftBelow(mInvalidList.get(i).getApplyType());
                    //交易状态
                    item.setContentRightAbove(mInvalidList.get(i).getStatus());
                    //交易份额
                    item.setContentRightBelow(MoneyUtils.transMoneyFormat(mInvalidList.get(i).getTransCount(),
                            mInvalidList.get(i).getCurrencyCode()));
                    showViewBeanList.add(item);

                } else {
                    for (int j = 0; j < 2; j++) {
                        ShowListBean item = new ShowListBean();
                        if (j == 0) {
                            item.type = ShowListBean.GROUP;
                            item.setGroupName(formatTime);
                            item.setTime(localDate);
                        } else {
                            item.type = ShowListBean.CHILD;
                            item.setTitleID(ShowListConst.TITLE_WEALTH);
                            item.setTime(localDate);
                            item.setChangeColor(true);
                            //交易基金名称+ 基金编码
                            item.setContentLeftAbove(mInvalidList.get(i).getFundName() + " " +
                                    mInvalidList.get(i).getFundCode());
                            //交易类型
                            item.setContentLeftBelow(mInvalidList.get(i).getApplyType());
                            //交易状态
                            item.setContentRightAbove(mInvalidList.get(i).getStatus());
                            //交易份额
                            item.setContentRightBelow(MoneyUtils.transMoneyFormat(mInvalidList.get(i).getTransCount(),
                                    mInvalidList.get(i).getCurrencyCode()));
                        }
                        showViewBeanList.add(item);
                    }
                }
            }
        }
    }


    @Override
    public void setPresenter(InvestMgContract.Presenter presenter) {

    }

    @Override
    protected InvestMgPresenter initPresenter() {
        return new InvestMgPresenter(this);
    }
}
