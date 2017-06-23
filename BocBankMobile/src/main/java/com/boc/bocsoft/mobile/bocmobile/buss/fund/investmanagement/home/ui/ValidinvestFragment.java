package com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.InvestParamsModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.ValidinvestModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.presenter.InvestMgPresenter;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huixiaobo on 2016/11/28.
 * 有效定投
 */
public class ValidinvestFragment extends MvpBussFragment<InvestMgPresenter>
        implements InvestMgContract.ValidinvestView {

    /**
     * 交易流水列表显示组件
     */
    protected View rootView;
    protected PinnedSectionListView PinnedSectionlv;
    protected ImageView pullupIcon;
    protected ImageView loadingIcon;
    protected TextView loadstateTv;
    protected ImageView resultIcon;
    protected RelativeLayout loadmoreView;
    protected PullToRefreshLayout validRefresh;
    protected TextView validTv;
    /**
     * 交易流水适配器
     */
    private ShowListAdapter showListAdapter;
    /**
     * list日期bean
     */
    private List<ShowListBean> showViewBeanList;
    /***/
    private List<ValidinvestModel.ListBean> mValidinvestList;
    /**当前加载页码*/
    private int pageCurrentIndex = 0;
    /**每页大小*/
    private final static int pageSize = ApplicationConst.PAGE_SIZE;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fund_validinvest, null);
        return rootView;
    }


    @Override
    public void initView() {
        super.initView();
        PinnedSectionlv = (PinnedSectionListView) rootView.findViewById(R.id.PinnedSectionlv);
        validRefresh = (PullToRefreshLayout) rootView.findViewById(R.id.validRefresh);
        validTv = (TextView) rootView.findViewById(R.id.validTv);

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
        super.initData();

        investListData();
    }

    /**
     * 011网络请求数据
     */
    private void investListData() {
        InvestParamsModel paramsModel = new InvestParamsModel();
        paramsModel.setDtFlag("");
        paramsModel.setPageSize(pageSize + "");
        paramsModel.setCurrentIndex(String.valueOf(pageCurrentIndex * pageSize));
        paramsModel.set_refresh("true");
        showLoadingDialog();
        getPresenter().queryValidinvest(paramsModel);
    }
    @Override
    public void setListener() {
        super.setListener();

        validRefresh.setOnLoadListener(new  PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

            }
        });

        PinnedSectionlv.setListener(new PinnedSectionListView.ClickListener() {
            @Override
            public void onItemClickListener(int position) {
                ValidDetailFragment validDetailFragment = new ValidDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(InvestConst.VALID_DETAIL, mValidinvestList.get(position));
                validDetailFragment.setArguments(bundle);
                start(validDetailFragment);
            }
        });
    }

    @Override
    protected InvestMgPresenter initPresenter() {
        return new InvestMgPresenter(this);
    }

    @Override
    public void setPresenter(InvestMgContract.Presenter presenter) {

    }

    @Override
    public void fundValidinvestFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void fundValidinvestSuccess(ValidinvestModel validinvestModel) {
        closeProgressDialog();
        if (validinvestModel != null) {
            mValidinvestList = validinvestModel.getList();
        }

        setValidInvestData();

    }

    /**
     *有效定投页面列表添加数据
     */
    private void setValidInvestData(){
        if (mValidinvestList != null && mValidinvestList.size() > 0) {
            showViewBeanList = new ArrayList<>();
            for (int i = 0; i< mValidinvestList.size(); i++ ) {
                LocalDate localDate = LocalDate.parse(mValidinvestList.get(i).getApplyDate(),
                        DateFormatters.dateFormatter1);
                String formatTime = "";
                String tempTime = "";
                if (localDate != null) {
                    formatTime = localDate.format(DateFormatters.monthFormatter1);
                }
                if (i > 0) {
                    tempTime = LocalDate.parse(mValidinvestList.get(i - 1).getApplyDate(),
                            DateFormatters.dateFormatter1).format(DateFormatters.monthFormatter1);
                }

                if (tempTime.equals(formatTime)) {
                    ShowListBean item = new ShowListBean();
                    item.type = ShowListBean.CHILD;
                    item.setTitleID(ShowListConst.TITLE_WEALTH);
                    item.setChangeColor(true);
                    item.setTime(localDate);

                    //基金名称+ 基金编码
                    item.setContentLeftAbove(mValidinvestList.get(i).getFundName() + " " +
                            mValidinvestList.get(i).getFundCode());
                    //交易类型
                    item.setContentLeftBelow(mValidinvestList.get(i).getTransType());
                    //交易状态
                    item.setContentRightAbove(mValidinvestList.get(i).getRecordStatus());
                    //交易份额
                    item.setContentRightBelow(MoneyUtils.transMoneyFormat(mValidinvestList.get(i).getApplyAmount(),
                            mValidinvestList.get(i).getCurrency()));

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
                            item.setContentLeftAbove(mValidinvestList.get(i).getFundName() + " " +
                                    mValidinvestList.get(i).getFundCode());
                            //交易类型
                            item.setContentLeftBelow(mValidinvestList.get(i).getTransType());
                            //交易状态
                            item.setContentRightAbove(mValidinvestList.get(i).getRecordStatus());
                            //交易份额
                            item.setContentRightBelow(MoneyUtils.transMoneyFormat(mValidinvestList.get(i).getApplyAmount(),
                                    mValidinvestList.get(i).getCurrency()));
                        }
                        showViewBeanList.add(item);
                    }
                }
            }
            showListAdapter.setData(showViewBeanList);
        }
    }

}
