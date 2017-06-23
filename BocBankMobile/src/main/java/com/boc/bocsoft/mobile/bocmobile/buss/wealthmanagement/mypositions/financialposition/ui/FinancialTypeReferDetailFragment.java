package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadReferProfitDetailQuery.PsnXpadReferProfitDetailQueryParams;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.emptyview.CommonEmptyView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullableListView;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransQueryExternalBankInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.adapter.FinancialTypeReferProfitAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnXpadSetBonusMode.psnXpadSetBonusModeResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnXpadreferprofitdetailquery.PsnXpadReferProfitDetailQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadproductbalancequery.PsnXpadProductBalanceQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadreferprofitquery.PsnXpadReferProfitQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.presenter.FinancialPositionContract;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.presenter.FinancialTypeFixedTermDetailPresenter;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;
import com.boc.bocsoft.remoteopenacc.common.view.pulltofresh.XListView;

import java.util.ArrayList;
import java.util.List;

import static com.boc.bocsoft.mobile.bocmobile.R.id.prl_refresh_layout;

/**
 * 收益列表详情显示
 * Created by cff on 2016/9/28.
 */
public class FinancialTypeReferDetailFragment extends MvpBussFragment<FinancialTypeFixedTermDetailPresenter> implements FinancialPositionContract.FinancialTypeFixedTermDetailView {

    private View rootView;
//    private ListView fixedlist;

    //    private XListView xlistview;
    private PullToRefreshLayout mPullToRefreshLayout;
    private PullableListView plv_pull;
    //空数据页面显示
    private CommonEmptyView referdetail_emptyview;

    /**
     * 列表适配器
     */
    private FinancialTypeReferProfitAdapter adapter;
    /**
     * 分页加载数据
     */
    private PsnXpadReferProfitDetailQueryResModel detailQueryResModel;
    //客户持仓信息
    private PsnXpadProductBalanceQueryResModel banlanceDeta;
    //会话ID
    private String mConversationID;
    //列表请求参数
    private PsnXpadReferProfitDetailQueryParams params = new PsnXpadReferProfitDetailQueryParams();
    //请求到的列表数据
    private List<PsnXpadReferProfitDetailQueryResModel.QueryModel> queryList = new ArrayList<PsnXpadReferProfitDetailQueryResModel.QueryModel>();
    //记录总的条目数
    private int queryNum_total;
    //记录当前页数
    private int page = 0;
    private String tranSeq = "";//交易流水号
    private PsnXpadReferProfitDetailQueryResModel resModel = null;
    private boolean isLoadMore = false;//是否需要加载更多

    /*
    * 加载View
     * @param mInflater
     * @return
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.view_fixedterm_referpro_dialog, null);
        return rootView;
    }

    /**
     * 得到头部标题
     *
     * @return
     */
    @Override
    protected String getTitleValue() {
        return getContext().getResources().getString(R.string.boc_trans_financial_position_main_income);
    }

    /**
     * 头部类型设置
     *
     * @return
     */
    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    /**
     * 头部标题布局
     *
     * @return
     */
    @Override
    protected View getTitleBarView() {
        TitleBarView titleBarView = (TitleBarView) super.getTitleBarView();
        titleBarView.setRightButton(null, null);
        return titleBarView;
    }

    /**
     * 初始化View
     */
    @Override
    public void initView() {
//        xlistview = (XListView) rootView.findViewById(R.id.fixedlist);
        mPullToRefreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.fixedlist);
        plv_pull = (PullableListView) rootView.findViewById(R.id.plv_pull);
        referdetail_emptyview = (CommonEmptyView) rootView.findViewById(R.id.referdetail_emptyview);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        adapter = new FinancialTypeReferProfitAdapter(mContext);
        plv_pull.setAdapter(adapter);
//        adapter.setProfirDate(detailQueryResModel.getList());
//        xlistview.setAdapter(adapter);
//        xlistview.setPullLoadEnable(true);
        if (!PublicUtils.isEmpty(queryList)) {
            queryList.clear();
        }
        //请求列表数据
        showLoadingDialog();
        getFixedtermDetailNet(page);
    }

    @Override
    public void setListener() {

        mPullToRefreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (resModel != null) {
                    if (!isLoadAllRecord()) {
                        page++;
                        getFixedtermDetailNet(page);
                        isLoadMore = true;
                    } else {
                        isLoadMore = false;
                        mPullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                    }
                } else {
                    // 暂时什么也不做
                }
            }
        });
    }

//    xlistview.setXListViewListener(new XListView.IXListViewListener()
//
//    {
//        @Override
//        public void onRefresh () {
//        //不支持下拉刷新
//    }
//
//        @Override
//        public void onLoadMore () {
//        //上拉加载更多
//        if (!isLoadAllRecord()) {
//            //请求更多，下一页
//            page++;
//            getFixedtermDetailNet(page);
//        } else {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    xlistview.endRefresh(false);
//                }
//            }, 300);
//        }
//    }
//    }
//
//    );
//}

    //为收益明细设置数据
    public void setReferDetail(String mConversationID, PsnXpadProductBalanceQueryResModel banlanceDeta, String tranSeq) {
        this.tranSeq = tranSeq;
        this.mConversationID = mConversationID;
        this.banlanceDeta = banlanceDeta;
    }

    @Override
    protected FinancialTypeFixedTermDetailPresenter initPresenter() {
        return new FinancialTypeFixedTermDetailPresenter(this);
    }

    //=============================自定义方法处理==============================

    /**
     * 请求收益详情列表
     */
    private void getFixedtermDetailNet(int page) {
        params.setConversationId(mConversationID);
        params.setAccountKey(banlanceDeta.getBancAccountKey());
        params.setProductCode(banlanceDeta.getProdCode());
        params.setProgressionflag(banlanceDeta.getProgressionflag());
        params.setKind(banlanceDeta.getProductKind());
//        params.setStartDate(banlanceDeta.getProdBegin());
//        if ("-1".equals(banlanceDeta.getProdEnd())) {
//            params.setEndDate(null);
//        } else {
//            params.setEndDate(banlanceDeta.getProdEnd());
//        }
        params.setPageSize("50");
        params.setCurrentIndex(page * 50 + "");

        params.set_refresh("true");
        params.setCashRemit(banlanceDeta.getCashRemit());
        params.setProductCode(banlanceDeta.getProdCode());
        params.setTranSeq(tranSeq);
        showLoadingDialog();
        getPresenter().getPsnXpadReferProfitDetailQuery(params);
    }

    /**
     * 判断是否已加载所有数据
     *
     * @return true 已经全部加载；false 没有全部加载
     */
    private boolean isLoadAllRecord() {
        int loadnum = queryList == null ? 0 : queryList.size();
        int totalnum = queryNum_total;
        if (loadnum >= totalnum) {
            return true;
        } else {
            return false;
        }
    }

//=================================接口请求处理============================

    @Override
    public void obtainPsnXpadReferProfitDetailQuerySuccess(PsnXpadReferProfitDetailQueryResModel resModel) {
        closeProgressDialog();
        mPullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
        if (resModel != null) {
            this.resModel = resModel;
            queryNum_total = Integer.parseInt(resModel.getRecordNumber());
            if (0 == page) {
                queryList = resModel.getList();
            } else {
                queryList.addAll(resModel.getList());
            }
            LogUtil.d("yx--------------收益明细--集合数据-得到数据--》" + queryList.size());
            if (PublicUtils.isEmpty(queryList)) {
                referdetail_emptyview.setVisibility(View.VISIBLE);
                referdetail_emptyview.setEmptyTips(getString(R.string.boc_trans_financial_fixedterm_nullrefer), R.drawable.no_result);
            } else {
                adapter.setProfirDate(queryList, banlanceDeta.getCurCode());
//            adapter.notifyDataSetChanged();
                // 达到最大数目 不可进行上拉刷新
//                if (isLoadAllRecord()) {
//                    xlistview.endRefresh(true);
//                } else {
//                    xlistview.endRefresh();
//                }
            }
        }
    }

    @Override
    public void obtainPsnXpadReferProfitDetailQueryFault() {
        closeProgressDialog();
        mPullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
        if (PublicUtils.isEmpty(queryList)) {
            referdetail_emptyview.setVisibility(View.VISIBLE);
            mPullToRefreshLayout.setVisibility(View.GONE);
            referdetail_emptyview.setEmptyTips(getString(R.string.boc_trans_financial_fixedterm_nullrefer), R.drawable.no_result);
        }


    }

    /**
     * 汇总查询成功，此处无用
     *
     * @param mViewModel
     */
    @Override
    public void obtainPsnXpadReferProfitQuerySuccess(PsnXpadReferProfitQueryResModel
                                                             mViewModel) {
    }

    /**
     * 汇总查询失败，此处无用
     */
    @Override
    public void obtainPsnXpadReferProfitQueryFail(BiiResultErrorException
                                                          biiResultErrorException) {
        closeProgressDialog();
        if (!"XPAD.A500".equalsIgnoreCase(biiResultErrorException.getErrorCode() + "")) {
            showErrorDialog(biiResultErrorException.getErrorMessage() + "");
        }
    }

    /**
     * 4.14 014修改分红方式交易 PsnXpadSetBonusMode 成功回调-此处无用
     *
     * @param mViewModel
     */
    @Override
    public void obtainPsnXpadSetBonusModeSuccess(psnXpadSetBonusModeResModel mViewModel) {
        closeProgressDialog();
    }

    /**
     * 4.14 014修改分红方式交易 PsnXpadSetBonusMode 失败回调-此处无用
     *
     * @param biiResultErrorException
     */
    @Override
    public void obtainPsnXpadSetBonusModeFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void setPresenter(BasePresenter presenter) {
    }
}
