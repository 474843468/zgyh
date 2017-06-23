package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;

import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullableListView;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanAccountListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanStatusListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.presenter.EloanqueryPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui.adapter.RepayRecordAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui.adapter.SettleRecordAdapter;

import java.util.List;

/**
 * Created by louis.hui on 2016/10/24.
 * 结清和 未结清列表数据
 */
public class EloanRepayrcdFragment extends MvpBussFragment<EloanqueryPresenter> implements
        EloanQueryContract.DrawRecordView, AdapterView.OnItemClickListener {

    protected View rootView;
    protected PullableListView lvContent;
    protected ImageView pullupIcon;
    protected ImageView loadingIcon;
    protected TextView loadstateTv;
    protected ImageView resultIcon;
    protected RelativeLayout loadmoreView;
    protected PullToRefreshLayout layoutPullToRefresh;
    protected TextView tvNoData;
    /**未结清当前加载页码*/
    private int pageCurrentIndex = 1;
    /**已结清加载页码*/
    private int pageScurrentIndex = 1;
    /**每页大小*/
    private final static int pageSize = ApplicationConst.PAGE_SIZE;
    /**结清，非结清标识符*/
    private String mLoanFlag;
    /**额度类型*/
    private String mQuoteType;
    /**未结清列表*/
    private EloanStatusListModel mRepayRecord;
    /**结清列表*/
    private EloanStatusListModel mSettledRecord;
    /**页面初始化*/
    private boolean isHeadView;
    /**是否是首次加载*/
    private boolean isVisibleBeforeInit;
    /**已结清列表是否为空*/
    private boolean isRepayscd;
    /**未结清列表是否为空*/
    private boolean isRepaycd;
    /**未结清适配器*/
    private RepayRecordAdapter repayRcdAdapter;
    /**已结清适配器*/
    private SettleRecordAdapter settleRcdAdapter;
    /**还款账号*/
    private String mPayAccount;
    /**额度编号*/
    private String mQuoteNo;
    /**未结清结清标识*/
    private static final String REPAYRCD_FLAG = "N";
    /**已结清标识*/
    private static final String REPAYSRCD_FLAG = "Y";

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.fragment_facility_use_record_qry, null);
        isHeadView = true;
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        lvContent = (PullableListView) rootView.findViewById(R.id.lvContent);
        pullupIcon = (ImageView) rootView.findViewById(R.id.pullup_icon);
        loadingIcon = (ImageView) rootView.findViewById(R.id.loading_icon);
        loadstateTv = (TextView) rootView.findViewById(R.id.loadstate_tv);
        resultIcon = (ImageView) rootView.findViewById(R.id.result_icon);
        loadmoreView = (RelativeLayout) rootView.findViewById(R.id.loadmore_view);
        layoutPullToRefresh = (PullToRefreshLayout) rootView.findViewById(R.id.layoutPullToRefresh);
        tvNoData = (TextView) rootView.findViewById(R.id.tvNoData);
    }

    @Override
    public void initData() {
        super.initData();
        setLoadmoreView();
    }

    @Override
    public void setListener() {
        super.setListener();
        layoutPullToRefresh.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

                if (isRepaycd
                        && mRepayRecord.getAccountListModel().getLoanList().size()
                        < mRepayRecord.getAccountListModel().getRecordNumber()
                        || (isRepayscd && mSettledRecord.getAccountListModel().getLoanList().size()
                        < mSettledRecord.getAccountListModel().getRecordNumber()) ) {
                    isVisibleBeforeInit = true;
                    getLoadingRep();
                }else{
                    layoutPullToRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                }
            }
        });

    }

    @Override
    protected EloanqueryPresenter initPresenter() {

        return new EloanqueryPresenter(this);
    }

    /**
     * 是否显示标题栏
     */
    protected boolean isHaveTitleBarView() {
        return false;
    }

    /**
     * 贷款返回
     * @param loanflag 结清标识
     * @param qutoType 额度类型
     */
    public void setLoanFlag(String loanflag, String qutoType) {
        mLoanFlag = loanflag;
        mQuoteType = qutoType;
    }

    /**
     * 上页传过来的账户，编号
     * @param payAccount
     * @param quoteNo
     */
    public void setLoanData(String payAccount, String quoteNo) {
        mPayAccount = payAccount;
        mQuoteNo = quoteNo;
    }

    /**
     * 上页传值已结清，未结清
     * @param repayRecord 未结清列表
     * @param settledRecord 已结清列表
     */
    public void setAccountLoanList(EloanStatusListModel repayRecord, EloanStatusListModel settledRecord) {
        //未结清列表
        mRepayRecord = repayRecord;
        //已结清列表
        mSettledRecord = settledRecord;

        if (mRepayRecord != null && mRepayRecord.getAccountListModel() != null
                &&(mRepayRecord.getAccountListModel().getLoanList()!= null
                && mRepayRecord.getAccountListModel().getLoanList().size() > 0)) {

            isRepaycd = true;
        }

        if (mSettledRecord != null &&
                mSettledRecord.getAccountListModel() != null
                && (mSettledRecord.getAccountListModel().getLoanList() != null
                && mSettledRecord.getAccountListModel().getLoanList().size() > 0)) {

            isRepayscd = true;
        }
    }
    /**
     * 052接口请求还款列表
     */
    private void getLoadingRep() {
        EloanAccountListModel accountList = new EloanAccountListModel();
        accountList.seteLoanState(mQuoteType);
        accountList.seteFlag(mLoanFlag);
        accountList.setPageSize(pageSize);
        if (REPAYRCD_FLAG.equals(mLoanFlag)) {
            accountList.setCurrentIndex(pageCurrentIndex * pageSize);
        } else if (REPAYSRCD_FLAG.equals(mLoanFlag)) {
            accountList.setCurrentIndex(pageScurrentIndex * pageSize);
        }
        if (REPAYRCD_FLAG.equals(mLoanFlag)) {
            accountList.setConversationId(mRepayRecord.getAccountListModel().getConversationId());
        } else if (REPAYSRCD_FLAG.equals(mLoanFlag)) {
            accountList.setSconversationId(mSettledRecord.getAccountListModel().getSconversationId());
        }
        if (pageCurrentIndex == 0) {
            accountList.set_refresh("true");
        } else {
            accountList.set_refresh("false");
        }

        getPresenter().queryLoanAccount(accountList);
    }

    /**
     * 添加适配器
     */
    private void setLoadmoreView() {
        if (REPAYRCD_FLAG.equals(mLoanFlag) && isRepaycd ) {
            if (repayRcdAdapter == null) {
                repayRcdAdapter = new RepayRecordAdapter(getContext());
                lvContent.setAdapter(repayRcdAdapter);
                lvContent.setOnItemClickListener(this);
            }
            repayRcdAdapter.setRepayRcdData(mRepayRecord.getAccountListModel().getLoanList());
            if (mRepayRecord.getAccountListModel().getLoanList().size() > 20) {
                lvContent.setSelection(10);
            }
            repayRcdAdapter.notifyDataSetChanged();

        } else if (REPAYSRCD_FLAG.equals(mLoanFlag) && isRepayscd) {
            if (settleRcdAdapter == null) {
                settleRcdAdapter = new SettleRecordAdapter(getContext());
                lvContent.setAdapter(settleRcdAdapter);
                lvContent.setOnItemClickListener(this);
            }
            settleRcdAdapter.setSettleRcdData(mSettledRecord.getAccountListModel().getLoanList());
            settleRcdAdapter.notifyDataSetChanged();
        } else {
            //已结清
            if (REPAYSRCD_FLAG.equals(mLoanFlag)) {
                tvNoData.setVisibility(View.VISIBLE);
                tvNoData.setText(getString(R.string.boc_repayRecord_settle));
            } //未结清
            else if (REPAYRCD_FLAG.equals(mLoanFlag)) {
                tvNoData.setVisibility(View.VISIBLE);
                tvNoData.setText(R.string.boc_repayRecord);
            }
        }
    }

    /**
     * 052接口请求失败
     */
    @Override
    public void eRepaymentFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        if (isVisibleBeforeInit) {
            layoutPullToRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
        }
    }

    /**
     * 052请求查询成功
     */
    @Override
    public void eRepaymentSuccess(EloanAccountListModel eAccountResult) {
        closeProgressDialog();
        if (eAccountResult != null && eAccountResult.getLoanList().size() > 0) {
            //上拉加载数据
            refreshData(eAccountResult);
            if (REPAYRCD_FLAG.equals(mLoanFlag)) {
                pageCurrentIndex ++;
            } else if (REPAYSRCD_FLAG.equals(mLoanFlag)) {
               pageScurrentIndex ++;
            }

            if (isVisibleBeforeInit) {
                layoutPullToRefresh.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
            }
            //加载数据
            setLoadmoreView();
        } else {
            if(isVisibleBeforeInit){
                layoutPullToRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
            } else {
                if (REPAYSRCD_FLAG.equals(mLoanFlag)) {
                    tvNoData.setVisibility(View.VISIBLE);
                    tvNoData.setText(getString(R.string.boc_repayRecord_settle));
                } //未结清
                else if (REPAYRCD_FLAG.equals(mLoanFlag)) {
                    tvNoData.setVisibility(View.VISIBLE);
                    tvNoData.setText(R.string.boc_repayRecord);
                }
            }
        }
    }

    @Override
    public void setPresenter(EloanQueryContract.Presenter presenter) {

    }

    /**
     *上拉刷新后的数据
     *
     */
    private void refreshData(EloanAccountListModel eAccountResult) {
        if (eAccountResult != null) {
            if (REPAYRCD_FLAG.equals(mLoanFlag)) {
                mRepayRecord.getAccountListModel().setRecordNumber(eAccountResult.getRecordNumber());
                mRepayRecord.getAccountListModel().setMoreFlag(eAccountResult.getMoreFlag());
                List<EloanAccountListModel.PsnLOANListEQueryBean> list = eAccountResult.getLoanList();
                for (int i = 0; i< list.size() ; i ++) {
                    EloanAccountListModel.PsnLOANListEQueryBean bean = list.get(i);
                    mRepayRecord.getAccountListModel().getLoanList().add(bean);
                }
            } else {
                mSettledRecord.getAccountListModel().setRecordNumber(eAccountResult.getRecordNumber());
                mSettledRecord.getAccountListModel().setMoreFlag(eAccountResult.getMoreFlag());
                List<EloanAccountListModel.PsnLOANListEQueryBean> list = eAccountResult.getLoanList();
                for (int i = 0; i< list.size() ; i ++) {
                    EloanAccountListModel.PsnLOANListEQueryBean bean = list.get(i);
                    mSettledRecord.getAccountListModel().getLoanList().add(bean);
                }
            }
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (REPAYSRCD_FLAG.equals(mLoanFlag)) {
            EloanSettleDetailFramgent detailFramgent = new EloanSettleDetailFramgent();
            Bundle bundle = new Bundle();
            bundle.putSerializable(EloanConst.LOAN_ACCOUNT_NUM,
                    mSettledRecord.getAccountListModel().getLoanList().get(position));
            //还款账号
            bundle.putString(EloanConst.PEPAY_ACCOUNT, mPayAccount);
            //系统当前时间
            bundle.putString(EloanConst.DATA_TIME, mSettledRecord.getAccountListModel().getEndDate());
            detailFramgent.setArguments(bundle);
            start(detailFramgent);

        } else if (REPAYRCD_FLAG.equals(mLoanFlag)) {
            EloanRepayDetailFragment eloanRepaymentFragment = new EloanRepayDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(EloanConst.ELON_PREPAY_DETAIL,
                    mRepayRecord.getAccountListModel().getLoanList().get(position));
            //还款账号
            bundle.putString(EloanConst.PEPAY_ACCOUNT, mPayAccount);
            //当前系统时间
            bundle.putString(EloanConst.DATA_TIME, mRepayRecord.getAccountListModel().getEndDate());

            bundle.putBoolean(EloanConst.LOAN_PREPAY_DETAIL, false);
            //额度编号
            bundle.putString(EloanConst.LOAN_QUOTENO, mQuoteNo);
            eloanRepaymentFragment.setArguments(bundle);
            start(eloanRepaymentFragment);
        }
    }

}
