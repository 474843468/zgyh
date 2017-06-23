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
import java.util.Comparator;
import java.util.List;

/**
 * Created by taoyongzhen on 2016/11/30.
 */

public class FundFloatingProfileLossListFragment extends MvpBussFragment<FundFloatingProfileLossConst.Prrsenter>
        implements FundFloatingProfileLossConst.queryFloatingProfitLossView, View.OnClickListener
        , SelectTimeRangeNew.ClickListener,ListView.OnItemClickListener {


    private View rootView;
    /**
     * 侧滑菜单
     */
    private SlipDrawerLayout mDrawerLayout;
    private LinearLayout footView;
    private LinearLayout llyHeader = null; //头部布局
    private TextView tvNodata = null; //无数据提示
    private SelectTimeRangeNew rightDrawer;//侧滑菜单
    private ExpandableItemView totleView;

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
    private FundFloatingProfileLossListAdapter adapter;

    private String fundCode = "";//由上一级页面传递过来
    private boolean isFirstQuery = true;//首次进入查询失败后，显示无数据页面，应隐藏筛选框；反之，由筛选查询，无数据应显示筛选框
    //保存返回结果中合计数，已不同币种计算
    private ArrayList<FundFloatingProfileLossModel.ResultListBean> totalBean = new ArrayList<>();
    private List<FundFloatingProfileLossModel.ResultListBean> singleBeas = new ArrayList<>();
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_fundfloatprofileloss_list, null);
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
        totleView = (ExpandableItemView) rootView.findViewById(R.id.item_view);
        initQueryTitleView();
    }

    @Override
    public void initData() {
        super.initData();
        adapter = new FundFloatingProfileLossListAdapter(mContext);
        listView.setAdapter(adapter);
        totleView.setHeadTextContent(getString(R.string.boc_fund_profileloss_list_totle_head));
        startLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().plusMonths(-MAX_QUERY_RANGE).plusDays(1);
        endLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();
        rightDrawer.setDefaultDate(startLocalDate.format(DateFormatters.dateFormatter1),
                endLocalDate.format(DateFormatters.dateFormatter1));
        tvTransferRecordRange.setText(getResources().getString(R.string.boc_fund_profile_one_year));
        queryList();
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
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
    public void setListener() {
        tvTransferRecordSelect.setOnClickListener(this);
        ivTransferRecordSelect.setOnClickListener(this);
        rightDrawer.setListener(this);
        listView.setOnItemClickListener(this);
        totleView.setOnClickListener(this);

    }

    private void queryList() {
        showLoadingDialog();
        FundFloatingProfileLossModel model = new FundFloatingProfileLossModel();
        model.setFundCode(fundCode);
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
    protected FundFloatingProfileLossConst.Prrsenter initPresenter() {
        return new FundFloatingProfileLossPresenter(this);
    }

    @Override
    public void queryFloatingProfitLossFail(BiiResultErrorException exception) {
        closeProgressDialog();
        totleView.setVisibility(View.GONE);
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
            totleView.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            tvNodata.setText(getString(R.string.boc_fund_profileloss_nodate));
            tvNodata.setVisibility(View.VISIBLE);
        } else {
            List<FundFloatingProfileLossBean> beans = new ArrayList<>();
            totalBean.clear();
            for (FundFloatingProfileLossModel.ResultListBean modelBean : model.getResultList()) {
                if (getString(R.string.boc_fund_profileloss_list_totle_hint).equals(modelBean.getFundCode())){
                    totalBean.add(modelBean);
                    continue;
                }
                FundFloatingProfileLossBean bean = new FundFloatingProfileLossBean();
                String leftContent = modelBean.getFundName();
                if (StringUtil.isNullOrEmpty(modelBean.getFundCode()) == false) {
                    leftContent = leftContent + "（" + modelBean.getFundCode() + ")";
                }
                bean.setLeftContent(leftContent);
                String rightContent = MoneyUtils.transMoneyFormat(modelBean.getTrAmount(), modelBean.getCurceny());
                if (StringUtil.isNullOrEmpty(rightContent) == false) {
                    rightContent += modelBean.getCashFlag();
                } else {
                    rightContent = "";
                }
                bean.setRightFirstContent(rightContent);
                beans.add(bean);
                singleBeas.add(modelBean);
            }
            adapter.setBeans(beans);
            listView.setVisibility(View.VISIBLE);
            showFootView(true);
            initTotleView();
        }

    }
    //合计计算；人民币取和；美元取和；排序待定，暂时依据币种排序
    private void initTotleView(){
        if (totalBean.size() <=0){
            totleView.setVisibility(View.GONE);
            return;
        }
        Collections.sort(totalBean,new beanCompare());
        List<ExpandableItemView.ItemView> itemViews = new ArrayList<>();
        for (FundFloatingProfileLossModel.ResultListBean bean :totalBean){
            String letfContent = MoneyUtils.transMoneyFormat(bean.getTrAmount(), bean.getCurceny());
            String rightContent = DataUtils.getCurrencyDescByLetter(mContext,bean.getCurceny());
            ExpandableItemView.ItemView itemView = new ExpandableItemView.ItemView(letfContent,rightContent);
            itemViews.add(itemView);
        }
        totleView.setItems(itemViews);

    }

    private void showFootView(boolean isShow){
        if (footView == null){
            footView = new LinearLayout(mContext);
            footView.setPadding(mContext.getResources().getDimensionPixelSize(R.dimen.boc_space_between_30px),
                    mContext.getResources().getDimensionPixelSize(R.dimen.boc_space_between_20px),
                    mContext.getResources().getDimensionPixelSize(R.dimen.boc_space_between_30px),
                    mContext.getResources().getDimensionPixelSize(R.dimen.boc_space_between_20px));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
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

    @Override
    public void setPresenter(BasePresenter presenter) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_transdetail_select || v.getId() == R.id.iv_transdetail_select){
            selectStatustChange();
            rightDrawer.setClickSelectDefaultData();
            mDrawerLayout.toggle();
            return;
        }

        if (v.getId() == R.id.item_view){
            FundFloatingProfileLossTotalDetailFragment fragment = new FundFloatingProfileLossTotalDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(FundFloatingProfileLossConstant.FUND_PROFILE_LOSS_TOTAL_BEAN_KEY,totalBean);
            fragment.setArguments(bundle);
            start(fragment);
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
        if (PublicUtils.judgeChoiceDateRange(startLocalDate, endLocalDate, MAX_QUERY_RANGE, FundFloatingProfileLossListFragment.this)) {
            mDrawerLayout.toggle();
            queryList();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FundFloatingProfileLossSingleDetailFragment fragment = new FundFloatingProfileLossSingleDetailFragment();
        fragment.setDetailType(FundFloatingProfileLossConstant.FUND_PROFILE_LOSS_DETAIL_TYPE_SINGLE_TOTAL);
        Bundle bundle = new Bundle();
        bundle.putParcelable(FundFloatingProfileLossConstant.FUND_PROFILE_LOSS_BEAN_KEY,singleBeas.get(position));
        fragment.setArguments(bundle);
        start(fragment);
    }


    static class beanCompare implements Comparator<FundFloatingProfileLossModel.ResultListBean>{

        @Override
        public int compare(FundFloatingProfileLossModel.ResultListBean lhs, FundFloatingProfileLossModel.ResultListBean rhs) {

            if (Integer.valueOf(lhs.getCurceny()) > Integer.valueOf(rhs.getCurceny())){
                return 1;
            }else{
                return -1;
            }
        }
    }
}
