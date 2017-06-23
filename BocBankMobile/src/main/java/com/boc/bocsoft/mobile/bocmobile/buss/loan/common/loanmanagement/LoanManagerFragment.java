package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullableListView;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.model.LoanAccountListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.model.LoanOverdueModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.presenter.LoanMangerPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.ui.HostoryLoanRecord;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.ui.HostoryLoanRecordDetail;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.ui.LoanHeaderAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.ui.LoanMangerContract;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.ui.LoanSettledAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.ui.MoreELoanFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.ui.MoreOptionFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.ui.UserLoanAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.ui.LoanApplyOtherSelectFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanAccountListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanQuoteViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui.EloanDrawFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui.LoanActivatedFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.nonreloan.query.ui.NonReloanQryFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.PledgeLoanTypeSelectFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.ui.ReloanStatusFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.consumefinance.ConsumefinanceConst;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.boc.bocsoft.mobile.bocmobile.R.id.lv_header_bank;

/**
 * Created by huixiaobo on 2016/8/18.
 * 贷款管理首页
 */
public class LoanManagerFragment extends MvpBussFragment<LoanMangerPresenter> implements View.OnClickListener, LoanMangerContract.LoanManageView {
    /**view*/
    protected View rootView;
    /**我要贷款按钮*/
    protected TextView loanleftTv;
    /**我的贷款按钮*/
    protected TextView loanrightTv;
    /**标题*/
    private TextView loanTv;
    /**抬头标题*/
    protected RelativeLayout loanmanageTitle;
    /**中银E贷*/
    protected LinearLayout eLoanApply;
    /**在线质押*/
    protected LinearLayout otherPliedApply;
    /**其他类贷款*/
    protected LinearLayout otherLoanApply;
    /**我要申请贷款布局*/
    protected LinearLayout userloanApply;
    /***/
    private LinearLayout loanTitleLy;
    /**左侧按钮*/
    private ImageView imgLeftIcon;
    /**右侧按钮*/
    private ImageView imgRigntIcon;
    //上拉加载
    private PullToRefreshLayout mPullToRefreshLayout;
    //上拉刷新组件listview
    private PullableListView pullListView;
    /**当前加载页码*/
    private int pageCurrentIndex = 0;
    /**已结清贷款列表*/
    private int pageScurrentIndex = 0;
    //上传参数对象
    private LoanAccountListModel loandata;
    /**测试错误提示码提示异常*/
    private static final String ELOAN_ERRORCODE = "BANCS.0188";
    /**每页大小*/
    private final static int pageSize = ApplicationConst.PAGE_SIZE;
    /**判断是否已结清贷款按钮*/
    private boolean isSloanAccount = false;
    /**逾期信息*/
    private String mOverdue;
    /**中银E贷返回值*/
    private List<EloanQuoteViewModel> mEloanQuote;
    /**贷款产品列表对象*/
    private LoanAccountListModel accountListModel;
    /**已结清贷款列表数据*/
    private LoanAccountListModel settledListModel;

    /**贷款产品列表*/
    private List<LoanAccountListModel.PsnLOANListEQueryBean> mLoanAccountList;
    /**头部布局*/
    private View mheaderView;
    /**结清贷款按钮*/
    private View mFooterView;
    /**头部条目listview*/
    private ListView mHeaderLv;
    /**贷款产品适配器*/
    private UserLoanAdapter mLoanAdapter;
    /**中银E贷适配器*/
    private LoanHeaderAdapter mHeaderAdaper;
    /**已结清贷款列表*/
    private LoanSettledAdapter mSettledAdapter;
    /**是否请求*/
    private boolean isPullToRefresh;
    /**是否显示我要贷款*/
    private boolean isUserLoan;
    /**还款账号借记卡号*/
    private String mCardNum;
    /**循环类行*/
    private String mCycleType;
    /**条目数*/
    private int mPosition;
    /**判断是否显示当前页*/
    private boolean isPager;
    /**判断是否刷新请求*/
    private boolean isRefresh = false;
    /**未结清标识*/
    public static final String LOAN_FLAG = "N";
    /**结清/未结清标识*/
    private String mLoanFlag;
    /**已结清标识*/
    public static final String LOAN_SFLAG = "Y";
    /**消费金融跳转传值*/
    private boolean isConsumefinance;
    /**已结清贷款抬头提示*/
    private TextView mLoanSettled;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_loanmanager, null);
        return rootView;

    }

    @Override
    public void initView() {
        super.initView();
        imgLeftIcon = (ImageView) rootView.findViewById(R.id.leftIconIv);
        imgRigntIcon = (ImageView) rootView.findViewById(R.id.rightIconIv);
        loanleftTv = (TextView) rootView.findViewById(R.id.loanleftTv);
        loanrightTv = (TextView) rootView.findViewById(R.id.loanrightTv);
        loanTv = (TextView) rootView.findViewById(R.id.loanTv);
        loanmanageTitle = (RelativeLayout) rootView.findViewById(R.id.loanmanageTitle);
        loanTitleLy = (LinearLayout) rootView.findViewById(R.id.loanTitleLy);
        mheaderView = View.inflate(getContext(), R.layout.boc_loanheard_listview, null);
        mHeaderLv = (ListView) mheaderView.findViewById(lv_header_bank);
        mFooterView = View.inflate(getContext(), R.layout.boc_loan_settledbut, null);
        mLoanSettled = (TextView) rootView.findViewById(R.id.loanSettled);
        loanleftTv.setOnClickListener(this);
        loanrightTv.setOnClickListener(this);

        //我要贷款
        eLoanApply = (LinearLayout) rootView.findViewById(R.id.eLoanApply);
        otherPliedApply = (LinearLayout) rootView.findViewById(R.id.otherPliedApply);
        otherLoanApply = (LinearLayout) rootView.findViewById(R.id.otherLoanApply);
        userloanApply = (LinearLayout) rootView.findViewById(R.id.userloanApply);
        eLoanApply.setOnClickListener(this);
        otherPliedApply.setOnClickListener(this);
        otherLoanApply.setOnClickListener(this);
        //我的贷款

        //布局文件
        mPullToRefreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.pull_refresh_layout);
        pullListView = (PullableListView) rootView.findViewById(R.id.pullListView);
    }
    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return true;
    }

    @Override
    public void initData() {
        super.initData();
        //消费金融跳转传值
        isConsumefinance = getArguments().getBoolean(ConsumefinanceConst.IS_CONSUMEFINANCE);
        showLoadingDialog();
        //074接口请求
        getPresenter().queryOverdue();
        //045接口请求
        getPresenter().queryEloanQuote();
    }

    /**
     * 返回界面后调用方法
     */
    @Override
    public void reInit() {
        super.reInit();

        mPullToRefreshLayout.setVisibility(View.GONE);
        userloanApply.setVisibility(View.GONE);
        //074接口请求
        showLoadingDialog(false);
        //返回刷新页面
        isRefresh = true;
        getPresenter().queryOverdue();
        //045接口请求
        getPresenter().queryEloanQuote();
    }


    /***
     * 052接口请求数据
     */
    private void queryLoanData(String drawflag){
        if (loandata == null) {
            loandata = new LoanAccountListModel();
        }
        mLoanFlag = drawflag;
        loandata.seteFlag(drawflag);
        loandata.seteLoanState("10");
        loandata.setPageSize(pageSize);
        if (drawflag.equals(LOAN_FLAG)) {
            loandata.setCurrentIndex(pageCurrentIndex * pageSize);
        } else if (drawflag.equals(LOAN_SFLAG)) {
            loandata.setCurrentIndex(pageScurrentIndex * pageSize);
        }

        //返回刷新页面
        if (isRefresh) {
            loandata.setRefresh(true);
            isPullToRefresh = false;
            pageCurrentIndex = 0;
            pageScurrentIndex = 0;
        }

        if ((drawflag.equals(LOAN_FLAG) && pageCurrentIndex == 0)
                || (drawflag.equals(LOAN_SFLAG) && pageScurrentIndex == 0)) {
            loandata.set_refresh("true");
        } else {
            if (drawflag.equals(LOAN_FLAG)) {
                loandata.setConversationId(accountListModel.getConversationId());
            } else {
                loandata.setSconversationId(settledListModel.getSconversationId());
            }
            loandata.set_refresh("false");
            showLoadingDialog();
        }
        getPresenter().queryLoanAccount(loandata);

    }

    @Override
    public void setListener() {
        super.setListener();
        imgLeftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleLeftIconClick();
            }
        });

        imgRigntIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleRightIconClick();
            }
        });
        //上拉加载更多
        mPullToRefreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (accountListModel != null && accountListModel.getLoanList() != null
                        && accountListModel.getLoanList().size() < accountListModel.getRecordNumber()) {
                    isRefresh = false;
                    isPullToRefresh = true;
                    pageCurrentIndex ++ ;
                    queryLoanData(LOAN_FLAG);
                    loandata.setRefresh(false);
                } else if ((settledListModel != null && settledListModel.getLoanList() != null
                        && settledListModel.getLoanList().size() < settledListModel.getRecordNumber())){
                    isRefresh = false;
                    isPullToRefresh = true;
                    pageScurrentIndex ++;
                    queryLoanData(LOAN_SFLAG);
                    loandata.setRefresh(false);
                } else {
                    isPullToRefresh = true;
                    mPullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                }
            }
        });

        // 贷款管理点击事件
        mHeaderLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                start(EloanDrawFragment.newInstance(mEloanQuote.get(position),mOverdue));
            }
        });

        pullListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ((mEloanQuote != null && mEloanQuote.size() > 0)) {
                    if (mLoanAccountList != null && mLoanAccountList.size() > 0) {
                        mCycleType = mLoanAccountList.get(position - 1).getCycleType();
                        mPosition = position - 1;
                        //账户转换借记卡
                        showLoadingDialog();
                        getPresenter().queryCardNumByAcctNum(mLoanAccountList.get(position -1).getLoanCycleRepayAccount());
                    } else {
                        start(EloanDrawFragment.newInstance(mEloanQuote.get(position), mOverdue));
                    }
                } else {
                    if (mLoanAccountList == null ||(mLoanAccountList != null && mLoanAccountList.size() <= 0
                            && settledListModel != null && settledListModel.getLoanList()!= null
                            && settledListModel.getLoanList().size() > 0)) {
                        HostoryLoanRecordDetail hostoryLoanDetail = new HostoryLoanRecordDetail();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(LoanCosnt.LOAN_DATA, settledListModel.getLoanList().get(position));
                        bundle.putString(LoanCosnt.LOAN_END_DATE,settledListModel.getEndDate());
                        hostoryLoanDetail.setArguments(bundle);
                        start(hostoryLoanDetail);

                    } else {
                        mCycleType = mLoanAccountList.get(position).getCycleType();
                        mPosition = position;
                        //账户转换借记卡
                        showLoadingDialog();
                        getPresenter().queryCardNumByAcctNum(mLoanAccountList.get(position).getLoanCycleRepayAccount());
                    }

                }
            }
        });

        mFooterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (settledListModel != null && settledListModel.getLoanList()!= null
                        && settledListModel.getLoanList().size() > 0) {

                    HostoryLoanRecord record = new HostoryLoanRecord();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(LoanCosnt.LOAN_ACCOUTLIST_MODEL, settledListModel);
                    record.setArguments(bundle);
                    start(record);
                }
            }
        });
    }

    @Override
    protected void titleLeftIconClick() {
        super.titleLeftIconClick();
    }

    @Override
    protected void titleRightIconClick() {
        super.titleRightIconClick();
        MoreOptionFragment mpFragment = new MoreOptionFragment();
        start(mpFragment);
    }

    @Override
    public void onClick(View v) {
        int view = v.getId();
        //我要贷款
        if (view == R.id.loanleftTv) {
            isPager = true;
            getLoanData(false);
        }//我的贷款
        else if (view == R.id.loanrightTv) {
            isPager = false;
            isConsumefinance = false;
            getLoanData(true);
        } //中银E贷
        else if (view == R.id.eLoanApply) {
            getEloanStartPager();
        } //在线质押
        else if (view == R.id.otherPliedApply) {
            if ("Y".equals(mOverdue)) {
                showErrorDialog(getString(R.string.boc_loan_pledgeoverdue));
                return;
            }
            //判断是不是身份证
            if (ApplicationContext.getInstance().getUser().getIdentityType().equals("1")) {
                LocalDateTime date = ApplicationContext.getInstance().getCurrentSystemDate();
                // 格式化当前时间
                String time = date.format(DateFormatters.dateFormatter3);
                String userNumber = ApplicationContext.getInstance().getUser().getIdentityNumber();
                //判断年龄是不是大于等于18岁且小于等于65岁
                if (isHas18Old(time, userNumber)) {
                    start(new PledgeLoanTypeSelectFragment());
                } else {
                    showErrorDialog(getString(R.string.boc_apply_account));
                }
            } //判断是不是军官证，警官证
            else  if (ApplicationContext.getInstance().getUser().getIdentityType().equals("4")
                    || ApplicationContext.getInstance().getUser().getIdentityType().equals("5")){
                start(new PledgeLoanTypeSelectFragment());
            } else {
                showErrorDialog(getString(R.string.boc_apply_account));
            }

        }//其他类贷款
        else if (view == R.id.otherLoanApply) {
            start(new LoanApplyOtherSelectFragment());
        }
    }

    /**
     *抬头按钮切换
     */
    private void refreshLayout(View tabView) {
        loanleftTv.setSelected(false);
        loanrightTv.setSelected(false);
        tabView.setSelected(true);
    }

    /**
     * 判断 显示我的贷款 或者显示 我要贷款
     */
    private void getLoanData(boolean isTableviw) {
        if (isTableviw) {
            loanTv.setVisibility(View.GONE);
            loanTitleLy.setVisibility(View.VISIBLE);
            if (isPager || isConsumefinance) {
                refreshLayout(loanleftTv);
                mPullToRefreshLayout.setVisibility(View.GONE);
                userloanApply.setVisibility(View.VISIBLE);
            } else {
                refreshLayout(loanrightTv);
                mPullToRefreshLayout.setVisibility(View.VISIBLE);
                userloanApply.setVisibility(View.GONE);
            }
        } else {
            if (isUserLoan) {
                loanTv.setVisibility(View.VISIBLE);
                loanTitleLy.setVisibility(View.GONE);
            } else {
                refreshLayout(loanleftTv);
            }
            mPullToRefreshLayout.setVisibility(View.GONE);
            userloanApply.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 045接口 中银E贷查询失败
     */
    @Override
    public void eloanQuoteFail (BiiResultErrorException biiResultErrorException) {
        if (ELOAN_ERRORCODE.equals(biiResultErrorException.getErrorCode())) {
            queryLoanData(LOAN_FLAG);
        } else {
            showErrorDialog(biiResultErrorException.getErrorMessage());
            closeProgressDialog();
        }
    }

    /**
     * 045接口 中银E贷查询成功
     */
    @Override
    public void eloanQuoteSuccess(List<EloanQuoteViewModel> equoteResult) {
        if (equoteResult != null) {
            mEloanQuote = equoteResult;
        }
        queryLoanData(LOAN_FLAG);
    }

    /**
     * 052接口 其它贷款产品查询成功
     */
    @Override
    public void queryLoanAccountFail(BiiResultErrorException biiResultErrorException) {
        if (ELOAN_ERRORCODE.equals(biiResultErrorException.getErrorCode())) {
            queryLoanData(LOAN_SFLAG);

        } else {
            if (isPullToRefresh) {
                mPullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
            }
            closeProgressDialog();
            showErrorDialog(biiResultErrorException.getErrorMessage());
        }
    }

    /**
     * 052接口 其它贷款产品查询成功
     */

    @Override
    public void queryLoanAccountSuccess(LoanAccountListModel eAccountResult) {
        if (eAccountResult == null) {
            return;
        }

        if (isPullToRefresh) {
            refreshData(eAccountResult);
            mLoanAccountList = builtAccoutList(accountListModel.getLoanList());
            setAdapterNotify();
            mPullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
        } else {
            accountListModel = eAccountResult;
        }
        //首次已结清
        if (!isPullToRefresh) {
            queryLoanData(LOAN_SFLAG);
           mLoanAccountList = builtAccoutList(accountListModel.getLoanList());
        }
        if (accountListModel != null && accountListModel.getRecordNumber() != 0
                && mLoanAccountList != null
                && mLoanAccountList.size() == accountListModel.getRecordNumber()) {

            isSloanAccount = true;
        }
        //上拉加载后出现底部已结清按钮
        if (isPullToRefresh) {
            if (isSloanAccount && settledListModel != null && settledListModel.getLoanList() != null
                    && settledListModel.getLoanList().size() > 0) {
                pullListView.addFooterView(mFooterView);
            }
        }
    }

    /**
     * 052已结清贷款列表失败
     */
    @Override
    public void queryLoanSettledFail(BiiResultErrorException biiResultErrorException) {
        if (ELOAN_ERRORCODE.equals(biiResultErrorException.getErrorCode())) {
            setAdapterNotify();
        } else {
            closeProgressDialog();
            showErrorDialog(biiResultErrorException.getErrorMessage());
        }
    }

    /**
     * 052已结清贷款列表成功
     */
    @Override
    public void queryLoanSettledSuccess(LoanAccountListModel eAccountResult) {
        if (eAccountResult == null) {
            return;
        }
        if (isPullToRefresh) {
            refreshData(eAccountResult);
            mPullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
        } else {
            settledListModel = eAccountResult;
        }
        setAdapterNotify();
        if (isSloanAccount && settledListModel.getLoanList() != null &&
                settledListModel.getLoanList().size() > 0) {
            pullListView.addFooterView(mFooterView);
        }
        closeProgressDialog();
    }

    /**
     * 判断中银E贷跳转页
     */
    private void getEloanStartPager() {
        if (mEloanQuote == null && mLoanAccountList == null) {
            if ("Y".equals(mOverdue)) {
                showErrorDialog(getString(R.string.boc_eloan_accountoverdue));
            } else {
                start(LoanActivatedFragment.newInstance(mOverdue));
            }
        }
        //过滤后的中银E贷
        List<EloanQuoteViewModel> eloanList = filterEloanList(mEloanQuote);
        // 过滤后的1046和1047 可用款的循环贷款
        List<LoanAccountListModel.PsnLOANListEQueryBean> loanAccountList = filterLoanList(mLoanAccountList);
        //判断是否有可用款的中银E贷
        if (eloanList != null && eloanList.size() >0
                && loanAccountList != null && loanAccountList.size() >0) {
            MoreELoanFragment moreELoanFragment = new MoreELoanFragment();
            moreELoanFragment.putEloanData(eloanList, loanAccountList, mOverdue);
            start(moreELoanFragment);
        } else if (eloanList != null && eloanList.size() > 0) {
            if (eloanList.size() >= 2) {
                MoreELoanFragment moreELoanFragment = new MoreELoanFragment();
                moreELoanFragment.putEloanData(eloanList, null, mOverdue);
                start(moreELoanFragment);
            } else if (eloanList.size() == 1) {
                start(EloanDrawFragment.newInstance(eloanList.get(0), mOverdue));
            }
        } //判断是否有可用款的循环
        else if (loanAccountList != null && loanAccountList.size() > 0) {
            if (loanAccountList.size() >= 2) {
                MoreELoanFragment moreELoanFragment = new MoreELoanFragment();
                moreELoanFragment.putEloanData(null, loanAccountList, mOverdue);
                start(moreELoanFragment);
            } else if (loanAccountList.size() == 1) {
                ReloanStatusFragment statusFragment = new ReloanStatusFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(LoanCosnt.LOAN_DATA, mLoanAccountList.get(0));
                bundle.putString(LoanCosnt.LOAN_ENDDATE, ApplicationContext.getInstance().getCurrentSystemDate().
                        format(DateFormatters.dateFormatter1));
                bundle.putString(LoanCosnt.LOAN_OVERDUE, mOverdue);
                bundle.putString(LoanCosnt.LOAN_REPYNUM, mCardNum);
                statusFragment.setArguments(bundle);
                start(statusFragment);
            }
        } else {
            start(LoanActivatedFragment.newInstance(mOverdue));
        }
    }

    /**
     * 过滤正常 和冻结的中银E贷
     */
    private List<EloanQuoteViewModel> filterEloanList(List<EloanQuoteViewModel> eLoanQuote) {
        List<EloanQuoteViewModel> eloanList = new ArrayList<EloanQuoteViewModel>();
        if (eLoanQuote != null && eLoanQuote.size() >0) {
            for (EloanQuoteViewModel loanQuote : eLoanQuote) {
                if (loanQuote.getQuoteState().equals("05") ||
                        loanQuote.getQuoteState().equals("20")) {
                    eloanList.add(loanQuote);
                }
            }
        }
        return eloanList;
    }

    /**
     * 过滤个人循环贷款的 1047 和 1067 list
     */
    private List<EloanAccountListModel.PsnLOANListEQueryBean> filterLoanList(
            List<EloanAccountListModel.PsnLOANListEQueryBean> cycleList) {

        List<EloanAccountListModel.PsnLOANListEQueryBean> cycleLoanList = new ArrayList<EloanAccountListModel.PsnLOANListEQueryBean>();
        if (cycleList != null && cycleList.size() > 0) {
            for (EloanAccountListModel.PsnLOANListEQueryBean loanList : cycleList) {
                if ("1046".equals(loanList.getLoanType()) || "1047".equals(loanList.getLoanType())) {
                    if ("5".equals(loanList.getLoanAccountStats())
                            || "7".equals(loanList.getLoanAccountStats())
                            || "12".equals(loanList.getLoanAccountStats())
                            || "8".equals(loanList.getLoanAccountStats())
                            || "50".equals(loanList.getLoanAccountStats())
                            || !"0".equals(loanList.getLoanCycleBalance()))
                        if ("1".equals(loanList.getCycleFlag())
                                || "01".equals(loanList.getCycleFlag()))
                    cycleLoanList.add(loanList);
                }
            }
        }
        return cycleLoanList;
    }

    /**
     * 贷款列表排序
     */
    private List<LoanAccountListModel.PsnLOANListEQueryBean> builtAccoutList(List<LoanAccountListModel.PsnLOANListEQueryBean> loanResult) {
        List<LoanAccountListModel.PsnLOANListEQueryBean> loanAccountList = new ArrayList<LoanAccountListModel.PsnLOANListEQueryBean>();
        List<LoanAccountListModel.PsnLOANListEQueryBean> eLoanList = new ArrayList<LoanAccountListModel.PsnLOANListEQueryBean>();
        List<LoanAccountListModel.PsnLOANListEQueryBean> otherLoan = new ArrayList<LoanAccountListModel.PsnLOANListEQueryBean>();
        List<LoanAccountListModel.PsnLOANListEQueryBean> rLoanList = new ArrayList<LoanAccountListModel.PsnLOANListEQueryBean>();
        if (loanResult != null && loanResult.size() > 0) {
            for (LoanAccountListModel.PsnLOANListEQueryBean eloanRst : loanResult) {
                if (("1046".equals(eloanRst.getLoanType()) ||
                        "1047".equals(eloanRst.getLoanType()))
                        && "R".equals(eloanRst.getCycleType())) {
                    eLoanList.add(eloanRst);
                } else if ("R".equals(eloanRst.getCycleType())){
                    rLoanList.add(eloanRst);
                } else {
                    otherLoan.add(eloanRst);
                }
            }
            loanAccountList.addAll(eLoanList);
            loanAccountList.addAll(rLoanList);
            loanAccountList.addAll(otherLoan);
        }
        return loanAccountList;
    }

    /**
     *上拉刷新后的数据
     * @param result
     */
    private void refreshData(LoanAccountListModel result) {
        if (result != null) {
            if (LOAN_FLAG.equals(mLoanFlag)) {
                accountListModel.setRecordNumber(result.getRecordNumber());
                accountListModel.setMoreFlag(result.getMoreFlag());
                List<LoanAccountListModel.PsnLOANListEQueryBean> list = result.getLoanList();
                for (int i = 0; i< list.size() ; i ++) {
                    LoanAccountListModel.PsnLOANListEQueryBean bean = list.get(i);
                    accountListModel.getLoanList().add(bean);
                }

            } else {
                settledListModel.setRecordNumber(result.getRecordNumber());
                settledListModel.setMoreFlag(result.getMoreFlag());
                List<LoanAccountListModel.PsnLOANListEQueryBean> list = result.getLoanList();
                for (int i = 0; i< list.size() ; i ++) {
                    LoanAccountListModel.PsnLOANListEQueryBean bean = list.get(i);
                    settledListModel.getLoanList().add(bean);
                }
            }
        }
    }

    /**
     * 刷新我的贷款页面
     */
    private void setAdapterNotify() {
        //判断是否有我的贷款
        if ((mEloanQuote == null && mLoanAccountList == null)
                || (mEloanQuote == null && mLoanAccountList != null
                && mLoanAccountList.size() <= 0)
                || (mEloanQuote != null && mEloanQuote.size() <= 0 && mLoanAccountList == null)
                || (mEloanQuote != null && mEloanQuote.size() <= 0
                && mLoanAccountList != null && mLoanAccountList.size() <= 0)) {

            if (settledListModel != null && settledListModel.getLoanList()!= null
                    && settledListModel.getLoanList().size() >0) {
                getLoanData(true);
                //已结清贷款列表抬头提示语
                mLoanSettled.setVisibility(View.VISIBLE);
                mLoanSettled.setText(getString(R.string.boc_loan_settledtitle));
                if (mSettledAdapter == null) {
                    mSettledAdapter = new LoanSettledAdapter(getContext());
                    pullListView.setAdapter(mSettledAdapter);
                }
                mSettledAdapter.setLoanAccountList(settledListModel.getLoanList());
                mSettledAdapter.notifyDataSetChanged();

            } else {
                isUserLoan = true;
                getLoanData(false);
            }

        } else {
            getLoanData(true);
            //没有中银E贷
            if (mEloanQuote == null) {
                if (mLoanAdapter == null) {
                    mLoanAdapter = new UserLoanAdapter(getContext());
                    pullListView.setAdapter(mLoanAdapter);
                }
                mLoanAdapter.setLoanAccountList(mLoanAccountList);
                mLoanAdapter.notifyDataSetChanged();

            } else {
                //有中银E贷
                if (mHeaderAdaper == null) {
                    mHeaderAdaper = new LoanHeaderAdapter(getContext());
                    mHeaderLv.setAdapter(mHeaderAdaper);
                } else {
                    mHeaderLv.setAdapter(mHeaderAdaper);
                }
                mHeaderAdaper.setLoanQuote(mEloanQuote);
                mHeaderAdaper.notifyDataSetChanged();

                int count = mHeaderAdaper.getCount();
                DisplayMetrics metrics = new DisplayMetrics();
                Activity activity = (Activity) mActivity;
                activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                float density = metrics.density;
                int itemHeight = 0;
                if (count == 1) {
                    itemHeight = (int) activity.getResources().getDimension(R.dimen.boc_space_between_220px);
                } else {
                    itemHeight = (int) activity.getResources().getDimension(R.dimen.boc_space_between_224px);
                }

                LinearLayout.LayoutParams latyoutParams = (LinearLayout.LayoutParams) mHeaderLv.getLayoutParams();
                latyoutParams.height = itemHeight * count;
                mHeaderLv.setLayoutParams(latyoutParams);

                if (mLoanAccountList != null) {
                    if (mLoanAdapter == null) {
                        pullListView.addHeaderView(mheaderView);
                        mLoanAdapter = new UserLoanAdapter(getContext());
                        pullListView.setAdapter(mLoanAdapter);
                    } else {
                        pullListView.setAdapter(mLoanAdapter);
                    }
                    mLoanAdapter.setLoanAccountList(mLoanAccountList);
                    mLoanAdapter.notifyDataSetChanged();
                } else {
                    pullListView.setAdapter(mHeaderAdaper);
                }
            }
       }
        closeProgressDialog();
    }

    /**
     * 074 逾期查询接口
     */
    @Override
    public void querOverdueFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /**
     * 074 逾期查询接口成功
     */
    @Override
    public void queryOverdueSuccess(LoanOverdueModel loanOverdue) {

        if (loanOverdue != null) {
            mOverdue = loanOverdue.getOverdueStatus();
        }

    }

    /**
     * 025查询借记卡号
     */
    @Override
    public void queryCardNumByAcctNumFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showErrorDialog(biiResultErrorException.getErrorMessage());
    }

    /**
     * 025查询借记卡号成功
     */
    @Override
    public void queryCardNumByAcctNumSuccess(String cardNum) {
        closeProgressDialog();
        if (!TextUtils.isEmpty(cardNum)) {
            mCardNum = cardNum;
        }
        getLoanPager();
    }

    @Override
    public void setPresenter(LoanMangerContract.Presenter presenter) {

    }

    /**
     * 贷款跳转页面
     */
    private void getLoanPager() {
        if (mCycleType == null || settledListModel == null) {
            return;
        }
        //循环类贷款
        if ("R".equals(mCycleType)) {

            ReloanStatusFragment statusFragment = new ReloanStatusFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(LoanCosnt.LOAN_DATA, mLoanAccountList.get(mPosition));
            bundle.putString(LoanCosnt.LOAN_ENDDATE, ApplicationContext.getInstance().getCurrentSystemDate().
                    format(DateFormatters.dateFormatter1));
            bundle.putString(LoanCosnt.LOAN_OVERDUE, mOverdue);
            bundle.putString(LoanCosnt.LOAN_REPYNUM, mCardNum);
            statusFragment.setArguments(bundle);
            start(statusFragment);
        } //非循环贷款
        else {
            NonReloanQryFragment reloanQryFragment = new NonReloanQryFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(LoanCosnt.LOAN_DATA, mLoanAccountList.get(mPosition));
            bundle.putString(LoanCosnt.LOAN_ENDDATE, accountListModel.getEndDate());
            bundle.putString(LoanCosnt.LOAN_REPYNUM, mCardNum);
            reloanQryFragment.setArguments(bundle);
            start(reloanQryFragment);
        }
    }

    @Override
    protected LoanMangerPresenter initPresenter() {
        return new LoanMangerPresenter(this);
    }

    /**
     * 判断是否年满18岁
     * @param sysDate 系统时间
     * @return true：>=18 且<= 65 false <18
     */
    public boolean isHas18Old(String sysDate, String IDNO){
        if(IDNO == null || IDNO.length()<10){
            return false;
        }
        if(sysDate == null){
            return false;
        }
        Date systemDate;
        Date birthDay;
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        try{
            systemDate=fmt.parse(sysDate);
        }catch(Exception e){
            return false;
        }
        try{
            String dateStr = IDNO.substring(6,14);
            birthDay =fmt.parse(dateStr);

        }catch(Exception e){
            return false;
        }
        int typeNo = getAgeByBirthday(systemDate, birthDay);
        if (typeNo >= 18 || typeNo <= 65) {
            return true;
        } else {
            return false;
        }
    }


    private static int getAgeByBirthday(Date sysDate, Date birthday) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(sysDate);

        if (cal.before(birthday)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birthday);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                age--;
            }
        }
        return age;
    }

}
