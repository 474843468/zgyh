package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttimerangenew.SelectTimeRangeNew;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.model.FundFloatingProfileLossModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.presenter.FundFloatingProfileLossPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui.adapter.FundFloatingProfileLossSingleAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui.bean.FundFloatingProfileLossBean;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui.bean.FundFloatingProfileLossSingleBean;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taoyongzhen on 2016/12/4.
 */

public class FundFloatingProfileLossSingleFragment extends MvpBussFragment<FundFloatingProfileLossConst.Prrsenter>
        implements FundFloatingProfileLossConst.queryFloatingProfitLossView, View.OnClickListener, SelectTimeRangeNew.ClickListener {

    private View rootView;
    /**
     * 侧滑菜单
     */
    private SlipDrawerLayout mDrawerLayout;
    private LinearLayout llyHeader = null; //头部布局
    private TextView tvNodata = null; //无数据提示
    private SelectTimeRangeNew rightDrawer;//侧滑菜单

    private TitleBarView queryTitleView;

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
    private final static int MAX_QUERY_RANGE = 3;
    //最大查询起始日期为一年
    private final static int MAX_QUERY_DATE = 12;

    private ListView listView;
    private FundFloatingProfileLossSingleAdapter adapter;

    private String fundCodeString = "";//由上一级页面传递过来
    private boolean isFirstQuery = true;//首次进入查询失败后，显示无数据页面，应隐藏筛选框；反之，由筛选查询，无数据应显示筛选框
    //保存返回结果中合计数，已不同币种计算
    private List<FundFloatingProfileLossModel.ResultListBean> totalBean = new ArrayList<>();
    private View headView;
    private LinearLayout footView;

    private LinearLayout fundInfo;
    private TextView fundName;
    private TextView fundCode;

    private TextView totalHint;
    private TextView totalAccount;

    private TextView leftTop;
    private TextView leftBottom;
    private TextView rightTop;
    private TextView rightBottom;
    private View devideView;
    private FundFloatingProfileLossModel.ResultListBean resultBean;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_fundfloatprofileloss_single, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        mDrawerLayout = (SlipDrawerLayout) rootView.findViewById(R.id.layoutSlipDrawer);
        llyHeader = (LinearLayout) rootView.findViewById(R.id.llyHeader);
        tvNodata = (TextView) rootView.findViewById(R.id.tvNoData);
        rightDrawer = (SelectTimeRangeNew) rootView.findViewById(R.id.viewSelectTime);
        tvTransferRecordRange = (TextView) rootView.findViewById(R.id.tv_transdetail_range);
        tvTransferRecordSelect = (TextView) rootView.findViewById(R.id.tv_transdetail_select);
        ivTransferRecordSelect = (ImageView) rootView.findViewById(R.id.iv_transdetail_select);
        listView = (ListView) rootView.findViewById(R.id.query_view);
        initQueryTitleView();
    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getArguments();
        if (bundle == null || bundle.containsKey(FundFloatingProfileLossConstant.FUND_CODE_KEY) == false
                || StringUtil.isNullOrEmpty(bundle.getString(FundFloatingProfileLossConstant.FUND_CODE_KEY))){

            listView.setVisibility(View.GONE);
            tvNodata.setText(getString(R.string.boc_fund_profileloss_nodate));
            tvNodata.setVisibility(View.VISIBLE);
            return;
        }

        querySingle();

    }

    @Override
    public void setListener() {
        super.setListener();
        tvTransferRecordSelect.setOnClickListener(this);
        ivTransferRecordSelect.setOnClickListener(this);
        rightDrawer.setListener(this);
    }

    /**
     * 初始化Title
     */
    private void initQueryTitleView() {
        queryTitleView = new TitleBarView(llyHeader.getContext());
        queryTitleView.setStyle(R.style.titlebar_common_white);
        queryTitleView.setTitle(getResources().getString(R.string.boc_fund_profileloss_title));
        queryTitleView.setRightImgBtnVisible(false);
        queryTitleView.setLeftButtonOnClickLinster(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleLeftIconClick();
            }
        });

        llyHeader.addView(queryTitleView);

    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }


    @Override
    protected FundFloatingProfileLossConst.Prrsenter initPresenter() {
        return new FundFloatingProfileLossPresenter(this);
    }

    private void querySingle() {
        showLoadingDialog();
        FundFloatingProfileLossModel model = new FundFloatingProfileLossModel();
        model.setFundCode(fundCodeString);
        model.setStartDate(startLocalDate.format(DateFormatters.dateFormatter1));
        model.setEndDate(endLocalDate.format(DateFormatters.dateFormatter1));
        getPresenter().queryFloatingProfitLoss(model);
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

    /**
     * 筛选按钮的颜色改变
     */
    private void selectStatustChange() {
        tvTransferRecordSelect.setTextColor(getResources().getColor(R.color.boc_text_color_red));
        ivTransferRecordSelect.setImageResource(R.drawable.boc_select_red);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_transdetail_select || v.getId() == R.id.iv_transdetail_select){
            selectStatustChange();
            rightDrawer.setClickSelectDefaultData();
            mDrawerLayout.toggle();
        }
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
        mDrawerLayout.toggle();
    }

    @Override
    public void rightClick(boolean haveSelected, String content) {
        if (haveSelected) {
            tvTransferRecordRange.setText(content + "查询结果");
        } else {
            tvTransferRecordRange.setText("");
        }


        String startTime = rightDrawer.getStartDate();
        String endTime = rightDrawer.getEndDate();
        startLocalDate = LocalDate.parse(startTime, DateFormatters.dateFormatter1);
        endLocalDate = LocalDate.parse(endTime, DateFormatters.dateFormatter1);
        if (PublicUtils.judgeChoiceDateRange(startLocalDate, endLocalDate, MAX_QUERY_RANGE, FundFloatingProfileLossSingleFragment.this)) {
            mDrawerLayout.toggle();
            querySingle();
        }
    }

    @Override
    public void queryFloatingProfitLossFail(BiiResultErrorException exception) {
        closeProgressDialog();
        listView.setVisibility(View.GONE);
        tvNodata.setText(getString(R.string.boc_fund_profileloss_nodate));
        tvNodata.setVisibility(View.VISIBLE);
    }

    @Override
    public void queryFloatingProfitLossSuccess(FundFloatingProfileLossModel model) {
        closeProgressDialog();
        boolean noDate = model == null || model.getResultList() == null
                || model.getResultList().size() <= 0;
        if (noDate) {
            listView.setVisibility(View.GONE);
            tvNodata.setText(getString(R.string.boc_fund_profileloss_nodate));
            tvNodata.setVisibility(View.VISIBLE);
        } else {
            List<FundFloatingProfileLossBean> beans = new ArrayList<>();
            totalBean.clear();
            displayView();
            addHeadView();
            showFootView(true);
        }

    }

    @Override
    public void setPresenter(BasePresenter presenter) {

    }

    private void displayView(){
        //期初市场 买入总金额 卖出净金额 卖出金额 交易费用 总盈亏 期末市值
        List<FundFloatingProfileLossSingleBean> allBeans = new ArrayList<>();
        FundFloatingProfileLossSingleBean startBean = new FundFloatingProfileLossSingleBean();
        startBean.setType(1);
        startBean.setTitleContent("期初市场");
        startBean.setValueContent(MoneyUtils.transMoneyFormat(resultBean.getStartcost(),resultBean.getCurceny()));
        allBeans.add(startBean);

        FundFloatingProfileLossSingleBean buyAllbean = new FundFloatingProfileLossSingleBean();
        buyAllbean.setType(1);
        buyAllbean.setTitleContent("买入总金额");
        buyAllbean.setValueContent(MoneyUtils.transMoneyFormat(resultBean.getTrAmount(),resultBean.getCurceny()));
        allBeans.add(buyAllbean);

        FundFloatingProfileLossSingleBean sellAllbean = new FundFloatingProfileLossSingleBean();
        sellAllbean.setType(1);
        sellAllbean.setTitleContent("卖出净金额");
        sellAllbean.setValueContent(MoneyUtils.transMoneyFormat(resultBean.getTrAmount(),resultBean.getCurceny()));
        allBeans.add(sellAllbean);

        FundFloatingProfileLossSingleBean sellbean = new FundFloatingProfileLossSingleBean();
        sellbean.setType(2);
        sellbean.setTitleContent("卖出金额");
        sellbean.setValueContent(MoneyUtils.transMoneyFormat(resultBean.getTrAmount(),resultBean.getCurceny()));
        allBeans.add(sellbean);

        FundFloatingProfileLossSingleBean tranbean = new FundFloatingProfileLossSingleBean();
        tranbean.setType(2);
        tranbean.setTitleContent("交易费用");
        tranbean.setValueContent(MoneyUtils.transMoneyFormat(resultBean.getJyAmount(),resultBean.getCurceny()));
        allBeans.add(tranbean);

        FundFloatingProfileLossSingleBean allProfileLossbean = new FundFloatingProfileLossSingleBean();
        allProfileLossbean.setType(2);
        allProfileLossbean.setTitleContent("总盈亏");
        allProfileLossbean.setValueContent(MoneyUtils.transMoneyFormat(resultBean.getEndFloat(),resultBean.getCurceny()));
        allBeans.add(allProfileLossbean);

        FundFloatingProfileLossSingleBean allEndbean = new FundFloatingProfileLossSingleBean();
        allEndbean.setType(2);
        allEndbean.setTitleContent("期末市值");
        allEndbean.setValueContent(MoneyUtils.transMoneyFormat(resultBean.getEndCost(),resultBean.getCurceny()));
        allBeans.add(allEndbean);
        adapter.setBeans(allBeans);
        listView.setVisibility(View.VISIBLE);

    }
    private void showFootView(boolean isShow){
        if (footView == null){
            footView = new LinearLayout(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(mContext.getResources().getDimensionPixelSize(R.dimen.boc_space_between_30px),
                    mContext.getResources().getDimensionPixelSize(R.dimen.boc_space_between_20px),
                    mContext.getResources().getDimensionPixelSize(R.dimen.boc_space_between_30px),
                    mContext.getResources().getDimensionPixelSize(R.dimen.boc_space_between_20px));
            footView.setLayoutParams(params);
            TextView textView = new TextView(footView.getContext());
            textView.setTextAppearance(mContext, R.style.tv_small);
            textView.setText(getString(R.string.boc_fund_profileloss_list_hint));
            footView.addView(textView);
        }
        if (isShow){
            if (listView.getFooterViewsCount() <=0 ){
                listView.addFooterView(footView);
            }
            footView.setVisibility(View.VISIBLE);
        }else{
            footView.setVisibility(View.GONE);
        }
    }


    private void addHeadView(){
        if (headView == null){
            headView = View.inflate(mContext,R.layout.boc_fragment_floatingprofilelosssingletotal_head,listView);
            fundInfo = (LinearLayout) headView.findViewById(R.id.fund_info);
            fundInfo.setVisibility(View.GONE);
            totalHint = (TextView) headView.findViewById(R.id.total_head);
            totalAccount = (TextView)headView.findViewById(R.id.total_account);
            rightTop = (TextView)headView.findViewById(R.id.right_top);
            leftTop = (TextView) headView.findViewById(R.id.left_top);
            rightBottom = (TextView)headView.findViewById(R.id.right_bottom);
            leftBottom = (TextView)headView.findViewById(R.id.left_bottom);
            devideView = headView.findViewById(R.id.divide_line);
            devideView.setVisibility(View.VISIBLE);
        }
        String totolProfileLossHint = getString(R.string.boc_fund_profileloss_total_profileloss_hint) + " ";
        totalHint.setText(totolProfileLossHint);

        String totalAccountContent = MoneyUtils.transMoneyFormat("","");
        totalAccount.setText(totalAccountContent);

        leftBottom.setText("已实现盈亏");
        rightBottom.setText("持仓盈亏");

    }


}
