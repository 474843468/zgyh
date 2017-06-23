package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DateTimePicker;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.SlipMenu.SlipDrawerLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.expandableitemview.ExpandableItemView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttimerangenew.SelectTimeRangeNew;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.model.FundFloatingProfileLossModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.presenter.FundFloatingProfileLossPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui.adapter.FundFloatingProfileLossListAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui.bean.FundFloatingProfileLossBean;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by taoyongzhen on 2016/12/23.
 */

public class FundFloatProfileLosslistFragemt extends MvpBussFragment<FundFloatingProfileLossConst.Prrsenter> implements FundFloatingProfileLossConst.queryFloatingProfitLossView
        , SelectTimeRangeNew.ClickListener, ListView.OnItemClickListener {

    private View rootView;
    private SlipDrawerLayout mDrawerLayout;//侧滑菜单
    private LinearLayout footView;
    private LinearLayout llyHeader; //头部布局
    private SelectTimeRangeNew rightDrawer;//侧滑菜单
    private ExpandableItemView totleView;
    private TitleBarView queryTitleView;
    //右侧提示语
    private TextView titleRightText;

    //筛选处的范围
    private TextView tvTransferRecordRange;

    //筛选
    private TextView tvTransferRecordSelect;
    //筛选的imageView
    private ImageView ivTransferRecordSelect;

    //起始时间
    private LocalDate startLocalDate;
    //结束时间
    private LocalDate endLocalDate;
    //最大查询范围为3个月
    private final static int MAX_QUERY_RANGE = 12;
    //最大查询起始日期为一年
    private final static int MAX_QUERY_DATE = 12 * 3;
    private String timeRangeHint;

    private ListView listView;

    private FundFloatingProfileLossListAdapter adapter;

    private LinearLayout llNoDate;
    private TextView tvNodata; //无数据提示

    private String fundCode = "";//由上一级页面传递过来
    private boolean isFirstQuery = true;//首次进入查询失败后，显示无数据页面，应隐藏筛选框；反之，由筛选查询，无数据应显示筛选框
    //保存返回结果中合计数，已不同币种计算
    private ArrayList<FundFloatingProfileLossModel.ResultListBean> totalBeans = new ArrayList<>();
    private List<FundFloatingProfileLossModel.ResultListBean> singleBeans = new ArrayList<>();

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_fund_floatprofileloss_list, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        mDrawerLayout = (SlipDrawerLayout) rootView.findViewById(R.id.layoutSlipDrawer);
        llyHeader = (LinearLayout) rootView.findViewById(R.id.llyHeader);
        llNoDate = (LinearLayout) rootView.findViewById(R.id.ll_no_date);
        tvNodata = (TextView) rootView.findViewById(R.id.tv_nodate);
        rightDrawer = (SelectTimeRangeNew) rootView.findViewById(R.id.viewSelectTime);
        rightDrawer.setSingleSelect(false);
        listView = (ListView) rootView.findViewById(R.id.query_view);
        initQueryTitleView();
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    /**
     * 初始化Title
     *
     */
    private void initQueryTitleView() {
        queryTitleView = new TitleBarView(llyHeader.getContext());
        queryTitleView.setStyle(R.style.titlebar_common_white);
        queryTitleView.setTitle(getResources().getString(R.string.boc_fund_profileloss_title));
        queryTitleView.getRightTextButton().setTextColor(mContext.getResources().getColor(R.color.boc_text_money_color_red));
        queryTitleView.setRightButton(getString(R.string.boc_fund_profile_one_year), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.toggle();
            }
        });
        titleRightText = queryTitleView.getRightTextButton();
        queryTitleView.setLeftButtonOnClickLinster(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleLeftIconClick();
            }
        });

        llyHeader.addView(queryTitleView);

    }

    @Override
    public void initData() {
        super.initData();
        timeRangeHint = getString(R.string.boc_fund_profile_one_year);
        adapter = new FundFloatingProfileLossListAdapter(mContext);
        listView.setAdapter(adapter);
        startLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().plusMonths(-MAX_QUERY_RANGE).plusDays(1);
        endLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();
        rightDrawer.setDefaultDate(startLocalDate.format(DateFormatters.dateFormatter1),
                endLocalDate.format(DateFormatters.dateFormatter1));
        queryList();

    }

    @Override
    public void setListener() {
        super.setListener();
        rightDrawer.setListener(this);
        listView.setOnItemClickListener(this);

    }

    private void queryList() {
        showLoadingDialog();
        FundFloatingProfileLossModel model = new FundFloatingProfileLossModel();
        model.setFundCode(fundCode);
        model.setStartDate(startLocalDate.format(DateFormatters.dateFormatter1));
        model.setEndDate(endLocalDate.format(DateFormatters.dateFormatter1));
        getPresenter().queryFloatingProfitLoss(model);
    }


    @Override
    protected FundFloatingProfileLossConst.Prrsenter initPresenter() {
        return new FundFloatingProfileLossPresenter(this);
    }

    @Override
    public void queryFloatingProfitLossFail(BiiResultErrorException exception) {//查询失败：近一年提示应该予以显示
        closeProgressDialog();
        showNodateView();
    }

    @Override
    public void queryFloatingProfitLossSuccess(FundFloatingProfileLossModel model) {

        closeProgressDialog();
        boolean noDate = model == null || model.getResultList() == null
                || model.getResultList().size() <= 0;
        if (noDate) {
            showNodateView();
        } else {
            List<FundFloatingProfileLossBean> beans = new ArrayList<>();
            totalBeans.clear();
            for (FundFloatingProfileLossModel.ResultListBean modelBean : model.getResultList()) {
                if (getString(R.string.boc_fund_profileloss_list_totle_hint).equals(modelBean.getFundCode())) {
                    totalBeans.add(modelBean);
                    continue;
                }
                FundFloatingProfileLossBean bean = new FundFloatingProfileLossBean();
                String leftContent = modelBean.getFundName();
                if (StringUtil.isNullOrEmpty(modelBean.getFundCode()) == false) {
                    leftContent = leftContent + "（" + modelBean.getFundCode() + "）";
                }
                bean.setLeftContent(leftContent);
                String rightFirstContent = MoneyUtils.transMoneyFormat(modelBean.getTrAmount(), modelBean.getCurceny());
                String rightSecondContent = DataUtils.getCurrencyAndCashFlagDes(mContext, modelBean.getCurceny(), modelBean.getCashFlag());
                if (StringUtil.isNullOrEmpty(rightFirstContent)) {
                    rightFirstContent = "-";
                    rightSecondContent = "";
                }
                bean.setRightFirstContent(rightFirstContent);
                //单位
                if (StringUtil.isNullOrEmpty(rightSecondContent)) {
                    rightSecondContent = "";
                }
                bean.setRightSecondContent(rightSecondContent);
                beans.add(bean);
                singleBeans.add(modelBean);
            }
            adapter.setBeans(beans);
            initTotleView();
            showFootView(true);
            listView.setVisibility(View.VISIBLE);
        }
    }

    //合计计算；人民币取和；美元钞、汇取和；排序待定，暂时依据币种排序
    private void initTotleView() {
        if (totalBeans.size() <= 0 ) {
            if (totleView != null){
                totleView.setVisibility(View.GONE);
            }
            return;
        }
        if (totleView == null) {
            totleView = new ExpandableItemView(mContext);
            totleView.setHeadTextContent(getString(R.string.boc_fund_profileloss_list_totle_head));
            totleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoTotalDetailFragment();
                }
            });
        }

        Collections.sort(totalBeans, new FundFloatingProfileLossListFragment.beanCompare());
        List<ExpandableItemView.ItemView> itemViews = new ArrayList<>();
        for (FundFloatingProfileLossModel.ResultListBean bean : totalBeans) {
            String letfContent = MoneyUtils.transMoneyFormat(bean.getTrAmount(), bean.getCurceny());
            String rightContent = DataUtils.getCurrencyAndCashFlagDes(mContext, bean.getCurceny(), bean.getCashFlag());
            ExpandableItemView.ItemView itemView = new ExpandableItemView.ItemView(letfContent, rightContent);
            if (rightContent.startsWith(DataUtils.NUM_POSITIVE_FLAG) == false) {
                itemView.setLeftTextColor(R.color.boc_text_money_color_red);
            } else {
                itemView.setLeftTextColor(R.color.boc_text_color_green);
            }
            itemViews.add(itemView);
        }
        totleView.setItems(itemViews);
        totleView.setVisibility(View.VISIBLE);
        if (listView.getHeaderViewsCount() <= 0){
            listView.addHeaderView(totleView);
        }

    }

    private void showFootView(boolean isShow) {
        if (footView == null) {
            footView = new LinearLayout(mContext);
            footView.setPadding(mContext.getResources().getDimensionPixelSize(R.dimen.boc_space_between_30px),
                    mContext.getResources().getDimensionPixelSize(R.dimen.boc_space_between_20px),
                    mContext.getResources().getDimensionPixelSize(R.dimen.boc_space_between_30px),
                    mContext.getResources().getDimensionPixelSize(R.dimen.boc_space_between_20px));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            footView.setLayoutParams(params);
            TextView textView = new TextView(footView.getContext());
            textView.setTextAppearance(mContext, R.style.tv_small);
            textView.setText(getString(R.string.boc_fund_profileloss_list_hint));
            footView.addView(textView);
        }
        if (isShow) {
            if (listView.getFooterViewsCount() <= 0) {
                listView.addFooterView(footView);
            }
            footView.setVisibility(View.VISIBLE);
        } else {
            footView.setVisibility(View.GONE);
        }
    }

    @Override
    public void setPresenter(BasePresenter presenter) {

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position <= 0 || position > singleBeans.size()){
            return;
        }
        FundFloatProfileLossSingleDetailFragment fragment = new FundFloatProfileLossSingleDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(DataUtils.FUND_FLOAT_PROFILELESS_BEAN_KEY, singleBeans.get(position-1));
        bundle.putString(DataUtils.FUND_PROFILE_LOSS_TIME_HINT,queryTitleView.getRightTextButton().getText().toString());
        fragment.setArguments(bundle);
        start(fragment);
    }

    @Override
    public void startClick() {
        judgeStartTimeAndSet(LocalDate.parse(rightDrawer.getStartDate(), DateFormatters.dateFormatter1));
    }

    @Override
    public void endClick() {
        judgeEndTimeAndSet(LocalDate.parse(rightDrawer.getEndDate(), DateFormatters.dateFormatter1));
    }

    @Override
    public void cancelClick() {

    }

    @Override
    public void rightClick(boolean haveSelected, String content) {

        String startTime = rightDrawer.getStartDate();
        String endTime = rightDrawer.getEndDate();
        queryTitleView.getRightTextButton().setText(startTime + "-" + endTime);
        startLocalDate = LocalDate.parse(startTime, DateFormatters.dateFormatter1);
        endLocalDate = LocalDate.parse(endTime, DateFormatters.dateFormatter1);
        if (PublicUtils.judgeChoiceDateRange(startLocalDate, endLocalDate, MAX_QUERY_RANGE, FundFloatProfileLosslistFragemt.this)) {
            mDrawerLayout.toggle();
            queryList();
        }
    }

    /**
     * 起始日期的选择
     */
    private void judgeStartTimeAndSet(LocalDate currentDate) {
        DateTimePicker.showDatePick(mContext, currentDate, DateFormatters.dateFormatter1, new DateTimePicker.DatePickCallBack() {
            @Override
            public void onChoiceDateSet(String strChoiceTime, LocalDate choiceDate) {
                if (ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().isBefore(choiceDate)) {
                    showErrorDialog(getResources().getString(R.string.boc_account_transdetail_start_before));
                    return;
                }
                if (PublicUtils.isCompareDateRange(choiceDate, ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate(), MAX_QUERY_DATE)) {
                    showErrorDialog(getResources().getString(R.string.boc_account_transdetail_start_year_change, PublicUtils.changeNumberToUpper(MAX_QUERY_DATE / 12)));
                    return;
                }
                rightDrawer.setStartDate(strChoiceTime);
            }
        });
    }

    /**
     * 结束日期的选择
     */
    private void judgeEndTimeAndSet(LocalDate currentDate) {
        DateTimePicker.showDatePick(mContext, currentDate, DateFormatters.dateFormatter1, new DateTimePicker.DatePickCallBack() {
            @Override
            public void onChoiceDateSet(String strChoiceTime, LocalDate choiceDate) {
                if (ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().isBefore(choiceDate)) {
                    showErrorDialog(getResources().getString(R.string.boc_account_transdetail_end_before));
                    return;
                }
                if (PublicUtils.isCompareDateRange(choiceDate, ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate(), MAX_QUERY_DATE)) {
                    showErrorDialog(getResources().getString(R.string.boc_account_transdetail_end_year_change, PublicUtils.changeNumberToUpper(MAX_QUERY_DATE / 12)));
                    return;
                }
                rightDrawer.setEndDate(strChoiceTime);
            }
        });
    }

    private boolean isBeyondTimeRange(int length) {
        if (startLocalDate == null || endLocalDate == null) {
            return true;
        }
        if (endLocalDate.plusMonths(-length).plusDays(1).isBefore(startLocalDate)) {
            return true;
        } else {
            return false;
        }
    }

    private void showNodateView() {
//        if (isBeyondTimeRange(MAX_QUERY_DATE)) {
//            queryTitleView.getRightTextButton().setVisibility(View.GONE);
//        }
        listView.setVisibility(View.GONE);
        if (totleView != null) {
            totleView.setVisibility(View.GONE);
        }
        llNoDate.setVisibility(View.VISIBLE);
    }


    private void gotoTotalDetailFragment(){
        FundFloatProfileLossTotalDetailFragment fragment = new FundFloatProfileLossTotalDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(DataUtils.FUND_PROFILE_LOSS_TOTAL_BEAN_KEY, totalBeans);
        bundle.putString(DataUtils.FUND_PROFILE_LOSS_TIME_HINT,queryTitleView.getRightTextButton().getText().toString());
        fragment.setArguments(bundle);
        start(fragment);
    }
}
