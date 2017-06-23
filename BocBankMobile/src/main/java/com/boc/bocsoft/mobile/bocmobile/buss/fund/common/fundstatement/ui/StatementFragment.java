package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.ui;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.container.ContainerPageAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.container.IContainer;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectCenterListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectCenterStringListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectStringListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.expandableitemview.ExpandableItemView;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.model.PersionaltrsModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.model.StatementModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.presenter.StatementPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.widget.tabLayout.SlidingTabLayout;

import org.threeten.bp.LocalDateTime;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by huixiaobo on 2016/11/22.
 * 基金对账单
 */
public class StatementFragment extends MvpBussFragment<StatementPresenter>
        implements StatementContract.StatementView {

    protected View rootView;
    protected ImageView leftIconIv;
    protected TextView titleTv;
    protected TextView rightTv;
    protected LinearLayout rightButtonContainer;

    protected RelativeLayout fundmanageTitle;
    protected ListView stateRecordLv;
    protected TextView tvNodate;
    protected LinearLayout llNoDate;
    protected ExpandableItemView toMarketValue;
    protected ExpandableItemView toFloatLoss;
    public ViewPager vpContent;
    protected SlidingTabLayout lytTab;
    protected StatementScrollView stsl;
    protected LinearLayout makeValuell;
    private StatementModel mStatement;
    private View contentHeardView;
    /**上个月*/
    private String months;

    //listview上一层主要布局的高度(不含标题栏)，listview会默认添加一个相同高度的header，以实现悬浮效果
    private int mainContentHeight = 0;
    /**
     * viewpager滑动条高度
     */
    private int viewpageTabHeight = 0;
    /**
     * 交易流水
     */
    private List<PersionaltrsModel> mPersionalList;
    // 容器的pageAdapter
    private ContainerPageAdapter mPageAdapter;
    /**
     * 持仓信息对象
     */
    private StatementRcdView rcdView;
    /**
     * 交易流水
     */
    private PersionltrsView persionltrsView;
    /**
     * 当前时间
     */
    private LocalDateTime date;

    /**
     * 弹出框组件
     */
    private SelectStringListDialog dateMothsDialog;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fund_statementfragment, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        //标题栏布局
        leftIconIv = (ImageView) rootView.findViewById(R.id.leftIconIv);
        titleTv = (TextView) rootView.findViewById(R.id.titleTv);
        rightTv = (TextView) rootView.findViewById(R.id.rightTv);
        rightButtonContainer = (LinearLayout) rootView.findViewById(R.id.rightButtonContainer);
        fundmanageTitle = (RelativeLayout) rootView.findViewById(R.id.fundmanageTitle);
        stateRecordLv = (ListView) rootView.findViewById(R.id.stateRecordLv);

        contentHeardView = rootView.findViewById(R.id.contentHeardView);
        tvNodate = (TextView) rootView.findViewById(R.id.tv_nodate);
        llNoDate = (LinearLayout) rootView.findViewById(R.id.ll_no_date);
        toMarketValue = (ExpandableItemView) rootView.findViewById(R.id.toMarketValue);
        toFloatLoss = (ExpandableItemView) rootView.findViewById(R.id.toFloatLoss);
        vpContent = (ViewPager) rootView.findViewById(R.id.vpContent);
        lytTab = (SlidingTabLayout) rootView.findViewById(R.id.lyt_tab);
        stsl = (StatementScrollView) rootView.findViewById(R.id.stsl);
        makeValuell = (LinearLayout) rootView.findViewById(R.id.makeValuell);

    }


    @Override
    public void initData() {
        super.initData();

        //当前时间
        date = ApplicationContext.getInstance().getCurrentSystemDate().minusMinutes(1);
        months = date.format(DateFormatters.monthFormatter2);
        rightTv.setText(months);
        loadingData(months);


    }

    private void setListViewScrollListener() {
        stsl.setOnScrollListener(new StatementScrollView.OnScrollListener() {
            @Override
            public void onScrollchanged(int t) {
                int translation = Math.max(t, vpContent.getTop() - viewpageTabHeight);
                lytTab.setTranslationY(translation);
            }

            @Override
            public void onTouchUp() {

            }

            @Override
            public void onTouchDown() {

            }
        });
        contentHeardView.post(new Runnable() {
            @Override
            public void run() {
                mainContentHeight = contentHeardView.getHeight();
                viewpageTabHeight = lytTab.getHeight();

            }
        });

        stsl.smoothScrollTo(0, 0);
    }

    /**
     * 对账单网络请求
     */
    private void loadingData(String monthsTime) {
        if (date == null) {
            return;
        }
        stsl.setVisibility(View.GONE);

        showLoadingDialog();
        //053查询持仓信息
        getPresenter().queryFundStatement(monthsTime);
    }

    /**
     * 持仓信息与交易流水
     *
     * @param
     */
    private void pagerView() {
        if (mStatement == null && mPersionalList == null) {
            llNoDate.setVisibility(View.VISIBLE);
            tvNodate.setText(getString(R.string.boc_statement_hit));
            return;
        }
        stsl.setVisibility(View.VISIBLE);
        rcdView = new StatementRcdView(getContext());
        rcdView.attach(this);
        rcdView.setDate(mStatement);
        persionltrsView = new PersionltrsView(getContext());
        persionltrsView.attach(this);
        persionltrsView.setData(mPersionalList);

        List<IContainer> containerList = new ArrayList<>();
        containerList.add(rcdView);
        containerList.add(persionltrsView);
        List<String> titleList = new ArrayList<>();
        titleList.add(getString(R.string.boc_fundstatement_viewtitle));
        titleList.add(getString(R.string.boc_fundstatement_persionltrs));

        titleList.add(getResources().getString(R.string.boc_crcd_bill_n_tab_1));
        mPageAdapter = new ContainerPageAdapter(titleList, containerList);

        vpContent.setAdapter(mPageAdapter);
        vpContent.setOffscreenPageLimit(2);
        lytTab.setViewPager(vpContent);

        setListViewScrollListener();
    }

    /**
     * 显示总市值，总盈亏数据
     */
    private void settleData() {
        if (mStatement == null || mStatement.getList() == null
                || mStatement.getList().size() <= 0) {
            makeValuell.setVisibility(View.GONE);
            return;
        }

        Collections.sort(mStatement.getList(),new beanCompare());
        List<ExpandableItemView.ItemView> maketItem = new ArrayList<>();
        List<ExpandableItemView.ItemView> lessitemViews = new ArrayList<>();
        for (StatementModel.ListBean listBean : mStatement.getList()) {
            String makeValue = listBean.getMarketValue().toString();
            String lessValue = listBean.getFloatLoss().toString();
            String currency= DataUtils.getCurrencyDescByLetter(mContext,listBean.getCurrency());
            ExpandableItemView.ItemView mv = new ExpandableItemView.ItemView(makeValue, currency);
            maketItem.add(mv);

            ExpandableItemView.ItemView fv = new ExpandableItemView.ItemView(lessValue, currency);
            lessitemViews.add(fv);
        }
        toMarketValue.setItems(maketItem);
        toFloatLoss.setItems(lessitemViews);
    }

    static class beanCompare implements Comparator<StatementModel.ListBean> {

        @Override
        public int compare(StatementModel.ListBean lhs, StatementModel.ListBean rhs) {

            if (Integer.valueOf(lhs.getCurrency()) > Integer.valueOf(rhs.getCurrency())) {
                return 1;
            } else {
                return -1;
            }
        }
    }


    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }


    @Override
    public void setListener() {
        super.setListener();
        leftIconIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleLeftIconClick();
            }
        });
        rightButtonContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMonthsDialog();
            }
        });

    }

    /**
     * 日期月份选择框
     */
    private void showMonthsDialog() {
        List<String> monthsList = new ArrayList<String>();
        for (int i = 0; i < 7; i++) {
            String months = date.minusMonths(i).format(DateFormatters.monthFormatter2);
            monthsList.add(months);
        }
        if (dateMothsDialog == null) {
            dateMothsDialog = new SelectStringListDialog(getContext());
        }
        dateMothsDialog.setHeaderTitleValue("选择月份");
        dateMothsDialog.isShowHeaderTitle(true);
        dateMothsDialog.setListData(monthsList);
        dateMothsDialog.setOnSelectListener(new SelectListDialog.OnSelectListener<String>() {
            @Override
            public void onSelect(int position, String model) {
                rightTv.setText(model);
                dateMothsDialog.dismiss();
                loadingData(model);
            }
        });
        if (!dateMothsDialog.isShowing()) {
            dateMothsDialog.show();
        }
    }

    /**
     * 抬头左侧点击事件
     */
    @Override
    protected void titleLeftIconClick() {
        super.titleLeftIconClick();
    }

    /**
     * 抬头右侧点击事件
     */
    @Override
    protected void titleRightIconClick() {
        super.titleRightIconClick();
    }

    @Override
    protected StatementPresenter initPresenter() {
        return new StatementPresenter(this);
    }

    /**
     * 053持仓信息接口返回失败
     */
    @Override
    public void fundStatementFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /**
     * 053持仓信息接口返回成功
     */
    @Override
    public void fundStatementSuccess(StatementModel statementList) {
        if (statementList != null) {
            mStatement = statementList;
        }

        getPresenter().queryPersionalTrans(getMothsLastTime(date.getYear(),date.getMonthValue()),
                getMothsFirstTime(date.getYear(),date.getMonthValue()));

    }
    /**月份第一天*/
    private String getMothsFirstTime(int year,int moths){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, moths -1);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH , maxDay);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        return format.format(calendar.getTime());
    }

    /**月份最后一天*/
    private String getMothsLastTime(int year,int moths){
        Calendar calendar =Calendar.getInstance();
        calendar.set(calendar.YEAR,year);
        calendar.set(Calendar.MONTH, moths -1);
        int minDay =calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, minDay);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        return format.format(calendar.getTime());
    }

    /**
     * 053交易流水请求失败
     */
    @Override
    public void fundPersionalTransFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /**
     * 059交易流水请求成功
     */
    @Override
    public void fundPersionalTransSuccess(List<PersionaltrsModel> persionalList) {
        closeProgressDialog();
        if (persionalList != null && persionalList.size() > 0) {
            mPersionalList = persionalList;
        }

        if (mStatement != null && mStatement.getList() != null
                && mStatement.getList().size() > 0) {
            settleData();
            pagerView();

        } else {
            llNoDate.setVisibility(View.VISIBLE);
            tvNodate.setText(getString(R.string.boc_statement_hit));

        }

    }

    @Override
    public void setPresenter(StatementContract.Presenter presenter) {

    }


}
