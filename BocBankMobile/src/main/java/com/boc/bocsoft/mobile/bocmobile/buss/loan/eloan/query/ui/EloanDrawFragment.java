package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.PartialLoadView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.waterwaveballview.WaterWaveBallView;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.LoanCosnt;

import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.ui.ChangeAccountResultFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.ui.ChangeRepaymentAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.ui.DrawFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanAccountListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanQuoteViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanStatusListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.presenter.EloanqueryPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui.adapter.RepayAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui.view.EloanListView;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by louis.hui on 2016/10/12.
 * 中银E贷用款页面
 */
public class EloanDrawFragment extends MvpBussFragment<EloanqueryPresenter> implements
        EloanQueryContract.DrawView,View.OnClickListener {

    private View rootView;
    protected WaterWaveBallView waveBllView;
    protected TextView explainTv;
    protected TextView sumTv;
    protected TextView reachdateTv;
    protected TextView drawEloanTv;
    protected TextView eloanRateTv;
    protected TextView exceptionTv;
    protected TextView eloanDrawingTv;
    protected LinearLayout drawingredLl;
    protected View lineview;
    protected EloanListView drawingRecordLv;
    protected TextView skipTv;
    protected LinearLayout eloanSettled;
    protected PartialLoadView ivLoading;
    /**中银E贷签约对象*/
    private EloanQuoteViewModel mEloanQuote;
    /**是否开始加载*/
    private boolean isLoading;
    /** 测试错误提示码提示异常 */
    private static final String ELOAN_ERRORCODE = "BANCS.0188";
    /**逾期常量*/
    private static final String LOAN_OVERDUE = "Y";
    /**有款对象*/
    private EloanAccountListModel loandata;
    /**当前加载页码*/
    private int pageCurrentIndex = 0;
    /**每页大小*/
    private final static int pageSize = ApplicationConst.PAGE_SIZE;
    /**结清 未结清 标识*/
    private boolean isDrawFlag;
    /**已结清*/
    private EloanStatusListModel mEloanSettled;
    /**未结清*/
    private EloanStatusListModel mEloanRepay;
    /**中银E贷签约类型数据*/
    private EloanStatusListModel mEloanQuoteData;
    /**首页适配器*/
    private RepayAdapter mAdapter;
    /**逾期信息*/
    private String mOverdue;
    private ScrollView drawScrll;
    private Handler mHandler;
    /**当前时间*/
    private String time;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_firstdraw_fragment, null);
        return rootView;
    }

    @Override
    protected EloanqueryPresenter initPresenter() {
        return new EloanqueryPresenter(this);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return true;

    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_eloan_middleTitle);
    }

    @Override
    protected void titleRightIconClick() {
        super.titleRightIconClick();
        // 功能页面
        if (mEloanQuoteData != null) {
            EloanMoreOptionFragment eloanMoreOptionFragment = new EloanMoreOptionFragment();
            if (mEloanRepay != null) {
                eloanMoreOptionFragment.setEloanDrawModel(mEloanRepay, mOverdue);
            } else {
                eloanMoreOptionFragment.setEloanDrawModel(mEloanQuoteData, mOverdue);
            }
            start(eloanMoreOptionFragment);
        }
    }

    @Override
    public void initView() {
        super.initView();
        waveBllView = (WaterWaveBallView) rootView.findViewById(R.id.waveBllView);
        explainTv = (TextView) rootView.findViewById(R.id.explainTv);
        sumTv = (TextView) rootView.findViewById(R.id.sumTv);
        reachdateTv = (TextView) rootView.findViewById(R.id.reachdateTv);
        drawEloanTv = (TextView) rootView.findViewById(R.id.drawEloanTv);
        eloanRateTv = (TextView) rootView.findViewById(R.id.eloanRateTv);
        exceptionTv = (TextView) rootView.findViewById(R.id.exceptionTv);
        drawScrll = (ScrollView) rootView.findViewById(R.id.drawScrll);
        eloanDrawingTv = (TextView) rootView.findViewById(R.id.eloanDrawingTv);
        drawingredLl = (LinearLayout) rootView.findViewById(R.id.drawingredLl);
        lineview = (View) rootView.findViewById(R.id.lineview);
        skipTv = (TextView) rootView.findViewById(R.id.skipTv);
        drawingRecordLv = (EloanListView) rootView.findViewById(R.id.drawingRecordLv);
        eloanSettled = (LinearLayout) rootView.findViewById(R.id.eloanSettled);
        ivLoading = (PartialLoadView) rootView.findViewById(R.id.iv_loading);
        ivLoading.setOnClickListener(this);
        drawEloanTv.setOnClickListener(this);
        skipTv.setOnClickListener(this);
        waveBllView.startWave();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    drawScrll.scrollTo(10,10);
                }
            }
        };
        drawScrll.scrollTo(0,0);
    }

    /**
     * 接收上个页面传递的值
     * @param quoteData E贷签约对象
     */
    public static EloanDrawFragment newInstance(EloanQuoteViewModel quoteData, String overdue) {
        EloanDrawFragment drawFragment = new EloanDrawFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EloanConst.LOAN_QUOTENO, quoteData);
        bundle.putString(LoanCosnt.LOAN_OVERDUE, overdue);
        drawFragment.setArguments(bundle);
        return drawFragment;
    }

    @Override
    public void reInit() {
        super.reInit();
        mHandler.sendEmptyMessage(0);
   }

    @Override
    public void initData() {
        super.initData();
        //上页面传值中银E贷
        mEloanQuote = (EloanQuoteViewModel) getArguments().getSerializable(EloanConst.LOAN_QUOTENO);
        //签约类型数据
        mEloanQuoteData = biultEloanStatuslist(mEloanQuote, null);
        //逾期信息
        mOverdue = getArguments().getString(LoanCosnt.LOAN_OVERDUE);
        setPagerData(mEloanQuote);
        if (!isLoading) {
            //首次加载数据
            eloanSettled.setVisibility(View.GONE);
            //加载动画
            setLoadStatus(PartialLoadView.LoadStatus.LOADING);
            queryLoanData("Y", mEloanQuote.getQuoteType());

        }
    }

    /**
     * 页面初始化值
     * @param loanquote 显示参数对象
     */
    private void setPagerData(EloanQuoteViewModel loanquote) {
        if (loanquote == null) {
            return;
        }
         LocalDateTime date =  ApplicationContext.getInstance().getCurrentSystemDate();
       // 格式化当前时间
        time = date.format(DateFormatters.dateFormatter1);
        if (!TextUtils.isEmpty(loanquote.getQuoteState())) {

            if ("20".equals(loanquote.getQuoteState())) {
                drawEloanTv.setText(getResources().getString(R.string.boc_repay_loss));
            } else if ("10".equals(loanquote.getQuoteState())){
                drawEloanTv.setText(getResources().getString(R.string.boc_amount_canlce));
            } else if ("40".equals(loanquote.getQuoteState())) {
                drawEloanTv.setText(getResources().getString(R.string.boc_amount_off));
            } //判断逾期字段
            else if (LOAN_OVERDUE.equals(mOverdue)) {
                drawEloanTv.setText(getResources().getString(R.string.boc_overduehit));

            } //判断额度到期
            else if (!TextUtils.isEmpty(time) && !TextUtils.isEmpty(loanquote.getLoanToDate())
                    && isDateBeforeNotEqual(loanquote.getLoanToDate(),time)) {
                drawEloanTv.setText(getResources().getString(R.string.boc_quote_todate));
            }
            //我要用款
            else {
                drawEloanTv.setText(getResources().getString(R.string.boc_eloan_submitEloanTv));
                //drawEloanTv.setBackgroundResource(R.drawable.boc_bg_round_blue_text);
                drawEloanTv.setBackgroundResource(R.drawable.boc_eloan_but);
            }

        }

        explainTv.setText(getResources().getString(R.string.boc_eloan_drawtitle));
        sumTv.setText(MoneyUtils.getLoanAmountShownRMB(loanquote.getAvailableAvl()));
        waveBllView.setFinallyWaterHeight((Float.parseFloat(loanquote.getAvailableAvl())/Float.parseFloat(loanquote.getLoanBanlance())));

        reachdateTv.setText(loanquote.getLoanToDate() + "到期");

        eloanRateTv.setText(getResources().getString(R.string.boc_eloan_interest)
               + " "+loanquote.getRate()+ "%");
    }

    /***
     * 052接口请求数据
     */
    private void queryLoanData(String eFlag, String quote){
        loandata = new EloanAccountListModel();
        loandata.seteFlag(eFlag);
        loandata.seteLoanState(quote);
        loandata.setPageSize(pageSize);
        loandata.setCurrentIndex(pageCurrentIndex);
        loandata.set_refresh("true");
        getPresenter().queryLoanAccount(loandata);

    }

    @Override
    public void setListener() {
        super.setListener();
        drawingRecordLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EloanRepayDetailFragment eloanDrawingDetailFragment = new EloanRepayDetailFragment();
                Bundle bundle = new Bundle();
                // 还款对象
                bundle.putSerializable(EloanConst.ELON_PREPAY_DETAIL,
                        mEloanRepay.getAccountListModel()
                                .getLoanList().get(position));
                // 还款账号
                bundle.putString(EloanConst.PEPAY_ACCOUNT,
                        mEloanRepay.getPayAccount());
                // 当前时间
                bundle.putString(EloanConst.DATA_TIME, mEloanRepay
                        .getAccountListModel().getEndDate());
                //判断跳转到那个页
                bundle.putBoolean(EloanConst.LOAN_PREPAY_DETAIL, true);
                //额度编号
                bundle.putString(EloanConst.LOAN_QUOTENO, mEloanRepay.getQuoteNo());
                eloanDrawingDetailFragment.setArguments(bundle);
                start(eloanDrawingDetailFragment);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.drawEloanTv) {
            // 我要用款
            if (ApplicationContext.getInstance().getUser().getMobile() == null) {
                showErrorDialog(getString(R.string.boc_eloan_mobile));
                return;
            }
            //逾期判断
            if (mEloanQuoteData != null) {
                if (LOAN_OVERDUE.equals(mOverdue)) {
                    return;
                }
                // 判断取消关闭 冻结状态
                if (isQuoteState()) {
                    return;
                }
                // 额度到期日判断
                if (isDateBeforeNotEqual(mEloanQuote.getLoanToDate(),time)) {
                    return;
                }
                // 判断用款金额大于1000;
                if (Double.parseDouble(mEloanQuoteData.getAvailableAvl()) >= Double
                        .parseDouble(EloanConst.ELOAN_DRAWAMOUNT)) {
                    DrawFragment drawFragment = new DrawFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(EloanConst.ELON_DRAW,
                            mEloanQuoteData);
                    if(!TextUtils.isEmpty(ChangeRepaymentAccountFragment.mQuoteno)
                            && ChangeRepaymentAccountFragment.mQuoteno.equals(mEloanQuoteData.getQuoteNo())
                            &&!StringUtils.isEmptyOrNull(ChangeAccountResultFragment.newAccount)) {

                        bundle.putString(EloanConst.PEPAY_ACCOUNT, ChangeAccountResultFragment.newAccount);
                    } else {
                        bundle.putString(EloanConst.PEPAY_ACCOUNT, mEloanQuoteData.getPayAccount());
                    }
                    drawFragment.setArguments(bundle);
                    start(drawFragment);
                } else {
                    showErrorDialog(getString(R.string.boc_repayAmount_flag));
                }
            }
        } else if (i == R.id.skipTv) {
            // 更多 ，已结清
           // EloanRepayRecordFragment recordFragment = new EloanRepayRecordFragment();
            EloanRepayFragment recordFragment = new EloanRepayFragment();
            Bundle bundle = new Bundle();
            // 账户类型
            bundle.putString(EloanConst.ELOAN_QUOTETYPE,
                    mEloanRepay.getQuoteType());
            // 还款账号
            bundle.putString(EloanConst.PEPAY_ACCOUNT,
                    mEloanRepay.getPayAccount());
            //额度编号
            bundle.putString(EloanConst.LOAN_QUOTENO, mEloanRepay.getQuoteNo());
            // 已结清 未结清列表
            recordFragment.setData(mEloanRepay, mEloanSettled);
            if (mEloanRepay.getAccountListModel() != null
                    && mEloanRepay.getAccountListModel().getLoanList() != null
                    && mEloanRepay.getAccountListModel().getLoanList()
                    .size() > 10) {
                bundle.putString(EloanConst.ELOAN_REPAYFLAG, "N");
            } else {
                bundle.putString(EloanConst.ELOAN_REPAYFLAG, "Y");
            }
            recordFragment.setArguments(bundle);
            start(recordFragment);
        } else  if (i == R.id.iv_loading) {
            if (isLoading) {
                setLoadStatus(PartialLoadView.LoadStatus.LOADING);
                queryLoanData("Y", mEloanQuote.getQuoteType());
            }
        }

    }

    /**
     * 052接口已结清列表失败
     */
    @Override
    public void eLoanSettleFail(BiiResultErrorException biiResultErrorException) {
        if (!TextUtils.isEmpty(biiResultErrorException.getErrorCode())
                &&biiResultErrorException.getErrorCode().equals(ELOAN_ERRORCODE)) {
            queryLoanData("N", mEloanQuote.getQuoteType());

        } else {
            setLoadStatus(PartialLoadView.LoadStatus.REFRESH);
            showErrorDialog(biiResultErrorException.getErrorMessage());
            isLoading = true;
        }
    }

    /**
     * 052接口已结清列表成功
     */
    @Override
    public void eLoanSettleSuccess(EloanAccountListModel eAccountResul) {
        isDrawFlag = true;
        mEloanSettled = biultEloanStatuslist(mEloanQuote, eAccountResul);
        queryLoanData("N", mEloanQuote.getQuoteType());
    }
    /**
     * 052接口未结清列表失败
     */
    @Override
    public void eLoanRepayFail(BiiResultErrorException biiResultErrorException) {
        if (!TextUtils.isEmpty(biiResultErrorException.getErrorCode()) &&
                biiResultErrorException.equals(ELOAN_ERRORCODE)) {
            setPegerRepay();
        } else {
            setLoadStatus(PartialLoadView.LoadStatus.REFRESH);
            showErrorDialog(biiResultErrorException.getErrorMessage());
            isLoading = true;
        }
    }

    /**
     * 052接口未结清列表成功
     */
    @Override
    public void eLoanRpaySusccess(EloanAccountListModel eAccountResult) {
        isLoading = true;
        setLoadStatus(PartialLoadView.LoadStatus.SUCCESS);
        eloanSettled.setVisibility(View.VISIBLE);
        //未结清用款
        mEloanRepay = biultEloanStatuslist(mEloanQuote, eAccountResult);
        setPegerRepay();
    }

    @Override
    public void setPresenter(EloanQueryContract.Presenter presenter) {

    }

    /**
     * 显示未还款列表
     */
    private void setPegerRepay() {
        if (mEloanRepay != null && mEloanRepay.getAccountListModel() != null
                && mEloanRepay.getAccountListModel().getLoanList() != null && mEloanRepay.getAccountListModel().getLoanList().size() > 0) {
            List<EloanAccountListModel.PsnLOANListEQueryBean> quoteList = getQuoteList(mEloanRepay.getAccountListModel().getLoanList());
            //显示未还款记录条数 和笔数
            eloanDrawingTv.setText(getResources().getString(R.string.boc_repayhat) + "共"
                    + (mEloanRepay.getAccountListModel().getRecordNumber()) +"笔" );

            setVisibleView(true);
            // 显示10条未还款记录
            mAdapter = new RepayAdapter(getContext(),quoteList);
            drawingRecordLv.setAdapter(mAdapter);

            //判断是否是大于10条处理
            if (mEloanRepay.getAccountListModel().getLoanList().size() <= 10) {
                skipTv.setText(getResources().getString(R.string.boc_squrare_record));
            } else {
                skipTv.setText(getResources().getString(R.string.boc_moedel));
            }
        } else {
            if (mEloanSettled != null && mEloanSettled.getAccountListModel() != null
                    && mEloanSettled.getAccountListModel().getLoanList() != null
                    && mEloanSettled.getAccountListModel().getLoanList().size() > 0) {

                setVisibleView(false);
                exceptionTv.setVisibility(View.VISIBLE);
                exceptionTv.setText(getResources().getString(R.string.boc_eloan_repayNo));
                skipTv.setText(getResources().getString(R.string.boc_squrare_record));
            } else {
                setVisibleView(false);
                lineview.setVisibility(View.GONE);
                skipTv.setVisibility(View.GONE);
                exceptionTv.setText(getResources().getString(R.string.boc_eloan_repayNo));

            }
        }
    }

    /**
     * 保存数据
     * @param quote 中银E贷
     * @param eloanAccount 用款记录列表
     */
    private EloanStatusListModel biultEloanStatuslist (EloanQuoteViewModel quote, EloanAccountListModel eloanAccount) {
        EloanStatusListModel statusBean = new EloanStatusListModel();
        //添加签约对象
        statusBean.setQuoteViewModel(quote);
        statusBean.setQuoteState(quote.getQuoteState());
        statusBean.setQuoteType(quote.getQuoteType());
        statusBean.setLoanToDate(quote.getLoanToDate());
        statusBean.setAvailableAvl(quote.getAvailableAvl());
        statusBean.setLoanBanlance(quote.getLoanBanlance());
        statusBean.setUseAvl(quote.getUseAvl());
        statusBean.setRate(quote.getRate());
        statusBean.setCurrencyCode(quote.getCurrency());
        statusBean.setLoanType(quote.getLoanType());
        statusBean.setNextRepayDate(quote.getNextRepayDate());
        statusBean.setIssueRepayDate(quote.getIssueRepayDate());
        statusBean.setQuoteNo(quote.getQuoteNo());
        statusBean.setPayAccount(quote.getRepayAcct());
        statusBean.setQuoteViewModel(quote);
        //用款 结清 未结清标识
        if (isDrawFlag) {
            statusBean.setClear(true);
        }
        if (eloanAccount != null) {
            statusBean.setAccountListModel(eloanAccount);
        }
        return statusBean;
    }

    /**
     * 判断是否是冻结、取消、关闭状态
     */
    private boolean isQuoteState() {
        if (!TextUtils.isEmpty(mEloanQuote.getQuoteState())) {
            if (mEloanQuote.getQuoteState().equals("20")
                    || mEloanQuote.getQuoteState().equals("10")
                    || mEloanQuote.getQuoteState().equals("40")) {
                return true;
            }
        }
        return false;

    }

    /**
     * 页面显示10条件记录
     */
    private List<EloanAccountListModel.PsnLOANListEQueryBean> getQuoteList
    (List<EloanAccountListModel.PsnLOANListEQueryBean> unClearList) {
        List<EloanAccountListModel.PsnLOANListEQueryBean> quoteType = new ArrayList<EloanAccountListModel.PsnLOANListEQueryBean>();
        if (unClearList != null && unClearList.size() > 0) {
            if (unClearList.size() <= 10) {
                quoteType = unClearList;
            } else {
                for (int i = 0; i < 10; i++) {
                    quoteType.add(unClearList.get(i));
                }
            }
        }
        return quoteType;
    }

    /**
     * 是否可显示组件
     * @param isVisible 是否可显示
     */
    private void setVisibleView (boolean isVisible) {
        if (isVisible) {
            drawingRecordLv.setVisibility(View.VISIBLE);
            drawingredLl.setVisibility(View.VISIBLE);
            lineview.setVisibility(View.VISIBLE);
            skipTv.setVisibility(View.VISIBLE);
            exceptionTv.setVisibility(View.GONE);
        } else {
            drawingRecordLv.setVisibility(View.GONE);
            drawingredLl.setVisibility(View.GONE);
            lineview.setVisibility(View.VISIBLE);
            skipTv.setVisibility(View.VISIBLE);
            exceptionTv.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 显示加载动画，或者显示刷新按钮，或者加载成功隐藏按钮。
     * 刷新按钮时可点击
     *
     * @param loadStatus 加载状态
     */
    public void setLoadStatus(PartialLoadView.LoadStatus loadStatus) {
        ivLoading.setLoadStatus(loadStatus);
    }

    /**
     * 比较date2是否在date1之后（日期相等返回false）
     *
     * @param date1
     *            yyyy/MM/dd格式
     * @param date2
     *            yyyy/MM/dd格式
     * @return true if date2>date1
     */
    public static Boolean isDateBeforeNotEqual(String date1, String date2) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        if (date1 == null || date2 == null) {
            return false;
        } else {
            Date date11 = null;
            Date date22 = null;
            try {
                date11 = format.parse(date1);
                date22 = format.parse(date2);
            } catch (ParseException e) {
                return false;
            }
            return date22.after(date11);
        }
    }

}
